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

//import java.io.IOException;
//import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.Shell;

import edu.lehigh.mab305.swproj.Application.TimeUtil;
import edu.lehigh.mab305.swproj.ConferenceModel.Conference;
//import edu.lehigh.mab305.swproj.ConferenceModel.OWL2ConfException;
import org.eclipse.swt.custom.StyledText;
import org.vafada.swtcalendar.*;

public class TestPane extends Composite {

	protected static String SERIES_TOOLTIP = "Attempts to load the provided URI via http to display the relevant " +
											"information in the 'Conference Series' tab. Leave this field blank " +
											"and enter new information in the 'Conference Series' tab to create " +
											"a new instance of a Conference Series, starting with this Conference.";
	
	protected ConfInfoPaneController controller = null;  //  @jve:decl-index=0:
	protected Label lblConfTitle = null, lblConfWebsite = null, lblConfStartDate = null,
			lblEndDate = null, lblPaperDate = null, lblBaseURI = null, lblDescription = null, lblAbstractDate = null;
	protected Text txtConfTitle = null, txtConfWebsite = null, txtConfStartDate = null,
			txtConfEndDate = null, txtConfPaperDate = null, txtBaseURI = null, txtAbstractDate = null;
	protected Button btnStartDateSelect = null, btnEndDateSelect = null, btnPaperDateSelect = null,
			btnAbstractDateSelect = null;;
	protected DateSelectionWidget dateSelector;
	protected DateSelectionController dsControlStartDate, dsControlEndDate, dsControlPaperDate, dsControlAbstractDate;
	protected StyledText txtDescription = null; 
	protected static enum DateSelectors {START_DATE, END_DATE, ABSTRACT_DATE, PAPER_DATE};
	protected DateSelectors dateSelectorOpen;
	protected Calendar startDateCalendar = null, abstractDateCalendar = null;
	protected boolean dateSelected = false;
	
	public TestPane(Composite parent, int style, ConfInfoPaneController controller) {
		super(parent, style);
		
		this.controller = controller;
		this.dsControlStartDate = new DateSelectionController(this.controller.getConf(), Conference.ConferenceDate.CONFERENCE_START_DATE);
		this.dsControlEndDate = new DateSelectionController(this.controller.getConf(), Conference.ConferenceDate.CONFERENCE_END_DATE);
		this.dsControlAbstractDate = new DateSelectionController(this.controller.getConf(), Conference.ConferenceDate.CONFERENCE_ABSTRACT_DATE);
		this.dsControlPaperDate = new DateSelectionController(this.controller.getConf(), Conference.ConferenceDate.CONFERENCE_PAPER_DATE);
		initialize();
	}

