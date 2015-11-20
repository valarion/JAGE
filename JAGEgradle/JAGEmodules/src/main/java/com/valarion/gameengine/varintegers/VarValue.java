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
package com.valarion.gameengine.varintegers;

import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.VarLong;
import com.valarion.gameengine.gamestates.Database;

/**
 * Class that gets a global variable value.
 * @author Rub�n Tom�s Gracia
 *
 */
public class VarValue implements VarLong {
	int var = 0;
	
	
	@Override
	public long getLong() {
		return Database.instance().getContext().getGlobalVars()[var];
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		var = Integer.parseInt(node.getAttribute("var"));
	}

}
