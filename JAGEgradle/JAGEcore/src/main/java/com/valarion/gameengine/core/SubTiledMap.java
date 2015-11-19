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
package com.valarion.gameengine.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.valarion.gameengine.util.Util;


/**
 * Manages a tiled editor map loading.
 * @author Rubén Tomás Gracia
 * 
 */
public class SubTiledMap extends TiledMap implements Updatable {

	protected boolean[][] blocked;

	protected int[][] types;
	protected Map<String, Integer> typesmap;
	protected Map<Integer, Boolean> blockedtypes;
	protected int lastTypeId;
	protected GameCore game;

	protected Map<String, Set<Event>> eventsByLayer;
	protected Map<String, Event> eventsById;
	protected Set<Event>[][] eventsByTile;
	protected Set<Event> events;
	
	protected Set<Event> tempdeleteds;

	protected boolean mustupdate;

	@SuppressWarnings("unchecked")
	public SubTiledMap(String ref, GameCore game, Object additional) throws SlickException {
		super(ref);

		this.game = game;

		typesmap = new HashMap<String, Integer>();
		blockedtypes = new HashMap<Integer, Boolean>();
		eventsByLayer = new HashMap<String, Set<Event>>();
		eventsById = new HashMap<String, Event>();
		eventsByTile = new Set[getWidth()][getHeight()];
		events = Util.getset();
		tempdeleteds = Util.getset();
		blocked = new boolean[getWidth()][getHeight()];
		types = new int[getWidth()][getHeight()];

		mustupdate = true;

		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				eventsByTile[i][j] = Util.getset();
			}
		}

		typesmap.put("none", 0);

		int tlindex = getLayerIndex("types");
		int lastTypeId = 0;

		// build a collision map based on tile properties in the TileD map

		if (tlindex >= 0)
			for (int xAxis = 0; xAxis < getWidth(); xAxis++) {
				for (int yAxis = 0; yAxis < getHeight(); yAxis++) {
					int tileID = getTileId(xAxis, yAxis, tlindex);
					String value = getTileProperty(tileID, "blocked", "false");

					if ("true".equals(value)) {
						blocked[xAxis][yAxis] = true;
					}

					int typeId = getTypeId(
							getTileProperty(tileID, "type", "none"), lastTypeId);

					if (typeId > lastTypeId) {
						lastTypeId = typeId;
					}

					types[xAxis][yAxis] = typeId;

					blockedtypes.put(types[xAxis][yAxis], "true".equals(value));
				}
			}

		loadEvents(ref.substring(0, ref.length() - 3) + "xml", additional);
	}

	/**
	 * Load events in the map
	 * @param filename Map filename.
	 * @param additional Additional object to pass to the events load method.
	 * @throws SlickException
	 */
	public void loadEvents(String filename, Object additional) throws SlickException {
		try {
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			dbFactory.setIgnoringComments(true);
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList childs = doc.getFirstChild().getChildNodes();

			for (int i = 0; i < childs.getLength(); i++) {
				Node n = childs.item(i);
				if ((n instanceof Element) && n.getNodeName() != null) {
					try {
						Map<String, Class<?>> map = game.getSets().get(Event.class);
						Class<?> c = map.get(n.getNodeName());
						Event e = (Event) c.newInstance();
						e.loadEvent((Element) n, additional);
						add(e);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the name of a layer by index.
	 * @param index
	 * @return
	 */
	public String getLayerName(int index) {
		return ((Layer) layers.get(index)).name;
	}

	/**
	 * create type if doesn't exist and return the id.
	 * @param type
	 * @param prevId
	 * @return
	 */
	protected int getTypeId(String type, int prevId) {
		Integer ret = typesmap.get(type);

		if (ret == null) {
			typesmap.put(type, ++prevId);
			return prevId;
		} else {
			return ret.intValue();
		}
	}

	/**
	 * Check if a tile is blocked.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isBlocked(int x, int y) {
		if (x < 0 || x >= blocked.length || y < 0 || y >= blocked[0].length) {
			return true;
		}

		boolean eventblocked = false;

		for (Event e : eventsByTile[x][y]) {
			eventblocked = e.isBlocking();
			if (eventblocked)
				break;
		}
		return eventblocked || blocked[x][y];
	}

	/**
	 * Set if a tile is blocked.
	 * @param x
	 * @param y
	 * @param state
	 */
	public void setBlocked(int x, int y, boolean state) {
		blocked[x][y] = state;
	}

	/**
	 * Set a tile to the default state.
	 * @param x
	 * @param y
	 */
	public void setToDefault(int x, int y) {
		blocked[x][y] = blockedtypes.get(types[x][y]);
	}

	/**
	 * Prerendering, for backgrounds.
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public void prerender(GameContainer container, Graphics g)
			throws SlickException {
		for (Event event : events) {
			event.prerender(container, g, getTileWidth(), getTileHeight());
		}
	}

	/**
	 * Actual rendering of game.
	 * @param container
	 * @param g
	 * @param common
	 * @throws SlickException
	 */
	public void render(GameContainer container, Graphics g, Set<Event> common)
			throws SlickException {

		for (int i = 0; i < getLayerCount(); i++) {
			String layername = getLayerName(i);
			if (!layername.equals("types")) {
				render(0, 0, i);

				Set<Event> set = eventsByLayer.get(layername);
				if (set != null)
					for (Event event : set) {
						event.render(container, g, getTileWidth(),
								getTileHeight());
					}
				
				for (Event event : common) {
					if(layername.equals(event.getLayerName()))
						event.render(container, g, getTileWidth(),
								getTileHeight());
				}
			}
		}
	}

	/**
	 * Postrendering, for GUIs.
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public void postrender(GameContainer container, Graphics g)
			throws SlickException {

		for (Event event : events) {
			event.postrender(container, g, getTileWidth(), getTileHeight());
		}
	}

	/**
	 * Add event to the map.
	 * @param event
	 */
	public void add(Event event) {
		Set<Event> eventslayer = eventsByLayer.get(event.getLayerName());

		if (eventslayer == null) {
			eventslayer = Util.getset();
			eventsByLayer.put(event.getLayerName(), eventslayer);
		}

		try {
			eventsByTile[event.getXPos()][event.getYPos()].add(event);
		} catch (IndexOutOfBoundsException e) {
		}

		String id = event.getId();

		if (id != null) {
			eventsById.put(id, event);
		}

		eventslayer.add(event);

		events.add(event);
	}

	/**
	 * Remove event from the map.
	 * @param event
	 */
	public void remove(Event event) {
		Set<Event> eventslayer = eventsByLayer.get(event.getLayerName());

		if (eventslayer != null) {
			eventslayer.remove(event);
		}

		try {
			eventsByTile[event.getXPos()][event.getYPos()].remove(event);
		} catch (IndexOutOfBoundsException e) {
		}

		events.remove(event);
	}
	
	/**
	 * Temporary delete an event.
	 * Event will be present next time map is loaded.
	 * @param e
	 */
	public void tempDelete(Event e) {
		if(events.contains(e)) {
			remove(e);
			tempdeleteds.add(e);
		}
	}
	
	/**
	 * Add temporary deleted events to the map again.
	 */
	public void addDeleteds() {
		for(Event e : tempdeleteds) {
			add(e);
		}
		tempdeleteds.clear();
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		for (Event event : events) {
			event.paralelupdate(container, delta, this);

		}
		if (mustupdate) {
			for (Event event : events) {
				event.update(container, delta, this);
			}
		}
	}

	/**
	 * Get events in a layer.
	 * @param layer
	 * @return
	 */
	public Set<Event> getEvents(String layer) {
		return eventsByLayer.get(layer);
	}

	/**
	 * Get events in a tile.
	 * @param x
	 * @param y
	 * @return
	 */
	public Set<Event> getEvents(int x, int y) {
		try {
			return eventsByTile[x][y];
		} catch (IndexOutOfBoundsException e) {
			return new HashSet<Event>();
		}
	}

	/**
	 * Get all the events.
	 * @return
	 */
	public Set<Event> getEvents() {
		return events;
	}

	/**
	 * check if the map must be updated.
	 * @return
	 */
	public boolean isMustupdate() {
		return mustupdate;
	}

	/**
	 * Set if the map must be updated.
	 * @param mustupdate
	 */
	public void setMustupdate(boolean mustupdate) {
		this.mustupdate = mustupdate;
	}

	/**
	 * Get game core instance.
	 * @return
	 */
	public GameCore getGame() {
		return game;
	}

	/**
	 * Get map containing events referenced by id.
	 * @return
	 */
	public Map<String, Event> getEventsById() {
		return eventsById;
	}
}
