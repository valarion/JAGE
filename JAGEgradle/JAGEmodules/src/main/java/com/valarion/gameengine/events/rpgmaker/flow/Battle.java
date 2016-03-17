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

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.events.rpgmaker.RPGMakerEvent;
import com.valarion.gameengine.gamestates.BattleState;
import com.valarion.gameengine.gamestates.Database;

/**
 * Event that saves the position of an event in two variables.
 * @author Rubén Tomás Gracia
 *
 */
public class Battle extends FlowEventClass {
	int enemyhp = -1, playerhp = -1;
	Image enemy;
	
	protected FlowEventInterface active;
	
	protected FlowEventInterface ways[] = new RPGMakerEvent[2];
	
	protected BattleState battle;
	
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		try {
			enemyhp = Integer.parseInt(node.getAttribute("enemyhp"));
		}
		catch(Exception e) {}
		try {
			playerhp = Integer.parseInt(node.getAttribute("playerhp"));
		}
		catch(Exception e) {}
		
		enemy = Database.instance().getImages().get(node.getAttribute("image"));
		
		NodeList childs = node.getChildNodes();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if (n instanceof Element) {
				if("win".equals(n.getNodeName())) {
					ways[0] = new RPGMakerEvent();
					ways[0].loadEvent((Element)n, this);
				}
				else if("loose".equals(n.getNodeName())) {
					ways[1] = new RPGMakerEvent();
					ways[1].loadEvent((Element)n, this);
				}
			}
		}
	}
	
	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		if (active != null)
			active.update(container, delta, map);
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if(battle != null && active == null) {
			switch(battle.getResult()) {
			case win:
				active = ways[0];
				break;
			case loose:
				active = ways[1];
				break;
			default:
				break;
			}
			
			if(active != null) {
				active.performAction(container, map, activator);
			}
		}
		else {
			if (active != null) {
				if (active.isWorking()) {
					active.paralelupdate(container, delta, map);
				}
				else {
					restart();
				}
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
	public boolean isWorking() {
		return battle != null;
	}
	
	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		super.performAction(container, map, e);
		battle = new BattleState(GameCore.getInstance().getActive(),enemy,enemyhp,playerhp);
		GameCore.getInstance().setActive(battle);
	}
	
	@Override
	public void restart() {
		if (active != null)
			active.restart();
		active = null;
		battle = null;
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
