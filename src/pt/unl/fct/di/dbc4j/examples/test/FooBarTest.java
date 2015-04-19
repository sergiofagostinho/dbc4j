package pt.unl.fct.di.dbc4j.examples.test;

import org.junit.Test;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.Bar;
import pt.unl.fct.di.dbc4j.examples.Foo;

/**
 * ...
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class FooBarTest {

	// TODO document!
	
	@Test(expected=PreconditionException.class)
	public void testFoo() {
		Foo temp = new Foo();
		temp.f(10);
	}
	
	@Test
	public void testFoo1() {
		Foo temp = new Foo();
		temp.f(11);
	}

	@Test
	public void testBar() {
		Bar temp = new Bar();
		temp.f(10);
	}
	
	@Test(expected=PreconditionException.class)
	public void testBar1() {
		Bar temp = new Bar();
		temp.f(0);
	}

	// TODO the following two tests are using dinamic type evaluation, is it correct?
	@Test(expected=PreconditionException.class)
	public void testFooBar() {
		Foo temp = new Bar();
		temp.f(10);
	}
	
	@Test
	public void testFooBar1() {
		Foo temp = new Bar();
		temp.f(11);
	}
}
