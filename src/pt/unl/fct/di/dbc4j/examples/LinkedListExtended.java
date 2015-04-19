package pt.unl.fct.di.dbc4j.examples;

import static pt.unl.fct.di.dbc4j.ContractMemory.old;
import static pt.unl.fct.di.dbc4j.ContractMemory.remember;
import static pt.unl.fct.di.dbc4j.ContractLogic.implies;

import java.util.Collection;
import java.util.LinkedList;

// TODO: port these contracts for the Collection interface
public class LinkedListExtended<E> extends LinkedList<E> {

	/* boolean 	add(E o)
          Appends the specified element to the end of this list.
	 */
	public boolean preAdd() {
		//remember(this);
		remember();
		return true;
	}
	public boolean postAdd(boolean result, E o) {
		//LinkedListExtended<E> old = (LinkedListExtended<E>) old(this);
		LinkedListExtended<E> old = (LinkedListExtended<E>) old();
		return // according to the Collection contract
			result == true
			&& // inserted element is at the tail
			(o != null && o.equals(getLast()))
			&& // one more element
			size() == old.size() + 1;
	}

	/* void 	add(int index, E element)
          Inserts the specified element at the specified position in this list.
     */
	public boolean preAdd(int index, E element) {
		//remember(this);
		remember();
		return
			// valid index
			(index < 0 || index > size());			
	}	
	public boolean postAdd(int index, E element) {
		//LinkedListExtended<E> old = (LinkedListExtended<E>) old(this);
		LinkedListExtended<E> old = (LinkedListExtended<E>) old();
		return // inserted element is at the required position
			(element != null && element.equals(get(index)))
			&& // one more element
			size() == old.size() + 1;
	}
	
	/* boolean 	addAll(Collection<? extends E> c)
          Appends all of the elements in the specified collection to the end of 
          this list, in the order that they are returned by the specified 
          collection's iterator.
     */
	public boolean preAddAll(Collection<? extends E> c) {
		//remember(this);
		remember();
		return // collection can't be null
			c != null;
	}
	public boolean postAddAll(boolean result, Collection<? extends E> c) {
		//LinkedListExtended<E> old = (LinkedListExtended<E>) old(this);
		LinkedListExtended<E> old = (LinkedListExtended<E>) old();
		return
			// returns true if the list changed
			implies(result, !old.equals(this))
			&& // more elements
			size() == old.size() + c.size();
	}
	
	/* boolean 	addAll(int index, Collection<? extends E> c)
          Inserts all of the elements in the specified collection into this 
          list, starting at the specified position.
	 */
	public boolean preAddAll(int index, Collection<? extends E> c) {
		//remember(this);
		remember();
		return
			// valid index
			(index < 0 || index > size())
			&& // collection can't be null
			c != null
			;
	}
	public boolean postAddAll(boolean result, int index, 
			Collection<? extends E> c) {
		//LinkedListExtended<E> old = (LinkedListExtended<E>) old(this);
		LinkedListExtended<E> old = (LinkedListExtended<E>) old();
		return
			// more elements
			size() == old.size() + c.size();
	}
	
	/* void 	addFirst(E o)
          Inserts the given element at the beginning of this list.
	 */
	public boolean preAddFirst(E o) {
		//remember(this);
		remember();
		return true;
	}
	public boolean postAddFirst(E o) {
		//LinkedListExtended<E> old = (LinkedListExtended<E>) old(this);
		LinkedListExtended<E> old = (LinkedListExtended<E>) old();
		return // element is added to the head
			getFirst().equals(o)
			&& // more elements
			size() == old.size() + 1;	
	}
}
