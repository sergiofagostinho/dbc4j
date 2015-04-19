package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractLogic;
import pt.unl.fct.di.dbc4j.ContractMemory;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * Simple list of names.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class NameList implements Cloneable {

	private static final int NOT_FOUND = -1;

	// representation

	private final int MAX_ELEMS = 666;

	protected String[] contents;
	protected int count;
	
	// creation

	/**
	 * Copy constructor. For use with clone().
	 */
	protected NameList(String[] contents, int count) {
		this.contents = contents;
		this.count = count;
	}
	
	/**
	 * Constructor.
	 * Make a new, empty list.
	 * 
	 */
	public NameList() {
		contents = new String[MAX_ELEMS];
		count = 0;
	}
	@SuppressWarnings("unused")
	private boolean postNameList() {
		return count() == 0;
	}
	
	// implementation support

	/**
	 * Position of 'name' in 'contents' or NOT_FOUND if 'name' is not in 'contents'.
	 * 
	 */
	private int positionOf(String name) {
		for (int i = 0; i < contents.length; i++)
			if (contents[i] != null && contents[i].equals(name))
				return i;
		return NOT_FOUND;
	}
	
	// basic queries

	/**
	 * The number of names in the list.
	 */
	public int count() {
		return count;
	}
	
	/**
	 * Is name in list?
	 * 
	 * @param name
	 * @return
	 */
	public boolean has(String name) {
		return positionOf(name) != NOT_FOUND;
	}
	@SuppressWarnings("unused")
	private boolean preHas(String name) {
		return
			name != null // name can't be null
			;
	}
	@SuppressWarnings("unused")
	private boolean postHas(boolean result) {
		return
			ContractLogic.implies(count() == 0, !result) // result consistent with count
			;
	}
	
	// commands
	
	/**
	 * Add name to list.
	 */
	public void put(String name) {
		for (int i = 0; i < contents.length; i++)
			if (contents[i] == null) {
				contents[i] = name;
				count++;
				return;
			}
		throw new RuntimeException("Maximum capacity exceeded.");
	}	
	@SuppressWarnings("unused")
	private boolean prePut(String name) {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			name != null
			&&
			!has(name) // name not in list
			;
	}
	@SuppressWarnings("unused")
	private boolean postPut(String name) {
		//NameList old = (NameList) ContractMemory.old(this);
		NameList old = (NameList) ContractMemory.old();
		return
			// count increased
			ContractLogic.implies(!old.has(name), count() == old.count() + 1)
			&&
			// name in list
			ContractLogic.implies(!old.has(name), has(name))
			;
	}
	
	/**
	 * Remove name from list.
	 * 
	 * @param name
	 */
	public void remove(String name) {
		contents[positionOf(name)] = null;
		count--;
	}
	@SuppressWarnings("unused")
	private boolean preRemove(String name) {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			name != null
			&&
			has(name) // name in list
			;
	}
	@SuppressWarnings("unused")
	private boolean postRemove(String name) {
		//NameList old = (NameList) ContractMemory.old(this);
		NameList old = (NameList) ContractMemory.old();
		return
			// name not in list
			ContractLogic.implies(old.has(name), !has(name))
			&&
			// count decreased
			ContractLogic.implies(old.has(name), count() == old.count() - 1)
			;
	}
	
	/**
	 * String representation of this object.
	 */
	public String toString() {
		String result = new String();
		result.concat(Integer.toString(count) + " elements: [");
		for (int i = 0; i < contents.length; i++)
			if (contents[i] != null)
				result.concat("'" + contents[i] + "'; ");
		result.concat("]");
		return null;
	}
	
	/**
	 * 
	 */
	public NameList clone() {
		return new NameList(contents.clone(), count);
	}
}
