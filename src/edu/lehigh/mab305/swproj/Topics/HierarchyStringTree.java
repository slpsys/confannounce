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

public class HierarchyStringTree {
	
	protected HierarchyStringTreeNode root;
	protected Hashtable<String, HierarchyStringTreeNode> hash;
	
	public HierarchyStringTree () {
		this.root = null;
		this.hash = new Hashtable<String, HierarchyStringTreeNode>();
	}
	
	public HierarchyStringTree (String obj) {
		this.root = new HierarchyStringTreeNode(obj);
	}
	
	public HierarchyStringTreeNode getRoot() {
		return this.root;
	}
	
	public HierarchyStringTreeNode findNode(String obj) {
		HierarchyStringTreeNode ret = null;
		
		if (this.hash.containsKey(obj)) {
			ret = this.hash.get(obj);
		}
		return ret;
	}
	
	// For future use to strip URIs
	public HierarchyStringTreeNode stripData(HierarchyStringTreeNode obj) { 
		if (obj.getData().getClass().equals(String.class)) {
			String s = (String) obj.getData();
			s = s.substring(s.indexOf("#") + 1);
			s = s.replace('_', ' ');
			obj.setShortName(s);
		}
		return obj; 
	}
	
	public void addSingletonNode(String singleton) {
		HierarchyStringTreeNode node = this.findNode(singleton);
		
		if (node == null) {
			node = new HierarchyStringTreeNode(singleton);
			node = this.stripData(node);
			this.hash.put(singleton, node);
		}
	}
	
	public void addSingletonNode(String singleton, String label) {
		HierarchyStringTreeNode node = this.findNode(singleton);
		
		if (node == null) {
			node = new HierarchyStringTreeNode(singleton);
			node = this.stripData(node);
			node.setShortName(label);
			this.hash.put(singleton, node);
		}
	}
	
	public void addSubClassLink(String parent, String child) {
		HierarchyStringTreeNode parentNode = this.findNode(parent);
		HierarchyStringTreeNode childNode = this.findNode(child);
		
		if (parentNode == null) {
			parentNode = new HierarchyStringTreeNode(parent);
			parentNode = this.stripData(parentNode);
			this.hash.put(parent, parentNode);
		}
		if (childNode == null) {
			childNode = new HierarchyStringTreeNode(child);
			childNode = this.stripData(childNode);
			this.hash.put(child, childNode);
		}
		parentNode.addChild(childNode);
		childNode.setHasParent(true);
	}
	
	public void addSubClassLink(String parent, String parentName, String child, String childName) {
		HierarchyStringTreeNode parentNode = this.findNode(parent);
		HierarchyStringTreeNode childNode = this.findNode(child);
		
		if (parentNode == null) {
			parentNode = new HierarchyStringTreeNode(parent);
			parentNode = this.stripData(parentNode);
			parentNode.setShortName(parentName);
			this.hash.put(parent, parentNode);
		}
		if (childNode == null) {
			childNode = new HierarchyStringTreeNode(child);
			childNode = this.stripData(childNode);
			childNode.setShortName(childName);
			this.hash.put(child, childNode);
		}
		parentNode.addChild(childNode);
		childNode.setHasParent(true);
	}
	
	public void prettyPrint(PrintStream printer) {
		ArrayList<HierarchyStringTreeNode> children;
		if (this.root != null) {
			printer.println("Root");
		    children = this.root.getChildren();
			for (HierarchyStringTreeNode node : children) {
				prettyPrintHelper(printer, "  |--", node);
			}
		}
	}
	
	public void prettyPrintHelper(PrintStream printer, String offset, HierarchyStringTreeNode node) {
		
		ArrayList<HierarchyStringTreeNode> children = node.getChildren();
		printer.println(offset + ((node.shortName != null) ? node.shortName : node.data));
		for (HierarchyStringTreeNode curnode : children) {
			prettyPrintHelper(printer, offset + "--", curnode);
		}
	}
	
	public void processTree() {
		Set<String> keys = this.hash.keySet();
		
		if (this.root == null) {
			this.root = new HierarchyStringTreeNode(null);
			this.root.setShortName("Root");
		}
		for (String obj : keys) {
			//System.out.println(obj);
			HierarchyStringTreeNode node = this.hash.get(obj);
			if (!node.getHasParent()) {
				this.getRoot().addChild(node);
			}
		}
	}
	
	public String toJSON() {
		StringBuilder jsonText = new StringBuilder();
		Iterator<HierarchyStringTreeNode> childIterator; // Kludgey, but no alternative exists 
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
				HierarchyStringTreeNode node = childIterator.next();
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
	
	protected String jsonHelper(HierarchyStringTreeNode node, int level) {
		StringBuilder jsonText = new StringBuilder();
		Iterator<HierarchyStringTreeNode> childIterator = null;
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
					HierarchyStringTreeNode cNode = childIterator.next();
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
