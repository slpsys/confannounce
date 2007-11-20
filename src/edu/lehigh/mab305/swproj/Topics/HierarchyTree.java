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

import java.util.*;
import java.io.PrintStream;


public class HierarchyTree<T extends Object> {
	
	protected HierarchyTreeNode<T> root;
	protected Hashtable<T, HierarchyTreeNode<T>> hash;
	
	public HierarchyTree () {
		this.root = null;
		this.hash = new Hashtable<T, HierarchyTreeNode<T>>();
	}
	
	public HierarchyTree (T obj) {
		this.root = new HierarchyTreeNode<T>(obj);
	}
	
	public HierarchyTreeNode<T> getRoot() {
		return this.root;
	}
	
	public HierarchyTreeNode<T> findNode(T obj) {
		HierarchyTreeNode<T> ret = null;
		
		if (this.hash.containsKey(obj)) {
			ret = this.hash.get(obj);
		}
		return ret;
	}
	
	// For future use to strip URIs
	/**
	 *
	 */
	@SuppressWarnings("all") public HierarchyTreeNode<T> stripData(HierarchyTreeNode<T> obj) { 
		if (obj.getData().getClass().equals(String.class)) {
			String s = (String) obj.getData();
			s = s.substring(s.indexOf("#") + 1);
			s = s.replace('_', ' ');
			obj.setShortName((T)s);
		}
		return obj; 
	}
	
	public void addSubClassLink(T parent, T child) {
		HierarchyTreeNode<T> parentNode = this.findNode(parent);
		HierarchyTreeNode<T> childNode = this.findNode(child);
		
		if (parentNode == null) {
			parentNode = new HierarchyTreeNode<T>(parent);
			parentNode = this.stripData(parentNode);
			this.hash.put(parent, parentNode);
		}
		if (childNode == null) {
			childNode = new HierarchyTreeNode<T>(child);
			childNode = this.stripData(childNode);
			this.hash.put(child, childNode);
		}
		parentNode.addChild(childNode);
		childNode.setHasParent(true);
	}
	
	public void addSubClassLink(T parent, T parentName, T child, T childName) {
		HierarchyTreeNode<T> parentNode = this.findNode(parent);
		HierarchyTreeNode<T> childNode = this.findNode(child);
		
		if (parentNode == null) {
			parentNode = new HierarchyTreeNode<T>(parent);
			parentNode = this.stripData(parentNode);
			parentNode.setShortName(parentName);
			this.hash.put(parent, parentNode);
		}
		if (childNode == null) {
			childNode = new HierarchyTreeNode<T>(child);
			childNode = this.stripData(childNode);
			childNode.setShortName(childName);
			this.hash.put(child, childNode);
		}
		parentNode.addChild(childNode);
		childNode.setHasParent(true);
	}
	
	public void prettyPrint(PrintStream printer) {
		ArrayList<HierarchyTreeNode<T>> children;
		if (this.root != null) {
			printer.println("Root");
		    children = this.root.getChildren();
			for (HierarchyTreeNode<T> node : children) {
				prettyPrintHelper(printer, "  |--", node);
			}
		}
	}
	
	public void prettyPrintHelper(PrintStream printer, String offset, HierarchyTreeNode<T> node) {
		
		ArrayList<HierarchyTreeNode<T>> children = node.getChildren();
		printer.println(offset + ((node.shortName != null) ? node.shortName : node.data));
		for (HierarchyTreeNode<T> curnode : children) {
			prettyPrintHelper(printer, offset + "--", curnode);
		}
	}
	
	public void processTree(T rootName) {
		Set<T> keys = this.hash.keySet();
		
		if (this.root == null) {
			this.root = new HierarchyTreeNode<T>(null);
			this.root.setShortName(rootName);
		}
		for (T obj : keys) {
			//System.out.println(obj);
			HierarchyTreeNode<T> node = this.hash.get(obj);
			if (!node.getHasParent()) {
				this.getRoot().addChild(node);
			}
		}
	}
	
	public String toJSON() {
		StringBuilder jsonText = new StringBuilder();
		Iterator<HierarchyTreeNode<T>> childIterator; // Kludgey, but no alternative exists 
												   //to know if we're at the last node.
		int level = 1;
		jsonText.append("{\n");
		jsonText.append("\t\"name\": \"Root\",\n");
		
		childIterator = this.getRoot().getChildren().iterator();
		
		if (childIterator.hasNext()) {
			jsonText.append("\t\"uri\": \"\",\n");
			jsonText.append("\t\"children\": [\n");
			level++;
			while (childIterator.hasNext()) {
				HierarchyTreeNode<T> node = childIterator.next();
				if (childIterator.hasNext()) {
					jsonText.append(jsonHelper(node, level) + ",\n");
				}
				else {
					jsonText.append(jsonHelper(node, level) + "\n");
				}
			}
		
			jsonText.append("\t]\n");
		}
		else {
			jsonText.append("\t\"uri\": \"\"\n");
		}
		jsonText.append("}\n");
		return jsonText.toString();
	}
	
	@SuppressWarnings("all") protected String jsonHelper(HierarchyTreeNode node, int level) {
		StringBuilder jsonText = new StringBuilder();
		Iterator<HierarchyTreeNode<T>> childIterator = null;
		String tab = "";
		int tabLevel = (level - 1) * 2;
		
		// Build tab level
		for (int i = 0; i < tabLevel; i++) {
			tab = tab + "\t";
		}

		jsonText.append(tab + "{\n");
		
		// Meat of it
		jsonText.append(tab + "\t" + "\"name\": \"" + node.getShortName() + "\",\n");
		
		if (node.getChildren() != null) {
			childIterator = node.getChildren().iterator();
			level++;
			
			if (childIterator.hasNext()) {
				jsonText.append(tab + "\t" + "\"uri\": \"" + node.getData() + "\",\n");
				jsonText.append(tab + "\t" + "\"children\": [\n");
				while (childIterator.hasNext()) {
					HierarchyTreeNode<T> cNode = childIterator.next();
					if (childIterator.hasNext()) {
						jsonText.append(jsonHelper(cNode, level) + ",\n");
					}
					else {
						jsonText.append(jsonHelper(cNode, level) + "\n");
					}
				}
				jsonText.append(tab + "\t" + "]\n");
			}
			else {
				jsonText.append(tab + "\t" + "\"uri\": \"" + node.getData() + "\"\n");
			}
		}
		else {
			jsonText.append(tab + "\t" + "\"uri\": \"" + node.getData() + "\"\n");
		}
		jsonText.append(tab + "}");
		return jsonText.toString();
	}
}
