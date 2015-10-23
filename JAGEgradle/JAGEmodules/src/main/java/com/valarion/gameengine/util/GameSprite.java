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

import org.newdawn.slick.Animation;

public class GameSprite {
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	protected Animation[] sides;

	protected int direction;

	protected float spritespeed;
	protected float multiplier;
	protected float movingspeed;

	protected String name;

	public GameSprite(Animation up, Animation down, Animation left,
			Animation right, float spritespeed, float movingspeed, String name) {
		sides = new Animation[4];

		sides[UP] = up;
		sides[DOWN] = down;
		sides[LEFT] = left;
		sides[RIGHT] = right;
		this.spritespeed = spritespeed;
		this.movingspeed = movingspeed;

		this.name = name;

		direction = UP;
	}

	public void update(long delta) {
		sides[direction].setSpeed(spritespeed * multiplier);
		sides[direction].update(delta);
	}

	public void setStopped() {
		switch (sides[direction].getFrame()) {
		case 1:
			sides[direction].setCurrentFrame(2);
		case 3:
			sides[direction].setCurrentFrame(0);
		}
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}

	public float getMultiplier() {
		return multiplier;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		if (direction >= 0 && direction <= 3)
			this.direction = direction;
	}

	public void draw(int x, int y) {
		sides[direction].draw(x, y);
	}

	public int getWidth() {
		return sides[direction].getCurrentFrame().getWidth();
	}

	public int getHeight() {
		return sides[direction].getCurrentFrame().getHeight();
	}

	public String getName() {
		return name;
	}

	public float getMovingspeed() {
		return movingspeed;
	}

	public float getSpritespeed() {
		return spritespeed;
	}
}
