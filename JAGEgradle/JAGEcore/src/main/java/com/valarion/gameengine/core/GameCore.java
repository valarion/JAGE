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

//import java.awt.GraphicsDevice;
//import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.valarion.gameengine.core.exceptions.StateNotFoundException;
import com.valarion.gameengine.core.interfaces.ColoredString;
import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.interfaces.GameState;
import com.valarion.gameengine.core.interfaces.Renderable;
import com.valarion.gameengine.core.interfaces.Updatable;
import com.valarion.gameengine.core.interfaces.VarLong;
import com.valarion.gameengine.util.DuplicatedPrintStream;
import com.valarion.pluginsystem.PluginUtil;

/**
 * Main game class. Starts an application, loads the plugins, searchs for an
 * Instance called "StartInstance" and loads it.
 * 
 * @author Rubén Tomás Gracia
 */
public class GameCore extends BasicGame {
	protected String startState;
	protected String gameName;
	protected int screenwidth;
	protected int screenheight;
	protected boolean fullscreen;

	protected AppGameContainer app;
	protected GameState active;
	protected Map<Class<?>, Map<String, Class<?>>> sets;

	protected static GameCore instance;

	public static final String modulesDir = "./modules";
	public static final Class<?>[] classes = new Class<?>[] { GameState.class, Event.class, ColoredString.class,
			Condition.class, VarLong.class, Updatable.class, Renderable.class };

	private GameCore(String gameName, String startState, int screenwidth, int screenheight, boolean fullscreen) {
		super(gameName);
		this.gameName = gameName;
		this.startState = startState;
		this.screenwidth = screenwidth;
		this.screenheight = screenheight;
		this.fullscreen = fullscreen;

		sets = new HashMap<Class<?>, Map<String, Class<?>>>();
	}

	public static void main(String[] arguments) {
		if(instance != null) {
			instance.getApp().exit();
		}
		System.out.println();
		PrintStream out = System.out;
		PrintStream err = System.err;
		try {
			System.out.println("Trying to create logging");
			FileOutputStream fos = new FileOutputStream("lastlog.txt");
			System.out.println("Log created");

			System.setOut(new DuplicatedPrintStream(fos, out, "out - "));
			System.setErr(new DuplicatedPrintStream(fos, err, "err - "));
		} catch (IOException e1) {
			System.err.println("Couldn't set logging");
			e1.printStackTrace();
		}
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());

		int w = 1024;
		int h = 768;
		boolean fs = false;

		try {
			w = Integer.parseInt(arguments[0]);
			h = Integer.parseInt(arguments[1]);
			fs = Boolean.parseBoolean(arguments[2]);
		} catch (Exception e) {
		}
		
		(instance = new GameCore("game", "StartState", w, h, fs)).start();
	}

	/**
	 * Singleton method.
	 * 
	 * @return Singleton instance.
	 */
	public static GameCore getInstance() {
		return instance;
	}

	/**
	 * Starts the engine.
	 */
	public void start() {
		try {
			app = new AppGameContainer(this);
			app.setDisplayMode(screenwidth, screenheight, fullscreen);
			app.setShowFPS(false);
			app.setMouseGrabbed(false);
			app.setAlwaysRender(!fullscreen);

			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) {
		try {
			sets = PluginUtil.loadPlugins(true, modulesDir, classes);

			for (Class<?> instanceClass : sets.get(GameState.class).values()) {
				if (instanceClass.getSimpleName().equals(startState)) {
					active = (GameState) instanceClass.getDeclaredConstructor().newInstance();
					break;
				}
			}

			if (active == null) {
				throw new StateNotFoundException(startState);
			}

			active.init(container);

		} catch (Exception e) {
			Log.error(e);
			app.exit();
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		active.update(container, delta);
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		active.render(container, g);
	}

	/**
	 * Get the app container.
	 * 
	 * @return
	 */
	public AppGameContainer getApp() {
		return app;
	}

	/**
	 * Get active game state.
	 * 
	 * @return
	 */
	public GameState getActive() {
		return active;
	}

	/**
	 * Set active game state.
	 * 
	 * @param active
	 */
	public void setActive(GameState active) {
		this.active = active;
	}

	/**
	 * Get the loaded plugin classes.
	 * 
	 * @return
	 */
	public Map<Class<?>, Map<String, Class<?>>> getSets() {
		return sets;
	}
}
