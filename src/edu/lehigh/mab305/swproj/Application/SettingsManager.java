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
import java.util.ArrayList;
import java.io.*;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Maintains the general application settings associated with ConfAnnounce,
 * and provides routines for copying/importing settings from serialized 
 * objects of this type.
 * <p>Note that the Internet proxy described below is a separate mechanism 
 * from the internal proxy described elsewhere. The Internet proxy settings
 * allow for the application to work behind a traditional Internet proxy and/or
 * firewall, where the host and port are specified. Currently, this does not 
 * allow for proxies which require authentication, but this could be fairly
 * trivially activated by adding the following snippet:
 *<p><pre>URLConnection connection = url.openConnection();
 * String password = "username:password";
 * String encodedPassword = base64Encode( password );
 * connection.setRequestProperty( "Proxy-Authorization", encodedPassword );</pre></p>
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 */
public class SettingsManager implements Serializable{

	protected HashMap<String, ArrayList<String>> settings = null;
	protected HashMap<String, Integer> reservedEntries = null;
	
	/**
	 * Reserved hash entry name for storing location ontologies loaded.
	 */
	public static final String LOCATIONS = "locations";
	/**
	 * Reserved hash entry name for storing topic ontologies loaded.
	 */
	public static final String TOPICS = "topics";
	/**
	 * Reserved hash entry name for storing the Internet proxy host address.
	 */
	public static final String PROXY_HOST = "proxyHost";
	/**
	 * Reserved hash entry name for storing the Internet proxy host port.
	 */
	public static final String PROXY_PORT = "proxyPort";
	/**
	 * Reserved hash entry name for storing a stringified boolean value, 
	 * determining whether or not the Internet proxy should be enabled.
	 */
	public static final String PROXY_ENABLED = "proxyEnabled";
	/**
	 * Reserved hash entry name for storing the mapping between the URLs and
	 * local file paths.
	 */
	public static final String MAPPING_ENTRIES = "localMapping";
	/**
	 * Reserved hash entry name for storing whether the user has seen the 
	 * warning message box describing how the "download ontologies" option
	 * works.
	 */
	public static final String DOWNLOAD_WARNING = "downloadWarning";
	/**
	 * Reserved hash entry name for storing whether the user has selected
	 * the "download ontologies" option.
	 */
	public static final String DOWNLOAD_IMPORTS = "downloadImports";
	/**
	 * The default names for the first row of entries in the internal proxy editor
	 * window. These will be overwritten as soon as the user enters data in the fields,
	 * but these values are used as a comparison, to
	 */
	public static final String DEFAULT_URL = "URL", DEFAULT_FILENAME = "Local Filename";
	
	/**
	 * The message displayed when a user cancels the window allowing them to supply 
	 * alternate local file mappings for initially-loaded ontologies.
	 */
	public static final String ERRLOADINGONTS_MSG =
			"One or more ontologies specified or imported by\n" +
			"a specified ontology could not be located by the\n" +
			"system. You may choose to re-add these later, but\n" +
			"be aware that composite ontologies loaded in the\n" + 
			"Location and Topic panes may not be correctly\n" +
			"viewable, if they are missing key imports.";
	
	public static String DEFAULT_ONTOLOGY_PATH = null;
	
	protected static SettingsManager _instance = null;
	protected boolean proxyEnabled = false;
	
	// Needs to be static, as it CANNOT be serialized.
	protected static Model openedModel = null;
	
	// This needs to be changed every time this class changes, so that previous versions
	// of the .settings file do not get loaded!
	protected static final long serialVersionUID = 3324857586888213249L;
	
	protected SettingsManager() {
		this.settings = new HashMap<String, ArrayList<String>>();
		reservedEntries = new HashMap<String,Integer>();
		reservedEntries.put(LOCATIONS, 1);
		reservedEntries.put(TOPICS, 1);
		reservedEntries.put(PROXY_HOST, 1);
		reservedEntries.put(PROXY_PORT, 1);
		reservedEntries.put(PROXY_ENABLED, 1);
		reservedEntries.put(MAPPING_ENTRIES, 1);
		reservedEntries.put(DEFAULT_URL, 1);
	}
	
