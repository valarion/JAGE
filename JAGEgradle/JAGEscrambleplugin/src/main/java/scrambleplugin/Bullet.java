package scrambleplugin;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.interfaces.Event;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.Database;

public class Bullet implements Event {
	protected Image sprite;
	protected float x,y,w,h;
	
	protected LinkedList<Area> collidables = new LinkedList<Area>();

	protected SubTiledMap map;
	
	protected Player player;
	
	public Bullet(Player player) {
		sprite = Database.instance().getImages().get("bullet");
		w = sprite.getWidth();
		h = sprite.getHeight();
		this.player = player;
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {}

	public static final float speed = 0.2f;
	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		x += speed*delta;
		
		if(x+w > map.getWidth()*map.getTileWidth()) {
			map.remove(this);
		}
		
		float scale = (float) (container.getHeight() * (100 - Player.top - Player.bot) / 100)
				/ ((float) map.getHeight() * map.getTileHeight());
		
		if (x - player.getXDraw(0) > container.getWidth() / scale) {
			map.remove(this);
		}
		
		for(Area area : collidables) {
			if(area.intersects(x,y+h/4,w,h/2)) {
				map.remove(this);
				Explosion s = new Explosion();
				s.setXPos(x+w-s.getWidth()/2);
				s.setYPos(y);
				map.add(s);
			}
		}
		
		for(Event e : map.getEvents()) {
			if(e instanceof Enemy) {
				Enemy enemy = (Enemy)e;
				if(enemy.collidesWith(x,y+h/4,w,h/2)) {
					if(enemy instanceof Fuel) {
						map.remove(enemy);
						map.remove(this);
						Explosion s = new Explosion();
						s.setXPos(x+w-s.getWidth()/2);
						s.setYPos(y);
						map.add(s);
						
						player.addFuel();
						player.addPoints(150);
					}
					else if(enemy instanceof Rocket) {
						map.remove(enemy);
						map.remove(this);
						Explosion s = new Explosion();
						s.setXPos(x+w-s.getWidth()/2);
						s.setYPos(y);
						map.add(s);
						if(enemy.isWorking()) {
							player.addPoints(80);
						}
						else {
							player.addPoints(50);
						}
					}
					else if(enemy instanceof Mistery) {
						map.remove(enemy);
						map.remove(this);
						Explosion s = new Explosion();
						s.setXPos(x+w-s.getWidth()/2);
						s.setYPos(y);
						map.add(s);
						
						player.addPoints((new Random().nextInt(3)+1)*50);
					}
					else if(enemy instanceof Ovni) {
						map.remove(enemy);
						map.remove(this);
						Explosion s = new Explosion();
						s.setXPos(x+w-s.getWidth()/2);
						s.setYPos(y);
						map.add(s);
						
						player.addPoints(100);
					}
					else if(enemy instanceof Boss) {
						map.remove(enemy);
						map.remove(this);
						Explosion s = new Explosion();
						s.setXPos(x+w-s.getWidth()/2);
						s.setYPos(y);
						map.add(s);

						player.addPoints(800);
						player.win();
					}
					else if(enemy instanceof Fireball) {
						map.remove(this);
						Explosion s = new Explosion();
						s.setXPos(x+w-s.getWidth()/2);
						s.setYPos(y);
						map.add(s);
					}
				}
			}
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
		return true;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map) throws SlickException {
		this.map = map;
		collidables.clear();
		for (int i = 0; i < map.getObjectGroupCount(); i++) {
			for (int j = 0; j < map.getObjectCount(i); j++) {
				if(!(map.getObjectShape(i, j) instanceof Path2D) && !"teleport".equals(map.getObjectGroupName(i))) {
					collidables.add(new Area(map.getObjectShape(i, j)));
				}
			}
		}
	}

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
	public void loadEvent(Element node, Object context) throws SlickException {
		// TODO Auto-generated method stub

	}

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
