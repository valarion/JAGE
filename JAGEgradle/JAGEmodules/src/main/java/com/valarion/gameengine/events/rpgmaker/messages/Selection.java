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
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.events.rpgmaker.RPGMakerEvent;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.util.Util;
import com.valarion.gameengine.util.WindowImage;

/**
 * Class that gives an input as a dialog for a selection between at max 4 inputs and a cancel.
 * @author Rubén Tomás Gracia
 *
 */
public class Selection extends FlowEventClass {
	public static final int TOP = 0;
	public static final int MID = 1;
	public static final int BOT = 2;

	protected int position;

	protected WindowImage window;

	protected boolean showing = false;

	protected FlowEventInterface ways[] = new RPGMakerEvent[5];

	protected FlowEventInterface active;

	protected Event activator;
	@SuppressWarnings("unchecked")
	protected LinkedList<ColoredString>[] options = new LinkedList[4];

	protected int limit;

	protected int selected;

	protected Image image = null;
	protected Image resized = null;

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		if (active != null)
			active.update(container, delta, map);
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if (showing) {
			window.update(delta);
			if (container.getInput().isKeyPressed(Controls.accept)) {
				showing = false;
				active = ways[selected];
				active.performAction(container, map, activator);
				Database.instance().playSound("menuaccept");
			} else if (container.getInput().isKeyPressed(Controls.moveUp)) {
				selected = (selected + limit - 1) % limit;
				Database.instance().playSound("menumove");
			} else if (container.getInput().isKeyPressed(Controls.moveDown)) {
				selected = (selected + 1) % limit;
				Database.instance().playSound("menumove");
			} else if (container.getInput().isKeyPressed(Controls.cancel)) {
				showing = false;
				active = ways[4];
				if (active != null) {
					active.performAction(container, map, activator);
				}
				Database.instance().playSound("menucancel");
			}
		} else if (active != null) {
			if (active.isWorking())
				active.paralelupdate(container, delta, map);
			else {
				restart();
			}
		}
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.prerender(container, g, tilewidth, tileheight);
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.render(container, g, tilewidth, tileheight);
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (showing) {
			window.setShowArrow(false);
			Graphics i = window.getContain().getGraphics();
			i.clear();

			int x = window.getContain().getWidth() / 20;
			int y = window.getContain().getHeight() / 6;

			if (image != null) {
				int h = window.getContain().getHeight() - 2 * y;
				if (image.getHeight() != h)
					resized = Util.getScaled(image,
							(int) (h / (float) image.getHeight() * image
									.getWidth()), h);
				else
					resized = image;

				i.drawImage(resized, x, y);

				x = x + resized.getWidth() + x;
			}

			Image arrow = window.getModel().getImage("rightArrow");
			int separation = (int) (((float) (arrow.getWidth())) * 1.5f);
			x += separation;

			int linewidth = (window.getContain().getHeight() - 2 * y) / 4;

			int in = 0;

			for (LinkedList<ColoredString> strings : options) {
				String output = "";

				if (strings == null)
					break;

				if (in == selected) {
					i.drawImage(arrow, x - separation, y);
				}

				for (ColoredString s : strings) {
					output += s.getString();
				}

				int index = 0;

				for (ColoredString s : strings) {

					i.getFont().drawString(x, y, output, s.getColor(), index,
							index + s.length());
					index += s.length();
				}

				y += linewidth;
				in = (in + 1) % limit;
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
		} else if (active != null)
			active.postrender(container, g, tilewidth, tileheight);
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);

		Database instance = Database.instance();
		window = instance.getWindowimages().get("dialog");

		String im = node.getAttribute("image");

		if (!im.equals(""))
			image = instance.getImages().get(im);
		else
			image = null;

		String pos = node.getAttribute("position");

		if ("top".equals(pos)) {
			position = TOP;
		} else if ("mid".equals(pos)) {
			position = MID;
		} else {
			position = BOT;
		}

		NodeList childs = node.getChildNodes();

		limit = 0;

		ways[4] = null;

		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if (n instanceof Element) {
				Element e = (Element) n;
				if ("option".equals(e.getNodeName()) && limit < 4) {

					NodeList childs2 = e.getChildNodes();

					for (int j = 0; j < childs2.getLength(); j++) {
						Node n2 = childs2.item(j);
						if (n2 instanceof Element) {
							Element e2 = (Element) n2;

							if ("events".equals(e2.getNodeName())) {
								ways[limit] = new RPGMakerEvent();
								ways[limit].loadEvent(e2, this);
							} else if ("text".equals(e2.getNodeName())) {
								options[limit] = Dialog.getDialog(e2, this);
							}
						}
					}

					limit++;
				} else if ("cancel".equals(e.getNodeName())) {
					ways[4] = new RPGMakerEvent();
					ways[4].loadEvent(e, this);
				}
			}
		}

		String onCancel = node.getAttribute("oncancel");

		if ("option1".equals(onCancel)) {
			ways[4] = ways[0];
		} else if ("option2".equals(onCancel)) {
			ways[4] = ways[1];
		} else if ("option3".equals(onCancel)) {
			ways[4] = ways[2];
		} else if ("option4".equals(onCancel)) {
			ways[4] = ways[3];
		}
	}

	/*protected LinkedList<ColoredString> getDialog(Element node)
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
					e.load((Element) n, event);
					strings.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return strings;
	}*/

	@Override
	public boolean isWorking() {
		return (showing || active != null);
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (!isWorking()) {
			showing = true;
			active = null;
			activator = e;
			selected = 0;
		}
	}

	@Override
	public void stop() {
		if (parent != null)
			parent.stop();

		restart();
	}

	@Override
	public void restart() {
		if (active != null)
			active.restart();
		showing = false;
		active = null;
	}

	@Override
	protected void searchAndActiveLabel(String label) {
		for (FlowEventInterface events : ways) {
			if (events != null && events.hasLabel(label)) {
				active = events;
				active.goToLabel(label);
			}
		}
	}
}
