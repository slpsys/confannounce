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

/**
 * Contains the URL constants which should be changed if the location of the base 
 * ontologies ever changes. Note that this <b>will not</b> affect the operation of 
 * the ConfAnnounce tool, but resulting conference OWL files will have incorrect
 * import statements for the files listed in this class.
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 */
public class URLConstants {

	/**
	 * URL Constant referring to the conference ontology, current value is
	 * http://www.lehigh.edu/~mab305/swproj/conference.owl 
	 */
	public static String CONFERENCE_ONTOLOGY = "http://www.lehigh.edu/~mab305/swproj/conference.owl";
	
	/**
	 * Filename constant of the local version of the conference ontology. 
	 */
	public static String LOCAL_CONFERENCE_ONTOLOGY = "conference.owl";
	
	/**
	 * URL Constant referring to the months ontology referenced in the conference
	 * ontology, current value is
	 * http://www.lehigh.edu/~mab305/swproj/months.owl
	 */
	public static String MONTHS_ONTOLOGY = "http://www.lehigh.edu/~mab305/swproj/months.owl";
	
	/**
	 * Filename constant of the local version of the months ontology. 
	 */
	public static String LOCAL_MONTHS_ONTOLOGY = "months.owl";
}
