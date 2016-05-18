package scrambleplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;

public class FireballGenerator implements Event {
	Player player;
	List<Integer> heights;
	
	int counter;
	public static final int period = 1500;
	public static final int distance = 400;
	public FireballGenerator(Player player) {
		this.player = player;
		heights = new ArrayList<Integer>();
		
		counter = 0;
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {}

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		counter += delta;
		
		if(counter > period) {
			if(player.getXDraw(0)+distance < map.getWidth()*map.getTileWidth()) {
				counter -= period;
				
				Fireball f = new Fireball(player);
				f.setYPos(heights.get((new Random()).nextInt(heights.size())));
				f.setXPos((int) (player.getXDraw(0)+distance));
				map.add(f);
			}
		}
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public float getXDraw(int tileWidth) {
		return 0;
	}

	@Override
	public float getYDraw(int tileHeight) {
		return 0;
	}

	@Override
	public int getXPos() {
		return (int) 0;
	}

	@Override
	public int getYPos() {
		return (int) 0;
	}

	@Override
	public void setXPos(int newPos) {}

	@Override
	public void setYPos(int newPos) {
		heights.add(newPos);
	}

	@Override
	public int getDirection() {
		return 0;
	}

	@Override
	public void setDirection(int direction) {}

	@Override
	public int getWidth() {
		return (int) 0;
	}

	@Override
	public int getHeight() {
		return (int) 0;
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
}
