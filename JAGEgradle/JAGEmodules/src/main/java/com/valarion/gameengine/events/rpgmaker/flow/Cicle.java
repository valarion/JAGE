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
package com.valarion.gameengine.events.rpgmaker.flow;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.RPGMakerEvent;

/**
 * Class that describes a constant loop.
 * To break it it's necessary to use a conditional with a break cicle event.
 * @author Rubén Tomás Gracia
 *
 */
public class Cicle extends FlowEventClass {
	boolean working;
	RPGMakerEvent events;

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if (working) {
			if (!events.isWorking()) {
				events.restart();
				events.performAction(container, map, activator);
			} else
				events.paralelupdate(container, delta, map);
		}
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (working)
			events.prerender(container, g, tilewidth, tileheight);
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (working)
			events.render(container, g, tilewidth, tileheight);
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (working)
			events.postrender(container, g, tilewidth, tileheight);
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		super.performAction(container, map, e);
		working = true;
	}

	@Override
	public boolean isWorking() {
		return working;
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);

		events = new RPGMakerEvent();
		events.loadEvent(node, this);
	}

	@Override
	public void breakCicle() {
		restart();
	}

	@Override
	public void restart() {
		working = false;
		events.restart();
	}

	@Override
	protected void searchAndActiveLabel(String label) {
		if (events.hasLabel(label))
			events.goToLabel(label);

		working = true;
	}
}
