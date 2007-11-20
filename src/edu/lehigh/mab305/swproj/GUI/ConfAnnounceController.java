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
import edu.lehigh.mab305.swproj.Application.*;
import java.util.ArrayList;
import java.util.regex.*;
import org.eclipse.swt.widgets.Tree;
import com.hp.hpl.jena.rdf.model.Model;

public class ConfAnnounceController {

	protected Conference conf;
	protected ConfInfoPaneController infoPaneController;
	protected FulltextPaneController fulltextPaneController;
	protected TopicPaneController topicPaneController;
	protected LocationPaneController locationPaneController;
	protected SeriesPaneController seriesPaneController;
	protected ConfAnnounceWindow window;
	
	public ConfAnnounceController(Conference conf) {
		super();
		this.conf = conf;
	}

	/**
	 * @return the conf
	 */
	public Conference getConf() {
		return conf;
	}

	/**
	 * @param conf the conf to set
	 */
	public void setConf(Conference conf) {
		this.conf = conf;
	}
		
	public synchronized void setConferenceStartDate(String date) {
		if (date != null && this.infoPaneController != null) {
			this.infoPaneController.setConferenceStartDate(date);
			this.conf.setConferenceStartDate(date);
		}
	}
	
	public synchronized void setConferenceEndDate(String date) {
		if (date != null && this.infoPaneController != null) {
			this.infoPaneController.setConferenceEndDate(date);
			this.conf.setConferenceEndDate(date);
		}
	}
	
	public synchronized void setConferencePaperSubmissionDeadline(String date) {
		if (date != null && this.infoPaneController != null) {
			this.infoPaneController.setConferencePaperSubmissionDeadline(date);
			this.conf.setConferencePaperSubmissionDeadline(date);
		}
	}
	
	public synchronized void setConferenceAbstractSubmissionDeadline(String date) {
		if (date != null && this.infoPaneController != null) {
			this.infoPaneController.setConferenceAbstractSubmissionDeadline(date);
			this.conf.setConferenceAbstractSubmissionDeadline(date);
		}
	}
	
	public synchronized void setConferenceName(String name) {
		if (name != null && this.infoPaneController != null) {
			this.infoPaneController.setConferenceName(name);
			this.conf.setConferenceName(name);
		}
	}
	
	public synchronized void setConferenceWebsite(String website) {
		if (website != null && this.infoPaneController != null) {
			this.infoPaneController.setConferenceWebsite(website);
			this.conf.setConferenceWebsite(website);
		}
	}
	
	public synchronized void setConferenceAddress (String address) {
		if (address != null && this.locationPaneController != null) {
			this.locationPaneController.setConferenceAddress(address);
			this.conf.setAddress(address);
		}
	}
	
	public synchronized void setFulltext(String text) {
		if (text != null && this.fulltextPaneController != null) {
			this.fulltextPaneController.setFulltext(text);
		}
	}
	
	public synchronized void setConferenceDescription (String description) {
		if (description != null && true) {
			this.infoPaneController.setDescription(description);
			this.conf.setConferenceDescription(description);
		}
	}
	
	public synchronized void setBaseURI(String uri) {
		if (Conference.matchesURIPattern(uri)) {
			this.conf.setBaseURI(uri + "#");
			this.infoPaneController.setBaseURI(uri);
		}
	}
	
