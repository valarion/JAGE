package com.valarion.gameengine.events.rpgmaker.flow;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.SubEventClass;

/**
 * Comment in the event. Does nothing. Usefull for commenting things or events without using
 * XML comments.
 * @author Rubén Tomás Gracia
 *
 */
public class Comment extends SubEventClass {

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {

	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {

	}

	@Override
	public boolean isWorking() {
		return false;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e) throws SlickException {

	}

}
