package com.valarion.gameengine.events.rpgmaker.messages;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;

public class PositionedVarShow extends FlowEventClass {
	String pre,post;
	int var;
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		pre = node.getAttribute("pre");
		var = Integer.parseInt(node.getAttribute("var"));
		post = node.getAttribute("post");
	}
	
	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		String output = pre + Database.instance().getContext().getGlobalVars()[var] + post;
		g.getFont().drawString(
				getEvent().getXDraw(tilewidth),
				getEvent().getYDraw(tilewidth),
				output,
				Color.black);
	}
	
	@Override
	public boolean isWorking() {
		return true;
	}
}
