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
package com.valarion.gameengine.events.rpgmaker.messages;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.ColoredString;
import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.SubEventClass;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.util.WindowImage;

public class Dialog extends SubEventClass {
	public static final int TOP = 0;
	public static final int MID = 1;
	public static final int BOT = 2;

	protected int position;

	protected WindowImage window;

	protected boolean showing = false;

	protected LinkedList<ColoredString> strings = new LinkedList<ColoredString>();

	protected Image image = null;
	protected Image resized = null;

	protected boolean apear = false;
	protected boolean apearing = false;

	protected int apearcount = 0;
	protected int updatecount = 0;

	protected int apearlimit = 50;

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if (showing) {
			window.update(delta);
			if (apear && apearing) {
				if (container.getInput().isKeyDown(Controls.accept))
					updatecount += 2 * delta;
				else if (container.getInput().isKeyPressed(Controls.cancel)) {
					apearing = false;
					InGameState.getInstance().stopSound("writting");
					InGameState.getInstance().playSound("menucancel");
				} else
					updatecount += delta;
				apearcount += updatecount / apearlimit;
				updatecount %= apearlimit;
			} else if (container.getInput().isKeyPressed(Controls.accept)) {
				showing = false;
				InGameState.getInstance().playSound("menuaccept");
			}
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (showing) {
			window.setShowArrow(true);

			Graphics i = window.getContain().getGraphics();
			i.clear();

			int x = window.getContain().getWidth() / 20;
			int y = window.getContain().getHeight() / 6;

			if (image != null) {
				int h = window.getContain().getHeight() - 2 * y;
				if (image.getHeight() != h)
					resized = image.getScaledCopy(
							(int) (h / (float) image.getHeight() * image
									.getWidth()), h);
				else
					resized = image;

				i.drawImage(resized, x, y);

				x = x + resized.getWidth() + x;
			}

			String output = "";

			for (ColoredString s : strings) {
				output += s.getString();
			}

			int index = 0;

			if (apearing && apearcount >= output.length()) {
				apearing = false;
				container.getInput().clearKeyPressedRecord();
				InGameState.getInstance().playSound("writting");
			}
			if (!apearing | !apear)
				apearcount = output.length();

			for (ColoredString s : strings) {

				i.getFont().drawString(
						x,
						y,
						output,
						s.getColor(),
						index,
						(index + s.length() < apearcount ? index + s.length()
								: apearcount));
				index += s.length();

				if (index > apearcount)
					break;
			}

			switch (position) {
			case TOP:
				y = 0;
				break;
			case MID:
				y = container.getHeight() / 2
						- window.getImage().getHeight() / 2;
				break;
			case BOT:
				y = container.getHeight() - window.getImage().getHeight();
				break;
			}

			i.flush();
			g.drawImage(window.getImage(), 0, y);
		}
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		InGameState instance = InGameState.getInstance();
		window = instance.getWindowimages().get("dialog");

		String pos = node.getAttribute("position");

		String im = node.getAttribute("image");

		if (!im.equals(""))
			image = InGameState.getInstance().getImages().get(im);
		else
			image = null;

		if ("top".equals(pos)) {
			position = TOP;
		} else if ("mid".equals(pos)) {
			position = MID;
		} else {
			position = BOT;
		}

		String ap = node.getAttribute("apear");

		if ("true".equals(ap))
			apear = true;

		strings = getDialog(node);
	}

	public static LinkedList<ColoredString> getDialog(Element node)
			throws SlickException {
		LinkedList<ColoredString> strings = new LinkedList<ColoredString>();

		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();

		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if (n instanceof Element) {
				try {
					ColoredString e = (ColoredString) game.getSets()
							.get(ColoredString.class).get(n.getNodeName())
							.newInstance();
					e.load((Element) n);
					strings.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return strings;
	}

	@Override
	public boolean isWorking() {
		return showing;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (!showing) {
			showing = true;
			if (apear) {
				apearing = true;
				updatecount = 0;
				apearcount = 0;
				InGameState.getInstance().playSound("writting");
			}
		}
	}
}
