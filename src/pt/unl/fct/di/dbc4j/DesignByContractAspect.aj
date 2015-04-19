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

import java.io.IOException;

/** Design by Contract aspect. Verifies pre/postconditions and invariants during
 * the execution of methods on any class.
 * Method preconditions are identified by the <i>pre</i> prefix, method
 * postconditions are identified by the <i>post</i> prefix and class invariants
 * by the <i>invariant</i> name. These conventions are the same as in
 * <tt>Contract4J</tt> <i>ContractBeans</i> format
 * (<url>http://www.contract4j.org</url>).
 * Supports method/condition overloading and overriding.
 * Also supports using only a subset of the method's arguments for
 * pre/postconditions, but its behaviour is undefined in conjunction
 * with ambiguous overloading.
 * Supports the old mechanism through the <tt>ContractMemory</tt> static class.
 * Supports static configuration through the <tt>rules.properties</tt> file, 
 * and run-time configuration through the <tt>ContractRules</tt> static class. 
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public aspect DesignByContractAspect {

	/* members */
	
	private static Rules rules;
	private static final String RULES_FILENAME = "rules.properties";

	/* constructor */
	
	/**
	 * Aspect constructor. Attempts to load a rules file, or if not available,
	 * uses the default settings.
	 */
	DesignByContractAspect() {
		// synchronized to avoid advicing before loading configuration
		synchronized (this) {
			try {
				rules = RulesLoader.load(RULES_FILENAME);
			}
			catch(IOException e) {
				rules = new Rules();
			}
		}
	}
	
	/* method */
	
	/**
	 * Returns the <i>singleton</i> instance of the Rules class.
	 */
	public static Rules getRules() {
		return rules;
	}

	/**
	 * Given a class name, returns the the run-time check level configured for
	 * that class.
	 */
	private CheckLevel fetchLevel(String className) {
		if (rules.hasClass(className))
			return rules.getCheck(className);
		else
			return rules.getDefaultAssertion();		
	}

	/**
	 * Are preconditions enabled for this specific class?
	 */
	private boolean isPreEnabled(String className) {
		CheckLevel level = fetchLevel(className);
		return
			!rules.isExposed(className)
			&& (level.equals(CheckLevel.ALL)
			|| level.equals(CheckLevel.POST)
			|| level.equals(CheckLevel.PRE));		
	}

	/**
	 * Are postconditions enabled for this specific class?
	 */
	private boolean isPostEnabled(String className) {
		CheckLevel level = fetchLevel(className);
		return
			!rules.isExposed(className)
			&& (level.equals(CheckLevel.ALL)
			|| level.equals(CheckLevel.POST));		
	}

	/**
	 * Are invariants enabled for this specific class?
	 */
	private boolean isInvariantEnabled(String className) {
		CheckLevel level = fetchLevel(className);
		return
			!rules.isExposed(className)
			&& level.equals(CheckLevel.ALL);
	}

	/* pointcuts */
	
	/**
	 * Pointcut which captures the execution of pre/postconditions and
	 * invariants, directly or indirectly.
	 * (These will be ignored in order to avoid infinite recursion.)
	 */
	pointcut assertionExecution():	
		 cflow(execution(private boolean *.pre*(..)))
		|| cflow(execution(private boolean *.post*(..)))
		|| cflow(execution(private boolean *.invariant()))
		|| cflow(execution(public boolean *.pre*(..)))
		|| cflow(execution(public boolean *.post*(..)))
		|| cflow(execution(public boolean *.invariant()))
		|| cflow(execution(private Boolean *.pre*(..)))
		|| cflow(execution(private Boolean *.post*(..)))
		|| cflow(execution(private Boolean *.invariant()))
		|| cflow(execution(public Boolean *.pre*(..)))
		|| cflow(execution(public Boolean *.post*(..)))
		|| cflow(execution(public Boolean *.invariant()));

	
	/**
	 * Pointcut which captures method execution in J2SE/AJ system packages.
	 * (These will be ignored for performance reasons.)
	 */
	pointcut systemPackageExecution():
		execution(java.* *(..))
		|| execution(javax.* *(..))
		|| execution(sun.reflect.* *(..))
		|| execution(org.aspectj.* *(..))
		|| execution(org.omg.* *(..))
		|| execution(org.w3c.* *(..))
		|| execution(org.xml.* *(..));
	
	/**
	 * Pointcut which captures all the execution of methods inherited or
	 * redefined from <tt>Object</tt>.
	 * (These will be ignored, for these methods are inherent to the Java
	 * language usage.)
	 */
	pointcut objectMethodExecution():
		execution(* clone())
		|| execution(* equals(..))
		|| execution(* finalize())
		|| execution(* getClass())
		|| execution(* hashCode())
		|| execution(* notify())
		|| execution(* notifyAll())
		|| execution(* toString())
		|| execution(* wait(..));
	
	/**
	 * Pointcut which captures all the situations to be excluded, including
	 * advice execution.
	 */
	pointcut excludedSituation():
		assertionExecution()
		|| systemPackageExecution()
		|| objectMethodExecution()
		|| execution(public static void main(..))
		|| within(pt.unl.fct.di.dbc4j.*)
		|| adviceexecution(); 
	
	/**
	 * Pointcut which captures all non-private, non-static method executions.
	 */
	public pointcut nonStaticMethodExecution(Object object):
		target(object)
		&& execution(!private !static * *.*(..))
		&& !excludedSituation();
	
	/**
	 * Pointcut which captures all non-private constructor executions.
	 */
	public pointcut constructorExecution(Object object):
		target(object)
		&& execution(!private *.new(..))
		&& !excludedSituation();
	
	/**
	 * Pointcut which captures all non-private static method executions.
	 */
	public pointcut staticMethodExecution():
		execution(!private static * *.*(..))
		&& !excludedSituation();
	
	/* advices */

	/**
	 * Advice that tests the invariant(s) and/or precondition(s) before the
	 * execution of a non-static, non-private method.
	 * @throws ContractException If the invariant(s) or precondition(s)
	 * do not hold.
	 */
	before(Object object) throws ContractException:
		nonStaticMethodExecution(object) {
		try {
			Class objectClass = thisJoinPoint.getTarget().getClass();
			
			if (isInvariantEnabled(objectClass.getName()))
				DesignByContract.verifyInvariant(thisJoinPoint, object,	objectClass);
			
			if (isPreEnabled(objectClass.getName())) {
				Boolean[] preconditions =
					DesignByContract.verifyPrecondition(thisJoinPoint, object, objectClass, MethodType.NORMAL);
				ContractMemory.storePreconditions(preconditions);
			}
		}
		catch (ContractException c) {
			throw c;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Advice that tests the precondition(s) before the execution of a
	 * non-private constructor.
	 * @throws PreconditionException If the precondition(s) do not hold.
	 */
	before(Object object) throws PreconditionException:
	constructorExecution(object) {
		try {
			Class objectClass = thisJoinPoint.getTarget().getClass();

			if (isPreEnabled(objectClass.getName())) {
				Boolean[] preconditions =
					DesignByContract.verifyPrecondition(thisJoinPoint, object, objectClass, MethodType.CONSTRUCTOR);
				ContractMemory.storePreconditions(preconditions);
			}
		}
		catch (PreconditionException c) {
			throw c;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Advice that tests the precondition(s) before the execution of a static
	 * non-private method.
	 * @throws PreconditionException If the precondition(s) do not hold.
	 */
	before() throws PreconditionException:
	staticMethodExecution() {
		try {
			Class staticClass = 
				thisJoinPoint.getStaticPart().getSourceLocation().getWithinType();

			if (isPreEnabled(staticClass.getName())) {
				Boolean[] preconditions =			
					DesignByContract.verifyPrecondition(thisJoinPoint, null, staticClass, MethodType.STATIC);
				ContractMemory.storePreconditions(preconditions);
			}
		}
		catch (PreconditionException c) {
			throw c;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Advice that tests the invariant(s) and/or postcondition(s) after the
	 * execution of a non-private constructor.
	 * @throws ContractException If the invariant(s) or postcondition(s)
	 * do not hold.
	 */
	after(Object object) throws ContractException:
	constructorExecution(object) {
		try {
			Class objectClass = thisJoinPoint.getTarget().getClass();

			if (isInvariantEnabled(objectClass.getName()))
				DesignByContract.verifyInvariant(thisJoinPoint, object,	objectClass);

			if (isPostEnabled(objectClass.getName())) {
				Boolean[] preconditions = ContractMemory.retrievePreconditions();
				DesignByContract.verifyPostcondition(thisJoinPoint, object,	objectClass, null, MethodType.CONSTRUCTOR, preconditions);				
			}
		}
		catch (ContractException c) {
			throw c;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Advice that tests the invariant(s) and/or postcondition(s) after the
	 * execution of a non-static, non-private method.
	 * @throws ContractException If the invariant(s) or postcondition(s)
	 * do not hold.
	 */
	after(Object object) returning (Object result) throws ContractException:
	nonStaticMethodExecution(object) {
		try {			
			Class objectClass = thisJoinPoint.getTarget().getClass();
			
			if (isInvariantEnabled(objectClass.getName()))
				DesignByContract.verifyInvariant(thisJoinPoint, object,	objectClass);
			
			if (isPostEnabled(objectClass.getName())) {
				Boolean[] preconditions = ContractMemory.retrievePreconditions();
				DesignByContract.verifyPostcondition(thisJoinPoint, object,	objectClass, result, MethodType.NORMAL, preconditions);
			}
		}
		catch (ContractException c) {
			throw c;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Advice that tests the postcondition(s) after the execution of a 
	 * static non-private method.
	 * @throws PostconditionException If the postcondition(s) do not hold.
	 */
	after() returning (Object result) throws PostconditionException:
	staticMethodExecution() {
		try {
			Class staticClass = 
				thisJoinPoint.getStaticPart().getSourceLocation().getWithinType();

			if (isPostEnabled(staticClass.getName())) {
				Boolean[] preconditions = ContractMemory.retrievePreconditions();
				DesignByContract.verifyPostcondition(thisJoinPoint, null, staticClass, result, MethodType.STATIC, preconditions);
			}
		}
		catch (PostconditionException c) {
			throw c;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
