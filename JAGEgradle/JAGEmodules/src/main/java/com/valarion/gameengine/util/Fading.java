package com.valarion.gameengine.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Fading {
	Color start;
	Color end;
	int time;
	
	Color actual;
	int elapsed;
	
	public Fading(Color start, Color end, int time) {
		this.start = start;
		this.end = end;
		this.time = time;
	}
	
	public void start() {
		elapsed = 0;
		actual = start;
	}

	
	public boolean update(int delta) {
		if(elapsed < time) {
			elapsed += delta;
			float endscale = (float)elapsed/(float)time;
			float startscale = 1.f-endscale;
			actual = start.scaleCopy(startscale).addToCopy(end.scaleCopy(endscale));
			//float r = start.r*startscale + end.r*endscale;
			//float g = start.g*startscale + end.g*endscale;
			//float b = start.b*startscale + end.b*endscale;
			//float a = start.a*startscale + end.a*endscale;
			//actual = new Color(r,g,b,a);
			return false;
		}
		else {
			actual = end;
			return true;
		}
	}
	
	public void render(GameContainer container, Graphics g) {
		Color color = g.getColor();
		g.setColor(actual);
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(color);
	}
	
	public boolean isWorking() {
		return elapsed < time;
	}
}
