package pt.unl.fct.di.dbc4j.examples.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.Stack;


/**
 * A JUnit test case class for <tt>Stack</tt>.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class StackTest {

	private Stack<Integer> stack;
	
	@Before
	public void before() {
		stack = new Stack<Integer>();
	}
	
	/**
	 * Put ok.
	 *
	 */
	@Test
	public void testPut() {		
		stack.put(10);
		stack.item();
		stack.put(20);
		stack.item();
		stack.put(30);
		stack.item();
	}
	
	/**
	 * Remove ok.
	 *
	 */
	@Test
	public void testRemove() {
		stack.put(10);
		stack.item();
		stack.put(20);
		stack.item();
		stack.put(30);
		stack.item();
	
		stack.remove();
		stack.item();
		stack.remove();
		stack.item();
		stack.remove();
	}

	/**
	 * Precondition for remove fails.
	 *
	 */
	@Test(expected=PreconditionException.class)
	public void testRemove2() {
		stack.put(10);
		stack.item();
		stack.put(20);
		stack.item();
		stack.put(30);
		stack.item();
	
		stack.remove();
		stack.item();
		stack.remove();
		stack.item();
		stack.remove();
		stack.item(); // here!
	}

	/**
	 * Is empty ok.
	 *
	 */
	@Test
	public void testIsEmpty() {
		assertTrue(stack.isEmpty());

		stack.put(10);
		stack.item();
		stack.put(20);
		stack.item();
		stack.put(30);
		stack.item();
	
		assertFalse(stack.isEmpty());
		
		stack.remove();
		stack.item();
		stack.remove();
		stack.item();
		stack.remove();
		
		assertTrue(stack.isEmpty());
	}
	
	@Test
	public void testItem() {
		stack.put(10);
		assertEquals(stack.item(), 10);
		
		stack.put(20);
		assertEquals(stack.item(), 20);
		
		stack.remove();
		assertEquals(stack.item(), 10);		
	}
	
	@Test(expected=PreconditionException.class)
	public void testItem2() {
		stack.item();
	}

}
