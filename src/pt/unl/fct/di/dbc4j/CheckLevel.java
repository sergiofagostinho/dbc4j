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
 * Enumeration that expresses the level of run-time checking performed.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 */
public enum CheckLevel {
	/**
	 * No condition is checked. Same as in Eiffel.
	 */
	NO {
		public String toString() { return "no"; }
	},
	/**
	 * Only preconditions are checked. Equivalent to 'require' in Eiffel
	 */
	PRE {
		public String toString() { return "pre"; }
	},
	/**
	 * Preconditions and postconditions are checked. Equivalent to 'ensure' in 
	 * Eiffel.
	 */
	POST {
		public String toString() { return "post"; }
	},
	/**
	 * Preconditions, postconditions and invariants are checked. Equivalent to 
	 * 'invariant' in Eiffel.
	 */
	ALL {
		public String toString() { return "all"; }
	}
}
