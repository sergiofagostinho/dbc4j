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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Class for loading contract rules from a properties file.
 * 
 * @author Sergio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
class RulesLoader {

	private static final String SEPARATOR_TOKEN = ";";
	private static final String DEFAULT_CHECKS = "default.checks";
	private static final String[] CHECKS = {
		"no.checks.classes", "pre.checks.classes",
		"post.checks.classes", "all.checks.classes"
	};
	
	/**
	 * Loads rules from a <tt>.properties</tt> file.
	 * @param filename The properties file.
	 * @return The newly created rules object.
	 * @throws IOException If the file does not exist.
	 */
	public static Rules load(String filename)
	throws IOException {
		String property;
		String[] classes;
		Rules rules = new Rules();
		Properties properties = new Properties();
		properties.load(new FileInputStream(filename));
		
		if (properties.getProperty(DEFAULT_CHECKS) == null)
			throw new RulesFileException("'" + DEFAULT_CHECKS + "' property not found.");
		
		property = properties.getProperty(DEFAULT_CHECKS).trim();
		rules.setDefaultAssertion(RulesLoader.parseCheckLevel(property));
		
		CheckLevel[] levels = CheckLevel.values();
		for (int i = 0; i < levels.length; i++) {
			classes = fetchClasses(CHECKS[i], properties);
			for (int j = 0; j < classes.length; j++)
				rules.addClass(classes[j], levels[i]);
		}
		
		return rules;
	}

	/**
	 * Auxiliar method that fetches the class names that are in a specifc
	 * property.
	 * @param option The name of the property.
	 * @param properties The properties object.
	 * @return A list of class names.
	 */
	private static String[] fetchClasses(String option, Properties properties) {
		String property;
		
		if (properties.getProperty(option) == null)
			throw new RulesFileException("'" + option + "' property not found.");
		property = properties.getProperty(option).trim();
		if (property.length() != 0) {
			return property.split(SEPARATOR_TOKEN);
		}
		return new String[]{};
	}

	/**
	 * Parse <tt>CheckLevel</tt> enum from <tt>String</tt>.
	 * @param checkLevel
	 * @return
	 * @throws Exception If the the string is invalid.
	 */
	public static CheckLevel parseCheckLevel(String checkLevel) {
		for (CheckLevel check: CheckLevel.values()) {
			if (checkLevel.equalsIgnoreCase(check.toString()))
				return check;
		}
		throw new RulesFileException("'" + checkLevel + "' is not a valid check level.");
	}


}
