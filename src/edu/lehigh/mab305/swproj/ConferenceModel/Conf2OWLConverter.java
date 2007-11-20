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
 */
package edu.lehigh.mab305.swproj.ConferenceModel;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.*;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import edu.lehigh.mab305.swproj.Application.Progress;
import edu.lehigh.mab305.swproj.Application.URLConstants;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;
//import com.hp.hpl.jena.graph.*;
//import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * This class contains the necessary functionality to transform a Conference 
 * domain object, and create a valid OWL file representing a Conference object
 * in OWL format, with all of the respective properties set. Note that the 
 * conference passed in must have all desired attributes set prior to calling
 * convert(), which is the final action from the API's view. To create an OWL
 * file while creating a new ConferenceSeries in conjunction with this class 
 * (if creating a ConferenceSeries in this manner, the Conference being created
 * will be a constituent of the Series, and the Conference will similarly be 
 * linked to the Series), the following actions are necessary:
 * <p><pre>
 * // conferenceObject and seriesObject have already been populated.
 * OWL2ConfConferter conv = new Conf2OWLConverter(conferenceObject, seriesObject);
 * // Import any additional ontologies. [optional, not necessary for topic and
 * // location ontologies] 
 * conv.importOntology(ontologyURI);
 * // Set the SWT shell, so that a ProgressBar window can be shown.
 * conv.setCallerShell(this.getSShell());
 * // Add this current object (or another object) as an observer, so that it can
 * be updated with the status of the save.
 * conv.addObserver(this);
 * conv.setOutputFileName(outFileName);
 * conv.convert();
 * </pre></p>
 * In order to create one without a Series, simply use the constructor with no 
 * ConferenceSeries object.
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 */
public class Conf2OWLConverter extends Observable implements Runnable {
	
	private static final String CONF_CLASS = URLConstants.CONFERENCE_ONTOLOGY + "#Conference";
	private static final String XML_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	protected Model model, localConfModel = null;
	protected Resource conferenceInd = null, seriesInd = null, ont = null;
	protected Conference conf = null;
	protected ConferenceSeries series = null;
	protected ArrayList<String> ontURIs = null;
	protected ArrayList<Model> topicOntologies = null;
	protected ArrayList<String> importURIs = null;
	protected String outputFileName = null;
	protected Shell callerShell = null;
	protected Exception exceptionBuf = null;

	/**
	 * Creates a new converter object, based on the Conference domain model object passed in. This
	 * method should be used with some measure of discretion, as it does begin to process some of
	 * th
	 * @param conf
	 */
	public Conf2OWLConverter(Conference conf) {
		super();
		this.conf = conf;
		
		String base = this.conf.baseURI;
		Resource cls;
		//ArrayList<String> ontLocalFiles;
		
		this.topicOntologies = new ArrayList<Model>();
		this.importURIs = new ArrayList<String>();
		this.model = ModelFactory.createDefaultModel();
		
		// Create the ontology manually
		this.ont = ResourceFactory.createResource(base);
		this.model.add(this.ont, RDF.type, OWL.Ontology);

		ontURIs = this.conf.getTopicOntologyURIs();
		
		cls = this.model.getResource(Conf2OWLConverter.CONF_CLASS);
		
		//System.out.println(base + this.conf.getConferenceName_URIFriendly());
		this.conferenceInd = ResourceFactory.createResource(base + this.conf.getConferenceName_URIFriendly());
		this.model.add(conferenceInd, RDF.type, cls);
	}
	
	public Conf2OWLConverter(Conference conf, ConferenceSeries series) {
		this(conf);
		this.series = series;
		Resource cls = this.model.getResource(ConferenceSeries.SERIES_TYPE_URI);
		
		this.seriesInd = ResourceFactory.createResource(this.series.getFullURI());
		this.model.add(seriesInd, RDF.type, cls);
	}
	
	public void importOntology (String uri) {
		this.importURIs.add(uri);
	}
	
	public void convert() {
		(new Thread(this)).start();
	}

