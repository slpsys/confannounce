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

/**
 * Progress is simply a message packaging format allowing the parameters 
 * intended for a ProgressWindow to be sent using the Observer pattern, such
 * that the integer progress, window title, and status text can be sent in
 * a defined format.
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 *
 */
public class Progress {
	protected String strStatus = null;
	protected int nStatus = 0;
	protected String windowTitle = "";
	
	/**
	 * Creates new Progress message object. 
	 * 
	 * @param strStatus The string message to be displayed in the progress window.
	 * @param nStatus The integer status level of the progress bar. 
	 */
	public Progress(String strStatus, int nStatus) {
		super();
		this.nStatus = nStatus;
		this.strStatus = strStatus;
	}
	
	/**
	 * Creates a progress bar wndow with only a status level.
	 * 
	 * @param status The integer status level of the progress bar.
	 */
	public Progress(int status) {
		super();
		this.nStatus = status;
		this.strStatus = "";
	}
	
	/** 
	 * Creates a progress bar window with only a label text, and an
	 * initial status value of 0.
	 * 
	 * @param status The string text of the progress bar window.
	 */
	public Progress(String status) {
		super();
		this.nStatus = 0;
		this.strStatus = status;
	}

	/**
	 * Creates a progress bar window with no label text, and an initial
	 * status value of 0.
	 */
	public Progress() {
		this.nStatus = 0;
		this.strStatus = "";
	}
	
	/**
	 * Returns the status value of the progress bar.
	 * 
	 * @return The status value of the progress bar.
	 */
	public int getNStatus() {
		return nStatus;
	}
	/**
	 * Sets the integer value of the progress bar.
	 * 
	 * @param status the nStatus to set
	 */
	public void setNStatus(int status) {
		nStatus = status;
	}
	/**
	 * Returns the text value of the progress window.
	 * 
	 * @return The text value of the progress window.
	 */
	public String getStrStatus() {
		return strStatus;
	}
	/**
	 * Sets the text value of the progress window lobel.
	 * 
	 * @param strStatus The window progress label
	 */
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	/**
	 * Returns the window title of the progress window.
	 * 
	 * @return The window title.
	 */
	public String getWindowTitle() {
		return windowTitle;
	}

	/**
	 * Sets the window title of the progress window.
	 * 
	 * @param windowTitle The window title of the window.
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}
	
	
}
