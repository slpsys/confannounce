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
 */
package edu.lehigh.mab305.swproj.GUI;

public class Progress {
	protected String strStatus = null;
	protected int nStatus = 0;
	protected String windowTitle = "";
	
	public Progress(String strStatus, int nStatus) {
		super();
		this.nStatus = nStatus;
		this.strStatus = strStatus;
	}
	
	public Progress(int status) {
		super();
		this.nStatus = status;
		this.strStatus = "";
	}
	
	public Progress(String status) {
		super();
		this.nStatus = 0;
		this.strStatus = status;
	}

	public Progress() {
		this.nStatus = 0;
		this.strStatus = "";
	}
	
	/**
	 * @return the nStatus
	 */
	public int getNStatus() {
		return nStatus;
	}
	/**
	 * @param status the nStatus to set
	 */
	public void setNStatus(int status) {
		nStatus = status;
	}
	/**
	 * @return the strStatus
	 */
	public String getStrStatus() {
		return strStatus;
	}
	/**
	 * @param strStatus the strStatus to set
	 */
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}
	
	
}
