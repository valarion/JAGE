/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Rub�n Tom�s Gracia
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
package com.valarion.gameengine.events.rpgmaker;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.Route;
import com.valarion.gameengine.gamestates.InGameState;

/**
 * Event that describes an actual game event.
 * This class has been designed to be the top-most parent of every event in the game.
 * @author Rub�n Tom�s Gracia
 *
 */
public class GameEvent implements FlowEventInterface {

	protected LinkedList<RPGMakerEvent> pages = new LinkedList<RPGMakerEvent>();
	protected RPGMakerEvent active = null;
	protected String id;
	
	protected SubTiledMap map;
	protected InGameState state;

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		if (active != null) {
			active.update(container, delta, map);
		}
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		setActive(container,map);
		
		if (active != null) {
			active.paralelupdate(container, delta, map);
		}
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null) {
			active.prerender(container, g, tilewidth, tileheight);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null) {
			active.render(container, g, tilewidth, tileheight);
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null) {
			active.postrender(container, g, tilewidth, tileheight);
		}
	}

	@Override
	public String getLayerName() {
		if (active != null) {
			return active.getLayerName();
		} else {
			return null;
		}
	}

	@Override
	public float getXDraw(int tileWidth) {
		if (active != null) {
			return active.getXDraw(tileWidth);
		} else {
			return -1;
		}
	}

	@Override
	public float getYDraw(int tileHeight) {
		if (active != null) {
			return active.getYDraw(tileHeight);
		} else {
			return -1;
		}
	}

	@Override
	public int getXPos() {
		if(active != null){
			return active.getXPos();
		}
		else {
			return -1;
		}
	}

	@Override
	public int getYPos() {
		if(active != null){
			return active.getYPos();
		}
		else {
			return -1;
		}
	}

	@Override
	public void setXPos(int newPos) {
		if(active != null){
			active.setXPos(newPos);
		}
	}

	@Override
	public void setYPos(int newPos) {
		if(active != null){
			active.setYPos(newPos);
		}
	}

	@Override
	public int getDirection() {
		if (active != null) {
			return active.getDirection();
		} else {
			return -1;
		}
	}

	@Override
	public void setDirection(int direction) {
		if (active != null) {
			active.setDirection(direction);
		}
	}

	@Override
	public int getWidth() {
		if (active != null) {
			return active.getWidth();
		} else {
			return -1;
		}
	}

	@Override
	public int getHeight() {
		if (active != null) {
			return active.getHeight();
		}
		else {
			return -1;
		}
	}

	@Override
	public boolean isBlocking() {
		if(active != null && active.isBlocking()) {
			return "player".equals(getLayerName());
		}
		else {
			return false;
		}
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map)
			throws SlickException {
		this.map = map;
		setActive(container,map);
	}

	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map)
			throws SlickException {
		map.setMustupdate(true);
	}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map)
			throws SlickException {
		for(RPGMakerEvent page : pages) {
			if(page.isWorking()){
				map.setMustupdate(false);
			}
			page.onMapSetAsActive(container, map);
		}
		
		/*if(active != null) {
			if(active.isWorking()){
				map.setMustupdate(false);
			}
			active.onMapSetAsActive(container, map);
		}*/
	}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map,
			Event e) throws SlickException {
		if (active!=null) {
			active.onEventActivation(container, map, e);
		}
	}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (active!=null) {
			active.onEventTouch(container, map, e);;
		}
	}

	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (active!=null) {
			active.onBeingTouched(container, map, e);;
		}
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		id = node.getAttribute("id");

		if ("".equals(id))
			id = null;

		if(context instanceof InGameState) {
			state = (InGameState) context;
		}
		
		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null) && "page".equals(((Element)n).getTagName())) {
				RPGMakerEvent event = new RPGMakerEvent();
				event.loadEvent((Element) (n), this);
				pages.addFirst(event);
			}
		}
		
		/*
		NodeList pagelist = node.getElementsByTagName("page");

		for (int i = 0; i < pagelist.getLength(); i++) {
			RPGMakerEvent event = new RPGMakerEvent();
			event.loadEvent((Element) (pagelist.item(i)), this);
			pages.addFirst(event);
		}*/
	}

	@Override
	public boolean isWorking() {
		if (active!=null) {
			return active.isWorking();
		}
		else {
			return false;
		}
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (active != null) {
			active.performAction(container, map, e);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void breakCicle() {

	}

	@Override
	public void stop() {
		restart();
	}

	@Override
	public void restart() {
		if (active != null)
			active.restart();
	}

	@Override
	public void addLabel(String label, FlowEventInterface child) {

	}

	@Override
	public void goToLabel(String label) {

	}

	@Override
	public boolean hasLabel(String label) {
		return false;
	}

	/**
	 * Set this event active page.
	 * @throws SlickException 
	 */
	protected void setActive(GameContainer container, SubTiledMap map) throws SlickException {
		if (active == null || !active.isWorking()) {
			int x = -1, y = -1, dir = 0;
			if(active != null) {
				x = active.getXPos();
				y = active.getYPos();
				dir = active.getDirection();
			}
			map.setMustupdate(true);
			if(active != null) {
				map.getEvents(active.getXPos(), active.getYPos()).remove(getEvent());
			}
			boolean found = false;
			for (RPGMakerEvent page : pages) {
				if (page.isActive(this, container, map)) {
					active = page;
					active.onGetActive(container, map);
					found = true;
					if(active.isInheritX())
						active.setXPos(x);
					if(active.isInheritY())
						active.setYPos(y);
					if(active.isInheritDirection())
						active.setDirection(dir);
					map.add(this);
					//map.getEvents(active.getXPos(), active.getYPos()).add(getEvent());
					break;
				}
			}
			if(!found) {
				active = null;
			}
		}
	}

	@Override
	public FlowEventInterface getParent() {
		return null;
	}

	@Override
	public FlowEventInterface getEvent() {
		return this;
	}

	@Override
	public void setRoute(Route route) {
		if(active != null)
			active.setRoute(route);
	}

	@Override
	public Route getRoute() {
		if(active != null) {
			return active.getRoute();
		}
		else {
			return null;
		}
	}

	@Override
	public InGameState getState() {
		return state;
	}

	@Override
	public void setBlocking(boolean blocking) {
		if(active != null) {
			active.setBlocking(blocking);
		}
	}
	
	public RPGMakerEvent getActive() {
		return active;
	}
}
