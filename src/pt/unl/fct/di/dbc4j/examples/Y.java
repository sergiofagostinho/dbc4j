package pt.unl.fct.di.dbc4j.examples;

public class Y extends X {

	public Y(int n) {
		super(n);
	}
	
	public void set(int n) {
		this.n = n*n;
	}	
	public boolean preSet(int n) {
		return n > 0;
	}
}
