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
package edu.lehigh.mab305.swproj.ConferenceModel;

import java.util.ArrayList;

public class ConferenceSeries {
	
	public static final String SERIES_TYPE_URI = Conference.confURI + "ConferenceSeries";
	public static final String SUBMISSIONS_DUE_IN_URI = Conference.confURI + "seriesSubmissionsDueIn";
	public static final String OCCURS_IN_URI = Conference.confURI + "seriesOccursIn";
	public static final String CONFERENCE_INSTANCE = Conference.confURI + "seriesConferenceInstance";
	
	protected ArrayList<String> constituentConferences = null, topicAreas = null;
	protected String submissionsDueIn = null, occursIn = null, seriesTitle = null,
					baseURI = null, fullURI = null, website = null;
	
	public ConferenceSeries () {
		constituentConferences = new ArrayList<String>();
		topicAreas = new ArrayList<String>();
	}
	
	public ConferenceSeries(String uri) {
		this.fullURI = uri;
		this.baseURI = uri.substring(0, uri.indexOf("#"));
	}

	/**
	 * @return the constituentConferences
	 */
	public ArrayList<String> getConstituentConferences() {
		return constituentConferences;
	}

	/**
	 * @param constituentConferences the constituentConferences to set
	 */
	public void setConstituentConferences(ArrayList<String> constituentConferences) {
		this.constituentConferences = constituentConferences;
	}

	/**
	 * @return the submissionsDueIn
	 */
	public String getSubmissionsDueIn() {
		return submissionsDueIn;
	}

	/**
	 * @param submissionsDueIn the submissionsDueIn to set
	 */
	public void setSubmissionsDueIn(String submissionsDueIn) {
		this.submissionsDueIn = submissionsDueIn;
	}

	/**
	 * @return the occursIn
	 */
	public String getOccursIn() {
		return occursIn;
	}

	/**
	 * @param occursIn the occursIn to set
	 */
	public void setOccursIn(String occursIn) {
		this.occursIn = occursIn;
	}

	/**
	 * @return the seriesTitle
	 */
	public String getSeriesTitle() {
		return seriesTitle;
	}

	/**
	 * @param seriesTitle the seriesTitle to set
	 */
	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
	}

	/**
	 * @return the baseURI
	 */
	public String getBaseURI() {
		return baseURI;
	}

	public String getFullURI() {
		String ret;
		if (this.fullURI != null) {
			ret = this.fullURI;
		}
		else {
			if (this.baseURI.contains("#") 
					&& this.baseURI.indexOf("#") == this.baseURI.length() - 1) {
				ret = this.baseURI + Conference.makeURIFriendly(this.seriesTitle);
			}
			else {
				ret = this.baseURI + "#" + Conference.makeURIFriendly(this.seriesTitle);
			}
		}
		return ret;
	}
	
	/**
	 * @param baseURI the baseURI to set
	 */
	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	/**
	 * @return the topicAreas
	 */
	public ArrayList<String> getTopicAreas() {
		return topicAreas;
	}

	/**
	 * @param topicAreas the topicAreas to set
	 */
	public void setTopicAreas(ArrayList<String> topicAreas) {
		this.topicAreas = topicAreas;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
}
