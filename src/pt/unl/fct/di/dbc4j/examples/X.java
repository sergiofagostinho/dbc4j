package pt.unl.fct.di.dbc4j.examples;

public class X {

	protected int n;
	
	public X(int n) {
		this.n = n;
	}
	
	public X(X original) {
		this(original.get());
	}
	
	public void set(int n) {
		this.n = n;
	}
	public boolean preSet(int n) {
		return n == 0;
	}

	public int get() {
		return n;
	}

	public boolean invariant() {
		return n < 100;
	}
}
