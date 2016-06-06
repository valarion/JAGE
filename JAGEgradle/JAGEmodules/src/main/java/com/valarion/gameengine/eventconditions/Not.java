package com.valarion.gameengine.eventconditions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;

public class Not implements Condition {
	Condition condition;
	@Override
	public boolean eval(Event e, GameContainer container, SubTiledMap map) {
		return !condition.eval(e, container, map);
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null)) {
				try {
					Condition e = (Condition) game.getSets().get(Condition.class).get(n.getNodeName()).newInstance();
					e.load((Element) n, context);
					condition = e;
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
