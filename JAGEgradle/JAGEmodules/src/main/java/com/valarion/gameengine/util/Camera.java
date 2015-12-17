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
package com.valarion.gameengine.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.Renderable;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.gamestates.InGameState;

/**
 * Class containing the mechanics of a camera.
 * This class changes matrix to show what's defined to show.
 * @author Rubén Tomás Gracia
 *
 */
public class Camera implements Renderable {
	protected Event center;
	protected float x, y;
	
	protected SubTiledMap map;
	
	protected GameCore game;
	protected InGameState instance;

	/**
	 * Create camera.
	 * @param game
	 * @param instance
	 */
	public Camera(GameCore game, InGameState instance) {
		this.game = game;
		this.instance = instance;
		center = null;
		x = 0.0f;
		y = 0.0f;
	}

	/**
	 * Focus camera at event.
	 * @param center
	 * @return
	 */
	public Camera focusAt(Event center) {
		this.center = center;
		return this;
	}

	/**
	 * Focus camera at position.
	 * @param x
	 * @param y
	 * @return
	 */
	public Camera focusAt(float x, float y) {
		this.map = null;
		this.center = null;
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Focus camera at map and fix it to screen.
	 * @param x
	 * @param y
	 * @return
	 */
	public Camera focusAt(SubTiledMap map) {
		this.map = map;
		this.center = null;
		return this;
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (center != null) {
			int playerXPos = (int) (game.getApp().getWidth() / 2 - center
					.getWidth() / 2);
			int playerYPos = (int) (game.getApp().getHeight() / 2 - center
					.getHeight() / 2);
			int mapXOff = -center.getXDraw(instance.getActive().getTileWidth())
					+ playerXPos;
			int mapYOff = -center
					.getYDraw(instance.getActive().getTileHeight())
					+ playerYPos;

			g.translate(mapXOff, mapYOff);
		} 
		else if(map != null) {
			float xscale = (float)container.getWidth()/((float)map.getWidth()*map.getTileWidth());
			float yscale = (float)container.getHeight()/((float)map.getHeight()*map.getTileHeight());
			g.scale(xscale,yscale);

		}
		else {
			g.translate(x, y);
		}
	}

	/**
	 * Get camera x position.
	 * @return
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get camera y position.
	 * @return
	 */
	public float getY() {
		return y;
	}
}
