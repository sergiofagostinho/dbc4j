package pt.unl.fct.di.dbc4j.examples;

public class Foo {

	// default constructor
	public Foo() {}
	
	// copy constructor
	public Foo(Foo other) {	}
	
	public int[] f(int n) {
		return new int[] {1, 2, 3};
	}
	public boolean preF(int n) {
		return n > 10;
	}
	public boolean postF(int[] result, int n) {
		return true;
	}
}
