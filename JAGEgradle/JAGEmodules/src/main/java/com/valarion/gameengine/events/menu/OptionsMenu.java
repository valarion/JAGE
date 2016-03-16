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

import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.SubState;
import com.valarion.gameengine.util.WindowImage;

/**
 * Describes the functioning of an options menu.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class OptionsMenu extends FlowEventClass {

	public static enum XPosition {
		left, right, center, custom;

		protected int position;

		public static XPosition customPosition(int position) {
			custom.position = position;
			return custom;
		}
	}

	public static enum YPosition {
		top, mid, bot, custom;

		protected int position;

		public static YPosition customPosition(int position) {
			custom.position = position;
			return custom;
		}
	}

	protected FlowEventInterface options[];

	protected WindowImage window;

	protected int selected = 0;

	protected XPosition xpos;
	protected YPosition ypos;

	protected SubState instance;

	protected boolean closeoncancel;
	
	protected int x;
	protected int y;

	public OptionsMenu(boolean closeoncancel, SubState instance, XPosition xpos, YPosition ypos, WindowImage window,
			FlowEventInterface... options) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.options = options;
		this.instance = instance;
		this.closeoncancel = closeoncancel;

		this.window = window;

		for (FlowEventInterface e : options) {
			try {
				e.loadEvent(null, this);
			} catch (SlickException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
		}
		
		if(xpos.equals(XPosition.custom)) {
			x = xpos.position;
		}
		if(ypos.equals(YPosition.custom)) {
			y = ypos.position;
		}
	}

	public OptionsMenu(boolean closeoncancel, SubState instance, XPosition xpos, YPosition ypos,
			FlowEventInterface... options) {
		this(closeoncancel, instance, xpos, ypos,
				Database.instance().getWindowimages().get(Integer.toString(options.length)), options);

	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
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
	public void postrender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
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
				i.getFont().drawString(x, y, options[in].toString(), Color.black);
			} else {
				i.getFont().drawString(x, y, options[in].toString(), Color.white);
			}

			y += linewidth;
		}

		i.flush();

		Image end = window.getImage();
		x = 0;
		switch (xpos) {
		case center:
			x = container.getWidth() / 2 - end.getWidth() / 2;
			break;
		case right:
			x = container.getWidth() - end.getWidth();
			break;
		case custom:
			x = this.x;
			break;
		default:
			break;
		}
		y = 0;
		switch (ypos) {
		case mid:
			y = container.getHeight() / 2 - end.getHeight() / 2;
			break;
		case bot:
			y = container.getHeight() - end.getHeight();
			break;
		case custom:
			y = this.y;
			break;
		default:
			break;
		}

		g.drawImage(window.getImage(), x, y);
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getSelected() {
		return selected;
	}
	
	public FlowEventInterface[] getOptions() {
		return options;
	}
	
	public void setOptions(FlowEventInterface... options) {
		this.options = options;
	}
	
	public WindowImage getWindow() {
		return window;
	}
}