	protected void initialize() {
		setSize(new Point(600, 363));
		setLayout(null);
		
		ConfInfoPaneEventHandler handler = new ConfInfoPaneEventHandler();
		
		this.addFocusListener(handler);
		this.addMouseListener(handler);
		lblConfTitle = new Label(this.getCompositePane(), SWT.NONE);
		lblConfTitle.setBounds(new Rectangle(5, 13, 156, 17));
		lblConfTitle.setText("Conference Name: *");
		lblConfTitle.setForeground(new Color(this.getCompositePane().getDisplay(), 225, 0, 0));
		lblConfTitle.addFocusListener(handler);
		txtConfTitle = new Text(this.getCompositePane(), SWT.BORDER);
		txtConfTitle.setBounds(new Rectangle(5, 30, 284, 19));
		txtConfTitle.addModifyListener(new ConfInfoPaneEventHandler());
		txtConfTitle.addFocusListener(handler);
		
		txtBaseURI = new Text(this.getCompositePane(), SWT.BORDER);
		txtBaseURI.addModifyListener(handler);
		txtBaseURI.addFocusListener(handler);
		//txtBaseURI.setBounds(new Rectangle(5, 163, 284, 19));
		txtBaseURI.setBounds(new Rectangle(5, 77, 284, 19));
		
		lblConfWebsite = new Label(this.getCompositePane(), SWT.NONE);
		lblConfWebsite.setBounds(new Rectangle(335, 13, 200, 16));
		lblConfWebsite.setText("Conference Website:");
		lblConfWebsite.addFocusListener(handler);
		txtConfWebsite = new Text(this.getCompositePane(), SWT.BORDER);
		txtConfWebsite.setBounds(new Rectangle(335, 30, 284, 19)); //284
		txtConfWebsite.addFocusListener(handler);
		//txtConfWebsite.setBounds(new Rectangle(5, 120, 172, 19));
		
		lblConfStartDate = new Label(this.getCompositePane(), SWT.NONE);
		lblConfStartDate.setBounds(new Rectangle(5, 103, 199, 13));
		lblConfStartDate.setText("Start Date:");
		lblConfStartDate.addFocusListener(handler);
		txtConfStartDate = new Text(this.getCompositePane(), SWT.BORDER);
		txtConfStartDate.setBounds(new Rectangle(5, 120, 200, 19));
		txtConfStartDate.addFocusListener(handler);
		btnStartDateSelect = new Button(this.getCompositePane(), SWT.NONE);
		btnStartDateSelect.setBounds(new Rectangle(206, 120, 27, 23));
		btnStartDateSelect.setText("...");
		btnStartDateSelect.addFocusListener(handler);
		
		lblEndDate = new Label(this.getCompositePane(), SWT.NONE);
		lblEndDate.setBounds(new Rectangle(335, 103, 199, 13));
		lblEndDate.setText("End Date:");
		lblEndDate.addFocusListener(handler);
		txtConfEndDate = new Text(this.getCompositePane(), SWT.BORDER);
		txtConfEndDate.setBounds(new Rectangle(335, 120, 200, 19));
		txtConfEndDate.addFocusListener(handler);
		btnEndDateSelect = new Button(this.getCompositePane(), SWT.NONE);
		btnEndDateSelect.setBounds(new Rectangle(537, 120, 27, 23));
		btnEndDateSelect.setText("...");
		btnEndDateSelect.addFocusListener(handler);
		
		lblAbstractDate = new Label(this.getCompositePane(), SWT.NONE);
		lblAbstractDate.setBounds(new Rectangle(5, 146, 199, 13));
		lblAbstractDate.setText("Abstract Submission Deadline:");
		lblAbstractDate.addFocusListener(handler);
		txtAbstractDate = new Text(this.getCompositePane(), SWT.BORDER);
		txtAbstractDate.setBounds(new Rectangle(5, 163, 200, 19));
		txtAbstractDate.addFocusListener(handler);
		btnAbstractDateSelect = new Button(this.getCompositePane(), SWT.NONE);
		btnAbstractDateSelect.setBounds(new Rectangle(206, 163, 27, 23));
		btnAbstractDateSelect.setText("...");
		btnAbstractDateSelect.addFocusListener(handler);
		
		lblPaperDate = new Label(this.getCompositePane(), SWT.NONE);
		lblPaperDate.setBounds(new Rectangle(335, 146, 200, 16));
		lblPaperDate.setText("Paper Submission Deadline:");
		lblPaperDate.addFocusListener(handler);
		txtConfPaperDate = new Text(this.getCompositePane(), SWT.BORDER);
		txtConfPaperDate.setBounds(new Rectangle(335, 163, 200, 19));
		txtConfPaperDate.addFocusListener(handler);
		btnPaperDateSelect = new Button(this.getCompositePane(), SWT.NONE);
		btnPaperDateSelect.setBounds(new Rectangle(537, 163, 27, 23));
		btnPaperDateSelect.setText("...");
		btnPaperDateSelect.addFocusListener(handler);
		
		lblBaseURI = new Label(this.getCompositePane(), SWT.NONE);
		//lblBaseURI.setBounds(new Rectangle(5, 146, 128, 13));
		lblBaseURI.setBounds(new Rectangle(5, 60, 250, 17));
		lblBaseURI.setText("Base URI: (No local Names) *");
		lblBaseURI.setForeground(new Color(this.getCompositePane().getDisplay(), 225, 0, 0));
		lblBaseURI.addFocusListener(handler);
		
		lblDescription = new Label(this.getCompositePane(), SWT.NONE);
		lblDescription.setBounds(new Rectangle(5, 189, 128, 13));
		lblDescription.setText("General Description:");
		lblDescription.addFocusListener(handler);
		txtDescription = new StyledText(this.getCompositePane(), SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		txtDescription.setBounds(new Rectangle(5, 206, 613, 199));
		txtDescription.addFocusListener(handler);
		
		btnStartDateSelect.addSelectionListener(new ConfInfoPaneEventHandler());
		btnEndDateSelect.addSelectionListener(new ConfInfoPaneEventHandler());
		btnPaperDateSelect.addSelectionListener(new ConfInfoPaneEventHandler());
		btnAbstractDateSelect.addSelectionListener(new ConfInfoPaneEventHandler());
	}
	
	public TestPane getCompositePane() {
		return this;
	}
	
	/**
	 * @return the controller
	 */
	public ConfInfoPaneController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(ConfInfoPaneController controller) {
		this.controller = controller;
	}
	
	/* Control methods */
	 public String getConferenceStartDate() {
		 String ret = null;
		 if (this.txtConfStartDate != null) {
			 ret = this.txtConfStartDate.getText();
		 }
		 return ret;
	 }
	 
	 public String getConferenceEndDate() {
		 String ret = null;
		 if (this.txtConfEndDate != null) {
			 ret = this.txtConfEndDate.getText();
		 }
		 return ret;
	 }
	 
	 public String getConferencePaperSubmissionDeadline() {
		 String ret = null;
		 if (this.txtConfPaperDate != null) {
			 ret = this.txtConfPaperDate.getText();
		 }
		 return ret;
	 }
	 
	 public String getConferenceAbstractSubmissionDeadline() {
		 String ret = null;
		 if (this.txtAbstractDate != null) {
			 ret = this.txtAbstractDate.getText();
		 }
		 return ret;
	 }
	 
	 public String getConferenceName() {
		 String ret = null;
		 if (this.txtConfTitle != null) {
			 ret = this.txtConfTitle.getText();
		 }
		 return ret;
	 }
	 
	 public String getConferenceWebsite() {
		 String ret = null;
		 if (this.txtConfWebsite != null) {
			 ret = this.txtConfWebsite.getText();
		 }
		 return ret;
	 }

	 public String getBaseURI() {
		 String ret = null;
		 if (this.txtBaseURI != null) {
			 ret = this.txtBaseURI.getText();
		 }
		 return ret;
	 }
	 
	 public String getDescription() {
		 String ret = null;
		 if (this.txtDescription != null) {
			 ret = this.txtDescription.getText();
		 }
		 return ret;
	 }
	 
	 public void setConferenceStartDate(String date) {
		 if (date != null) {
			 this.txtConfStartDate.setText(date);
		 }
	 }
	 
	 public void setConferenceEndDate(String date) {
		 if (date != null) {
			 this.txtConfEndDate.setText(date);
		 }
	 }
	 
	 public void setConferencePaperSubmissionDeadline(String date) {
		 if (date != null) {
			 this.txtConfPaperDate.setText(date);
		 }
	 }
	 
	 public void setConferenceAbstractSubmissionDeadline(String date) {
		 if (date != null) {
			 this.txtAbstractDate.setText(date);
		 }
	 }
	 
	 public void setConferenceName(String title) {
		 if (title != null){
			 this.txtConfTitle.setText(title);
		 }
	 }
	 
	 public void setConferenceWebsite(String website) {
		 if (website != null) 
		 {
			 this.txtConfWebsite.setText(website); 
		 }
	 }
	 
	 public void setBaseURI(String uri) {
		 if (uri != null) {
			 this.txtBaseURI.setText(uri);
		 }
	 }
	 
	 public void setDescription(String description) {
		 if (description != null) {
			 this.txtDescription.setText(description);
		 }
	 }
	 
	 protected void resetInterface() {
		 this.setBaseURI("");
		 this.setConferenceEndDate("");
		 this.setConferenceName("");
		 this.setConferenceAbstractSubmissionDeadline("");
		 this.setConferencePaperSubmissionDeadline("");
		 this.setConferenceStartDate("");
		 this.setConferenceWebsite("");
		 this.setDescription("");
	 }
	 
	 protected class ConfInfoPaneEventHandler extends org.eclipse.swt.events.SelectionAdapter 
			implements ModifyListener, MouseListener, FocusListener, KeyListener {
		 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			 if (e.getSource().equals(btnStartDateSelect)) {
				 btnStartDateCalendarClickedInline(e);
			 }	
			 else if (e.getSource().equals(btnEndDateSelect)) {
				 btnEndDateCalendarClickedInline(e);
			 }	
			 else if (e.getSource().equals(btnPaperDateSelect)) {
				 btnPaperDateCalendarClickedInline(e);
			 }
			 else if (e.getSource().equals(btnAbstractDateSelect)) {
				 btnAbstractDateCalendarClickedInline(e);
			 }
		 }
	
		 public void focusLost(FocusEvent e) {
		 }
		 
		 public void focusGained(FocusEvent e) {
			 Object o = e.getSource();
			 if (o instanceof DateSelectionWidget || o instanceof SWTCalendar || o instanceof SWTDayChooser) {
				 dateSelected = true;
			 }
			 else if (dateSelected && !(o instanceof DateSelectionWidget) && !(o instanceof SWTCalendar)
					 && !(o instanceof SWTDayChooser))  {
				 if (!dateSelector.isDisposed()) {
					 closeDateSelector();
				 }
			 }
		 }
		 
		 public void modifyText(ModifyEvent e) {
			 if (e.getSource().equals(txtBaseURI) || e.getSource().equals(txtConfTitle)) {
				 if (verifyInfo()) {
					 controller.setPublishable(true);
				 }
				 else {
					 controller.setPublishable(false); 
				 }
			 }
		 }
		 
		 public void mouseDown(MouseEvent e) {
			 Point p = new Point(e.x, e.y);
			 if (getCompositePane().getBounds().contains(p)) {
				 if (dateSelector != null && !dateSelector.isDisposed() && !dateSelector.getBounds().contains(p)) {
					 closeDateSelector();
				 }
			 }
		 }
		 public void mouseUp(MouseEvent e) {} 
		 
		 public void keyPressed(KeyEvent e) {
			 if (e.keyCode == SWT.ESC && ! dateSelector.isDisposed()) {
					 closeDateSelector();
			 }
		 }
		 public void keyReleased(KeyEvent e) {}
		 
		 public void mouseDoubleClick(MouseEvent e) {
			 selectDate();
			if (dateSelector != null && !dateSelector.isDisposed()) {
				 dateSelector.dispose();
			}
		 }
		 
		 protected void selectDate() {
			 if (dateSelectorOpen != null) {
				 switch (dateSelectorOpen) {
				 case START_DATE:
					 startDateCalendar = dateSelector.getCalendar();
					 if (controller.getConf().getConferenceStartDate() != null) {
						 txtConfStartDate.setText(controller.getConf().getConferenceStartDate());
					 }
					 break;
				 case END_DATE:
					 if (controller.getConf().getConferenceEndDate() != null) {
						 txtConfEndDate.setText(controller.getConf().getConferenceEndDate());
					 }
					 break;
				 case ABSTRACT_DATE:
					 abstractDateCalendar = dateSelector.getCalendar();
					 if (controller.getConf().getConferenceAbstractSubmissionDeadline() != null) {
						 txtAbstractDate.setText(controller.getConf().getConferenceAbstractSubmissionDeadline());
					 }
					 break;
				 case PAPER_DATE:
					 if (controller.getConf().getConferencePaperSubmissionDeadline() != null) {
						 txtConfPaperDate.setText(controller.getConf().getConferencePaperSubmissionDeadline());
					 }
					 break;
				 }
				 closeDateSelector();
			 }
		 }
		 
		 protected void closeDateSelector() {
			 
			 switch (dateSelectorOpen) {
			 case START_DATE:
				 btnEndDateSelect.setEnabled(true);
				 btnPaperDateSelect.setEnabled(true);
				 btnAbstractDateSelect.setEnabled(true);
				 break;
			 case END_DATE:
				 btnStartDateSelect.setEnabled(true);
				 btnPaperDateSelect.setEnabled(true);
				 btnAbstractDateSelect.setEnabled(true);
				 break;
			 case ABSTRACT_DATE:
				 btnStartDateSelect.setEnabled(true);
				 btnPaperDateSelect.setEnabled(true);
				 btnEndDateSelect.setEnabled(true);
				 break;
			 case PAPER_DATE:
				 btnStartDateSelect.setEnabled(true);
				 btnEndDateSelect.setEnabled(true);
				 btnAbstractDateSelect.setEnabled(true);
				 break;
			 }
			 if (!dateSelector.isDisposed()) {
				 dateSelector.dispose();
			 }
		 }
		 
		 protected boolean verifyInfo() {
			 boolean ret = false;
			 
			 if (txtBaseURI.getText().length() != 0 && txtConfTitle.getText().length() != 0) {
				 ret = true;
			 }
			 return ret;
		 }
		 
		 protected void btnStartDateCalendarClickedInline (org.eclipse.swt.events.SelectionEvent e) {
			 // OR IT IS DISPOSED
			 if ( !dsControlStartDate.isOpen() ) {
				 Rectangle r = txtConfStartDate.getBounds();
				 boolean dateParsed = false;
				 
				 dateSelector = new DateSelectionWidget(getCompositePane(), SWT.NULL);
				 dateSelector.setController(dsControlStartDate);
				 dateSelector.setBounds(r.x, r.y + 20, 200, 150);
				 dateSelector.setVisible(true);
				 dateSelector.addMouseListener(this);
				 dateSelector.addFocusListener(this);
				 dateSelector.addKeyListener(this);
				 dateSelector.forceFocus();
				 dateSelector.moveAbove(lblEndDate);
				 dateSelectorOpen = TestPane.DateSelectors.START_DATE;
				 
				 String dateText = txtConfStartDate.getText();
				 if (dateText != null && dateText.length() > 0) {
					//String dateString = EasyConfController.parseDate(dateText);
					//if (dateString != null && dateString.length() > 0) {
					 if (dateText != null && dateText.length() > 0) {
						try {
							DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
							Calendar c = Calendar.getInstance();
							c.setTime(df.parse(TimeUtil.makeParseable(dateText)));
							dateSelector.setCalendar(c);
							dateParsed = true;
						}
						catch (ParseException p) {
							System.err.println(p.getMessage());
							System.err.println(dateText);
						}
					}
				 }
				 
				 // Set the default calendar date
				 if (startDateCalendar != null && !dateParsed) {
					 dateSelector.setCalendar(startDateCalendar);
				 }
				 
				 lblAbstractDate.moveBelow(dateSelector);
				 txtAbstractDate.moveBelow(dateSelector);
				 lblDescription.moveBelow(dateSelector);
				 txtDescription.moveBelow(dateSelector);
				 
				 btnEndDateSelect.setEnabled(false);
				 btnPaperDateSelect.setEnabled(false);
				 btnAbstractDateSelect.setEnabled(false);
				 
				 getCompositePane().redraw();
				 dsControlStartDate.setOpen(true);
			 }
			 else {
				 selectDate();
				 getCompositePane().redraw();
			 }
		 }
		 
		 protected void btnEndDateCalendarClickedInline (org.eclipse.swt.events.SelectionEvent e) {
			 if ( !dsControlEndDate.isOpen() ) {
				 Rectangle r = txtConfEndDate.getBounds();
				 boolean dateParsed = false;
				 
				 dateSelector = new DateSelectionWidget(getCompositePane(), SWT.NULL);
				 dateSelector.setController(dsControlEndDate);
				 dateSelector.setBounds(r.x, r.y + 20, 200, 150);
				 dateSelector.setVisible(true);
				 dateSelector.moveAbove(lblPaperDate);
				 dateSelector.addMouseListener(this);
				 dateSelector.addFocusListener(this);
				 dateSelector.addKeyListener(this);
				 dateSelector.forceFocus();
				 dateSelectorOpen = TestPane.DateSelectors.END_DATE;
				 String dateText = txtConfEndDate.getText();
				 if (dateText != null && dateText.length() > 0) {
					 if (dateText != null && dateText.length() > 0) {
						 try {
							 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
							 Calendar c = Calendar.getInstance();
							 c.setTime(df.parse(TimeUtil.makeParseable(dateText)));
							 dateSelector.setCalendar(c);
							 dateParsed = true;
						 }
						 catch (ParseException p) {
							 System.err.println(p.getMessage());
							 System.err.println(dateText);
						 }
					 }
				 }
				 
				 // Secondarily, try the start date
				 if (!dateParsed) {
					 dateText = txtConfStartDate.getText();
					 if (dateText != null && dateText.length() > 0) {
						 if (dateText != null && dateText.length() > 0) {
							 try {
								 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
								 Calendar c = Calendar.getInstance();
								 c.setTime(df.parse(TimeUtil.makeParseable(dateText)));
								 dateSelector.setCalendar(c);
								 dateParsed = true;
							 }
							 catch (ParseException p) {
								 System.err.println(p.getMessage());
								 System.err.println(dateText);
							 }
						 }
					 }
				 }
				 // Set the default calendar date
				 if (startDateCalendar != null && !dateParsed) {
					 dateSelector.setCalendar(startDateCalendar);
				 }
				 lblAbstractDate.moveBelow(dateSelector);
				 txtAbstractDate.moveBelow(dateSelector);
				 lblDescription.moveBelow(dateSelector);
				 txtDescription.moveBelow(dateSelector);
				 
				 getCompositePane().redraw();
			 }
			 else {
				 selectDate();
				 getCompositePane().redraw();
			 }
		 }
		 
		 protected void btnPaperDateCalendarClickedInline (org.eclipse.swt.events.SelectionEvent e) {
			 if ( !dsControlPaperDate.isOpen() ) {
				 Rectangle r = txtConfPaperDate.getBounds();
				 boolean dateParsed = false;
				
				 dateSelector = new DateSelectionWidget(getCompositePane(), SWT.NULL);
				 dateSelector.setController(dsControlPaperDate);
				 dateSelector.setBounds(r.x, r.y + 20, 200, 150);
				 dateSelector.setVisible(true);
				 dateSelector.addMouseListener(new ConfInfoPaneEventHandler());
				 dateSelector.addFocusListener(new ConfInfoPaneEventHandler());
				 dateSelector.addKeyListener(new ConfInfoPaneEventHandler());
				 dateSelectorOpen = TestPane.DateSelectors.PAPER_DATE;
				 String dateText = txtConfPaperDate.getText();
				 if (dateText != null && dateText.length() > 0) {
					 if (dateText != null && dateText.length() > 0) {
						try {
							DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
							Calendar c = Calendar.getInstance();
							c.setTime(df.parse(TimeUtil.makeParseable(dateText)));
							dateSelector.setCalendar(c);
							dateParsed = true;
						}
						catch (ParseException p) {
							System.err.println(p.getMessage());
							System.err.println(dateText);
						}
					 }
				 }
				 
				 // Set the default calendar date
				 if (abstractDateCalendar != null && !dateParsed) {
					 dateSelector.setCalendar(abstractDateCalendar);
				 }
				 
				 txtDescription.moveBelow(dateSelector);
				 btnStartDateSelect.setEnabled(false);
				 btnEndDateSelect.setEnabled(false);
				 btnAbstractDateSelect.setEnabled(false);
				 
				 getCompositePane().redraw();
			 }
			 else {
				 selectDate();
				 getCompositePane().redraw();
			 }
		 }
		 
		 protected void btnAbstractDateCalendarClickedInline(SelectionEvent e) {
			 if ( !dsControlAbstractDate.isOpen() ) {
				 Rectangle r = txtAbstractDate.getBounds();
				 boolean dateParsed = false;
				 dateSelector = new DateSelectionWidget(getCompositePane(), SWT.NULL);
				 dateSelector.setController(dsControlAbstractDate);
				 dateSelector.setBounds(r.x, r.y + 20, 200, 150);
				 dateSelector.setVisible(true);
				 dateSelector.addMouseListener(this);
				 dateSelector.addFocusListener(this);
				 dateSelector.addKeyListener(this);
				 dateSelector.forceFocus();
				 dateSelectorOpen = TestPane.DateSelectors.ABSTRACT_DATE;
				 
				 String dateText = txtAbstractDate.getText();
				 if (dateText != null && dateText.length() > 0) {
					 if (dateText != null && dateText.length() > 0) {
						 try {
							 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
							 Calendar c = Calendar.getInstance();
							 c.setTime(df.parse(TimeUtil.makeParseable(dateText)));
							 dateSelector.setCalendar(c);
							 dateParsed = true;
						 }
						 catch (ParseException p) {
							 System.err.println(p.getMessage());
							 System.err.println(dateText);
						 }
					 }
				 }
				 
				 // Secondarily, try the paper date
				 if (!dateParsed) {
					 dateText = txtAbstractDate.getText();
					 if (dateText != null && dateText.length() > 0) {
						 if (dateText != null && dateText.length() > 0) {
							 try {
								 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
								 Calendar c = Calendar.getInstance();
								 c.setTime(df.parse(TimeUtil.makeParseable(dateText)));
								 dateSelector.setCalendar(c);
								 dateParsed = true;
							 }
							 catch (ParseException p) {
								 System.err.println(p.getMessage());
								 System.err.println(dateText);
							 }
						 }
					 }
				 }
				 
				 // Set the default calendar date
				 if (abstractDateCalendar != null && !dateParsed) {
					 dateSelector.setCalendar(abstractDateCalendar);
				 }
				 
				 lblDescription.moveBelow(dateSelector);
				 txtDescription.moveBelow(dateSelector);
				 btnStartDateSelect.setEnabled(false);
				 btnEndDateSelect.setEnabled(false);
				 btnPaperDateSelect.setEnabled(false);
				 
				 getCompositePane().redraw();
			 }
			 else {
				 selectDate();
				 getCompositePane().redraw();
			 }
		 }
	 }
}
