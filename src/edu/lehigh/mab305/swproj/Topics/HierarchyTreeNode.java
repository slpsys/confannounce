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

public class HierarchyTreeNode<T> {

	protected ArrayList<HierarchyTreeNode<T>> children;
	protected T data;
	protected T shortName;
	protected boolean hasParent;
	
	public HierarchyTreeNode(T obj) {
		this.data = obj;
		this.children = new ArrayList<HierarchyTreeNode<T>>();
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

	public void addChild(HierarchyTreeNode<T> obj) {
		children.add(obj);
	}
	
	public ArrayList<HierarchyTreeNode<T>> getChildren() {
		return this.children;
	}


	/**
	 * @return the shortName
	 */
	public T getShortName() {
		return shortName;
	}


	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(T shortName) {
		this.shortName = shortName;
	}


	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}


	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	public String toString() {
		return this.data.toString();
	}
}
