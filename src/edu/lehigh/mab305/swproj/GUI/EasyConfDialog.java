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

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.StyledText;

public class EasyConfDialog {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	protected StyledText txtareaFullText = null;
	protected Button btnClose = null;
	protected EasyConfController controller = null;
	protected Label lblFulltext = null;
	
	protected Menu contextMenu = null;
	@SuppressWarnings("unused") protected MenuItem helperItem = null, barItem = null, setNameItem = null, setWebsiteItem = null, 
					setAddressItem = null, setStartDateItem = null, setEndDateItem = null, 
					setPaperDateItem = null, setBaseURIItem = null, setDateRangeItem = null, 
					setFulltextItem = null, setDescriptionItem = null, setAbstractDateItem = null;

	public EasyConfDialog (EasyConfController controller, String windowText, String labelText) {
		this.controller = controller;
		createSShell();
		this.lblFulltext.setText(labelText);
		this.sShell.setText(windowText);
	}
	
	public void open() {
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
	protected void createSShell() {
		sShell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.BORDER | SWT.APPLICATION_MODAL);
		sShell.setText("Shell");
		sShell.setSize(new Point(491, 316));
		sShell.setLayout(null);
		txtareaFullText = new StyledText(sShell, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		txtareaFullText.setBounds(new Rectangle(6, 28, 468, 221));
		txtareaFullText.addKeyListener(new ConfFullTextKeyListener());
		
		btnClose = new Button(sShell, SWT.NONE);
		btnClose.setBounds(new Rectangle(397, 256, 76, 23));
		btnClose.setText("Close");
		btnClose.addSelectionListener(new ConfFullTextButtonListener());
		lblFulltext = new Label(sShell, SWT.NONE);
		lblFulltext.setBounds(new Rectangle(5, 4, 468, 16));
		lblFulltext.setText("Label");
		
		this.contextMenu = new Menu(sShell, SWT.POP_UP);
		txtareaFullText.setMenu(contextMenu);
		
		helperItem = new MenuItem(contextMenu, SWT.PUSH);
		helperItem.setText("Set Conference Attributes below:");
		helperItem.addSelectionListener(new EasyConfWindowItemListener());
		
		barItem = new MenuItem(contextMenu, SWT.BAR);
		
		setBaseURIItem = new MenuItem(contextMenu, SWT.PUSH);;
		setBaseURIItem.setText("Announcement Base URI");
		setBaseURIItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setNameItem = new MenuItem(contextMenu, SWT.PUSH);
		setNameItem.setText("Set Name/Title");
		setNameItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setWebsiteItem = new MenuItem(contextMenu, SWT.PUSH);
		setWebsiteItem.setText("Set Website");
		setWebsiteItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setAddressItem = new MenuItem(contextMenu, SWT.PUSH);
		setAddressItem.setText("Street Address");
		setAddressItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setStartDateItem = new MenuItem(contextMenu, SWT.PUSH);
		setStartDateItem.setText("Start Date");
		setStartDateItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setEndDateItem = new MenuItem(contextMenu, SWT.PUSH);
		setEndDateItem.setText("End Date");
		setEndDateItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setDateRangeItem = new MenuItem(contextMenu, SWT.PUSH);
		setDateRangeItem.setText("Conference Date Range");
		setDateRangeItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setAbstractDateItem = new MenuItem(contextMenu, SWT.PUSH);
		setAbstractDateItem.setText("Abstract Submission Deadline");
		setAbstractDateItem.addSelectionListener(new EasyConfWindowItemListener());
		
		
		setPaperDateItem = new MenuItem(contextMenu, SWT.PUSH);
		setPaperDateItem.setText("Paper Submission Deadline");
		setPaperDateItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setDescriptionItem = new MenuItem(contextMenu, SWT.PUSH);
		setDescriptionItem.setText("General Description");
		setDescriptionItem.addSelectionListener(new EasyConfWindowItemListener());
		
		setFulltextItem = new MenuItem(contextMenu, SWT.PUSH);
		setFulltextItem.setText("Announcement Full Text");
		setFulltextItem.addSelectionListener(new EasyConfWindowItemListener());
	}

	/**
	 * @return the controller
	 */
	public EasyConfController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(EasyConfController controller) {
		this.controller = controller;
	}

	/***
	 * dispose: Note: *not* a real disposal method, this simply allows the shell to be disposed from
	 * inside the Listener class.
	 *
	 */
	public void dispose() {
		this.sShell.dispose();
	}
	
	protected class EasyConfWindowItemListener implements SelectionListener {
		 public void widgetSelected(SelectionEvent event) {
			 String selection = txtareaFullText.getSelectionText();
			 if (selection != null && ! selection.equals("")) {
				 if (event.getSource().equals(setNameItem)) {
					 getController().setConferenceName(selection);
				 }
				 else if (event.getSource().equals(setWebsiteItem)) {
					 getController().setConferenceWebsite(selection);
				 }
				 else if (event.getSource().equals(setAddressItem)) {
					 getController().setConferenceAddress(selection);
				 }
				 else if (event.getSource().equals(setStartDateItem)) {
					 getController().setConferenceStartDate(selection);
				 }
				 else if (event.getSource().equals(setEndDateItem)) {
					 getController().setConferenceEndDate(selection);
				 }
				 else if (event.getSource().equals(setPaperDateItem)) {
					 getController().setConferencePaperDate(selection);
				 }
				 else if (event.getSource().equals(setDateRangeItem)) {
					 getController().setConferenceDates(selection);
				 }
				 else if (event.getSource().equals(setBaseURIItem)) {
					 getController().setBaseURI(selection);
				 }
				 else if (event.getSource().equals(setDescriptionItem)) {
					 getController().setConferenceDescription(selection);
				 }
				 else if (event.getSource().equals(setFulltextItem)) {
					 getController().setFullText(selection);
				 }
				 else if (event.getSource().equals(setAbstractDateItem)) {
					 getController().setConferenceAbstractDate(selection);
				 }
			 }
		 }
		 public void widgetDefaultSelected(SelectionEvent event) {
			 this.widgetSelected(event);
		 }
	 }
	
	 protected class ConfFullTextKeyListener implements org.eclipse.swt.events.KeyListener {
		 public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
			 if (e.keyCode == 'a' && ( (SWT.CONTROL == (e.stateMask & SWT.CONTROL)) 
					 || (SWT.COMMAND == (e.stateMask & SWT.COMMAND))) ) {
				 txtareaFullText.selectAll();
			 } 
		 }
		 
		 public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
			 
		 }
	 }	
	 
	 protected class ConfFullTextButtonListener extends org.eclipse.swt.events.SelectionAdapter {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (e.getSource().equals(btnClose)) {
				btnCancelClicked(e);
			}
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
	}
}
