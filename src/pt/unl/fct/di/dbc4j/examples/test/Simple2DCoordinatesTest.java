package pt.unl.fct.di.dbc4j.examples.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.InvariantException;
import pt.unl.fct.di.dbc4j.examples.Simple2DCoordinates;


public class Simple2DCoordinatesTest {

	// Normal behaviour.
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
		Simple2DCoordinates coords = new Simple2DCoordinates(14, 41);
	}

	// Invariant fails, latitude is not valid.
	@Test(expected=InvariantException.class)
	public void testConstructor1() {
		@SuppressWarnings("unused")
		Simple2DCoordinates coords = new Simple2DCoordinates(100, 100); // here
	}

	// Normal behaviour.
	@Test
	public void testDefaultConstructor() {
		Simple2DCoordinates coords = new Simple2DCoordinates();
		assertEquals(coords.getLatitude(), 0);
		assertEquals(coords.getLongitude(), 0);
	}

	// Normal behaviour.
	@Test
	public void testSetters() {
		Simple2DCoordinates coords = new Simple2DCoordinates();
		coords.setLatitude(14);
		coords.setLongitude(41);
		assertEquals(coords.getLatitude(), 14);
		assertEquals(coords.getLongitude(), 41);
	}
	
	// Invariant fails, longitude is not valid.
	@Test(expected=InvariantException.class)
	public void testSetters1() {
		Simple2DCoordinates coords = new Simple2DCoordinates();
		coords.setLatitude(90);
		coords.setLongitude(200); // here
	}
	
	// Normal behaviour.
	@Test
	public void testParse() {
		String input = "45º 90º";
		Simple2DCoordinates coords = Simple2DCoordinates.parse(input);
		assertEquals(coords.getLatitude(), 45);
		assertEquals(coords.getLongitude(), 90);
	}
	
	// Normal behaviour.
	@Test
	public void testParse1() {
		String input = "-45º 180º";
		Simple2DCoordinates coords = Simple2DCoordinates.parse(input);
		assertEquals(coords.getLatitude(), -45);
		assertEquals(coords.getLongitude(), 180);
	}
	
	// Normal behaviour.
	@Test
	public void testParse2() {
		String input = "-90º -180º";
		Simple2DCoordinates coords = Simple2DCoordinates.parse(input);
		assertEquals(coords.getLatitude(), -90);
		assertEquals(coords.getLongitude(), -180);
	}
	
	// Invariant fails, latitude is not valid.
	@Test(expected=InvariantException.class)
	public void testParse3() {
		String input = "91º -180º";
		Simple2DCoordinates coords = Simple2DCoordinates.parse(input);
		assertEquals(coords.getLatitude(), 91);
		assertEquals(coords.getLongitude(), -180);
	}

	// Precondition fails, input is malformed.
	@Test(expected=PreconditionException.class)
	public void testParse4() {
		String input = "91 -180";
		@SuppressWarnings("unused")
		Simple2DCoordinates coords = Simple2DCoordinates.parse(input); // here
	}
}
