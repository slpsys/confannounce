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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class LoadProgressWindow {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	protected Label lblStatus = null;
	protected ProgressBar progressBar = null;
	protected boolean running; 
	
	public LoadProgressWindow (String windowText, String labelText) {
		createSShell();
		this.sShell.setText(windowText);
		this.running = false;
	}
	
	public void stop() {
		this.running = false;
	}
	
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
	
	public void updateBar(int status) {
		if (!this.sShell.isDisposed() && !this.progressBar.isDisposed()) { 
			this.progressBar.setSelection(this.progressBar.getSelection() + status);
		}
	}
	
	public synchronized void setBarStatus(int status) {
		if (!this.sShell.isDisposed() && !this.progressBar.isDisposed()) {
			this.progressBar.setSelection(status);
		}
	}
	
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
	 * dispose: Note: *not* a real disposal method, this simply allows the shell to be disposed from
	 * inside the Listener class.
	 *
	 */
	public synchronized void dispose() {
		this.sShell.dispose();
	}
}