	public Conf2OWLConverter() {
		this.model = ModelFactory.createDefaultModel();
	}
	
	/**
	 * Takes a list of local ontology files used internally for building topic trees, reads them
	 * in as Ontologies, and returns the URIs associated with them by removing imported ontology URIs from the list of URIs
	 * associated with each local ontology file. This allows us to use local ontology files (taken from the Web), and map
	 * them with the actual URI basenames.
	 * @param localFiles
	 * @return
	 */
	protected ArrayList<String> getTopicURIOntologies (ArrayList<String> localFiles) {
		ArrayList<String> retList = new ArrayList<String>();
	
		try { 
			for (String localFile : localFiles) {
				Model model = ModelFactory.createDefaultModel();
				//System.out.println("adding uri:" + localFile);
				//model.setDynamicImports(false);
				
				this.topicOntologies.add(model);
				//this.model.add(model);
				// Tries to access the internet here!
				model.read(new FileInputStream(localFile), "");		
				
				ResIterator riter = model.listSubjectsWithProperty(RDF.type, OWL.Ontology);
				while (riter.hasNext()) {
					retList.add(riter.nextResource().toString());
				}
				
				/*imports = model.listImportedOntologyURIs();
				e = model.listOntologies();
				while (e.hasNext()) {
					Ontology o = (Ontology) e.next();
					if (!imports.contains(o.getURI())) {
						retList.add(o.getURI());
					}
				}*/
			}
		}
		catch (IOException io) {
			System.err.println(io.getMessage());
		}
		return retList;
	}
	
