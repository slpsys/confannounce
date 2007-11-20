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

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Text;

import edu.lehigh.mab305.swproj.Application.*;

public class InternalProxyEditorWindow {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	protected InternalProxyEditorController controller = null;
	protected Label lblProxyEditor = null;
	protected Table tblEditor = null;
	protected TableEditor editor = null;
	protected TableItem activeItem = null;
	protected Control activeText = null;
	protected ToolBar toolBar = null;	
	protected ToolItem newListRowItem = null, delListRowItem = null;
	protected int colIndex = 0;
	protected static final int ROW_HEIGHT = 20;
	protected boolean isSystemWide = false;
	protected ArrayList<String> errorURLList = null;
	
	public InternalProxyEditorWindow (InternalProxyEditorController controller, String windowText) {
		this.controller = controller;
		this.isSystemWide = true;
		createSShell();
		this.sShell.setText(windowText);
	}
	
	public InternalProxyEditorWindow (InternalProxyEditorController controller, String windowText, ArrayList<String> urls) {
		this.controller = controller;
		this.isSystemWide = false;
		this.errorURLList = urls;
		createSShell();
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
		GridLayout gridLayout = new GridLayout(1, false);
		HashMap<String, String> fileMap;
		
		sShell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.BORDER | SWT.RESIZE | SWT.APPLICATION_MODAL);
		sShell.setText("Shell");
		sShell.setSize(new Point(500, 400));
		sShell.setLayout(gridLayout);
		sShell.addKeyListener(new InternalProxyTableEventHandler());
		sShell.addDisposeListener(new InternalProxyTableEventHandler());
		
		try {
			this.toolBar = new ToolBar(sShell, SWT.HORIZONTAL);
		    //this.coolBar.setLayoutData(new GridData(SWT));
			this.toolBar.setLayout(new FillLayout());
		    
			this.newListRowItem = new ToolItem(toolBar, SWT.FLAT);
			this.newListRowItem.setImage(new Image(sShell.getDisplay(), ImageFileConstants.ADD_ICON));
			this.newListRowItem.addSelectionListener(new InternalProxyTableEventHandler());
			this.newListRowItem.setToolTipText("Add Row");
			
		    this.delListRowItem = new ToolItem(toolBar, SWT.FLAT);
		    this.delListRowItem.setImage(new Image(sShell.getDisplay(), ImageFileConstants.REMOVE_ICON));
		    this.delListRowItem.addSelectionListener(new InternalProxyTableEventHandler());
		    this.delListRowItem.setToolTipText("Delete Row");
		}
		catch (SWTException se) {
			
		}
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		GridData gridData2 = new GridData(GridData.FILL_BOTH);
	    gridData.grabExcessHorizontalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
	    
		this.lblProxyEditor = new Label(sShell, SWT.NONE);
		this.lblProxyEditor.setLayoutData(gridData);
		this.lblProxyEditor.setText("Mapping Table:");
		
	    tblEditor = new Table(sShell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
	    tblEditor.setLinesVisible(true);
	    tblEditor.setHeaderVisible(true);
	    tblEditor.setLayoutData(gridData2);
	    tblEditor.addMouseListener(new InternalProxyTableEventHandler());
	    tblEditor.addKeyListener(new InternalProxyTableEventHandler());
	    
	    TableColumn col = new TableColumn(tblEditor, SWT.NONE);
	    col.setText("URL");
	    col.setWidth(200);
	    col = new TableColumn(tblEditor, SWT.NONE);
	    col.setText("Local Filename/Path");
	    col.setWidth(300);
	    
	    if (this.isSystemWide) {
	    	fileMap = SettingsManager.getInstance().getMapping();
	    }
	    else {
	    	fileMap = new HashMap<String, String>();
	    	for (String s : this.errorURLList) {
	    		fileMap.put(s, "");
	    	}
	    }
	    for (String s : fileMap.keySet()) {
	    	TableItem test = new TableItem(tblEditor, SWT.NONE);
	    	Image image = new Image(null, 1, ROW_HEIGHT);
	    	test.setImage(image);
	    	test.setText(0, s);	    
	    	test.setText(1, fileMap.get(s));
	    }
	    if (fileMap.keySet().size() == 0) {
	    	TableItem test = new TableItem(tblEditor, SWT.NONE);
	    	Image image = new Image(null, 1, ROW_HEIGHT);
	    	test.setImage(image);
	    	test.setText(0, SettingsManager.DEFAULT_URL);	    
	    	test.setText(1, SettingsManager.DEFAULT_FILENAME);
	    }
	    
	    editor = new TableEditor (tblEditor);
	    tblEditor.addSelectionListener (new InternalProxyTableEventHandler());
	}

