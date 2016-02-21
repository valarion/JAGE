package scrambleplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.GameState;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.StartState;
import com.valarion.gameengine.util.OrderedLinkedList;

public class Scores extends GameState {
	
	public static class Registry implements Serializable, Comparable<Registry> {
		private static final long serialVersionUID = -2083191512983275334L;
		
		public final int score;
		public final boolean win;
		public final String name;
		
		public Registry(int score, boolean win, String name) {
			this.score = score;
			this.win = win;
			this.name = name;
		}

		@Override
		public int compareTo(Registry arg0) {
			return -Integer.compare(score, arg0.score);
		}
	}
	
	public static final int limit = 10;
	
	protected OrderedLinkedList<Registry> wins = new OrderedLinkedList<Registry>();
	protected OrderedLinkedList<Registry> loses = new OrderedLinkedList<Registry>();
	protected OrderedLinkedList<Registry> mixed = new OrderedLinkedList<Registry>();
	
	@SuppressWarnings("unchecked")
	public Scores() {
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File("scores")));
			wins = (OrderedLinkedList<Registry>) input.readObject();
			loses = (OrderedLinkedList<Registry>) input.readObject();
			input.close();
			mixed.addAll(wins);
			mixed.addAll(loses);
		} catch (IOException | ClassNotFoundException e) {
			//e.printStackTrace();
		}
	}
	
	public Scores(int score, boolean win, String name) {
		this();
		Registry r = new Registry(score,win,name);
		mixed.add(r);
		if(win) {
			wins.add(r);
			if(wins.size() > limit) {
				mixed.remove(wins.get(10));
				wins.remove(10);
			}
		}
		else {
			loses.add(r);
			if(loses.size() > limit) {
				mixed.remove(loses.get(10));
				loses.remove(10);
			}
		}
		
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File("scores")));
			output.writeObject(wins);
			output.writeObject(loses);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
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
		int y = 2;
		for(Registry r : mixed) {
			g.getFont().drawString(2, y, Integer.toString(r.score));
			g.getFont().drawString(2+g.getFont().getWidth("000000000000 "), y,(r.win ? "win" : "loose"));
			g.getFont().drawString(2+g.getFont().getWidth("000000000000 loose     "), y, r.name);
			y+=g.getFont().getLineHeight()+2;
		}
	}

	@Override
	public void init(GameContainer container) throws Exception {
		// TODO Auto-generated method stub

	}

}
