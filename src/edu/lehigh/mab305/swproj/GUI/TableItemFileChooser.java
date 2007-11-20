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

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.lehigh.mab305.swproj.Application.*;

public class TableItemFileChooser extends Composite {

	protected Text txtFileName = null;
	protected Button btnFileOpen = null;
	protected static boolean userSelectedCustomPath = false;
	
	public TableItemFileChooser(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	protected void initialize() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setSize(new Point(295, 20));
		setLayout(gridLayout);
		
		GridData gridButton = new GridData(GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_END);
		GridData gridText = new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING);
		
		txtFileName = new Text(this, SWT.BORDER);
		txtFileName.setLayoutData(gridText);
		btnFileOpen = new Button(this, SWT.PUSH);
		btnFileOpen.setLayoutData(gridButton);
		btnFileOpen.setText("   ...   ");
		btnFileOpen.addSelectionListener(new TableItemFileEventHandler());
	}
	
	public void setText(String text) {
		this.txtFileName.setText(text);
	}
	
	public String getText() {
		return this.txtFileName.getText();
	}
	
	public void addModifyListener(ModifyListener listener) {
		this.txtFileName.addModifyListener(listener);
	}
	
	public void addKeyListener(KeyListener listener) {
		this.txtFileName.addKeyListener(listener);
	}
	
	protected Shell getParentPane() {
		return this.getShell();
	}
	
	protected class TableItemFileEventHandler implements SelectionListener {
		
		public void widgetSelected(SelectionEvent e) {
			FileDialog fd = new FileDialog(getParentPane(), SWT.OPEN);
			String infile = "";
			String exts[] = {"*.owl;*.rdf"};
			fd.setFilterPath(SettingsManager.getDefaultOntologypath());
			fd.setFilterExtensions(exts);
			infile = fd.open();
			if (infile != null) {
				System.out.println(fd.getFilterPath());
				txtFileName.setText(infile);
			}
		}
		
		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
