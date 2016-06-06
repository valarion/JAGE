package scrambleplugin;

import java.util.LinkedList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Database;

public class Explosion implements Event {
	float x, y, w, h;
	
	Animation anim;
	public Explosion() {
		Image img = Database.instance().getImages().get("explosion");
		h = img.getHeight();
		w = h;
		LinkedList<Image> l = new LinkedList<Image>();
		
		for(int i=0; i<img.getWidth();i+=w) {
			l.add(img.getSubImage(i, 0, (int)w, (int)h));
		}
		
		anim = new Animation(l.toArray(new Image[0]), 100,false);
		anim.setLooping(false);
		anim.start();
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		if(anim.isStopped()) {
			map.remove(this);
		}
		anim.update(delta);
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
		anim.draw(x, y);
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
	
	public void setXPos(float newPos) {
		x = newPos;
	}

	public void setYPos(float newPos) {
		y = newPos;
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
		return false;
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

}
