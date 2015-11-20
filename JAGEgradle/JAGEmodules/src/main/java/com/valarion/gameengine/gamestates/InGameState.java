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
package com.valarion.gameengine.gamestates;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.Player;
import com.valarion.gameengine.events.menu.ingamemenu.MenuMain;
import com.valarion.gameengine.util.Camera;
import com.valarion.gameengine.util.Util;

/**
 * State containing all the mechanics of the actual game.
 * @author Rubén Tomás Gracia
 *
 */
public class InGameState extends SubState {

	protected Set<Event> activeEvents;

	protected Map<String, File> mapfiles;
	protected Map<String, Set<String>> groups;
	protected Map<String, String> groupreference;
	protected Map<String, SubTiledMap> loadedmaps;
	protected SubTiledMap active;

	protected Player player;

	protected Camera camera;

	@Override
	public void init(GameContainer container) throws Exception {
		activeEvents = Util.getset();

		mapfiles = new HashMap<String, File>();
		groups = new HashMap<String, Set<String>>();
		groupreference = new HashMap<String, String>();
		loadedmaps = new HashMap<String, SubTiledMap>();

		File fXmlFile = new File("config.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		File[] mapArray = new File("res/map").listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".tmx");
			}
		});
		for (File map : mapArray) {
			String mapname = map.getName().substring(0,
					map.getName().lastIndexOf('.'));

			int separator = mapname.indexOf('-');

			if (separator > 0) {
				String group = mapname.substring(0, separator);

				Set<String> groupset = groups.get(group);

				if (groupset == null) {
					groupset = new HashSet<String>();
					groups.put(group, groupset);
				}

				mapname = mapname.substring(separator + 1, mapname.length());

				groupset.add(mapname);
				groupreference.put(mapname, group);
			}

			mapfiles.put(mapname, map);
		}

		if (Database.instance().getContext().getPlayer() == null) {
			player = new Player();
			player.loadEvent(((Element) doc.getFirstChild()), this);
		} else {
			player = Database.instance().getContext().getPlayer();
		}

		Database.instance().getContext().setPlayer(player);

		camera = new Camera(GameCore.getInstance(), this).focusAt(player);

		String mapname;

		if (Database.instance().getContext().getActivemap() != null) {
			mapname = Database.instance().getContext().getActivemap();
		} else {
			mapname = ((Element) doc.getFirstChild()).getAttribute("start");
		}

		setAsActive(container, mapname);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();
		if (!player.isMoving() && activeEvents.size() == 0
				&& input.isKeyPressed(Controls.cancel)) {
			activeEvents.add(new MenuMain(this));
		} else {
			if (active != null) {
				if (activeEvents.size() > 0)
					for (Event e : activeEvents)
						e.paralelupdate(container, delta, active);
				else
					active.update(container, delta);
			}
		}

		input.clearKeyPressedRecord();
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (active != null)
			active.prerender(container, g);
		for (Event e : activeEvents) {
			e.prerender(container, g, active.getTileWidth(),
					active.getTileHeight());
		}
		
		g.pushTransform();
		
		if (camera != null)
			camera.render(container, g);
		if (active != null)
			active.render(container, g,activeEvents);

		g.popTransform();
		
		if (active != null)
			active.postrender(container, g);

		for (Event e : activeEvents) {
			e.postrender(container, g, active.getTileWidth(),
					active.getTileHeight());
		}
	}

	/**
	 * Get camera.
	 * @return
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Remove all loaded maps.
	 */
	protected void removeLoadedMaps() {
		loadedmaps.clear();
	}

	/**
	 * Remove a loaded map group.
	 * @param group
	 */
	protected void removeLoadedGroup(String group) {
		for (String entry : groups.get(group)) {
			loadedmaps.remove(entry);
		}
	}

	/**
	 * Load a group.
	 * @param container
	 * @param group
	 * @throws SlickException
	 */
	protected void loadGroup(GameContainer container, String group)
			throws SlickException {
		Set<String> maps = groups.get(group);
		if (maps != null)
			for (String entry : maps) {
				loadMap(container, entry);
			}
	}

	/**
	 * Load a map.
	 * @param container
	 * @param mapname
	 * @return
	 * @throws SlickException
	 */
	protected SubTiledMap loadMap(GameContainer container, String mapname)
			throws SlickException {
		SubTiledMap map = new SubTiledMap(mapfiles.get(mapname)
				.getAbsolutePath(), GameCore.getInstance(), this);
		map.add(player);
		loadedmaps.put(mapname, map);

		Set<Event> deletions = new HashSet<Event>();

		for (Event e : map.getEvents()) {
			if (e.getId() != null && Database.instance().getContext().getDeletedEvents().contains(e.getId()))
				deletions.add(e);
			else
				e.onMapLoad(container, map);
		}

		for (Event e : deletions) {
			map.remove(e);
		}

		return map;
	}

	/**
	 * Set a map as active.
	 * Loads it if it's necessary.
	 * @param container
	 * @param mapname
	 * @throws SlickException
	 */
	public void setAsActive(GameContainer container, String mapname)
			throws SlickException {
		SubTiledMap prev = active;

		if (prev != null) {
			for (Event e : prev.getEvents()) {
				e.onMapSetAsInactive(container, prev);
			}

			for (Event e : activeEvents) {
				e.onMapSetAsInactive(container, prev);
			}
		}

		active = loadedmaps.get(mapname);
		if (active == null) {
			removeLoadedMaps();
			loadGroup(container, groupreference.get(mapname));
			active = loadedmaps.get(mapname);
			if (active == null) {
				active = loadMap(container, mapname);
			}
			Database.instance().getContext().getDeletedEvents().clear();
		}
		
		active.addDeleteds();

		for (Event e : active.getEvents()) {
			e.onMapSetAsActive(container, active);
		}

		for (Event e : activeEvents) {
			e.onMapSetAsActive(container, active);
		}

		Database.instance().getContext().setActivemap(mapname);
	}

	/**
	 * Get player.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get active map.
	 * @return
	 */
	public SubTiledMap getActive() {
		return active;
	}

	@Override
	public Set<Event> getActiveEvents() {
		return activeEvents;
	}

	
	
	
}
