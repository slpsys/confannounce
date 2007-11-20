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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.io.*;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.*;

import edu.lehigh.mab305.swproj.Application.*;
import edu.lehigh.mab305.swproj.ConferenceModel.Conference;
import edu.lehigh.mab305.swproj.ConferenceModel.OWLUtil;
import edu.lehigh.mab305.swproj.Topics.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;

public class TopicPane extends Composite {

	protected ConferenceTopicController topicController;
	protected TopicPaneController controller;  //  @jve:decl-index=0:
	protected Tree tree = null;
	protected ArrayList<String> localTopicOntologyFiles;
	protected DataList<String> lstOntologies = null, lstSelectedTopics = null;
	protected Label lblOntologies = null, lblTree = null, lblSelectedTopics = null;
	protected Button btnAddOntology = null, btnRemOntology = null, btnReloadTree = null,
				btnAddSelection = null, btnRemoveSelection = null;
	protected Menu contextTreeMenu = null;
	protected MenuItem itemTreeReload = null;
	protected HashMap<String,Integer> hashTopics = null;
	protected HashMap<String,Model> hashModels = null;
	
	// Search fields
	protected Label lblSearch = null;
	protected Text txtSearch = null;
	protected Button btnSearch = null;
	protected TreeItem sortedItems[] = null;	
	protected HashMap<String, Integer> hashSortAlphas = new HashMap<String, Integer>();
	
	public TopicPane(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		// Make changes here
		this.localTopicOntologyFiles = new ArrayList<String>();
		//this.localTopicOntologyFiles.add(ConferenceTopicController.ONTO_FILE);
		this.hashTopics = new HashMap<String,Integer>();
		this.hashModels = new HashMap<String,Model>();
		
		initialize();
	}

