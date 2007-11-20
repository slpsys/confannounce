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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Collection;
import java.io.*;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.*;

import edu.lehigh.mab305.swproj.Application.SettingsManager;
import edu.lehigh.mab305.swproj.Topics.*;
import edu.lehigh.mab305.swproj.ConferenceModel.*;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

public class LocationPane extends Composite {

	protected ConferenceLocationController locationController;
	protected LocationPaneController controller;  //  @jve:decl-index=0:
	protected Tree tree = null;
	protected Model compositeModel = null;
	protected ArrayList<String> localLocationOntologyFiles = null, locationOntologyURIs = null;
	protected Button btnAddOntology = null, btnRemOntology = null, btnReloadTree = null, 
			btnAddSelection = null, btnRemoveSelection = null;
	protected Label lblOntologies = null, lblTree = null, lblAddress = null, lblSelectedLocations = null;
	protected DataList<String> lstOntologies = null;
	protected Tree treeSelectedLocation = null; //, lstSelectedLocation = null;
	protected Menu contextTreeMenu = null;
	protected MenuItem itemTreeReload = null;
	protected Text txtAddress = null;
	protected Location newLocation = null;
	protected HashMap<String,Model> hashModels = null;
	
	// Search Fields
	protected Label lblSearch = null;
	protected Text txtSearch = null;
	protected Button btnSearch = null;
	protected TreeItem sortedItems[] = null;	
	protected HashMap<String, Integer> hashSortAlphas = new HashMap<String, Integer>();
	
	public LocationPane(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		
		// Make changes here
		this.localLocationOntologyFiles = new ArrayList<String>();
		this.locationOntologyURIs = new ArrayList<String>();
		hashModels = new HashMap<String,Model>();
		//this.localTopicOntologyFiles.add(ConferenceTopicController.ONTO_FILE);
		initialize();
	}

