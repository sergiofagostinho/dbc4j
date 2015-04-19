package pt.unl.fct.di.dbc4j.examples.test;

import java.util.Calendar;

import pt.unl.fct.di.dbc4j.examples.WebUser;

/**
 * A test class for experimenting. For proper (unit) testing, check the 
 * <tt>WebUserTest</tt> class.
 * @deprecated Don't use it!
 * @author Sérgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class Test {

	static void test01() {
		for(int i = 0; i < 1000; i++) {
			Calendar calendar = Calendar.getInstance();
			WebUser bob = new WebUser();
			bob.setName("bob");
			bob.setAddress("www.bob.com");
			bob.toString();
			System.out.println(new Long(Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()));
		}		
	}
	
	static void test02() {
		WebUser bob = new WebUser();
		bob.setName("bob");
		bob.setAddress("www.bob.com");
		System.out.println(bob);
	}

	static void test26() {
		WebUser bob = new WebUser();
		bob.setId(1);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// test01();
		// test02();
		//test26();		
		
		//A obj = new A(1);
		//B obj = new B(1);
		//X obj = new X(1);
		//Y obj = new Y(1);
		
		//obj.set(-1);
		
		//Rules rules = ContractRules.getRules();
		//System.out.println(rules);
		//rules.addClass("pt.unl.fct.di.dbc4j.examples.User", CheckLevel.ALL);
		//System.out.println(rules);
		
		//User alice = new User(null, null, null);
		
		
				
		
		//User alice = new User(null, null, null);
		//String signature = "public static pt.unl.fct.StringBasic f()";
		//try {
		//	Class returnType = DesignByContract.extractReturnClass(signature);
		//	System.out.println(returnType);
		//}
		//catch (Exception e) {}

	}

}
