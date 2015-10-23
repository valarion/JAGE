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

import org.newdawn.slick.Input;

public class Keyboard {
	public static String getChar(Input input) {
		String ret = "";
		if (input.isKeyPressed(Input.KEY_A))
			ret = "a";
		else if (input.isKeyPressed(Input.KEY_B))
			ret = "b";
		else if (input.isKeyPressed(Input.KEY_C))
			ret = "c";
		else if (input.isKeyPressed(Input.KEY_D))
			ret = "d";
		else if (input.isKeyPressed(Input.KEY_E))
			ret = "e";
		else if (input.isKeyPressed(Input.KEY_F))
			ret = "f";
		else if (input.isKeyPressed(Input.KEY_G))
			ret = "g";
		else if (input.isKeyPressed(Input.KEY_H))
			ret = "h";
		else if (input.isKeyPressed(Input.KEY_I))
			ret = "i";
		else if (input.isKeyPressed(Input.KEY_J))
			ret = "j";
		else if (input.isKeyPressed(Input.KEY_K))
			ret = "k";
		else if (input.isKeyPressed(Input.KEY_L))
			ret = "l";
		else if (input.isKeyPressed(Input.KEY_M))
			ret = "m";
		else if (input.isKeyPressed(Input.KEY_N))
			ret = "n";
		else if (input.isKeyPressed(Input.KEY_O))
			ret = "o";
		else if (input.isKeyPressed(Input.KEY_P))
			ret = "p";
		else if (input.isKeyPressed(Input.KEY_Q))
			ret = "q";
		else if (input.isKeyPressed(Input.KEY_R))
			ret = "r";
		else if (input.isKeyPressed(Input.KEY_S))
			ret = "s";
		else if (input.isKeyPressed(Input.KEY_T))
			ret = "t";
		else if (input.isKeyPressed(Input.KEY_U))
			ret = "u";
		else if (input.isKeyPressed(Input.KEY_V))
			ret = "v";
		else if (input.isKeyPressed(Input.KEY_W))
			ret = "w";
		else if (input.isKeyPressed(Input.KEY_X))
			ret = "x";
		else if (input.isKeyPressed(Input.KEY_Y))
			ret = "y";
		else if (input.isKeyPressed(Input.KEY_Z))
			ret = "z";
		else if (input.isKeyPressed(Input.KEY_SPACE))
			ret = " ";
		else if (input.isKeyPressed(Input.KEY_0))
			ret = "0";
		else if (input.isKeyPressed(Input.KEY_1))
			ret = "1";
		else if (input.isKeyPressed(Input.KEY_2))
			ret = "2";
		else if (input.isKeyPressed(Input.KEY_3))
			ret = "3";
		else if (input.isKeyPressed(Input.KEY_4))
			ret = "4";
		else if (input.isKeyPressed(Input.KEY_5))
			ret = "5";
		else if (input.isKeyPressed(Input.KEY_6))
			ret = "6";
		else if (input.isKeyPressed(Input.KEY_7))
			ret = "7";
		else if (input.isKeyPressed(Input.KEY_8))
			ret = "8";
		else if (input.isKeyPressed(Input.KEY_9))
			ret = "9";

		if (input.isKeyDown(Input.KEY_LSHIFT)
				|| input.isKeyDown(Input.KEY_RSHIFT))
			return ret.toUpperCase();
		else
			return ret;
	}
}
