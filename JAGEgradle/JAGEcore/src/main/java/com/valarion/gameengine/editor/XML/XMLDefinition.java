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

import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class containing a custom XML node definition
 * @author Rubén Tomás Gracia
 *
 */
public class XMLDefinition {
	
	/**
	 * Create definition of a concrete type with multiplicity [0,n].
	 * @param type
	 */
	public XMLDefinition(Class<?> type) {
		this.name = type.getSimpleName();
		this.min = 0;
		this.max = Integer.MAX_VALUE;
		
		this.parent = null;
		this.superclass = type;

		this.attributes = new LinkedList<AttributeDefinition>();
		this.sharednodes = new LinkedList<NodeDefinition>();
		this.customnodes = new LinkedList<XMLDefinition>();
	}
	
	/**
	 * Create definition of a concrete type with a defined multiplicity.
	 * @param type
	 */
	public XMLDefinition(Class<?> type, int multiplicity) {
		this.name = type.getSimpleName();
		this.min = multiplicity;
		this.max = multiplicity;
		
		this.parent = null;
		this.superclass = type;

		this.attributes = new LinkedList<AttributeDefinition>();
		this.sharednodes = new LinkedList<NodeDefinition>();
		this.customnodes = new LinkedList<XMLDefinition>();
	}
	
	/**
	 * Create definition of a concrete type with multiplicity [min,max].
	 * @param type
	 */
	public XMLDefinition(Class<?> type, int min, int max) {
		this.name = type.getSimpleName();
		this.min = min;
		this.max = max;
		
		this.parent = null;
		this.superclass = type;

		this.attributes = new LinkedList<AttributeDefinition>();
		this.sharednodes = new LinkedList<NodeDefinition>();
		this.customnodes = new LinkedList<XMLDefinition>();
	}
	
	/**
	 * Create definition of a concrete name with multiplicity [0,n].
	 * @param type
	 */
	public XMLDefinition(String name) {
		this.name = name;
		this.min = 0;
		this.max = Integer.MAX_VALUE;
		
		this.parent = null;
		this.superclass = null;

		this.attributes = new LinkedList<AttributeDefinition>();
		this.sharednodes = new LinkedList<NodeDefinition>();
		this.customnodes = new LinkedList<XMLDefinition>();
	}

	/**
	 * Create definition of a concrete name with a defined multiplicity.
	 * @param type
	 */
	public XMLDefinition(String name, int multiplicity) {
		this.name = name;
		this.min = multiplicity;
		this.max = multiplicity;
		
		this.parent = null;
		this.superclass = null;

		this.attributes = new LinkedList<AttributeDefinition>();
		this.sharednodes = new LinkedList<NodeDefinition>();
		this.customnodes = new LinkedList<XMLDefinition>();
	}
	
	/**
	 * Create definition of a concrete name with multiplicity [min,max].
	 * @param type
	 */
	public XMLDefinition(String name, int min, int max) {
		this.name = name;
		this.min = min;
		this.max = max;
		
		this.parent = null;
		this.superclass = null;

		this.attributes = new LinkedList<AttributeDefinition>();
		this.sharednodes = new LinkedList<NodeDefinition>();
		this.customnodes = new LinkedList<XMLDefinition>();
	}

	protected String name;
	protected int min, max;

	protected LinkedList<AttributeDefinition> attributes;
	protected LinkedList<NodeDefinition> sharednodes;
	protected LinkedList<XMLDefinition> customnodes;
	
	protected XMLDefinition parent;
	protected Class<?> superclass;
	
	/**
	 * Get attribute by index.
	 * @param index
	 * @return
	 */
	public AttributeDefinition getAttribute(int index) {
		return attributes.get(index);
	}
	
	/**
	 * Get shared node by index.
	 * @param index
	 * @return
	 */
	public NodeDefinition getSharedNode(int index) {
		return sharednodes.get(index);
	}
	
	/**
	 * Get custom node by index.
	 * @param index
	 * @return
	 */
	public XMLDefinition getCustomNode(int index) {
		return customnodes.get(index);
	}
	
	/**
	 * Get attribute count.
	 * @return
	 */
	public int getAttributeCount() {
		return attributes.size();
	}
	
	/**
	 * Get shared node count
	 * @return
	 */
	public int getSharedNodesCount() {
		return sharednodes.size();
	}
	
	/**
	 * Get custom node count.
	 * @return
	 */
	public int getCustomNodesCount() {
		return customnodes.size();
	}

	/**
	 * Get name.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get minimum multiplicity.
	 * @return
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Get maximum multiplicity.
	 * @return
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Add attribute.
	 * @param attribute
	 */
	public XMLDefinition addAttribute(AttributeDefinition attribute) {
		attributes.add(attribute);
		return this;
	}

	/**
	 * Add shared node.
	 * @param node
	 */
	public XMLDefinition addSharedNode(NodeDefinition node) {
		sharednodes.add(node);
		return this;
	}

	/**
	 * Add custom node.
	 * @param node
	 */
	public XMLDefinition addCustomNode(XMLDefinition node) {
		if(node.parent != null) {
			node.parent.customnodes.remove(node);
		}
		node.parent = this;
		customnodes.add(node);
		return this;
	}
	
	@Override
	public String toString() {
		if(parent == null) {
			return name;
		}
		else {
			return name + " - [" + min + (min != max ? "," + max : "") + "]";
		}
	}
	
	/**
	 * Get node implementation.
	 * @return
	 */
	public XMLImplementation getImplementation() {
		return new XMLImplementation(this);
	}

	/**
	 * Get node definition tree.
	 * @return
	 */
	public DefaultMutableTreeNode createDefinitionTree() {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);

		for (AttributeDefinition attr : attributes) {
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(attr, false);
			root.add(leaf);
		}

		for (NodeDefinition n : sharednodes) {
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(n);
			root.add(leaf);
		}

		for (XMLDefinition n : customnodes) {
			DefaultMutableTreeNode treenode = n.createDefinitionTree();
			root.add(treenode);
		}

		return root;
	}
	
	/**
	 * Get node implementation tree.
	 * @return
	 */
	public DefaultMutableTreeNode createImplementationTree() {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getImplementation());

		for (AttributeDefinition attr : attributes) {
			DefaultMutableTreeNode leaf;
			if(attr.getDefaultValue() != null) {
				leaf = new DefaultMutableTreeNode(attr.getImplementation(attr.getDefaultValue()), false);
			}
			else {
				leaf = new DefaultMutableTreeNode(attr.getImplementation(), false);
			}
			root.add(leaf);
		}

		for (NodeDefinition n : sharednodes) {
			for(int i=0; i<n.getMin(); i++) {
				DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(n.getImplementation());
				root.add(leaf);
			}
		}

		for (XMLDefinition n : customnodes) {
			for(int i=0; i<n.getMin(); i++) {
				DefaultMutableTreeNode treenode = n.createImplementationTree();
				root.add(treenode);
			}
		}

		return root;
	}

	/**
	 * Set node superclass.
	 * @param type
	 */
	public void setSuperClass(Class<?> type) {
		this.superclass = type;
	}
	
	/**
	 * Get node superclass.
	 * @return
	 */
	public Class<?> getSuperClass(){
		return superclass;
	}
}
