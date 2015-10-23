package com.valarion.pluginsystem;

public class DirectoryNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1028741701800179550L;
	
	public DirectoryNotFoundException(String dir) {
		super(dir);
	}

}
