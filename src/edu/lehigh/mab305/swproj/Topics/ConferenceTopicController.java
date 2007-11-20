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
package edu.lehigh.mab305.swproj.Topics;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;

import edu.lehigh.mab305.swproj.ConferenceModel.Conference;
import edu.lehigh.mab305.swproj.ConferenceModel.OWLUtil;
import edu.lehigh.mab305.swproj.Application.URLConstants;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.vocabulary.*;


public class ConferenceTopicController {

	protected ArrayList<Model> ontModels = null;
	protected ArrayList<String> subTopicURIS = null;
	protected Model model;
	protected HierarchyStringTree tree;
	protected static final String NIL = "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil";
	
	public static final String SUBTOPIC_BASE_URI = URLConstants.CONFERENCE_ONTOLOGY + "#subTopicOf";
		
	public ConferenceTopicController (ArrayList<Model> ontModels) {
		this.ontModels = ontModels;
		this.tree = null;
		this.model = ModelFactory.createDefaultModel();
		this.subTopicURIS = new ArrayList<String>();
		
		for (Model m : ontModels) {
			if (m != null) {
				this.model.add(m);
			}
		}
		
		// New feature: preemptively load _all_ imported ontologies, utilizing
		// the url/local mapping proxy.
		//Model imports = OWLUtil.getImportedModels(this.model);
		//this.model.add(imports);
		
		this.subTopicURIS.addAll(OWLUtil.getSubAndEquivalentProperties(this.model, SUBTOPIC_BASE_URI
				));
		ResIterator riter = this.model.listSubjectsWithProperty(RDFS.subClassOf, 
				ResourceFactory.createResource(ConferenceTopicController.SUBTOPIC_BASE_URI));
		if (riter.hasNext()) {
			ArrayList<String> a = new ArrayList<String>();
			while (riter.hasNext()) {
				String s = riter.nextResource().toString();
				a.add(s);
				this.subTopicURIS.add(s);
			}
			subClassHelper(a);
		}
 	}
	
	protected void subClassHelper(ArrayList<String> subclasses) {
		ArrayList<String> mySubs = new ArrayList<String>();
		for (String subclass : subclasses) {
			ResIterator riter = this.model.listSubjectsWithProperty(RDFS.subClassOf, 
					ResourceFactory.createResource(subclass));
			while (riter.hasNext()) {
				String s = riter.nextResource().toString();
				mySubs.add(s);
				this.subTopicURIS.add(s);
			}
		}
		subClassHelper(mySubs);
	}
	
	protected Model createModel (InputStream in) {
		
		Model model = null; //,model2 = null;
		//InfModel inf;
		
		// Create explicit OWL ontology model.
		model = ModelFactory.createDefaultModel();
		model.read(in, "");

		return model;
	}
	
	public void prettyPrintTree() {
		if (this.tree == null) {
			this.buildTopicTree();
		}
		this.tree.prettyPrint(System.out);
	}
	
	public void buildTopicTree() {
		HashMap<String, Integer> addedURIs = new HashMap<String,Integer>();
		for (String subtopicProperty : this.subTopicURIS) {
			Property subtopic = ResourceFactory.createProperty(subtopicProperty);
			StmtIterator stmt = this.model.listStatements((Resource) null, subtopic, (RDFNode) null);
		
			if (this.tree == null) {
				this.tree = new HierarchyStringTree();
			}
			while (stmt.hasNext()) {
				Triple trp = stmt.nextStatement().asTriple();
				this.tree.addSubClassLink(trp.getObject().toString(), trp.getSubject().toString());
				addedURIs.put(trp.getObject().toString(), 1);
				addedURIs.put(trp.getSubject().toString(), 1);
			}
		}
		
		// Add locations without sub/super topics.
		addSingletonTopics(addedURIs);
		
		this.tree.processTree();
	}
	
	protected void addSingletonTopics(HashMap<String,Integer> addedURIs) {
		ArrayList<String> topicTypes = OWLUtil.getInferredTypes(this.model, Conference.TOPIC);
		for (String topicType : topicTypes) {
			ResIterator riter = this.model.listSubjectsWithProperty(RDF.type, this.model.getResource(topicType));
			while (riter.hasNext()) {
				String topic  = riter.nextResource().toString();
				if (!addedURIs.containsKey(topic)) {
					// Add the Singleton location and get its label, if it has one
					String label = null;
					NodeIterator niter = this.model.listObjectsOfProperty(this.model.getResource(topic), RDFS.label);
					if (niter.hasNext()) {
						label = niter.nextNode().toString();
					}
					
					if (label != null) {
						this.tree.addSingletonNode(topic, label);
					}
					else {
						this.tree.addSingletonNode(topic);
					}
					addedURIs.put(topic, 1);
				}
			}
		}
	}
	
	/**
	 * @return the tree
	 */
	public HierarchyStringTree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(HierarchyStringTree tree) {
		this.tree = tree;
	}

	public static void main (String args[]) {
	}
}
