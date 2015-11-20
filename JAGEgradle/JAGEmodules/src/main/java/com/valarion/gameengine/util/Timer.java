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

import java.io.Serializable;

/**
 * Class containing a simple timer.
 * @author Rubén Tomás Gracia
 *
 */
public class Timer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6008093945017799941L;
	protected long starttime, endtime;
	
	/**
	 * Create timer.
	 */
	public Timer() {
		starttime=0;
		endtime=0;
	}
	
	/**
	 * start timer.
	 */
	public void start() {
		starttime = System.currentTimeMillis();
		endtime=starttime;
	}
	
	/**
	 * stop timer.
	 */
	public void stop() {
		endtime = System.currentTimeMillis();
	}
	
	/**
	 * Return the time recorded in milliseconds.
	 * @return
	 */
	public long getTime() {
		if(endtime == starttime)
			return System.currentTimeMillis()-starttime;
		else
			return endtime-starttime;
	}
}
