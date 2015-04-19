package pt.unl.fct.di.dbc4j.examples;

public class A {

	protected int n;
	
	public A(int n) {
		this.n = n;
	}
	
	public void set(int n) {
		this.n = n;
	}
	private boolean preSet(int n) {
		return n == 0;
	}
	
	public int get() {
		return n;
	}

	private boolean invariant() {
		return n < 100;
	}
}
