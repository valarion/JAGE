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
package com.valarion.gameengine.strings;

import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.interfaces.ColoredString;
import com.valarion.gameengine.core.interfaces.VarLong;

/**
 * Wrapper class for a long value of the class VarLong.
 * @author Rubén Tomás Gracia
 *
 */
public class NumberText implements ColoredString {
	String text;
	Color color;

	VarLong number;

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public String getString() {
		if(number != null)
			text = Long.toString((long) number.getLong());
		else
			text = "";
		return text;
	}

	@Override
	public int length() {
		return text.length();
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		try {
			color = new Color(Integer.parseInt(node.getAttribute("r")),
					Integer.parseInt(node.getAttribute("g")),
					Integer.parseInt(node.getAttribute("b")));
		} catch (Exception e) {
			color = Color.black;
		}
		Node n = node.getFirstChild();
		Map<String, Class<?>> map = GameCore.getInstance().getSets().get(VarLong.class);
		Class<?> c = map.get(node.getNodeName());
		VarLong e;
		try {
			e = (VarLong) c.newInstance();
			e.load((Element) n, context);
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
	}

}
