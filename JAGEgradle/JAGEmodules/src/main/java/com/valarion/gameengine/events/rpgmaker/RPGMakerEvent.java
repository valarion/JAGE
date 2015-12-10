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
package com.valarion.gameengine.events.rpgmaker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TileSet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.Condition;
import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.Moving;
import com.valarion.gameengine.events.Player;
import com.valarion.gameengine.events.Route;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.util.GameSprite;

/**
 * Class that describes a page in a game event.
 * @author Rubén Tomás Gracia
 *
 */
public class RPGMakerEvent extends FlowEventClass {
	
	public static final int ONACTIVATION = 0;
	public static final int ONTOUCH = 1;
	public static final int ONBEINGTOUCHED = 2;
	public static final int ONSTART = 3;
	public static final int PARALEL = 4;

	protected int x=-1;
	protected int y=-1;
	
	protected LinkedList<Condition> conditions = new LinkedList<Condition>();
	protected LinkedList<Event> events = new LinkedList<Event>();
	protected Iterator<Event> iterator = null;
	protected Event active = null;
	protected Event activator = null;
	protected boolean working = false;
	protected SubTiledMap map = null;
	
	protected int tileId = -1;
	protected Image tile = null;
	protected GameSprite sprite = null;
	protected float xOff = 0;
	protected float yOff = 0;
	
	protected int count = 0;
	protected boolean moving = false;
	protected int nextmove = -1;
	protected float movingspeed = 0.1f;
	protected float spritespeed = 1.0f;
	protected int movingdirection = 0;
	
	protected int prevdirection = -1;
	
	protected int type = -1;
	
	protected String layer = null;
	
	protected boolean animatedmovement = true;
	protected boolean animated = false;
	protected boolean fixeddirection = false;
	protected boolean ghost = false;
	protected float speed = 1;
	protected int period = Integer.MAX_VALUE;
	protected int movement = FIXED;
	
	protected Route customrouteobject;
	protected Iterator<Integer> customiterator;
	protected boolean customfirst = true;
	protected Route movementrouteobject;
	protected Iterator<Integer> movementiterator;
	protected boolean movementfirst = true;
	protected int ignored = Moving.NOTHING;
	protected int prevignored = Moving.NOTHING;

	protected Iterator<Integer> activeiterator;
	protected Route activeroute;
	
