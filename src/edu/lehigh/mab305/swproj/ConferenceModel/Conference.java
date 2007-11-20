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
import java.util.regex.*;
import edu.lehigh.mab305.swproj.Application.URLConstants;

public class Conference {

	public  static final String confURI = URLConstants.CONFERENCE_ONTOLOGY + "#";
	
	public static enum ConferenceDate {CONFERENCE_START_DATE, CONFERENCE_END_DATE, 
		CONFERENCE_ABSTRACT_DATE, CONFERENCE_PAPER_DATE};
	
	public static final String CONFERENCE = confURI + "Conference";
	public static final String CONFERENCE_START_DATE_URI = confURI + "conferenceStartDate";
	public static final String CONFERENCE_END_DATE_URI = confURI + "conferenceEndDate";
	public static final String CONFERENCE_PAPER_DATE_URI = confURI + "conferencePaperSubmissionDeadline";
	public static final String CONFERENCE_ABSTRACT_DATE_URI = confURI + "conferenceAbstractSubmissionDeadline";
	public static final String ANNOUNCEMENT_FULLTEXT_URI = confURI + "announcementFullText";
	public static final String CONFERENCE_TOPIC_AREA_URI = confURI + "conferenceTopicArea";
	public static final String CONFERENCE_DESCRIPTION_URI = confURI + "conferenceDescription";
	public static final String CONFERENCE_WEBSITE_URI = confURI + "conferenceWebsite";
	public static final String CONFERENCE_TITLE_URI = confURI + "conferenceTitle";
	public static final String CONFERENCE_ADDRESS_URI = confURI + "conferenceStreetAddress";
	public static final String IS_IN_URI = confURI + "isIn";
	public static final String CONFERENCE_LOCATED_URI = confURI + "conferenceLocatedIn";
	public static final String PART_OF_SERIES_URI = confURI + "partOfSeries";
	public static final String CITY = confURI + "City";
	public static final String STATE = confURI + "State";
	public static final String PROVINCE = confURI + "Province";
	public static final String COUNTRY = confURI + "Country";
	public static final String LOCATION = confURI + "Location";
	public static final String TOPIC = confURI + "Topic";
	public static final String SUBTOPIC_OF = confURI + "subTopicOf";
	
	protected ArrayList<String> conferenceTopicArea = null;
	protected ArrayList<String> topicOntologyURIs = null, locationOntologyURIs = null;
	protected String conferenceStartDate = null;
	protected String conferenceEndDate = null;
	protected String conferencePaperSubmissionDeadline = null;
	protected String conferenceAbstractSubmissionDeadline = null;
	protected String baseURI = null;
	protected String announcementFullText = null;
	protected String conferenceName = null;
	protected String conferenceName_URIFriendly = null;
	protected String conferenceDescription = null;
	protected String conferenceWebsite = null;
	protected String city = null;
	protected String city_URIFriendly = null;
	protected String state = null;
	protected String state_URIFriendly = null;
	protected String province = null;
	protected String province_URIFriendly = null;
	protected String country = null;
	protected String country_URIFriendly = null;
	protected String address = null;
	protected String seriesURI = null;
	protected String locatedInURI = null;
	
	protected ConferenceSeries series = null;
	protected Location location = null;
	
	public Conference () {
		this.conferenceTopicArea = new ArrayList<String>();
		this.topicOntologyURIs = new ArrayList<String>();
		this.locationOntologyURIs = new ArrayList<String>();
	}
	
	public static boolean matchesURIPattern(String str) {
		boolean ret = false;
		Pattern p = Pattern.compile("http://[a-zA-Z0-9_/\\~%\\-\\.]+");
		Matcher m = p.matcher(str);
		
		if (m.matches()) {
			ret = true;
		}
		return ret;
	}
	
