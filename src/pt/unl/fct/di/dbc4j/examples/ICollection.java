package pt.unl.fct.di.dbc4j.examples;

/**
 * An "compiler exposed" version of 
 * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Collection.html">
 * java.util.Collection</a>, without the throws clause.
 * 
 * @author Sérgio Agostinho
 *
 */
public interface ICollection<E> /*extends IIterable*/ {
	
	/**
	 * Returns an iterator over a set of elements of type T.
	 * 
	 * @return
	 */
	public IIterator<E> iterator();
    
	/**
	 * Ensures that this collection contains the specified element 
	 * (optional operation).
	 * 
	 * @param o
	 * @return
	 */
	public boolean add(E o);
	
	/**
	 * Adds all of the elements in the specified collection to this collection 
	 * (optional operation).
	 * 
	 * @param c
	 * @return
	 */
	public boolean addAll(ICollection<? extends E> c);
	
	/**
	 * Removes all of the elements from this collection (optional operation).
	 *
	 */
	public void clear();
	
	/**
	 * Returns true if this collection contains the specified element.
	 * 
	 * @param o
	 * @return
	 */
	public boolean contains(Object o);
	
	/**
	 * Returns true if this collection contains all of the elements in the 
	 * specified collection.
	 * 
	 * @param c
	 * @return
	 */
	public boolean containsAll(ICollection<?> c);
	
	/**
	 * Compares the specified object with this collection for equality.
	 * 
	 * @param o
	 * @return
	 */
	public boolean equal(Object o);
	
	/**
	 * Returns the hash code value for this collection.
	 * 
	 * @return
	 */
	public int hashCode();
	
	/**
	 * Returns true if this collection contains no elements.
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * Removes a single instance of the specified element from this collection,
	 * if it is present (optional operation).
	 * 
	 * @param o
	 * @return
	 */
    public boolean remove(Object o);
    
    /**
     * Removes all this collection's elements that are also contained in the 
     * specified collection (optional operation).
     * 
     * @param c
     * @return
     */
    public boolean removeAll(ICollection<?> c);
    
    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).
     * 
     * @param c
     * @return
     */
    public boolean retainAll(ICollection<?> c);
    
    /**
     * Returns the number of elements in this collection.
     * 
     * @return
     */
    public int size();
    
    /**
     * Returns an array containing all of the elements in this collection.
     * 
     * @return
     */
    public Object[] toArray();

    /**
     * Returns an array containing all of the elements in this collection; the 
     * runtime type of the returned array is that of the specified array.
     * 
     * @param <T>
     * @param a
     * @return
     */
    public <T> T[] toArray(T[] a);

}