	/**
	 * @return the controller
	 */
	public InternalProxyEditorController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(InternalProxyEditorController controller) {
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
	 
	protected void close() {
		 HashMap<String, String> map = new java.util.HashMap<String,String>();
		 for (int i = 0; i < tblEditor.getItemCount(); i++) {
			 TableItem item = tblEditor.getItem(i);
			 if (item != null) {
				 String k = item.getText(0), v = item.getText(1);
				 if (k != null && k.length() > 0 && v != null && v.length() > 0) {
					 map.put(item.getText(0), item.getText(1));
				 }
			 }
		 }
		 if (this.isSystemWide) {
			 SettingsManager.getInstance().setMapping(map);
		 }
		 else {
			 SettingsManager manager = SettingsManager.getInstance();
			 HashMap<String, String> newMap = manager.getMapping();
			 newMap.putAll(map);
			 manager.setMapping(newMap);
		 }
		 this.sShell.dispose();
	 }
	 
	protected class InternalProxyTableEventHandler extends org.eclipse.swt.events.SelectionAdapter 
	 	implements KeyListener, MouseListener, ModifyListener, DisposeListener {
				
		public void modifyText(ModifyEvent e) {
			if (e.getSource() instanceof Text) {
				activeItem.setText(colIndex, ((Text)e.getSource()).getText());
			}
		}
		
		public void widgetDisposed(DisposeEvent e) {
			if (e.getSource().equals(sShell)) {
				close();
			}
		}
		
		public void widgetSelected(SelectionEvent e) {
			boolean newAddedOrGoToItemSelect = false;
			if (e.getSource().equals(newListRowItem)) {
				if (tblEditor.getItemCount() == 0) {
					TableItem newItem = new TableItem(tblEditor, SWT.NONE);
					Image image = new Image(null, 1, ROW_HEIGHT);
					newItem.setImage(image);
					newItem.setText(0, "");	    
					newItem.setText(1, "");
					tblEditor.select(tblEditor.getItemCount()-2);
					colIndex = 0;
					newAddedOrGoToItemSelect = true;
				}
				else {
					TableItem item = tblEditor.getItem(tblEditor.getItemCount()-1);
					if (item != null && item.getText(0) != null && item.getText(0).length() > 0) {
						TableItem newItem = new TableItem(tblEditor, SWT.NONE);
						Image image = new Image(null, 1, ROW_HEIGHT);
						newItem.setImage(image);
						newItem.setText(0, "");	    
						newItem.setText(1, "");
						tblEditor.select(tblEditor.getItemCount()-1);
						tblEditor.select(tblEditor.getItemCount()-1);
						colIndex = 0;
						newAddedOrGoToItemSelect = true;
					}
				}
 			}
			else if (e.getSource().equals(delListRowItem)) {
				int index = tblEditor.getSelectionIndex ();
		   		if (index == -1) return;
		   		TableItem item = tblEditor.getItem (index);
		   		
		   		if (activeText != null && activeItem.equals(item) && !activeText.isDisposed()) {
		   			activeText.dispose();
		   		}
		   		if (item != null) {
		   			tblEditor.remove(index);
		   		}
		   		tblEditor.select(tblEditor.getItemCount()-1);
		   		tblEditor.redraw();
		   		sShell.forceFocus();
			}
			else {
				newAddedOrGoToItemSelect = true;
			}
			if (newAddedOrGoToItemSelect) {
				// Clean up any previous editor control
		   		Control oldEditor = editor.getEditor();
		   		if (oldEditor != null)
		   			oldEditor.dispose();	
		   		
		   		//tblEditor.getItem(p).
		   		// Identify the selected row
		   		int index = tblEditor.getSelectionIndex ();
		   		if (index == -1) return;
		   		TableItem item = tblEditor.getItem (index);
		   		activeItem = item;
	
		   		// The control that will be the editor must be a child of the Table
		   		Control ctrl = null;
		   		if (0 == colIndex) {
		   			String itemText;
		   			ctrl = new Text(tblEditor, SWT.NONE);
		   			Text text = (Text) ctrl;
		   			text.addModifyListener(this);
		   			itemText = item.getText(colIndex);
		   			if (itemText != SettingsManager.DEFAULT_URL) {
		   				text.setText(itemText);
		   			}
		   		}	
		   		else {
		   			String itemText;
		   			ctrl = new TableItemFileChooser(tblEditor, SWT.NONE);
		   			TableItemFileChooser chooser = (TableItemFileChooser) ctrl;
		   			chooser.addModifyListener(this);
		   			itemText = item.getText(colIndex);
		   			if (itemText != SettingsManager.DEFAULT_FILENAME) {
		   				chooser.setText(item.getText(colIndex));
		   			}
		   		}
		   		//The text editor must have the same size as the cell and must
		   		//not be any smaller than 50 pixels.
		   		editor.horizontalAlignment = SWT.LEFT;
		   		editor.grabHorizontal = true;
		   		editor.minimumWidth = 50;
		   		
		   		// Open the text editor in the second column of the selected row.
		   		activeText = ctrl;
		   		editor.setEditor (ctrl, item, colIndex);
		   		// Assign focus to the text control
		   		ctrl.setFocus ();
			}
		}
		
		public void mouseDown(MouseEvent e) {
			int index = tblEditor.getSelectionIndex ();
	   		if (index == -1) return;
	   		TableItem item = tblEditor.getItem (index);

	   		for (int i = 0; i < tblEditor.getColumnCount(); i++) {
	   			Rectangle rect = item.getBounds(i);
	   			if(rect.contains(e.x, e.y)) {
	   				colIndex = i;
	   				break;
	   			}
	   		}
		}
		
		public void mouseUp(MouseEvent e) {
		
		}
		
		public void mouseDoubleClick(MouseEvent e) {
			
		}
		
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.ESC) {
				close();
			}
			else {
			//if ((e.getSource() instanceof Text || e.getSource() instanceof Table) && 
				if	(e.keyCode == SWT.TAB || e.keyCode == SWT.LF || e.keyCode == SWT.CR) {
					System.out.println("ok");
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
		}
	}
}
