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

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import edu.lehigh.mab305.swproj.Application.ImageFileConstants;
import edu.lehigh.mab305.swproj.Application.LoadProgressWindow;
import edu.lehigh.mab305.swproj.Application.Progress;
import edu.lehigh.mab305.swproj.Application.SettingsManager;

import edu.lehigh.mab305.swproj.ConferenceModel.Conference;
import edu.lehigh.mab305.swproj.ConferenceModel.ConferenceSeries;
import edu.lehigh.mab305.swproj.ConferenceModel.Conf2OWLConverter;
import edu.lehigh.mab305.swproj.ConferenceModel.OWL2ConfReader;
import edu.lehigh.mab305.swproj.ConferenceModel.OWL2ConfException;

public class ConfAnnounceWindow implements Observer {

	protected static String INFO_PANE_TOOLTIP = "Enter/edit general Conference information";
	protected static String TOPIC_PANE_TOOLTIP = "Load a set of local Conference Topic ontologies and select " +
												"one or several to associate with this Conference";
	protected static String LOCATION_PANE_TOOLTIP = "Load a set of local Location ontologies and select a " +
													"location for the conference";
	protected static String FULLTEXT_PANE_TOOLTIP = "Enter/edit the full textual Conference announcement";
	protected static String SERIES_PANE_TOOLTIP = "Enter/edit information pertaining to a series this Conference " +
												"is in";
	
	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="20,11"
	protected ConfAnnounceController controller = null;  //  @jve:decl-index=0:
	protected Menu menuBar = null;
	protected Menu fileMenu = null;
	MenuItem fileMenuItem = null, newMenuItem = null, exitMenuItem = null, publishItem = null, sep = null,
					entryPaneMenuItem = null, proxyMenuItem = null, openOntItem = null, proxyEditorItem = null,
					chkDownloadImportsMenuItem= null;
	
	// Panes/Tabs
	protected CTabFolder tabMainFolder = null;
	protected CTabItem tabInfoPane = null, tabTopicPane = null, tabFulltextPane = null,
					tabLocationPane = null, tabSeriesPane = null;
	protected TestPane paneConfInfo = null;
	protected ConfInfoPaneController ctlConfInfo = null;
	protected TopicPane paneTopic = null;
	protected TopicPaneController ctlTopicPane = null;
	protected FulltextPane paneFulltext = null;
	protected FulltextPaneController ctlFulltext = null;
	protected LocationPane paneLocation = null;
	protected LocationPaneController ctlLocation = null;
	protected SeriesPane paneSeries = null;
	protected SeriesPaneController ctlSeriesPane = null;
	protected ToolBar toolBar = null;	
	@SuppressWarnings("unused")protected ToolItem newToolItem = null, ecToolItem = null, openToolItem = null, 
				publishToolItem = null, separatorToolItem = null;
	
	// For use with multithreaded observer/observable
	protected String msgBuffer;
	protected Conference confBuffer;
	protected Progress progress;
	protected LoadProgressWindow progressWindow = null;
	
	public ConfAnnounceWindow(ConfAnnounceController controller) {
		this.controller = controller;
		createSShell();
		controller.setWindow(this);
		controller.setInfoPaneController(this.ctlConfInfo);
		this.ctlConfInfo.setController(controller);
		controller.setFulltextPaneController(this.ctlFulltext);
		controller.setLocationPaneController(this.ctlLocation);
		controller.setSeriesPaneController(this.ctlSeriesPane);
		controller.setTopicPaneController(this.ctlTopicPane);
		
		// This MUST go last, as all the controllers need to be initialized before 
		// building the trees.
		this.paneTopic.initSavedOntologies();
		this.paneLocation.initSavedOntologies();
	}
	