	public static String makeURIFriendly(String str) {
		Pattern p = Pattern.compile(" ");
		Matcher m = p.matcher(str);
		str = m.replaceAll("_");
		
		// two-stage
		p = Pattern.compile("[^\\w_]");
		m = p.matcher(str);
		str = m.replaceAll("");
		
		return str;
	}
	
	public void addTopicOntologyURI(String uri) {
		this.topicOntologyURIs.add(uri);
	}
	
	public void removeTopicOntologyURI(String uri) {
		this.topicOntologyURIs.remove(uri);
	}
	
	public ArrayList<String> getTopicOntologyURIs() {
		return this.topicOntologyURIs;
	}
	
	public void addTopicOntologyURIs(ArrayList<String> topicOntologyURIs) {
		this.topicOntologyURIs.addAll(topicOntologyURIs);
	}
	
	/**
	 * @return the conferenceTopicArea
	 */
	public ArrayList<String> getConferenceTopicAreas() {
		return conferenceTopicArea;
	}

	/**
	 * @param conferenceTopicArea the conferenceTopicArea to set
	 */
	public void setConferenceTopicArea(ArrayList<String> conferenceTopicArea) {
		this.conferenceTopicArea = conferenceTopicArea;
	}

	/**
	 * @return the conferenceEndDate
	 */
	public String getConferenceEndDate() {
		return conferenceEndDate;
	}

	/**
	 * @param conferenceEndDate the conferenceEndDate to set
	 */
	public void setConferenceEndDate(String conferenceEndDate) {
		conferenceEndDate = removeBrackets(conferenceEndDate);
		this.conferenceEndDate = conferenceEndDate;
	}

	/**
	 * @return the conferencePaperSubmissionDeadline
	 */
	public String getConferencePaperSubmissionDeadline() {
		return conferencePaperSubmissionDeadline;
	}

	/**
	 * @param conferencePaperSubmissionDeadline the conferencePaperSubmissionDeadline to set
	 */
	public void setConferencePaperSubmissionDeadline(
			String conferencePaperSubmissionDeadline) {
		conferencePaperSubmissionDeadline = removeBrackets(conferencePaperSubmissionDeadline);
		this.conferencePaperSubmissionDeadline = conferencePaperSubmissionDeadline;
	}

	/**
	 * @return the conferenceStartDate
	 */
	public String getConferenceStartDate() {
		return conferenceStartDate;
	}

	/**
	 * @param conferenceStartDate the conferenceStartDate to set
	 */
	public void setConferenceStartDate(String conferenceStartDate) {
		conferenceStartDate = removeBrackets(conferenceStartDate);
		this.conferenceStartDate = conferenceStartDate;
	}

	/**
	 * @return the baseURI
	 */
	public String getBaseURI() {
		return baseURI;
	}

	/**
	 * @param baseURI the baseURI to set
	 */
	public void setBaseURI(String baseURI) {
		baseURI = removeBrackets(baseURI);
		this.baseURI = baseURI;
	}

	/**
	 * @return the announcementFullText
	 */
	public String getAnnouncementFullText() {
		return announcementFullText;
	}

	/**
	 * @param announcementFullText the announcementFullText to set
	 */
	public void setAnnouncementFullText(String announcementFullText) {
		announcementFullText = removeBrackets(announcementFullText);
		this.announcementFullText = announcementFullText;
	}

	/**
	 * @return the conferenceName
	 */
	public String getConferenceName() {
		return conferenceName;
	}

