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

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.ArrayList;

import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.hp.hpl.jena.rdf.model.*;

public class AddOntologyURIDialog {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	@SuppressWarnings("unused") private Label lblURI = null, lblLocalName = null, lblBlank = null;
	protected Button btnOk = null;
	protected Button btnCancel = null;
	protected Text txtURI = null;
	protected TableItemFileChooser fileLocalName = null;
	protected Composite parent = null;
	
	// Properties
	protected String _ontologyURL = null, _ontologyLocalPath = null, _ontologyLabel = null;
	protected Model _model = null;
	
	
	public AddOntologyURIDialog (String windowText, Composite parent) {
		createSShell();
		this.parent = parent;
		this.sShell.setText(windowText);
	}
	
	public Shell getCompositePane() {
		Shell retComposite = null;
		if (this.sShell != null && !this.sShell.isDisposed()) {
			retComposite = this.sShell;
		}
		else {
			retComposite = this.parent.getShell();
		}
		return retComposite;
	}
	
	public void open() {
		Display disp = this.sShell.getDisplay();
		this.sShell.open();
		while(!this.sShell.isDisposed()) {
			if(!disp.readAndDispatch()) {
				disp.sleep();
			}
		}
		this.sShell.dispose();
	}
	
	/**
	 * This method initializes sShell
	 */
	protected void createSShell() {
		GridLayout gridLayout = new GridLayout(4, false);
		GridData gdButton = new GridData(), 
					gdFillHoriz = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gdFillHoriz.grabExcessHorizontalSpace = true;
		gdFillHoriz.horizontalSpan = 4;
		//gdFillHoriz.horizontalAlignment = GridData.BEGINNING;
		gdButton.horizontalAlignment = GridData.END;
		gdButton.grabExcessHorizontalSpace = true;
		
		sShell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.BORDER | SWT.APPLICATION_MODAL);
		sShell.setText("Shell");
		sShell.setSize(new Point(387, 185));
		sShell.setLayout(gridLayout);

		lblURI = new Label(sShell, SWT.NONE);
		lblURI.setText("Location Ontology URL:");
		gdFillHoriz.heightHint = 13;
		lblURI.setLayoutData(gdFillHoriz);
		
		txtURI = new Text(sShell, SWT.BORDER);
		gdFillHoriz.heightHint = 17;
		txtURI.setLayoutData(gdFillHoriz);
		txtURI.addKeyListener(new URIDialogButtonListener());
		
		lblLocalName = new Label(sShell, SWT.NONE);
		lblLocalName.setText("Ontology Local Path:");
		gdFillHoriz.heightHint = 13;
		gdFillHoriz.widthHint = 200;
		lblLocalName.setLayoutData(gdFillHoriz);
		
		fileLocalName = new TableItemFileChooser(sShell, SWT.BORDER);
		gdFillHoriz.heightHint = 26;
		fileLocalName.setLayoutData(gdFillHoriz);
		fileLocalName.addKeyListener(new URIDialogButtonListener());
		
		lblBlank = new Label(sShell, SWT.NONE);
		lblLocalName.setLayoutData(gdButton);
		lblBlank = new Label(sShell, SWT.NONE);
		lblLocalName.setLayoutData(gdButton);
		
		gdButton.heightHint = 22;
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setText("OK");
		btnOk.setLayoutData(gdButton);
		btnOk.addSelectionListener(new URIDialogButtonListener());
		
