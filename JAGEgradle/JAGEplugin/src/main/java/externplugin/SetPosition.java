package externplugin;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;

public class SetPosition extends FlowEventClass {
	int xvar = -1, yvar=-1;
	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		super.loadEvent(node, context);
		try {
			xvar = Integer.parseInt(node.getAttribute("xvar"));
		}
		catch(Exception e) {}
		try {
			yvar = Integer.parseInt(node.getAttribute("yvar"));
		}
		catch(Exception e) {}
	}
	

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e)
			throws SlickException {
		if(xvar >= 0) {
			getEvent().setXPos((int)Database.instance().getContext().getGlobalVars()[xvar]);
		}
		if(yvar >= 0) {
			getEvent().setYPos((int)Database.instance().getContext().getGlobalVars()[yvar]);
		}
	}
}
