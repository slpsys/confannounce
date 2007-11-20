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

import edu.lehigh.mab305.swproj.Application.*;

public class InternalProxyEditorController {

	protected ConfAnnounceController windowController;
	protected String host = "";
	protected int port = -1;
	protected boolean enabled = false;

	public InternalProxyEditorController(ConfAnnounceController cont) {
		super();
		this.windowController = cont;
	}
	
	public void setProxyHost(String host) {
		this.host = host;
		SettingsManager.getInstance().setProxyHost(host);
	}
	
	public void setProxyPort(int port) {
		this.port = port;
		SettingsManager.getInstance().setProxyPort(port);
	}
	
	public void setProxyEnabled(boolean enabled) {
		this.enabled = enabled;
		SettingsManager.getInstance().setProxyEnabled(enabled);
	}
	
	public String getProxyHost() {
		return SettingsManager.getInstance().getProxyHost();
	}
	
	public int getProxyPort() {
		return SettingsManager.getInstance().getProxyPort();
	}
	
	public boolean getProxyEnabled() {
		return SettingsManager.getInstance().getProxyEnabled();
	}
}
