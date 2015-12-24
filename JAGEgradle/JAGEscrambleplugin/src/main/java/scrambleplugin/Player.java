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
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
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
 * Class overwriting the player class in the modules to have a player that
 * controls a scramble game.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class Player extends com.valarion.gameengine.events.Player {
	private static final long serialVersionUID = -5256271335877871766L;
	InGameState state;

	protected LinkedList<Area> collidables = new LinkedList<Area>();

	protected SubTiledMap map;

	protected float x, xoffset, yoffset, w, h;

	protected Image sprite;

	protected int lifes = 3;

	public Player() throws SlickException {
		sprite = Database.instance().getImages().get("ship");
		sprite = Util.getScaled(sprite, sprite.getWidth(), sprite.getHeight());
		w = sprite.getWidth();
		h = sprite.getHeight();
	}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {

	}

	protected int count = 0;

	public static final float speed = 0.025f;

	public static final float top = 20, bot = 10;

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		float scale = (float) (container.getHeight() * (100 - top - bot) / 100)
				/ ((float) map.getHeight() * map.getTileHeight());

		count++;

		// if (count % 20 == 0) {
		// int prevx = x;
		x += delta * speed;

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
					// x = prevx;
					xoffset--;
					if (xoffset < 0) {
						// Game over
					}
				}
				break;
			}
		}
		// }

		// if (count % 4 == 0) {

		Input input = container.getInput();

		float prevy = yoffset;

		if (input.isKeyDown(Controls.moveDown)) {
			yoffset += delta * speed * 2;
		}

		if (input.isKeyDown(Controls.moveUp)) {
			yoffset -= delta * speed * 2;
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

		float prevx = xoffset;

		if (input.isKeyDown(Controls.moveLeft)) {
			xoffset -= delta * speed * 2;
		}

		if (input.isKeyDown(Controls.moveRight)) {
			xoffset += delta * speed * 2;
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
					xoffset = prevx;
				}
				break;
			}
		}
		// }

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			Bullet b = new Bullet(this);
			b.setXPos((x + xoffset + w - b.getWidth()));
			b.setYPos((yoffset + h / 2 - b.getWidth() / 2));
			b.loadEvent(null, state);
			b.onMapLoad(container, map);
			map.add(b);
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
		float scale = container.getHeight() * (bot * 80 / 100) / 100 / sprite.getHeight();
		float w = sprite.getWidth() * scale;
		float h = sprite.getHeight() * scale;
		float space = container.getHeight() * (bot * 10 / 100) / 100;
		float x = space;
		float y = container.getHeight() * (100 - bot) / 100 + space;

		for (int i = 1; i < lifes; i++) {
			g.drawImage(sprite, x, y, x + w, y + h, 0, 0, sprite.getWidth(), sprite.getHeight());
			x += w + space;
		}
	}

	@Override
	public float getXDraw(int tilewidth) {
		return x;
	}

	@Override
	public float getYDraw(int tileheight) {
		return (yoffset + h / 2);
	}

	@Override
	public int getWidth() {
		return (int) w;
	}

	@Override
	public int getHeight() {
		return (int) h;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map) {
		this.map = map;
		collidables.clear();
		for (int i = 0; i < map.getObjectGroupCount(); i++) {
			if ("fuel".equals(map.getObjectGroupName(i))) {
				for (int j = 0; j < map.getObjectCount(i); j++) {
					if ((map.getObjectShape(i, j) instanceof Path2D)) {
						Path2D path = (Path2D) map.getObjectShape(i, j);

						for (PathIterator it = path.getPathIterator(null); !it.isDone(); it.next()) {

							float coords[] = new float[2];
							it.currentSegment(coords);
							if(coords[0] != 0) {
								Fuel f = new Fuel();
								f.setXPos((int) coords[0]);
								f.setYPos((int) coords[1] - f.getHeight());
								map.add(f);
							}
						}
					}
				}
			} else {
				for (int j = 0; j < map.getObjectCount(i); j++) {
					if (!(map.getObjectShape(i, j) instanceof Path2D)) {
						collidables.add(new Area(map.getObjectShape(i, j)));
					}
				}
			}
		}

		if (state != null && state.getCamera() instanceof Camera) {
			((Camera) state.getCamera()).focusAt(map, this);
			((Camera) state.getCamera()).setGuiPercentage(top, bot);
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
