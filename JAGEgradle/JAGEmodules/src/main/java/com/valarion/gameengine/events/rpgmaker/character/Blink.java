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
package com.valarion.gameengine.events.rpgmaker.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.events.rpgmaker.GameEvent;
import com.valarion.gameengine.util.GameSprite;

/**
 * Class that sets an interrupt/s.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class Blink extends FlowEventClass {
	int time;
	int period;

	int elapsed;
	protected GameSprite sprite;

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		elapsed += delta;
		FlowEventInterface event = getEvent();
		System.out.println(delta);
		if (event instanceof GameEvent) {
			((GameEvent) event).getActive().setRendersprite(elapsed / period % 2 == 0);
		}
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);

		time = Integer.parseInt(node.getAttribute("time"));
		period = Integer.parseInt(node.getAttribute("period"));
	}

	@Override
	public boolean isWorking() {
		return elapsed < time;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e) throws SlickException {
		elapsed = 0;
	}

}
