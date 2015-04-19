package pt.unl.fct.di.dbc4j.examples.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.Queue;


/**
 * A JUnit test case class for <tt>Queue</tt>.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class QueueTest {

	private Queue<String> queue;
	private static final int MAX_ELEMS = 6;
	
	@Before
	public void before() {
		queue = new Queue<String>(MAX_ELEMS);
	}

	@Test
	public void testPut() {
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
	}

	/**
	 * Precondition for put fails! (max elems)
	 *
	 */
	@Test(expected=PreconditionException.class)
	public void testPut2() {
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.put("Pre-combat");
		queue.put("Combat");
		queue.put("Post-combat");
		queue.put("Cleanup"); // exception here!
	}

	@Test
	public void testRemove() {
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.remove();
		queue.remove();
		queue.remove();
	}

	/**
	 * Precondition fails for remove.
	 *
	 */
	@Test(expected=PreconditionException.class)
	public void testRemove2() {
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.remove();
		queue.remove();
		queue.remove();
		queue.remove(); // exception here!
	}

	@Test
	public void testCount() {
		assertEquals(queue.count(), 0);
		queue.put("Untap");
		assertEquals(queue.count(), 1);
		queue.put("Upkeep");
		assertEquals(queue.count(), 2);
		queue.put("Draw");
		assertEquals(queue.count(), 3);
		queue.remove();
		assertEquals(queue.count(), 2);
		queue.remove();
		assertEquals(queue.count(), 1);
		queue.remove();
		assertEquals(queue.count(), 0);
	}

	@Test
	public void testIsEmpty() {
		assertTrue(queue.isEmpty());
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		assertFalse(queue.isEmpty());
		queue.remove();
		queue.remove();
		queue.remove();
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testIsFull() {
		assertFalse(queue.isFull());
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.put("Pre-combat");
		queue.put("Combat");
		queue.put("Post-combat");
		assertTrue(queue.isFull());
		queue.remove();
		assertFalse(queue.isFull());
	}
	
	@Test
	public void testHead() {
		queue.put("Untap");
		assertEquals(queue.head(), "Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.put("Pre-combat");
		queue.put("Combat");
		queue.put("Post-combat");
		assertEquals(queue.head(), "Untap");
		queue.remove();
		assertEquals(queue.head(), "Upkeep");
	}
	
	@Test
	public void testItems() {
		assertEquals(queue.count(), queue.items().count());
		assertEquals(queue.isEmpty(), queue.items().isEmpty());
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.put("Pre-combat");
		queue.put("Combat");
		queue.put("Post-combat");
		assertEquals(queue.count(), queue.items().count());
		assertEquals(queue.isEmpty(), queue.items().isEmpty());
		assertEquals(queue.head(), queue.items().getHead());
		queue.remove();
		queue.remove();
		queue.remove();
		queue.remove();
		queue.remove();
		assertEquals(queue.count(), queue.items().count());
		assertEquals(queue.isEmpty(), queue.items().isEmpty());
		assertEquals(queue.head(), queue.items().getHead());
	}
	
	@Test
	public void testNextLocationAfter() {
		queue.put("Untap");
		queue.put("Upkeep");
		queue.put("Draw");
		queue.put("Pre-combat");
		queue.put("Combat");
		queue.put("Post-combat");
		for (int i = 0; i < queue.capacity() - 1; i++)
			assertEquals(queue.nextLocationAfter(i), i + 1);
		assertEquals(queue.nextLocationAfter(queue.capacity() - 1), 0);
	}
}
