package externplugin;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;

public class ScoresOption extends FlowEventClass {
	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		GameCore.getInstance().setActive(new Scores());
	}
	
	@Override
	public String toString() {
		return "Watch scores";
	}
	
	@Override
	public boolean isWorking() {
		return true;
	}
}
