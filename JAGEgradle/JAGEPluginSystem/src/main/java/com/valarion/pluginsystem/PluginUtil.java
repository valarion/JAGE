package com.valarion.pluginsystem;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utils for getting the jar files from a directory and it's subdirectories
 * @author Valarionch <valarionch@gmail.com>
 */
public class PluginUtil {
	
	/**
	 * Loads all jars from a directory and it's subdirectories and filters classes
	 * of all jars. Also maps the classes loaded by name.
	 * @param shortname If true, String key will be the class name. If false, full name
	 * 					with packages will be used.
	 * @param dir	Directory to load.
	 * @param classes	Classes to filter,
	 * @return A map to filtered classes classified by filter and classname.
	 * @throws DirectoryNotFoundException
	 * @throws IOException
	 * @throws NameConflictException
	 */
	public static HashMap<Class<?>, Map<String, Class<?>>> loadPlugins(boolean shortname, String dir, Class<?>... classes) throws DirectoryNotFoundException, IOException, NameConflictException {
		HashMap<Class<?>, Map<String, Class<?>>> sets = new HashMap<Class<?>, Map<String, Class<?>>>();
		
		File modulesDirs = new File(dir);
		for (Class<?> c : classes) {
			sets.put(c, new HashMap<String, Class<?>>());
		}

		if (!modulesDirs.isDirectory()) {
			throw new DirectoryNotFoundException(
					modulesDirs.getAbsolutePath());
		}
		
		Set<String> jars = getJarsFromFile(modulesDirs, true);
		URL[] urls = convert(jars).toArray(new URL[0]);
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		for (String jar : jars) {
			PluginContainer pluginGames = PluginContainer.loadJar(jar, cl,
					classes);

			for (Class<?> c1 : classes) {
				for (Class<?> c2 : pluginGames.getFiltered(c1)) {
					String name = (shortname?c2.getSimpleName():c2.getName());
					Map<String, Class<?>> dummy = sets.get(c1);
					if(dummy.containsKey(name)) {
						//throw new NameConflictException(name);
						try {
							Method methods[] = c2.getMethods();
							
							for(Method m : methods) {
								if(Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(ClassOverrider.class) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(Class.class) && m.getReturnType().equals(Class.class)) {
									c2 = (Class<?>) m.invoke(null, dummy.get(name));
									dummy.put(name, c2);
									break;
								}
							}
						}
						catch(Exception e){}
					}
					else {
						dummy.put(name, c2);
					}
					
				}
			}
		}
		return sets;
	}
	
	/**
	 * Gets a String Set of paths to jar files from a directory path.
	 * @param path Path to search.
	 * @param recursive Whether or not to search into subdirectories.
	 * @return String Set Containing all the paths from the jars.
	 */
	public static Set<String> getJarsFromPath(String path, boolean recursive) {
		return getJarsFromFile(new File(path), recursive);
	}
	
	/**
	 * Gets a String Set of paths to jar files from a directory File.
	 * @param dir Directory to search.
	 * @param recursive whether or not to search into subdirectories.
	 * @return String Set containing all the paths from the jars.
	 */
	public static Set<String> getJarsFromFile(File dir, boolean recursive) {
		Set<String> ret = new HashSet<String>();
		
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			int point;
			
			for(File file: files) {
				String path = file.getPath();
				if(file.isDirectory() && recursive) {
					ret.addAll(getJarsFromFile(file,true));
				}
				else if(file.isFile() && (point = path.lastIndexOf('.')) > 0 && path.substring(point).equals(".jar")) {
					ret.add(path);
				}
			}
		}
		
		return ret;
	}
	/**
	 * Gets a String Set of paths to jar files from an array of paths.
	 * @param recursive Whether or not to search into subdirectories.
	 * @param paths Paths to search.
	 * @return String Set Containing all the paths from the jars.
	 */
	public static Set<String> getJarsStringsFromPaths(boolean recursive, String... paths) {
		Set<String> ret = new HashSet<String>();
		
		for(String path : paths) {
			ret.addAll(getJarsFromPath(path,recursive));
		}
		
		return ret;
	}
	/**
	 * Gets a URL Set of paths to jar files from an array of paths.
	 * @param recursive Whether or not to search into subdirectories.
	 * @param paths Paths to search.
	 * @return URL Set Containing all the paths from the jars.
	 */
	
	public static Set<URL> getJarsURLsFromPaths(boolean recursive, String... paths) {
		return convert(getJarsStringsFromPaths(recursive,paths));
	}
	
	/**
	 * Returns the URL to a jar file.
	 * @param fileJar file to get the URL.
	 * @return URL to the jar file.
	 * @throws MalformedURLException
	 */
	public static URL getURL(String file) throws MalformedURLException {
		return new URL("jar:file:" + file +"!/");
	}
	
	/**
	 * Utility function that merges sets into one set.
	 * @param sets Sets to merge.
	 * @return Set containing all elements of all sets.
	 */
	@SafeVarargs
	public static <T> Set<T> merge(Set<T>... sets) {
		Set<T> ret = new HashSet<T>();
		
		for(Set<T> set : sets) {
			ret.addAll(set);
		}
		
		return ret;
	}
	
	/**
	 * Converts a jar String Set into a URL set.
	 * @param set Jar String Set.
	 * @return URL Set.
	 */
	public static Set<URL> convert(Set<String> set) {
		Set<URL> ret = new HashSet<URL>();
		
		for(String file : set) {
			try {
				ret.add(getURL(file));
			} catch (MalformedURLException e) {
				// Shouldn't be reached
				e.printStackTrace();
			}
		}
		
		return ret;
	}
}
