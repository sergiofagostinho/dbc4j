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

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Stack;

/** 
 * Contract memory aspect. It is responsible for catching the
 * <tt>ContractMemory</tt> class calls and introduce the
 * <i>old</i> mechanism behaviour.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *   
 */
public aspect ContractMemoryAspect {

	/* precedences */
	
	declare precedence: ContractMemoryAspect, DesignByContractAspect;


	/* members */
	
	private static final String CLONE_METHOD = "clone";
	
	private Stack<Hashtable<Integer,Object>> objectStack = 
		new Stack<Hashtable<Integer,Object>>();
	private Stack<Hashtable<String,Object>> valueStack = 
		new Stack<Hashtable<String,Object>>();
	private Stack<Boolean[]> preconditionStack = 
		new Stack<Boolean[]>();
	
	/* pointcuts */
	
	/**
	 * Poincut which captures situations within the a precondition code.
	 */
	pointcut withinPrecondition():
		withincode(public boolean *.pre*(..))
		|| withincode(private boolean *.pre*(..))
		|| withincode(public Boolean *.pre*(..))
		|| withincode(private Boolean *.pre*(..));

	/**
	 * Poincut which captures situations within the a postcondition code.
	 */
	pointcut withinPostcondition():
		withincode(public boolean *.post*(..))
		|| withincode(private boolean *.post*(..))
		|| withincode(public Boolean *.post*(..))
		|| withincode(private Boolean *.post*(..));

	/**
	 * Pointcut which captures ContractMemory.remember() calls within a
	 * precondition code.
	 */
	pointcut rememberCallWithinPrecondition(Object object):
		withinPrecondition()
		&& call(public static void ContractMemory.remember())
		&& this(object);
	
	/**
	 * Pointcut which captures ContractMemory.old() calls within a
	 * postcondition code.
	 */
	pointcut oldCallWithinPostcondition(Object object):
		withinPostcondition()
		&& call(public static Object ContractMemory.old())
		&& this(object);
	
	/**
	 * Pointcut which captures ContractMemory.observe() calls within a
	 * precondition code.
	 */
	pointcut observeCallWithinPrecondition(String tag, Object value):
		withinPrecondition()
		&& call(public static void ContractMemory.observe(String, ..))
		&& args(tag, value);
	
	/**
	 * Pointcut which captures ContractMemory.attribute() calls within a
	 * postcondition code.
	 */
	pointcut attributeCallWithinPostcondition(String tag):
		withinPostcondition()
		&& call(public static Object ContractMemory.attribute(String))
		&& args(tag);

	/**
	 * Pointcut which captures a store preconditions call.
	 */
	pointcut storeCall(Boolean[] arr):
		call(static void storePreconditions(Boolean[]))
		&& args(arr);
	
	/**
	 * Pointcut which captures a retrieve preconditions call.
	 */
	pointcut retrieveCall():
		call(static Boolean[] retrievePreconditions());
	
	
	/* advices */

	/**
	 * Advice that prepare a precondition execution for a static method, 
	 * regarding the remember() calls.
	 * To improve performance, hash tables are only created at the first call
	 * of remember().
	 */
	before():
	DesignByContractAspect.staticMethodExecution() {
		objectStack.push(null);
		valueStack.push(null);
		preconditionStack.push(null);
	}
	
	/**
	 * Advice that prepare a precondition execution for a non-static method, 
	 * regarding the remember() calls.
	 * To improve performance, hash tables are only created at the first call
	 * of remember().
	 */
	before(Object object):
	DesignByContractAspect.nonStaticMethodExecution(object)
	|| DesignByContractAspect.constructorExecution(object) {
		objectStack.push(null);
		valueStack.push(null);
		preconditionStack.push(null);
	}

	/**
	 * Advice that does "garbage collection" at the end of the execution of the
	 * postcondition of a static method.
	 */
	after():
	DesignByContractAspect.staticMethodExecution() {
		objectStack.pop();
		valueStack.pop();
		preconditionStack.pop();
	}

	/**
	 * Advice that does "garbage collection" at the end of the execution of the
	 * postcondition of a non-static method
	 */
	after(Object object):
	DesignByContractAspect.nonStaticMethodExecution(object)
	|| DesignByContractAspect.constructorExecution(object) {
		objectStack.pop();
		valueStack.pop();
		preconditionStack.pop();
	}

	/**
	 * Advice that stores the old state of an object, when the 
	 * remember method is invoked.
	 */
	void around(Object object) throws NonCloneableException:
	rememberCallWithinPrecondition(object) {
		Hashtable<Integer,Object> hashTable = objectStack.pop();
		
		if (hashTable == null)
			hashTable = new Hashtable<Integer,Object>();
		objectStack.push(hashTable);
		
		Class objectClass = object.getClass();
	
		try {
			Method cloneMethod = objectClass.getMethod(CLONE_METHOD, (Class[])null);
			hashTable.put(object.hashCode(), cloneMethod.invoke(object, (Object[])null));
		}
		catch(Exception e) {
			throw new NonCloneableException(objectClass.getName());
		}
	}

	/**
	 * Advice that retrieves the old state of an object, when the old
	 * method is invoked.
	 */
	Object around(Object object):
	oldCallWithinPostcondition(object) {
		Hashtable<Integer,Object> hashTable = objectStack.peek();
		return hashTable.get(object.hashCode());
	}

	/**
	 * Advice that stores the old state of a value, when the 
	 * remember method is invoked.
	 * Since the supplied value is auto-boxed, there is no need to clone it.
	 */
	void around(String tag, Object value):
	observeCallWithinPrecondition(tag, value) {
		Hashtable<String,Object> hashTable = valueStack.pop();
		
		if (hashTable == null)
			hashTable = new Hashtable<String,Object>();
		valueStack.push(hashTable);
		hashTable.put(tag, value);
	}

	/**
	 * Advice that retrieves the old state of an object, when the old
	 * method is invoked.
	 */
	Object around(String tag):
	attributeCallWithinPostcondition(tag) {
		Hashtable<String,Object> hashTable = valueStack.peek();
		return hashTable.get(tag);
	}
	
	/**
	 * Advice that stacks the context information regarding a store call.
	 */
	void around(Boolean[] arr):
	storeCall(arr) {
		preconditionStack.pop();
		preconditionStack.push(arr);		
	}

	/**
	 * Advice that unstacks the context information regarding a store call.
	 */
	Boolean[] around():
	retrieveCall() {
		return preconditionStack.peek();		
	}
}
