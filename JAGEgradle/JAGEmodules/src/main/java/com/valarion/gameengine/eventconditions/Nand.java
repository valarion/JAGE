package com.valarion.gameengine.eventconditions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;

public class Nand implements Condition {
	And and;
	@Override
	public boolean eval(Event e, GameContainer container, SubTiledMap map) {
		return !and.eval(e, container, map);
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		and = new And();
		and.load(node, context);
	}

}
