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
package com.valarion.gameengine.events.menu.ingamemenu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.GameContext;
import com.valarion.gameengine.util.Keyboard;
import com.valarion.gameengine.util.WindowImage;
/**
 * Save option of the ingame menu.
 * Also, save menu.
 * @author Rub�n Tom�s Gracia
 *
 */
public class SaveMenu extends FlowEventClass {

	protected LinkedList<GameContext> saves = new LinkedList<GameContext>();
	protected LinkedList<String> filenames = new LinkedList<String>();

	protected int selection = -1;
	protected int showing = -1;
	protected int limit = 4;

	protected boolean editing = false;
	protected String editname = null;
	protected boolean showbar = true;
	protected int count = 0;

	protected SecureRandom random = new SecureRandom();

	protected SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	protected String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	public SaveMenu() {
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
					filenames.add(save.getAbsolutePath());
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
		if (!editing) {
			if (input.isKeyPressed(Controls.accept)) {
				editing = true;
				showbar = true;
				count = 0;
				if (selection == -1) {
					editname = "";
				} else {
					editname = saves.get(selection).getSavename();
				}
				Database.instance().playSound("menuaccept");
			} else if (input.isKeyPressed(Controls.cancel)) {
				getState().getActiveEvents().add(parent);
				getState().getActiveEvents().remove(this);
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
				if (selection < -1) {
					selection = -1;
				} else
					Database.instance().playSound("menumove");
				if (selection < showing)
					showing--;
			}
		} else {
			count += delta;
			if (count > 500) {
				showbar = !showbar;
				count %= 500;
			}

			if (input.isKeyPressed(Controls.cancel)) {
				editing = false;
				Database.instance().playSound("menucancel");
			} else if (input.isKeyPressed(Controls.accept)) {
				String filename = null;

				if (selection >= 0) {
					filename = filenames.get(selection);
					new File(filename).delete();
				}

				save(filename, editname);

				getState().getActiveEvents().remove(this);
				Database.instance().playSound("menuaccept");
			} else if (input.isKeyPressed(Input.KEY_BACK)) {
				if (editname.length() > 0)
					editname = editname.substring(0, editname.length() - 1);
				Database.instance().playSound("menumove");
			} else {
				String ch = Keyboard.getChar(input);
				if (!"".equals(ch)) {
					Database.instance().playSound("menumove");
					if (editname.length() <= 30)
						editname += ch;
				}
			}
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		rendertop(container, g);
		rendergames(container, g);
	}

	/**
	 * Render the top part of the menu.
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	private void rendertop(GameContainer container, Graphics g)
			throws SlickException {
		WindowImage window = Database.instance().getWindowimages()
				.get("loadtop");

		window.setShowArrow(false);
		Image contain = window.getContain();
		Graphics i = window.getContain().getGraphics();
		i.clear();

		Font font = i.getFont();

		String text = "Save game";

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
	private void rendergames(GameContainer container, Graphics g)
			throws SlickException {
		WindowImage window = Database.instance().getWindowimages()
				.get("loadpart");
		WindowImage top = Database.instance().getWindowimages()
				.get("loadtop");

		window.setShowArrow(false);
		Image contain = window.getContain();
		Graphics i = contain.getGraphics();

		for (int it = showing; it < showing + limit && it < saves.size(); it++) {
			
			
			if (it == -1) {
				i.clear();
				
				String text = "New Game";
				Image rightarrow = window.getModel().getImage("rightArrow");

				if (editing && it == selection) {
					text = editname;
					if (showbar)
						text += "_";
				}

				if (it == selection) {
					i.drawImage(rightarrow, 10, 10);
				}
				i.drawString(text, 10 + rightarrow.getWidth() + 5, 10);
				
				i.flush();
			} else {
				GameContext context = saves.get(it);

				String text = null;

				if (editing && it == selection) {
					text = editname;
					if (showbar)
						text += "_";
				}
				
				context.draw(window, text, it == selection);
			}
			
			g.drawImage(window.getImage(), 0, top.getWindow().getHeight()
					+ (it - showing) * window.getWindow().getHeight());
		}
	}

	@Override
	public String toString() {
		return "Save";
	}

	@Override
	public boolean isWorking() {
		return Database.instance().getContext().isSaveEnabled();
	}

	/**
	 * Save game.
	 * @param filename
	 * @param savename
	 */
	protected void save(String filename, String savename) {
		try {
			GameContext context = Database.instance().getContext();

			context.setSavetime();
			context.setSavename(savename);

			if (filename != null) {
				File f = new File(filename);
				if (f.exists())
					f.delete();
			}

			filename = savename + "-" + dt.format(context.getSavetime());
			filename = filename.replace(':', '-');

			OutputStream file = new FileOutputStream("saves/" + filename);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			output.writeObject(context);

			output.close();
		} catch (IOException ex) {
			Log.error("Cannot perform output.", ex);
		}
	}
}
