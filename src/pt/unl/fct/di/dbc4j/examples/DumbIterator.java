package pt.unl.fct.di.dbc4j.examples;

public class DumbIterator<E> implements IIterator {

	public DumbIterator() {}
	
	public boolean hasNext() {
		return false;
	}
	
	public E next() {
		return null;
	}
	
	public void remove() {}
}