	 /* Control methods */
	 public synchronized String getConferenceStartDate() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = this.infoPaneController.getConferenceStartDate();
			 if (ret != null && ret.length() > 0) {
				 Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
				 Matcher m = p.matcher(ret);
				 if (!m.matches()) {
					 ret = this.conf.getConferenceStartDate();
				 }
			 }
		 }
		 return ret;
	 }
	 
	 public synchronized String getConferenceEndDate() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = this.infoPaneController.getConferenceEndDate();
			 if (ret != null && ret.length() > 0) {
				 Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
				 Matcher m = p.matcher(ret);
				 if (!m.matches()) {
					 ret = this.conf.getConferenceEndDate();
				 }
			 }
		 }
		 return ret;
	 }
		 
	 public synchronized String getConferencePaperSubmissionDeadline() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = this.infoPaneController.getConferencePaperSubmissionDeadline();
			 if (ret != null && ret.length() > 0) {
				 Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
				 Matcher m = p.matcher(ret);
				 if (!m.matches()) {
					 ret = this.conf.getConferencePaperSubmissionDeadline();
				 }
			 }
		 }
		 return ret;	 
	 }
	
	 public synchronized String getConferenceAbstractSubmissionDeadline() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = this.infoPaneController.getConferenceAbstractSubmissionDeadline();
			 if (ret != null && ret.length() > 0) {
				 Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
				 Matcher m = p.matcher(ret);
				 if (!m.matches()) {
					 ret = this.conf.getConferenceAbstractSubmissionDeadline();
				 }
			 }
		 }
		 return ret;	 
	 }
	 
	 public synchronized String getConferenceName() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = this.infoPaneController.getConferenceName();
		 }
		 return ret;
	 }
		 
	 public synchronized String getConferenceWebsite() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = this.infoPaneController.getConferenceWebsite();
		 }
		 return ret;
	 }

	 public synchronized String getConferenceAddress() {
		 String ret = null;
		 if (this.locationPaneController != null) {
			 ret = this.locationPaneController.getConferenceAddress();
		 }
		 return ret;
	 }
	 
	 public synchronized String getAnnouncementFulltext() {
		 String ret = null;
		 if (this.fulltextPaneController != null) {
			 ret = this.fulltextPaneController.getFulltext();
		 }
		 return ret;
	 }
	 
	 public synchronized String getBaseURI() {
		 String ret = null;
		 if (this.infoPaneController != null) {
			 ret = infoPaneController.getBaseURI();
		 }
		 return ret;
	 }
	
	public synchronized ArrayList<String> getConferenceTopicAreas() {
		ArrayList<String> ret = null;
		if (this.topicPaneController != null) {
			ret = this.topicPaneController.getConferenceTopicAreas(); 
		}
		return ret;
	}
	
	public synchronized ArrayList<String> getConferenceTopicAreaLabels() {
		ArrayList<String> ret = null;
		if (this.topicPaneController != null) {
			ret = this.topicPaneController.getConferenceTopicAreaLabels(); 
		}
		return ret;
	}
	 
	public synchronized ArrayList<String> getLocalTopicOntologies() {
		System.out.println("This shouldn't happen!");
		return new ArrayList<String>();
	}
	
	public synchronized String getConferenceDescription () {
		String ret = null;
		if (this.topicPaneController != null) {
			ret = this.infoPaneController.getDescription(); 
		}
		return ret;
	}
	
	public synchronized String getLocationURI() {
		String ret = null;
		if (this.locationPaneController != null) {
			ret = this.locationPaneController.getConferenceLocationURI(); 
		}
		return ret;
	}
	
	public synchronized String getSeriesBaseURI() {
		String ret = null;
		if (this.infoPaneController != null) {
			ret = this.seriesPaneController.getSeriesBaseURI(); 
		}
		return ret;
	}

	/**
	 * @return the infoPaneController
	 */
	public ConfInfoPaneController getInfoPaneController() {
		return infoPaneController;
	}

	/**
	 * @param infoPaneController the infoPaneController to set
	 */
	public void setInfoPaneController(ConfInfoPaneController infoPaneController) {
		this.infoPaneController = infoPaneController;
	}

	/**
	 * @return the fulltextPaneController
	 */
	public FulltextPaneController getFulltextPaneController() {
		return fulltextPaneController;
	}

	/**
	 * @param fulltextPaneController the fulltextPaneController to set
	 */
	public void setFulltextPaneController(
			FulltextPaneController fulltextPaneController) {
		this.fulltextPaneController = fulltextPaneController;
	}

	/**
	 * @return the topicPaneController
	 */
	public TopicPaneController getTopicPaneController() {
		return topicPaneController;
	}

	/**
	 * @param topicPaneController the topicPaneController to set
	 */
	public void setTopicPaneController(TopicPaneController topicPaneController) {
		this.topicPaneController = topicPaneController;
	}

	/**
	 * @return the locationPaneController
	 */
	public LocationPaneController getLocationPaneController() {
		return locationPaneController;
	}

	/**
	 * @param locationPaneController the locationPaneController to set
	 */
	public void setLocationPaneController(
			LocationPaneController locationPaneController) {
		this.locationPaneController = locationPaneController;
	}
	
	public synchronized void setLocation(Location location) {
		if (location != null) {
			this.locationPaneController.setLocation(location);
		}
	}
	
	public synchronized void setLocationURI(String location) {
		if (this.locationPaneController != null) {
			this.locationPaneController.setConferenceLocation(location); 
		}
	}

	public ConfAnnounceWindow getWindow() {
		return window;
	}

	public void setWindow(ConfAnnounceWindow window) {
		this.window = window;
	}
	
	public synchronized void setWindowPublishable(boolean publishable) {
		this.window.setPublishable(publishable);
	}
	
	public synchronized boolean getWindowPublishable() {
		return this.window.getPublishable();
	}

	public synchronized String getSeriesName() {
		return seriesPaneController.getSeriesTitle();
	}
	
	public synchronized void setSeriesName(String name) {
		seriesPaneController.setSeriesTitle(name);
	}
	
	public synchronized String getSeriesOccursIn() {
		return seriesPaneController.getOccursIn();
	}
	
	public synchronized void setSeriesOccursIn(String occursIn) {
		seriesPaneController.setOccursIn(occursIn);
	}
	
	public synchronized String getSeriesSubmissionsDueIn() {
		return seriesPaneController.getSubmissionsDueIn();
	}
	
	public synchronized void setSeriesSubmissionsDueIn(String submissionsDueIn) {
		seriesPaneController.setSubmissionsDueIn(submissionsDueIn);
	}
	
	public synchronized ArrayList<String> getSeriesConstituentConferences() {
		return seriesPaneController.getConstituentConfs();
	}
	
	public synchronized void setSeriesConstituentConferences(ArrayList<String> confs) {
		seriesPaneController.setConstituentConfs(confs);
	}
	
	public synchronized ArrayList<String> getSeriesTopicAreas() {
		return seriesPaneController.getTopicAreas();
	}
	
	public synchronized void setSeriesTopicAreas(ArrayList<String> topicAreas) {
		seriesPaneController.setTopicAreas(topicAreas);
	}
	
	public SeriesPaneController getSeriesPaneController() {
		return seriesPaneController;
	}

	public void setSeriesPaneController(SeriesPaneController seriesPaneController) {
		this.seriesPaneController = seriesPaneController;
	}
	
	public synchronized void setSeriesURI(String uri) {
		this.seriesPaneController.setSeriesURI(uri);
	}
	
	public synchronized void addConferenceTopicArea(String topic) {
		this.topicPaneController.addConferenceTopicArea(topic);
	}
	
	public synchronized void setSeriesWebsite(String website) {
		this.seriesPaneController.setWebsite(website);
	}
	
	public synchronized String getSeriesWebsite() {
		return this.seriesPaneController.getWebsite();
	}
	
	public Tree getSeriesTree() {
		SeriesPaneController s = this.seriesPaneController;
		return s.getSeriesTree();
	}
	
	public Model getCompositeTopicModel() {
		return topicPaneController.getCompositeTopicModel();
	}
	
	public void refreshNew() {
		conf = new Conference();
		SettingsManager.setOpenedModel(null);
		infoPaneController.refreshNew();
		fulltextPaneController.refreshNew();
		topicPaneController.refreshNew();
		locationPaneController.refreshNew();
		seriesPaneController.refreshNew();
	}
}
