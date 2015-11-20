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

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class containing the info of a sprite.
 * Game sprites should cbe created with this helper.
 * @author Rubén Tomás Gracia
 *
 */
public class SpriteInfo {
	SpriteSheet spritesheet;

	Image[] up;
	Image[] down;
	Image[] left;
	Image[] right;

	String name;

	/**
	 * Load sprite info from XML node.
	 * @param node
	 * @throws SlickException
	 */
	public SpriteInfo(Element node) throws SlickException {
		NodeList tiles = node.getChildNodes();

		name = node.getAttribute("name");

		String filename = node.getAttribute("image");
		int tilewidth = Integer.parseInt(node.getAttribute("tilewidth"));
		int tileheight = Integer.parseInt(node.getAttribute("tileheight"));

		spritesheet = new SpriteSheet("res/sprite/" + filename, tilewidth,
				tileheight);

		for (int i = 0; i < tiles.getLength(); i++) {
			Node e = tiles.item(i);

			if ("up".equals(e.getNodeName())) {
				up = createImage(e);
			} else if ("down".equals(e.getNodeName())) {
				down = createImage(e);
			} else if ("left".equals(e.getNodeName())) {
				left = createImage(e);
			} else if ("right".equals(e.getNodeName())) {
				right = createImage(e);
			}
		}
	}

	/**
	 * Create sprite with the given info.
	 * @param spritespeed
	 * @param movingspeed
	 * @return
	 */
	public GameSprite createSprite(float spritespeed, float movingspeed) {

		int verticalsingleduration = (int) (up[0].getHeight() / movingspeed / 2);

		int horizontalsingleduration = (int) (up[0].getWidth() / movingspeed / 2);

		int[] verticalduration = new int[] { verticalsingleduration,
				verticalsingleduration, verticalsingleduration,
				verticalsingleduration };
		int[] horizontalduration = new int[] { horizontalsingleduration,
				horizontalsingleduration, horizontalsingleduration,
				horizontalsingleduration };

		return new GameSprite(new Animation(up, verticalduration, false),
				new Animation(down, verticalduration, false), new Animation(
						left, horizontalduration, false), new Animation(right,
						horizontalduration, false), spritespeed, movingspeed,
				name);
	}

	/**
	 * Create sub image of a spritesheet given an XML node.
	 * @param node
	 * @return
	 */
	protected Image[] createImage(Node node) {
		Image[] ret = new Image[4];

		NodeList nodes = node.getChildNodes();

		try {
			int j = 0;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if ("tile".equals(n.getNodeName())) {
					Element e = (Element) nodes.item(i);
					ret[j++] = spritesheet.getSubImage(
							Integer.parseInt(e.getAttribute("x")),
							Integer.parseInt(e.getAttribute("y")));
				}
			}
		} catch (Exception e) {
			return null;
		}

		return ret;
	}

	/**
	 * Get name of the sprite.
	 * @return
	 */
	public String getName() {
		return name;
	}
}
