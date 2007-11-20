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
package edu.lehigh.mab305.swproj.Application;

/**
 * Contains the local path constants which should be changed if the location of the
 * icon images ever change. Note that these <b>must</b> be correct at runtime, else
 * the ConfAnnounce tool will immediately crash. Also, all local path names should 
 * be relative; that is, the generic icon path is "../icons/", which is relative to
 * the location of the executable jar. Any changes to this file should be relative,
 * else the resulting program will [probably] not work on other machines.
 * 
 * @author <a href="mailto:mab305@lehigh.edu">Marc Bollinger</a>
 */
public class ImageFileConstants {
	public static final String APP_ICON = "../icons/ca.png";
	public static final String ADD_ICON = "../icons/add.png";
	public static final String REMOVE_ICON = "../icons/remove.png";
	public static final String NEW_ICON = "../icons/new_menu.png";
	public static final String EASYCONF_ICON = "../icons/eject_menu.png";
	public static final String OPEN_ICON = "../icons/folder_menu.png";
	public static final String SAVE_ICON = "../icons/burn_menu.png";
	
}
