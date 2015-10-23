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
package com.valarion.gameengine.events;

public abstract class Moving {
	
	public static final int NOTHING = -1;
	public static final int LOOKUP = 0;
	public static final int LOOKDOWN = 1;
	public static final int LOOKLEFT = 2;
	public static final int LOOKRIGHT = 3;
	public static final int MOVEUP = 4;
	public static final int MOVEDOWN = 5;
	public static final int MOVELEFT = 6;
	public static final int MOVERIGHT = 7;
	public static final int MOVERANDOM = 8;
	public static final int FOLLOW = 9;
	public static final int RUNAWAY = 10;
	public static final int FORWARD = 11;
	public static final int BACKWARD = 12;
	public static final int JUMP = 13;
	public static final int WAIT = 14;
	public static final int TURNLEFT = 15;
	public static final int TURNRIGHT = 16;
	public static final int TURNRANDOM = 17;
	public static final int TURNOVER = 18;
	public static final int RANDOMCOURSE = 19;
	public static final int LOOK = 20;
	public static final int DONTLOOK = 21;
	public static final int ENABLEINTERRUPT = 22;
	public static final int DISABLEINTERRUPT = 23;
	public static final int SETSPEED = 24;
	public static final int SETFREQ = 25;
	public static final int ENABLEMOVINGANIM = 26;
	public static final int DISABLEMOVINGANIM = 27;
	public static final int ENABLESTANDINGANIM = 28;
	public static final int DISABLESTANDINGANIM = 29;
	public static final int ENABLESTILLDIRECTION = 30;
	public static final int DISABLESTILLDIRECTION = 31;
	public static final int ENABLEGHOST = 32;
	public static final int DISABLEGHOST = 33;
}
