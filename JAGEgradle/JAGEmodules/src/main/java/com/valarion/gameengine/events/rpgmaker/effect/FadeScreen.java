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
package com.valarion.gameengine.events.rpgmaker.effect;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.SubEventClass;
import com.valarion.gameengine.util.Fading;

/**
 * Class that sets an interrupt/s.
 * @author Rubén Tomás Gracia
 *
 */
public class FadeScreen extends SubEventClass {
	
	
	Fading fading;

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		fading.update(delta);
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		String f = node.getAttribute("from").replace("0x", "");
		int r = Integer.parseInt(f.substring(0, 2),16);
		int g = Integer.parseInt(f.substring(2, 4),16);
		int b = Integer.parseInt(f.substring(4, 6),16);
		int a = Integer.parseInt(f.substring(6, 8),16);
		Color from = new Color(r,g,b,a);
		f = node.getAttribute("to").replace("0x", "");
		r = Integer.parseInt(f.substring(0, 2),16);
		g = Integer.parseInt(f.substring(2, 4),16);
		b = Integer.parseInt(f.substring(4, 6),16);
		a = Integer.parseInt(f.substring(6, 8),16);
		Color to = new Color(r,g,b,a);
		int in = Integer.parseInt(node.getAttribute("in"));
		fading = new Fading(from,to,in);
	}
	
	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) {
		fading.render(container, g);
	}

	@Override
	public boolean isWorking() {
		return fading.isWorking();
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		fading.start();
	}

}
