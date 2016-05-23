package com.valarion.gameengine.eventconditions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Condition;
import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;

public class Xnor implements Condition {
	Xnor xnor;
	@Override
	public boolean eval(Event e, GameContainer container, SubTiledMap map) {
		return !xnor.eval(e, container, map);
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		xnor = new Xnor();
		xnor.load(node, context);
	}

}
