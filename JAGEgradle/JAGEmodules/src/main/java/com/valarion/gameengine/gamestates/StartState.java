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
package com.valarion.gameengine.gamestates;

import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.events.menu.startmenu.StartMenu;
import com.valarion.gameengine.util.Util;
import com.valarion.gameengine.util.WindowImage;

public class StartState extends SubState {

	protected WindowImage window;

	InGameState instance;

	protected Set<Event> activeEvents;

	protected Image background;

	protected static StartState startinstance;

	public StartState() {
		startinstance = this;

		instance = InGameState.getInstance();
		if (instance == null)
			instance = InGameState.createInstance();

		window = instance.getWindowimages().get("startmenu");

		try {
			background = instance
					.getImages()
					.get(instance.getTitleBackground())
					.getScaledCopy(GameCore.getInstance().getApp().getWidth(),
							GameCore.getInstance().getApp().getHeight());
		} catch (Exception e) {
		}

		instance.playMusic(instance.getTitleMusic());

		activeEvents = Util.getset();

		activeEvents.add(new StartMenu());
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		for (Event e : activeEvents) {
			e.paralelupdate(container, delta, null);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.drawImage(background, 0, 0);
		for (Event e : activeEvents) {
			e.postrender(container, g, 0, 0);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {

	}

	public Set<Event> getActiveEvents() {
		return activeEvents;
	}

	public static StartState getInstance() {
		return startinstance;
	}

}
