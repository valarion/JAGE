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
package com.valarion.gameengine.gamestates;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.events.Player;
import com.valarion.gameengine.util.Timer;
import com.valarion.gameengine.util.WindowImage;

/**
 * Class containing everything that defines a single gameplay.
 * @author Rubén Tomás Gracia
 *
 */
public class GameContext implements Serializable, Comparable<GameContext> {

	protected long globalVars[] = new long[100];

	protected boolean globalInterrupts[] = new boolean[100];

	protected Map<String, Object> globalObjects = new HashMap<String, Object>();

	protected Set<String> deletedEvents = new HashSet<String>();

	protected Map<String, Boolean[]> eventInterrupts = new HashMap<String, Boolean[]>();
	
	protected Map<String, Long> stats = new HashMap<String,Long>();
	
	protected String activemap = null;

	protected Player player = null;

	protected boolean saveEnabled = true;

	protected Date savetime = null;

	protected String savename = null;
	
	protected Timer timer = new Timer();

	/**
	 * 
	 */
	private static final long serialVersionUID = -4687670843701849294L;

	/**
	 * Get global variables.
	 * @return
	 */
	public long[] getGlobalVars() {
		return globalVars;
	}

	/**
	 * Get global interrupts.
	 * @return
	 */
	public boolean[] getGlobalInterrupts() {
		return globalInterrupts;
	}

	/**
	 * Get global objects.
	 * @return
	 */
	public Map<String, Object> getGlobalObjects() {
		return globalObjects;
	}

	/**
	 * Get deleted events.
	 * @return
	 */
	public Set<String> getDeletedEvents() {
		return deletedEvents;
	}

	/**
	 * Get local interrupts of an event id.
	 * @param eventId
	 * @return
	 */
	public Boolean[] getInterrupts(String eventId) {
		Boolean[] ret = eventInterrupts.get(eventId);

		if (ret == null) {
			ret = new Boolean[] { false, false, false, false };
			eventInterrupts.put(eventId, ret);
		}

		return ret;
	}

	/**
	 * Get active map.
	 * @return
	 */
	public String getActivemap() {
		return activemap;
	}

	/**
	 * Set active map.
	 * @param activemap
	 */
	public void setActivemap(String activemap) {
		this.activemap = activemap;
	}

	/**
	 * Get player.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Set player.
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Return whether save menu is enabled.
	 * @return
	 */
	public boolean isSaveEnabled() {
		return saveEnabled;
	}

	/**
	 * Set whether save menu is enabled.
	 * @param saveEnabled
	 */
	public void setSaveEnabled(boolean saveEnabled) {
		this.saveEnabled = saveEnabled;
	}

	/**
	 * Get the time this context was saved.
	 * @return
	 */
	public Date getSavetime() {
		return savetime;
	}

	/**
	 * Set the time this context is being saved.
	 */
	public void setSavetime() {
		savetime = Calendar.getInstance().getTime();
	}

	/**
	 * Get save name.
	 * @return
	 */
	public String getSavename() {
		return savename;
	}

	/**
	 * Set save name.
	 * @param savename
	 */
	public void setSavename(String savename) {
		this.savename = savename;
	}

	@Override
	public int compareTo(GameContext gc) {
		return gc.savetime.compareTo(savetime);
	}
	
	/**
	 * Get statistic.
	 * @param name
	 * @return
	 */
	public long getStat(String name) {
		Long ret = stats.get(name);
		if(ret == null)
			return 0;
		else
			return ret.longValue();
	}
	
	/**
	 * Set statistic.
	 * @param name
	 * @param value
	 */
	public void setStat(String name, long value) {
		stats.put(name, new Long(value));
	}

	/**
	 * Get global timer.
	 * @return
	 */
	public Timer getTimer() {
		return timer;
	}
	
	/**
	 * Draw game context. used on save and load menu.
	 * @param window
	 * @param name
	 * @param selected
	 * @throws SlickException
	 */
	public void draw(WindowImage window, String name, boolean selected) throws SlickException {
		window.setShowArrow(false);
		Image contain = window.getContain();
		Graphics i = contain.getGraphics();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		
		i.clear();

		Font font = i.getFont();

		String text;
		
		if(name != null) {
			text = name;
		}
		else {
			text = getSavename();
		}

		String date = dt.format(getSavetime());
		Image rightarrow = window.getModel().getImage("rightArrow");

		if (selected) {
			i.drawImage(rightarrow, 10, 10);
		}
		i.drawString(text, 10 + rightarrow.getWidth() + 5, 10);

		i.drawString(date, contain.getWidth() - font.getWidth(date) - 10,
				contain.getHeight() - font.getLineHeight());

		i.flush();
	}

}
