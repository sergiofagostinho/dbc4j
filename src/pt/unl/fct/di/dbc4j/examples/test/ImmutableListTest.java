package pt.unl.fct.di.dbc4j.examples.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pt.unl.fct.di.dbc4j.examples.ImmutableCell;
import pt.unl.fct.di.dbc4j.examples.ImmutableList;


/**
 * A JUnit test case class for <tt>ImmutableList</tt>.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class ImmutableListTest {

	private ImmutableList<String> list;
	
	@Before
	public void before() {
		list = new ImmutableList<String>();
	}
	
	@Test
	public void testNewCell() {
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", null);		
		ImmutableCell<String> bar = new ImmutableCell<String>("olé", null);
		foo.setRight(bar);
		assertEquals(foo.getRight(), bar);
	}

	@Test
	public void testSetHead() {
		ImmutableCell<String> bar = new ImmutableCell<String>("olé", null);
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", bar);		
		list.setHead(foo, 2);
	}

	@Test
	public void testGetHead() {
		ImmutableCell<String> bar = new ImmutableCell<String>("olé", null);
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", bar);		
		list.setHead(foo, 2);
		assertEquals(foo.getItem(), list.getHead());
	}

	@Test
	public void testTail() {
		ImmutableCell<String> bar = new ImmutableCell<String>("olé", null);
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", bar);		
		list.setHead(foo, 2);
		assertEquals(bar.getItem(), list.tail().getHead());
	}

	@Test
	public void testItem() {
		ImmutableCell<String> bar = new ImmutableCell<String>("olé", null);
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", bar);		
		list.setHead(foo, 2);
		assertEquals(list.getHead(), list.item(0));
		assertEquals(list.tail().getHead(), list.item(1));
	}
	
	@Test
	public void testCount() {
		assertEquals(list.count(), 0);
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", null);		
		list.setHead(foo, 1);
		assertEquals(list.count(), 1);		
	}

	@Test
	public void testIsEmpty() {
		assertTrue(list.isEmpty());
		assertEquals(list.isEmpty(), list.count() == 0);
		ImmutableCell<String> foo = new ImmutableCell<String>("olá", null);		
		list.setHead(foo, 1);
		assertFalse(list.isEmpty());
		assertEquals(list.isEmpty(), list.count() == 0);
	}
	
	
	

}
