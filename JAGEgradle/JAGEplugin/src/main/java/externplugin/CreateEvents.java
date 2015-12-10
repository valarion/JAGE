package externplugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.events.rpgmaker.GameEvent;

public class CreateEvents extends FlowEventClass {
	List<Element> events;
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		events = new LinkedList<Element>();
		
		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node n = childs.item(i);
			if ((n instanceof Element) && n.getNodeName() != null) {
				events.add((Element)n);
			}
		}
	}
	

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		for(Element element : events) {
			try {
				Map<String, Class<?>> plugins = GameCore.getInstance().getSets().get(Event.class);
				Class<?> c = plugins.get(element.getNodeName());
				Event event = (Event) c.newInstance();
				event.loadEvent((Element) element, getState());
				event.onMapSetAsActive(container, map);
				map.add(event);
			} catch (InstantiationException | IllegalAccessException exception) {
				exception.printStackTrace();
			}
		}
	}
}
