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
package externplugin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.pluginsystem.ClassOverrider;

/**
 * Class representing the player and all it's mechanics.
 * @author Rubén Tomás Gracia
 *
 */
public class Player extends com.valarion.gameengine.events.Player {
	private static final long serialVersionUID = -1220851568209641413L;
	
	InGameState state;
	
	Map<String,Boolean> pressed = new ConcurrentHashMap<String,Boolean>();

	public Map<String, Boolean> getPressed() {
		return pressed;
	}
	
	@Override
	public void update(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map)
			throws SlickException {
		/*Input input = container.getInput();
		for(Field f : Controls.class.getDeclaredFields()) {
			try {
				boolean is = input.isKeyPressed(f.getInt(null));
				if(is) {
					pressed.put(f.getName(), true);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {

	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth,
			int tileheight) throws SlickException {

	}

	@Override
	public int getXDraw(int tilewidth) {
		return (int) (getXPos() * tilewidth + tilewidth/2 - getWidth()/2 + xOff);
	}

	@Override
	public int getYDraw(int tileheight) {
		return (int) ((getYPos() + 1) * tileheight - getHeight() + yOff);
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map) {
	}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map) {
	}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map,
			Event e) {
	}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		if(context instanceof InGameState) {
			state = (InGameState)context;
		}
	}
	
	@Override
	public int getXPos() {
		if(state != null && state.getActive() != null) {
			return state.getActive().getWidth()/2;
		}
		return 0;
	}

	@Override
	public int getYPos() {
		if (state != null && state.getActive() != null) {
			return state.getActive().getHeight() / 2;
		}
		return 0;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public int getDirection() {
		return 0;
	}

	@Override
	public void setDirection(int direction) {

	}

	@Override
	public boolean isWorking() {
		return true;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {

	}

	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map)
			throws SlickException {

	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@ClassOverrider
	public static Class<?> override(Class<?> c) {
		return Player.class;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {
		stream.defaultReadObject();
	}
}
