package pt.unl.fct.di.dbc4j.examples;

/**
 * Simple implementation of mathematical functions, using contracts.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class MathFunctions {

	/**
	 * Factorial function.
	 * See <tt>http://en.wikipedia.org/wiki/Factorial</tt>.
	 * 
	 * @param n
	 * @return
	 */
	public static long factorial(int n) {
		if (n == 0)
			return 1;
		else
			return n*factorial(n-1);
	}
	public static boolean preFactorial(int n) {
		return n >= 0;
	}
	public static boolean postFactorial(long result, int n) {
		if (n == 0)
			return (result == 1); // factorial(0) = 0
		else // n > 0: assured by the precondition 
			return (result > 0); // factorial(n) > 0
	}
	
	/**
	 * Fibonnaci number.
	 * See <tt>http://en.wikipedia.org/wiki/Fibonacci_number</tt>.
	 * 
	 * @param n
	 * @return
	 */
	public static long fibonnaci(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		
		return fibonnaci(n-1) + fibonnaci(n-2);
	}
	public static boolean preFibonnaci(int n) {
		return n >= 0;
	}
	public static boolean postFibonnaci(long result, int n) {
		if (n == 0)
			return (result == 0); // fibonnaci(0) = 0
		if (n == 1)
			return (result == 1); // fibonnaci(1) = 1
		// n > 1: assured by the precondition
		return (result > 0); // fibonnaci(n) > 0
	}
}