	protected void initialize() {
		SearchAdapter searcher = new SearchAdapter();
		tree = new Tree(this, SWT.MULTI | SWT.BORDER);
		tree.setBounds(new Rectangle(7, 23, 406, 342)); // NEW
		tree.addMouseListener(new ConfInfoPaneButtonListener());
		tree.addKeyListener(new ConfInfoPaneButtonListener());
		lblTree = new Label(this.getCompositePane(), SWT.NONE);
		lblTree.setBounds(new Rectangle(7, 6, 135, 13));
		lblTree.setText("Topic Hierarchy Tree:");
		
		// Set tree context menu
		this.contextTreeMenu = new Menu(this.getCompositePane().getShell(), SWT.POP_UP);
		tree.setMenu(contextTreeMenu);
		
		this.itemTreeReload = new MenuItem(contextTreeMenu, SWT.PUSH);
		itemTreeReload.setText("Reload");
		itemTreeReload.addSelectionListener(new ConfInfoPaneButtonListener());

		lstOntologies = new DataList<String>(this.getCompositePane(), SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		lstOntologies.setBounds(new Rectangle(420, 23, 196, 91));
		lstOntologies.addKeyListener(new ConfInfoPaneButtonListener());
		
		lblOntologies = new Label(this.getCompositePane(), SWT.NONE);
		lblOntologies.setBounds(new Rectangle(420, 6, 255, 13));
		lblOntologies.setText("Topic Ontologies Loaded:");
		
		btnReloadTree = new Button(this.getCompositePane(), SWT.NONE);
		btnReloadTree.setBounds(new Rectangle(420, 118, 86, 23));
		btnReloadTree.setText("Reload Tree");
		btnReloadTree.addSelectionListener(new ConfInfoPaneButtonListener());
		
		btnAddOntology = new Button(this.getCompositePane(), SWT.NONE);
		btnAddOntology.setBounds(new Rectangle(515, 118, 46, 23));
		btnAddOntology.setText("Add");
		btnAddOntology.addSelectionListener(new ConfInfoPaneButtonListener());
		
		btnRemOntology = new Button(this.getCompositePane(), SWT.NONE);
		btnRemOntology.setBounds(new Rectangle(561, 118, 56, 23));
		btnRemOntology.setText("Remove");
		btnRemOntology.addSelectionListener(new ConfInfoPaneButtonListener());

		lblSelectedTopics = new Label(this.getCompositePane(), SWT.NONE);
		lblSelectedTopics.setBounds(new Rectangle(420, 148, 196, 17));
		lblSelectedTopics.setText("Selected Topics:");
		
		lstSelectedTopics = new DataList<String>(this.getCompositePane(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		lstSelectedTopics.setBounds(new Rectangle(420, 165, 196, 200));
		lstSelectedTopics.addKeyListener(new ConfInfoPaneButtonListener());
		
		btnAddSelection = new Button(this.getCompositePane(), SWT.PUSH);
		btnAddSelection.setBounds(new Rectangle(515, 365, 46, 23));
		btnAddSelection.setText("Add");
		btnAddSelection.addSelectionListener(new ConfInfoPaneButtonListener());
		btnRemoveSelection = new Button(this.getCompositePane(), SWT.PUSH);
		btnRemoveSelection.setBounds(new Rectangle(561, 365, 56, 23));
		btnRemoveSelection.setText("Remove");
		btnRemoveSelection.addSelectionListener(new ConfInfoPaneButtonListener());
		
		lblSearch = new Label(this.getCompositePane(), SWT.NONE);
		lblSearch.setBounds(new Rectangle(7, 373, 130, 17));
		lblSearch.setText("Search Tree by Keyword:");
		
		txtSearch = new Text(this.getCompositePane(), SWT.BORDER);
		txtSearch.setBounds(new Rectangle(140, 370, 200, 19));
		txtSearch.addKeyListener(searcher);
		
		btnSearch = new Button(this.getCompositePane(), SWT.PUSH);
		btnSearch.setBounds(new Rectangle(345, 368, 65, 23));
		btnSearch.setText("Search");
		btnSearch.addSelectionListener(searcher);
		btnSearch.setToolTipText("Pressing Search multiple times iterates through all possible matches.");

		this.setLayout(null);
		this.setSize(new Point(420, 415));
	}
	
	public Composite getCompositePane() {
		return this;
	}

	protected void buildTree(GridData grid, GridLayout layout) {
		HierarchyStringTree dataTree;
		ArrayList<HierarchyStringTreeNode> children;
		ArrayList<TreeItem> uiItems = new ArrayList<TreeItem>();
		TreeItem curItem, seriesItem;
		
		this.tree.removeAll();
		this.controller.clearSeriesTree();
		topicController.buildTopicTree();
		dataTree = topicController.getTree();
		this.setLayout(layout);
				
		if (dataTree.getRoot() != null) {
			curItem = new TreeItem(tree,SWT.NULL);
			uiItems.add(curItem);
			curItem.setText("Root");
			children = dataTree.getRoot().getChildren();
			seriesItem = new TreeItem(controller.getSeriesTree() ,SWT.NULL);
			seriesItem.setText("Root");
			for (HierarchyStringTreeNode node : children) {
				buildTreeHelper(curItem, seriesItem, node, uiItems);
			}
		}
		
		// Preprocess some search indices after tree building
		int lastIndex = 0;
		this.sortedItems = new TreeItem[uiItems.size()];
		this.sortedItems = uiItems.toArray(this.sortedItems);
		Arrays.sort(this.sortedItems, new TreeItemComparator());
		for (char i = 'a'; i <= 'z'; i++ ) {
			for (int j = lastIndex; j < this.sortedItems.length; j++) {
				if (this.sortedItems[j].getText().toLowerCase().charAt(0) == i) {
					this.hashSortAlphas.put(new Character(i).toString(), j);
					lastIndex = j;
					break;
				}
			}
		}
	}
	
	protected void buildTreeHelper(TreeItem curItem, TreeItem curSeriesItem, HierarchyStringTreeNode node, ArrayList<TreeItem> uiItems) {
		ArrayList<HierarchyStringTreeNode> children = node.getChildren();
		TreeItem item, seriesItem;
		
		item = new TreeItem(curItem,SWT.NULL);
		uiItems.add(item);
		item.setText(((node.getShortName() != null) ? node.getShortName() : node.getData()));
		item.setData(node);
		seriesItem = new TreeItem(curSeriesItem,SWT.NULL);
		seriesItem.setText(((node.getShortName() != null) ? node.getShortName() : node.getData()));
		seriesItem.setData(node);
		
		for (HierarchyStringTreeNode curnode : children) {
			buildTreeHelper(item, seriesItem, curnode, uiItems);
		}
	}
	
	/**
	 * @return the controller
	 */
	public TopicPaneController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(TopicPaneController controller) {
		this.controller = controller;
		this.controller.setPane(this);
	}
	
	@SuppressWarnings("unchecked") public void initSavedOntologies() {
		ArrayList<String> errorURLs = new ArrayList<String>(), 
			loadedOnts = SettingsManager.getInstance().getOntologyList(SettingsManager.TOPICS);
		if (loadedOnts != null) {
			for (String ont : loadedOnts) {
				Model m = ModelFactory.createDefaultModel();
				
				try {
					ArrayList<String> previousURLs = null;
					String label = null;
					 while (true) {
						 errorURLs.clear();
						 OWLUtil.getModelWithProxy(ont, m, errorURLs);
						 if (errorURLs.size() == 0 ) {
					 		 break;
					 	 }
						 else if (previousURLs != null && errorURLs.containsAll(previousURLs)) {
							 MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_WARNING);
					 		 mb.setText("Error Loading Initial Ontologies");
					 		 mb.setMessage(SettingsManager.ERRLOADINGONTS_MSG);
					 		 mb.open();
					 		 break;
						 }
						 else {
							 /* Brings up an intermediary proxy editor, displaying only files the application could not
					 		  * find via the Internet, etc.
					 		  */
					 		 SettingsManager.getInstance().getMapping();
					 		 InternalProxyEditorController proxyController 
					 		 	= new InternalProxyEditorController(this.controller.getController());
					 		 InternalProxyEditorWindow dialog 
					 		 	= new InternalProxyEditorWindow(proxyController, 
					 		 			"Edit URL/Local File Mappings", errorURLs);
					 		 dialog.open();
					 		 previousURLs = (ArrayList<String>) errorURLs.clone();
						 }
					 }
					label = OWLUtil.getOntologyLabel(ont, m);
					this.hashModels.put(ont, m);
					if (label != null && label.length() > 0) {
						this.lstOntologies.add(label, ont);
					}
					else {
						this.lstOntologies.add(ont);
					}
				}
				catch (IOException e) {
					MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
					mb.setText("Error Loading Previous Ontologies");
					mb.setMessage("The ontologies last loaded in ConfAnnounce cannot be opened for any of a number of reasons, the " +
							"specific error message is as follows:\n" + e.getMessage());
					mb.open();
				}
			}
			reload();
		}
	}
	
	public ArrayList<Model> getTopicModels() {
		ArrayList<Model> a = new ArrayList<Model>();
		a.addAll(this.hashModels.values());
		return a;
	}
	
	public Model getCompositeTopicModel() {
		Model retMod = ModelFactory.createDefaultModel();
		for (Model m : this.hashModels.values()) {
			retMod.add(m);
		}
		return retMod;
	}
	
	public ArrayList<String> getTopicOntologies() {
		String items[] = this.lstOntologies.getItems();
		ArrayList<String> retArray = new ArrayList<String>(); 
		for (int i = 0; i < items.length; i++)  {
			String s = this.lstOntologies.getDataItem(items[i]);
			if (s == null) {
				s = items[i];
			}
			retArray.add(s);
		}
		return retArray;
	 }
	 
	 public ArrayList<String> getConferenceTopicAreas() {
		 String[] items = this.lstSelectedTopics.getItems();
		 ArrayList<String> itemData = new ArrayList<String>();
		 for (String item : items) {
			itemData.add(this.lstSelectedTopics.getDataItem(item));
		 }
		 return itemData;
	 }
	 
	 public ArrayList<String> getConferenceTopicLabels() {
		 String[] items = this.lstSelectedTopics.getItems();
		 ArrayList<String> itemLabels = new ArrayList<String>();
		 for (String item: items) {
			 itemLabels.add(item);
		 }
		 return itemLabels;
	 }
	 
	 public void addConferenceTopicArea(String topic) {
		 boolean foundLabel = false;
		 String label = "";
		 for (Model m : this.hashModels.values()) {
			 Resource r = m.getResource(topic);
			 StmtIterator siter = m.listStatements(r, (Property) null, (RDFNode)null);
			 NodeIterator niter = m.listObjectsOfProperty(r, RDFS.label);
			 if (niter.hasNext()) {
				 label = niter.nextNode().toString();
				 foundLabel = true;
				 break;
			 }
			 while (siter.hasNext()) {
				 System.out.println(siter.nextStatement());
			 }
			 System.out.println("");
		 }
		 if (foundLabel) {
			 if (label.contains("@")) {
				 this.lstSelectedTopics.add(label.substring(0, label.indexOf('@')), topic);
			 }
			 else {
				 this.lstSelectedTopics.add(label, topic);
			 }
		 }
		 else {
			 this.lstSelectedTopics.add(topic);
		 }
	 }
	 
	 public void reload() {
		 ArrayList<Model> onts = controller.getTopicModels();
		 if (onts != null && onts.size() > 0) {
			 topicController = new ConferenceTopicController(controller.getTopicModels());
			
			 buildTree(null, null);
		 }
		 else {
			 tree.removeAll();
		 }
	 }
	
	 public void addOntology(String uri) {
		 if (!this.hashModels.containsKey(uri)) {
			 try {
				 String label = null;
				 Model m = ModelFactory.createDefaultModel();
				 OWLUtil.getModelWithProxy(uri, m, new ArrayList<String>());
				 this.hashModels.put(uri, m);
				 label = OWLUtil.getOntologyLabel(uri, m);
				 this.lstOntologies.add(label, uri);
			 }
			 catch (IOException ie) {
				 MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				 mb.setText("Error loading ontology imported from file.");
				 mb.setMessage("Error loading an imported file from the opened Conference:" 
						 + ie.getMessage());
				 System.err.println(ie.getMessage());
				 mb.open();
			 }
		 }
		 else {
			 String onts[] = this.lstOntologies.getItems();
			 boolean hasOntLoaded = false;
			 
			 for (int i = 0; i < onts.length; i++) {
				if (this.lstOntologies.getDataItem(onts[i]).equals(uri)) {
					hasOntLoaded = true;
					break;
				}
			 }
			 if (!hasOntLoaded) {
				 System.err.println("This should never happen! Loaded ontologies, the models of" +
				 		"which are already loaded, but not placed in the UI list"); 
			 }
			 // Else, do nothing
		 }
	 }
	 
	 protected void addOntologyToUI() {
		 String url = null;
		 AddOntologyURIDialog dialog;
		 
		 dialog = new AddOntologyURIDialog("Add Ontology URL:", this.getShell());
		 dialog.open();
		 url = dialog.getURL();
		 
		 if (dialog.getLocalPath() != null && url != null) {
			 String label = dialog.getOntologyLabel();
			 if (label != null && label.length() > 0) {
				 lstOntologies.add(label, url);
			 }
			 else {
				 lstOntologies.add(url);
			 }
			 if (dialog.getModel() != null) {
				 hashModels.put(url, dialog.getModel());
			 }
			 populateSettings();
			 reload();
		 }
	 }	
	 
	 protected class ConfInfoPaneButtonListener extends org.eclipse.swt.events.SelectionAdapter 
			implements org.eclipse.swt.events.MouseListener, org.eclipse.swt.events.KeyListener {
		
		 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			 if (e.getSource().equals(btnAddOntology)) {
				 btnAddOntologyClicked(e);
			 }
			 else if (e.getSource().equals(btnRemOntology)) {
				 removeOntologies();
			 }
			 else if (e.getSource().equals(itemTreeReload) || e.getSource().equals(btnReloadTree)) {
				 reload();
			 }
			 else if (e.getSource().equals(btnAddSelection)) {
				 addSelections();
			 }
			 else if (e.getSource().equals(btnRemoveSelection)) {
				 removeSelections();
			 }
		 }
		 
		 protected void btnAddOntologyClicked (org.eclipse.swt.events.SelectionEvent e) {
			 addOntologyToUI();
		 }
		 
		 protected void addSelections() {
			 TreeItem items[] = tree.getSelection();
			 for (int i = 0; i < items.length; i++) {
				 String s;
				 if (items[i].getData() instanceof edu.lehigh.mab305.swproj.Topics.HierarchyStringTreeNode) {
					 s = ((edu.lehigh.mab305.swproj.Topics.HierarchyStringTreeNode) items[i].getData()).getData();
					 if (!hashTopics.containsKey(s)) {
						 lstSelectedTopics.add(items[i].getText(), s);
						 hashTopics.put(s, 1);
					 }
				 }
			 }
		 }
		 
		 protected void removeSelections() {
			 int selections[] = lstSelectedTopics.getSelectionIndices();
			 lstSelectedTopics.remove(selections);
		 }
		 
		 protected void removeOntologies() {
			 String strs[] = lstOntologies.getSelection();
			 ArrayList<String> itemsList = new ArrayList<String>();
			 for (String s : strs) {	
				 hashModels.remove(lstOntologies.getDataItem(s));
				 lstOntologies.remove(s);
			 }
			 for (String s : lstOntologies.getItems()) {
				 itemsList.add(s);
			 }
			 SettingsManager.getInstance().setOntologyList(SettingsManager.TOPICS, itemsList);
		 }
		 
		 public void mouseDoubleClick(MouseEvent e) {
			 addSelections();
		 }
		 
		 
		 public void keyPressed(KeyEvent e) {
			 if (e.getSource() == tree && (e.keyCode == SWT.LF || e.keyCode == SWT.CR)) {
				 addSelections();
			 }
			 else if (lstSelectedTopics.equals(e.getSource()) && (e.keyCode == SWT.BS || e.keyCode == SWT.DEL)) {
				 removeSelections();
			 }
			 
			 else if (lstOntologies.equals(e.getSource()) && (e.keyCode == SWT.BS || e.keyCode == SWT.DEL)) {
				 removeOntologies();
			 }
		 }
		 
		 // Methods not implemented.
		 public void mouseDown(MouseEvent e) {
		 }
		 
		 public void mouseUp(MouseEvent e) {
		 }
		 
		 public void keyReleased(KeyEvent e) {
			 
		 }
	 }

	 protected class SearchAdapter extends org.eclipse.swt.events.SelectionAdapter 
		implements org.eclipse.swt.events.KeyListener {
		 protected boolean searching = false;
		 protected int searchIndex = 0;
		 
		 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			 if (e.getSource().equals(btnSearch)) {
				 String searchText = txtSearch.getText().toLowerCase();
				 for (int i = searchIndex; i < sortedItems.length; i++) {
					 if (sortedItems[i].getText().toLowerCase().contains(searchText)) {
						 tree.setSelection(sortedItems[i]);
						 searchIndex = i + 1;
						 break;
					 }
					 searching = true;
				 }
			 }
		 }
		 
		 protected void resetSearch() {
			 searchIndex = 0;
			 searching = false;
		 }
		 
		 public void keyReleased(KeyEvent e) {
			 if (e.getSource().equals(txtSearch)) {
				 resetSearch();
				 if ((e.character >= 'a' && e.character <= 'z') || (e.character >= 'A' && e.character <= 'Z') 
						 || e.character == SWT.DEL || e.character == SWT.BS) {
					 String searchText = txtSearch.getText();
					 int len = searchText.length();
					 if (len > 0) {
						 if (len == 1 && hashSortAlphas.containsKey(searchText.toLowerCase())) {
							 tree.setSelection(sortedItems[hashSortAlphas.get(searchText)]);
						 }
						 else {
							 
							 if (hashSortAlphas.containsKey(searchText.toLowerCase().substring(0,1))) {
								 int begin = hashSortAlphas.get(searchText.toLowerCase().substring(0,1));
								 char endChar = searchText.toLowerCase().charAt(0);
								 endChar++;
								 int end = hashSortAlphas.get( (new Character(endChar)).toString());
								 for (int i = begin; i < end; i++) {
									 if (sortedItems[i].getText().length() >= len 
											 && sortedItems[i].getText().substring(0, len).toLowerCase().equals(searchText)) {
										 tree.setSelection(sortedItems[i]);
										 break;
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 
		 public void keyPressed (KeyEvent e) {
			 
		 }
	 }
	 
	 protected class TreeItemComparator implements Comparator<TreeItem> {
		 public int compare (TreeItem a, TreeItem b) {
			 return a.getText().compareTo(b.getText());
		 }
	 }
	 
	public void resetInterface() {
		//this.lstOntologies.removeAll();
		this.localTopicOntologyFiles = new ArrayList<String>();
		//this.tree.removeAll();
		this.lstSelectedTopics.removeAll();
		this.hashTopics = new HashMap<String, Integer>();
	}
	
	public void clearOntologies() {
		this.hashModels.clear();
		//this.reloadCompositeModel();
		this.lstOntologies.removeAll();
		SettingsManager.getInstance().setOntologyList(SettingsManager.TOPICS, null);
		this.reload();
	}
	
	 protected void populateSettings() {
		 String files[];
		 ArrayList<String> listFiles = new ArrayList<String>();
		 // Now get all files in the listbox, and add them to the SettingsManager.
		 files = lstOntologies.getItems();
		 for (int i = 0; i < files.length; i++) {
			 String s = lstOntologies.getDataItem(files[i]);
			 if (s == null) {
				 s = files[i];
			 }
			 listFiles.add(s);
		 }
		 SettingsManager.getInstance().setOntologyList(SettingsManager.TOPICS, listFiles);
	 }
	 
	 public void filterOntologies() {
		 // Ugh, why didn't I make a composite model structure?
		 Model composite = ModelFactory.createDefaultModel();
		 for (String key : this.hashModels.keySet()) {
			 composite.add(this.hashModels.get(key));
		 }
		 
		 ArrayList<String> contributedPropertyTypes = new ArrayList<String>(), contributedTypes = new ArrayList<String>();
		 contributedPropertyTypes.addAll(OWLUtil.getSubAndEquivalentProperties(composite, Conference.SUBTOPIC_OF));
		 contributedTypes.addAll(OWLUtil.getInferredTypes(composite, Conference.TOPIC));
		 ArrayList<String> removeList = new ArrayList<String>();
		 for (String s : this.hashModels.keySet()) {
			 Model m = this.hashModels.get(s);
			 boolean remove = true;
			 StmtIterator siter;
			 
			 for (String prop : contributedPropertyTypes) {
				 siter = m.listStatements((Resource) null, m.getProperty(prop), (RDFNode) null);
				 if (siter != null && siter.hasNext()) {
					 remove = false;
					 break;
				 }
			 }
			 if (remove) {
				 for (String type : contributedTypes) {
					 siter = m.listStatements((Resource)null, RDF.type, m.getResource(type));
					 if (siter != null && siter.hasNext()) {
						 remove = false;
						 break;
					 }
				 }
			 }
				
			 if (remove) {
				 removeList.add(s);
			 }
		 }
			
		 for (String s : removeList) {
			 this.hashModels.remove(s);
			 this.lstOntologies.removeByData(s);
			 this.populateSettings();
			 this.reload();
		 }
		 populateSettings();
		 reload();
	 }
}  //  @jve:decl-index=0:visual-constraint="18,0"
