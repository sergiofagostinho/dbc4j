package pt.unl.fct.di.dbc4j.examples.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.Dictionary;


/**
 * A JUnit test case class for <tt>DictionaryTest</tt>.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class DictionaryTest {

	private Dictionary<String,String> dictionary;
	
	@Before
	public void before() {
		dictionary = new Dictionary<String,String>();
	}
	
	@Test
	public void testPut() {
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
	}
	
	@Test
	public void testCount() {
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		assertEquals(dictionary.count(), 6);
	}
	
	@Test
	public void testHas() {
		assertFalse(dictionary.has("agm@uninova.pt"));
		assertFalse(dictionary.has("ja@di.fct.unl.pt"));
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		assertTrue(dictionary.has("agm@uninova.pt"));
		assertFalse(dictionary.has("ja@di.fct.unl.pt"));
	}

	@Test
	public void testRemove() {
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		assertTrue(dictionary.has("sma@uninova.pt"));
		assertEquals(dictionary.count(), 6);
		dictionary.remove("sma@uninova.pt");
		assertEquals(dictionary.count(), 5);
		assertFalse(dictionary.has("sma@uninova.pt"));
	}

	@Test(expected=PreconditionException.class)
	public void testRemove2() {
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		dictionary.remove("ja@uninova.pt"); // exception here
	}
	
	@Test
	public void testValueFor() {
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		assertEquals(dictionary.valueFor("sma@uninova.pt"), "Sérgio Agostinho");		
	}

	@Test(expected=PreconditionException.class)
	public void testValueFor2() {
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		dictionary.put("agm@uninova.pt", "André Marques");
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		assertEquals(dictionary.valueFor("sma@uninova.pt"), "Sérgio Agostinho");
		dictionary.remove("sma@uninova.pt");
		dictionary.valueFor("sma@uninova.pt"); // exception here
	}

	@Test
	public void testAll() {
		assertEquals(dictionary.count(), 0);
		assertFalse(dictionary.has("sma@uninova.pt"));
		assertFalse(dictionary.has("agm@uninova.pt"));
		assertFalse(dictionary.has("rmf@uninova.pt"));
		assertFalse(dictionary.has("rfr@uninova.pt"));
		assertFalse(dictionary.has("amm@di.fct.unl.pt"));
		assertFalse(dictionary.has("pg@di.fct.unl.pt"));
		
		dictionary.put("sma@uninova.pt", "Sérgio Agostinho");
		assertTrue(dictionary.has("sma@uninova.pt"));
		assertEquals(dictionary.count(), 1);
		
		dictionary.put("agm@uninova.pt", "André Marques");
		assertTrue(dictionary.has("agm@uninova.pt"));
		assertEquals(dictionary.count(), 2);
		
		dictionary.put("rmf@uninova.pt", "Ricardo Ferreira");
		assertTrue(dictionary.has("rmf@uninova.pt"));
		assertEquals(dictionary.count(), 3);
		
		dictionary.put("rfr@uninova.pt", "Ricardo Raminhos");
		assertTrue(dictionary.has("rfr@uninova.pt"));
		assertEquals(dictionary.count(), 4);
		
		dictionary.put("amm@di.fct.unl.pt", "Ana Moreira");
		assertTrue(dictionary.has("amm@di.fct.unl.pt"));
		assertEquals(dictionary.count(), 5);
		
		dictionary.put("pg@di.fct.unl.pt", "Pedro Guerreiro");
		assertTrue(dictionary.has("pg@di.fct.unl.pt"));
		assertEquals(dictionary.count(), 6);
		
		assertEquals(dictionary.valueFor("amm@di.fct.unl.pt"), "Ana Moreira");
		
		dictionary.remove("amm@di.fct.unl.pt");
		assertFalse(dictionary.has("amm@di.fct.unl.pt"));
		assertEquals(dictionary.count(), 5);

		dictionary.put("amm@di.fct.unl.pt", "Profª Ana Moreira");
		assertTrue(dictionary.has("amm@di.fct.unl.pt"));
		assertEquals(dictionary.count(), 6);
		assertEquals(dictionary.valueFor("amm@di.fct.unl.pt"), "Profª Ana Moreira");
	}
	
}
