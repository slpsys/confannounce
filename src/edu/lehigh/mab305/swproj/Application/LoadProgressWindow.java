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
package edu.lehigh.mab305.swproj.Application;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

/**
 * Controls an instance of a progress bar, for use in the multithreaded load and save
 * operations.
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 */
public class LoadProgressWindow {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	protected Label lblStatus = null;
	protected ProgressBar progressBar = null;
	protected boolean running; 
	
	/**
	 * Creates a new progress window with the desired window text and label.
	 * 
	 * @param windowText The text of the progress bar's window.
	 * @param labelText The text displayed in the window, above the progress bar
	 * itself.
	 */
	public LoadProgressWindow (String windowText, String labelText) {
		createSShell();
		this.sShell.setText(windowText);
		this.running = false;
	}
	
	/**
	 * Closes and destroys the progress bar window.
	 */
	public void stop() {
		this.running = false;
	}
	
	/**
	 * The main loop for the window.
	 */
	public void run() {
		Display disp = this.sShell.getDisplay();
		this.running = true;
		this.sShell.open();
		while(!this.sShell.isDisposed() && this.running) {
			if(!disp.readAndDispatch()) {
				disp.sleep();
			}
		}
		this.sShell.dispose();
	}

	/**
	 * Allows the caller to tell if the progress bar window is disposed, or
	 * is in the process of disposing.
	 * 
	 * @return Whether or not the window is/is being destroyed.
	 */
	public boolean isDisposed() {
		return this.sShell.isDisposed();
	}
	
	/**
	 * Updates the progress bar's, well, progress. Note that this is additive, thus
	 * if the progress is at 40%, and the desired progress is 50%, an integer value
	 * of 10 should be provided to this method.
	 * 
	 * @param status The integerial (0-100) progress of the window's progress bar.
	 */
	protected void updateBar(int status) {
		if (!this.sShell.isDisposed() && !this.progressBar.isDisposed()) { 
			this.progressBar.setSelection(this.progressBar.getSelection() + status);
		}
	}
	
	/**
	 * Sets the progress of the progress bar outright; that is, if the current 
	 * progress is 40%, sending a value of 10 will set the progress to 10%.
	 * 
	 * @param status The integerial (0-100) progress of the window's progress bar.
	 */
	public synchronized void setBarStatus(int status) {
		if (!this.sShell.isDisposed() && !this.progressBar.isDisposed()) {
			this.progressBar.setSelection(status);
		}
	}
	
	/**
	 * Modifies the text of the progress bar window's label.
	 * 
	 * @param status The new text label for the progress bar window.
	 */
	public synchronized void updateStatus(String status) {
		this.lblStatus.setText(status);
	}
	
	/**
	 * This method initializes sShell
	 */
	protected void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
		
		sShell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.BORDER | SWT.APPLICATION_MODAL);
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setLayoutData(new GridData(SWT.FILL));
		sShell.setSize(new Point(491, 103));
		lblStatus = new Label(sShell, SWT.NONE);
		lblStatus.setText("Label");
		progressBar = new ProgressBar(sShell, SWT.SMOOTH);
		
	}

	/***
	 * Note: *not* a real disposal method, this simply allows the shell to be disposed from
	 * inside the Listener class.
	 *
	 */
	public synchronized void dispose() {
		this.sShell.dispose();
	}
	
	protected class EasyConfWindowItemListener implements SelectionListener {
		 public void widgetSelected(SelectionEvent event) {
		 }
		 public void widgetDefaultSelected(SelectionEvent event) {
			 this.widgetSelected(event);
		 }
	 }
	
	protected class ConfFulTextKeyListener implements org.eclipse.swt.events.KeyListener {
		 public void keyPressed(org.eclipse.swt.events.KeyEvent e) {

		 }
		 
		 public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
			 
		 }
	 }	
	 
	protected class ConfFullTextButtonListener extends org.eclipse.swt.events.SelectionAdapter {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			/*if (e.getSource().equals(btnClose)) {
				btnCancelClicked(e);
			}*/
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
	}
}
