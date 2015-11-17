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
package com.valarion.gameengine.events.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.SubState;
import com.valarion.gameengine.util.WindowImage;

public class OptionsMenu extends FlowEventClass {

	public static final int top = 0;
	public static final int mid = 1;
	public static final int bot = 2;

	public static final int left = 0;
	public static final int center = 1;
	public static final int right = 2;

	protected FlowEventInterface options[];

	protected WindowImage window;

	protected int selected = 0;

	protected int xpos, ypos;

	protected SubState instance;

	protected boolean closeoncancel;

	public OptionsMenu(boolean closeoncancel, SubState instance, int xpos,
			int ypos, FlowEventInterface... options) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.options = options;
		this.instance = instance;
		this.closeoncancel = closeoncancel;

		for (FlowEventInterface e : options) {
			try {
				e.loadEvent(null, this);
			} catch (SlickException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
		}
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		Input input = container.getInput();
		if (input.isKeyPressed(Controls.accept)) {
			FlowEventInterface menu = options[selected];
			menu.restart();
			menu.loadEvent(null, this);
			instance.getActiveEvents().add(menu);
			instance.getActiveEvents().remove(this);
			Database.instance().playSound("menuaccept");
		} else if (input.isKeyPressed(Controls.cancel)) {
			if (closeoncancel) {
				instance.getActiveEvents().remove(this);
			}
			Database.instance().playSound("menucancel");
		} else if (input.isKeyPressed(Controls.moveDown)) {
			selected = (selected + 1) % options.length;
			while (!options[selected].isWorking()) {
				selected = (selected + 1) % options.length;
			}
			Database.instance().playSound("menumove");
		} else if (input.isKeyPressed(Controls.moveUp)) {
			selected = (selected + options.length - 1) % options.length;
			while (!options[selected].isWorking()) {
				selected = (selected + options.length - 1) % options.length;
			}
			Database.instance().playSound("menumove");
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		window = Database.instance().getWindowimages()
				.get(Integer.toString(options.length));
		window.setShowArrow(false);
		Graphics i = window.getContain().getGraphics();
		i.clear();

		int x = window.getContain().getWidth() / 20;
		int y = window.getModel().getImage("top").getHeight();

		Image arrow = window.getModel().getImage("rightArrow");
		int separation = (int) (((float) (arrow.getWidth())) * 1.5f);
		x += separation;

		int linewidth = (int) (i.getFont().getLineHeight() * 2);

		for (int in = 0; in < options.length; in++) {
			if (in == selected) {
				i.drawImage(arrow, x - separation, y);
			}
			if (!options[in].isWorking()) {
				i.getFont().drawString(x, y, options[in].toString(),
						Color.black);
			} else {
				i.getFont().drawString(x, y, options[in].toString(),
						Color.white);
			}

			y += linewidth;
		}

		i.flush();

		Image end = window.getImage();
		x = 0;
		switch (xpos) {
		case 1:
			x = container.getWidth() / 2 - end.getWidth() / 2;
			break;
		case 2:
			x = container.getWidth() - end.getWidth();
		}
		y = 0;
		switch (ypos) {
		case 1:
			y = container.getHeight() / 2 - end.getHeight() / 2;
			break;
		case 2:
			x = container.getHeight() - end.getHeight();
		}

		g.drawImage(window.getImage(), x, y);
	}
}
