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

import java.util.Hashtable;


/**
 * Class for representing contract rules.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class Rules implements Cloneable {
	
	private static final boolean PLACEBO = true;
	
	private CheckLevel defaultAssertion;
	private Hashtable<String,CheckLevel> checks;
	private Hashtable<String,Boolean> exposed;
	
	/**
	 * Default constructor. Creates a rules definition, with default checks
	 * set to <tt>ALL</tt>, and no class-specific checks.
	 *
	 */
	public Rules() {
		this(CheckLevel.ALL, new Hashtable<String,CheckLevel>(), 
			new Hashtable<String,Boolean>());
	}
	
	/**
	 * Constructor which provides all the class members.
	 * 
	 * @param defaultAssertion The default assertion run-time check level.
	 * @param assertions An hash table of check levels for specific classes.
	 * @param exposed An hash table of exposed classes.
	 */
	private Rules(CheckLevel defaultAssertion, 
			Hashtable<String,CheckLevel> assertions, 
			Hashtable<String,Boolean> exposed) {
		this.defaultAssertion = defaultAssertion;
		this.checks = assertions;
		this.exposed = exposed;
	}
	
	/**
	 * Adds an assertion for the given class.
	 * PRE: !hasClass(className)
	 * POST: hasClass(className)
	 * 
	 * @param className The class name
	 * @param assertion The assertion check level.
	 */
	public void addClass(String className, CheckLevel assertion) {
		checks.put(className, assertion);
	}
	
	/**
	 * Removes the assertion of the given class.
	 * PRE: hasClass(className)
	 * POST: return != null && !hasClass(className)
	 * 
	 * @param className The class name.
	 * @return The <tt>CheckLevel</tt> of the removed class.
	 */
	public CheckLevel removeClass(String className) {
		return checks.remove(className);
	}
	
	/**
	 * Verifies if a given class in under assertion.
	 * @param className The class name.
	 * @return True if the given class is under specific assertion.
	 */
	public boolean hasClass(String className) {
		return checks.containsKey(className);
	}
	
	/**
	 * Gets the <tt>CheckLevel</tt> for a given class.
	 * PRE: hasAssertion(className)
	 * POST: return != null
	 * 
	 * @param className The class name.
	 * @return The <tt>CheckLevel</tt> of the queried class.
	 */
	public CheckLevel getCheck(String className) {
		return checks.get(className);
	}
		
	/**
	 * Gets the default <tt>CheckLevel</tt> for classes.
	 * 
	 * @return The default <tt>CheckLevel</tt> for classes.
	 */
	public CheckLevel getDefaultAssertion() {
		return defaultAssertion;
	}

	/**
	 * Sets the default <tt>CheckLevel</tt> for classes.
	 * 
	 * @param defaultAssertion The default <tt>CheckLevel</tt> for classes.
	 */
	public void setDefaultAssertion(CheckLevel defaultAssertion) {
		this.defaultAssertion = defaultAssertion;
	}
	
	/**
	 * Exposes a specific class. Until un-exposed, the class' assertions
	 * will not be evaluated.
	 * 
	 * @param className The class name.
	 */
	public void expose(String className) {
		exposed.put(className, PLACEBO);
	}
	
	/**
	 * Exposes a specific class. Until un-exposed, the class' assertions
	 * will not be evaluated.
	 * 
	 * @param theClass The class type.
	 */
	public void expose(Class theClass) {
		expose(theClass.getName());
	}
	
	/**
	 * Un-exposes a specific class.
	 * 
	 * @param className The class name.
	 */
	public void unexpose(String className) {
		exposed.remove(className);
	}

	/**
	 * Un-exposes a specific class.
	 * 
	 * @param theClass The class type.
	 */
	public void unexpose(Class theClass) {
		unexpose(theClass.getName());
	}

	/**
	 * Is the class exposed?
	 * @param className
	 * @return
	 */
	public boolean isExposed(String className) {
		return exposed.containsKey(className);
	}
	
	/**
	 * @return A clone of this object.
	 */
	@SuppressWarnings("unchecked")
	public Object clone() {
		return
			new Rules(defaultAssertion, 
					(Hashtable<String,CheckLevel>) checks.clone(),
					(Hashtable<String,Boolean>) exposed.clone());
	}
	
	/**
	 * @return A string representation of this class
	 */
	public String toString() {
		return
			"default assertion: " + defaultAssertion + 
			"\nassertions: " + checks + "\nexposed: " + exposed;
	}

}
