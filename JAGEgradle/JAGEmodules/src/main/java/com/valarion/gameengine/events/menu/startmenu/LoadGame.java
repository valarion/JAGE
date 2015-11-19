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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.LinkedList;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.GameContext;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.gamestates.SubState;
import com.valarion.gameengine.util.WindowImage;

/**
 * Load game option of the start menu.
 * @author Rubén Tomás Gracia
 *
 */
public class LoadGame extends FlowEventClass {

	protected LinkedList<GameContext> saves = new LinkedList<GameContext>();

	protected int selection = 0;
	protected int showing = 0;
	protected int limit = 4;
	
	SubState state;

	/**
	 * Reads saved games from disk.
	 * @param instance State in which this menu is working.
	 */
	public LoadGame(SubState instance) {
		state = instance;
		File savesdir = new File("saves");
		if(!savesdir.exists()) {
			savesdir.mkdirs();
		}
		File[] savefiles = savesdir.listFiles();

		if (savefiles != null)
			for (File save : savefiles) {

				try {
					ObjectInputStream in;
					in = new ObjectInputStream(new FileInputStream(save));
					GameContext context = (GameContext) (in.readObject());
					saves.add(context);
					in.close();
				} catch (IOException | ClassNotFoundException e) {
				}

			}

		Collections.sort(saves);
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		Input input = container.getInput();

		if (input.isKeyPressed(Controls.accept)) {
			try {
				Database.instance().setContext(saves.get(selection));
				InGameState state = new InGameState();
				state.init(container);
				GameCore.getInstance()
						.setActive(state);
				state.getActiveEvents().remove(this);
				Database.instance().stopMusic(Database.instance().getTitleMusic());
				Database.instance().playSound("menuaccept");
				Database.instance().playSound(Database.instance().getTitleMusic());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}

		} else if (input.isKeyPressed(Controls.cancel)) {
			state.getActiveEvents().add(parent);
			state.getActiveEvents().remove(this);
			Database.instance().playSound("menucancel");
		} else if (input.isKeyPressed(Controls.moveDown)) {
			selection += 1;
			if (selection >= saves.size()) {
				selection = saves.size() - 1;
			} else
				Database.instance().playSound("menumove");
			if (selection >= (showing + limit))
				showing++;

		} else if (input.isKeyPressed(Controls.moveUp)) {
			selection -= 1;
			if (selection < 0) {
				selection = 0;
			} else
				Database.instance().playSound("menumove");
			if (selection < showing)
				showing--;
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		rendertop(container, g);
		rendergames(container, g);
	}

	/**
	 * Render the top of the menu.
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	protected void rendertop(GameContainer container, Graphics g)
			throws SlickException {
		WindowImage window = Database.instance().getWindowimages()
				.get("loadtop");

		window.setShowArrow(false);
		Image contain = window.getContain();
		Graphics i = window.getContain().getGraphics();
		i.clear();

		Font font = i.getFont();

		String text = "Load game";

		i.drawString(text, contain.getWidth() / 2 - font.getWidth(text) / 2,
				contain.getHeight() / 2 - font.getLineHeight() / 2);

		i.flush();
		g.drawImage(window.getImage(), 0, 0);
	}

	/**
	 * Render the saved games part of the menu.
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	protected void rendergames(GameContainer container, Graphics g)
			throws SlickException {
		WindowImage window = Database.instance().getWindowimages()
				.get("loadpart");
		WindowImage top = Database.instance().getWindowimages()
				.get("loadtop");

		window.setShowArrow(false);

		for (int it = showing; it < showing + limit && it < saves.size(); it++) {
			GameContext context = saves.get(it);
			
			context.draw(window, null, it == selection);

			g.drawImage(window.getImage(), 0, top.getWindow().getHeight()
					+ (it - showing) * window.getWindow().getHeight());
		}
	}

	@Override
	public void restart() {
		selection = 0;
		showing = 0;
	}

	@Override
	public String toString() {
		return "Load Game";
	}

	@Override
	public boolean isWorking() {
		return saves.size() > 0;
	}
}
