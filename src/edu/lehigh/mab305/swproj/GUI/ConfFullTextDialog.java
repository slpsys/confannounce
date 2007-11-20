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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class ConfFullTextDialog {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Text txtareaFullText = null;
	private Button btnOk = null;
	private Button btnCancel = null;
	private ConfFullTextController controller = null;
	private Label lblFulltext = null;

	public ConfFullTextDialog (ConfFullTextController controller, String windowText, String labelText) {
		this.controller = controller;
		createSShell();
		this.lblFulltext.setText(labelText);
		this.sShell.setText(windowText);
		this.runURIDialog();
	}
	
	private void runURIDialog() {
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
		sShell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.BORDER | SWT.APPLICATION_MODAL);
		sShell.setText("Shell");
		sShell.setSize(new Point(491, 316));
		sShell.setLayout(null);
		txtareaFullText = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txtareaFullText.setBounds(new Rectangle(6, 28, 468, 221));
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setBounds(new Rectangle(318, 256, 76, 23));
		btnOk.setText("Ok");
		btnOk.addSelectionListener(new ConfFullTextButtonListener());
		btnCancel = new Button(sShell, SWT.NONE);
		btnCancel.setBounds(new Rectangle(397, 256, 76, 23));
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new ConfFullTextButtonListener());
		lblFulltext = new Label(sShell, SWT.NONE);
		lblFulltext.setBounds(new Rectangle(5, 4, 468, 16));
		lblFulltext.setText("Label");
	}

	/**
	 * @return the controller
	 */
	public ConfFullTextController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(ConfFullTextController controller) {
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
	
	protected class ConfFullTextButtonListener extends org.eclipse.swt.events.SelectionAdapter {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (e.getSource().equals(btnOk)) {
				btnOkClicked(e);
			}
			else if (e.getSource().equals(btnCancel)) {
				btnCancelClicked(e);
			}
		}
	
		public void btnOkClicked (org.eclipse.swt.events.SelectionEvent e) {
			controller.setFullText(txtareaFullText.getText());
			dispose();
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
	}
}
