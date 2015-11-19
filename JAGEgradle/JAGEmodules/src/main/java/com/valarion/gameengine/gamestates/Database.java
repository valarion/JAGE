package com.valarion.gameengine.gamestates;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.Sound;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.util.SpriteInfo;
import com.valarion.gameengine.util.Window;
import com.valarion.gameengine.util.WindowImage;

public class Database {
	protected GameContext context;

	protected Map<String, SpriteInfo> sprites;
	protected Map<String, Window> windows;
	protected Map<String, Music> musics;
	protected Map<String, Sound> sounds;
	protected Map<String, Image> images;

	protected Map<String, WindowImage> windowimages;
	
	protected String defaultwindow;
	
	protected String titleBackground;
	protected String titleMusic;
	
	protected boolean initialized;
	
	protected static Database instance = new Database();
	
	public static Database createInstance() {
		synchronized(instance) {
			if(!instance.initialized) {
				instance.initialized = true;
				instance.init();
			}
		}
		return instance;
	}
	
	public static Database instance() {
		return instance;
	}

	protected Database() {
		initialized = false;
	}
	
	protected void init() {
		sprites = new HashMap<String, SpriteInfo>();
		musics = new HashMap<String, Music>();
		sounds = new HashMap<String, Sound>();
		images = new HashMap<String, Image>();
		windows = new HashMap<String, Window>();
		windowimages = new HashMap<String, WindowImage>();
		
		try {
			File fXmlFile = new File("config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringComments(true);
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);

			defaultwindow = ((Element) doc.getFirstChild()).getAttribute("window");

			titleBackground = ((Element) doc.getFirstChild()).getAttribute("background");
			titleMusic = ((Element) doc.getFirstChild()).getAttribute("music");

			loadDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadDatabase() throws Exception {
		File fXmlFile = new File("res/database.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringComments(true);
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
	
	public Map<String, SpriteInfo> getSprites() {
		return sprites;
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

	// TODO music and sound control

	public boolean playMusic(String name) {
		Music m = musics.get(name);
		if (m != null) {
			m.play();
			return true;
		} else {
			return false;
		}
	}

	public boolean playMusic(String name, int volume, int pitch) {
		Music m = musics.get(name);
		if (m != null) {
			m.play(pitch, volume);
			return true;
		} else {
			return false;
		}
	}

	public boolean loopMusic(String name) {
		Music m = musics.get(name);
		if (m != null) {
			m.loop();
			return true;
		} else {
			return false;
		}
	}

	public boolean loopMusic(String name, float volume, float pitch) {
		Music m = musics.get(name);
		if (m != null) {
			m.loop(pitch, volume);
			return true;
		} else {
			return false;
		}
	}

	public boolean stopMusic(String name) {
		Music m = musics.get(name);
		if (m != null) {
			m.stop();
			return true;
		} else {
			return false;
		}
	}

	public boolean pauseMusic(String name) {
		Music m = musics.get(name);
		if (m != null) {
			m.pause();
			return true;
		} else {
			return false;
		}
	}

	public boolean resumeMusic(String name) {
		Music m = musics.get(name);
		if (m != null) {
			m.resume();
			return true;
		} else {
			return false;
		}
	}

	public boolean fadeMusic(String name, int duration, float endVolume, boolean stopAfterFade) {
		Music m = musics.get(name);
		if (m != null) {
			m.fade(duration, endVolume, stopAfterFade);
			return true;
		} else {
			return false;
		}
	}

	public boolean setMusicVolume(String name, float volume) {
		Music m = musics.get(name);
		if (m != null) {
			m.setVolume(volume);
			return true;
		} else {
			return false;
		}
	}

	public float getMusicVolume(String name) {
		Music m = musics.get(name);
		if (m != null) {
			return m.getVolume();
		} else {
			return -1.0f;
		}
	}

	public boolean setMusicPosition(String name, float position) {
		Music m = musics.get(name);
		if (m != null) {
			m.setPosition(position);
			return true;
		} else {
			return false;
		}
	}

	public float getMusicPosition(String name) {
		Music m = musics.get(name);
		if (m != null) {
			return m.getPosition();
		} else {
			return -1.0f;
		}
	}

	public boolean isMusicPlaying(String name) {
		Music m = musics.get(name);
		if (m != null) {
			return m.playing();
		} else {
			return false;
		}
	}

	public boolean addMusicListener(String name, MusicListener listener) {
		Music m = musics.get(name);
		if (m != null) {
			m.addListener(listener);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeMusicListener(String name, MusicListener listener) {
		Music m = musics.get(name);
		if (m != null) {
			m.removeListener(listener);
			return true;
		} else {
			return false;
		}
	}

	// sound control

	public boolean playSound(String name) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.play();
			return true;
		} else {
			return false;
		}
	}

	public boolean playSound(String name, int volume, int pitch) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.play(pitch, volume);
			return true;
		} else {
			return false;
		}
	}

	public boolean loopSound(String name) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.loop();
			return true;
		} else {
			return false;
		}
	}

	public boolean loopSound(String name, float volume, float pitch) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.loop(pitch, volume);
			return true;
		} else {
			return false;
		}
	}

	public boolean stopSound(String name) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.stop();
			return true;
		} else {
			return false;
		}
	}

	public boolean isSoundPlaying(String name) {
		Sound s = sounds.get(name);
		if (s != null) {
			return s.playing();
		} else {
			return false;
		}
	}

}
