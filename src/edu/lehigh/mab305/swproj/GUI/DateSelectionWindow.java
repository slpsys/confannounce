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
import org.eclipse.swt.widgets.Composite;

import java.text.DateFormat;
import org.eclipse.swt.SWT;
import java.text.SimpleDateFormat;
import org.vafada.swtcalendar.SWTCalendar;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;

public class DateSelectionWindow extends Composite {
	
	protected DateFormat XSDFormat;
	protected SWTCalendar calendar;
	protected Button btnOk = null;
	protected Button btnCancel = null;
	protected DateSelectionController controller= null;
	
	public DateSelectionWindow(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		this.XSDFormat = new SimpleDateFormat("yyyy-MM-dd");
		initialize(parent);
	}
	
	protected void initialize(Composite parent) {
		calendar = new SWTCalendar(this, SWT.NONE | SWTCalendar.RED_WEEKEND);
		calendar.setBounds(new Rectangle(6, 7, 185, 134));
		btnOk = new Button(this, SWT.NONE);
		btnOk.setBounds(new Rectangle(35, 152, 76, 23));
		btnOk.setText("OK");
		btnOk.addSelectionListener(new DateSelectionWidgetButtonListener());
		
		
		btnCancel = new Button(this, SWT.NONE);
		btnCancel.setBounds(new Rectangle(116, 152, 76, 23));
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new DateSelectionWidgetButtonListener());
		
		setSize(new Point(197, 182));
		setLayout(null);
		this.controller.isOpen = true;
	}
	
	/**
	 * @return the controller
	 */
	public DateSelectionController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(DateSelectionController controller) {
		this.controller = controller;
	}
	
	public void dispose() {
		this.controller.isOpen = false;
		super.dispose();
		//this.getShell().dispose();
	}
	
	protected class DateSelectionWidgetButtonListener extends org.eclipse.swt.events.SelectionAdapter {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (e.getSource().equals(btnOk)) {
				btnOkClicked(e);
			}
			else if (e.getSource().equals(btnCancel)) {
				btnCancelClicked(e);
			}
		}
	
		public void btnOkClicked (org.eclipse.swt.events.SelectionEvent e) {
			getController().setDate(XSDFormat.format(calendar.getCalendar().getTime()));			
			dispose();
		}
		
		public void btnCancelClicked (org.eclipse.swt.events.SelectionEvent e) {
			dispose();
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
