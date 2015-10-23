package com.valarion.pluginsystem;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Class containing all the info from a jar ans its filtered classes. Also contains 
 * methods to load a jar.
 * @author Valarionch <valarionch@gmail.com>
 */
public class PluginContainer {
	protected String path;
	protected String filename;
	protected Manifest manifest;
	protected Map<String,Class<?>> classes;
	
	protected Map<Class<?>,Set<Class<?>>> selected;
	
	protected PluginContainer() {
		classes = new HashMap<String,Class<?>>();
		selected = new HashMap<Class<?>,Set<Class<?>>>();
	}
	
	/**
	 * Selects the classes filtered from a jar.
	 * @param file Jar to search.
	 * @param cl ClassLoader containing all 
	 * @param mustSelect Classes to filter by.
	 * @return PluginContainer containing the classes filtered and other information.
	 * @throws IOException if and I/O error has ocurred.
	 */
	public static PluginContainer loadJar(String file, URLClassLoader cl, Class<?>... mustSelect)  throws IOException {
		int startindex = file.lastIndexOf(File.separatorChar)+1;
		int endindex = file.lastIndexOf('.');
		
		if(endindex <= startindex) {
			return null;
		}
		
		String filename = file.substring(startindex,endindex);
		
		PluginContainer plugin = new PluginContainer();
		plugin.path = file;
		plugin.filename = filename;	
				
		JarFile jarFile = new JarFile(file);
		
		plugin.manifest = jarFile.getManifest();
		
		if(plugin.manifest != null) {
			
		}
		
        Enumeration<JarEntry> e = jarFile.entries();

        
        for(Class<?> filter : mustSelect) {
        	plugin.selected.put(filter,new HashSet<Class<?>>());
        }

        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6).replace('/', '.');
            try {
				Class<?> c = cl.loadClass(className);
				
				for(Class<?> pat : mustSelect) {
					if(pat.isAssignableFrom(c)) {
						plugin.selected.get(pat).add(c);
					}
				}
				
				plugin.classes.put(className,c);
			} catch (ClassNotFoundException e1) {
				
			}

        }
        
        jarFile.close();
        
        return plugin;
	}
	
	/**
	 * Gets all classes from a specific filter. That means, all classes that are subclass
	 * of the specified Class as filter.
	 * @param filter Class filter
	 * @return Class Set with all the classes filtered
	 */
	public Set<Class<?>> getFiltered(Class<?> filter) {
		return selected.get(filter);
	}

	/**
	 * Gets the path where the jar is.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Gets the path to the jar file.
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Gets the manifest of the jar file.
	 */
	public Manifest getManifest() {
		return manifest;
	}
	
	/**
	 * Gets all the classes in the file.
	 */
	public Map<String, Class<?>> getClasses() {
		return classes;
	}
	
	/**
	 * Gets all filter classes along with the filtered ones.
	 */
	public Map<Class<?>, Set<Class<?>>> getSelected() {
		return selected;
	}
}
