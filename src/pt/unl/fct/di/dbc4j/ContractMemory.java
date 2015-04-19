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


/**
 * Class for memorizing the <tt>old</tt> state of an object or member.
 * This class is a stub, requiring <tt>ContractMemoryAspect</tt> advice to run.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class ContractMemory {

	/**
	 * Stores an object state into memory.
	 * PRE: object has not been memorized
	 * POST: object was memorized
	 * 
	 */
	public static void remember() {
		// nothing to do
	}
	
	/**
	 * Retrieves an object state from memory and deletes it.
	 * PRE: object was memorized
	 * 
	 * @return The "old" object state
	 */
	public static Object old() {
		// nothing to do
		return null;
	}
	
	/**
	 * Stores the value of a <tt>byte</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, byte value) {
		// nothing to do
	}

	/**
	 * Stores the value of a <tt>short</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, short value) {
		// nothing to do
	}
	
	/**
	 * Stores the value of an <tt>int</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, int value) {
		// nothing to do
	}

	/**
	 * Stores the value of an <tt>long</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, long value) {
		// nothing to do
	}

	/**
	 * Stores the value of an <tt>char</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, char value) {
		// nothing to do
	}

	/**
	 * Stores the value of an <tt>float</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, float value) {
		// nothing to do
	}

	/**
	 * Stores the value of an <tt>double</tt> value.
	 * PRE: value has not been memorized
	 * POST: value was memorized
	 * 
	 * @param tag
	 * @param value
	 */
	public static void observe(String tag, double value) {
		// nothing to do
	}

	/**
	 * Retrieves a primitive type value through a wrapper class.
	 * PRE: value was memorized
	 * 
	 * @param tag
	 * @return A wrapper class with the "old" value.
	 */
	public static Object attribute(String tag) {
		// nothing to do
		return null;
	}
	
	
	/**
	 * Stores precondition evaluation results.
	 * This is necessary to implemented the Liskov Substitution Principle
	 * as defined in the Ecma Eiffel standard.
	 */
	static void storePreconditions(Boolean[] preconditions) {
		// nothing to do
	}
	
	/**
	 * Retrieves precondition evaluation results.
	 * This is necessary to implemented the Liskov Substitution Principle
	 * as defined in the Ecma Eiffel standard.
	 */
	static Boolean[] retrievePreconditions() {
		// nothing to do
		return null;
	}

}
