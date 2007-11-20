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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.custom.CCombo;
import com.hp.hpl.jena.rdf.model.*;

public class SeriesPane extends Composite implements Observer {

	protected static String SERIES_TOOLTIP = "Attempts to load the provided URI via http to display the relevant " +
	"information in the 'Conference Series' tab. Leave this field blank " +
	"and enter new information in the 'Conference Series' tab to create " +
	"a new instance of a Conference Series, starting with this Conference.";
	protected static final String CONSTITUENT_TOOLTIP = "Hit 'Delete' to remove a constituent Conference";
	
	protected SeriesPaneController controller = null;  //  @jve:decl-index=0:
	protected Label lblConfTitle = null, lblConstituentConfs = null, lblMonthStarts = null,
				lblMonthSubmissions = null, lblAddConstituentConf = null, lblTopics = null, 
				lblSelectedTopics = null, lblSeriesURI = null, lblSeriesWebsite = null;
	protected Text txtConfTitle = null, txtAddConstituentConf = null, txtSeriesURI = null, txtSeriesWebsite = null;
	protected Tree tree = null;
	protected Button btnAddConstituentConf = null,btnAddSelection = null, 
					btnRemoveSelection = null, btnCopySelection = null, btnSeriesURI = null;
	protected CCombo cmbMonthStarts = null, cmbMonthSubmissions = null;
	protected List lstConstituentConfs = null;
	protected DataList<String> lstSelectedTopics = null;
	protected HashMap<String,Integer> monthHash = null, confURIHash = null, hashTopics = null;
	protected Model compositeModel = null;
	protected Exception exceptionBuffer = null;
	protected boolean _editable = true;
	
	public SeriesPane(Composite parent, int style, SeriesPaneController controller) {
		super(parent, style);
		this.monthHash = new HashMap<String,Integer>();
		this.confURIHash = new HashMap<String,Integer>();
		this.hashTopics = new HashMap<String,Integer>();
		this.controller = controller;
		initialize();
	}

