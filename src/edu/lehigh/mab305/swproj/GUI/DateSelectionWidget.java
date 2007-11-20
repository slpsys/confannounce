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

import java.util.Calendar;
import java.text.DateFormat;
import org.eclipse.swt.SWT;
import java.text.SimpleDateFormat;
import org.vafada.swtcalendar.SWTCalendar;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Simple wrapper class around the SWTCalendar class, exposes some elements of
 * the SWTCalendar's API, and allows for better integration with the controller 
 * model of the ConfAnnounce application.
 *  
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 * @see org.fafada.swtcalendar.SWTCalendar
 *
 */
public class DateSelectionWidget extends Composite {
	
	protected DateFormat XSDFormat;
	protected SWTCalendar calendar;
	protected DateSelectionController controller= null;  //  @jve:decl-index=0:
	
	/**
	 * Default SWT-like constructor.
	 * @param parent Parent composite pane.
	 * @param style SWT style constants.
	 */
	public DateSelectionWidget(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		this.XSDFormat = new SimpleDateFormat("yyyy-MM-dd");
		initialize(parent);
	}
	
	/**
	 * Forwards events to SWTCalendar's event handlers.
	 */	
	public void addFocusListener(FocusListener listener) {
		this.calendar.addFocusListener(listener);
		super.addFocusListener(listener);
	}
	
	/**
	 * Forwards events to SWTCalendar's event handlers.
	 */
	public void addKeyListener(KeyListener listener) {
		this.calendar.addKeyListener(listener);
		super.addKeyListener(listener);
	}
	
	 /**
	  * Forwards events to SWTCalendar's event handlers.
	  */
	public void addMouseListener(MouseListener listener) {
		this.calendar.addMouseListener(listener);
	}
	 
	 /**
	  * Disposes this wrapper widget and its constituents.
	  */
	public void dispose() {
		this.controller.setOpen(false);
		this.calendar.dispose();
		super.dispose();
		//this.getShell().dispose();
	}
	
	/**
	 * Retuns to the java.util.Calendar controlling this Widget. 
	 * @return The java.util.Calendar controlling this Widget.
	 */
	public Calendar getCalendar() {
		return this.calendar.getCalendar();
	}
	
	/**
	 * @return This object's DateSelectionController
	 */
	public DateSelectionController getController() {
		return controller;
	}

	
	/**
	 * Sets the java.util.Calendar controlling this Widget.
	 * @param cal the java.util.Calendar controlling this Widget.
	 */
	public void setCalendar(Calendar cal) {
		this.calendar.setCalendar(cal);
	}
	
	/**
	 * @param controller The DateSelectionController to set
	 */
	public void setController(DateSelectionController controller) {
		this.controller = controller;
	}
	
	/**
	 * Sets the widget pane's visibility.
	 */
	public void setVisible(boolean vis) {
		if (vis) {
			this.controller.setOpen(true);
		}
		super.setVisible(vis);
	}
	
	protected void initialize(Composite parent) {
		calendar = new SWTCalendar(this, SWT.NONE | SWTCalendar.RED_WEEKEND);
		calendar.setBounds(new Rectangle(6, 7, 185, 134));
		calendar
				.addSWTCalendarListener(new org.vafada.swtcalendar.SWTCalendarListener() {
					public void dateChanged(org.vafada.swtcalendar.SWTCalendarEvent e) {
						getController().setDate(XSDFormat.format(calendar.getCalendar().getTime()));
					}
				});
		setSize(new Point(197, 150));
		setLayout(null);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
