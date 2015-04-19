package pt.unl.fct.di.dbc4j.examples.test;

import org.junit.Test;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.DumbIterator;

public class DumbIteratorTest {

	@Test(expected=PreconditionException.class)
	public void test() {
		DumbIterator<String> it = new DumbIterator<String>();
		it.next();
	}
}
