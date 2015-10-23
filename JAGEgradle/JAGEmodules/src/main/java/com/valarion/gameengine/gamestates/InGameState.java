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
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.Player;
import com.valarion.gameengine.events.menu.ingamemenu.MenuMain;
import com.valarion.gameengine.util.Camera;
import com.valarion.gameengine.util.SpriteInfo;
import com.valarion.gameengine.util.Util;
import com.valarion.gameengine.util.Window;
import com.valarion.gameengine.util.WindowImage;

public class InGameState extends SubState {

	protected Set<Event> activeEvents;

	protected GameContext context;

	protected Map<String, File> mapfiles;
	protected Map<String, Set<String>> groups;
	protected Map<String, String> groupreference;
	protected Map<String, SubTiledMap> loadedmaps;
	protected SubTiledMap active;

	protected Map<String, SpriteInfo> sprites;
	protected Map<String, Window> windows;
	protected Map<String, Music> musics;
	protected Map<String, Sound> sounds;
	protected Map<String, Image> images;

	protected Map<String, WindowImage> windowimages;

	protected Player player;

	protected Camera camera;

	protected String defaultwindow;

	protected static InGameState instance;

	protected String titleBackground;
	protected String titleMusic;

	protected InGameState() {
		sprites = new HashMap<String, SpriteInfo>();
		musics = new HashMap<String, Music>();
		sounds = new HashMap<String, Sound>();
		images = new HashMap<String, Image>();
		windows = new HashMap<String, Window>();
		windowimages = new HashMap<String, WindowImage>();

		try {
			File fXmlFile = new File("config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);

			defaultwindow = ((Element) doc.getFirstChild())
					.getAttribute("window");

			titleBackground = ((Element) doc.getFirstChild())
					.getAttribute("background");
			titleMusic = ((Element) doc.getFirstChild()).getAttribute("music");

			loadDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static InGameState createInstance() {
		return (instance = new InGameState());
	}

	public static InGameState getInstance() {
		return instance;
	}

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

		if (context.getPlayer() == null) {
			player = new Player();
			player.loadEvent(((Element) doc.getFirstChild()), this);
		} else {
			player = context.getPlayer();
		}

		context.setPlayer(player);

		camera = new Camera(GameCore.getInstance(), this).focusAt(player);

		String mapname;

		if (context.getActivemap() != null) {
			mapname = context.getActivemap();
		} else {
			mapname = ((Element) doc.getFirstChild()).getAttribute("start");
		}

		setAsActive(container, mapname);
	}

	public void loadDatabase() throws Exception {
		File fXmlFile = new File("res/database.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		NodeList spritelist = doc.getElementsByTagName("spritesheet");

		for (int i = 0; i < spritelist.getLength(); i++) {
			Element sprite = (Element) spritelist.item(i);
			SpriteInfo spriteinfo = new SpriteInfo(sprite);
			sprites.put(spriteinfo.getName(), spriteinfo);
		}

		NodeList windowlist = doc.getElementsByTagName("window");

		for (int i = 0; i < windowlist.getLength(); i++) {
			Element window = (Element) windowlist.item(i);
			Window windowinfo = new Window(window);
			windows.put(windowinfo.getName(), windowinfo);
		}

		NodeList musiclist = doc.getElementsByTagName("music");

		for (int i = 0; i < musiclist.getLength(); i++) {
			Element musicelement = (Element) musiclist.item(i);
			Music music = new Music("res/music/"
					+ musicelement.getAttribute("src"));
			musics.put(musicelement.getAttribute("name"), music);
		}

		NodeList soundlist = doc.getElementsByTagName("sound");

		for (int i = 0; i < soundlist.getLength(); i++) {
			Element soundelement = (Element) soundlist.item(i);
			Sound sound = new Sound("res/sound/"
					+ soundelement.getAttribute("src"));
			sounds.put(soundelement.getAttribute("name"), sound);
		}

		NodeList imagelist = doc.getElementsByTagName("image");

		for (int i = 0; i < imagelist.getLength(); i++) {
			Element imageelement = (Element) imagelist.item(i);
			Image image = new Image("res/" + imageelement.getAttribute("src"));
			try {
				int x = Integer.parseInt(imageelement.getAttribute("x"));
				int y = Integer.parseInt(imageelement.getAttribute("y"));
				int w = Integer.parseInt(imageelement.getAttribute("w"));
				int h = Integer.parseInt(imageelement.getAttribute("h"));

				image = image.getSubImage(x, y, w, h);
			} catch (Exception e) {

			}
			images.put(imageelement.getAttribute("name"), image);
		}

		Window w = windows.get(defaultwindow);

		windowimages.put("dialog", w.createWindow(GameCore.getInstance().getApp()
				.getWidth(), GameCore.getInstance().getApp().getHeight() / 4,
				true));

		windowimages.put("loadtop", w.createWindow(GameCore.getInstance().getApp()
				.getWidth(), GameCore.getInstance().getApp().getHeight() / 9,
				true));

		windowimages.put("loadpart", w.createWindow(GameCore.getInstance().getApp()
				.getWidth(),
				GameCore.getInstance().getApp().getHeight() / 9 * 2, true));

		int top = w.getImage("top").getHeight();
		int bottom = w.getImage("bottom").getHeight();

		for (int i = 1; i <= 10; i++) {
			windowimages.put(
					Integer.toString(i),
					w.createWindow(GameCore.getInstance().getApp().getWidth() / 6, top
							+ bottom
							+ i
							* GameCore.getInstance().getApp().getGraphics().getFont()
									.getLineHeight() * 2, true));
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();
		if (!player.isMoving() && activeEvents.size() == 0
				&& input.isKeyPressed(Controls.cancel)) {
			activeEvents.add(new MenuMain());
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

	public Camera getCamera() {
		return camera;
	}

	public void removeLoadedMaps() {
		loadedmaps.clear();
	}

	public void removeLoadedGroup(String group) {
		for (String entry : groups.get(group)) {
			loadedmaps.remove(entry);
		}
	}

	public void loadGroup(GameContainer container, String group)
			throws SlickException {
		Set<String> maps = groups.get(group);
		if (maps != null)
			for (String entry : maps) {
				loadMap(container, entry);
			}
	}

	private SubTiledMap loadMap(GameContainer container, String mapname)
			throws SlickException {
		SubTiledMap map = new SubTiledMap(mapfiles.get(mapname)
				.getAbsolutePath(), GameCore.getInstance());
		map.add(player);
		loadedmaps.put(mapname, map);

		Set<Event> deletions = new HashSet<Event>();

		for (Event e : map.getEvents()) {
			if (e.getId() != null && context.getDeletedEvents().contains(e.getId()))
				deletions.add(e);
			else
				e.onMapLoad(container, map);
		}

		for (Event e : deletions) {
			map.remove(e);
		}

		return map;
	}

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
			context.getDeletedEvents().clear();
		}
		
		active.addDeleteds();

		for (Event e : active.getEvents()) {
			e.onMapSetAsActive(container, active);
		}

		for (Event e : activeEvents) {
			e.onMapSetAsActive(container, active);
		}

		context.setActivemap(mapname);
	}

	public Map<String, SpriteInfo> getSprites() {
		return sprites;
	}

	public Player getPlayer() {
		return player;
	}

	public SubTiledMap getActive() {
		return active;
	}

	public Map<String, Window> getWindows() {
		return windows;
	}

	public Map<String, WindowImage> getWindowimages() {
		return windowimages;
	}

	public GameContext getContext() {
		return context;
	}

	public Set<Event> getActiveEvents() {
		return activeEvents;
	}

	public void setContext(GameContext context) {
		this.context = context;
	}

	public Map<String, Image> getImages() {
		return images;
	}

	public String getTitleBackground() {
		return titleBackground;
	}

	public String getTitleMusic() {
		return titleMusic;
	}
	
	//TODO music and sound control
	
	public boolean playMusic(String name) {
		Music m = musics.get(name);
		if(m != null) {
			m.play();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean playMusic(String name, int volume, int pitch) {
		Music m = musics.get(name);
		if(m != null) {
			m.play(pitch,volume);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean loopMusic(String name) {
		Music m = musics.get(name);
		if(m != null) {
			m.loop();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean loopMusic(String name, float volume, float pitch) {
		Music m = musics.get(name);
		if(m != null) {
			m.loop(pitch,volume);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean stopMusic(String name) {
		Music m = musics.get(name);
		if(m != null) {
			m.stop();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean pauseMusic(String name) {
		Music m = musics.get(name);
		if(m != null) {
			m.pause();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean resumeMusic(String name) {
		Music m = musics.get(name);
		if(m != null) {
			m.resume();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean fadeMusic(String name, int duration,float endVolume, boolean stopAfterFade) {
		Music m = musics.get(name);
		if(m != null) {
			m.fade(duration, endVolume, stopAfterFade);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean setMusicVolume(String name, float volume) {
		Music m = musics.get(name);
		if(m != null) {
			m.setVolume(volume);
			return true;
		}
		else {
			return false;
		}
	}
	
	public float getMusicVolume(String name) {
		Music m = musics.get(name);
		if(m != null) {
			return m.getVolume();
		}
		else {
			return -1.0f;
		}
	}
	
	public boolean setMusicPosition(String name, float position) {
		Music m = musics.get(name);
		if(m != null) {
			m.setPosition(position);
			return true;
		}
		else {
			return false;
		}
	}
	
	public float getMusicPosition(String name) {
		Music m = musics.get(name);
		if(m != null) {
			return m.getPosition();
		}
		else {
			return -1.0f;
		}
	}
	
	public boolean isMusicPlaying(String name) {
		Music m = musics.get(name);
		if(m != null) {
			return m.playing();
		}
		else {
			return false;
		}
	}
	
	public boolean addMusicListener(String name, MusicListener listener) {
		Music m = musics.get(name);
		if(m != null) {
			m.addListener(listener);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean removeMusicListener(String name, MusicListener listener) {
		Music m = musics.get(name);
		if(m != null) {
			m.removeListener(listener);
			return true;
		}
		else {
			return false;
		}
	}
	
	//sound control
	
	public boolean playSound(String name) {
		Sound s = sounds.get(name);
		if(s != null) {
			s.play();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean playSound(String name, int volume, int pitch) {
		Sound s = sounds.get(name);
		if(s != null) {
			s.play(pitch,volume);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean loopSound(String name) {
		Sound s = sounds.get(name);
		if(s != null) {
			s.loop();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean loopSound(String name, float volume, float pitch) {
		Sound s = sounds.get(name);
		if(s != null) {
			s.loop(pitch,volume);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean stopSound(String name) {
		Sound s = sounds.get(name);
		if(s != null) {
			s.stop();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isSoundPlaying(String name) {
		Sound s = sounds.get(name);
		if(s != null) {
			return s.playing();
		}
		else {
			return false;
		}
	}	
	
}
