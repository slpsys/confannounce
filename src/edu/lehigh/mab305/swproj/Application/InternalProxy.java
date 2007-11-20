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
 * @author Marc Bollinger 
 */
package edu.lehigh.mab305.swproj.Application;

import java.util.HashMap;

/**
 * An internal proxy which maps URLs to local files, for use in expediting the process
 * of loading repeatedly-used file. This class is also a Singleton, and getInstance()
 * must be used to retrieve the proxy object.
 *  
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 * @see SettingsManager
 *
 */
public class InternalProxy {
	
	protected static InternalProxy _instance = null;
	protected HashMap<String, String> translator = null;
	
	protected InternalProxy() {
		this.translator = new HashMap<String, String>();
	}
	
	/**
	 * Sets the URL/Local file map.
	 * 
	 * @param map The new URL/Local file mapping.
	 */
	public synchronized void setMapping(HashMap<String, String> map) {
		this.translator = map;
	}
	
	/**
	 * Adds an entry into the proxy's map between URLs and Local files.
	 * 
	 * @param url Public URL of a file.
	 * @param location The local path of the file.
	 */
	public synchronized void addURLMapping(String url, String location) {
		this.translator.put(url, location);
	}
	
	/**
	 * Removes a URL/Local file mapping, based on its URL key.
	 * 
	 * @param url Removes an entry in the proxy associated with this URL
	 */
	public synchronized void removeURLMapping (String url) {
		this.translator.remove(url);
	}
	
	/**
	 * Returns the local path to a file, as linked by its public URL.
	 * 
	 * @param url The desired URL.
	 * @return The path linked to the URL.
	 */
	public synchronized String translateURL(String url) {
		return this.translator.get(url);
	}
	
	/**
	 * Returns the Singleton instance of the InternalProxy; this method
	 * must be used in order to retrieve the instance, and begin using 
	 * the proxy itself.
	 * 
	 * @return The Singleton instance of the InternalProxy object.
	 */
	public synchronized static InternalProxy getInstance() {
		if (InternalProxy._instance == null) {
			InternalProxy._instance = new InternalProxy();
		}
		return InternalProxy._instance;
	}
}
