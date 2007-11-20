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
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.StyledText;

public class ProxyDialog {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	protected StyledText txtareaFullText = null;
	protected Button btnCancel = null, btnOk = null, chkEnable = null;
	protected ProxyDialogController controller = null;
	protected Label lblProxyHost = null, lblProxyPort = null, lblEnableProxy = null;
	protected Text txtProxyHost = null, txtProxyPort = null;
	
	public ProxyDialog (ProxyDialogController controller, String windowText) {
		boolean b;
		this.controller = controller;
		createSShell();
		this.sShell.setText(windowText);
		if (controller.getProxyHost() == null) {
			this.txtProxyHost.setText("");
		}
		else {
			this.txtProxyHost.setText(controller.getProxyHost());
		}
		
		if (controller.getProxyPort() != -1) {
			this.txtProxyPort.setText(Integer.toString(controller.getProxyPort()));
		}
		else {
			this.txtProxyPort.setText("");
		}
		b = controller.getProxyEnabled();
		this.chkEnable.setSelection(b);
		this.txtProxyHost.setEnabled(b);
		this.txtProxyPort.setEnabled(b);
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
		sShell.setSize(new Point(300, 165));
		sShell.setLayout(null);
		sShell.addKeyListener(new ProxyDialogEventHandler());
		
		btnCancel = new Button(sShell, SWT.NONE);
		btnCancel.setBounds(new Rectangle(215, 100, 76, 23));
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new ProxyDialogEventHandler());
		btnCancel.addKeyListener(new ProxyDialogEventHandler());
		
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setBounds(new Rectangle(158, 100, 56, 23));
		btnOk.setText("OK");
		btnOk.addSelectionListener(new ProxyDialogEventHandler());
		btnOk.addKeyListener(new ProxyDialogEventHandler());
		
		lblProxyHost = new Label(sShell, SWT.NONE);
		lblProxyHost.setBounds(new Rectangle(5, 13, 156, 17));
		lblProxyHost.setText("Proxy Hostname:");
		
		txtProxyHost = new Text(sShell, SWT.BORDER);
		txtProxyHost.setBounds(new Rectangle(5, 30, 284, 19));
		txtProxyHost.addKeyListener(new ProxyDialogEventHandler());
		txtProxyHost.setEnabled(false);
		
		lblProxyPort = new Label(sShell, SWT.NONE);
		lblProxyPort.setBounds(new Rectangle(5, 60, 468, 16));
		lblProxyPort.setText("Proxy Port Number:");
		
		txtProxyPort = new Text(sShell, SWT.BORDER);
		txtProxyPort.setBounds(new Rectangle(5, 77, 284, 19));
		txtProxyPort.addKeyListener(new ProxyDialogEventHandler());
		txtProxyPort.setEnabled(false);
		
		chkEnable = new Button(sShell, SWT.CHECK);
		chkEnable.setBounds(new Rectangle(5, 105, 25, 15));
		chkEnable.addSelectionListener(new ProxyDialogEventHandler());
		chkEnable.addKeyListener(new ProxyDialogEventHandler());
		
		lblEnableProxy = new Label(sShell, SWT.NONE);
		lblEnableProxy.setText("Enable Proxy");
		lblEnableProxy.setBounds(new Rectangle(35, 105, 75, 17));
	}

	/**
	 * @return the controller
	 */
	public ProxyDialogController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(ProxyDialogController controller) {
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
	
	public Composite getCompositePane() {
		return this.sShell;
	}
	
	 class EasyConfWindowItemListener implements SelectionListener {
		 public void widgetSelected(SelectionEvent event) {
			 String selection = txtareaFullText.getSelectionText();
			 if (selection != null && ! selection.equals("")) {
			 }
		 }
		 public void widgetDefaultSelected(SelectionEvent event) {
			 this.widgetSelected(event);
		 }
	 }
	 
	 class ProxyDialogEventHandler extends org.eclipse.swt.events.SelectionAdapter implements KeyListener {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (e.getSource().equals(btnCancel)) {
				btnCancelClicked(e);
			}
			else if (e.getSource().equals(btnOk)) {
				btnOKClicked(e);
			}
			else if (e.getSource().equals(chkEnable)) {
				txtProxyPort.setEnabled(chkEnable.getSelection());
				txtProxyHost.setEnabled(chkEnable.getSelection());
			}
		}
		
		protected void accept() {
			try {
				int port = -1;			
				if (chkEnable.getSelection()) {
					lblProxyHost.setForeground(new Color(getCompositePane().getDisplay(), 0, 0, 0));
					lblProxyHost.setText("Proxy Host:");
					port = Integer.parseInt(txtProxyPort.getText());
					
					// Error checking
					if (port > 0) {
						System.getProperties().put("proxyPort", txtProxyPort.getText());
					}
					if (txtProxyHost.getText() == null || txtProxyHost.getText().length() == 0) {
						throw new Exception();
					}
					
					// System settings here
					System.getProperties().put("proxySet","true");
					System.getProperties().put("proxyHost", txtProxyHost.getText());
					System.getProperties().put("proxyPort", txtProxyPort.getText());
				}
				else {
					txtProxyHost.setText("");
					txtProxyHost.setText("");
					System.getProperties().put("proxySet","false");
					System.getProperties().put("proxyHost", "");
					System.getProperties().put("proxyPort", "");
				}
				controller.setProxyPort(port);
				controller.setProxyHost(txtProxyHost.getText());
				controller.setProxyEnabled(chkEnable.getSelection());
				dispose();
			}
			catch (NumberFormatException ne) {
				if (txtProxyHost.getText() == null || txtProxyHost.getText().length() == 0) {
					lblProxyHost.setForeground(new Color(getCompositePane().getDisplay(), 225, 0, 0));
					lblProxyHost.setText("Proxy Host: (Must be provided!)");
				}
				lblProxyPort.setForeground(new Color(getCompositePane().getDisplay(), 225, 0, 0));
				lblProxyPort.setText("Proxy Port Number: (Must be an integer!)");
			}
			catch (Exception ue) {
				if (txtProxyPort.getText() != null && txtProxyPort.getText().length() > 0) {
					lblProxyPort.setForeground(new Color(getCompositePane().getDisplay(), 0, 0, 0));
					lblProxyPort.setText("Proxy Port Number:");
				}
				lblProxyHost.setForeground(new Color(getCompositePane().getDisplay(), 225, 0, 0));
				lblProxyHost.setText("Proxy Host: (Must be provided!)");
			}
		}
		
		public void btnOKClicked (SelectionEvent e) {
			accept();
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
		
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.ESC) {
				dispose();
			}
			if (e.getSource() instanceof Text ) {
				if (e.character == SWT.CR || e.character == SWT.LF) {
					accept();
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
		}
	}
}