	/**
	 * Sets the list of loaded ontologies, either topic or location, depending on
	 * the string key entered.
	 * 
	 * @param type Should be either SettingsManager.LOCATIONS or SettingsManager.TOPICS,
	 * otherwise the list will not be properly retrieved at next program startup. 
	 * @param onts A list of ontologies to be loaded at next program startup, note that
	 * the constituent strings should be the URLs of the ontologies, and will be 
	 * retrieved using the internal proxy settings (i.e. mapped to local files, if 
	 * applicable).
	 */
	public synchronized void setOntologyList(String type, ArrayList<String> onts) {
		this.settings.put(type, onts);
	}
	
	/**
	 * Retrieves a list of the currently loaded topic or location ontologies.
	 * 
	 * @param type Should be either SettingsManager.LOCATIONS or SettingsManager.TOPICS,
	 * otherwise the list will not be properly retrieved at next program startup. 
	 * @return The list containing the currently loaded topic or location ontologies, if
	 * one of the two above settings is passed in, else null.
	 */
	public synchronized ArrayList<String> getOntologyList(String type) {
		ArrayList<String> ret = null;
		if (type.equals(SettingsManager.LOCATIONS) || type.equals(SettingsManager.TOPICS)) {
			ret = this.settings.get(type); 
		}
		return ret;
	}
	
	/**
	 * Determines if a key is currently used in the settings hash.
	 * 
	 * @param s The hash key to desired.
	 * @return True if the settings hash has a value for the key s, else false.
	 */
	public synchronized boolean hasSetting(String s) {
		return this.settings.containsKey(s);
	}
	
	/**
	 * Takes an object meant to come directly from the deserializer, and if the
	 * parameter is a SettingsManager, import its settings into this SettingsManager.
	 * This somewhat odd behavior is due to the function of serialized objects, in 
	 * conjunction with the SettingsManager being a Singleton, thus at the time of 
	 * import, there probably already is one SettingsManager, and we don't want to 
	 * swap objects.
	 * 
	 * @param obj The deserialized SettingsManager object (passed in as an Object).
	 */
	public synchronized void importSettings(Object obj) {
		if (obj instanceof SettingsManager) {
			SettingsManager settingsManager = (SettingsManager) obj;
			if (settingsManager.hasSetting(SettingsManager.TOPICS)) {
				this.settings.put(SettingsManager.TOPICS, settingsManager.getOntologyList(SettingsManager.TOPICS));
			}
			if (settingsManager.hasSetting(SettingsManager.LOCATIONS)) {
				this.settings.put(SettingsManager.LOCATIONS, settingsManager.getOntologyList(SettingsManager.LOCATIONS));
			}
			
			if (settingsManager.hasSetting(SettingsManager.MAPPING_ENTRIES)) {
				this.settings.put(SettingsManager.MAPPING_ENTRIES, settingsManager.getMappingAsList());
			}
			else {
				ArrayList<String> defaultSettings = new ArrayList<String>();
				defaultSettings.add(URLConstants.CONFERENCE_ONTOLOGY);
				defaultSettings.add(SettingsManager.getDefaultOntologypathWithDelimiter() + URLConstants.LOCAL_CONFERENCE_ONTOLOGY);
				defaultSettings.add(URLConstants.MONTHS_ONTOLOGY);
				defaultSettings.add(SettingsManager.getDefaultOntologypathWithDelimiter() + URLConstants.LOCAL_MONTHS_ONTOLOGY);
				this.settings.put(SettingsManager.MAPPING_ENTRIES, defaultSettings);
			}
			
			// Retrieve and set up all of the proxy settings.
			if (settingsManager.hasSetting(SettingsManager.PROXY_ENABLED)) {
				boolean b;
				ArrayList<String> a = settingsManager.settings.get(SettingsManager.PROXY_ENABLED);
				// Persist it!
				this.settings.put(PROXY_ENABLED, a);
				b = Boolean.parseBoolean(a.get(0));
				this.proxyEnabled = b;
				if (b) {
					// Set the system property
					System.getProperties().put("proxySet","true");
				}
				else {
					System.getProperties().put("proxySet","false");
				}
			}
			if (settingsManager.hasSetting(SettingsManager.PROXY_HOST)) {
				ArrayList<String> a = settingsManager.settings.get(SettingsManager.PROXY_HOST);
				// Persist it!
				this.settings.put(SettingsManager.PROXY_HOST, a);
				if (this.proxyEnabled && a.get(0) != null) { 
					System.getProperties().put("proxyHost", a.get(0));
				}
			}
			if (settingsManager.hasSetting(SettingsManager.PROXY_PORT)) {
				ArrayList<String> a = settingsManager.settings.get(SettingsManager.PROXY_PORT);
				// Persist it!
				this.settings.put(SettingsManager.PROXY_PORT, a);
				if (this.proxyEnabled && a.get(0) != null) { 
					System.getProperties().put("proxyPort", a.get(0));
				}
			}
			if (!settingsManager.getDownloadWarning()) {
				this.setNoDownloadWarning();
			}
			if (settingsManager.settings.containsKey(DOWNLOAD_IMPORTS)) {
				this.setDownloadImports(settingsManager.getDownloadImports());
			}
		}
	}
	
