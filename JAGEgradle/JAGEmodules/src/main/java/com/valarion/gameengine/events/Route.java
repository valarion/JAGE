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
package com.valarion.gameengine.events;

import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class that saves a moving route.
 * @author Rubén Tomás Gracia
 *
 */
public class Route {
	protected LinkedList<Integer> route = new LinkedList<Integer>();
	
	protected boolean mustignore = false;
	protected boolean loop = true;
	protected boolean canactivate = true;
	protected boolean wait = true;
	
	/**
	 * Loads the route form an XML node.
	 * @param node XML node.
	 * @param canactivate Whether this object can activate other events "on touch".
	 */
	public Route(Element node, boolean canactivate) {
		this.canactivate = canactivate;
		if(node == null)
			return;
		
		NodeList childs = node.getChildNodes();
		
		mustignore = Boolean.parseBoolean(node.getAttribute("ignore"));
		loop = Boolean.parseBoolean(node.getAttribute("loop"));
		wait = Boolean.parseBoolean(node.getAttribute("wait"));
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if (n instanceof Element) {
				Element e = (Element)n;
				
				try {
					route.add((Integer) Moving.class.getDeclaredField(e.getNodeName().toUpperCase()).get(null));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				/*if("lookup".equals(e.getNodeName())) {
					route.add(Moving.LOOKUP);
				}
				else if("lookdown".equals(e.getNodeName())) {
					route.add(Moving.LOOKDOWN);
				}
				else if("lookleft".equals(e.getNodeName())) {
					route.add(Moving.LOOKLEFT);
				}
				else if("lookright".equals(e.getNodeName())) {
					route.add(Moving.LOOKRIGHT);
				}
				else if("moveup".equals(e.getNodeName())) {
					route.add(Moving.MOVEUP);
				}
				else if("movedown".equals(e.getNodeName())) {
					route.add(Moving.MOVEDOWN);
				}
				else if("moveleft".equals(e.getNodeName())) {
					route.add(Moving.MOVELEFT);
				}
				else if("moveright".equals(e.getNodeName())) {
					route.add(Moving.MOVERIGHT);
				}*/
			}
		}
	}
	
	/**
	 * Gets whether to ignore impossible movements or not.
	 * @return
	 */
	public boolean isMustignore() {
		return mustignore;
	}
	
	/**
	 * Gets whether to loop the movement or not.
	 * @return
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * Gets whether this object can activate other events "on touch".
	 * @return
	 */
	public boolean isCanactivate() {
		return canactivate;
	}

	/**
	 * Returns whether the event must wait to this route to end before continuing.
	 * @return
	 */
	public boolean isWait() {
		return wait;
	}

	/**
	 * Get the route.
	 * @return
	 */
	public LinkedList<Integer> getRoute() {
		return new LinkedList<Integer>(route);
	}

}
