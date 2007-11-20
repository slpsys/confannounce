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
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;

public class LocationPaneController {
	protected Conference conf;
	protected LocationPane pane;
	protected ConfAnnounceController confControl;
	
	public LocationPaneController(ConfAnnounceController confControl) {
		super();
		this.confControl = confControl;
		this.conf = this.confControl.getConf();
	}

	/**
	 * @return the conf
	 */
	public Conference getConf() {
		return conf;
	}

	public void setPane(LocationPane pane) {
		this.pane = pane;
	}
	
	/**
	 * @param conf the conf to set
	 */
	public void setConf(Conference conf) {
		this.conf = conf;
	}
	
	public void setConferenceLocation(String location) {
		this.pane.setConferenceLocation(location);
	}
	
	public String getConferenceLocationURI() {
		//return this.conf.getConferenceTopicArea();
		return this.pane.getConferenceLocationURI();
	}
	
	public Location getNewLocation() {
		return this.pane.getConferenceLocation();
	}
	
	public ArrayList<String> getLocalLocationOntologies() {
		return this.pane.getLocationOntologies();
	}

	public ArrayList<Model> getLocationModels() {
		return this.pane.getLocationModels();
	}
	
	public ArrayList<String> getLocationOntologies() {
		return this.pane.getLocationOntologyURIs();
	}
	
	public void setConferenceAddress(String address) {
		this.pane.setConferenceAddress(address);
	}
	
	public String getConferenceAddress() {
		return this.pane.getConferenceAddress();
	}
	
	public void refreshNew() {
		this.pane.resetInterface();
	}

	/**
	 * @return the confControl
	 */
	public ConfAnnounceController getController() {
		return confControl;
	}

	/**
	 * @param confControl the confControl to set
	 */
	public void setController(ConfAnnounceController confControl) {
		this.confControl = confControl;
	}
	
	public void addOntology(String uri) {
		this.pane.addOntology(uri);
	}
	
	public void setLocation(Location location) {
		this.pane.setNewLocation(location);
	}
	
	public void clearOntologies() {
		this.pane.clearOntologies();
	}
	
	public void filterOntologies() {
		this.pane.filterOntologies();
	}
}
