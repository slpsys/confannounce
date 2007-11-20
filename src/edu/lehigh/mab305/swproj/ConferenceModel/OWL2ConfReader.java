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

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

import edu.lehigh.mab305.swproj.Application.Progress;
import edu.lehigh.mab305.swproj.Application.SettingsManager;

import com.hp.hpl.jena.rdf.model.*;
//import com.hp.hpl.jena.graph.*;
//import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.vocabulary.*;

public class OWL2ConfReader extends Observable implements Runnable {

	protected Conference outConf = null;
	protected FileInputStream fs = null;
	protected Model model = null;
	protected Resource confResource = null, ontResource = null;
	
	public OWL2ConfReader() {
		super();
	}
	
	public void loadConference(String infile) throws FileNotFoundException {
		this.fs = new FileInputStream(infile);
		this.outConf = new Conference();

		(new Thread(this)).start();
	}
	
	public void notifyObservers(Object e) {
		this.setChanged();
		super.notifyObservers(e);
	}
	
	protected void buildConference() { 
		this.model = ModelFactory.createDefaultModel();
		
		// Set up progress bar window
		this.notifyObservers(new Progress("Loading model from Ontology... " 
				+ "(This may take a minute)", 20));
		
		this.model.read(fs, null);
		
		// Load this model into the Application-wide SettingsManager.
		SettingsManager.setOpenedModel(this.model);
		
		if (!getConfBasics()) {
			OWL2ConfException e = null;
			e = new OWL2ConfException("Ontology does not contain a Conference object.");

			this.notifyObservers(e);
			return;
		}
		
		getConfOptionals();
		this.notifyObservers(this.outConf);
	}
	
