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
package com.valarion.gameengine.util;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Window {

	protected String name;

	protected Map<String, Image> images;

	int border;

	public Window(Element node) throws SlickException {
		NodeList nodes = node.getChildNodes();

		images = new HashMap<String, Image>();

		name = node.getAttribute("name");

		Image full = new Image("res/window/" + node.getAttribute("image"));

		border = Integer.parseInt(node.getAttribute("border"));

		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if ((n instanceof Element) && ("sub".equals(n.getNodeName()))) {
				Element e = (Element) n;

				int x = Integer.parseInt(e.getAttribute("x"));
				int y = Integer.parseInt(e.getAttribute("y"));
				int w = Integer.parseInt(e.getAttribute("w"));
				int h = Integer.parseInt(e.getAttribute("h"));

				Image im = full.getSubImage(x, y, w, h);

				images.put(e.getAttribute("name"), im);
			}
		}
	}

	public WindowImage createWindow(int w, int h, boolean fix)
			throws SlickException {
		Image ret = new Image(w, h);
		Image front = new Image(w, h);
		Image back = new Image(w, h);
		Graphics r = ret.getGraphics();
		Graphics f = front.getGraphics();
		Graphics b = back.getGraphics();
		Image background = images.get("background");
		Image decoration = images.get("decoration");
		Image topleft = images.get("topleft");
		Image topright = images.get("topright");
		Image bottomleft = images.get("bottomleft");
		Image bottomright = images.get("bottomright");
		Image top = images.get("top");
		Image bottom = images.get("bottom");
		Image left = images.get("left");
		Image right = images.get("right");

		Image arrow1 = images.get("arrow1");
		Image arrow2 = images.get("arrow2");
		Image arrow3 = images.get("arrow3");
		Image arrow4 = images.get("arrow4");

		int twcovered, lhcovered, bwcovered, rhcovered;

		Image background2 = Util.getScaled(background,background.getWidth(), h);

		// Draw background
		for (int i = 0; i < w; i += background.getWidth()) {
			b.drawImage(background2, i, 0);
		}

		for (int i = 0; i < w; i += decoration.getWidth()) {
			for (int j = 0; j < h; j += decoration.getHeight()) {
				b.drawImage(decoration, i, j);
			}
		}

		b.flush();

		back.setAlpha(50);

		f.drawImage(topleft, 0, 0);

		for (twcovered = topleft.getWidth(); twcovered + top.getWidth() <= w
				- topright.getWidth(); twcovered += top.getWidth()) {
			f.drawImage(top, twcovered, 0);
		}

		f.drawImage(topright, twcovered, 0);

		for (lhcovered = topleft.getHeight(); lhcovered + left.getHeight() <= h
				- bottomleft.getHeight(); lhcovered += left.getHeight()) {
			f.drawImage(left, 0, lhcovered);
		}

		f.drawImage(bottomleft, 0, lhcovered);

		for (bwcovered = bottomleft.getWidth(); bwcovered + bottom.getWidth() <= w
				- bottomright.getWidth(); bwcovered += bottom.getWidth()) {
			f.drawImage(bottom, bwcovered, lhcovered);
		}

		for (rhcovered = topright.getHeight(); rhcovered + right.getHeight() <= h
				- bottomright.getHeight(); rhcovered += right.getHeight()) {
			f.drawImage(right, twcovered, rhcovered);
		}

		f.drawImage(bottomright, bwcovered, rhcovered);

		twcovered += topright.getWidth();
		lhcovered += bottomleft.getHeight();
		bwcovered += bottomright.getWidth();
		rhcovered += bottomright.getHeight();

		if ((twcovered != bwcovered) || (lhcovered != rhcovered))
			return null;

		f.flush();

		r.drawImage(back, border, border, twcovered - border, lhcovered
				- border, border, border, twcovered - border, lhcovered
				- border);
		r.drawImage(front, 0, 0);
		r.flush();

		if (twcovered != w || lhcovered != h) {
			ret = ret.getSubImage(0, 0, twcovered, lhcovered);

			if (fix) {
				ret = ret.getScaledCopy(w, h);
			}
		}

		Image[] arrow = { arrow1, arrow2, arrow3, arrow4 };
		int[] duration = { 100, 100, 100, 100 };

		Animation arrowanim = new Animation(arrow, duration, false);

		return new WindowImage(ret, new Image(twcovered - left.getWidth()
				- right.getWidth(), lhcovered - top.getHeight()
				- bottom.getHeight()), left.getWidth(), top.getHeight(),
				arrowanim, this);
	}

	public Image getImage(String name) {
		return images.get(name);
	}

	public String getName() {
		return name;
	}
}
