package externplugin;

import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;

public class SetPriority extends FlowEventClass {
	int value = SubTiledMap.defaultpriority;
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		try {
			value = Integer.parseInt(node.getAttribute("value"));
		}
		catch(Exception e) {
			
		}
	}
	

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		for(Set<Event> set : map.getEventsByPriority().values()) {
			if(set.contains(getEvent())) {
				set.remove(getEvent());
				break;
			}
		}
		map.add(getEvent(),value);
	}
}
