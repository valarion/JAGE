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
package com.valarion.gameengine.eventconditions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.util.GameSprite;

/**
 * Condition of a variable being greater than or equal that a given value.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class CanDirectlySeeCondition implements Condition {

	protected String id = null;
	protected boolean player;

	protected int limit;

	@Override
	public boolean eval(Event from, GameContainer container, SubTiledMap map) {
		Event to;
		try {
			if (id != null) {
				to = map.getEventsById().get(id);
			} else {
				to = Database.instance().getContext().getPlayer();
			}
		} catch (Exception ex) {
			to = null;
		}

		if (to != null && from != null) {
			if (to.getXPos() == from.getXPos()) {
				if (to.getYPos() == from.getYPos()) {
					return true;
				}
				if (from.getDirection() == GameSprite.UP) {
					for (int i = 0; i < limit; i++) {
						if (map.getEvents(from.getXPos(), from.getYPos() - i).contains(to)) {
							return true;
						}
						if (map.isBlocked(from.getXPos(), from.getYPos() - i)) {
							return false;
						}
					}
				} else if (from.getDirection() == GameSprite.DOWN) {
					for (int i = 0; i < limit; i++) {
						if (map.getEvents(from.getXPos(), from.getYPos() + i).contains(to)) {
							return true;
						}
						if (map.isBlocked(from.getXPos(), from.getYPos() + i)) {
							return false;
						}
					}
				}
			} else if (to.getYPos() == from.getYPos()) {
				if (from.getDirection() == GameSprite.LEFT) {
					for (int i = 0; i < limit; i++) {
						if (map.getEvents(from.getXPos() - i, from.getYPos()).contains(to)) {
							return true;
						}
						if (map.isBlocked(from.getXPos() - i, from.getYPos())) {
							return false;
						}
					}
				} else if (from.getDirection() == GameSprite.RIGHT) {
					for (int i = 0; i < limit; i++) {
						if (map.getEvents(from.getXPos() + i, from.getYPos()).contains(to)) {
							return true;
						}
						if (map.isBlocked(from.getXPos() + i, from.getYPos())) {
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		String ob = node.getAttribute("objetive");

		if ("player".equals(ob)) {
			player = true;
		} else {
			id = node.getAttribute("event");
		}

		try {
			limit = Integer.parseInt(node.getAttribute("limit"));
		} catch (Exception e) {
			limit = Integer.MAX_VALUE;
		}
	}

}
