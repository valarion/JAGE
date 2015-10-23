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
package com.valarion.gameengine.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.Renderable;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.core.GameCore;

public class Camera implements Renderable {
	protected Event center;
	protected float x, y;
	protected GameCore game;
	protected InGameState instance;

	public Camera(GameCore game, InGameState instance) {
		this.game = game;
		this.instance = instance;
		center = null;
		x = 0.0f;
		y = 0.0f;
	}

	public Camera focusAt(Event center) {
		this.center = center;
		return this;
	}

	public Camera focusAt(float x, float y) {
		this.x = x;
		this.y = y;
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
		} else {
			g.translate(x, y);
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
