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

/**
 * Class that defines a shared node.
 * @author Rubén Tomás Gracia
 *
 */
public class NodeDefinition {
	/**
	 * Create share node of a concrete type with a multiplicity of [0,n].
	 * @param type
	 */
	public NodeDefinition(Class<?> type) {
		this.type = type;
		this.min = 0;
		this.max = Integer.MAX_VALUE;
	}

	/**
	 * Create share node of a concrete type with a defined multiplicity.
	 * @param type
	 * @param multiplicity
	 */
	public NodeDefinition(Class<?> type, int multiplicity) {
		this.type = type;
		this.min = multiplicity;
		this.max = multiplicity;
	}
	
	/**
	 * Create share node of a concrete type with a multiplicity of [min,max].
	 * @param type
	 */
	public NodeDefinition(Class<?> type, int min, int max) {
		this.type = type;
		this.min = min;
		this.max = max;
	}

	protected Class<?> type;

	protected int min, max;

	/**
	 * Get type.
	 * @return
	 */
	public Class<?> getType() {
		return type;
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
	
	@Override
	public String toString() {
		return type.getName() + " - [" + min + (min != max ? "," + (max != Integer.MAX_VALUE ? max : "n") : "")
				+ "]";
	}
	
	/**
	 * Get a wrapper implementation.
	 * @return
	 */
	public EmptyNodeImplementation getImplementation() {
		return new EmptyNodeImplementation(this);
	}
}