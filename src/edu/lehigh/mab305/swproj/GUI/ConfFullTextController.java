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

import edu.lehigh.mab305.swproj.ConferenceModel.Conference;

public class ConfFullTextController {

	Conference conference;

	public ConfFullTextController(Conference conference) {
		super();
		this.conference = conference;
	}

	/**
	 * @return the conference
	 */
	public Conference getConference() {
		return conference;
	}

	/**
	 * @param conference the conference to set
	 */
	public void setConference(Conference conference) {
		this.conference = conference;
	}
	
	public void setFullText(String fullText) {
		this.conference.setAnnouncementFullText(fullText);
	}
	
	public String getFullText() {
		return this.conference.getAnnouncementFullText();
	}
	
}
