/*
Copyright (c) 2007 - Sérgio Agostinho

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package pt.unl.fct.di.dbc4j;



/**
 * Class for emulating first order logic operators and quantifiers.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class ContractLogic {
	
	/**
	 * Implies operator (<tt>=></tt>). 
	 * Note that this is not a very efficient implementation, since it uses 
	 * <i>eager evaluation</i>.
	 */
	public static boolean implies(boolean foo, boolean bar) {
		return (!foo) || bar;
	}

	// Future work:
	
	//public static boolean forall(..) { ... }

	/*
	public static <T> boolean forAll(Collection<T> collection,
		Predicate<T> predicate) {
		return false;
	}
	*/
	
	//public static boolean exists(..) { ... }

	/*
	public static <T> boolean exists(Collection<T> collection,
			Predicate<T> predicate) {
			return false;
		}
	*/
	
	//public static Object[] elements(..) { ... }
	
}
