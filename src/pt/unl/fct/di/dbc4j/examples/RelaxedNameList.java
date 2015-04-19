package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractLogic;
import pt.unl.fct.di.dbc4j.ContractMemory;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * Simple list of names (relaxed preconditions on put and remove).
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class RelaxedNameList extends NameList {

	/**
	 * Copy constructor. For use with clone().
	 * 
	 */
	private RelaxedNameList(String[] contents, int count) {
		super(contents, count);
	}

	/**
	 * Constructor.
	 * Make a new, empty list.
	 * 
	 */
	public RelaxedNameList() {
		super();
	}
	
	
	// commands
	
	/**
	 * Add name to list.
	 * 
	 */
	public void put(String name) {
		if (!has(name))
			super.put(name);
	}
	@SuppressWarnings("unused")
	private boolean prePut(String name) {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			name != null
			&&
			has(name) // name in list
			;
	}
	@SuppressWarnings("unused")
	private boolean postPut(String name) {
		//RelaxedNameList old = (RelaxedNameList) ContractMemory.old(this);
		RelaxedNameList old = (RelaxedNameList) ContractMemory.old();
		return
			// count unchanged if name was already in list
			ContractLogic.implies(old.has(name), count() == old.count())
			;
	}
	
	/**
	 * Remove name from list.
	 * 
	 */
	public void remove(String name) {
		if (has(name))
			super.remove(name);
	}
	@SuppressWarnings("unused")
	private boolean preRemove(String name) {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			name != null
			&&
			!has(name) // name not in list
			;
	}
	@SuppressWarnings("unused")
	private boolean postRemove(String name) {
		//RelaxedNameList old = (RelaxedNameList) ContractMemory.old(this);
		RelaxedNameList old = (RelaxedNameList) ContractMemory.old();
		return
			// count unchanged if name was not in list
			ContractLogic.implies(!old.has(name), count() == old.count())
			;
	}

	/**
	 * 
	 */
	public RelaxedNameList clone() {
		return new RelaxedNameList(contents.clone(), count);
	}
}
