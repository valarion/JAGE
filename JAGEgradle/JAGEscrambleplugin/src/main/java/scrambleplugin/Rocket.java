package scrambleplugin;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Database;

public class Rocket implements Enemy {
	float x,y,w,h;
	
	Image sprite;
	
	boolean active = false;
	
	int distance;
	
	public static final float speed = 0.04f;
	
	Player player;
	public Rocket(Player player) {
		sprite = Database.instance().getImages().get("rocket");
		w = sprite.getWidth();
		h = sprite.getHeight();
		this.player = player;
		distance = new Random().nextInt(320)-20;
		// TODO load fire
	}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		if(active) {
			y -= speed*(float)delta;
			if(y + 2*h < 0) {
				map.remove(this);
			}
		}
		else {
			if(x-player.getXDraw(0) < distance) {
				active = true;
			}
		}
		
		if(player.collidesWith(x, y, w, h)) {
			player.die();
		}
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
		g.drawImage(sprite, x, y);
		
		if(active) {
			// TODO draw fire
		}
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public float getXDraw(int tileWidth) {
		return x;
	}

	@Override
	public float getYDraw(int tileHeight) {
		return y;
	}

	@Override
	public int getXPos() {
		return (int) x;
	}

	@Override
	public int getYPos() {
		return (int) y;
	}

	@Override
	public void setXPos(int newPos) {
		x = newPos;
	}

	@Override
	public void setYPos(int newPos) {
		y = newPos;
	}

	@Override
	public int getDirection() {
		return 0;
	}

	@Override
	public void setDirection(int direction) {}

	@Override
	public int getWidth() {
		return (int) w;
	}

	@Override
	public int getHeight() {
		return (int) h;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map) throws SlickException {}

	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map) throws SlickException {}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map) throws SlickException {}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map, Event e) throws SlickException {}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e) throws SlickException {}

	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e) throws SlickException {}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {}

	@Override
	public boolean isWorking() {
		return active;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e) throws SlickException {}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean collidesWith(float x, float y, float w, float h) {
		return new Rectangle2D.Float(x,y,w,h).intersects(this.x, this.y, this.w, this.h);
	}

}