	public void runConfAnnounce() {
		Display disp = this.sShell.getDisplay();
		//this.sShell.pack();
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
	private void createSShell() {
		GridLayout shellLayout = new GridLayout();
		Display display;
		shellLayout.numColumns = 1;
		shellLayout.makeColumnsEqualWidth = true;
		Color gradient[];
		
		sShell = new Shell();
		sShell.setText("ConfAnnounce - Easy Conference Metadata publishing");
		sShell.setSize(new Point(650, 540));
		sShell.setLayout(shellLayout);
		sShell.setImage(new Image(sShell.getDisplay(), ImageFileConstants.APP_ICON));
		
		try {
			this.toolBar = new ToolBar(sShell, SWT.HORIZONTAL);
		    //this.coolBar.setLayoutData(new GridData(SWT));
			this.toolBar.setLayout(new FillLayout());
		    
			this.newToolItem = new ToolItem(toolBar, SWT.FLAT);
			this.newToolItem.setImage(new Image(sShell.getDisplay(), ImageFileConstants.NEW_ICON));
			this.newToolItem.addSelectionListener(new ConfAnnounceWindowItemListener());
			this.newToolItem.setToolTipText("New Conference");
			
		    this.ecToolItem = new ToolItem(toolBar, SWT.FLAT);
		    this.ecToolItem.setImage(new Image(sShell.getDisplay(), ImageFileConstants.EASYCONF_ICON));
		    this.ecToolItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		    this.ecToolItem.setToolTipText("Create a new Conference using EasyConf");
		    
		    this.separatorToolItem = new ToolItem(toolBar, SWT.SEPARATOR);
		    
		    this.openToolItem = new ToolItem(toolBar, SWT.FLAT);
		    this.openToolItem.setImage(new Image(sShell.getDisplay(), ImageFileConstants.OPEN_ICON));
		    this.openToolItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		    this.openToolItem.setToolTipText("Open a Conference ontology");
		    
		    this.publishToolItem = new ToolItem(toolBar, SWT.FLAT);
		    this.publishToolItem.setImage(new Image(sShell.getDisplay(), ImageFileConstants.SAVE_ICON));
		    this.publishToolItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		    this.publishToolItem.setToolTipText("Save Conference ontology!");
		}
		catch (SWTException se) {
			
		}
		display = sShell.getDisplay();
		gradient =  new Color [] { new Color(display, 0, 0, 255),
				new Color(display, 20, 20, 255),
				new Color(display, 40, 40, 255),
				new Color(display, 60, 60, 255),
				new Color(display, 80, 80, 255) };
		
		this.tabMainFolder = new CTabFolder(sShell, SWT.TOP | SWT.BORDER);
		this.tabMainFolder.setSimple(false);
		this.tabMainFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
	    this.tabMainFolder.setBorderVisible(true);
	    
	    this.tabMainFolder.setSelectionBackground(gradient, new int[] {20, 40, 60, 80});
	    this.tabMainFolder.setSelectionForeground(display.getSystemColor(SWT.COLOR_WHITE));
	    
		this.tabInfoPane = new CTabItem(this.tabMainFolder, SWT.NULL);
		this.tabInfoPane.setText("Main Info");
		this.tabInfoPane.setToolTipText(INFO_PANE_TOOLTIP);
		
		this.tabTopicPane = new CTabItem(this.tabMainFolder, SWT.NULL);
		this.tabTopicPane.setText("Conference Topics");
		this.tabTopicPane.setToolTipText(TOPIC_PANE_TOOLTIP);
		
		this.tabLocationPane = new CTabItem(this.tabMainFolder, SWT.NULL);
		this.tabLocationPane.setText("Location");
		this.tabLocationPane.setToolTipText(LOCATION_PANE_TOOLTIP);
		
		this.tabFulltextPane = new CTabItem(this.tabMainFolder, SWT.NULL);
		this.tabFulltextPane.setText("Announcement Full Text");
		this.tabFulltextPane.setToolTipText(FULLTEXT_PANE_TOOLTIP);
		
		this.tabSeriesPane = new CTabItem(this.tabMainFolder, SWT.NULL);
		this.tabSeriesPane.setText("Conference Series");
		this.tabSeriesPane.setToolTipText(SERIES_PANE_TOOLTIP);
		
		this.ctlConfInfo = new ConfInfoPaneController(this.controller.getConf());
		this.paneConfInfo = new TestPane(this.tabMainFolder, SWT.NULL, this.ctlConfInfo);
		this.ctlConfInfo.setPane(this.paneConfInfo);
		this.tabInfoPane.setControl(this.paneConfInfo);
		
		this.ctlLocation = new LocationPaneController(controller);
		this.paneLocation = new LocationPane(this.tabMainFolder, SWT.NULL);
		this.ctlLocation.setPane(this.paneLocation);
		this.paneLocation.setController(this.ctlLocation);
		this.tabLocationPane.setControl(this.paneLocation);
		
		this.ctlFulltext = new FulltextPaneController(this.controller);
		this.paneFulltext = new FulltextPane(this.tabMainFolder, SWT.NULL);
		this.ctlFulltext.setPane(this.paneFulltext);
		this.paneFulltext.setController(this.ctlFulltext);
		this.tabFulltextPane.setControl(this.paneFulltext);
		
		this.ctlSeriesPane = new SeriesPaneController(controller);
		this.paneSeries = new SeriesPane(this.tabMainFolder, SWT.NULL, this.ctlSeriesPane);
		this.ctlSeriesPane.setPane(this.paneSeries);
		this.paneSeries.setController(this.ctlSeriesPane);
		this.tabSeriesPane.setControl(this.paneSeries);
		
		this.ctlTopicPane = new TopicPaneController(controller);
		this.paneTopic = new TopicPane(this.tabMainFolder, SWT.NULL);
		this.ctlTopicPane.setPane(this.paneTopic);
		this.tabTopicPane.setControl(this.paneTopic);
		this.paneTopic.setController(this.ctlTopicPane);
		
		this.tabMainFolder.forceFocus();
		buildMenu();
		this.publishToolItem.setEnabled(false);
		this.publishItem.setEnabled(false);
	}
	
	private void buildMenu() {
		
		menuBar = new Menu(sShell, SWT.BAR);
		sShell.setMenuBar(menuBar);
		fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuItem.setText("File");
		
		fileMenu = new Menu (sShell, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);
				
		newMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
		newMenuItem.setText("New Conference");
		newMenuItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		entryPaneMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
		entryPaneMenuItem.setText("EasyConf...");
		entryPaneMenuItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		sep = new MenuItem(fileMenu, SWT.SEPARATOR);
		
		proxyMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
		proxyMenuItem.setText("Internet Proxy Settings...");
		proxyMenuItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		proxyEditorItem = new MenuItem(fileMenu, SWT.CASCADE);
		proxyEditorItem.setText("Edit URL/Local File Mappings...");
		proxyEditorItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		chkDownloadImportsMenuItem = new MenuItem(fileMenu, SWT.CHECK);
		chkDownloadImportsMenuItem.setText("Save Imported Ontologies to Disk");
		chkDownloadImportsMenuItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		if (SettingsManager.getInstance().getDownloadImports()) {
			chkDownloadImportsMenuItem.setSelection(true);
		}
		
		sep = new MenuItem(fileMenu, SWT.SEPARATOR);
		
		openOntItem = new MenuItem(fileMenu, SWT.CASCADE);
		openOntItem.setText("Open Conference File...");
		openOntItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		publishItem = new MenuItem(fileMenu, SWT.CASCADE);
		publishItem.setText("Save Conference To File...");
		publishItem.addSelectionListener(new ConfAnnounceWindowItemListener());
		
		sep = new MenuItem(fileMenu, SWT.SEPARATOR);
		
		exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
		exitMenuItem.setText("E&xit");
		exitMenuItem.addSelectionListener(new ConfAnnounceWindowItemListener());
	}

	/**
	 * @return the controller
	 */
	public ConfAnnounceController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(ConfAnnounceController controller) {
		this.controller = controller;
	}

	/* Control methods */
	 public void setConferenceStartDate(String date) {
		 this.paneConfInfo.getController().setConferenceStartDate(date);
		 //this.txtConfStartDate.setText(date);
	 }
	 
	 public void setConferenceEndDate(String date) {
		 this.paneConfInfo.getController().setConferenceEndDate(date);
		 //this.txtConfEndDate.setText(date);
	 }
	 
	 public void setConferencePaperSubmissionDeadline(String date) {
		 this.paneConfInfo.getController().setConferencePaperSubmissionDeadline(date);
		 //this.txtConfPaperDate.setText(date);
	 }
	 
	 public void setConferenceName(String title) {
		 this.paneConfInfo.getController().setConferenceName(title);
		 //this.txtConfTitle.setText(title);
	 }
	 
	 public void setConferenceWebsite(String website) {
		 this.paneConfInfo.getController().setConferenceWebsite(website);
		 //this.txtConfWebsite.setText(website);
	 }

	 public void setConferenceAddress(String address) {		
		 this.paneLocation.getController().setConferenceAddress(address);
		 //this.txtAddress.setText(address);
	 }
	 
	 /* Accessors */

	 /* Control methods */
	 public String getConferenceStartDate() {
		 return this.paneConfInfo.getController().getConferenceStartDate();
	 }
	 
	 public String getConferenceEndDate() {
		 return this.paneConfInfo.getController().getConferenceEndDate();
	 }
		 
	 public String getConferencePaperSubmissionDeadline() {
		 return this.paneConfInfo.getController().getConferencePaperSubmissionDeadline();	 
	 }
		 
	 public String getConferenceName() {
		 return this.paneConfInfo.getController().getConferenceName();	 
	 }
		 
	 public String getConferenceWebsite() {
		 return this.paneConfInfo.getController().getConferenceWebsite();	 
	 }
		 
	 public String getConferenceAddress() {		
		 return this.paneLocation.getController().getConferenceAddress();
	 }
	
	 public void addLocalTopicOntology(String ontfile) {
		 //this.listOntologies.add(ontfile);
		 this.controller.getConf().addTopicOntologyURI(ontfile);
	 }
	
	 public void publish() {
		 FileDialog fd = new FileDialog(sShell, SWT.SAVE);
		 String outfile = "";
		 Conf2OWLConverter conv;
		 Conference c = controller.getConf();
		 ConferenceSeries s = null;
		 pullConfProperties(c);
		 
		 outfile = fd.open();
		 if (outfile != null) {
			 String seriesURI = this.controller.getSeriesBaseURI();
			 String seriesName = this.controller.getSeriesName();
			 if (this.controller.getSeriesPaneController().isEditable() && seriesURI != null 
					 && seriesURI.length() > 0 && seriesName != null && seriesName.length() > 0) {
				 s = pullConfSeriesProperties();
				 conv = new Conf2OWLConverter(c,s);
			 }
			 else {
				 conv = new Conf2OWLConverter(c);
				 if (seriesURI != null && seriesURI.length() > 0 && seriesName != null && seriesName.length() > 0) {
					 conv.importOntology(this.controller.getSeriesBaseURI());
				 }
			 }
			 conv.setCallerShell(this.getSShell());
			 conv.addObserver(this);
			 conv.setOutputFileName(outfile);
			 conv.convert();
		 }
	 }
	 
	 public void setPublishable(boolean publishable) {
		 this.publishToolItem.setEnabled(publishable);
		 this.publishItem.setEnabled(publishable);
	 }
	 
	 public boolean getPublishable() {
		 return this.publishItem.getEnabled();
	 }
	 
	 protected void pullConfSeriesProperties(ConferenceSeries s) {
		 s.setBaseURI(controller.getBaseURI());
		 s.setConstituentConferences(controller.getSeriesConstituentConferences());
		 s.setOccursIn(controller.getSeriesOccursIn());
		 s.setSubmissionsDueIn(controller.getSeriesSubmissionsDueIn());
		 s.setSeriesTitle(controller.getSeriesName());
	 }
	 
	 protected ConferenceSeries pullConfSeriesProperties() {
		 ConferenceSeries s;
		 s = new ConferenceSeries(controller.getSeriesPaneController().getSeriesURI());
		 s.setConstituentConferences(controller.getSeriesConstituentConferences());
		 s.setOccursIn(controller.getSeriesOccursIn());
		 s.setSubmissionsDueIn(controller.getSeriesSubmissionsDueIn());
		 s.setSeriesTitle(controller.getSeriesName());
		 s.setTopicAreas(controller.getSeriesTopicAreas());
		 s.setWebsite(controller.getSeriesWebsite());
		 return s;
	 }
	 
	 protected void pullConfProperties(Conference c) {
		 String s;
		 ArrayList<String> ontlist;
			c.setConferenceName(controller.getConferenceName());
			c.setConferenceWebsite(controller.getConferenceWebsite());
			c.setConferenceStartDate(controller.getConferenceStartDate());
			c.setConferenceEndDate(controller.getConferenceEndDate());
			c.setConferencePaperSubmissionDeadline(controller.getConferencePaperSubmissionDeadline());
			c.setConferenceAbstractSubmissionDeadline(controller.getConferenceAbstractSubmissionDeadline());
			c.setAddress(controller.getConferenceAddress());
			c.setAnnouncementFullText(controller.getAnnouncementFulltext());

			s = controller.getLocationURI();
			if (s != null) {
				ArrayList<String> a = controller.getLocationPaneController().getLocationOntologies();
				c.addLocationOntologyURIs(a);
			}
			if (s != null && controller.getLocationPaneController().getNewLocation() != null) {
				c.setLocation(controller.getLocationPaneController().getNewLocation());
			}
			else {
				c.setLocatedInURI(controller.getLocationURI());
			}
			c.setConferenceTopicArea(controller.getConferenceTopicAreas());
			
			ontlist = controller.getConferenceTopicAreas();
			if (ontlist != null && ontlist.size() > 0) {
				for (String str : controller.getTopicPaneController().getTopicOntologies()) {
					c.addTopicOntologyURI(str);
				}
			}
			
			if (controller.getBaseURI().charAt(controller.getBaseURI().length()-1) != '#') {
				if (controller.getBaseURI().contains("#")) {
					// Misplaced # symbol in user input, erase everything after the '#'
					String str = controller.getBaseURI();
					controller.setBaseURI(str.substring(0, str.indexOf("#")));
				}
				else {
					c.setBaseURI(controller.getBaseURI() + "#");
				}
			}
			else {
				c.setBaseURI(controller.getBaseURI());
			}
			c.setConferenceDescription(controller.getConferenceDescription());
		
			// Conference Series Stuff
			String seriesURI = controller.getSeriesPaneController().getSeriesURI();
			
			if (seriesURI != null && seriesURI.length() > 0) {
				c.setSeriesURI(seriesURI);
			}
	 }

	 protected void easyConf() {
		 EasyConfController easyCont = new EasyConfController(getController());
		 EasyConfDialog dialog = new EasyConfDialog(easyCont, "EasyConf Markup Utility", "Full Text of Conference Announcement:");
		 dialog.open();
	 }
	 
	 protected void proxyDialog() {
		 ProxyDialogController proxyController = new ProxyDialogController(getController());
		 ProxyDialog dialog = new ProxyDialog(proxyController, "Setup an Internet Proxy");
		 dialog.open();
	 }
	 
	 protected void internalProxyEditor() {
		 InternalProxyEditorController proxyController = new InternalProxyEditorController(getController());
		 InternalProxyEditorWindow dialog = new InternalProxyEditorWindow(proxyController, "Edit URL/Local File Mappings");
		 dialog.open();
	 }
	 
	 protected void openOntology () {
		 FileDialog fd = new FileDialog(sShell, SWT.OPEN);
		 String exts[] = {"*.owl;*.rdf"};
		 fd.setFilterExtensions(exts);
		 String infile = "";
		 OWL2ConfReader reader = new OWL2ConfReader();
		 reader.addObserver(this);
		 
		 infile = fd.open();
		 if (infile != null) {
			 try {
				 this.controller.refreshNew();
				 reader.loadConference(infile);
			 }
			 catch (FileNotFoundException fe) {
				 MessageBox b = new MessageBox(this.sShell, SWT.ICON_ERROR | SWT.OK);
				 b.setText("Error opening ontology");
				 b.setMessage("Cannot open file: " + fe.getMessage());
				 b.open();
			 }
		 }
	 }
	 
	 public void update(Observable obs, Object arg) {
		 if (obs instanceof OWL2ConfReader || obs instanceof Conf2OWLConverter) {
		
			 if (arg instanceof OWL2ConfException) {		 
				 OWL2ConfException e = (OWL2ConfException) arg;
				 this.msgBuffer = e.getMessage();
				 this.sShell.getDisplay().asyncExec( new Runnable() {
				 		public void run() {
				 			MessageBox b = new MessageBox(sShell, SWT.ICON_ERROR | SWT.OK);
				 			b.setText("Error opening ontology");
				 			b.setMessage("Cannot open file: " + msgBuffer);
				 			b.open();
				 		}
			 		}
				 );
			 }
			 else if (arg instanceof Conference) {
				 this.confBuffer = (Conference) arg;
				 this.sShell.getDisplay().asyncExec( new Runnable() {
				 		public void run() {
				 			loadConfInfo((Conference) confBuffer);
				 			progressWindow.dispose();
				 			progressWindow = null;
				 		}
			 		}
				 );
			 }
			 else if (arg instanceof Progress) {
				 this.progress = (Progress) arg;
				 this.sShell.getDisplay().asyncExec( new Runnable () {
					public void run() {
						if (progressWindow == null) {
							if (progress.getWindowTitle() != null) {
								progressWindow = new LoadProgressWindow(progress.getWindowTitle(),"");
							}
							else {
								progressWindow = new LoadProgressWindow("Importing Ontology","");
							}
							progressWindow.setBarStatus(progress.getNStatus());
							progressWindow.updateStatus(progress.getStrStatus());
							//progressWindow.run();
						}
						else {
							if (progress.getNStatus() != 100) {
								progressWindow.setBarStatus(progress.getNStatus());
								progressWindow.updateStatus(progress.getStrStatus());
							}
							else {
								if (!progressWindow.isDisposed()) {
									progressWindow.dispose();
									progressWindow = null;
								}
							}
						}
					}
				 });
			 }
		 }
	 }
	 
	 protected void loadConfInfo(Conference c) {
		 // Put code to unload progress window here
		 this.controller.setBaseURI(c.getBaseURI());
		 this.controller.setConferenceName(c.getConferenceName());
		 this.controller.setConferenceStartDate(c.getConferenceStartDate());
		 this.controller.setConferencePaperSubmissionDeadline(c.getConferencePaperSubmissionDeadline());
		 this.controller.setConferenceAbstractSubmissionDeadline(c.getConferenceAbstractSubmissionDeadline());
		 this.controller.setConferenceEndDate(c.getConferenceEndDate());
		 this.controller.setConferenceAddress(c.getAddress());
		 this.controller.setConferenceDescription(c.getConferenceDescription());
		 this.controller.setFulltext(c.getAnnouncementFullText());
		 this.controller.setConferenceWebsite(c.getConferenceWebsite());
		 this.controller.setSeriesURI(c.getSeriesURI());
		 
		 // Topics
		 if (c.getTopicOntologyURIs() != null)  {
			 ArrayList<String> newOnts = c.getTopicOntologyURIs();
			 this.controller.getTopicPaneController().clearOntologies();
			 
			 for (String s : newOnts) {
				 this.controller.getTopicPaneController().addOntology(s);
			 }
			 this.controller.getTopicPaneController().filterOntologies();
		 }
		 for (String str : c.getConferenceTopicAreas()) {
			 this.controller.addConferenceTopicArea(str);
		 }
		 
		 if (c.getLocationOntologyURIs() != null) {
			 ArrayList<String> newOnts = c.getLocationOntologyURIs();
			 this.controller.getLocationPaneController().clearOntologies();
			 for (String s : newOnts) {
				 this.controller.getLocationPaneController().addOntology(s);
			 }
			 this.controller.getLocationPaneController().filterOntologies();
		 }
		 this.controller.setLocation(c.getLocation());
		 this.controller.setLocationURI(c.getLocatedInURI());
		 
		 // Series stuff..this may get a little hairy..
		 if (c.getSeriesURI() != null && c.getSeriesURI().length() > 0) {
			 ConferenceSeries series = null;
			 series = c.getSeries();
			 if (series != null) {
				 // There is a series defined in this conference.
				 this.controller.setSeriesName(series.getSeriesTitle());
				 this.controller.setSeriesOccursIn(series.getOccursIn());
				 this.controller.setSeriesSubmissionsDueIn(series.getSubmissionsDueIn());
				 this.controller.setSeriesConstituentConferences(series.getConstituentConferences());
				 this.controller.setSeriesTopicAreas(series.getTopicAreas());
				 this.controller.setSeriesWebsite(series.getWebsite());
			 }
			 else {
				 this.controller.getSeriesPaneController().setEditable(false);
			 }
		 }
	 }
	 
	public Shell getSShell() {
		return sShell;
	}

	public void setSShell(Shell shell) {
		sShell = shell;
	}

	public void refreshNew() {
		this.controller.refreshNew();
	}
	
	 
	protected class ConfAnnounceWindowItemListener implements SelectionListener {
		 public void widgetSelected(SelectionEvent event) {
			 if (event.getSource().equals(exitMenuItem)) {
				 sShell.close();
				 //sShell.getDisplay().dispose();
			 }
			 else if (event.getSource().equals(entryPaneMenuItem) || event.getSource().equals(ecToolItem)) {
				 easyConf();
			 }
			 else if (event.getSource().equals(publishItem) || event.getSource().equals(publishToolItem)) {
				 publish();
			 }
			 else if (event.getSource().equals(openToolItem) || event.getSource().equals(openOntItem)) {
				 openOntology();
			 }
			 else if (event.getSource().equals(newToolItem) || event.getSource().equals(newMenuItem)) {
				 refreshNew();
			 }
			 else if (event.getSource().equals(proxyMenuItem)) {
				 proxyDialog();
			 }
			 else if (event.getSource().equals(proxyEditorItem)) {
				 internalProxyEditor();
			 }
			 else if (event.getSource().equals(chkDownloadImportsMenuItem)) {
				 SettingsManager manager = SettingsManager.getInstance();
				 if (manager.getDownloadWarning()) {
					 MessageBox mbox = new MessageBox(getSShell(), SWT.ICON_INFORMATION | SWT.OK);
					 mbox.setText("Download Imported Ontologies");
					 mbox.setMessage("Checking this item forces ConfAnnounce to download ontology files " +
							 "imported from user-selected ontology files to a specified location, rather " +
							 "than retrieving them from their associated URL each time the program is run. " +
							 "(This message will only appear once)");
					 mbox.open();
					 manager.setNoDownloadWarning();
				 }
				 manager.setDownloadImports(chkDownloadImportsMenuItem.getSelection());
			 }
		 }
		 		 
		 protected void runTopicWindow(SelectionEvent event) {
			 Display disp = sShell.getDisplay();
			 Shell shell = new Shell(disp);
				
			 // Code to create a TopicTreeWindow + Controller. 
			 TopicPane test = null;
			 TopicPaneController ttcontrol = new TopicPaneController(controller);
			 
			 test = new TopicPane(shell, SWT.NULL);
			 test.setController(ttcontrol);

			 shell.pack();
			 shell.open();
			 while(!shell.isDisposed() && !test.isDisposed()) {
				 if(!disp.readAndDispatch()) {
					 disp.sleep();
				 }	
			 }
			 test.dispose();
			 shell.dispose();
		 }

		 public void widgetDefaultSelected(SelectionEvent event) {
			 this.widgetSelected(event);
		 }
	 }
}
