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

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import java.util.HashMap;
import java.util.ArrayList;

public class DataList<T> {

	protected HashMap<String, T> itemHash = null;
	protected List _list = null;
	
	public DataList(org.eclipse.swt.widgets.Composite parent, int style) {
		_list = new List(parent, style);
		itemHash = new HashMap<String, T>();
	}
	
	// Overridden
	public void add(String string) {
		if (string != null && !itemHash.containsKey(string)) {
			itemHash.put(string, null);
			_list.add(string);
		}
	}
	
	// New version
	public void add(String string, T data) {
		if (!itemHash.containsKey(string)) {
			_list.add(string);
		}
		itemHash.put(string, data);
	}
	
	// Overridden
	public void add(String string, int index) {
		if (!itemHash.containsKey(string)) {
			_list.add(string, index);
		}
		itemHash.put(string, null);
	}
	
	// New version
	public void add(String string, int index, T data) {
		if (!itemHash.containsKey(string)) {
			_list.add(string, index);
		}
		itemHash.put(string, data);
	}
	
	public T getDataItem(String string) {
		return itemHash.get(string);
	}
	
	public void remove(int index) {
		itemHash.remove(_list.getItem(index));
		_list.remove(index);
	}
	
	public void remove(int indices[]) {
		for (int i = 0; i < indices.length; i++) {
			itemHash.remove(_list.getItem(indices[i]));
		}
		_list.remove(indices);
	}
	
	public void remove(int start, int end) {
		if (start > 0 && start <= end && end < _list.getItemCount()) {
			// Remove(int, int) is inclusive for List class.
			for (int i = start; i <= end; i++) {
				itemHash.remove(_list.getItem(i));
			}
			_list.remove(start, end);
		}
	}
	
	public void remove(String string) {
		if (_list.indexOf(string) >= 0) {
			itemHash.remove(_list.getItem(_list.indexOf(string)));
			_list.remove(string);
		}
	}
	
	public void removeByData(T data) {
		ArrayList<String> removeList = new ArrayList<String>();
		for (String key : itemHash.keySet()) {
			if (itemHash.get(key).equals(data)) {
				_list.remove(key);
				removeList.add(key);
			}
		}
		for (String key : removeList) {
			itemHash.remove(key);
		}
	}
	
	public void removeAll() {
		itemHash = new HashMap<String, T>();
		_list.removeAll();
	}
	
	public String[] getItems() {
		return _list.getItems();
	}
	
	public String[] getSelection() {
		return _list.getSelection();
	}
	
	public int[] getSelectionIndices() {
		return _list.getSelectionIndices();
	}
	
	public void setBounds(Rectangle rect) {
		_list.setBounds(rect);
	}
	
	public void addKeyListener(KeyListener listener) {
		_list.addKeyListener(listener);
	}
	
	public void setToolTipText(String text) {
		_list.setToolTipText(text);
	}
	
	public boolean equals(Object obj) {
		return _list.equals(obj);
	}
}
