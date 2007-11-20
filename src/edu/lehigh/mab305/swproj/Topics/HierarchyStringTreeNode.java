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
package edu.lehigh.mab305.swproj.Topics;

import java.util.ArrayList;
import java.io.Serializable;

public class HierarchyStringTreeNode implements Serializable {

	private static final long serialVersionUID = 1643583248768861251L;
	
	protected ArrayList<HierarchyStringTreeNode> children;
	protected String data;
	protected String shortName;
	protected boolean hasParent;
	
	public HierarchyStringTreeNode(String obj) {
		this.data = obj;
		this.children = new ArrayList<HierarchyStringTreeNode>();
		this.hasParent = false;
		this.shortName = null;
	}
	
	
	/**
	 * @return the hasParent
	 */
	public boolean getHasParent() {
		return hasParent;
	}

	/**
	 * @param hasParent the hasParent to set
	 */
	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	public void addChild(HierarchyStringTreeNode obj) {
		children.add(obj);
	}
	
	public ArrayList<HierarchyStringTreeNode> getChildren() {
		return this.children;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}


	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	public String toString() {
		return this.data.toString();
	}
}
