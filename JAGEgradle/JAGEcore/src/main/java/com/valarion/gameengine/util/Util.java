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

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Class containing different utilities.
 * @author Rubén Tomás Gracia
 *
 */
public class Util {
	/**
	 * Get a concurrent hash set.
	 * It needs to be created from a hash map, due to the jre missing a concurrent hash set.
	 * @return
	 */
	public static <T> Set<T> getset() {
		return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
	}
	
	/**
	 * Get a scaled copy of an image.
	 * Differs from Image.getScaledCopy in that this scales the texture.
	 * This avoids the use of long versions of Graphics.drawImage to set the resize correctly.
	 * @param input
	 * @param w
	 * @param h
	 * @return
	 * @throws SlickException
	 */
	public static Image getScaled(Image input, int w, int h) throws SlickException {
		Image im = new Image(w,h);
		Graphics g = im.getGraphics();
		g.drawImage(input, 0, 0, im.getWidth(), im.getHeight(), 0, 0, input.getWidth(), input.getHeight());
		g.flush();
		return im;
	}
}
