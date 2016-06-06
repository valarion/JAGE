package com.valarion.gameengine.events.rpgmaker.effect;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;

public class PlayEmote extends FlowEventClass {
	protected Image image;
	protected int duration;
	protected Animation animation;
	protected int elapsed;
	protected String id = null;
	protected boolean player;
	protected SubTiledMap map;
	
	protected Renderable rendered;
	
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		duration = Integer.parseInt(node.getAttribute("time"));
		if(node.hasAttribute("spritesheet") && node.hasAttribute("sprite")) {
			rendered = animation = Database.instance().getSprites().get(node.getAttribute("spritesheet")).createAnimation(node.getAttribute("sprite"), duration);
		}
		else {
			rendered = image = Database.instance().getImages().get(node.getAttribute("image"));
		}
		
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
	}
	
	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		super.performAction(container, map, e);
		this.map = map;
		if(animation != null) {
			animation.setLooping(false);
			animation.setCurrentFrame(0);
			animation.start();
		}
		else {
			elapsed = 0;
		}
	}
	
	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		
		if(animation != null) {
			animation.update(delta);
		}
		else {
			elapsed += delta;
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		Event e;
		try {
			if(id != null) {
				e = map.getEventsById().get(id);
			}
			else if(!player) {
				e = getEvent();
			}
			else {
				e = getState().getPlayer();
			}
		}
		catch(Exception ex) {
			e = null;
		}
		
		if(e != null && animation == null || animation.getCurrentFrame()!=null) {
			float x = e.getXDraw(map.getTileWidth()) + e.getWidth()/2.f - (animation != null?animation.getWidth():image.getWidth())/2.f;
			float y = e.getYDraw(map.getTileWidth())-(animation != null?animation.getHeight():image.getHeight());
			rendered.draw(x, y);
		}
	}
	
	@Override
	public boolean isWorking() {
		if(animation != null) {
			return !animation.isStopped();
		}
		else {
			return elapsed < duration;
		}
	}
}