	protected void initialize() {
		setSize(new Point(600, 363));
		setLayout(null);
		
		SeriesPaneEventHandler handler = new SeriesPaneEventHandler();
		
		lblConfTitle = new Label(this.getCompositePane(), SWT.NONE);
		lblConfTitle.setBounds(new Rectangle(5, 13, 175, 17));
		lblConfTitle.setText("Conference Series Name:");
		txtConfTitle = new Text(this.getCompositePane(), SWT.BORDER);
		txtConfTitle.setBounds(new Rectangle(5, 30, 324, 19));
		
		lblSeriesURI = new Label(this.getCompositePane(), SWT.NONE);
		lblSeriesURI.setBounds(new Rectangle(5, 60, 188, 13));
		lblSeriesURI.setText("Conference Series URL:");
		txtSeriesURI = new Text(this.getCompositePane(), SWT.BORDER);
		txtSeriesURI.setBounds(new Rectangle(5, 77, 265, 19));;
		btnSeriesURI = new Button(this.getCompositePane(), SWT.PUSH);
		btnSeriesURI.setBounds(new Rectangle(271, 77, 60, 23));
		btnSeriesURI.setText("Load URI");
		btnSeriesURI.setToolTipText(SERIES_TOOLTIP);
		btnSeriesURI.addSelectionListener(handler);
		
		lblSeriesWebsite = new Label(this.getCompositePane(), SWT.NONE);
		lblSeriesWebsite.setBounds(new Rectangle(5, 103, 175, 17));
		lblSeriesWebsite.setText("Conference Series Website:");
		txtSeriesWebsite = new Text(this.getCompositePane(), SWT.BORDER);
		txtSeriesWebsite.setBounds(new Rectangle(5, 120, 324, 19));
		
		lblMonthSubmissions = new Label(this.getCompositePane(), SWT.NONE);
		lblMonthSubmissions.setBounds(new Rectangle(5, 146, 156, 17));
		lblMonthSubmissions.setText("Submissions Due:");
		cmbMonthSubmissions = new CCombo(this.getCompositePane(), SWT.BORDER);
		cmbMonthSubmissions.setBounds(new Rectangle(5, 163, 125, 17));
		
		lblMonthStarts = new Label(this.getCompositePane(), SWT.NONE);
		lblMonthStarts.setBounds(new Rectangle(165, 146, 156, 17));
		//lblMonthStarts.setBounds(new Rectangle(377, 13, 200, 16));
		lblMonthStarts.setText("Month Starts:");
		cmbMonthStarts = new CCombo(this.getCompositePane(), SWT.BORDER);
		cmbMonthStarts.setBounds(new Rectangle(165, 163, 125, 17));
		//cmbMonthStarts.setBounds(new Rectangle(377, 30, 238, 17));
		
		lblConstituentConfs = new Label(this.getCompositePane(), SWT.NONE);
		lblConstituentConfs.setBounds(new Rectangle(377, 13, 200, 16));
		lblConstituentConfs.setText("Constituent Conferences:");
		lstConstituentConfs = new List(this.getCompositePane(), SWT.BORDER | SWT.H_SCROLL 
										| SWT.V_SCROLL | SWT.SINGLE);
		//lstConstituentConfs.setBounds(5, 77, 284, 109);
		lstConstituentConfs.setBounds(new Rectangle(377, 30, 238, 111));
		lstConstituentConfs.setToolTipText(CONSTITUENT_TOOLTIP);
		lstConstituentConfs.addKeyListener(handler);
		
		lblAddConstituentConf = new Label(this.getCompositePane(), SWT.NONE);
		lblAddConstituentConf.setBounds(new Rectangle(377, 146, 188, 13));
		lblAddConstituentConf.setText("Add Constituent Conference:");
		txtAddConstituentConf = new Text(this.getCompositePane(), SWT.BORDER);
		txtAddConstituentConf.setBounds(new Rectangle(377, 163, 210, 19));
		btnAddConstituentConf = new Button(this.getCompositePane(), SWT.NONE);
		btnAddConstituentConf.setBounds(new Rectangle(589, 163, 27, 23));
		btnAddConstituentConf.setText("...");
		btnAddConstituentConf.addSelectionListener(handler);
		
		lblTopics = new Label(this.getCompositePane(), SWT.NONE);
		lblTopics.setBounds(new Rectangle(5, 189, 200, 13));
		lblTopics.setText("General Series Topics:");
		tree = new Tree(this.getCompositePane(), SWT.MULTI | SWT.BORDER);
		tree.setBounds(new Rectangle(5, 206, 365, 200));
		tree.addMouseListener(handler);
		tree.addKeyListener(handler);
		
		lstSelectedTopics = new DataList<String>(this.getCompositePane(), SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		lstSelectedTopics.setBounds(new Rectangle(377, 206, 234, 180));
		lstSelectedTopics.addKeyListener(handler);
		lblSelectedTopics = new Label(this.getCompositePane(), SWT.NONE);
		lblSelectedTopics.setBounds(new Rectangle(377, 189, 200, 13));
		lblSelectedTopics.setText("Selected Series Topics: ");
		
		btnCopySelection = new Button(this.getCompositePane(), SWT.NONE);
		btnCopySelection.setBounds(new Rectangle(377, 387, 120, 23));
		btnCopySelection.setText("Copy from Topic Pane");
		btnCopySelection.addSelectionListener(handler);
		
		btnAddSelection = new Button(this.getCompositePane(), SWT.PUSH);	
		btnAddSelection.setBounds(new Rectangle(510, 387, 46, 23));
		btnAddSelection.setText("Add");
		btnAddSelection.addSelectionListener(handler);
		
		btnRemoveSelection = new Button(this.getCompositePane(), SWT.PUSH);
		btnRemoveSelection.setBounds(new Rectangle(555, 387, 56, 23));
		btnRemoveSelection.setText("Remove");
		btnRemoveSelection.addSelectionListener(handler);
		
		populateCombos();
	}
	
	public void populateCombos() {
		this.monthHash.put("January", 0);
		this.cmbMonthStarts.add("January");
		this.cmbMonthSubmissions.add("January");
		this.monthHash.put("February", 1);
		this.cmbMonthStarts.add("February");
		this.cmbMonthSubmissions.add("February");
		this.monthHash.put("March", 2);
		this.cmbMonthStarts.add("March");
		this.cmbMonthSubmissions.add("March");
		this.monthHash.put("April", 3);
		this.cmbMonthStarts.add("April");
		this.cmbMonthSubmissions.add("April");
		this.monthHash.put("May", 4);
		this.cmbMonthStarts.add("May");
		this.cmbMonthSubmissions.add("May");
		this.monthHash.put("June", 5);
		this.cmbMonthStarts.add("June");
		this.cmbMonthSubmissions.add("June");
		this.monthHash.put("July", 6);
		this.cmbMonthStarts.add("July");
		this.cmbMonthSubmissions.add("July");
		this.monthHash.put("August", 7);
		this.cmbMonthStarts.add("August");
		this.cmbMonthSubmissions.add("August");
		this.monthHash.put("September", 8);
		this.cmbMonthStarts.add("September");
		this.cmbMonthSubmissions.add("September");
		this.monthHash.put("October", 9);
		this.cmbMonthStarts.add("October");
		this.cmbMonthSubmissions.add("October");
		this.monthHash.put("November", 10);
		this.cmbMonthStarts.add("November");
		this.cmbMonthSubmissions.add("November");
		this.monthHash.put("December", 11);
		this.cmbMonthStarts.add("December");
		this.cmbMonthSubmissions.add("December");
	}
	
	public Composite getCompositePane() {
		return this;
	}
	
	protected SeriesPane getSeriesPane() {
		return this;
	}
	
	/**
	 * @return the controller
	 */
	public SeriesPaneController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(SeriesPaneController controller) {
		this.controller = controller;
	}

	public boolean isEditable() {
		return _editable;
	}

	public void setEditable(boolean editable) {
		this.cmbMonthStarts.setEditable(editable);
		this.cmbMonthStarts.setEnabled(editable);
		this.cmbMonthSubmissions.setEditable(editable);
		this.cmbMonthSubmissions.setEnabled(editable);
		this.txtAddConstituentConf.setEditable(editable);
		this.btnAddConstituentConf.setEnabled(editable);
		this.btnAddSelection.setEnabled(editable);
		this.btnRemoveSelection.setEnabled(editable);
		this.txtConfTitle.setEditable(editable);
		this._editable = editable;
	}
	
	public void setMonthStarts(String month) {
		if (this.monthHash.containsKey(month)) {
			this.cmbMonthStarts.select(this.monthHash.get(month));
		}
	}
	
	public String getMonthStarts() {
		return this.cmbMonthStarts.getText();
	}
	
	public void setMonthSubmissions(String month) {
		if (this.monthHash.containsKey(month)) {
			this.cmbMonthSubmissions.select(this.monthHash.get(month));
		}
	}
	
	public String getMonthSubmissions() {
		return this.cmbMonthSubmissions.getText();
	}
	
	public void setSeriesTitle(String title) {
		if (title != null) {
			this.txtConfTitle.setText(title);
		}
		else {
			this.txtConfTitle.setText("");
		}
	}
	
	public String getSeriesTitle() {
		return this.txtConfTitle.getText();
	}
	
	public void setSeriesBaseURI(String uri) {
		if (uri != null) {
			this.txtSeriesURI.setText(uri);
		}
	}
	
	public String getSeriesWebsite() {
		return this.txtSeriesWebsite.getText();
	}
	
	public void setSeriesWebsite(String website) {
		if (website != null) {
			this.txtSeriesWebsite.setText(website);
		}
		else {
			this.txtSeriesWebsite.setText("");
		}
	}
	
	public String getSeriesBaseURI() {
		String ret = null;
		if (this.txtSeriesURI != null) {
			ret = this.txtSeriesURI.getText();
		}
		return ret;
	}
	
	public void setConstituentConfs(ArrayList<String> confs) {
		for (String s : confs) {
			this.lstConstituentConfs.add(s);
		}
	}
	
	public ArrayList<String> getConstituentConfs() {
		String s[] = this.lstConstituentConfs.getItems();
		ArrayList<String> ret = new ArrayList<String>();
		
		for (int i = 0; i < s.length; i++) {
			ret.add(s[i]);
		}
		return ret;
	}
	
	public void resetInterface() {
		this.txtSeriesURI.setText("");
		this.txtAddConstituentConf.setText("");
		this.txtConfTitle.setText("");
		this.lstConstituentConfs.removeAll();
		this.cmbMonthStarts.clearSelection();
		this.cmbMonthSubmissions.clearSelection();
		this.hashTopics = new HashMap<String,Integer>();
		this.lstSelectedTopics.removeAll();
		this.tree.removeAll();
		this.setEditable(true);
	}
	
	public void addConstituentConf(String conf) {
		if (!this.confURIHash.containsKey(conf)) {
			try {
				URI test = new URI(conf);
				if (test.getScheme() != null && test.getScheme() != "") {
					lstConstituentConfs.add(conf);
					this.confURIHash.put(conf, 1);
				}
			}
			catch (URISyntaxException ue) {
				
			}
		}
	}
	
	public Tree getTree() {
		return this.tree;
	}
	
	public ArrayList<String> getTopicAreas() {
		ArrayList<String> topicAreas = new ArrayList<String>();
		String items[] = this.lstSelectedTopics.getItems();
		for (String item : items) {
			topicAreas.add(this.lstSelectedTopics.getDataItem(item));
		}
		return topicAreas;
	}
	
	public void setTopicAreas(ArrayList<String> topicAreas) {
		if (this.compositeModel == null) {
			this.compositeModel = this.controller.getCompositeTopicModel();
		}
		
		this.lstSelectedTopics.removeAll();
		for (String s : topicAreas) {
			String label = null;
			label = OWLUtil.getRDFSLabel(this.compositeModel, s);
			if (label == null || label.length() == 0) {
				this.lstSelectedTopics.add(s);
			}
			else {
				this.lstSelectedTopics.add(label, s);
			}
		}		
	}
	
	 public void update (Observable obs, Object e) {
		 if (obs instanceof SeriesPaneController) {
			 if (e instanceof OWL2ConfException || e instanceof MalformedURLException 
					 || e instanceof IOException) {
				 Shell sShell = this.controller.getController().getWindow().getSShell();
				 this.exceptionBuffer = (Exception) e;
				 sShell.getDisplay().asyncExec( new Runnable () {
					 public void run() {
						 MessageBox m = new MessageBox(controller.getController().getWindow().getSShell(), SWT.ICON_ERROR | SWT.OK);
						 m.setText("Error opening series ontology");
						 m.setMessage("Cannot open file: " + exceptionBuffer.getMessage());
						 m.open();
					 }
				 });
			 }
		 }
	 }
	
	protected class SeriesPaneEventHandler extends org.eclipse.swt.events.SelectionAdapter 
		implements org.eclipse.swt.events.KeyListener, org.eclipse.swt.events.MouseListener{
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			 if (e.getSource().equals(btnAddConstituentConf)) {
				 if (txtAddConstituentConf.getText() != null 
						 && txtAddConstituentConf.getText() != "") {
					 addConstituentConf(txtAddConstituentConf.getText());
					 txtAddConstituentConf.setText("");
				 }
			 }
			 else if (e.getSource().equals(btnAddSelection)) {
				 addSelections();
			 }
			 else if (e.getSource().equals(btnRemoveSelection)) {
				 removeSelections();
			 }
			 else if (e.getSource().equals(btnCopySelection)) {
				 copySelection();
			 } 
			 else if (e.getSource().equals(btnSeriesURI)) {
				 btnSeriesURIClicked();
			 }
		}
		
		protected void btnSeriesURIClicked() {
			 controller.addObserver(getSeriesPane());
			 controller.loadSeriesOntology(txtSeriesURI.getText());
		 }
		
		protected void copySelection() {
			ArrayList<String> topics = controller.getConferenceTopicAreas();
			ArrayList<String> labels = controller.getConferenceTopicAreaLabels();
			int index = 0;
			lstSelectedTopics.removeAll();
			for (String label : labels) {
				lstSelectedTopics.add(label, new String(topics.get(index)));
				index++;
			}
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
		
		public void mouseDoubleClick(MouseEvent e) {
			addSelections();
		}
		
		public void mouseDown(MouseEvent e) {
			
		}

		public void mouseUp(MouseEvent e) {
	
		}
		 
		public void keyPressed(KeyEvent e) {
			if (e.getSource().equals(lstConstituentConfs) && (e.keyCode == SWT.DEL || e.keyCode == SWT.BS)) {
				lstConstituentConfs.remove(lstConstituentConfs.getSelectionIndex());
			}
			if (e.getSource() == tree && (e.keyCode == SWT.LF || e.keyCode == SWT.CR)) {
				addSelections();
			}
			if (e.getSource() == lstSelectedTopics && (e.keyCode == SWT.BS || e.keyCode == SWT.DEL)) {
				removeSelections();
			}
		}
		public void keyReleased(KeyEvent e) {
				
		}
	 }
	
	public void clearTree() {
		this.tree.removeAll();
	}
}
