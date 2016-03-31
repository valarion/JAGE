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
package com.valarion.gameengine.events.rpgmaker.movement;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.Player;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.util.GameSprite;

/**
 * Teleport player to a destination.
 * This is the way to change maps.
 * @author Rubén Tomás Gracia
 *
 */
public class Teleport extends FlowEventClass {

	protected int x;
	protected int y;

	protected int destX;
	protected int destY;

	protected String destMap;

	protected int destDirection;

	protected boolean teleporting = false;
	protected boolean shading = false;
	protected int alpha;

	protected Player player;

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if (teleporting) {
			if (shading) {
				if (alpha == 255) {
					shading = false;

					InGameState gameinstance = getState();
					Event player = gameinstance.getPlayer();

					map.getEvents(player.getXPos(), player.getYPos()).remove(
							player);

					player.setXPos(destX);
					player.setYPos(destY);
					player.setDirection(destDirection);

					gameinstance.setAsActive(container, destMap);
					gameinstance.getActive().add(this);

				} else {
					alpha += delta;

					if (alpha >= 255) {
						alpha = 255;
					}
				}
			} else {
				if (alpha == 0) {
					teleporting = false;
				} else {
					alpha -= delta;
					if (alpha <= 0) {
						alpha = 0;
					}
				}
			}

		}

	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (teleporting) {
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, container.getWidth(), container.getHeight());
		}
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		this.destX = Integer.parseInt(node.getAttribute("xDest"));
		this.destY = Integer.parseInt(node.getAttribute("yDest"));
		this.destMap = node.getAttribute("destMap");
		String dir = node.getAttribute("destDirection");

		if ("up".equals(dir)) {
			this.destDirection = GameSprite.UP;
		} else if ("down".equals(dir)) {
			this.destDirection = GameSprite.DOWN;
		} else if ("left".equals(dir)) {
			this.destDirection = GameSprite.LEFT;
		} else if ("right".equals(dir)) {
			this.destDirection = GameSprite.RIGHT;
		} else {
			this.destDirection = -1; // keeps the direction
		}
	}

	@Override
	public boolean isWorking() {
		return teleporting;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (!teleporting) {
			teleporting = true;
			shading = true;
			alpha = 0;
		}
	}

}
