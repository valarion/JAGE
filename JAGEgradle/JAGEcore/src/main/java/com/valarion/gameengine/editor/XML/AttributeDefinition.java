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
 * Class containing an XML attribute definition.
 * @author Rubén Tomás Gracia
 *
 */
public class AttributeDefinition {
	public static AttributeDefinition CDATA = new AttributeDefinition("CDATA",String.class);
	
	/**
	 * Create attribute with name and type.
	 * @param name
	 * @param type
	 */
	public AttributeDefinition(String name, Class<?> type) {
		this.name = name;
		this.type = type;
		this.constraint = null;
		this.defaultvalue = null;
	}

	/**
	 * Create attribute with name, type and constraint.
	 * @param name
	 * @param type
	 * @param constraint
	 */
	public AttributeDefinition(String name, Class<?> type, String constraint) {
		this.name = name;
		this.type = type;
		this.constraint = constraint;
		this.defaultvalue = null;
	}
	
	/**
	 * Create attribute with name and default value. Type will be value class.
	 * @param name
	 * @param defaultvalue
	 */
	public AttributeDefinition(String name, Object defaultvalue) {
		this.name = name;
		this.type = defaultvalue.getClass();
		this.constraint = null;
		this.defaultvalue = defaultvalue;
	}

	/**
	 * Create attribute with name, default value and constraint. Type will be value class.
	 * @param name
	 * @param defaultvalue
	 */
	public AttributeDefinition(String name, Object defaultvalue, String constraint) {
		this.name = name;
		this.type = defaultvalue.getClass();
		this.constraint = constraint;
		this.defaultvalue = defaultvalue;
	}

	protected String name;
	protected Class<?> type;
	protected Object defaultvalue;
	protected String constraint;

	/**
	 * Get name.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get type
	 * @return
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Get default value.
	 * @return
	 */
	public Object getDefaultValue() {
		return defaultvalue;
	}

	/**
	 * Get constrait.
	 * @return
	 */
	public String getConstraint() {
		return constraint;
	}
	
	@Override
	public String toString() {
		return name + " - " + type.getSimpleName() + (defaultvalue != null ? "(" + defaultvalue.toString() + ")" : "")
				+ (constraint != null ? " - " + constraint : "");
	}
	
	/**
	 * Get attribute implementation.
	 * @return
	 */
	public AttributeImplementation getImplementation() {
		return new AttributeImplementation(this);
	}
	
	/**
	 * Get attribute implementation with object.
	 * @param value
	 * @return
	 */
	public AttributeImplementation getImplementation(Object value) {
		return new AttributeImplementation(this).setValue(value);
	}
}