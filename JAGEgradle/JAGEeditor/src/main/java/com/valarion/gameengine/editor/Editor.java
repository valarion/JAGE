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
package com.valarion.gameengine.editor;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.util.Log;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.interfaces.ColoredString;
import com.valarion.gameengine.core.interfaces.Condition;
import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.interfaces.VarLong;
import com.valarion.gameengine.editor.XML.DefinableXML;
import com.valarion.pluginsystem.PluginUtil;

/**
 * Event editor user interface. Searches every definable xml class.
 * Creates an editing tree with RootNode as tree.
 * RootNode must be defined in a module.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class Editor {
	
	public Map<Class<?>, Map<String, Class<?>>> getSets() {
		return sets;
	}

	public Map<String, Class<?>> getEventsclasses() {
		return eventsclasses;
	}

	public Map<Class<?>, Set<Class<?>>> getInheritedclasses() {
		return inheritedclasses;
	}

	protected Map<Class<?>, Map<String, Class<?>>> sets;
	
	protected Map<String, Class<?>> eventsclasses;
	
	protected Map<Class<?>,Set<Class<?>>> inheritedclasses;

	protected static Editor instance;
	
	public static Editor instance() {
		return instance;
	}

	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		instance = new Editor(args);
	}
	
	protected Editor(String[] args) {
		inheritedclasses = new HashMap<Class<?>,Set<Class<?>>>();
		try {
			sets = PluginUtil.loadPlugins(true, GameCore.modulesDir, new Class<?>[] {DefinableXML.class});
			
			eventsclasses = sets.get(DefinableXML.class);
			
			inheritedclasses.put(Event.class, new HashSet<Class<?>>());
			inheritedclasses.put(VarLong.class, new HashSet<Class<?>>());
			inheritedclasses.put(Condition.class, new HashSet<Class<?>>());
			inheritedclasses.put(ColoredString.class, new HashSet<Class<?>>());
			
			for(Class<?> c : eventsclasses.values()) {
				inheritedclasses.put(c, new HashSet<Class<?>>());
			}
			
			for(Class<?> child : inheritedclasses.keySet()) {
				if(!child.isInterface() && !Modifier.isAbstract(child.getModifiers())) {
					for(Class<?> parent : inheritedclasses.keySet()) {
						if(parent.isAssignableFrom(child)) {
							inheritedclasses.get(parent).add(child);
						}
					}
				}
			}
			
			EventTreeWindow.main(args);
		} catch (Exception e) {
			Log.error(e);
		}
	}

}