	protected void initialize() {
		SearchAdapter searcher = new SearchAdapter();
		lblAddress = new Label(this.getCompositePane(), SWT.NONE);
		lblAddress.setBounds(new Rectangle(7, 6, 200, 13));
		lblAddress.setText("Street Address:");
		txtAddress = new Text(this.getCompositePane(), SWT.BORDER);
		txtAddress.setBounds(new Rectangle(7, 23, 406, 19));
		
		tree = new Tree(this, SWT.BORDER);
		tree.setBounds(new Rectangle(7, 63, 406, 302)); // NEW
		tree.addMouseListener(new LocationPaneButtonListener());
		tree.addKeyListener(new LocationPaneButtonListener());
		lblTree = new Label(this.getCompositePane(), SWT.NONE);
		lblTree.setBounds(new Rectangle(7, 48, 135, 13));
		lblTree.setText("Locations:");
		
		// Set tree context menu
		this.contextTreeMenu = new Menu(this.getCompositePane().getShell(), SWT.POP_UP);
		tree.setMenu(contextTreeMenu);
		
		this.itemTreeReload = new MenuItem(contextTreeMenu, SWT.PUSH);
		itemTreeReload.setText("Reload");
		itemTreeReload.addSelectionListener(new LocationPaneButtonListener());
		
		lstOntologies = new DataList<String>(this.getCompositePane(), SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		lstOntologies.setBounds(new Rectangle(420, 23, 196, 91));
		lstOntologies.addKeyListener(new LocationPaneButtonListener());
		lblOntologies = new Label(this.getCompositePane(), SWT.NONE);
		lblOntologies.setBounds(new Rectangle(420, 6, 255, 13));
		lblOntologies.setText("Location Ontologies Loaded:");
		
		btnReloadTree = new Button(this.getCompositePane(), SWT.NONE);
		btnReloadTree.setBounds(new Rectangle(420, 118, 86, 23));
		btnReloadTree.setText("Reload List");
		btnReloadTree.addSelectionListener(new LocationPaneButtonListener());
		
		btnAddOntology = new Button(this.getCompositePane(), SWT.NONE);
		btnAddOntology.setBounds(new Rectangle(515, 118, 46, 23));
		btnAddOntology.setText("Add");
		btnAddOntology.addSelectionListener(new LocationPaneButtonListener());
		
		btnRemOntology = new Button(this.getCompositePane(), SWT.NONE);
		btnRemOntology.setBounds(new Rectangle(561, 118, 56, 23));
		btnRemOntology.setText("Remove");
		
		btnRemOntology.addSelectionListener(new LocationPaneButtonListener());
		
		lblSelectedLocations = new Label(this.getCompositePane(), SWT.NONE);
		lblSelectedLocations.setBounds(new Rectangle(420, 148, 196, 17));
		lblSelectedLocations.setText("Selected Location:");
		
		/*lstSelectedLocation = new DataList<String>(this.getCompositePane(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		lstSelectedLocation.setBounds(new Rectangle(420, 165, 196, 200));
		lstSelectedLocation.addKeyListener(new LocationPaneButtonListener());*/
		treeSelectedLocation = new Tree(this.getCompositePane(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		treeSelectedLocation.setBounds(new Rectangle(420, 165, 196, 200));
		treeSelectedLocation.addKeyListener(new LocationPaneButtonListener());
		
		btnAddSelection = new Button(this.getCompositePane(), SWT.PUSH);
		btnAddSelection.setBounds(new Rectangle(515, 365, 46, 23));
		btnAddSelection.setText("Add");
		btnAddSelection.addSelectionListener(new LocationPaneButtonListener());
		btnRemoveSelection = new Button(this.getCompositePane(), SWT.PUSH);
		btnRemoveSelection.setBounds(new Rectangle(561, 365, 56, 23));
		btnRemoveSelection.setText("Remove");
		btnRemoveSelection.addSelectionListener(new LocationPaneButtonListener());
		
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
	
	public void expandAll(Tree tree) {
		TreeItem items[] = tree.getItems();
		
		for (TreeItem item : items) {
			expandAllHelper(item);
		}
	}
	
	protected void expandAllHelper(TreeItem item) {
		TreeItem items[] = item.getItems();
		item.setExpanded(true);
		for (TreeItem child : items) {
			expandAllHelper(child);
		}
	}
	
	public Composite getCompositePane() {
		return this;
	}

	public String getConferenceAddress() {
		return this.txtAddress.getText();
	}
	
	public void setConferenceAddress(String address) {
		this.txtAddress.setText(address);
	}
	
	protected void reloadCompositeModel() {
		Collection<Model> c;
		Model m = ModelFactory.createDefaultModel();
		c = this.hashModels.values();
		for (Model model : c) {
			m.add(model); 
		}
		this.compositeModel = m;
	}
	
	protected void buildTree(GridData grid, GridLayout layout) {
		HierarchyStringTree dataTree;
		//ArrayList<HierarchyTreeNode<String>> children;
		ArrayList<TreeItem> uiItems = new ArrayList<TreeItem>();
		TreeItem curItem;
		
		this.tree.removeAll();
		locationController.buildLocationTree();
		dataTree = locationController.getTree();
		this.locationOntologyURIs = locationController.getOntologyURIs();
		
		this.setLayout(layout);
				
		if (dataTree.getRoot() != null) {
			Object[] children;
			curItem = new TreeItem(tree,SWT.NULL);
			uiItems.add(curItem);
			curItem.setText("Root");
			children = sortChildren(dataTree.getRoot().getChildren());
			
			for (int i = 0; i < children.length; i++) {
				try {
					HierarchyStringTreeNode node = (HierarchyStringTreeNode) children[i];
					buildTreeHelper(curItem, node, uiItems);
				}
				catch (ClassCastException ce) {
					
				}
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
	
	protected Object[] sortChildren(ArrayList<HierarchyStringTreeNode> c) {
		Object sortedChildren[];
		sortedChildren = c.toArray();
		Arrays.sort(sortedChildren, new Comparator<Object> () {
			public int compare (Object a, Object b) {
				int ret = 0;
				try {
					HierarchyStringTreeNode hA, hB;
					hA = (HierarchyStringTreeNode) a;
					hB = (HierarchyStringTreeNode) b;
					ret = hA.getShortName().compareToIgnoreCase(hB.getShortName());
				}
				catch (ClassCastException ce) {
					
				}
				return ret;
			}
			
			public boolean equals (Object obj) {
				return this.equals(obj);
			}
		}
				);
		return sortedChildren;
	}
	
	protected void buildTreeHelper(TreeItem curItem, HierarchyStringTreeNode node, ArrayList<TreeItem> uiItems) {
		Object children[];
		TreeItem item;
		
		children = sortChildren(node.getChildren());
		
		item = new TreeItem(curItem,SWT.NULL);
		uiItems.add(item);
		item.setText(((node.getShortName() != null) ? node.getShortName() : node.getData()));
		item.setData(node);
		for (int i = 0; i < children.length; i++ ) {
			try {
				HierarchyStringTreeNode curnode = (HierarchyStringTreeNode) children[i];
				buildTreeHelper(item, curnode, uiItems);
			}
			catch (ClassCastException ce) {
				
			}
		}
	}
	
	/**
	 * @return the controller
	 */
	public LocationPaneController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(LocationPaneController controller) {
		this.controller = controller;
		this.controller.setPane(this);
	}
	
	 public ArrayList<String> getLocationOntologies() {
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
	 
	 public ArrayList<Model> getLocationModels() {
		 ArrayList<Model> a = new ArrayList<Model>();
		 a.addAll(this.hashModels.values());
		 this.reloadCompositeModel();
		 return a;
	 }
	 
	 public String getConferenceLocationURI() {
		 //String[] items = this.lstSelectedLocation.getItems();
		 String ret = null;

		 if (treeSelectedLocation.getItemCount() > 0) {
			 TreeItem curNode = this.treeSelectedLocation.getTopItem();
			 if (curNode != null) {
				 while (curNode.getItemCount() > 0) {
					 curNode = curNode.getItem(0);
				 }
				 String s = curNode.getData().toString();
				 if ((s != null && s.length() > 0) || s.charAt(0) == '#') {
					 // There's a new location.
					 ret = "";
				 }
				 else {
					 ret = s;
				 }
			 }
		 }
		 return ret;
	 }
	 
	 public Location getConferenceLocation() {
		 Location retLocation = null;
		 if (this.getNewLocation() != null) {
			 String parentURI = null;
			 
			 retLocation = this.getNewLocation();
			 parentURI = retLocation.getParentURI();
			 if (parentURI != null) {
				 String type = OWLUtil.getLocationType(this.compositeModel, parentURI);
				 retLocation.setParentTypeURI(type);
			 }
		 }
		 return retLocation;
	 }
	 
	 public void resetInterface() {
		 //this.lstOntologies.removeAll();
		 this.localLocationOntologyFiles = new ArrayList<String>();
		 //this.lstSelectedLocation.removeAll();
		 this.treeSelectedLocation.removeAll();
		 this.txtAddress.setText("");
		 reload();
		 //this.tree.removeAll();
	 }
	
	 @SuppressWarnings("unchecked") public void initSavedOntologies() {
		 ArrayList<String> errorURLs = new ArrayList<String>(), loadedOnts = 
			 SettingsManager.getInstance().getOntologyList(SettingsManager.LOCATIONS);
		 if (loadedOnts != null) {
			 for (String ont : loadedOnts) {
				 // Make sure the files are there first.
				 Model m = ModelFactory.createDefaultModel();

				 try {
					 ArrayList<String> previousURLs = null;
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
					 String label = OWLUtil.getOntologyLabel(ont, m);
					 this.hashModels.put(ont, m);
					 reloadCompositeModel();
					 if (label != null && label.length() > 0) {
						 this.lstOntologies.add(label, ont);
					 }	
					 else {
						 this.lstOntologies.add(ont);
					 }
				 }
				 catch (IOException ie) {
					 MessageBox mb = new MessageBox(getCompositePane().getShell(), SWT.ICON_ERROR);
					 mb.setText("Error loading file");
					 mb.setMessage(ie.getMessage());
				 }
			 }
			 
		 }
		 reload();
	 }
	 
	 protected void reload() {
		 LocationPaneController c = controller;
		 ArrayList<String> onts = c.getLocalLocationOntologies();
		 if (onts != null && onts.size() > 0) {
			 locationController = new ConferenceLocationController(controller.getLocationModels());
			
			 buildTree(null, null);
		 }
		 else {
			 getController().pane.tree.removeAll();
		 }
	 }
	 
	 public void setConferenceLocation(String uri) {
		 reload();
		 setConferenceLocationInternal(uri);
		 expandAll(treeSelectedLocation);
	 }
	 
	 protected void setConferenceLocationInternal(String uri) {
		 if (uri != null && uri.length() > 0) {
			 ArrayList<String> hierarchy = OWLUtil.getLocationHierarchyAsList(locationController.getModel(), uri, locationController.getSubLocationURIs());
			 int i = 0;
	
			 TreeItem parent = null;
			 for (String s : hierarchy) {
				 String label = OWLUtil.getRDFSLabel(locationController.getModel(), s);
				 TreeItem t;
				 if (0 == i) {
					 t = new TreeItem(treeSelectedLocation, SWT.NULL);
				 }
				 else {
					 t = new TreeItem(parent, SWT.NULL);
				 }
				 if (label != null) {
					 t.setText(label);
				 }
				 else {
					 t.setText("Error Loading RDFS Label for Location");
				 }
				 t.setData(s);
				 parent = t;
				 i++;
			 }
		 }
	 }
	 
	 protected void setConferenceLocationInternal(String uri, Location newLoc) {
		 ArrayList<String> hierarchy = OWLUtil.getLocationHierarchyAsList(locationController.getModel(), uri, locationController.getSubLocationURIs());
		 int i = 0;
		 TreeItem parent = null;
		 for (String s : hierarchy) {
			 String label = OWLUtil.getRDFSLabel(locationController.getModel(), s);
			 TreeItem t;
			 if (0 == i) {
				 t = new TreeItem(treeSelectedLocation, SWT.NULL);
			 }
			 else {
				 t = new TreeItem(parent, SWT.NULL);
			 }
			 if (label == null) {
				 if (newLoc == null) {
					 t.setText("Error Loading RDFS Label for Location");
				 }
				 else {
					 t.setText(newLoc.getDisplayName());
				 }
			 }
			 else {
				 t.setText(label);
			 }
			 t.setData(s);
			 parent = t;
			 i++;
		 }
	 }
	 
	 public void addOntology(String uri) {
		 
		 if (!this.hashModels.containsKey(uri)) {
			 try {
				 String label = null;
				 Model m = ModelFactory.createDefaultModel();
				 OWLUtil.getModelWithProxy(uri, m, new ArrayList<String>());
				 this.hashModels.put(uri, m);
				 this.reloadCompositeModel();
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
		 SettingsManager.getInstance().setOntologyList(SettingsManager.LOCATIONS, listFiles);
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
				 this.reloadCompositeModel();
			 }
			 populateSettings();
			 reload();
		 }
	 }
	 
	 protected class LocationPaneButtonListener extends org.eclipse.swt.events.SelectionAdapter
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
			 String uri = null;
			 Location loc = null;
			 boolean addLocationClickedOn = false, newLocation = false;
			 if (items.length > 0 && items[0].getData() instanceof edu.lehigh.mab305.swproj.Topics.HierarchyStringTreeNode) {
				 //s = ((edu.lehigh.mab305.swproj.Topics.HierarchyStringTreeNode) items[0].getData()).getData();
				 if (locationController.getModel() != null && items[0].getData() != null) {
					 // Find out what the location type the user clicked on
					 String typeURI = OWLUtil.getLocationType(locationController.getModel(), items[0].getData().toString());
					 
					 if (typeURI != null && (typeURI.equals(Conference.COUNTRY) || typeURI.equals(Conference.PROVINCE) 
							 || typeURI.equals(Conference.STATE))) {
						 NewLocationController newLocController = new NewLocationController(getController().getController());
						 NewLocationDialog dialog = new NewLocationDialog(newLocController, 
								 "Create a new city within this location?");
						 dialog.open();
						 
						 if (items[0].getData() != null && dialog.getLocationName() != null) {
							 Model m;
							 Resource locRes;
							 loc = new Location(items[0].getData().toString(), typeURI, dialog.getLocationURI());
							 loc.setDisplayName(dialog.getLocationName());
							 setNewLocation(loc);
							 
							 // Add the axiom to the location controller's model
							 m = locationController.getModel();
							 locRes = m.createResource(loc.getLocationURI());
							 m.add(m.createStatement(locRes, RDF.type, m.getResource(Conference.CITY)));
							 // Add the label name
							 m.add(m.createStatement(locRes, RDFS.label, m.createResource(dialog.getLocationName())));
							 // Add the isIn property
							 m.add(m.createStatement(locRes, m.getProperty(Conference.IS_IN_URI), m.createResource(items[0].getData().toString())));
							 
							 /*lstSelectedLocation.removeAll();
							 lstSelectedLocation.add(dialog.getLocationName(), "");*/
							 treeSelectedLocation.removeAll();
							 uri = loc.getLocationURI();
							 newLocation = true;
						 }
						 else {
							 uri = ((HierarchyStringTreeNode)items[0].getData()).getData();
							 /*lstSelectedLocation.removeAll();
							 lstSelectedLocation.add(items[0].getText(), uri);*/
							 treeSelectedLocation.removeAll();
						 }
					 }
					 else { 
						 addLocationClickedOn = true;
					 }
				 }
				 if (addLocationClickedOn) {
					 uri = ((HierarchyStringTreeNode)items[0].getData()).getData();
					 /*lstSelectedLocation.removeAll();
					 lstSelectedLocation.add(items[0].getText(), uri);*/
					 treeSelectedLocation.removeAll();
				 }
				 if (uri != null && uri.length() > 0) {
					 if (newLocation) {
						 setConferenceLocationInternal(uri, loc);
					 }
					 else {
						 setConferenceLocationInternal(uri);
					 }
				 }
			 }
			 
			 expandAll(treeSelectedLocation);
		 }
		 
		 public void removeSelections() {
			 treeSelectedLocation.removeAll();
			 // I don't understand the hashModels line..the data item is a string, and won't do anything.
			 /*String selections[] = treeSelectedLocation.getSelection();
			 for (String s : selections) {
				 hashModels.remove(lstSelectedLocation.getDataItem(s));
				 lstSelectedLocation.remove(s);
			 }*/
		 }
		 
		 protected void removeOntologies() {
			 String strs[] = lstOntologies.getSelection();
			 ArrayList<String> list = new ArrayList<String>();
			 for (int i = 0; i < strs.length; i++) {
				 lstOntologies.remove(strs[i]);
			 }
			 for (String s :lstOntologies.getItems()) {
				 list.add(s);
			 }
			 SettingsManager.getInstance().setOntologyList(SettingsManager.LOCATIONS, list);
		 }
		 
		 public void mouseDoubleClick(MouseEvent e) {
			 addSelections();
		 }
		 
		 public void keyPressed(KeyEvent e) {
			 if (e.getSource().equals(tree) && (e.keyCode == SWT.LF || e.keyCode == SWT.CR)) {
				 addSelections();
			 }
			 else if (treeSelectedLocation.equals(e.getSource()) && (e.keyCode == SWT.BS || e.keyCode == SWT.DEL)) {
				 removeSelections();
			 }
			 else if (lstOntologies.equals(e.getSource())) {
				 if (e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
				 removeOntologies();
				 }
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

	/**
	 * @return the locationOntologyURIs
	 */
	public ArrayList<String> getLocationOntologyURIs() {
		return this.getLocationOntologies();
	}

	/**
	 * @param locationOntologyURIs the locationOntologyURIs to set
	 */
	public void setLocationOntologyURIs(ArrayList<String> locationOntologyURIs) {
		this.locationOntologyURIs = locationOntologyURIs;
	}

	/**
	 * @return the newLocation
	 */
	public Location getNewLocation() {
		return newLocation;
	}

	/**
	 * @param newLocation the newLocation to set
	 */
	public void setNewLocation(Location newLocation) {
		this.newLocation = newLocation;
	}
	
	public void clearOntologies() {
		this.hashModels.clear();
		this.reloadCompositeModel();
		this.lstOntologies.removeAll();
		SettingsManager.getInstance().setOntologyList(SettingsManager.LOCATIONS, null);
		this.reload();
	}
	
	public void filterOntologies() {
		ArrayList<String> contributedPropertyTypes = new ArrayList<String>(), contributedTypes = new ArrayList<String>();
		contributedPropertyTypes.addAll(OWLUtil.getSubAndEquivalentProperties(this.compositeModel, Conference.IS_IN_URI));
		contributedTypes.addAll(OWLUtil.getInferredTypes(this.compositeModel, Conference.LOCATION));
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
			this.reloadCompositeModel();
			this.populateSettings();
			this.reload();
		}
		populateSettings();
		reload();
	}

}  //  @jve:decl-index=0:visual-constraint="18,0"
