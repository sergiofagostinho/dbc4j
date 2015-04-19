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
 * Exception used when trying to use the 'remember' mecanism on a
 * non-cloneable class. (See <tt>ContractMemory</tt>)
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class NonCloneableException extends RuntimeException {

	/**
	 * Creates a new exception.
	 * @param message The exception detailed message
	 */
	public NonCloneableException(String message) {
		super("Class '" + message + "' is not cloneable!");
	}
}
