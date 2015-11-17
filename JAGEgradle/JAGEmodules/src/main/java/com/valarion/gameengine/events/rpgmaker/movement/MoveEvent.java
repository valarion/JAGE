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
package com.valarion.gameengine.events.rpgmaker.movement;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.Route;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;

public class MoveEvent extends FlowEventClass {
	protected Route route = null;
	protected String id = null;
	protected boolean player;
	
	FlowEventInterface event;
	
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		String ob = node.getAttribute("objetive");
		
		if("player".equals(ob)) {
			player = true;
		}
		else if ("self".equals(ob)){
			player = false;
		}
		else {
			id = node.getAttribute("event");
		}
		
		route = new Route(node,false);
	}
	
	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e) {
		try {
			if(id != null) {
					event = (FlowEventInterface) map.getEventsById().get(id);
			}
			else if(!player) {
				event = getEvent();
			}
			else {
				event = (FlowEventInterface) getState().getPlayer();
			}
		}
		catch(Exception ex) {
			event = null;
		}
		
		if(event != null) {
			event.setRoute(route);
		}
		
	}
	
	@Override
	public boolean isWorking() {
		//TODO
		return (event!=null && route.isWait() && event.getRoute() != null && event.getRoute().getRoute().size() > 0);
	}
}
