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

import java.lang.reflect.InvocationTargetException;
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

/**
 * State containing the start menu mechanics.
 * @author Rubén Tomás Gracia
 *
 */
public class StartState extends SubState {

	protected WindowImage window;

	protected Set<Event> activeEvents;

	protected Image background;

	/**
	 * Creates a start menu.
	 */
	public StartState() {
		Database.createInstance();

		window = Database.instance().getWindowimages().get("startmenu");

		try {
			background = Database.instance()
					.getImages()
					.get(Database.instance().getTitleBackground());
		} catch (Exception e) {
		}

		Database.instance().loopMusic(Database.instance().getTitleMusic());

		activeEvents = Util.getset();

		try {
			activeEvents.add((Event) GameCore.getInstance().getSets().get(Event.class).get("StartMenu").getDeclaredConstructor(SubState.class).newInstance(this));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			activeEvents.add(new StartMenu(this));
		}
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
		g.drawImage(background, 0, 0, GameCore.getInstance().getApp().getWidth(),
				GameCore.getInstance().getApp().getHeight(), 0, 0, background.getWidth(), background.getHeight());
		for (Event e : activeEvents) {
			e.postrender(container, g, 0, 0);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {

	}

	@Override
	public Set<Event> getActiveEvents() {
		return activeEvents;
	}
}
