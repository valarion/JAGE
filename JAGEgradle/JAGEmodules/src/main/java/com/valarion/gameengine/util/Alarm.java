package com.valarion.gameengine.util;

public class Alarm {
	protected long time;
	protected long starttime = -1;
	
	public Alarm(long ms) {
		time = ms;
	}
	
	public void start() {
		starttime = System.currentTimeMillis();
	}
	
	public boolean isDone() {
		return System.currentTimeMillis() - starttime > time;
	}
}