	/**
	 * Factory method for the SettingsManager Singleton.
	 * 
	 * @return The lone SettingsManager object.
	 */
	public synchronized static SettingsManager getInstance() {
		if (_instance == null) {
			_instance = new SettingsManager();
		}
		return _instance;
	}

	protected synchronized ArrayList<String> getMappingAsList() {
		ArrayList<String> temp = null;
		temp = this.settings.get(MAPPING_ENTRIES);
		return temp;
	}
	
	/**
	 * Sets whether the download warning has been seen by the user to true.
	 */
	public synchronized void setNoDownloadWarning() {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("true");
		this.settings.put(DOWNLOAD_WARNING, temp);
	}
	
	/**
	 * Returns whether the download warning has been seen by the user.
	 * 
	 * @return Whether the download warning has been seen by the user
	 */
	public synchronized boolean getDownloadWarning() {
		boolean ret = true;
		if (this.settings.containsKey(DOWNLOAD_WARNING)) {
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Sets whether downloaded URLs should be saved to a permanent local
	 * file.
	 * 
	 * @param download Whether downloaded URLs should be saved to a permanent
	 * local file.
	 */
	public synchronized void setDownloadImports(boolean download) {
		ArrayList<String> temp = new ArrayList<String>();
		if (download) {
			temp.add("true");
		}
		else {
			temp.add("false");
		}
		this.settings.put(DOWNLOAD_IMPORTS, temp);
	}
	
	/**
	 * Returns whether downloaded URLs should be saved to a permanent local file.
	 * 
	 * @return Whether downloaded URLs should be saved to a permanent local file.
	 */
	public synchronized boolean getDownloadImports() {
		boolean ret = false;
		ArrayList<String> temp;
		if (this.settings.containsKey(DOWNLOAD_IMPORTS)) {
			temp = this.settings.get(DOWNLOAD_IMPORTS);
			if (temp != null && temp.size() > 0) {
				ret = Boolean.parseBoolean(temp.get(0));
			}
		}
		return ret;
	}
	
	/**
	 * Retrieves the URL/Local file mapping for this application.
	 * 
	 * @return A hashmap representing the URL/Local file mappings.
	 */
	public synchronized HashMap<String,String> getMapping() {
		HashMap<String, String> map = new HashMap<String, String>();
		ArrayList<String> temp;
		int count =  0;
		temp = this.settings.get(MAPPING_ENTRIES);
		if (temp != null) {
			String str = null;
			for (String s : temp) {
				if (0 == count % 2) {
					str = s;
				}
				else {
					map.put(str, s);
				}
				count++;
			}
		}
		return map;
	}
	
	/**
	 * Sets the URL/Local file mapping for this application.
	 * 
	 * @param map A hashmap representing the URL/Local file mappings.
	 */
	public synchronized void setMapping(HashMap<String,String> map) {
		ArrayList<String> temp = new ArrayList<String>();
		if (map != null) {
			for (String key : map.keySet()) {
				if (!this.reservedEntries.containsKey(key)) {
					temp.add(key);
					temp.add(map.get(key));
				}
			}
			this.settings.put(SettingsManager.MAPPING_ENTRIES, temp);
			this.settings.remove(DEFAULT_URL);
		}
	}
	
	/**
	 * Retrieves the host address of the Internet proxy.
	 * 
	 * @return A string version of the Internet proxy host address.
	 */
	public synchronized String getProxyHost() {
		String ret = null;
		ArrayList<String> temp;
		temp = this.settings.get(PROXY_HOST);
		if (temp != null && temp.size() > 0) {
			ret = temp.get(0);
		}
		return ret;
	}
	
	/**
	 * Sets the host address of the Internet proxy.
	 * 
	 * @param host A string version of the Internet proxy host address.
	 */
	public synchronized void setProxyHost(String host) {
		ArrayList<String> temp = new ArrayList<String>();
		if (host != null && host.length() > 0) {
			temp.add(host);
		}
		this.settings.put(PROXY_HOST, temp);
	}
	
	/**
	 * Retrieves the host port of the Internet proxy.
	 * 
	 * @return A string version of the Internet proxy host port.
	 */
	public synchronized int getProxyPort() {
		int ret = -1;
		ArrayList<String> temp;
		temp = this.settings.get(PROXY_PORT);
		if (temp != null && temp.size() > 0) {
			String s = temp.get(0);
			try {
				ret = Integer.parseInt(s);
			}
			catch (NumberFormatException ne){
				ret = -1;
			}
		}
		return ret;
	}
	
	/**
	 * Sets the host port of the Internet proxy.
	 * 
	 * @param port A string version of the Internet proxy host port.
	 */
	public synchronized void setProxyPort(int port) {
		ArrayList<String> temp = new ArrayList<String>();
		if (port > 0) {
			temp.add(Integer.toString(port));
		}
		this.settings.put(PROXY_PORT, temp);
	}
	
	/**
	 * Retrieves whether the Internet proxy should be enabled.
	 * 
	 * @return Whether the Internet proxy should be enabled.
	 */
	public synchronized boolean getProxyEnabled() {
		boolean ret = false;
		ArrayList<String> temp;
		temp = this.settings.get(PROXY_ENABLED);
		if (temp != null && temp.size() > 0) {
			String s = temp.get(0);
			ret = Boolean.parseBoolean(s);
		}
		return ret;
	}
	
	/**
	 * Sets whether the Internet proxy should be enabled.
	 * 
	 * @param enabled Whether the Internet proxy should be enabled.
	 */
	public synchronized void setProxyEnabled(boolean enabled) {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(Boolean.toString(enabled));
		this.settings.put(PROXY_ENABLED, temp);
	}
	
	/**
	 * Returns the default path local to this host computer where ontologies
	 * can be found.
	 * 
	 * @return The local path of the default ontologies directory.
	 */
	public synchronized static String getDefaultOntologypath() {
		String ret = SettingsManager.DEFAULT_ONTOLOGY_PATH;
		if (ret == null) {
			if (System.getProperty("user.dir") != null) {
				String delimiter;
				ret = System.getProperty("user.dir");
				if (ret.contains("/")) {
					delimiter = "/";
				}
				else {
					delimiter = "\\";
				}
				ret = ret.concat(delimiter + "ontologies"); 
			}
		}
		return ret;
	}
	
	/**
	 * Returns the default path local to this host computer where ontologies
	 * can be found.
	 * 
	 * @return The local path of the default ontologies directory.
	 */
	public synchronized static String getDefaultOntologypathWithDelimiter() {
		String ret = SettingsManager.DEFAULT_ONTOLOGY_PATH;
		if (ret == null) {
			if (System.getProperty("user.dir") != null) {
				String delimiter;
				ret = System.getProperty("user.dir");
				if (ret.contains("/")) {
					delimiter = "/";
				}
				else {
					delimiter = "\\";
				}
				if (ret.charAt(ret.length()-1) == delimiter.toCharArray()[0]) {
					ret.substring(0, ret.length()-2);
				}
				ret = ret.substring(0, ret.lastIndexOf(delimiter));
				ret = ret.concat(delimiter + "ontologies" + delimiter); 
			}
		}
		return ret;
	}

	/**
	 * @return the openedModel
	 */
	public static Model getOpenedModel() {
		return SettingsManager.openedModel;
	}

	/**
	 * @param openedModel the openedModel to set
	 */
	public static void setOpenedModel(Model openedModel) {
		SettingsManager.openedModel = openedModel;
	}
}
