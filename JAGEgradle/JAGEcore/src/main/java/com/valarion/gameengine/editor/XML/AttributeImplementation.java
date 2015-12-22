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

import java.lang.reflect.InvocationTargetException;

/**
 * Class that contains an XML attribute.
 * @author Rubén Tomás Gracia
 *
 */
public class AttributeImplementation {
	/**
	 * Create attribute from definition.
	 * @param definition
	 */
	public AttributeImplementation(AttributeDefinition definition) {
		this.value = null;
		this.definition = definition;
	}
	
	protected Object value;
	
	protected AttributeDefinition definition;

	/**
	 * Get name.
	 * @return
	 */
	public String getName() {
		return definition.getName();
	}

	/**
	 * Get type.
	 * @return
	 */
	public Class<?> getType() {
		return definition.getType();
	}
	
	@Override
	public String toString() {
		return definition.getName() + " - " + (value != null ? value : "");
	}
	
	/**
	 * Set attribute value.
	 * The value must be subclass of the attribute type or the atrribute type
	 * have a constructor of the value class.
	 * @param value
	 * @return
	 */
	public AttributeImplementation setValue(Object value) {
		if(!definition.getType().isAssignableFrom(value.getClass())) {
			try {
				value = definition.getType().getDeclaredConstructor(value.getClass()).newInstance(value);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new ClassCastException(value.getClass().getName() + " can't be cast into " +definition.getType().getName());
			}
		}
		this.value = value;
		return this;
	}

	/**
	 * Get value.
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Get default value.
	 * @return
	 */
	public Object getDefaultValue() {
		return definition.getDefaultValue();
	}

	/**
	 * Get constraint.
	 * @return
	 */
	public Object getConstraint() {
		return definition.getConstraint();
	}
}