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

/**
 * Controller for the wrapper widget DateSelectionWidget, which wraps around an
 * SWTCalendar. The type of domain-specific date is set upon object instantiation,
 * and connects 
 *  
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 * @see org.fafada.swtcalendar.SWTCalendar
 *
 */
public class DateSelectionController {

	protected Conference conf;
	protected Conference.ConferenceDate dateField;
	protected boolean isOpen;

	public DateSelectionController(Conference conf, Conference.ConferenceDate dateField) {
		super();
		this.conf = conf;
		this.isOpen = false;
		this.dateField = dateField;
	}

	/**
	 * @return the conf
	 */
	public Conference getConf() {
		return conf;
	}

	public String getDate() {
		String retDate = "";
	
		switch (this.dateField) { 
		case CONFERENCE_START_DATE:
			retDate = this.conf.getConferenceStartDate();
			break;
		case CONFERENCE_END_DATE:
			retDate = this.conf.getConferenceEndDate();
			break;
		case CONFERENCE_PAPER_DATE:
			retDate = this.conf.getConferencePaperSubmissionDeadline();
			break;
		case CONFERENCE_ABSTRACT_DATE:
			break;
		}
		return retDate;
	}

	/**
	 * @param dateField the dateField to set
	 */
	public void setDate(String date) {
		switch(this.dateField){
		case CONFERENCE_START_DATE:
			this.conf.setConferenceStartDate(date);
			break;
		case CONFERENCE_END_DATE:
			this.conf.setConferenceEndDate(date);
			break;
		case CONFERENCE_PAPER_DATE:
			this.conf.setConferencePaperSubmissionDeadline(date);
			break;
		case CONFERENCE_ABSTRACT_DATE:
			this.conf.setConferenceAbstractSubmissionDeadline(date);
			break;
		}
	}

	/**
	 * @return the dateField
	 */
	public Conference.ConferenceDate getDateField() {
		return dateField;
	}

	/**
	 * @param dateField the dateField to set
	 */
	public void setDateField(Conference.ConferenceDate dateField) {
		this.dateField = dateField;
	}

	/**
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * @param isOpen the isOpen to set
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
