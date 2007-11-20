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

import edu.lehigh.mab305.swproj.Application.TimeUtil;
import edu.lehigh.mab305.swproj.ConferenceModel.Conference;
import java.util.regex.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.text.ParseException;

public class EasyConfController {

	protected Conference conference;
	protected ConfAnnounceController windowController;

	public EasyConfController(ConfAnnounceController cont) {
		super();
		this.windowController = cont;
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
		this.windowController.setFulltext(fullText);
	}
	
	public String getFullText() {
		return this.conference.getAnnouncementFullText();
	}
	
	public void setConferenceName(String name) {
		this.windowController.setConferenceName(name);
	}
	
	public void setConferenceWebsite(String name) {
		this.windowController.setConferenceWebsite(name);
	}
	
	public void setConferenceAddress(String address) {
		this.windowController.setConferenceAddress(address);
	}
	
	public void setConferenceDescription(String description) {
		this.windowController.setConferenceDescription(description);
	}
	
	public void setConferenceStartDate(String date) {	
		this.windowController.setConferenceStartDate(TimeUtil.parseDate(date));
	}
	
	public void setConferenceEndDate(String date) {
		this.windowController.setConferenceEndDate(TimeUtil.parseDate(date));
	}
	
	public void setConferencePaperDate(String date) {
		this.windowController.setConferencePaperSubmissionDeadline(TimeUtil.parseDate(date));
	}
	
	public void setConferenceAbstractDate(String date) {
		this.windowController.setConferenceAbstractSubmissionDeadline(TimeUtil.parseDate(date));
	}
	
	public void setConferenceDates(String dateStr) {
		//Pattern p = Pattern.compile("^(\\w+)\\.?\\s+(\\d+)\\s*-\\s*(\\d+)\\s*,+(\\d+)");
		Pattern p = Pattern.compile("\\s*(\\w+)\\s+(\\d+)\\s*-\\s*(\\d+)\\s*,\\s*(\\d+)\\s*");
		Matcher m = p.matcher(dateStr);
		DateFormat df = DateFormat.getDateInstance();
		
		if (m.matches()) {
			Calendar cal1, cal2;
			String date1, date2;
			StringBuffer outDate1 = new StringBuffer(), outDate2;
			date1 = m.group(1) + " " + m.group(2) + ", " + m.group(4);
			date2 = m.group(1) + " " + m.group(3) + ", " + m.group(4);
			cal1 = Calendar.getInstance();
			cal2 = Calendar.getInstance();
			try {
				cal1.setTime(df.parse(date1));
				cal2.setTime(df.parse(date2));
				outDate1.append(cal1.get(Calendar.YEAR));
				outDate1.append("-");
				outDate2 = new StringBuffer(outDate1.toString());
				outDate1.append(cal1.get(Calendar.MONTH) + 1);
				outDate1.append("-");
				outDate1.append(cal1.get(Calendar.DAY_OF_MONTH));
				outDate2.append(cal2.get(Calendar.MONTH) + 1);
				outDate2.append("-");
				outDate2.append(cal2.get(Calendar.DAY_OF_MONTH));
				this.windowController.setConferenceStartDate(outDate1.toString());
				this.windowController.setConferenceEndDate(outDate2.toString());
			}
			catch (ParseException pe) {
				
			}
			
		}
		else { System.out.println("uh uh + " + dateStr); }
	}
	
	public void setBaseURI(String uri) {
		this.windowController.setBaseURI(uri);
	}
}