	protected void createIndividual () {
		//StmtIterator testIter;
		ArrayList<String> topics;
		Progress firstProgress = new Progress("Beginning save.", 0);
		firstProgress.setWindowTitle("Saving new Conference ontology...");
		this.notifyObservers(firstProgress);
		String s;
		
		topics = this.conf.getConferenceTopicAreas();
		
		// Set the start date
		s = this.conf.getConferenceStartDate();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_START_DATE_URI), this.conf.getConferenceStartDate());
		}
		
		// Set the end date
		s = this.conf.getConferenceEndDate();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_END_DATE_URI), this.conf.getConferenceEndDate());
		}
			
		// Set the paper submission deadline
		s = this.conf.getConferencePaperSubmissionDeadline();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_PAPER_DATE_URI), this.conf.getConferencePaperSubmissionDeadline());
		}
		
		// Set the abstract submisison deadline
		s = this.conf.getConferenceAbstractSubmissionDeadline();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_ABSTRACT_DATE_URI), this.conf.getConferenceAbstractSubmissionDeadline());
		}
		
		this.notifyObservers(new Progress("Writing topic ontologies..", 15));
		for (String topic : topics) {
			//Individual i = this.model.getIndividual(topic);
			Resource i = this.model.getResource(topic);
			if (i != null) {
				this.model.add(this.model.createStatement(this.conferenceInd, ResourceFactory.createProperty(Conference.CONFERENCE_TOPIC_AREA_URI), i));				
			}
		}
		
		this.notifyObservers(new Progress("Writing description and attributes..", 55));
		
		// Conference Description
		s = this.conf.getConferenceDescription();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_DESCRIPTION_URI),this.conf.getConferenceDescription());
		}
		
		// Conference Website
		s = this.conf.getConferenceWebsite();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_WEBSITE_URI),this.conf.getConferenceWebsite());
		}

		// Conference Street Address.
		s = this.conf.getAddress();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_ADDRESS_URI), this.conf.getAddress());
		}
		
		// Conference Title
		s = this.conf.getConferenceName();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_TITLE_URI),this.conf.getConferenceName());
		}
				
		this.notifyObservers(new Progress("Writing full announcement text..", 65));
		// Announcement Text
		
		s = this.conf.getAnnouncementFullText();
		if (s != null && s != "" && s.length() > 0) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.ANNOUNCEMENT_FULLTEXT_URI), this.conf.getAnnouncementFullText());
		}
		
		s = this.conf.getSeriesURI();
		if (s != null && s != "" && s.length() > 0) {
			Resource ind = this.model.createResource(this.conf.getSeriesURI());
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.PART_OF_SERIES_URI), ind);
		}
		
		this.notifyObservers(new Progress("Writing location..", 73));
		// Locations area little trickier, as objects.
		//createLocations();
		Location loc = this.conf.getLocation();
		s = this.conf.getLocatedInURI();
		if (s != null && !s.equals("") && s.length() > 0) {
			Resource ind = this.model.createResource(s);
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_LOCATED_URI), ind);
		}
		else if (loc != null) {
			Resource location = null;
			if (loc.getLocationURI().charAt(0) == '#') {
				String locURI = loc.getLocationURI().substring(1);
				if (this.conf.baseURI.contains("#")) {
					location = this.model.createResource(this.conf.baseURI + locURI);
				}
				else {
					location = this.model.createResource(this.conf.baseURI + "#" + locURI);
				}
			}
			else {
				location = this.model.createResource(loc.getLocationURI());
			}
			this.model.add(location, RDF.type, this.model.getResource(loc.getTypeURI()));
			this.model.add(location, RDFS.label, loc.getDisplayName());
			this.model.add(location, ResourceFactory.createProperty(Conference.IS_IN_URI), this.model.getResource(loc.getParentURI()));
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_LOCATED_URI), location);
		}
		
		
		this.notifyObservers(new Progress("Finishing up model write..", 95));
		// Finish model creation
		
		//this.ont.addImport( this.model.createResource(Conf2OWLConverter.CONF_ONTFILE) );
		this.model.add(this.ont, OWL.imports, this.model.createResource(URLConstants.CONFERENCE_ONTOLOGY));
		
		for (Model mdl : this.topicOntologies) {
			this.model.remove(mdl);
		}
		
		// DO NOT add imports for anything other than the conference ontology!
		// This causes Hawk to blow up!
		for (String uri : this.ontURIs) {
			//ont.addImport( this.model.createResource(uri));
			this.model.add(this.ont, OWL.imports, this.model.createResource(uri));

		}
		
		for (String uri : conf.getLocationOntologyURIs()) {
			//ont.addImport( this.model.createResource(uri));
			this.model.add(this.ont, OWL.imports, this.model.createResource(uri));
		}
		
		for (String uri : this.importURIs) {
			//ont.addImport( this.model.createResource(uri));
			this.model.add(this.ont, OWL.imports, this.model.createResource(uri));
		}
		
		/*testIter = this.model.listStatements();
		while (testIter.hasNext()) {
			Statement node = testIter.nextStatement();
			//System.out.println(node);
		}*/
		
		if (this.series != null) {
			createSeries();
		}
		
		// Write out the file
		if (this.outputFileName != null) {
			writeConference(this.outputFileName);
		}
		this.notifyObservers(new Progress("Finished!", 100));
	}
	
	/**
	 * Create
	 * 
	 */
	protected void createSeries() {
		ArrayList<String> list;
		boolean addedReflectiveConference = false;
		String reflectiveURI = this.conf.getBaseURI();
		if (reflectiveURI.contains("#")) {
			reflectiveURI.concat(this.conf.getConferenceName_URIFriendly());
		}
		else {
			reflectiveURI.concat("#" + this.conf.getConferenceName_URIFriendly());
		}
		
		// Series Title
		if (this.series.getSeriesTitle() != null && this.series.getSeriesTitle() != "")
			this.model.add(this.seriesInd, this.model.getProperty(Conference.CONFERENCE_TITLE_URI), this.series.getSeriesTitle());
		
		// Series Website
		if (this.series.getWebsite() != null && this.series.getWebsite() != "")
			this.model.add(this.seriesInd, this.model.getProperty(Conference.CONFERENCE_WEBSITE_URI), this.series.getWebsite());

		// Series occursIn
		if (this.series.getOccursIn() != null && this.series.getOccursIn() != "")
			this.model.add(this.seriesInd, this.model.getProperty(ConferenceSeries.OCCURS_IN_URI), 
					this.model.createResource(URLConstants.MONTHS_ONTOLOGY + "#" + this.series.getOccursIn()));
		
		// Series submissionsDueIn		
		if (this.series.getSubmissionsDueIn() != null && this.series.getSubmissionsDueIn() != "")
			this.model.add(this.seriesInd, this.model.getProperty(ConferenceSeries.SUBMISSIONS_DUE_IN_URI),
					this.model.createResource(URLConstants.MONTHS_ONTOLOGY + "#" + this.series.getSubmissionsDueIn()));
		
		// Add properties for Series Topics, also add the Conference being created to the list of constituent Conferences
		// for this Series, if it is not explictly added by the user.
		list = this.series.getConstituentConferences();
		for (String s : list) {
			this.model.add(this.seriesInd, this.model.getProperty(ConferenceSeries.CONFERENCE_INSTANCE), 
					this.model.createResource(s));
			if ((reflectiveURI + this.conf.getConferenceName_URIFriendly()) == s) { 
				addedReflectiveConference = true;
			}
		}
		
		if (!addedReflectiveConference) {
			this.model.add(this.seriesInd, this.model.getProperty(ConferenceSeries.CONFERENCE_INSTANCE), 
					this.model.createResource(reflectiveURI + this.conf.getConferenceName_URIFriendly()));
		}
		
		// Add properties for Series Topics
		list = this.series.getTopicAreas();
		for (String s : list) {
			this.model.add(this.seriesInd, this.model.getProperty(Conference.CONFERENCE_TOPIC_AREA_URI), this.model.createResource(s));
		}
	}
	
	public void createLocations() {
		Resource city = null, state = null, province = null, country = null;
		if (this.conf.getCountry() != null && !this.conf.getCountry().equals("")) {
			country = this.model.createResource(this.conf.getBaseURI() + this.conf.getCountry_URIFriendly());
			this.model.add(country, RDF.type, this.model.createResource(Conference.COUNTRY));
		}
		if (this.conf.getState() != null && !this.conf.getState().equals("")) {
			//state = this.model.createIndividual(this.conf.getBaseURI() + this.conf.getState_URIFriendly(), this.model.getOntClass(Conference.STATE));
			state = this.model.createResource(this.conf.getBaseURI() + this.conf.getState_URIFriendly());
			this.model.add(country, RDF.type, this.model.createResource(Conference.STATE));
			if (this.conf.getCountry() != null && !this.conf.getCountry().equals("")) {
				this.model.add(state, this.model.getProperty(Conference.IS_IN_URI), country);
			}
		}
		else {
			// We only really want a province if there's no state
			if (this.conf.getProvince() != null && !this.conf.getProvince().equals("")) {
				//province = this.model.createIndividual(this.conf.getBaseURI() + this.conf.getProvince_URIFriendly(), this.model.getOntClass(Conference.PROVINCE));
				province = this.model.createResource(this.conf.getBaseURI() + this.conf.getProvince_URIFriendly());
				this.model.add(country, RDF.type, this.model.createResource(Conference.PROVINCE));
				if (this.conf.getCountry() != null && !this.conf.getCountry().equals("")) {
					this.model.add(province, this.model.getProperty(Conference.IS_IN_URI), country);
				}
			}
		}
		
		if (this.conf.getCity() != null && !this.conf.getCity().equals("")) {
			//city = this.model.createIndividual(this.conf.getBaseURI() + this.conf.getCity_URIFriendly(), this.model.getOntClass(Conference.CITY));
			city = this.model.createResource(this.conf.getBaseURI() + this.conf.getCity_URIFriendly());
			this.model.add(country, RDF.type, this.model.createResource(Conference.CITY));
			if (this.conf.getState() != null && !this.conf.getState().equals("")) {
				this.model.add(city, this.model.getProperty(Conference.IS_IN_URI), state);
			}
			else if (this.conf.getProvince() != null && !this.conf.getProvince().equals("")) {
				this.model.add(city, this.model.getProperty(Conference.IS_IN_URI), province);
			}
			else if (this.conf.getCountry() != null && !this.conf.getCountry().equals("")) {
				this.model.add(city, this.model.getProperty(Conference.IS_IN_URI), country);
			}
			
			// Set location.
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_LOCATED_URI), city);
		}
		else if (this.conf.getState() != null && !this.conf.getState().equals("")) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_LOCATED_URI), state);
		}
		else if (this.conf.getProvince() != null && !this.conf.getProvince().equals("")) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_LOCATED_URI), province);
		}
		else if (this.conf.getCountry() != null && !this.conf.getCountry().equals("")) {
			this.model.add(this.conferenceInd, this.model.getProperty(Conference.CONFERENCE_LOCATED_URI), country);
		}
		
	}
	
	public synchronized void writeConference (String filename) {

		try {
			FileInputStream in;
			FileOutputStream out;
			BufferedReader bufread;
			BufferedWriter bufwrite;
			StringBuilder totalText = new StringBuilder();
			String str;
			
			model.write(new FileOutputStream(filename));
			in = new FileInputStream(filename);
			bufread = new BufferedReader(new InputStreamReader(in));
			totalText.append(Conf2OWLConverter.XML_ENCODING);
			str = bufread.readLine();
			while (str != null) {
				totalText.append(str + "\n");
				str = bufread.readLine();
			}
			bufread.close();
			
			out = new FileOutputStream(filename);
			bufwrite = new BufferedWriter(new OutputStreamWriter(out));
			bufwrite.write(totalText.toString(), 0, totalText.length());
			bufwrite.close();
			
			this.callerShell.getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (callerShell != null && !callerShell.isDisposed()) {
						MessageBox mb = new MessageBox(callerShell, SWT.ICON_WORKING);
						mb.setText("Save Completed");
						mb.setMessage("Conference save successful.");
						mb.open();
					}
				}
			}
			);
		}
		catch (com.hp.hpl.jena.shared.JenaException je) {
			this.exceptionBuf = je;
			this.callerShell.getDisplay().asyncExec(new Runnable() {
					public void run() {
					if (callerShell != null && !callerShell.isDisposed()) {
						MessageBox mb = new MessageBox(callerShell, SWT.ICON_ERROR);
						mb.setText("Error Writing Output");
						mb.setMessage(exceptionBuf.getLocalizedMessage());
						mb.open();
					}
				}
			}
			);
		}
		catch (IOException ie) {
			this.exceptionBuf = ie;
			this.callerShell.getDisplay().asyncExec(new Runnable() {
					public void run() {
					if (callerShell != null && !callerShell.isDisposed()) {
						MessageBox mb = new MessageBox(callerShell, SWT.ICON_ERROR);
						mb.setText("Error Writing Output");
						mb.setMessage(exceptionBuf.getLocalizedMessage());
						mb.open();
					}
				}
			}
			);
		}
	}

	/**
	 * @return the conferenceInd
	 */
	protected Resource getConferenceInd() {
		return conferenceInd;
	}

	/**
	 * @param conferenceInd the conferenceInd to set
	 */
	protected void setConferenceInd(Individual conferenceInd) {
		this.conferenceInd = conferenceInd;
	}
	
	/**
	 * @return the conf
	 */
	protected Conference getConf() {
		return conf;
	}

	/**
	 * @param conf the conf to set
	 */
	protected void setConf(Conference conf) {
		this.conf = conf;
	}
	
	public void notifyObservers(Object e) {
		this.setChanged();
		super.notifyObservers(e);
	}
	
	public void run() {
		createIndividual();
	}

	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName() {
		return outputFileName;
	}

	/**
	 * @param outputFileName the outputFileName to set
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public Shell getCallerShell() {
		return callerShell;
	}

	public void setCallerShell(Shell callerShell) {
		this.callerShell = callerShell;
	}
}