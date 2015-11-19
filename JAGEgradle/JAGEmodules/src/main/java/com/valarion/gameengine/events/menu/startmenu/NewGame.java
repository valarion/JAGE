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
package com.valarion.gameengine.events.menu.startmenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.GameContext;
import com.valarion.gameengine.gamestates.InGameState;

/**
 * New game option of the start menu.
 * @author Rubén Tomás Gracia
 *
 */
public class NewGame extends FlowEventClass {

	public NewGame() {

	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		try {
			Database.instance().setContext(new GameContext());
			InGameState state = new InGameState();
			state.init(container);
			GameCore.getInstance()
					.setActive(state);
			Database.instance().stopMusic(Database.instance().getTitleMusic());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {

	}

	@Override
	public String toString() {
		return "New Game";
	}

	@Override
	public boolean isWorking() {
		return true;
	}
}
