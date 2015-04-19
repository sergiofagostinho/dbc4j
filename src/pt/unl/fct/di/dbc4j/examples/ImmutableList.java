package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractLogic;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * Lists have a head and a tail. Lists are composed of cells.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class ImmutableList<G> {

	private ImmutableCell<G> head;
	private int count;
	
	// creation
	
	/**
	 * Default constructor.
	 * Make a new empty list.
	 *
	 */
	public ImmutableList() {
		head = null;
		count = 0;
	}
	private boolean postImmutableList() {
		return
			isEmpty() // is empty
			;
	}

	/**
	 * Initialize 'Current' to hold the list of cells beginning with 'cell' (which can be Void).
	 * 'a_count' should be the number of cells in the list of cells beginning with 'a_cell'.
	 * 			
	 * @param head
	 * @param count
	 */
	public void setHead(ImmutableCell<G> head, int count) {
		this.head = head;
		this.count = count;
	}
	private boolean postSetHead(ImmutableCell<G> head, int count) {
		return
			(
				head == null && getHead() == null
					||
				head != null && getHead().equals(head.getItem())
			)
			&&
			count() == count
			;
	}
	
	// basic queries

	/**
	 * Does the list contain no items?
	 * 
	 */
	public boolean isEmpty() {
		return count == 0;
	}
	
	/**
	 * The first element in the list.
	 * 
	 * @return
	 */
	public G getHead() {
		return head.getItem();
	}
	private boolean preGetHead() {
		return
			!isEmpty() // not is empty
			;
	}
	private boolean postGetHead(G result) {
		return
			result != null // result can't be null
			;
	}
	
	/**
	 * A new list formed from 'Current' minus its 'head'.
	 * 
	 * @return
	 */
	public ImmutableList<G> tail() {
		ImmutableList<G> tail = new ImmutableList<G>();
		tail.setHead(head.getRight(), count - 1);
		return tail;
	}
	private boolean preTail()
	 {
		return
			!isEmpty() // not is empty
			;
	}
	
	// derived queries
	
	/**
	 * The number of items in the list.
	 *
	 */
	public int count() {
		return count;
	}
	private boolean postGetCount(int result) {
		return
			// is empty means count is zero
			ContractLogic.implies(isEmpty(), result == 0)
			&&
			// else count is one greater than count of tail
			ContractLogic.implies(!isEmpty(), result == 1 + tail().count())
			;
	}
	
	/**
	 * A new list formed from Current with 'g' added at the head.
	 * 
	 * @param g
	 * @return
	 */
	public ImmutableList<G> precededBy(G g) {		
		ImmutableCell<G> newHead = new ImmutableCell<G>(g, head);
		ImmutableList<G> newList = new ImmutableList<G>();
		newList.setHead(newHead, count()+1);
		return newList;
	}
	private boolean postPrecededBy(ImmutableList<G> result, G g) {
		return
			result.getHead() == g // head is g
			&&
			result.tail().equals(this) // tail is original list
			&&
			!result.isEmpty() // not empty
			;
	}
	
	/**
	 * Does this contain the same items as 'other', in the same order?
	 * 
	 * @param other Other list under comparison.
	 * @return
	 */
	public boolean equals(ImmutableList<G> other) {
		return
			(isEmpty() && other.isEmpty())
			||
			(
				!isEmpty()
				&&
				!other.isEmpty()
				&&
				getHead().equals(other.getHead())
				&&
				tail().equals(other.tail())
			)
			;

	}
	public boolean preEquals(ImmutableList<G> other) {
		return
			other != null // other exists
			;
	}
	private boolean postEquals(boolean result, ImmutableList<G> other) {
		return
			(count() == 0 && other.count() == 0) // both lists are empty
			|| // or else
			(
				count() > 0 && other.count() > 0 // neither list is empty, and ...
				&&
				getHead().equals(other.getHead()) && tail().equals(other.tail()) // ... their heads are equal and their tails are equal.

			)
			;
	}
	
	/**
	 * The i-th item in the list (the head is item(0)).
	 * 
	 * @param i
	 * @return
	 */
	public G item(int i) {
		if (i == 0)
			return getHead();
		else
			return tail().item(i-1);
	}
	private boolean preItem(int i) {
		return
			i >= 0 // i large enough
			&&
			i < count() // i small enough
			;
	}
	private boolean postItem(G result, int i) {
		return
			(i == 0 && result.equals(getHead()))
			||
			(result.equals(tail().item(i - 1)))
			;
	}
	
	/**
	 * A new list formed from the items at 'from' through 'to'.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public ImmutableList<G> sublist(int from, int to) {
		ImmutableList<G> result = new ImmutableList<G>();
		ImmutableCell<G> currentCell, lastCell;
		lastCell = null;
		for (int i = from; i < to; i++) {			
			currentCell = new ImmutableCell(item(i), lastCell);
			result.setHead(currentCell, (i - from + 1));
			lastCell = currentCell;
		}
		return result;
	}
	private boolean preSublist(int from, int to) {
		return
			from >= 0 // from position large enough
			&&
			from <= to + 1 // from position small enough
			&&
			to < count() // to position small enough
			;
	}
	private boolean postSublist(ImmutableList<G> result, int from, int to) {
		return
			// is empty consistent with from and to position
			result.isEmpty() == (from > to)			
			&&
			// result head is at from position in this
			ContractLogic.implies(from <= to, result.getHead().equals(item(from)))
			&&
			// result tail is correct sublist within this
			ContractLogic.implies(from < to, result.tail().equals(sublist(from + 1, to)))
			;
	}
	
	//private boolean invar() { return true; }
}
