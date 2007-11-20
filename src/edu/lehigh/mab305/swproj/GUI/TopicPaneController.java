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
import org.eclipse.swt.widgets.Tree;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;

public class TopicPaneController {
	protected Conference conf;
	protected TopicPane pane;
	protected ConfAnnounceController confControl;
	
	public TopicPaneController(ConfAnnounceController confControl) {
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

	public void setPane(TopicPane pane) {
		this.pane = pane;
	}
	
	/**
	 * @param conf the conf to set
	 */
	public void setConf(Conference conf) {
		this.conf = conf;
	}
	
	public void setConferenceTopicAreas(ArrayList<String> topicURIs) {
		this.conf.setConferenceTopicArea(topicURIs);
	}
	
	public ArrayList<String> getConferenceTopicAreas() {
		//return this.conf.getConferenceTopicArea();
		return this.pane.getConferenceTopicAreas();
	}
	
	public ArrayList<String> getConferenceTopicAreaLabels() {
		return this.pane.getConferenceTopicLabels();
	}
	
	public ArrayList<Model> getTopicModels() {
		return this.pane.getTopicModels();
	}
	
	public Model getCompositeTopicModel() {
		return this.pane.getCompositeTopicModel();
	}
	
	public ArrayList<String> getTopicOntologies() {
		return this.pane.getTopicOntologies();
	}
	
	public void addConferenceTopicArea(String topic) {
		this.pane.addConferenceTopicArea(topic);
	}
	
	public void addOntology(String uri) {
		this.pane.addOntology(uri);
	}
	
	public Tree getSeriesTree() {
		return this.confControl.getSeriesTree();
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
	public void setController(ConfAnnounceController controller) {
		this.confControl = controller;
	}
	
	public void clearOntologies() {
		this.pane.clearOntologies();
	}
	
	public void filterOntologies() {
		this.pane.filterOntologies();
	}
	
	public void clearSeriesTree() {
		this.confControl.getSeriesPaneController().clearTree();
	}
}
