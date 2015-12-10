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
package com.valarion.gameengine.events.menu.ingamemenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.StartState;

/**
 * Exit option of the ingame menu.
 * @author Rubén Tomás Gracia
 *
 */
public class MenuExit extends FlowEventClass {
	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		getState().getActiveEvents().remove(this);
		for (Event e : map.getEvents()) {
			e.onMapSetAsInactive(container, map);
		}
		GameCore.getInstance()
				.setActive(
						new StartState());
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {

	}

	@Override
	public String toString() {
		return "Exit";
	}

	@Override
	public boolean isWorking() {
		return Database.instance().getContext().isSaveEnabled();
	}
}
