package pt.unl.fct.di.dbc4j.examples;

public class Bar extends Foo {
	
	public int[] f(int n) {
		return new int[] { 0, 1, 2, 3 };
	}
	public boolean preF(int n) {
		return n > 0;
	}
	public boolean postF(int[] result, int n) {
		return true;
	}
}
