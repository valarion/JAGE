package externplugin;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.interfaces.GameState;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;

public class GameOver extends GameState {
	UnicodeFont font;
	String text;
	String name = "Write your name:";

	TextField nameInput;

	String input = null;
	boolean victory;
	int score;
	@SuppressWarnings("unchecked")
	public GameOver(String text, int score, boolean victory) {
		this.text = text;
		this.score = score;
		this.victory = victory;
		font = new UnicodeFont(Database.instance().getDefaultFont().getFont(), 50, true, false);

		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect());
		try {
			font.loadGlyphs();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
	
		nameInput = new TextField(GameCore.getInstance().getApp(), Database.instance().getDefaultFont(), 150, 20, 500, Database.instance().getDefaultFont().getLineHeight()+20, new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
				input = nameInput.getText();
			}

		});
		nameInput.setFocus(true);
		nameInput.setBorderColor(Color.black);
		nameInput.setText("Anonymous");
		nameInput.setMaxLength(20);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if (input != null) {
			GameCore.getInstance().setActive(new Scores(score, victory, input));
			container.getInput().isKeyPressed(Controls.accept);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		nameInput.render(container, g);
		float w = font.getWidth(text);
		float h = font.getLineHeight();
		font.drawString(container.getWidth() / 2 - w / 2, container.getHeight() / 2 - h / 2, text);
		
		UnicodeFont namefont = Database.instance().getDefaultFont();
		namefont.drawString(container.getWidth()/8, container.getHeight() / 2 - h / 2 + font.getLineHeight(), name);
		nameInput.setLocation(container.getWidth()/8 + namefont.getWidth(name) + 20, (int) (container.getHeight() / 2 - h / 2 + font.getLineHeight()));
		
		nameInput.render(container, g);
	}

	@Override
	public void init(GameContainer container) throws Exception {
		
	}

}
