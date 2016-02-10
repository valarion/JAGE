package scrambleplugin;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.GameState;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.StartState;

public class GameOver extends GameState {
	UnicodeFont font;
	String text = "GAME OVER";
	
	@SuppressWarnings("unchecked")
	public GameOver() {
		font = new UnicodeFont(Database.instance().getDefaultFont().getFont(),50, true, false);
		
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect());
		try {
			font.loadGlyphs();
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(container.getInput().isKeyPressed(Controls.accept)) {
			GameCore.getInstance().setActive(new StartState());
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		float w = font.getWidth(text);
		float h = font.getLineHeight();
		font.drawString(container.getWidth()/2-w/2, container.getHeight()/2-h/2, text);
	}

	@Override
	public void init(GameContainer container) throws Exception {
		// TODO Auto-generated method stub

	}

}
