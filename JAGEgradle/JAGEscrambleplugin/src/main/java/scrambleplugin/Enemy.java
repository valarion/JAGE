package scrambleplugin;

import com.valarion.gameengine.core.interfaces.Event;

public interface Enemy extends Event {
	public boolean collidesWith(float x, float y, float w, float h);
}
