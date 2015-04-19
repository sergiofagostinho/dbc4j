package pt.unl.fct.di.dbc4j.examples.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.NameList;


/**
 * A JUnit test case class for <tt>NameList</tt>.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class NameListTest {

	private NameList list;
	
	@Before
	public void before() {
		list = new NameList();
	}
	
	@Test
	public void testPut() {
		list.put("Sérgio Agostinho");
		list.put("André Marques");
		list.put("Ricardo Ferreira");
		list.put("Ricardo Raminhos");
		list.put("Ana Moreira");
		list.put("Pedro Guerreiro");
	}

	@Test(expected=PreconditionException.class)
	public void testPut2() {
		list.put("Sérgio Agostinho");
		list.put("André Marques");
		list.put("Ricardo Ferreira");
		list.put("Ricardo Raminhos");
		list.put("Ana Moreira");
		list.put("Pedro Guerreiro");
		list.put("Sérgio Agostinho"); // exception here!
	}
	
	@Test
	public void testRemove() {
		list.put("Sérgio Agostinho");
		list.put("André Marques");
		list.put("Ricardo Ferreira");
		list.put("Ricardo Raminhos");
		list.put("Ana Moreira");
		list.put("Pedro Guerreiro");

		list.remove("Ana Moreira");
		list.remove("André Marques");
		list.remove("Pedro Guerreiro");
		list.remove("Ricardo Ferreira");
		list.remove("Ricardo Raminhos");
		list.remove("Sérgio Agostinho");
	}
	
	@Test(expected=PreconditionException.class)
	public void testRemove2() {
		list.put("Sérgio Agostinho");
		list.put("André Marques");
		list.put("Ricardo Ferreira");
		list.put("Ricardo Raminhos");
		list.put("Ana Moreira");
		list.put("Pedro Guerreiro");

		list.remove("Ana Moreira");
		list.remove("André Marques");
		list.remove("Pedro Guerreiro");
		list.remove("Ricardo Ferreira");
		list.remove("Ricardo Raminhos");
		list.remove("Sérgio Agostinho");
		
		list.remove("Ana Moreira"); // exception here!
	}

	@Test
	public void testAll() {
		assertEquals(list.count(), 0);
		assertFalse(list.has("Ana Moreira"));
		assertFalse(list.has("João Araújo"));
		
		list.put("Sérgio Agostinho");
		list.put("André Marques");
		list.put("Ricardo Ferreira");
		list.put("Ricardo Raminhos");
		list.put("Ana Moreira");
		list.put("Pedro Guerreiro");

		assertEquals(list.count(), 6);
		assertTrue(list.has("Ana Moreira"));
		assertFalse(list.has("João Araújo"));
		
		list.remove("Ana Moreira");
		list.remove("André Marques");
		list.remove("Pedro Guerreiro");
		list.remove("Ricardo Ferreira");
		list.remove("Ricardo Raminhos");
		list.remove("Sérgio Agostinho");

		assertEquals(list.count(), 0);
		assertFalse(list.has("Ana Moreira"));
		assertFalse(list.has("João Araújo"));
	}
}
