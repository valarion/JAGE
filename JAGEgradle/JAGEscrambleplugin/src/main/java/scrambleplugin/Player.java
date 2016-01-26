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
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.gamestates.StartState;
import com.valarion.gameengine.gamestates.SubState;
import com.valarion.gameengine.util.Util;
import com.valarion.pluginsystem.ClassOverrider;

/**
 * Class overwriting the player class in the modules to have a player that
 * controls a scramble game.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class Player extends com.valarion.gameengine.events.Player implements Enemy {
	private static final long serialVersionUID = -5256271335877871766L;
	protected InGameState state;

	protected LinkedList<Area> collidables = new LinkedList<Area>();

	protected SubTiledMap map;

	protected float x, xoffset, yoffset, w, h;

	protected Image sprite;

	protected int lifes = 3;

	protected float fuel = 100;

	protected UnicodeFont font;
	protected boolean dying;
	
	protected int punctuation = 0;
	
	protected String phases[] = new String[]{"1ST","2ND","3RD","4TH","5TH","BASE"};
	protected int phase = 0;

	protected float teleportx = Float.MAX_VALUE;
	
	protected Animation death;
	
	public Player() throws SlickException {
		sprite = Database.instance().getImages().get("ship");
		sprite = Util.getScaled(sprite, sprite.getWidth(), sprite.getHeight());
		w = sprite.getWidth();
		h = sprite.getHeight();
		
		Image img = Database.instance().getImages().get("explosion");
		float hexp = img.getHeight();
		float wexp = hexp;
		LinkedList<Image> l = new LinkedList<Image>();
		
		for(int i=0; i<img.getWidth();i+=wexp) {
			l.add(img.getSubImage(i, 0, (int)wexp, (int)hexp));
		}
		
		death = new Animation(l.toArray(new Image[0]), 100);
		death.setLooping(false);
	}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {

	}

	public static final float speed = 0.025f;

	public static final float top = 20, bot = 10;
	
	protected int deltacount = 0;

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		deltacount += delta;
		if(deltacount > 1000) {
			punctuation += 10;
			deltacount -= 1000;
		}
		Input input = container.getInput();
		
		if (input.isKeyPressed(Controls.cancel)) {
			((SubState)GameCore.getInstance().getActive()).getActiveEvents().add(new PauseMenu(((SubState)GameCore.getInstance().getActive())));
		}
		else if (!dying) {
			float scale = (float) (container.getHeight() * (100 - top - bot) / 100)
					/ ((float) map.getHeight() * map.getTileHeight());
			float movement = delta * speed;
			fuel -= delta / 1000.f;
			if (fuel < 0) {
				fuel = 0;
			}

			x += movement;

			for (Area area : collidables) {
				if (collidesWith(area)) {
					if (!collidesWith(area, 0, movement)) {
						yoffset = yoffset + movement;
					} else if (!collidesWith(area, 0, -movement)) {
						yoffset = yoffset - movement;
					} else if (!collidesWith(area, 0, 2 * movement)) {
						yoffset = yoffset + 2 * movement;
					} else if (!collidesWith(area, 0, -2 * movement)) {
						yoffset = yoffset - 2 * movement;
					} else {
						xoffset -= delta * speed;
						if (xoffset < 0) {
							die();
						}
					}
					break;
				}
			}

			float prevy = yoffset;

			if (input.isKeyDown(Controls.moveDown)) {
				yoffset += movement * 2;
			}

			if (input.isKeyDown(Controls.moveUp)) {
				yoffset -= movement * 2;
			}

			for (Area area : collidables) {
				if (collidesWith(area)) {
					if (!collidesWith(area, movement, 0)) {
						xoffset = xoffset + movement;
					} else if (!collidesWith(area, -movement, 0)) {
						xoffset = xoffset - movement;
					} else if (!collidesWith(area, 2 * movement, 0)) {
						xoffset = xoffset + 2 * movement;
					} else if (!collidesWith(area, -2 * movement, 0)) {
						xoffset = xoffset - 2 * movement;
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
				xoffset -= movement * 2;
			}

			if (input.isKeyDown(Controls.moveRight)) {
				xoffset += movement * 2;
			}

			if (xoffset + w > container.getWidth() / scale) {
				xoffset = (int) (container.getWidth() / scale - w);
			}

			if (xoffset < 0) {
				xoffset = 0;
			}

			for (Area area : collidables) {
				if (collidesWith(area)) {
					if (!collidesWith(area, 0, movement)) {
						yoffset = yoffset + movement;
					} else if (!collidesWith(area, 0, -movement)) {
						yoffset = yoffset - movement;
					} else if (!collidesWith(area, 0, 2*movement)) {
						yoffset = yoffset + 2*movement;
					} else if (!collidesWith(area, 0, -2*movement)) {
						yoffset = yoffset - 2*movement;
					} else {
						xoffset = prevx;
					}
					break;
				}
			}

			if (input.isKeyPressed(Input.KEY_SPACE)) {
				Bullet b = new Bullet(this);
				b.setXPos((x + xoffset + w - b.getWidth()));
				b.setYPos((yoffset + h / 2 - b.getWidth() / 2));
				b.loadEvent(null, state);
				b.onMapLoad(container, map);
				map.add(b);
			}
			
			if(x >= teleportx) {
				phase++;
				state.setAsActive(container, "phase"+ phase);
				x -= teleportx;
				
				for(Event e : map.getEvents()) {
					e.setXPos((int) (e.getXPos()-teleportx));
					if(e.getXPos() > 0) {
						state.getActive().add(e);
					}
				}
			}
		}
		else {
			death.update(delta);
			
			if(death.isStopped()) {
				GameCore.getInstance().setActive(new StartState());
			}
		}
	}

	protected boolean collidesWith(Area area) {
		return collidesWith(area, 0, 0);
	}

	protected boolean collidesWith(Area area, float xdifference, float ydifferece) {
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
		if(!dying) {
			g.drawImage(sprite, x + xoffset, yoffset);
		}
		else {
			death.draw(x + xoffset + w/2 - death.getWidth()/2, yoffset + h/2 - death.getHeight()/2);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
		float scale = container.getHeight() * (bot * 35 / 100) / 100 / sprite.getHeight();

		// render lifes
		float w = sprite.getWidth() * scale;
		float h = sprite.getHeight() * scale;
		float space = container.getHeight() * (bot * 10 / 100) / 100;
		float x = space;
		float y = container.getHeight() * ((100 - bot)+(bot * 45 / 100)) / 100 + space;

		g.setColor(Color.black);

		g.fill(new Rectangle(0, 0, container.getWidth(), container.getHeight() * top / 100));
		g.fill(new Rectangle(0, container.getHeight() * (100 - bot) / 100, container.getWidth(),
				container.getHeight() * bot / 100));

		for (int i = 1; i < lifes; i++) {
			g.drawImage(sprite, x, y, x + w, y + h, 0, 0, sprite.getWidth(), sprite.getHeight());
			x += w + space;
		}

		// render fuel
		if (font == null) {
			font = new UnicodeFont(Database.instance().getDefaultFont().getFont(), (int) h, true, false);
			font.addAsciiGlyphs();
			font.getEffects().add(new ColorEffect());
			try {
				font.loadGlyphs();
			} catch (SlickException e1) {
				e1.printStackTrace();
			}
		}
		
		x = space;
		y = container.getHeight() * ((100 - bot)) / 100 + space;
		
		font.drawString(x, y, "FUEL",Color.yellow);
		x += font.getWidth("FUEL") + space;

		float left = container.getWidth() - x - space;

		float part = left / 100;

		g.setColor(Color.yellow);
		g.fill(new Rectangle(x, y, part * fuel, h));
		g.setColor(Color.darkGray);
		g.fill(new Rectangle(x + part * fuel, y, left - part * fuel, h));
		
		// Render punctuation
		
		space = container.getHeight() * (top * 10 / 100) / 100;
		x = space;
		y = space;
		font.drawString(x, y, "SCORE: ",Color.white);
		x+=font.getWidth("SCORE: ");
		font.drawString(x, y, Integer.toString(punctuation),Color.yellow);
		
		// Render phases
		part = container.getWidth() / (phases.length+1);
		x = part / 2;
		y = 2*space + font.getLineHeight();
		
		g.setColor(Color.yellow);
		g.fill(new Rectangle(x, y, part*phases.length, font.getLineHeight()*2+3));
		
		for(int i=0; i< phases.length; i++) {
			String phasename = phases[i];
			font.drawString(x+1, y+1, phasename,Color.blue);
			g.setColor(i <= phase ? Color.green : Color.red);
			g.fill(new Rectangle(x+1, y+1+font.getLineHeight()+1, part-2, font.getLineHeight()));
			x += part;
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
							if (coords[0] != 0) {
								Fuel f = new Fuel(this);
								f.setXPos((int) coords[0]);
								f.setYPos((int) coords[1] - f.getHeight());
								map.add(f);
							}
						}
					}
				}
			} else if ("rocket".equals(map.getObjectGroupName(i))) {
				for (int j = 0; j < map.getObjectCount(i); j++) {
					if ((map.getObjectShape(i, j) instanceof Path2D)) {
						Path2D path = (Path2D) map.getObjectShape(i, j);

						for (PathIterator it = path.getPathIterator(null); !it.isDone(); it.next()) {

							float coords[] = new float[2];
							it.currentSegment(coords);
							if (coords[0] != 0) {
								Rocket f = new Rocket(this);
								f.setXPos((int) coords[0]);
								f.setYPos((int) coords[1] - f.getHeight());
								map.add(f);
							}
						}
					}
				}
			} else if ("mistery".equals(map.getObjectGroupName(i))) {
				for (int j = 0; j < map.getObjectCount(i); j++) {
					if ((map.getObjectShape(i, j) instanceof Path2D)) {
						Path2D path = (Path2D) map.getObjectShape(i, j);

						for (PathIterator it = path.getPathIterator(null); !it.isDone(); it.next()) {

							float coords[] = new float[2];
							it.currentSegment(coords);
							if (coords[0] != 0) {
								Mistery f = new Mistery(this);
								f.setXPos((int) coords[0]);
								f.setYPos((int) coords[1] - f.getHeight());
								map.add(f);
							}
						}
					}
				}
			} else if ("teleport".equals(map.getObjectGroupName(i))) {
				for (int j = 0; j < map.getObjectCount(i); j++) {
					if ((map.getObjectShape(i, j) instanceof Rectangle2D.Float)) {
						Rectangle2D.Float rect = (Rectangle2D.Float) map.getObjectShape(i, j);

						teleportx = (float) rect.getX();
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

	public void addFuel() {
		fuel += 10;
		if (fuel > 100) {
			fuel = 100;
		}
	}

	@Override
	public boolean collidesWith(float x, float y, float w, float h) {
		return collidesWith(new Area(new Rectangle2D.Float(x, y, w, h)));
	}
	
	public void addPoints(int points) {
		punctuation+=points;
	}

	protected void die() {
		if(!dying) {
			dying = true;
			lifes--;
			death.restart();
		}
	}
}
