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
package com.valarion.gameengine.events.rpgmaker.progress;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.VarLong;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.SubEventClass;
import com.valarion.gameengine.gamestates.Database;

/**
 * Operate with a variable/s and save the result in the same variable/s.
 * works like =, +=, -=, *=, /= and %=
 * @author Rubén Tomás Gracia
 *
 */
public class VarOperation extends SubEventClass {
	public static final byte SET = 0;
	public static final byte ADD = 1;
	public static final byte SUB = 2;
	public static final byte MUL = 3;
	public static final byte DIV = 4;
	public static final byte REM = 5;

	protected int from = 0, to = 0;
	protected byte operation = -1;
	protected VarLong value;

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {

	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		from = Integer.parseInt(node.getAttribute("var"));
		try {
			to = Integer.parseInt(node.getAttribute("to"));
		} catch (Exception e) {
			to = from;
		}

		String op = node.getAttribute("operation");

		if ("set".equals(op)) {
			operation = SET;
		} else if ("add".equals(op)) {
			operation = ADD;
		} else if ("sub".equals(op)) {
			operation = SUB;
		} else if ("mul".equals(op)) {
			operation = MUL;
		} else if ("div".equals(op)) {
			operation = DIV;
		} else if ("rem".equals(op)) {
			operation = REM;
		}

		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();

		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null)) {
				try {
					value = (VarLong) game.getSets().get(VarLong.class)
							.get(n.getNodeName()).newInstance();
					value.load((Element) n, null);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isWorking() {
		return false;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		long val = value.getLong();
		
		
		for (int i = from; i <= to; i++) {
			long var = Database.instance().getContext().getGlobalVars()[i];
			switch (operation) {
			case SET:
				var = val;
				break;
			case ADD:
				var += val;
				break;
			case SUB:
				var -= val;
				break;
			case MUL:
				var *= val;
				break;
			case DIV:
				var /= val;
				break;
			case REM:
				var %= val;
				break;
			}
			Database.instance().getContext().getGlobalVars()[i] = var;
		}
		
	}
}