	protected boolean getConfBasics() {
		boolean hasConference = false;
		ResIterator res = this.model.listSubjectsWithProperty(RDF.type, 
					ResourceFactory.createResource(Conference.CONFERENCE));
		ResIterator ontRes= this.model.listSubjectsWithProperty(RDF.type, OWL.Ontology);
		NodeIterator niter;
		RDFNode n;
		Resource r;
		String uri; 
		
		this.notifyObservers(new Progress("Extracting basic properties... ", 60));
		if (!res.hasNext()) { return hasConference; }
		
		r = res.nextResource();
		uri = r.getURI();
		//Keep a Resource copy of the full Conference name, then strip out the base URI
		this.confResource = ResourceFactory.createResource(uri);
		
		uri = uri.substring(0, uri.indexOf("#"));
		this.outConf.setBaseURI(uri);
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_TITLE_URI));
		
		if (!niter.hasNext()) { return hasConference; }
		
		n = niter.nextNode();
		this.outConf.setConferenceName(n.toString());
		
		if (ontRes.hasNext()) {
			this.ontResource = ontRes.nextResource();
		}
		hasConference = true;
		return hasConference;
	}
	
	protected void getConfOptionals() {
		NodeIterator niter;
		ResIterator riter;
		Resource location;
		ArrayList<String> list;
		String series = null;
		boolean hasTopics = false;
		
		// This really shouldn't happen.
		if (this.confResource == null) { return; }
		
		this.notifyObservers(new Progress("Loading optional properties... ", 85));
		// Query the properties and add them to the Conference object.
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_ADDRESS_URI));
		if (niter.hasNext()) {
			this.outConf.setAddress(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_START_DATE_URI));
		if (niter.hasNext()) {
			this.outConf.setConferenceStartDate(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_PAPER_DATE_URI));
		if (niter.hasNext()) {
			this.outConf.setConferencePaperSubmissionDeadline(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_ABSTRACT_DATE_URI));
		if (niter.hasNext()) {
			this.outConf.setConferenceAbstractSubmissionDeadline(niter.nextNode().toString());
		}

		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_END_DATE_URI));
		if (niter.hasNext()) {
			this.outConf.setConferenceEndDate(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_WEBSITE_URI));
		if (niter.hasNext()) {
			this.outConf.setConferenceWebsite(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_DESCRIPTION_URI));
		if (niter.hasNext()) {
			this.outConf.setConferenceDescription(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.ANNOUNCEMENT_FULLTEXT_URI));
		if (niter.hasNext()) {
			this.outConf.setAnnouncementFullText(niter.nextNode().toString());
		}	
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_LOCATED_URI));
		if (niter.hasNext()) {
			this.outConf.setLocatedInURI(niter.nextNode().toString());
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.CONFERENCE_TOPIC_AREA_URI));
		list = new ArrayList<String>();
		while (niter.hasNext()) {
			  list.add(niter.nextNode().toString());
			  hasTopics = true;
		}
		this.outConf.setConferenceTopicArea(list);
		
		if (hasTopics && this.ontResource != null) {
			ArrayList<String> imports = new ArrayList<String>();
			niter = this.model.listObjectsOfProperty(this.ontResource, OWL.imports);
			while (niter.hasNext()) {
				imports.add(niter.nextNode().toString());
			}
			this.outConf.addLocationOntologyURIs(imports);
			this.outConf.addTopicOntologyURIs(imports);
		}
		
		niter = this.model.listObjectsOfProperty(this.confResource, ResourceFactory.createProperty(Conference.PART_OF_SERIES_URI));
		if (niter.hasNext()) {
			series = niter.nextNode().toString();
			this.outConf.setSeriesURI(series);
		}
		
		if (series != null && series.length() > 0) {
			Resource seriesInd = this.model.getResource(series);
			if (seriesInd != null) {
				ConferenceSeries s = new ConferenceSeries(series);
				niter = this.model.listObjectsOfProperty(seriesInd, ResourceFactory.createProperty(Conference.CONFERENCE_TITLE_URI));
				if (niter.hasNext()) {
					s.setSeriesTitle(niter.nextNode().toString());
				}
				
				niter = this.model.listObjectsOfProperty(seriesInd, ResourceFactory.createProperty(Conference.CONFERENCE_WEBSITE_URI));
				if (niter.hasNext()) {
					s.setWebsite(niter.nextNode().toString());
				}
				
				niter = this.model.listObjectsOfProperty(seriesInd, ResourceFactory.createProperty(ConferenceSeries.SUBMISSIONS_DUE_IN_URI));
				if (niter.hasNext()) {
					s.setSubmissionsDueIn(niter.nextNode().toString());
				}
				
				niter = this.model.listObjectsOfProperty(seriesInd, ResourceFactory.createProperty(ConferenceSeries.OCCURS_IN_URI));
				if (niter.hasNext()) {
					s.setOccursIn(niter.nextNode().toString());
				}
				
				list = new ArrayList<String>();
				niter = this.model.listObjectsOfProperty(seriesInd, ResourceFactory.createProperty(ConferenceSeries.CONFERENCE_INSTANCE));
				while (niter.hasNext()) {
					list.add(niter.nextNode().toString());
				}
				s.setConstituentConferences(list);
				
				list = new ArrayList<String>();
				niter = this.model.listObjectsOfProperty(seriesInd, ResourceFactory.createProperty(Conference.CONFERENCE_TOPIC_AREA_URI));
				while (niter.hasNext()) {
					list.add(niter.nextNode().toString());
				}
				s.setTopicAreas(list);
				
				this.outConf.setSeries(s);
			}			
		}
		
		// Supplemental location
		riter = this.model.listSubjectsWithProperty(RDF.type, this.model.getResource(Conference.CITY));
		if (riter.hasNext()) {
			Location newLoc;
			RDFNode parent = null;
			location = riter.nextResource();
			niter = this.model.listObjectsOfProperty(location, this.model.getProperty(Conference.IS_IN_URI));
			if (niter.hasNext()) {
				parent = niter.nextNode();
			}
			newLoc = new Location(parent.toString(), null, location.toString());
			niter = this.model.listObjectsOfProperty(location, RDFS.label);
			if (niter.hasNext()) {
				newLoc.setDisplayName(niter.nextNode().toString());
			}
			this.outConf.setLocation(newLoc);
		}
	}
	
	public void run() {
		buildConference();
	}
}