		btnCancel = new Button(sShell, SWT.NONE);
		btnCancel.setLayoutData(gdButton);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new URIDialogButtonListener());
	}

	/***
	 * dispose: Note: *not* a real disposal method, this simply allows the shell to be disposed from
	 * inside the Listener class.
	 *
	 */
	public void dispose() {
		this.sShell.dispose();
	}
	
	protected class URIDialogButtonListener extends SelectionAdapter implements KeyListener {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (e.getSource().equals(btnOk)) {
				btnOkClicked(e);
			}
			else if (e.getSource().equals(btnCancel)) {
				btnCancelClicked(e);
			}
		}
	
		public void btnOkClicked (org.eclipse.swt.events.SelectionEvent e) {
			addOntology();
		}
		
		public void keyPressed (KeyEvent e) {
			if (e.keyCode == SWT.CR || e.keyCode == SWT.LF) {
				addOntology();
			}
			else if (e.keyCode == SWT.ESC) {
				dispose();
			}
		}
		
		public void keyReleased (KeyEvent e) {
			
		}
		
		@SuppressWarnings("unchecked") public void addOntology() {
			String uri = txtURI.getText(), name = fileLocalName.getText();
			Model loadedModel = null;
			boolean invalid = false;
			ArrayList<String> errorURLList = new ArrayList<String>(), 
			previousURLs = null;
			lblURI.setForeground(new Color(getCompositePane().getDisplay(), 0, 0, 0));
			if ((uri == null || uri.length() <= 0) && (name == null || name.length() <= 0)) {
				lblURI.setForeground(new Color(getCompositePane().getDisplay(), 225, 0, 0));
				lblURI.setText("Location Ontology URI: (Cannot be null)");
				invalid = true;
			}
			if (!invalid) {
				boolean foundFile = false, ontLoaded = false;
				HashMap<String, String> map;
				dispose();
				if (name != null && name.length() > 0) {
					try {
						FileInputStream fs = new FileInputStream(name);
						loadedModel = ModelFactory.createDefaultModel();
						String baseURI;
						// File exists
						baseURI = OWLUtil.getBaseURIForLocalOntology(fs, loadedModel);
						fs.close();
						if (!baseURI.equals(uri)) {
							map = SettingsManager.getInstance().getMapping();
							if (uri != null && uri.length() > 0) {
								int result;
								MessageBox mb = new MessageBox(getCompositePane(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
								mb.setText("URL and Base URI differ");
								mb.setMessage("The URL provided and the ontology's base URI differ, would you like to continue" 
										 + " using the URL provided?");
								result = mb.open();
								if (result == SWT.YES) {
									map.put(uri, name);
									_ontologyURL = uri;
								}
								else {
									map.put(baseURI, name);
									_ontologyURL = baseURI;
								}
							}
							else {
								uri = baseURI;
								map.put(baseURI, name);
								_ontologyURL = baseURI;
							}
							// Here too
							while (true)  {
								errorURLList.clear();
								loadedModel.add(OWLUtil.getImportedModels(loadedModel, errorURLList));
								if (errorURLList.size() == 0) {
									break;
								}
								else if (previousURLs != null && errorURLList.containsAll(previousURLs)){
									MessageBox mb = new MessageBox(parent.getShell(), SWT.ICON_WARNING);
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
							 		 // Dangerous, but necessary, for now.
							 		 InternalProxyEditorController proxyController 
							 		 	= new InternalProxyEditorController(null);
							 		 InternalProxyEditorWindow dialog 
							 		 	= new InternalProxyEditorWindow(proxyController, 
							 		 			"Edit URL/Local File Mappings", errorURLList);
							 		 dialog.open();
							 		 previousURLs = (ArrayList<String>) errorURLList.clone();
								}
							}	
							SettingsManager.getInstance().setMapping(map);
							_ontologyLocalPath = name;
						}
						else {
							map = SettingsManager.getInstance().getMapping();
							map.put(uri, name);
							SettingsManager.getInstance().setMapping(map);
							_ontologyURL = uri;
							_ontologyLocalPath = name;
						}
						setModel(loadedModel);
						foundFile = true;
						ontLoaded = true;
					}
					catch (FileNotFoundException fe) {
						MessageBox mb = new MessageBox(getCompositePane(), SWT.ICON_ERROR);
						mb.setText("Error opening file");
						mb.setMessage("Cannot open file: " + fe.getLocalizedMessage());
						mb.open();
						return;
					}
					catch (IOException ie) {
						MessageBox mb = new MessageBox(getCompositePane(), SWT.ICON_ERROR);
						mb.setText("Error opening file");
						mb.setMessage("Problem reading file: " + ie.getLocalizedMessage());
						mb.open();
						return;
					}
				}
				if (!foundFile){
					try {
						String localName = "";
						if (!uri.contains(".owl")) {
							throw new MalformedURLException("File must be a valid OWL file!");
						}
						map = SettingsManager.getInstance().getMapping();
						loadedModel = ModelFactory.createDefaultModel();
						
						while (true)  {
							errorURLList.clear();
							localName = OWLUtil.getModelWithProxy(uri, loadedModel, errorURLList);
							if (errorURLList.size() == 0) {
								break;
							}
							else if (previousURLs != null && errorURLList.containsAll(previousURLs)){
								MessageBox mb = new MessageBox(parent.getShell(), SWT.ICON_WARNING);
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
						 		 // Dangerous, but necessary, for now.
						 		 InternalProxyEditorController proxyController 
						 		 	= new InternalProxyEditorController(null);
						 		 InternalProxyEditorWindow dialog 
						 		 	= new InternalProxyEditorWindow(proxyController, 
						 		 			"Edit URL/Local File Mappings", errorURLList);
						 		 dialog.open();
						 		 previousURLs = (ArrayList<String>) errorURLList.clone();
							}
						}	
						
						_ontologyURL = uri;
						_ontologyLocalPath = localName;
						setModel(loadedModel);
						ontLoaded = true;
						// Spot fix to assure that if the URL *is* found, but not downloaded, we can proceed normally.
						if (_ontologyLocalPath == null) {
							_ontologyLocalPath = "";
						}
					}
					catch (MalformedURLException me) {
						MessageBox mb = new MessageBox(getCompositePane(), SWT.ICON_ERROR);
						mb.setText("Error opening URL");
						mb.setMessage("Improperly-formed URL: " + me.getLocalizedMessage());
						mb.open();
						return;
					}
					catch (IOException ie) {
						MessageBox mb = new MessageBox(getCompositePane(), SWT.ICON_ERROR);
						mb.setText("Error opening URL");
						mb.setMessage("Cannot access URL: " + ie.getLocalizedMessage());
						mb.open();
						return;
					}
				}
				if (ontLoaded) {
					/*if (loadedModel != null) {
						StmtIterator s = loadedModel.listStatements();
						while (s.hasNext()) {
							System.out.println(s.nextStatement().toString());
						}
					}*/
					setOntologyLabel(OWLUtil.getOntologyLabel(uri, getModel()));
				}
			}
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
	}

	/**
	 * @return the _locationName
	 */
	public String getLocalPath() {
		return _ontologyLocalPath;
	}

	/**
	 * @return the _locationURI
	 */
	public String getURL() {
		return _ontologyURL;
	}

	/**
	 * @return the _model
	 */
	public Model getModel() {
		return _model;
	}

	/**
	 * @param _model the _model to set
	 */
	public void setModel(Model _model) {
		this._model = _model;
	}

	/**
	 * @return the _ontologyLabel
	 */
	public String getOntologyLabel() {
		return _ontologyLabel;
	}

	/**
	 * @param label the _ontologyLabel to set
	 */
	public void setOntologyLabel(String label) {
		_ontologyLabel = label;
	}
}
