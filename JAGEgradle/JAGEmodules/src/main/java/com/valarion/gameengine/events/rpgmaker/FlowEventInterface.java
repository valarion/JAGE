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
package com.valarion.gameengine.events.rpgmaker;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.events.Route;
import com.valarion.gameengine.gamestates.InGameState;

/**
 * Interface that adds methods to control the flow of an event.
 * @author Rubén Tomás Gracia
 *
 */
public interface FlowEventInterface extends Event {
	/**
	 * Breaks the first loop found in the parents.
	 */
	public void breakCicle();
	
	/**
	 * Stops the event execution.
	 */
	public void stop();

	/**
	 * Restart the event execution.
	 */
	public void restart();

	/**
	 * Add a label. Works like GOTO labels.
	 * @param label
	 * @param child
	 */
	public void addLabel(String label, FlowEventInterface child);

	/**
	 * GOTO instruction.
	 * @param label
	 */
	public void goToLabel(String label);

	/**
	 * Check if label exists.
	 * @param label
	 * @return
	 */
	public boolean hasLabel(String label);
	
	/**
	 * Get parent event. 
	 * @return
	 */
	public FlowEventInterface getParent();
	
	/**
	 * Get top-most parent event. This should call recursively.
	 * @return
	 */
	public FlowEventInterface getEvent();
	
	/**
	 * Set top-most event route.
	 * @param route
	 */
	public void setRoute(Route route);
	
	/**
	 * Get top-most parent route.
	 * @return
	 */
	public Route getRoute();
	
	/**
	 * Get InGameSate containing this event.
	 * @return
	 */
	public InGameState getState();
	
	/**
	 * Set blocking property.
	 * @param blocking
	 */
	public void setBlocking(boolean blocking);

}
