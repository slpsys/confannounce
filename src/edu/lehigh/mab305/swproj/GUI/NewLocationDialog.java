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

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Text;

public class NewLocationDialog {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	protected NewLocationController controller;  //  @jve:decl-index=0:
	@SuppressWarnings("unused") private Label lblURI = null, lblDisplayName = null, lblBlank = null;
	protected Button btnOk = null;
	protected Button btnCancel = null;
	protected Text txtURI = null, txtDisplayName = null;
	protected String _locationURI = null, _locationName = null;
	
	public NewLocationDialog (NewLocationController controller, String windowText) {
		this.controller = controller;
		createSShell();
		//this.lblURI.setText(labelText);
		this.sShell.setText(windowText);
		//this.runNewLocationDialog();
	}
	
	public Composite getCompositePane() {
		return this.sShell;
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
		sShell.setSize(new Point(387, 165));
		sShell.setLayout(gridLayout);

		lblURI = new Label(sShell, SWT.NONE);
		lblURI.setText("New Location URI:");
		gdFillHoriz.heightHint = 13;
		lblURI.setLayoutData(gdFillHoriz);
		//lblURI.setBounds(new Rectangle(5, 5, 417, 13));
		
		txtURI = new Text(sShell, SWT.BORDER);
		gdFillHoriz.heightHint = 17;
		txtURI.setLayoutData(gdFillHoriz);
		txtURI.addKeyListener(new URIDialogEventHandler());
		
		lblDisplayName = new Label(sShell, SWT.NONE);
		lblDisplayName.setText("Location Display Name:");
		gdFillHoriz.heightHint = 13;
		gdFillHoriz.widthHint = 200;
		lblDisplayName.setLayoutData(gdFillHoriz);
		
		txtDisplayName = new Text(sShell, SWT.BORDER);
		gdFillHoriz.heightHint = 17;
		txtDisplayName.setLayoutData(gdFillHoriz);
		txtDisplayName.addKeyListener(new URIDialogEventHandler());
		
		lblBlank = new Label(sShell, SWT.NONE);
		lblDisplayName.setLayoutData(gdButton);
		lblBlank = new Label(sShell, SWT.NONE);
		lblDisplayName.setLayoutData(gdButton);
		
		gdButton.heightHint = 22;
		btnOk = new Button(sShell, SWT.NONE);
		//btnOk.setBounds(new Rectangle(264, 45, 76, 23));
		btnOk.setText("OK");
		btnOk.setLayoutData(gdButton);
		btnOk.addSelectionListener(new URIDialogEventHandler());
		
		btnCancel = new Button(sShell, SWT.NONE);
		//btnCancel.setBounds(new Rectangle(344, 45, 76, 23));
		btnCancel.setLayoutData(gdButton);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new URIDialogEventHandler());
	}

	/***
	 * dispose: Note: *not* a real disposal method, this simply allows the shell to be disposed from
	 * inside the Listener class.
	 *
	 */
	public void dispose() {
		this.sShell.dispose();
	}
	
	/**
	 * @return the control
	 */
	public NewLocationController getController() {
		return controller;
	}

	/**
	 * @param control the control to set
	 */
	public void setController(NewLocationController controller) {
		this.controller = controller;
	}
	
	protected class URIDialogEventHandler extends org.eclipse.swt.events.SelectionAdapter implements KeyListener {
		
		public void keyPressed(KeyEvent ke) {
			if (ke.keyCode == SWT.ESC) {
				dispose();
			}
			else if (ke.getSource() instanceof Text) {
				if (ke.character == SWT.LF || ke.character == SWT.CR) {
					accept();
				}
			}
		}
		
		public void keyReleased(KeyEvent ke) {}
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (e.getSource().equals(btnOk)) {
				btnOkClicked(e);
			}
			else if (e.getSource().equals(btnCancel)) {
				btnCancelClicked(e);
			}
		}
	
		public void btnOkClicked (org.eclipse.swt.events.SelectionEvent e) {
			accept();
		}

		protected void accept() {
			String uri = txtURI.getText(), name = txtDisplayName.getText();
			boolean invalid = false;
			lblURI.setForeground(new Color(getCompositePane().getDisplay(), 0, 0, 0));
			lblDisplayName.setForeground(new Color(getCompositePane().getDisplay(), 0, 0, 0));
			if (name == null || name.length() <= 0) {
				lblDisplayName.setForeground(new Color(getCompositePane().getDisplay(), 225, 0, 0));
				lblDisplayName.setText("Location Display Name: (Cannot be null)");
				invalid = true;;
			}
			if (!invalid) {
				_locationURI = uri;
				/*if (uri == null || uri.length() <= 0) {
					_locationName = "#name";
				} else {*/
					_locationName = name;
				//}
				dispose();
			}
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
	}

	/**
	 * @return the _locationName
	 */
	public String getLocationName() {
		return _locationName;
	}

	/**
	 * @return the _locationURI
	 */
	public String getLocationURI() {
		return _locationURI;
	}
}
