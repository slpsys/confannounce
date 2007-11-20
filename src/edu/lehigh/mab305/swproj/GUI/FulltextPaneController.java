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

import edu.lehigh.mab305.swproj.ConferenceModel.Conference;

public class FulltextPaneController {
	
	protected ConfAnnounceController control = null;
	protected Conference conf = null;
	protected FulltextPane pane = null;
	
	public FulltextPaneController(ConfAnnounceController control) {
		super();
		this.control = control;
		this.conf = this.control.getConf();
	}
	
	public void setFulltext(String text) {
		if (this.pane != null && text != null) {
			this.pane.setFulltext(text);
		}
	}
	
	public String getFulltext() {
		return this.pane.getFulltext();
	}

	public FulltextPane getPane() {
		return pane;
	}

	public void setPane(FulltextPane pane) {
		this.pane = pane;
	}
	
	public void refreshNew() {
		this.pane.resetInterface();
	}
}
