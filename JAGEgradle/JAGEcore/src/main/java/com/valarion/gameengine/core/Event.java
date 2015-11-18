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
package com.valarion.gameengine.core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

/**
 * Class that contains all the methods needed for an event based game.
 * @author Rubén Tomás Gracia
 *
 */
public interface Event {
	/**
	 * Updates the event if updates are active.
	 * 
	 * @param container
	 * @param delta
	 * @param map
	 * @throws SlickException
	 */
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException;

	/**
	 * Updates the event even when updates aren't active.
	 * 
	 * @param container
	 * @param delta
	 * @param map
	 * @throws SlickException
	 */
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException;

	/**
	 * Prerendering, for backgrounds.
	 * 
	 * @param container
	 * @param g
	 * @param tilewidth
	 * @param tileheight
	 * @throws SlickException
	 */
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException;

	/**
	 * Actual rendering of game.
	 * 
	 * @param container
	 * @param g
	 * @param tilewidth
	 * @param tileheight
	 * @throws SlickException
	 */
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException;

	/**
	 * Postrendering, for GUIs.
	 * 
	 * @param container
	 * @param g
	 * @param tilewidth
	 * @param tileheight
	 * @throws SlickException
	 */
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException;

	/**
	 * Layer where this event should be rendered by the render method.
	 * 
	 * @return
	 */
	public String getLayerName();

	/**
	 * x position of screen where this should be rendered.
	 * 
	 * @param tileWidth
	 * @return
	 */
	public int getXDraw(int tileWidth);

	/**
	 * y position of screen where this should be rendered.
	 * 
	 * @param tileHeight
	 * @return
	 */
	public int getYDraw(int tileHeight);

	/**
	 * x position of the map where this event is.
	 * 
	 * @return
	 */
	public int getXPos();

	/**
	 * y position of the map where this event is.
	 * 
	 * @return
	 */
	public int getYPos();

	/**
	 * Sets a new x position in the map
	 * 
	 * @param newPos
	 */
	public void setXPos(int newPos);

	/**
	 * Sets a new y position in the map
	 * 
	 * @param newPos
	 */
	public void setYPos(int newPos);

	/**
	 * Gets the direction of this event
	 * 
	 * @return
	 */
	public int getDirection();

	/**
	 * Sets the direction of this event
	 * 
	 * @param direction
	 */
	public void setDirection(int direction);

	/**
	 * Gets the width of this event.
	 * 
	 * @return
	 */
	public int getWidth();

	/**
	 * Gets the height of this event.
	 * 
	 * @return
	 */
	public int getHeight();

	/**
	 * Gets whether this event must block the tile where it is.
	 * 
	 * @return
	 */
	public boolean isBlocking();

	/**
	 * Action performed when map is loaded.
	 * 
	 * @param container
	 * @param map
	 * @throws SlickException
	 */
	public void onMapLoad(GameContainer container, SubTiledMap map)
			throws SlickException;

	/**
	 * Action performed when the map is set as inactive.
	 * 
	 * @param container
	 * @param map
	 * @throws SlickException
	 */
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map)
			throws SlickException;

	/**
	 * Action performed when the map is set as active.
	 * 
	 * @param container
	 * @param map
	 * @throws SlickException
	 */
	public void onMapSetAsActive(GameContainer container, SubTiledMap map)
			throws SlickException;

	/**
	 * Action performed when event is activated.
	 * 
	 * @param container
	 * @param map
	 * @param e
	 * @throws SlickException
	 */
	public void onEventActivation(GameContainer container, SubTiledMap map,
			Event e) throws SlickException;

	/**
	 * Action performed when event is touched.
	 * 
	 * @param container
	 * @param map
	 * @param e
	 * @throws SlickException
	 */
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e)
			throws SlickException;

	/**
	 * Action performed when event touch others.
	 * 
	 * @param container
	 * @param map
	 * @param e
	 * @throws SlickException
	 */
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e)
			throws SlickException;

	/**
	 * Load event from xml.
	 * 
	 * @param node
	 * @param context
	 * @throws SlickException
	 */
	public void loadEvent(Element node, Object context) throws SlickException;

	/**
	 * Return whether or not this event is working.
	 * 
	 * @return
	 */
	public boolean isWorking();

	/**
	 * Action performed to start event.
	 * 
	 * @param container
	 * @param map
	 * @param e
	 * @throws SlickException
	 */
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException;

	/**
	 * Must return a not null unique Id if you want this event to have custom
	 * properties like being deleted and not showing it in next game loads.
	 * 
	 * @return
	 */
	public String getId();
}
