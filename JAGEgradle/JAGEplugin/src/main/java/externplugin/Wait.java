package externplugin;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;

public class Wait extends FlowEventClass {
	long waittime, waited;
	
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		
		waittime = waited = Integer.parseInt(node.getAttribute("time"));
	}
	
	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		super.performAction(container, map, e);
		waited = 0;
	}
	
	@Override
	public void paralelupdate(GameContainer container, int delta,
			SubTiledMap map) throws SlickException {
		if(isWorking()) {
			waited += delta;
		}
	}
	
	@Override
	public boolean isWorking() {
		return waited < waittime;
	}
}
