package pt.unl.fct.di.dbc4j.examples;

/**
 * An example class demonstrating DbC4J. Based in an example taken from
 * <i>Design by Contract, by Example</i>.
 * 
 * A cell holds an item and a reference to the first cell in a list of cells.
 *  
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class ImmutableCell<G> {

	private G item;
	private ImmutableCell<G> right;
	
	/**
	 * Default constructor.
	 * Make a new cell.
	 * 
	 * @param item
	 * @param right
	 */
	public ImmutableCell(G item, ImmutableCell<G> right) {
		this.item = item;
		this.right = right;
	}
	public boolean preImmutableCell(G item) {
		return
			item != null // item can't be null
			;
	}
	public boolean postImmutableCell(G item, ImmutableCell<G> right) {
		return
			getItem().equals(item)
			&&
			(
				right == null && getRight() == null
				||
				right != null && getRight().equals(right)
			)
			;
	}
	
	public G getItem() {
		return item;
	}
	
	public void setItem(G item) {
		this.item = item;
	}
	
	public ImmutableCell<G> getRight() {
		return right;
	}
	
	/**
	 * Set 'Current' to point to a cell (which can be Void).
	 * 
	 * @param right
	 */
	public void setRight(ImmutableCell<G> right) {
		this.right = right;
	}
	public boolean postSetRight(ImmutableCell<G> right) {
		return
			getRight().equals(right)
			;
	}

	
}
