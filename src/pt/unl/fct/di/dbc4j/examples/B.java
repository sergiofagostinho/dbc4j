package pt.unl.fct.di.dbc4j.examples;

public class B extends A {

	public B(int n) {
		super(n);
	}

	public void set(int n) {
		this.n = n*n;
	}	
	private boolean preSet(int n) {
		return n > 0;
	}

}
