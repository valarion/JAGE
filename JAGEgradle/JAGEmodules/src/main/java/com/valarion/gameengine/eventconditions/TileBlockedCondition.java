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
package com.valarion.gameengine.eventconditions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Database;

/**
 * Condition of a specific position in hte map being blocked.
 * @author Rubén Tomás Gracia
 *
 */
public class TileBlockedCondition implements Condition{
	protected int xvar, yvar;
	protected int x, y;

	@Override
	public boolean eval(Event e, GameContainer container, SubTiledMap map) {
		int xdir = (x!=-1?x:(int)Database.instance().getContext().getGlobalVars()[xvar]);
		int ydir = (y!=-1?y:(int)Database.instance().getContext().getGlobalVars()[yvar]);
		return map.isBlocked(xdir,ydir);
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		try {
			xvar = Integer.parseInt(node.getAttribute("xvar"));
		}catch(Exception e) {
			xvar = -1;
		}
		
		try {
			yvar = Integer.parseInt(node.getAttribute("yvar"));
		}catch(Exception e) {
			yvar = -1;
		}
		
		try {
			x = Integer.parseInt(node.getAttribute("x"));
		}catch(Exception e) {
			x = -1;
		}
		
		try {
			y = Integer.parseInt(node.getAttribute("y"));
		}catch(Exception e) {
			y = -1;
		}
		
		if((x == -1 && xvar == -1) || (y == -1 && yvar == -1)) {
			throw new IndexOutOfBoundsException("(x and xvar not set) or (y and yvar not set)");
		}
	}

}
