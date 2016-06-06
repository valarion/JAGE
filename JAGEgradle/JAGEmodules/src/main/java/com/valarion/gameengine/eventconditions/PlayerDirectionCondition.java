package com.valarion.gameengine.eventconditions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.util.GameSprite;

public class PlayerDirectionCondition implements Condition {
	int direction = -1;

	@Override
	public boolean eval(Event e, GameContainer container, SubTiledMap map) {
		try {
			return Database.instance().getContext().getPlayer().getDirection() == direction;
		}
		catch(Exception ex) {
			return false;
		}
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		switch(node.getAttribute("direction")) {
		case "up":
			direction = GameSprite.UP;
			break;
		case "down":
			direction = GameSprite.DOWN;
			break;
		case "left":
			direction = GameSprite.LEFT;
			break;
		case "right":
			direction = GameSprite.RIGHT;
			break;
		}
	}

}
