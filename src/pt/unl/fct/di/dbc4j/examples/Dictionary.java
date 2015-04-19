package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractLogic;
import pt.unl.fct.di.dbc4j.ContractMemory;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * Dictionaries hold keys. Each key has an associated value.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class Dictionary<KEY,VALUE> implements Cloneable {
	
	private static final int MAX_ELEMS = 100;
	
	/**
	 * Note: Using an Object[], because Java doesn't allow instantiating generic
	 * types.
	 */
	private Object[] keys;
	/**
	 * Note: Using an Object[], because Java doesn't allow instantiating generic
	 * types.
	 */
	private Object[] values;
	private int count;
	
	/**
	 * Copy constructor. For use with clone().
	 * 
	 * @param keys
	 * @param values
	 */
	private Dictionary(Object[] keys, Object[] values, int count) {
		this.keys = keys;
		this.values = values;
		this.count = count;
	}
	
	// Creation commands
	
	/**
	 * Default constructor.
	 *
	 */
	public Dictionary() {
		keys = new Object[MAX_ELEMS];
		values = new Object[MAX_ELEMS];
		count = 0;
	}
	private boolean postDictionary() {
		return
			count() == 0 // dictionary is empty
			;
	}
	
	// Basic queries

	/**
	 * @return The number of keys in the dictionary.
	 */
	public int count() {
		return count;
	}
	
	/**
	 * Does the dictionary contain key 'k'?
	 * 
	 * @param k The given key.
	 * @return True if the dictionary contains the given key.
	 */
	public boolean has(KEY k) {
		for (Object key : keys) {
			if (k.equals(key))
				return true;
		}		
		return false;
	}
	private boolean preHas(KEY k) {
		return
			k != null // key exists
			;		
	}
	private boolean postHas(boolean result) {
		return
			ContractLogic.implies((count() == 0), (!result)) // consistent with count
			;
	}
	
	/**
	 * The value associated with key 'k'.
	 * 
	 * @param k The given key.
	 * @return The associated value with the given key.
	 */
	public VALUE valueFor(KEY k) {
		for (int i = 0; i < count; i++) {
			if (k.equals(keys[i]))
				return (VALUE) values[i];
		}
		return null; // necessary for the compiler
	}
	private boolean preValueFor(KEY k) {
		return
			k != null // key exists
			&&
			has(k) // key in dictionary
			;
	}
	private boolean postValueFor(VALUE result) {
		return
			result != null // sma: just to be sure :)
			;
	}

	// Other commands
	
	/**
	 * Put key 'k' into the dictionary with associated value 'v'.
	 */
	public void put(KEY k, VALUE v) {
		keys[count] = k;
		values[count] = v;
		count++;
	}
	private boolean prePut(KEY k) {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			k != null // key exists
			&&
			!has(k) // key not in dictionary
			;
	}
	private boolean postPut(KEY k, VALUE v) {
		//Dictionary<KEY, VALUE> old = (Dictionary<KEY, VALUE>) ContractMemory.old(this);
		Dictionary<KEY, VALUE> old =
			(Dictionary<KEY, VALUE>) ContractMemory.old();
		return
			count() == old.count() + 1 // count increased
			&&
			has(k) // key in dictionary
			&&
			valueFor(k) == v // value for k is v
			;
	}
	
	/**
	 * Remove key 'k' from the dictionary.
	 * 
	 * @param k
	 */
	public void remove(KEY k) {
		for (int i = 0; i < count; i++) {
			if (k.equals(keys[i])) {
				keys[i] = null;
				values[i] = null;
				count--;
			}				
		}			
	}
	private boolean preRemove(KEY k) {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			k != null // key exists
			&&
			has(k) // key in dictionary
			;
	}
	private boolean postRemove(KEY k) {
		//Dictionary<KEY, VALUE> old = (Dictionary<KEY, VALUE>) ContractMemory.old(this);
		Dictionary<KEY, VALUE> old = (Dictionary<KEY, VALUE>) ContractMemory.old();
		return
			count() == old.count() - 1 // count decreased
			&&
			!has(k) // key not in dictionary
			// (...)
			;
	}
	
	private boolean invariant() {
		return
			count() >= 0 // count never negative
			;
	}
	
	public Dictionary clone() {
		return new Dictionary(keys, values, count);
	}
}
