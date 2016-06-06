package com.valarion.gameengine.eventconditions;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;

public class And implements Condition {
	List<Condition> conditions;
	@Override
	public boolean eval(Event e, GameContainer container, SubTiledMap map) {
		boolean ret = true;
		
		for(Condition condition : conditions) {
			ret = ret && condition.eval(e, container, map);
			if(ret == false)
				break;
		}
		
		return ret;
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();
		
		conditions = new LinkedList<Condition>();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null)) {
				try {
					Condition e = (Condition) game.getSets().get(Condition.class).get(n.getNodeName()).newInstance();
					e.load((Element) n, context);
					conditions.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
