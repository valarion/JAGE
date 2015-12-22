/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Rubén Tomás Gracia
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package com.valarion.gameengine.editor.XML;

import java.util.HashMap;
import java.util.HashSet;

import javax.swing.tree.DefaultMutableTreeNode;
/**
 * Class that contains the implementation of an XML node.
 * @author Rubén Tomás Gracia
 *
 */
public class XMLImplementation {
	/**
	 * Create implementation from definition
	 * @param definition
	 */
	public XMLImplementation(XMLDefinition definition) {
		this.definition = definition;
	}
	
	protected XMLDefinition definition;
	
	
	protected HashSet<String> canaddchildset;
	
	protected HashMap<String,Integer> count;
	
	/**
	 * Get attribute definition by index.
	 * @param index
	 * @return
	 */
	public AttributeDefinition getAttribute(int index) {
		return definition.getAttribute(index);
	}
	
	/**
	 * Get shared node definition by index.
	 * @param index
	 * @return
	 */
	public NodeDefinition getSharedNode(int index) {
		return definition.getSharedNode(index);
	}
	
	/**
	 * Get custom node definition by index.
	 * @param index
	 * @return
	 */
	public XMLDefinition getCustomNode(int index) {
		return definition.getCustomNode(index);
	}
	
	/**
	 * Get attribute count.
	 * @return
	 */
	public int getAttributeCount() {
		return definition.getAttributeCount();
	}
	
	/**
	 * Get definition shared nodes count.
	 * @return
	 */
	public int getSharedNodesCount() {
		return definition.getSharedNodesCount();
	}
	
	/**
	 * Get definition custom nodes count.
	 * @return
	 */
	public int getCustomNodesCount() {
		return definition.getCustomNodesCount();
	}
	
	/**
	 * Get name.
	 * @return
	 */
	public String getName() {
		return definition.getName();
	}

	/**
	 * Get minimum multiplicity.
	 * @return
	 */
	public int getMin() {
		return definition.getMin();
	}

	/**
	 * Get maximum multiplicity.
	 * @return
	 */
	public int getMax() {
		return definition.getMax();
	}
	
	@Override
	public String toString() {
		return definition.getName();
	}

	/**
	 * Get superclass.
	 * @return
	 */
	public Class<?> getSuperClass() {
		return definition.getSuperClass();
	}
	
	/**
	 * Refresh children count from tree.
	 * @param parent
	 * @return
	 */
	public XMLImplementation refreshChildrenCount(DefaultMutableTreeNode parent) {
		count = new HashMap<String,Integer>();
		
		for(NodeDefinition node : definition.sharednodes) {
			count.put(node.getType().getSimpleName(), 0);
		}
		for(XMLDefinition node : definition.customnodes) {
			count.put(node.getName(), 0);
		}
		
		for(int i=0; i < parent.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
			Object uo = child.getUserObject();
			String key = null;
			if(uo instanceof XMLImplementation) {
				XMLImplementation implementation = (XMLImplementation) uo;
				if(implementation.getSuperClass() != null){
					key = implementation.getSuperClass().getSimpleName();
				}
				else {
					key = implementation.getName();
				}
			}
			else if(uo instanceof EmptyNodeImplementation) {
				key = ((EmptyNodeImplementation)uo).getType().getSimpleName();
			}
			if(key != null) {
				count.put(key,count.get(key)+1);
			}
		}
		
		return this;
	}

	/**
	 * Get whether or not this node can add more children.
	 * @return
	 */
	public boolean canAddChildren() {
		int canaddchilds = 0;
		
		canaddchildset = new HashSet<String>();
		
		for(NodeDefinition node : definition.sharednodes) {
			if(count.get(node.getType().getSimpleName()) < node.getMax()) {
				canaddchildset.add(node.getType().getSimpleName());
				canaddchilds++;
			}
		}
		for(XMLDefinition node : definition.customnodes) {
			if(count.get(node.getName()) < node.getMax()) {
				canaddchildset.add(node.getName());
				canaddchilds++;
			}
		}
		
		return canaddchilds > 0;
	}
	
	/**
	 * Check whether or not this node can add a child of a concrete type.
	 * @param child
	 * @return
	 */
	public boolean canAddChild(String child) {
		for(NodeDefinition node : definition.sharednodes) {
			if(node.getType().getSimpleName().equals(child)) {
				return count.get(child) < node.getMax();
			}
		}
		for(XMLDefinition node : definition.customnodes) {
			if(node.getName().equals(child)) {
				return count.get(child) < node.getMax();
			}
		}
		return false;
	}
	
	/**
	 * Check whether or not this node must add a child to get to the minimum multiplicity and
	 * return the new child or null if there's no  need to add a child.
	 * @param child
	 * @return
	 */
	public Object mustAddChild(String child) {
		for(NodeDefinition node : definition.sharednodes) {
			if(node.getType().getSimpleName().equals(child) && count.get(child) < node.getMin()) {
				return node.getImplementation();
			}
		}
		for(XMLDefinition node : definition.customnodes) {
			if(node.getName().equals(child) && count.get(child) < node.getMin()) {
				return node;
			}
		}
		return null;
	}
}