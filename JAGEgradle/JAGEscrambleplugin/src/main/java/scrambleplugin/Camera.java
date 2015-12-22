package scrambleplugin;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.pluginsystem.ClassOverrider;

/**
 * Camera with height fixed to map height and x centered in wherever is selected.
 * @author Rubén Tomás Gracia
 *
 */
public class Camera extends com.valarion.gameengine.util.Camera {

	public Camera(GameCore game, InGameState instance) {
		super(game, instance);
	}
	
	/**
	 * Focus camera at map and event and fix height to screen.
	 * @param x
	 * @param y
	 * @return
	 */
	public Camera focusAt(SubTiledMap map, Event center) {
		this.map = map;
		this.center = center;
		return this;
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (center != null && map != null) {
			float scale = (float)container.getHeight()/((float)map.getHeight()*map.getTileHeight());
			
			float playerXPos = (game.getApp().getWidth() / 2.f - center
					.getWidth() / 2.f);
			float mapXOff = (-(center.getXDraw(0) + GameCore.getInstance().getApp().getWidth() / 2.f / scale)
					+ playerXPos/scale);
			
			g.scale(scale, scale);
			g.translate(mapXOff+2, 0);
		} 
		else {
			super.render(container, g);
		}
	}
	
	@ClassOverrider
	public static Class<?> override(Class<?> c) {
		if(c.equals(com.valarion.gameengine.util.Camera.class)){
			return Camera.class;
		}
		else {
			return c;
		}
	}
}
