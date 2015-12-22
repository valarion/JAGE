package scrambleplugin;
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

import java.awt.geom.Area;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.util.Util;
import com.valarion.pluginsystem.ClassOverrider;

/**
 * Class Overwriting the player class in the modules to have a player that
 * doesn't render and that controls a tetris game.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class Player extends com.valarion.gameengine.events.Player {
	private static final long serialVersionUID = -5256271335877871766L;
	InGameState state;

	protected LinkedList<Area> collidables = new LinkedList<Area>();

	protected SubTiledMap map;

	protected int x, xoffset, yoffset, w, h;

	protected Image sprite;

	public Player() throws SlickException {
		sprite = Database.instance().getImages().get("ship");
		sprite = Util.getScaled(sprite, sprite.getWidth(), sprite.getHeight());
		w = sprite.getWidth();
		h = sprite.getHeight();
	}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {

	}

	int count = 0;

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		float scale = (float) container.getHeight() / ((float) map.getHeight() * map.getTileHeight());

		count++;

		if (count % 20 == 0) {
			int prevx = x;
			x++;

			for (Area area : collidables) {
				if (collidesWith(area)) {
					if (!collidesWith(area, 0, 1)) {
						yoffset = yoffset + 1;
					} else if (!collidesWith(area, 0, -1)) {
						yoffset = yoffset - 1;
					} else if (!collidesWith(area, 0, 2)) {
						yoffset = yoffset + 2;
					} else if (!collidesWith(area, 0, -2)) {
						yoffset = yoffset - 2;
					} else {
						// Game over
						x = prevx;
					}
					break;
				}
			}
		}

		if (count % 4 == 0) {

			Input input = container.getInput();

			int prevy = yoffset;

			if (input.isKeyDown(Controls.moveDown)) {
				yoffset++;
			}

			if (input.isKeyDown(Controls.moveUp)) {
				yoffset--;
			}

			for (Area area : collidables) {
				if (collidesWith(area)) {
					if (!collidesWith(area, 1, 0)) {
						xoffset = xoffset + 1;
					} else if (!collidesWith(area, -1, 0)) {
						xoffset = xoffset - 1;
					} else if (!collidesWith(area, 2, 0)) {
						xoffset = xoffset + 2;
					} else if (!collidesWith(area, -2, 0)) {
						xoffset = xoffset - 2;
					} else {
						yoffset = prevy;
					}
					break;
				}
			}

			if (yoffset + h > map.getHeight() * map.getTileHeight()) {
				yoffset = map.getHeight() * map.getTileHeight() - h;
			}

			if (yoffset < 0) {
				yoffset = 0;
			}

			int prevx = xoffset;

			if (input.isKeyDown(Controls.moveLeft)) {
				xoffset--;
			}

			if (input.isKeyDown(Controls.moveRight)) {
				xoffset++;
			}

			if (xoffset + w > container.getWidth() / scale) {
				xoffset = (int) (container.getWidth() / scale - w);
			}

			if (xoffset < 0) {
				xoffset = 0;
			}

			for (Area area : collidables) {
				if (collidesWith(area)) {
					if (!collidesWith(area, 0, 1)) {
						yoffset = yoffset + 1;
					} else if (!collidesWith(area, 0, -1)) {
						yoffset = yoffset - 1;
					} else if (!collidesWith(area, 0, 2)) {
						yoffset = yoffset + 2;
					} else if (!collidesWith(area, 0, -2)) {
						yoffset = yoffset - 2;
					} else {
						// Game over
						xoffset = prevx;
					}
					break;
				}
			}
		}
	}

	protected boolean collidesWith(Area area) {
		return collidesWith(area, 0, 0);
	}

	protected boolean collidesWith(Area area, int xdifference, int ydifferece) {
		return area.intersects(x + xoffset + xdifference, yoffset + ydifferece, 2 * w / 3, h)
				|| area.intersects(x + xoffset + xdifference, yoffset + ydifferece + h / 2, w, 1);
	}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {

	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
		g.drawImage(sprite, x + xoffset, yoffset);
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {

	}

	@Override
	public int getXDraw(int tilewidth) {
		return x;
	}

	@Override
	public int getYDraw(int tileheight) {
		return yoffset + h / 2;
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getHeight() {
		return h;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map) {
		this.map = map;
		collidables.clear();
		for (int i = 0; i < map.getObjectGroupCount(); i++) {
			for (int j = 0; j < map.getObjectCount(i); j++) {
				collidables.add(new Area(map.getObjectShape(i, j)));
			}
		}

		if (state != null && state.getCamera() instanceof Camera) {
			((Camera) state.getCamera()).focusAt(map, this);
		}
	}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map) {

	}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		if (context instanceof InGameState) {
			state = (InGameState) context;
		}
	}

	@Override
	public int getXPos() {
		return 0;
	}

	@Override
	public int getYPos() {
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
	public void performAction(GameContainer container, SubTiledMap map, Event e) throws SlickException {

	}

	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map) throws SlickException {
	}

	@Override
	public String getId() {
		return "player";
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@ClassOverrider
	public static Class<?> override(Class<?> c) {
		if (c.equals(com.valarion.gameengine.events.Player.class)) {
			return Player.class;
		} else {
			return c;
		}
	}
}
