/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Rub�n Tom�s Gracia
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
package com.valarion.gameengine.util;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WindowImage {
	Image window;
	Image contain;
	Image merged;
	Animation arrow;
	Window model;
	int x, y;

	boolean showArrow = true;

	public WindowImage(Image window, Image contain, int x, int y,
			Animation arrow, Window model) throws SlickException {
		this.window = window;
		this.contain = contain;
		this.x = x;
		this.y = y;
		this.arrow = arrow;
		this.model = model;

		merged = new Image(window.getWidth(), window.getHeight());

		Graphics g = getContain().getGraphics();
		g.clear();
		g.flush();

		getImage();
	}

	public Image getContain() {
		return contain;
	}

	public Image getWindow() {
		return window;
	}

	public Image getImage() throws SlickException {
		Graphics g = merged.getGraphics();

		g.clear();

		g.drawImage(window, 0, 0);
		g.drawImage(contain, x, y);
		if (showArrow)
			g.drawImage(arrow.getCurrentFrame(), x + contain.getWidth() / 2
					- arrow.getCurrentFrame().getWidth() / 2,
					y + contain.getHeight()
							- arrow.getCurrentFrame().getHeight());
		// g.setDrawMode(Graphics.MODE_NORMAL);
		g.flush();

		return merged;
	}

	public void update(long delta) {
		if (arrow != null)
			arrow.update(delta);
	}

	public Window getModel() {
		return model;
	}

	public boolean isShowArrow() {
		return showArrow;
	}

	public void setShowArrow(boolean showArrow) {
		this.showArrow = showArrow;
	}
}
