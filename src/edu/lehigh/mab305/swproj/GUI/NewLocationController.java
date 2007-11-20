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
 * @author Marc Bollinger mab305@lehigh.edu
 */
package edu.lehigh.mab305.swproj.GUI;

import edu.lehigh.mab305.swproj.ConferenceModel.*;

public class NewLocationController {
	
	ConfAnnounceController controller;
	Conference conference;
	String internalURI;

	public NewLocationController(ConfAnnounceController controller) {
		super();
		this.controller = controller;
		this.conference = controller.getConf();
		this.internalURI = this.conference.getBaseURI();
	}

	/**
	 * @return the conference
	 */
	public Conference getConf() {
		return conference;
	}

	/**
	 * @param conf the conf to set
	 */
	public void setConf(Conference conference) {
		this.conference = conference;
	}
	
	public void setBaseURI (String baseURI) {
		this.internalURI = null;
		
		if (Conference.matchesURIPattern(baseURI)) {
			this.internalURI = baseURI + "#";
		}
	}
	
	public String getBaseURI () {
		return this.internalURI;
	}
	
	public void affixURI() {
		this.controller.setBaseURI(this.internalURI);
	}
}
