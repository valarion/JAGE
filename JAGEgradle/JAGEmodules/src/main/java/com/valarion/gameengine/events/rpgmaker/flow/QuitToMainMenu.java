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
package com.valarion.gameengine.events.rpgmaker.flow;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.menu.ingamemenu.MenuExit;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;

/**
 * Class that stops the execution of an event.
 * @author Rub�n Tom�s Gracia
 *
 */
public class QuitToMainMenu extends FlowEventClass {
	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		MenuExit exit = new MenuExit();
		exit.loadEvent(null, this);
		exit.paralelupdate(container, 0, map);
	}

}