	/**
	 * @param conferenceName the conferenceName to set
	 */
	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
		this.conferenceName = removeBrackets(this.conferenceName);
		this.conferenceName_URIFriendly = makeURIFriendly(this.conferenceName);
	}

	/**
	 * @return the conferenceDescription
	 */
	public String getConferenceDescription() {
		return conferenceDescription;
	}

	/**
	 * @param conferenceDescription the conferenceDescription to set
	 */
	public void setConferenceDescription(String conferenceDescription) {
		conferenceDescription = removeBrackets(conferenceDescription);
		this.conferenceDescription = conferenceDescription;
	}

	/**
	 * @return the conferenceWebsite
	 */
	public String getConferenceWebsite() {
		return conferenceWebsite;
	}

	/**
	 * @param conferenceWebsite the conferenceWebsite to set
	 */
	public void setConferenceWebsite(String conferenceWebsite) {
		conferenceWebsite = removeBrackets(conferenceWebsite);
		this.conferenceWebsite = conferenceWebsite;
	}

	public String getConferenceName_URIFriendly() {
		return conferenceName_URIFriendly;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	public String getCity_URIFriendly() {
		return this.city_URIFriendly;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		city = removeBrackets(city);
		this.city = city;
		this.city_URIFriendly = makeURIFriendly(this.city);
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	public String getState_URIFriendly() {
		return this.state_URIFriendly;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		state = removeBrackets(state);
		this.state = state;
		this.state_URIFriendly = makeURIFriendly(this.state);
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	public String getCountry_URIFriendly() {
		return this.country_URIFriendly;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		country = removeBrackets(country);
		this.country = country;
		this.country_URIFriendly = makeURIFriendly(this.country);
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	public String getProvince_URIFriendly() {
		return this.province_URIFriendly;
	}
	
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		province = removeBrackets(province);
		this.province = province;
		this.province_URIFriendly = makeURIFriendly(this.province);
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		address = removeBrackets(address);
		this.address = address;
	}

	/**
	 * @return the seriesURI
	 */
	public String getSeriesURI() {
		return seriesURI;
	}

	/**
	 * @param seriesURI the seriesURI to set
	 */
	public void setSeriesURI(String seriesURI) {
		seriesURI = removeBrackets(seriesURI);
		this.seriesURI = seriesURI;
	}

	/**
	 * @return the locatedInURI
	 */
	public String getLocatedInURI() {
		return locatedInURI;
	}

	/**
	 * @param locatedInURI the locatedInURI to set
	 */
	public void setLocatedInURI(String locatedInURI) {
		locatedInURI = removeBrackets(locatedInURI);
		this.locatedInURI = locatedInURI;
	}

	/**
	 * @return the locationImports
	 */
	public ArrayList<String> getLocationOntologyURIs() {
		return this.locationOntologyURIs;
	}

	/**
	 * @param locationImports the locationImports to set
	 */
	public void setLocationOntologyURIs(ArrayList<String> locationImports) {
		if (locationImports != null) {
			this.locationOntologyURIs = locationImports;
		}
	}
		
	/**
	 * @param locationOntologyURIs the locationOntologyURIs to set
	 */
	public void addLocationOntologyURI(String locationOntologyURIs) {
		this.locationOntologyURIs.add(locationOntologyURIs);
	}
	
	public void addLocationOntologyURIs(ArrayList<String> locationOntologyURIs) {
		this.locationOntologyURIs.addAll(locationOntologyURIs);
	}

	/**
	 * @return the series
	 */
	public ConferenceSeries getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(ConferenceSeries series) {
		this.series = series;
	}

	/**
	 * @return the conferenceAbstractSubmissionDeadline
	 */
	public String getConferenceAbstractSubmissionDeadline() {
		return conferenceAbstractSubmissionDeadline;
	}

	/**
	 * @param conferenceAbstractSubmissionDeadline the conferenceAbstractSubmissionDeadline to set
	 */
	public void setConferenceAbstractSubmissionDeadline(
			String conferenceAbstractSubmissionDeadline) {
		conferenceAbstractSubmissionDeadline = removeBrackets(conferenceAbstractSubmissionDeadline);
		this.conferenceAbstractSubmissionDeadline = conferenceAbstractSubmissionDeadline;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	protected String removeBrackets(String s) {
		String ret = null;
		if (s != null) {
			// These characters will mess up XML parsers
			ret = s.replaceAll("<", "");
			ret = s.replaceAll(">", "");
		}
		return ret;
	}
}
