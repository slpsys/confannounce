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

import edu.lehigh.mab305.swproj.ConferenceModel.*;
import edu.lehigh.mab305.swproj.Application.*;
import java.io.*;

/**
 * The main class of the application (which should then be set as
 * the main class in the packaged Jar file), which primarily facilitates
 * the serialization/deserialization of settings, creation of domain objects,
 * and beginning the window's main loop.
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a> 
 */
public class ConfAnnounceMain {
	
	/**
	 * Main function called when running the application. 
	 * 
	 * @param args Command-line args, system default.
	 */
	public static void main(String args[]) {
		runConfWindow();
	}
	
	/** 
	 * Creates the domain objects, controllers, and the main window, begins 
	 * the main window loop, and controls reading and writing settings on 
	 * load and exit, respectively.
	 */
	public static void runConfWindow() {
		Conference c = new Conference();
		ConfAnnounceController control = new ConfAnnounceController(c);
		readSettings();
		ConfAnnounceWindow hWnd = new ConfAnnounceWindow(control);
		
		hWnd.runConfAnnounce();
		writeSettings();
	}
	
	/**
	 * Run when the application first starts, this method deserializes the 
	 * .settings file in the application's directory, and passes it to be
	 * imported by the SettingsManager.
	 */
	public static void readSettings() {
		ObjectInputStream objOut = null;
		Object obj = null;
		FileInputStream ins = null;
		
		try {
			objOut = new ObjectInputStream(new FileInputStream(".settings"));
			obj = objOut.readObject();
			ins = new FileInputStream("urls.map");
		}
		catch (ClassNotFoundException ce) {
			System.err.print("Can't find class");
		}
		catch (FileNotFoundException fe) {
			System.err.println("Cannot write output ");
		}
		catch (IOException ie) {
			System.err.println("Other IO Exception");
		}
		if (obj != null) {
			SettingsManager.getInstance().importSettings(obj);
		}
		else {
			SettingsManager.getInstance().importSettings(SettingsManager.getInstance());
		}
		SettingsManager.getInstance().setMapping(MappingSerializer.readMappingFile(ins));
		try {
			if (ins != null) {
				ins.close();
			}
		}
		catch (IOException ie) {
			
		}
	}
	
	/**
	 * Serializes the SettingsManager object to the .settings file in the application's
	 * directory; this method is run 
	 *
	 */
	public static void writeSettings() {
		ObjectOutputStream objOut;
		FileOutputStream outs = null;
		
		try {
			SettingsManager s;
			objOut = new ObjectOutputStream(new FileOutputStream(".settings"));
			s = SettingsManager.getInstance();
			objOut.writeObject(s);
			outs = new FileOutputStream("urls.map");
			MappingSerializer.writeMappingFile(outs, SettingsManager.getInstance().getMapping());
			outs.close();
		}
		catch (FileNotFoundException fe) {
			System.err.println("Cannot write output ");
		}
		catch (IOException ie) {
			System.err.println("Other IO Exception");
			System.err.println(ie.getLocalizedMessage());
		}
	}
}
