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

public class Ovni implements Enemy {
	float x,y,w,h;
	float starty, endy;
	
	Image sprite;
	
	public static final float speed = 0.04f;
	
	boolean direction;
	
	Player player;
	public Ovni(Player player) {
		sprite = Database.instance().getImages().get("ovni");
		w = sprite.getWidth();
		h = sprite.getHeight();
		this.player = player;
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		if(direction) {
			y+=delta*0.1;
			
			if(y>endy) {
				y=endy;
				direction =!direction;
			}
		}
		else {
			y-=delta*0.1;
			
			if(y<starty) {
				y=starty;
				direction =!direction;
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
		if(starty == 0) {
			starty = newPos;
		}
		else {
			endy = newPos;
		}
		y = starty + (new Random()).nextInt(Math.abs((int) (endy-starty)));
		direction = (new Random()).nextBoolean();
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
		return true;
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
