/*
Copyright (c) 2007 - Sérgio Agostinho

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package pt.unl.fct.di.dbc4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;


/**
 * DesignByContract class. Supports most of the features required by the
 * <tt>DesignByContract</tt> aspect.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
abstract class DesignByContract {

	/* atributes */
	
	private static final String INIT_KEYWORD = "<init>";
	
	private static final int PRE = 0;
	private static final int POST = 1;
	private static final int INVARIANT = 2;

	private static final String[] tokens = { "pre", "post", "invariant" };
	
	/**
	 * Java primitive types and the <tt>void</tt> keyword.
	 */
	private static final String[] primitiveTypes = {
		"boolean", "byte", "short", "int", "long",
		"char", "float", "double", "void"
		};
	
	/**
	 * Java primitive types and <tt>void</tt> keyword classes. 
	 */
	private static final Class[] primitveClasses = {
		boolean.class, byte.class, short.class, int.class, long.class,
		char.class,	float.class, double.class, void.class
		};
	
	/**
	 * See <tt>Java Language specification</tt>, third edition (page 214).
	 */
	private static final String[] methodModifiers =
		{ "public", "protected", "private", "abstract", "static", "final",
		"synchronized", "native", "strictfp" };

	/* public methods */
	
	/**
	 * Verifies if the precondition(s) of a method invocation are valid. See 
	 * the algorithm's activity diagram.
	 * @param joinPoint The joint point.
	 * @param object The object of which the method is to be invoked.
	 * @param objectClass The class of the object.
	 * @param methodType The type of method.
	 * @throws PreconditionException If the precondition fails.
	 * @throws Exception
	 */
	public static Boolean[] verifyPrecondition(JoinPoint joinPoint, Object object,
			Class objectClass, MethodType methodType)
	throws PreconditionException, Exception {
		Object args[] = joinPoint.getArgs();
		Signature signature = joinPoint.getSignature();
		Method assertion;
		
		if (methodType == MethodType.CONSTRUCTOR || methodType == MethodType.STATIC) {
			// no LSP for constructors or static methods
			assertion = getPrecondition(objectClass, signature);
			if (assertion == null || isValidAssertion(assertion, object, objectClass, args))
				return new Boolean[] { true };
			else
				throw new PreconditionException(signature.toString());
		}
		
		Class classHierarchy[] = fetchHierarchy(objectClass);
		Boolean partialValues[] = new Boolean[classHierarchy.length];
		Boolean valid;
		
		// evaluating preconditions bottom-up
		for (int i = 0; i < classHierarchy.length; i++) {
			assertion = getPrecondition(classHierarchy[i], signature);
			if (assertion == null) // non-existent precondition, not the same as 'true'
				partialValues[i] = null;
			else
				partialValues[i] = isValidAssertion(assertion, object, classHierarchy[i], args);
		}
		valid = preconditionDisjunction(partialValues);
		if (!valid)
			throw new PreconditionException(signature.toString());
		return partialValues;
	}

	/**
	 * Verifies if the postcondition(s) of a method invocation are valid. See 
	 * the algorithm's activity diagram.
	 * @param joinPoint The joint point.
	 * @param object The object of which the method is to be invoked.
	 * @param objectClass The class of the object.
	 * @param result The returned value of the method's invocation.
	 * @param methodType <tt>true</tt> if evaluating a postcondition of a constructor.
	 * execution
	 * @param preconditions TODO
	 * @throws PostconditionException If the postcondition fails.
	 * @throws Exception
	 */
	public static void verifyPostcondition(JoinPoint joinPoint, Object object, 
			Class objectClass, Object result, MethodType methodType, Boolean[] preconditions)
	throws PostconditionException, Exception {
		Signature signature = joinPoint.getSignature();
		Class resultClass =	(methodType == MethodType.CONSTRUCTOR)
			? (null) : extractReturnClass(signature.toLongString());
		Method assertion = getPostcondition(objectClass, signature, result, resultClass);
		Vector<Object> args = new Vector<Object>();
		
		// treating constructors and void return methods
		if (methodType != MethodType.CONSTRUCTOR && !resultClass.equals(void.class) 
				&& !resultClass.equals(Void.class))
			args.add(result);
		Object argsTmp[] = joinPoint.getArgs();
		for (int i = 0; i < argsTmp.length; i++)
			args.add(argsTmp[i]);

		if (methodType == MethodType.CONSTRUCTOR || methodType == MethodType.STATIC) {
			// no LSP for constructors or static methods
			assertion = getPostcondition(objectClass, signature, result, resultClass);
			if (assertion == null || isValidAssertion(assertion, object, objectClass, args.toArray()))
				return;
			else
				throw new PostconditionException(signature.toString());			
		}
		
		Class classHierarchy[] = fetchHierarchy(objectClass);
		Boolean partialValues[] = new Boolean[classHierarchy.length];
		Boolean valid;
		
		// evaluating postcondition top-down
		for (int i = classHierarchy.length - 1; i >= 0; i--) {
			assertion = getPostcondition(classHierarchy[i], signature, result, resultClass);
			if (assertion == null) // non-existent postcondition, not the same as 'true'
				partialValues[i] = null;
			else
				partialValues[i] = isValidAssertion(assertion, object, classHierarchy[i], args.toArray());
		}
		//valid = postconditionConjunction(partialValues);
		valid = postconditionConjunction(preconditions, partialValues);
		if (!valid)
			throw new PostconditionException(signature.toString());
	}
	
	/**
	 * 
	 * Verifies if the invariant(s) of a method invocation are valid. See the 
	 * algorithm's activity diagram.
	 * @param joinPoint The joint point.
	 * @param object The object of which the method is to be invoked.
	 * @param objectClass The class of the object.
	 * @throws InvariantException If the invariant fails.
	 * @throws Exception
	 */
	public static void verifyInvariant(JoinPoint joinPoint, Object object, 
			Class objectClass)
	throws InvariantException, Exception {
		Signature signature = joinPoint.getSignature();
		Method assertion = null;
		Boolean valid = null;

		assertion = getAssertion(objectClass, tokens[INVARIANT], (Class[])null);
		
		// Invariant exists?
		if (assertion == null) { // no
			// Get super class
			Class superClass = objectClass.getSuperclass();
			if (superClass != null && !superClass.equals(Object.class)) // ok
				verifyInvariant(joinPoint, object, superClass);
			return;
		}
		// yes
		
		valid = invokeAssertion(assertion, object, objectClass, null);

		// Invariant valid?
		if (valid) { // yes
			// Get super class
			Class superClass = objectClass.getSuperclass();
			if (superClass != null && !superClass.equals(Object.class)) // ok
				verifyInvariant(joinPoint, object, superClass);
			return;
		}
		else // no
			throw new InvariantException(signature.toString());
	}

	/* private methods */
	
	/**
	 * Auxiliar to several methods.
	 * Fetchs the primitive class of a primitive 
	 * type.
	 * @param name The name of the primitive type.
	 * @return The correspondent class, or <tt>null</tt> if there is no such 
	 * primitive type.
	 */
	private static Class typeToClass(String name) {
		for (int i = 0; i < primitiveTypes.length; i++)
			if (name.equals(primitiveTypes[i]))
				return primitveClasses[i];
		
		return null;
	}

	/**
	 * Auxiliar to <tt>verifyPostCondition</tt>.
	 * Determines the type of the 
	 * return type of a method, based on its signature.
	 * @param signature The method signature.
	 * @return The class type.
	 * @throws ClassNotFoundException If the signature refers to a non-existent
	 * class.
	 */
	private static Class extractReturnClass(String signature)
	throws ClassNotFoundException {
		String tokens[] = signature.split(" ");
		String resultClassName = null;
		Class resultClass;
		
		for (String token : tokens) {
			boolean found = true;
			for (String modifier : methodModifiers) {
				if (token.equals(modifier))
					found = false;
			}
			if (found) {
				resultClassName = token;
				break;
			}
		}
		resultClass = typeToClass(resultClassName);
		if (resultClass == null)
			resultClass = ClassLoader.getSystemClassLoader().loadClass(resultClassName);
		if (resultClassName.endsWith("[]")) // dealing with array classes
			resultClassName = resultClassName.substring(0, resultClassName.length()-2);
		return resultClass;
	}
	
	/**
	 * Auxiliar to <tt>verifyPrecondition</tt>.
	 * Fetchs a precondition method.
	 * @param objectClass The class of which the method is part of
	 * @param signature The method signature.
	 * @return The class type.
	 * @throws ClassNotFoundException If the signature refers to a non-existent
	 * class.
	 */
	private static Method getPrecondition(Class objectClass, Signature signature)
	throws ClassNotFoundException {
		String conditionName;
		String[] classNames;
		Vector<Class> classes = new Vector<Class>();
		Method method = null;
		
		conditionName = extractAssertionName(objectClass, signature, PRE);
		classNames = extractClassNames(signature);
		
		method = getAssertion(objectClass, conditionName, classes);
		if (method != null && method.getDeclaringClass().equals(objectClass))
			return method;

		if (classNames.length == 1 && classNames[0].trim().length() == 0)
			return method; // no arguments
		
		for (int i = 0; i < classNames.length; i++) {
			Class tmp = typeToClass(classNames[i]);
			if (tmp != null) {
				classes.add(tmp);
			}
			else {
				if (classNames[i].endsWith("[]")) // dealing with array classes
					classNames[i] = classNames[i].substring(0, classNames[i].length()-2);
				classes.add(ClassLoader.getSystemClassLoader().loadClass(classNames[i]));
			}
			
			method = getAssertion(objectClass, conditionName, classes);
			if (method != null && method.getDeclaringClass().equals(objectClass))
				return method;
		}
		
		return null;
	}

	/**
	 * Auxiliar to <tt>verifyPrecondition</tt>.
	 * Fetchs a postcondition method.
	 * @param objectClass The class of which the method is part of.
	 * @param signature The method signature.
	 * @param result The value returned by the method invocation.
	 * @param resultClass The class type of the returned value.
	 * @return The class type.
	 * @throws ClassNotFoundException If the signature refers to a non-existent
	 * class.
	 */
	private static Method getPostcondition(Class objectClass, 
			Signature signature, Object result, Class resultClass)
	throws ClassNotFoundException {
		String conditionName;
		String[] classNames;
		Vector<String> classNamesVector = new Vector<String>();
		Vector<Class> classes = new Vector<Class>();
		Method assertion = null;
		
		conditionName = extractAssertionName(objectClass, signature, POST);
		classNames = extractClassNames(signature);
		
		assertion = getAssertion(objectClass, conditionName, classes);
		if (assertion != null && assertion.getDeclaringClass().equals(objectClass))
			return assertion;

		if (classNames.length == 1 && classNames[0].trim().length() == 0)
			return assertion; // no arguments

		if (resultClass != null && !resultClass.equals(void.class)
				&& !resultClass.equals(Void.class)) {
			classNamesVector.add(resultClass.getName());
			classes.add(resultClass);
		}

		for (int i = 0; i < classNames.length; i++)
			classNamesVector.add(classNames[i]);

		assertion = getAssertion(objectClass, conditionName, classes);
		if (assertion != null && assertion.getDeclaringClass().equals(objectClass))
			return assertion;
		
		for (int i = classes.size(); i < classNamesVector.size(); i++) {
			Class tmp = typeToClass(classNamesVector.get(i));
			if (tmp != null) {
				classes.add(tmp);
			}
			else {
				if (classNamesVector.get(i).endsWith("[]")) // dealing with array classes
					classNamesVector.set(i,classNamesVector.get(i).substring(0, classNamesVector.get(i).length()-2));
				classes.add(ClassLoader.getSystemClassLoader().loadClass(classNamesVector.get(i)));
			}
			
			assertion = getAssertion(objectClass, conditionName, classes);
			if (assertion != null && assertion.getDeclaringClass().equals(objectClass))
				return assertion;
		}
		
		return null;
	}
	
	/**
	 * Auxiliar method for <tt>getPrecondition</tt> and 
	 * <tt>getPostcondition</tt>.
	 * @param objectClass
	 * @param signature
	 * @param type The assertion type (<tt>PRE</tt> or <tt>POST</tt>)
	 * @return The assertion name.
	 */
	private static String extractAssertionName(Class objectClass, 
			Signature signature, int type) {
		String assertionName = tokens[type];
		if (signature.getName().equals(INIT_KEYWORD)) {
			assertionName += objectClass.getSimpleName();
		}
		else {
			String tmp = signature.getName();
			// passing the method name to lowerCamelCase:
			assertionName += new String(new char[]{tmp.charAt(0)}).toUpperCase()
				+ tmp.substring(1);
		}
		return assertionName;
	}

	/**
	 * Auxiliar method for <tt>getPrecondition</tt> and 
	 * <tt>getPostcondition</tt>.
	 * @param signature The singnature of the method to be analyzed.
	 * @return The class names.
	 */
	private static String[] extractClassNames(Signature signature) {
		String arguments;
		String[] classNames;
		arguments = signature.toLongString();
		arguments = arguments.substring(arguments.indexOf('(')+1,arguments.length()-1);
		classNames = arguments.split(", ");
		return classNames;
	}
	
	/**
	 * Auxiliar method for <tt>getPrecondition</tt> and 
	 * <tt>getPostcondition</tt>. 
	 * @param objectClass The object class.
	 * @param assertionName The name of the condition.
	 * @param classes The classes of the arguments.
	 * @return The method of <tt>null</tt> if it does not exist.
	 */
	private static Method getAssertion(Class objectClass, String assertionName, 
			Class[] classes) {
		Method method = null;
		try {
			method = objectClass.getDeclaredMethod(assertionName, classes);
		}
		catch(NoSuchMethodException e) {
			/* nothing to do */
		}
		return method;
	}
	private static Method getAssertion(Class objectClass, String assertionName, 
			Vector<Class> classes) {
		return getAssertion(objectClass, assertionName, classes.toArray(new Class[0]));
	}


	/**
	 * Auxiliar to <tt>verifyPrecondition</tt> and 
	 * <tt>verifyPostcondition</tt>.
	 * Determines whether a condition method returns a <tt>true</tt> value or 
	 * not.
	 * @param assertion The condition method to be invoked
	 * @param object The object on which the method is to be invoked
	 * @param objectClass The class of the object
	 * @param args The arguments to be passed to the condition method
	 * @return The return value of the method invocation
	 * @throws Exception In case of an anomalous situation.
	 */
	private static boolean isValidAssertion(Method assertion, Object object, 
			Class objectClass, Object[] args)
	throws Exception {
		Boolean valid;
		Vector<Object> tmpArgs = new Vector<Object>();
		
		valid = invokeAssertion(assertion, object, objectClass, (Object[])null);
		if (valid != null)
			return valid;
			
		for (int i = 0; i < args.length; i++) {
			tmpArgs.add(args[i]);
			valid = invokeAssertion(assertion, object, objectClass, tmpArgs.toArray(new Object[0]));
			if (valid != null)
				return valid;
		}
		throw new Exception("Internal error in DesignByContract.isValidCondition()");
	}

	/**
	 * Auxiliary method for <tt>isValidCondition</tt>.
	 * @param assertion The method to be invoked.
	 * @param object The object in which the method is invoked (null if it is a
	 * static method).
	 * @param objectClass The class of which the method is part of.
	 * @param arr An array with the arguments to be passed to the invoked method.
	 * @return The return value of the invoked method.
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static Boolean invokeAssertion(Method assertion, Object object, 
			Class objectClass, Object[] arr)
	throws IllegalAccessException, InvocationTargetException {
		Boolean valid = null;
		try {
			int mod = assertion.getModifiers();
			// private, static or no inheritance contract: no polymorphism
			if (Modifier.isPrivate(mod) || Modifier.isStatic(mod)
					|| object.getClass().equals(objectClass)) {
				// warning: disabling Java Access Protection!
				assertion.setAccessible(true);
				if (object == null) // static
					valid = ((Boolean) assertion.invoke(null, arr));
				else // non-static
					valid = ((Boolean) assertion.invoke(objectClass.cast(object), arr));			
			}
			else
				// non-static public contract with inheritance: must use a copy constructor
				if (Modifier.isPublic(mod) && !object.getClass().equals(objectClass)) {
					Class sig[] = { objectClass };
					Constructor copy = objectClass.getConstructor(sig);
					Object objectCopy = copy.newInstance(object);
					valid = ((Boolean) assertion.invoke(objectCopy, arr));				
				}
				else
					throw new InvalidVisibilityException(assertion.toString());			
		}
		catch(IllegalArgumentException e) {
			// nothing to do
		}
		catch(NoSuchMethodException e) {
			throw new CopyConstructorException(objectClass.toString());
		}
		catch(InstantiationException e) {
			throw new CopyConstructorException(objectClass.toString());
		}
		return valid;
	}
	
	/**
	 * Fetchs a class hierarchy of a given class (with the exception of the 
	 * <tt>Object</tt> class.
	 * @param startClass The starting point of the class hierachy.
	 * @return An array with the classes of the class hierarcy (bottom-up).
	 */
	private static Class[] fetchHierarchy(Class startClass) {
		Vector<Class> result = new Vector<Class>();
		Class aClass;
		
		if (startClass.equals(Object.class))
			return new Class[]{};
		
		result.add(startClass);
		aClass = startClass;
		
		while (!(aClass = aClass.getSuperclass()).equals(Object.class))
		{
			result.add(aClass);
		}
		return result.toArray(new Class[]{});
	}
	
	/**
	 * Performs the logical disjunction evaluation of preconditions needed for
	 * obtaining contract <i>contravariance</i>. 
	 * @param arr The partial preconditions evaluation, class type bottom-up.
	 * @return The composed precondition evaluation.
	 */
	private static boolean preconditionDisjunction(Boolean[] arr) {
		boolean empty = true;
		boolean valid = false;
		
		for (Boolean elem: arr) {
			if (elem != null) {
				empty = false;
				if (elem)
					valid = true;
			}
		}
		return valid || empty;
	}
	
	/**
	 * Performs the logical conjunction evaluation of postconditions needed for
	 * obtaining contract <i>covariance</i>.
	 * This follows the Ecma Eiffel standard.
	 * @param pre The partial preconditions evaluation, class type bottom-up.
	 * @param post The partial postconditions evaluation, class type top-down.
	 * @return The composed postcondition evaluation.
	 */
	private static boolean postconditionConjunction(Boolean[] pre, Boolean[] post) {
		boolean empty = true;
		boolean valid = true;
		
		Boolean foo, bar;
		for (int i = 0; i < post.length; i++) {
			foo = pre[i];
			bar = post[i];
			if (bar != null) {
				empty = false;
				if ( foo != null && !ContractLogic.implies(foo, bar)
						|| foo == null && !bar )
					valid = false;
			}
		}
		return valid || empty;
	}

	/**
	 * Performs the logical conjunction evaluation of postconditions needed for
	 * obtaining contract <i>covariance</i>.
	 * @deprecated This follows the Eiffel original algorithm.
	 * @param arr The partial postconditions evaluation, class type top-down.
	 * @return The composed postcondition evaluation.
	 */
	private static boolean postconditionConjunction(Boolean[] arr) {
		boolean empty = true;
		boolean valid = true;
		
		for (Boolean elem: arr) {
			if (elem != null) {
				empty = false;
				if (!elem)
					valid = false;
			}
		}
		return valid || empty;
	}
}
