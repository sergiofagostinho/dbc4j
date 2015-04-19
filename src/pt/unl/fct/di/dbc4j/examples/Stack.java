package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractMemory;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * Simple version of stack, with few features and no protection against
 * overfilling, but with carefully-written contracts.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class Stack<G> implements Cloneable {

	private static final int MAX_ELEMS = 100;
	
	/**
	 * Note: Using an Object[] because Java doesn't allow instantiating generic
	 * types.
	 */
	private Object[] contents;
	private int count;

	public boolean invariant() {
		return
			count() >= 0 // count is never negative
			;
	}

	// creation commands:
	
	/**
	 * Copy constructor. For use with clone().
	 * 
	 */
	private Stack(Object[] contents, int count) {
		this.contents = contents;
		this.count = count;
	}
	
	/**
	 * Initialize the stack to have no items.
	 * 
	 */
	public Stack() {
		contents = new Object[MAX_ELEMS];
		count = 0;
	}
	public boolean postStack() {
		return
			count() == 0 // stack contains zero items
			;
	}
	
	// basic queries:
	
	/**
	 * The number of items in the stack.
	 * 
	 */
	public int count() {
		return count;
	}
	
	/**
	 * The item at position 'i', where position 'count' is the top of the
	 * stack.
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public G itemAt(int i) {
		return (G) contents[i];
	}
	public boolean preItemAt(int i) {
		return
			i >= 0 // i big enough
			&&
			i < count() // i small enough
			;
	}
	
	// derived queries:

	/**
	 * The item on the top of the stack.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public G item() {
		return (G) contents[count - 1];
	}	
	public boolean preItem() {
		return
			count() > 0 // stack not empty
			;
	}
	public boolean postItem(G result) {
		return
			result.equals(itemAt(count()-1)) // consistent with item at
			;
	}
	

	/**
	 * Does the stack contain no items?
	 * 
	 */
	public boolean isEmpty() {
		return count == 0;
	}
	public boolean postIsEmpty(boolean result) {
		return
			result == (count() == 0) // consistent with count
			;
	}

	// other commands:
	
	/**
	 * Push 'g' onto the top of the stack.
	 * 
	 */
	public void put(G g) {
		contents[count] = g;
		count++;
	}
	public boolean prePut() {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return true;
	}
	public boolean postPut(G g) {
		//Stack old = (Stack) ContractMemory.old(this);
		Stack old = (Stack) ContractMemory.old();
		return
			count() == old.count() + 1 // count increased
			&&
			g.equals(item())// g on top
			;
	}
	
	/**
	 * Remove the top item.
	 *
	 */
	public void remove() {
		contents[count-1] = null;
		count--;
	}	
	public boolean preRemove() {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			count() > 0 // stack not empty
			;
	}	
	public boolean postRemove() {
		//Stack old = (Stack) ContractMemory.old(this);
		Stack old = (Stack) ContractMemory.old();
		return
			count() == old.count() - 1 // count decreased
			;
	}

	/**
	 * 
	 */
	public Stack clone() {
		return new Stack(contents, count);
	}	
}
