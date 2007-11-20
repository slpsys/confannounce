/**
 * Copyright 2007 Marc Bollinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package edu.lehigh.mab305.swproj.Application;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * This class simply contains two static methods which converb back and forth
 * between XML Schema xsd:date format and a format recognized by the
 * java.text.Dateformat class. 
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 */
public class TimeUtil {

	/**
	 * Takes a stringified date, and returns it in xsd:date format.
	 * 
	 * @param date The stringified date, recognizeable by Java's Calendar class.
	 * @return The string in xsd:date format.
	 */
	public static String parseDate(String date) {
		StringBuilder outDate = new StringBuilder();
		if (date != null) {
			DateFormat df = DateFormat.getDateInstance();
			df.setLenient(true);
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(df.parse(date));
				outDate.append(c.get(Calendar.YEAR));
				outDate.append("-");
				outDate.append(c.get(Calendar.MONTH) + 1);
				outDate.append("-");
				outDate.append(c.get(Calendar.DAY_OF_MONTH));
			}
			catch (ParseException e) {
				
			}
		}
		return outDate.toString();
	}
	
	/**
	 * Converts a stringified xsd:date object into a format actually recognized
	 * by the Java Calendar class. Primarily, this just requires changing from
	 * mm-dd-yy[yy] to mm/dd/yy[yy] 
	 * 
	 * @param calDate String version of a date in xsd:date format.
	 * @return A modified version of the string parseable by Java's calendar class.
	 */
	public static String makeParseable(String calDate) {
		StringBuilder ret = new StringBuilder();
		ret.append(calDate.substring(calDate.indexOf("-") + 1, calDate.lastIndexOf("-")));
		ret.append("/");
		ret.append(calDate.substring(calDate.lastIndexOf("-") + 1));
		ret.append("/");
		ret.append(calDate.substring(0, calDate.indexOf("-")));
		return ret.toString();
	}

}
