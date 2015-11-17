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
package com.valarion.gameengine.events.rpgmaker.flow;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.Condition;
import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.events.rpgmaker.RPGMakerEvent;

public class Conditional extends FlowEventClass {
	protected FlowEventInterface active;
	
	protected FlowEventInterface ways[] = new RPGMakerEvent[2];
	
	protected LinkedList<Condition> conditions = new LinkedList<Condition>();
	
	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		if (active != null)
			active.update(container, delta, map);
	}
	
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if (active != null) {
			if (active.isWorking()) {
				active.paralelupdate(container, delta, map);
			}
			else {
				restart();
			}
		}
	}
	
	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.prerender(container, g, tilewidth, tileheight);
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.render(container, g, tilewidth, tileheight);
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.postrender(container, g, tilewidth, tileheight);
	}
	
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);

		NodeList childs = node.getChildNodes();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if (n instanceof Element) {
				if("conditions".equals(n.getNodeName())) {
					loadConditions((Element)n,context);
				}
				else if("true".equals(n.getNodeName())) {
					ways[0] = new RPGMakerEvent();
					ways[0].loadEvent((Element)n, this);
				}
				else if("false".equals(n.getNodeName())) {
					ways[1] = new RPGMakerEvent();
					ways[1].loadEvent((Element)n, this);
				}
			}
		}
	}
	
	protected void loadConditions(Element node, Object context) throws SlickException {
		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null)) {
				try {
					Condition e = (Condition) game.getSets().get(Condition.class)
							.get(n.getNodeName()).newInstance();
					e.load((Element) n, null);
					conditions.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public boolean isWorking() {
		return active != null;
	}
	
	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		boolean result = true;
		
		for(Condition c : conditions) {
			result = result && c.eval(getEvent());
		}
		
		if(result) {
			active = ways[0];
		}
		else {
			active = ways[1];
		}
		
		if(active != null) {
			active.performAction(container, map, e);
		}
	}
	
	@Override
	public void restart() {
		if (active != null)
			active.restart();
		active = null;
	}
	
	@Override
	protected void searchAndActiveLabel(String label) {
		for (FlowEventInterface events : ways) {
			if (events != null && events.hasLabel(label)) {
				active = events;
				active.goToLabel(label);
			}
		}
	}
}
