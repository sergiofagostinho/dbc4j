package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractLogic;
import pt.unl.fct.di.dbc4j.ContractMemory;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * Simple queue with contracts written in terms of immutable lists.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class Queue<G> implements Cloneable {

	private Object[] contents;
	private int firstPos;
	private int lastPos;
	private int count;

	private boolean invariant() {
		return
			capacity() >= 1 // capacity is at least one
			&&
			items().count() >= 0 // count never negative
			&&
			items().count() <= capacity() // count never greater than capacity
			&&
			count() == items().count() // count consistent with items
			;
	}

	// creation
	
	/**
	 * Copy constructor. Used in clone().
	 */
	private Queue(Object[] contents, int firstPos, int lastPos, int count) {
		this.contents = contents;
		this.firstPos = firstPos;
		this.lastPos = lastPos;
		this.count = count;
	}
	
	/**
	 * Make an empty queue with the given capacity.
	 * 
	 */
	public Queue(int capacity) {
		contents = new Object[capacity];
		firstPos = 0;
		lastPos = 0;
		count = 0;
	}
	private boolean postQueue(int capacity) {
		return
			capacity() == capacity // capacity set
			&&
			items().count() == 0 // empty
			&&
			(firstPos == 0 && lastPos == 0) // representation set
			;
	}
	
	// representation (queue elements are stored in an array, which is treated as if it were circular)

	/**
	 * The next queue location after location i, treating the array as circular
	 * (the array has locations 0 through 'capacity' - 1).
	 * 
	 * @param i
	 * @return
	 */
	public int nextLocationAfter(int i) {
		if (i < capacity() - 1)
			return i + 1;
		else
			return 0;
	}
	private boolean preNextLocationAfter(int i) {
		return
			i >= 0 // sma
			&&
			i < capacity() // sma
			;
	}
	private boolean postNextLocationAfter(int result, int i) {
		return
			// if not at end then its a plain increment
			ContractLogic.implies(i < capacity() - 1, result == i + 1)
			&&
			// if at end then its back to one
			ContractLogic.implies(i == capacity() - 1, result == 0)
			;
	}
	
	/**
	 * The previous queue location before location i, treating the array as circular
	 * (the array has locations 0 through 'capacity' - 1).
	 * Used by items().
	 * 
	 * @param i
	 * @return
	 */
	private int previousLocationBefore(int i) {
		// TODO this is wrong!
		if (i == 0)
			return capacity() - 1;
		else
			return i - 1;
	}
	// basic queries
	
	/**
	 * Maximum number of items.
	 * 
	 */
	public int capacity() {
		return contents.length;
	}
	
	/**
	 * The items in the queue, in queue order.
	 * 
	 * @return
	 */
	public ImmutableList<G> items() {
		ImmutableList<G> list = new ImmutableList<G>();
		for (int i = 0, n = previousLocationBefore(lastPos); i < count; i++, n = previousLocationBefore(n))
			list = list.precededBy((G)contents[n]);
		return list;
	}
	
	// derived queries
	
	/**
	 * The number of items in the queue.
	 * 
	 */
	public int count() {
		return count;
	}
	
	/**
	 * Does the queue contain no items?
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return count() == 0;
	}
	private boolean postIsEmpty(boolean result) {
		return
			result == (items().count() == 0) // consistent with items count
			;
	}
	
	/**
	 * Does the queue have no room for another item?
	 * 
	 * @return
	 */
	public boolean isFull() {
		return count() == capacity();
	}
	private boolean postIsFull(boolean result) {
		return
			result == (items().count() == capacity()) // consistent with items count and capacity
			;
	}
	
	/**
	 * The first item in the queue.
	 * 
	 * @return
	 */
	public G head() {
		return (G) contents[firstPos];
	}
	private boolean preHead() {
		return
			!isEmpty() // sma
			;
	}
	private boolean postHead(G result) {
		return
			result.equals(items().getHead()) // consistent with items
			;
	}
	
	// other commands
	
	/**
	 * Add 'g' to the queue as the oldest item.
	 * 
	 */
	public void put(G g) {
		contents[lastPos] = g;
		if (lastPos + 1 == capacity())
			lastPos = 0;
		else
			lastPos++;
		count++;
	}
	private boolean prePut() {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			this.count() < this.capacity() // not full
			;
	}
	private boolean postPut(G g) {
		//Queue<G> old = (Queue<G>) ContractMemory.old(this);
		Queue<G> old = (Queue<G>) ContractMemory.old();
		return
			items().count() == old.items().count() + 1
			&&
			items().item(count() - 1).equals(g)
			;
	}
	
	/**
	 * Remove the oldest item from the queue.
	 *
	 */
	public void remove() {
		contents[firstPos] = null;
		if (firstPos >= capacity())
			firstPos = 0;
		else
			firstPos++;
		count--;
	}
	private boolean preRemove() {
		//ContractMemory.remember(this);
		ContractMemory.remember();
		return
			count() > 0 // not empty
			;
	}
	private boolean postRemove() {
		//Queue<G> old = (Queue<G>) ContractMemory.old(this);
		Queue<G> old = (Queue<G>) ContractMemory.old();
		return
			// count decreased
			items().count() == old.items().count() - 1
			&&
			// items left shifted
			items().equals(old.items().tail())
			/*
			 *  forall i in the range 1 to the count of the current items,
			 *       items @ i = old ( items @ (i+1) )
			 */
			;

	}
	
	/**
	 * 
	 */
	public Queue clone() {
		return new Queue<G>(contents.clone(), firstPos, lastPos, count);
	}
	
}