	public static final int FIXED = 0;
	public static final int RANDOMMOVEMENT = 1;
	public static final int TOPLAYER = 2;
	public static final int CUSTOM = 3;
	

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		if (active != null)
			active.update(container, delta, map);
	}

	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if(sprite != null) {
			if(animated || (animatedmovement && moving)) {
				sprite.setMultiplier(1/speed);
				sprite.update(delta);
			}
		}
		
		if (!working && !moving && type == PARALEL) {
			performAction(container, map, null);
		}

		if (working) {
			work(container,delta,map);
		}

			if(moving) {
				move(container,delta,map);
			}
			else if(nextmove != Moving.NOTHING) {
				startmove(container,delta,map);
			}
			else {
				calcnextmove(container,delta,map);
			}
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.prerender(container, g, tilewidth, tileheight);
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if(sprite != null) {
			sprite.draw(getXDraw(tilewidth), getYDraw(tileheight));
		}
		else if(tile != null) {
			g.drawImage(tile, getXDraw(tilewidth), getYDraw(tileheight));
		}
		
		if (active != null)
			active.render(container, g, tilewidth, tileheight);
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
		if (active != null)
			active.postrender(container, g, tilewidth, tileheight);
	}
	
	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map)
			throws SlickException {
		map.setMustupdate(true);
	}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map)
			throws SlickException {
		if(this.map == null) {
			this.map = map;
		}
		if(tileId>=0) {
			TileSet tileset = map.getTileSetByGID(tileId);
			tile = tileset.tiles.getSubImage(tileset.getTileX(tileId), tileset.getTileY(tileId));
		}
		
		if (map.equals(this.map) && !working && !moving && type == ONSTART) {
			performAction(container, map, null);
		}
		
		if(working) {
			map.setMustupdate(false);
		}
	}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map,
			Event e) throws SlickException {
		if (!working && !moving && type == ONACTIVATION) {
			performAction(container, map, e);
		}
	}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (!working && !moving && type == ONTOUCH) {
			performAction(container, map, e);
		}
	}


	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if (!working && !moving && type == ONBEINGTOUCHED) {
			performAction(container, map, e);
		}
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		
		try {
			this.x = Integer.parseInt(node.getAttribute("x"));
			this.y = Integer.parseInt(node.getAttribute("y"));
		} catch (Exception e) {

		}

		String t = node.getAttribute("type");
		
		String direction = node.getAttribute("direction");


		layer = node.getAttribute("layer");

		if ("activation".equals(t)) {
			type = ONACTIVATION;
		} else if ("touch".equals(t)) {
			type = ONTOUCH;
		} else if ("beingtouched".equals(t)) {
			type = ONBEINGTOUCHED;
		} else if ("start".equals(t)) {
			type = ONSTART;
		} else if ("paralel".equals(t)) {
			type = PARALEL;
		} else {
			type = -1;
		}
		
		String mov = node.getAttribute("movement");
		
		if("random".equals(mov)){
			movement = RANDOMMOVEMENT;
		}
		else if("toplayer".equals(mov)){
			movement = TOPLAYER;
		}
		else if("custom".equals(mov)){
			movement = CUSTOM;
		}
		else {
			movement = FIXED;
		}
		
		try {
			period = Integer.parseInt(node.getAttribute("period"));
		}
		catch(Exception e) {
			period = Integer.MAX_VALUE;
		}
		
		try {
			tileId = Integer.parseInt(node.getAttribute("tile"));
		}
		catch(Exception e) {
			tileId = -1;
		}
		
		try {
			speed = Float.parseFloat(node.getAttribute("speed"));
		}
		catch(Exception e) {
			speed = 1;
		}
		
		try {
			sprite = Database.instance().getSprites().get(node.getAttribute("sprite")).createSprite(movingspeed, spritespeed);
		}
		catch(Exception e) {
			sprite = null;
		}
		
		if("up".equals(direction)) {
			setDirection(GameSprite.UP);
		}
		else if("down".equals(direction)) {
			setDirection(GameSprite.DOWN);
		}
		else if("left".equals(direction)) {
			setDirection(GameSprite.LEFT);
		}
		else if("right".equals(direction)) {
			setDirection(GameSprite.RIGHT);
		}
		
		animatedmovement = Boolean.parseBoolean(node.getAttribute("animatedmovement"));
		animated = Boolean.parseBoolean(node.getAttribute("animated"));
		fixeddirection = Boolean.parseBoolean(node.getAttribute("fixeddirection"));
		ghost = Boolean.parseBoolean(node.getAttribute("ghost"));
		
		if(parent instanceof GameEvent) {
			NodeList childs = node.getChildNodes();
			
			for (int i = 0; i < childs.getLength(); i++) {
				Node n = childs.item(i);
				if (n instanceof Element) {
					if("conditions".equals(n.getNodeName())) {
						loadConditions((Element)n,context);
					}
					else if("events".equals(n.getNodeName())) {
						loadEvents((Element)n,context);
					}
					else if("route".equals(n.getNodeName())) {
						customrouteobject = new Route((Element)n,true);
					}
				}
			}
		}
		else {
			loadEvents(node,context);
		}
		
		if(customrouteobject == null)
			customrouteobject = new Route(null,true);
		
		movementrouteobject = new Route(null,false);
	}
	
	protected void loadEvents(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null)) {
				try {
					Event e = (Event) game.getSets().get(Event.class)
							.get(n.getNodeName()).newInstance();
					e.loadEvent((Element) n, this);
					events.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void loadConditions(Element node, Object context) throws SlickException {
		NodeList childs = node.getChildNodes();

		GameCore game = GameCore.getInstance();
		
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && (n.getNodeName() != null)) {
				try {
					Condition e = (Condition) game.getSets().get(Condition.class)
							.get(n.getNodeName()).newInstance();
					e.load((Element) n, context);
					conditions.add(e);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isWorking() {
		return working || moving;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		working = true;
		activator = e;
		map.setMustupdate(false);
		if(type != PARALEL) {
			getState().getActiveEvents().add(this);
		}
		
		Player player = getState().getPlayer();
		
		if(type != ONSTART && type != PARALEL) {
			prevdirection = getDirection();
			if(map.getEvents(getXPos(),getYPos()-1).contains(player)) {
				setDirection(GameSprite.UP);
			}
			else if(map.getEvents(getXPos(),getYPos()+11).contains(player)) {
				setDirection(GameSprite.DOWN);
			}
			else if(map.getEvents(getXPos()-1,getYPos()).contains(player)) {
				setDirection(GameSprite.LEFT);
			}
			else if(map.getEvents(getXPos()+1,getYPos()).contains(player)) {
				setDirection(GameSprite.RIGHT);
			}
		}
	}

	@Override
	public void restart() {
		working = false;
		iterator = null;
		if(prevdirection >= 0) {
			setDirection(prevdirection);
			prevdirection = -1;
		}
		try{
			getState().getActive().setMustupdate(true);
		}
		catch (Exception e) {}
		getState().getActiveEvents().remove(this);
	}
	
	@Override
	public String getLayerName() {
		return layer;
	}

	@Override
	protected void searchAndActiveLabel(String label) {
		Iterator<Event> it = events.iterator();
		
		while(it.hasNext()) {
			Event e = it.next();
			
			if((e instanceof FlowEventInterface) && (((FlowEventInterface)e).hasLabel(label))) {
				iterator = it;
				active = e;
				working = true;
				((FlowEventInterface)e).goToLabel(label);
				break;
			}
		}
	}
	
	public boolean isActive(Event eval, GameContainer container, SubTiledMap map) {
		boolean ret = true;
		
		for(Condition c : conditions){
			ret = ret && c.eval(eval, container,map);
		}
		
		return ret;
	}

	@Override
	public int getXDraw(int tileWidth) {
		return (int) (getXPos() * tileWidth + xOff);
	}

	@Override
	public int getYDraw(int tileHeight) {
		return (int) (getYPos() * tileHeight + yOff);
	}

	@Override
	public int getDirection() {
		if(sprite != null) {
			return sprite.getDirection();
		}
		else {
			return -1;
		}
	}

	@Override
	public void setDirection(int direction) {
		movingdirection = direction;
		if(sprite != null && !fixeddirection) {
			sprite.setDirection(direction);
		}
	}

	@Override
	public int getWidth() {
		if(sprite != null) {
			return sprite.getWidth();
		}
		else if(tile != null) {
			return tile.getWidth();
		}
		return -1;
	}

	@Override
	public int getHeight() {
		if(sprite != null) {
			return sprite.getHeight();
		}
		else if(tile != null) {
			return tile.getHeight();
		}
		return -1;
	}
	
	@Override 
	public boolean isBlocking() {
		return !ghost;
	}
	
	protected void work(GameContainer container, int delta, SubTiledMap map) throws SlickException{
		if (iterator == null) {
			iterator = events.iterator();
		}

		if (active == null) {
			if (iterator.hasNext()) {
				active = iterator.next();
				active.performAction(container, map, activator);
			} else {
				restart();
				return;
			}
		}
		
		if (active != null) {
			if (!active.isWorking()) {
				active = null;
				paralelupdate(container,delta,map);
			} else {
				active.paralelupdate(container, delta, map);
				if (!active.isWorking()) {
					active = null;
					paralelupdate(container,delta,map);
				}
			}
		}
	}
	
	protected void startmove(GameContainer container, int delta, SubTiledMap map)  throws SlickException{
		Player player = getState().getPlayer();
		
		switch(nextmove) {
		case Moving.LOOKUP:
			setDirection(GameSprite.UP);
			
			break;
		case Moving.LOOKDOWN:
			setDirection(GameSprite.DOWN);
			
			break;
		case Moving.LOOKLEFT:
			setDirection(GameSprite.LEFT);
			
			break;
		case Moving.LOOKRIGHT:
			setDirection(GameSprite.RIGHT);
			break;
		case Moving.MOVEUP:
			setDirection(GameSprite.UP);
			if ((ghost && getYPos() > 0) || !map.isBlocked(getXPos(), getYPos() - 1)) {
				map.getEvents(getXPos(), getYPos() - 1).add(getEvent());
				moving = true;

				yOff = 0.0f;
			} else {
				ignored = nextmove;
				if((activeroute == null || activeroute.isCanactivate()) && !player.isMoving() && map.getEvents(getXPos(), getYPos() - 1).contains(player)) {
					player.setDirection(GameSprite.DOWN);
					onEventTouch(container, map, player);
				}
			}
			break;
		case Moving.MOVEDOWN:
			setDirection(GameSprite.DOWN);
			if ((ghost && getYPos() < map.getHeight()-1) ||!map.isBlocked(getXPos(), getYPos() + 1)) {
				map.getEvents(getXPos(), getYPos() + 1).add(getEvent());
				moving = true;

				yOff = 0.0f;

			} else {
				ignored = nextmove;
				if((activeroute == null || activeroute.isCanactivate()) && !player.isMoving() && map.getEvents(getXPos(), getYPos() + 1).contains(player)) {
					player.setDirection(GameSprite.UP);
					onEventTouch(container, map, player);
				}
			}
			break;
		case Moving.MOVELEFT:
			setDirection(GameSprite.LEFT);
			if ((ghost && getXPos() > 0) || !map.isBlocked(getXPos() - 1, getYPos())) {
				map.getEvents(getXPos() - 1, getYPos()).add(getEvent());
				moving = true;

				xOff = 0.0f;
			} else {
				ignored = nextmove;
				if((activeroute == null || activeroute.isCanactivate()) && !player.isMoving() && map.getEvents(getXPos()-1, getYPos()).contains(player)) {
					player.setDirection(GameSprite.RIGHT);
					onEventTouch(container, map, player);
				}
			}
			break;
		case Moving.MOVERIGHT:
			setDirection(GameSprite.RIGHT);
			if ((ghost && getXPos() < map.getWidth()-1) || !map.isBlocked(getXPos() +1, getYPos())) {
				map.getEvents(getXPos() + 1, getYPos()).add(getEvent());
				moving = true;

				xOff = 0.0f;
			} else {
				ignored = nextmove;
				if((activeroute == null || activeroute.isCanactivate()) && !player.isMoving() && map.getEvents(getXPos()+1, getYPos()).contains(player)) {
					player.setDirection(GameSprite.LEFT);
					onEventTouch(container, map, player);
				}
			}
			break;
		}
		
		nextmove = Moving.NOTHING;
	}
	
	protected void move(GameContainer container, int delta, SubTiledMap map)  throws SlickException{
		float multiplier = 1/speed;
		
		switch (movingdirection) {
		case GameSprite.UP:
			yOff -= delta * movingspeed * multiplier;
			if (-yOff >= map.getTileHeight()) {
				yOff = 0.0f;
				map.getEvents(getXPos(), getYPos()).remove(getEvent());
				setYPos(getYPos()-1);
				moving = false;
			}
			break;
		case GameSprite.DOWN:
			yOff += delta * movingspeed * multiplier;

			if (yOff >= map.getTileHeight()) {
				yOff = 0.0f;
				map.getEvents(getXPos(), getYPos()).remove(getEvent());
				setYPos(getYPos()+1);
				moving = false;
			}
			break;
		case GameSprite.LEFT:
			xOff -= delta * movingspeed * multiplier;

			if (-xOff >= map.getTileWidth()) {
				xOff = 0.0f;
				map.getEvents(getXPos(), getYPos()).remove(getEvent());
				setXPos(getXPos()-1);
				moving = false;
			}
			break;
		case GameSprite.RIGHT:
			xOff += delta * movingspeed * multiplier;
			if (xOff >= map.getTileWidth()) {
				xOff = 0.0f;
				map.getEvents(getXPos(), getYPos()).remove(getEvent());
				setXPos(getXPos()+1);
				moving = false;
			}
			break;
		}

		if (moving == false) {
			if(sprite != null)
				sprite.setStopped();
		}
	}
	
	protected void calcnextmove(GameContainer container, int delta, SubTiledMap map)  throws SlickException{
		activeroute = null;
		
		if(movementrouteobject != null && movementrouteobject.getRoute().size() > 0) {
			if(movementfirst || (movementiterator == null && customrouteobject.isLoop())){
				movementiterator = movementrouteobject.getRoute().iterator();
				prevignored = ignored;
				ignored = Moving.NOTHING;
				movementfirst = false;
			}
			
			if(!movementrouteobject.isMustignore() && ignored > Moving.NOTHING) {
				nextmove = ignored;
			}
			else {
				if(!movementiterator.hasNext()){
					movementrouteobject = null;
					ignored = prevignored;
					movementfirst = true;
					movementiterator = null;
					if(!working)
						getState().getActiveEvents().remove(getEvent());
				}
				else {
					nextmove = movementiterator.next();
					activeiterator = movementiterator;
					activeroute = movementrouteobject;
				}
			}
		}
		else if(!working && movement != FIXED) {
			count+=delta;
			if(count > period) {
				count = 0;
				
				if(movement == RANDOMMOVEMENT) {
					Random r = new Random();
					
					nextmove = r.nextInt(4)+Moving.MOVEUP;
				}
				else if(movement == TOPLAYER) {
					Player p = getState().getPlayer();
					
					int xDif = p.getXPos()-getXPos();
					int yDif = p.getYPos()-getYPos();
					
					if(Math.abs(xDif) >= Math.abs(yDif)) {
						if(xDif>0) {
							nextmove = Moving.MOVERIGHT;
						}
						else {
							nextmove = Moving.MOVELEFT;
						}
					}
					else {
						if(yDif>0) {
							nextmove = Moving.MOVEDOWN;
						}
						else {
							nextmove = Moving.MOVEUP;
						}
					}
				}
				else if(movement == CUSTOM) {
					if(customfirst){
						customiterator = customrouteobject.getRoute().iterator();
						customfirst = false;
					}
					
					if(!customrouteobject.isMustignore() && ignored > Moving.NOTHING) {
						nextmove = ignored;
					}
					else if(customiterator != null) {
						if(!customiterator.hasNext()){
							if(customrouteobject.isLoop()) {
								customiterator = customrouteobject.getRoute().iterator();
							}
							else {
								customiterator = null;
							}
						}
						if(customiterator != null && customiterator.hasNext()){
							nextmove = customiterator.next();
							activeiterator = customiterator;
							activeroute = customrouteobject;
						}
					}
					
				}
			}
		}
		
		ignored = Moving.NOTHING;
	}
	
	public void setRoute(Route route) {
		movementrouteobject = route;
		movementiterator = null;
		movementfirst = false;
	}
	
	public Route getRoute() {
		return movementrouteobject;
	}
	
	@Override
	public int getXPos() {
		return x;
	}

	@Override
	public int getYPos() {
		return y;
	}

	@Override
	public void setXPos(int newPos) {
		map.getEvents(x, y).remove(getEvent());
		x = newPos;
		map.add(getEvent());
	}

	@Override
	public void setYPos(int newPos) {
		map.getEvents(x, y).remove(getEvent());
		y = newPos;
		map.add(getEvent());
	}
	
	@Override
	public void setBlocking(boolean blocking) {
		ghost = !blocking;
	}
}

