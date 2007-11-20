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
public class ConfInfoPaneController {

	protected Conference conf;
	protected ConfAnnounceController controller;
	protected TestPane pane;
	
	public ConfInfoPaneController(Conference conf) {
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
	
	/* Set conference vars and update the UI */
	public void setPane (TestPane window) {
		this.pane = window;
	}
	
	public String getConferenceStartDate() {
		String ret = null;
		if (this.pane.getConferenceStartDate() != null) {
			ret = this.pane.getConferenceStartDate();
		}
		return ret;
	}
	
	public String getConferenceEndDate() {
		String ret = null;
		if (this.pane.getConferenceEndDate() != null) {
			ret = this.pane.getConferenceEndDate();
		}
		return ret;
	}
	
	public String getConferencePaperSubmissionDeadline() {
		String ret = null;
		if (this.pane.getConferencePaperSubmissionDeadline() != null){
			ret = this.pane.getConferencePaperSubmissionDeadline();
		}
		return ret;
	}
	
	public String getConferenceAbstractSubmissionDeadline() {
		String ret = null;
		if (this.pane.getConferenceAbstractSubmissionDeadline() != null){
			ret = this.pane.getConferenceAbstractSubmissionDeadline();
		}
		return ret;
	}
	
	public String getConferenceName() {
		String ret = null;
		if (this.pane.getConferenceName() != null) {
			ret = this.pane.getConferenceName();
		}
		return ret;
	}
	
	public String getConferenceWebsite() {
		String ret = null;
		if (this.pane.getConferenceWebsite() != null) {
			ret = this.pane.getConferenceWebsite();
		}
		return ret;
	}
	
	public String getBaseURI() {
		String ret = null;
		if (this.pane.getBaseURI() != null) {
			ret = this.pane.getBaseURI();
		}
		return ret;
	}
	
	public String getDescription() {
		String ret = null;
		if (this.pane.getDescription() != null) {
			ret = this.pane.getDescription();
		}
		return ret;
	}
	
	public void setConferenceStartDate(String date) {
		this.pane.setConferenceStartDate(date);
	}
	
	public void setConferenceEndDate(String date) {
		this.pane.setConferenceEndDate(date);
	}
	
	public void setConferencePaperSubmissionDeadline(String date) {
		this.pane.setConferencePaperSubmissionDeadline(date);
	}
	
	public void setConferenceAbstractSubmissionDeadline(String date) {
		this.pane.setConferenceAbstractSubmissionDeadline(date);
	}
	
	public void setConferenceName(String name) {
		this.pane.setConferenceName(name);
	}
	
	public void setConferenceWebsite(String website) {
		this.pane.setConferenceWebsite(website);
	}

	public void setBaseURI(String uri) {
		if (Conference.matchesURIPattern(uri)) {
			this.conf.setBaseURI(uri + "#");
			this.pane.setBaseURI(uri);
		}
	}
	
	public void setDescription (String description) {
		this.pane.setDescription(description);
	}
	
	public void setPublishable(boolean publishable) {
		if (this.controller != null) {
			this.controller.setWindowPublishable(publishable);
		}
	}
	
	public boolean getPublishable() {
		boolean ret = false;
		if (this.controller != null && this.controller.getWindowPublishable()) {
			ret = true;
		}
		return ret;
	}

	public ConfAnnounceController getController() {
		return controller;
	}

	public void setController(ConfAnnounceController controller) {
		this.controller = controller;
	}
	
	public void refreshNew() {
		this.pane.resetInterface();
	}
}
