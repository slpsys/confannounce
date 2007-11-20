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
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.vocabulary.*;

import edu.lehigh.mab305.swproj.ConferenceModel.Conference;
import edu.lehigh.mab305.swproj.ConferenceModel.OWLUtil;
import edu.lehigh.mab305.swproj.Application.*;;

public class ConferenceLocationController {

	protected ArrayList<Model> ontModels = null;
	protected ArrayList<String> ontologyURIs = null;
	protected Model model = null;
	protected HierarchyStringTree tree = null;
	protected ArrayList<String> subLocationURIs = null;
	protected static final String NIL = "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil";
	
	//protected static final String[] subURIs = {"http://www.lehigh.edu/~mab305/swproj/State.owl#country", "http://www.lehigh.edu/~mab305/swproj/USCity.owl#state"};
	
	public ConferenceLocationController (ArrayList<Model> ontModels) {
		this.ontModels = ontModels;
		this.tree = null;
		this.model = ModelFactory.createDefaultModel();
		this.ontologyURIs = new ArrayList<String>();
		this.subLocationURIs = new ArrayList<String>();
		
		for (Model m : ontModels) {
			this.model.add(m);
			StmtIterator siter = m.listStatements(null, RDF.type, OWL.Ontology);
			while (siter.hasNext()) {
				Statement s = siter.nextStatement();
				ontologyURIs.add(s.getSubject().toString());
			}
		}
		
		// Add the currently open model, if there is one.
		if (SettingsManager.getOpenedModel() != null) {
			 /*StmtIterator siter = SettingsManager.getOpenedModel().listStatements();
			 	while (siter.hasNext()) {
				 System.out.println(siter.nextStatement());
			 }*/
			this.model.add(SettingsManager.getOpenedModel());
		}
		
		this.subLocationURIs.addAll(OWLUtil.getSubAndEquivalentProperties(this.model, Conference.IS_IN_URI));
 	}
	
	public void prettyPrintTree() {
		if (this.tree == null) {
			this.buildLocationTree();
		}
		this.tree.prettyPrint(System.out);
	}
	
	public void buildLocationTree() {
		HashMap<String, Integer> addedURIs = new HashMap<String,Integer>();
		for (String uri : this.subLocationURIs) {
			Property sublocation = ResourceFactory.createProperty(uri);
			StmtIterator stmt = this.model.listStatements((Resource) null, sublocation, (RDFNode) null);
			
			if (this.tree == null) {
				this.tree = new HierarchyStringTree();
			}
			while (stmt.hasNext()) {
				String parent, parentName = null, child, childName = null;
				NodeIterator ni;
				// Well, we have the parent and child, now we just need to retrieve their RDFS labels.
				Triple trp = stmt.nextStatement().asTriple();
				parent = trp.getObject().toString();
				child = trp.getSubject().toString();
				
				// Get parent name 
				ni = this.model.listObjectsOfProperty(ResourceFactory.createResource(parent), RDFS.label);
				if (ni.hasNext()) {
					parentName = ni.next().toString();
				}
				
				// Get child name
				ni = this.model.listObjectsOfProperty(ResourceFactory.createResource(child), RDFS.label);
				if (ni.hasNext()) {
					childName = ni.next().toString();
				}
				
				// Are there parent & child names?
				if (childName != null || parentName != null) {
					String c = child, p = parent;
					if (childName != null) {
						c = childName;
					}
					if (parentName != null) {
						p = parentName;
					}
					this.tree.addSubClassLink(parent, p, child, c);
					addedURIs.put(parent, 1);
					addedURIs.put(child, 1);
				}
				else {
					//System.out.println(parentName + " -> "+ childName);
					this.tree.addSubClassLink(parent, child);
					addedURIs.put(parent, 1);
					addedURIs.put(child, 1);
				}
			}
		}
		
		// Add locations without sub/super locations.
		addSingletonLocations(addedURIs);
		
		
		this.tree.processTree();
	}
	
	protected void addSingletonLocations(HashMap<String,Integer> addedURIs) {
		ArrayList<String> locationTypes = OWLUtil.getInferredTypes(this.model, Conference.LOCATION);
		for (String locationType : locationTypes) {
			ResIterator riter = this.model.listSubjectsWithProperty(RDF.type, this.model.getResource(locationType));
			while (riter.hasNext()) {
				String location  = riter.nextResource().toString();
				if (!addedURIs.containsKey(location)) {
					// Add the Singleton location and get its label, if it has one
					String label = null;
					NodeIterator niter = this.model.listObjectsOfProperty(this.model.getResource(location), RDFS.label);
					if (niter.hasNext()) {
						label = niter.nextNode().toString();
					}
					
					if (label != null) {
						this.tree.addSingletonNode(location, label);
					}
					else {
						this.tree.addSingletonNode(location);
					}
					addedURIs.put(location, 1);
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

	/**
	 * @return the ontologyURIs
	 */
	public ArrayList<String> getOntologyURIs() {
		return ontologyURIs;
	}

	/**
	 * @param ontologyURIs the ontologyURIs to set
	 */
	public void setOntologyURIs(ArrayList<String> ontologyURIs) {
		this.ontologyURIs = ontologyURIs;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * @return the subLocationURIs
	 */
	public ArrayList<String> getSubLocationURIs() {
		return subLocationURIs;
	}
}
