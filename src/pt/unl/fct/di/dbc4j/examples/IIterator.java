package pt.unl.fct.di.dbc4j.examples;

/**
 * An "compiler exposed" version of 
 * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Iterator.html">
 * java.util.Iterator</a>, without the throws clause.
 * 
 * @author Sérgio Agostinho
 *
 */
public interface IIterator<E> {

	/**
	 * Returns true if the iteration has more elements.
	 * 
	 * @return
	 */
	public boolean hasNext();
    
	/**
	 * Returns the next element in the iteration.
	 * 
	 * @return
	 */
	public E next();
    
	/**
	 * Removes from the underlying collection the last element returned by the
	 * iterator (optional operation).
	 *
	 */
	public void remove();    
}
