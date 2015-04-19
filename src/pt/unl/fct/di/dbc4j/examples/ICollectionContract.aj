/*package pt.unl.fct.di.dbc4j.examples;

import static pt.unl.fct.di.dbc4j.ContractMemory.remember;
import static pt.unl.fct.di.dbc4j.ContractMemory.old;

public aspect ICollectionContract {

	public boolean ICollection.preAdd(E o) {
		remember();
		return true;
	}	
	public boolean ICollection.postAdd(boolean result, E o) {
		ICollection old = (ICollection) old();
		boolean sizeAssert;
		boolean containsAssert;
		
		if (result) { // if collection is modified, then size changes
			sizeAssert = old.size() == size() + 1;
		}
		else { // else, size is unchanged
			sizeAssert = old.size() == size();
		}
		// the collection must contain the added element
		containsAssert = contains(o);
		
		return sizeAssert && containsAssert;
	}
	
	public boolean ICollection.preAddAll(ICollection<? extends E> c) {
		remember();
		return // the parameter cannot be null or the self
			c != null && !this.equals(c);
	}
	public boolean ICollection.postAddAll(boolean result,
			ICollection<? extends E> c) {
		ICollection old = (ICollection) old();
		boolean sizeAssert;
		boolean containsAssert;
		
		if (result) { // if collection is modified, then size changes
			sizeAssert = old.size() >= size() + 1;
		}
		else { // else, size is unchanged
			sizeAssert = old.size() == size();
		}
		
		// the collection must contain the added element
		containsAssert = true;
		IIterator it = c.iterator();
		for(Object element = it.next(); it.hasNext(); element = it.next()) {
			if(!contains(element))
				containsAssert = false;
		}
		
		return sizeAssert && containsAssert;
	}
	
	public boolean ICollection.postClear() {
		return // the collection must become empty
			size() == 0;
	}
	
	public boolean ICollection.preContainsAll(Collection c) {
		return
			c != null;
	}
}
*/