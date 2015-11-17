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

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.SubTiledMap;

public abstract class SubEventClass implements Event {

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		// Not needed
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		// Not needed
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		// Not needed
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		// Not needed
	}

	@Override
	public String getLayerName() {
		// Not needed
		return null;
	}

	@Override
	public int getXDraw(int tileWidth) {
		// Not needed
		return 0;
	}

	@Override
	public int getYDraw(int tileHeight) {
		// Not needed
		return 0;
	}

	@Override
	public int getXPos() {
		// Not needed
		return 0;
	}

	@Override
	public int getYPos() {
		// Not needed
		return 0;
	}

	@Override
	public void setXPos(int newPos) {
		// Not needed
	}

	@Override
	public void setYPos(int newPos) {
		// Not needed
	}

	@Override
	public int getDirection() {
		// Not needed
		return 0;
	}

	@Override
	public void setDirection(int direction) {
		// Not needed
	}

	@Override
	public int getWidth() {
		// Not needed
		return 0;
	}

	@Override
	public int getHeight() {
		// Not needed
		return 0;
	}

	@Override
	public boolean isBlocking() {
		// Not needed
		return false;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map)
			throws SlickException {
		// Not needed

	}

	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map)
			throws SlickException {
		// Not needed
	}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map)
			throws SlickException {
		// Not needed
	}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map,
			Event e) throws SlickException {
		// Not needed
	}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		// Not needed
	}

	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		// Not needed
	}

	@Override
	public String getId() {
		// Not needed
		return null;
	}
}
