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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyledText;

public class FulltextPane extends Composite {

	protected FulltextPaneController controller = null;  //  @jve:decl-index=0:
	protected Label lblFulltext = null;
	protected StyledText txtareaFullText = null;
	
	public FulltextPane(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		
		// Make changes here
		//this.localTopicOntologyFiles.add(ConferenceTopicController.ONTO_FILE);
		initialize();
	}

	protected void initialize() {
		
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 1;
	    gridLayout.makeColumnsEqualWidth = true;
	    
	    this.setLayout(gridLayout);
		
	    this.lblFulltext = new Label(this.getCompositePane(), SWT.NONE);
	    this.lblFulltext.setText("Conference Announcement Full Text:");
	    this.lblFulltext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.txtareaFullText = new StyledText(this.getCompositePane(), SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		//txtareaFullText.setBounds(new Rectangle(7, 23, 468, 221));
	    this.txtareaFullText.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
		//this.setLayout(null);
		this.setSize(new Point(420, 415));
	}
	
	public Composite getCompositePane() {
		return this;
	}

	/**
	 * @return the controller
	 */
	public FulltextPaneController getController() {
		return controller;
	}
	
	/**
	 * @param controller the controller to set
	 */
	public void setController(FulltextPaneController controller) {
		this.controller = controller;
	}
	
	public void setFulltext(String text) {
		if (text != null) {
			this.txtareaFullText.setText(text);
		}
	}
	
	public String getFulltext() {
		return this.txtareaFullText.getText(); 
	}
	
	public void resetInterface() {
		this.txtareaFullText.setText("");
	}
}  //  @jve:decl-index=0:visual-constraint="18,0"
