package pt.unl.fct.di.dbc4j.examples.test;

import org.junit.Test;

import pt.unl.fct.di.dbc4j.InvariantException;
import pt.unl.fct.di.dbc4j.PostconditionException;
import pt.unl.fct.di.dbc4j.PreconditionException;
import pt.unl.fct.di.dbc4j.examples.User;
import pt.unl.fct.di.dbc4j.examples.WebUser;

/**
 * A JUnit test case class for WebUser.
 * 
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class WebUserTest {

	// Testing User.setPhoneNumber overloaded methods with preconditions.
	// User.setPhoneNumber(int) precondtion fails
	@Test(expected=PreconditionException.class)
	public void test01() {
		User alice = new User();
		alice.setPhoneNumber("0");
	}
	
	// Testing User.setPhoneNumber overloaded methods with preconditions.
	// User.setPhoneNumber(String) precondtion fails
	@Test(expected=PreconditionException.class)
	public void test02() {
		User alice = new User();
		alice.setPhoneNumber("666");
	}
	
	// Testing User.setPhoneNumber overloaded methods with preconditions.
	// Preconditions ok
	@Test
	public void test03() throws Exception {
		User alice = new User();
		alice.setPhoneNumber("123456789");
	}
	
	// Testing User.setPhoneNumber overloaded methods with preconditions.
	// User.setPhoneNumber(String) precondition fails
	@Test(expected=PreconditionException.class)
	public void test04() {
		User alice = new User();
		alice.setPhoneNumber(69);
	}

	// Testing User.setPhoneNumber overloaded methods with preconditions.
	// User.setPhoneNumber(int) precondition fails
	@Test(expected=PreconditionException.class)
	public void test05() {
		User alice = new User();
		alice.setPhoneNumber(-1);
	}

	// Testing User.setPhoneNumber overloaded methods with preconditions.
	// Preconditions ok
	@Test
	public void test06() throws Exception {
		User alice = new User();
		alice.setPhoneNumber(123456789);
	}

	// Testing User.setName postcondition and class invariant.
	// Precondition and invariant ok
	@Test
	public void test07() throws Exception {
		User alice = new User();
		alice.setName("José Povinho");
	}

	// Testing User.setName postcondition and class invariant.
	// Invariant fails
	@Test(expected=InvariantException.class)
	public void test08() {
		User alice = new User();
		alice.setName(null);
	}

	// Testing User.setAddress precondition and class invariant.
	// Precondition fails
	@Test(expected=PreconditionException.class)
	public void test09() {
		User alice = new User();
		alice.setAddress(null);
	}

	// Testing User.setAddress precondition and class invariant.
	// Precondition and class invariant ok
	@Test
	public void test10() throws Exception {
		User alice = new User();
		alice.setName("Jane Doe");
		alice.setAddress("Praça vermelha");
	}

	// Testing User.setAddress overloaded method precondition and class invariant.
	// Precondition and class invariant ok
	@Test
	public void test11() throws Exception {
		User alice = new User();
		alice.setName("Jane Doe");
		alice.setAddress("Praça vermelha", "666");
	}

	// Testing User invariant in the class contructor.
	// Invariant fails
	@Test(expected=InvariantException.class)
	public void test12() {
		User alice = new User(null, null, null);
		if (true)
			return;
		alice.toString();		
	}

	// Testing invariant inherance User > WebUser in the class constructor.
	// Invariant fails
	@Test(expected=InvariantException.class)
	public void test13() {
		WebUser bob = new WebUser(null, null, null);
		if (true)
			return;
		bob.toString();
	}
	
	// Testing WebUser.setAddress overrided method precondition
	// Preconditions fail
	@Test(expected=PreconditionException.class)
	public void test14() {
		WebUser bob = new WebUser();
		bob.setAddress("www.bob.com");
	}

	// Testing WebUser.setAddress overrided method precondition
	// Overrided precondition ok
	@Test
	public void test15() throws Exception {
		WebUser bob = new WebUser();
		bob.setAddress("http://www.bob.com");
	}

	// Testing WebUser.setAddress overrided method precondition
	// Precondition ok
	@Test
	public void test16() throws Exception {
		WebUser bob = new WebUser();
		bob.setName("bob");
		bob.setAddress("www.bob.com");
	}

	// Testing WebUser.setId postcondition with the 'old' mechanism.
	// Postcondition ok.
	@Test
	public void test17() throws Exception {
		WebUser bob = new WebUser(13);
		bob.setId(69);
	}

	// Testing WebUser.setId postcondition with the 'old' mechanism.
	// Postcondition fails.
	@Test(expected=PostconditionException.class)
	public void test18() throws Exception {
		WebUser bob = new WebUser(13);
		bob.setId(11);
	}

 	// Testing WebUser constructor precondition.
	// Precondition fails.
	@Test(expected=PreconditionException.class)
	public void test19() {
		WebUser bob = new WebUser(-1);
		bob.setId(69);
	}

	// Testing WebUser postcondition on static method
	// Postcondition ok.
	@Test
	public void test20() throws Exception {
		WebUser.dumbMethod(14);
	}

	// Testing WebUser postcondition on static method
	// Postcondition fails.
	@Test(expected=PostconditionException.class)
	public void test21() {
		WebUser.dumbMethod(13);
	}

	// Testing WebUser precondition with an array argument
	// Precondition ok.
	@Test
	public void test22() throws Exception {
		WebUser bob = new WebUser();
		String[] notes = { "todo: find better examples" };
		bob.setNotes(notes);
	}
	
	// Testing WebUser precondition with an array argument
	// Precondition fails.
	@Test
	public void test23() throws Exception {
		WebUser bob = new WebUser();
		bob.setNotes(new String[] {});
	}

	// Testing WebUser postcondition with void result type
	// Postcondition ok.
	@Test
	public void test24() throws Exception {
		WebUser.lessDumbMethod(14);
	}

	// Testing WebUser postcondition with void result type
	// Postcondition fails.
	@Test(expected=PostconditionException.class)
	public void test25() {
		WebUser.lessDumbMethod(13);
	}
//*/	
	// Testing WebUser.setId postcondition with the 'old' mechanism, with recursion.
	// Postcondition fails.
	@Test(expected=PostconditionException.class)
	public void test26() throws Exception {
		WebUser bob = new WebUser();
		bob.setId(1);
	}

}
