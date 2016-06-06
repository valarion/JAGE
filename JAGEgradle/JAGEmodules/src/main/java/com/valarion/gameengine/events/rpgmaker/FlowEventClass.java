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
package com.valarion.gameengine.events.rpgmaker;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.Route;
import com.valarion.gameengine.gamestates.InGameState;

/**
 * Class that defines most of the methods used by an event to control the flow.
 * @author Rubén Tomás Gracia
 *
 */
public abstract class FlowEventClass extends SubEventClass implements
		FlowEventInterface {
	protected FlowEventInterface parent;
	
	protected InGameState state;

	protected Event activator;

	protected Map<String, FlowEventInterface> labels = new HashMap<String, FlowEventInterface>();

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		if (context instanceof FlowEventInterface)
			parent = (FlowEventInterface) context;
		else if(context instanceof InGameState)
			state = (InGameState) context;
	}

	@Override
	public String getId() {
		if (parent != null)
			return parent.getId();
		return null;
	}

	@Override
	public void breakCicle() {
		if (parent != null)
			parent.breakCicle();

		restart();
	}

	@Override
	public void stop() {
		if (parent != null)
			parent.stop();

		restart();
	}

	public void restart() {
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
	}

	@Override
	public boolean isWorking() {
		return false;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		activator = e;
	}

	@Override
	public void addLabel(String label, FlowEventInterface child) {
		labels.put(label, child);
		if (parent != null)
			parent.addLabel(label, this);
	}

	@Override
	public void goToLabel(String label) {
		if (hasLabel(label)) {
			searchAndActiveLabel(label);
		} else if (parent != null) {
			parent.goToLabel(label);
		}
	}

	/**
	 * Search for a label and activate it.
	 * @param label
	 */
	protected void searchAndActiveLabel(String label) {

	}

	@Override
	public boolean hasLabel(String label) {
		return labels.containsKey(label);
	}
	
	@Override
	public String getLayerName() {
		if (parent != null) {
			return parent.getLayerName();
		}
		else {
			return null;
		}
	}

	@Override
	public float getXDraw(int tileWidth) {
		if (parent != null) {
			return parent.getXDraw(tileWidth);
		}
		else {
			return -1;
		}
	}

	@Override
	public float getYDraw(int tileHeight) {
		if (parent != null) {
			return parent.getYDraw(tileHeight);
		}
		else {
			return -1;
		}
	}

	@Override
	public int getXPos() {
		if (parent != null) {
			return parent.getXPos();
		}
		else {
			return -1;
		}
	}

	@Override
	public int getYPos() {
		if (parent != null) {
			return parent.getYPos();
		}
		else {
			return -1;
		}
	}

	@Override
	public void setXPos(int newPos) {
		if (parent != null) {
			parent.setXPos(newPos);;
		}
	}

	@Override
	public void setYPos(int newPos) {
		if (parent != null) {
			parent.setYPos(newPos);
		}
	}

	@Override
	public int getDirection() {
		if (parent != null) {
			return parent.getDirection();
		}
		else {
			return -1;
		}
	}

	@Override
	public void setDirection(int direction) {
		if (parent != null) {
			parent.setDirection(direction);
		}
	}

	@Override
	public int getWidth() {
		if (parent != null) {
			return parent.getWidth();
		}
		else {
			return -1;
		}
	}

	@Override
	public int getHeight() {
		if (parent != null) {
			return parent.getHeight();
		}
		else {
			return -1;
		}
	}

	@Override
	public boolean isBlocking() {
		if (parent != null) {
			return parent.isBlocking();
		}
		else {
			return false;
		}
	}

	@Override
	public FlowEventInterface getParent(){
		return parent;
	}

	@Override
	public FlowEventInterface getEvent() {
		if(parent != null) {
			return parent.getEvent();
		}
		else {
			return this;
		}
	}

	@Override
	public void setRoute(Route route) {
		if(parent != null) {
			parent.setRoute(route);
		}
	}

	@Override
	public Route getRoute() {
		if(parent != null) {
			return parent.getRoute();
		}
		else {
			return null;
		}
	}

	@Override
	public InGameState getState() {
		if(parent != null) {
			return parent.getState();
		}
		else {
			return state;
		}
	}
	
	@Override
	public void setBlocking(boolean blocking) {
		if(parent != null) {
			parent.setBlocking(blocking);
		}
	}
}
