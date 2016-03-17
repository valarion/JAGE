package com.valarion.gameengine.gamestates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.GameState;
import com.valarion.gameengine.events.menu.OptionsMenu.XPosition;
import com.valarion.gameengine.events.menu.OptionsMenu.YPosition;
import com.valarion.gameengine.events.menu.battlemenu.BattleMenu;
import com.valarion.gameengine.events.menu.battlemenu.BattleOption;
import com.valarion.gameengine.events.menu.battlemenu.BattleText;
import com.valarion.gameengine.events.menu.battlemenu.CommonOption;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.util.Util;

public class BattleState extends SubState {
	protected GameState next;
	protected Image enemy;

	protected int enemymaxhp;
	protected float enemyhp;

	protected int playermaxhp;
	protected float playerhp;

	protected Stance stance;

	protected int nextplayerhp;
	protected int nextenemyhp;

	protected Attack playerattack;
	protected Attack enemyattack;

	protected Set<Event> active;

	protected BattleMenu menu;

	protected BattleText text;
	protected BattleText tooltip;

	public static enum Attack {

		thrust("Thrust","Deals 50-60 HP. Negates \"feint and side cut\"."), 
		feintandsidecut("Feint and side cut","Deals 60-80 HP. Negates \"shield defense\"."), 
		shielddefense("Shield defense","Negates \"thrust\". Deals 10-30 damage if enemy used it."), 
		heal("Heal","Heals 55-65 HP.");
		
		private Attack(String name, String tooltip) {
			this.name = name;
			this.tooltip = tooltip;
		}
		
		protected final String name;
		protected final String tooltip;

		/**
		 * Returns whether this attack negates the effect of the contrary
		 * attack.
		 * 
		 * @param attack
		 * @return
		 */
		public boolean negates(Attack attack) {
			return (this.equals(attack) && (this.equals(thrust) || this.equals(feintandsidecut)))
					|| (this.equals(thrust) && attack.equals(feintandsidecut))
					|| (this.equals(feintandsidecut) && attack.equals(shielddefense))
					|| (this.equals(shielddefense) && attack.equals(thrust));
		}

		public int hpDealt(Attack oponentattack) {
			switch (this) {
			case feintandsidecut:
				return new Random().nextInt(11) + 50;
			case thrust:
				return new Random().nextInt(21) + 60;
			case shielddefense:
				if(oponentattack.equals(thrust)) {
					return new Random().nextInt(21) + 10;
				}
			default:
				return 0;
			}

		}

		public int hpHealed() {
			switch (this) {
			case heal:
				return new Random().nextInt(11) + 55;
			default:
				return 0;
			}
		}

		public String getToolTip() {
			return tooltip;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	public static enum Stance {
		playerturn, enemyturn, calculatenewhealth, win, loose, draw
	}

	public BattleState(GameState next, Image enemy, int enemyhp, int playerhp) {
		this.next = next;
		this.enemy = enemy;
		this.enemymaxhp = enemyhp;
		this.playermaxhp = playerhp;
		this.enemyhp = enemyhp;
		this.playerhp = playerhp;

		stance = Stance.playerturn;

		active = Util.getset();

		try {
			text = new BattleText(this,XPosition.right,YPosition.bot,"2x1");
			text.setOptions(new FlowEventInterface[] { new CommonOption(""), new CommonOption(""), new CommonOption(""),
					new CommonOption("Player: ") });
			text.setSelected(-1);
			tooltip = new BattleText(this,XPosition.center,YPosition.top,"loadtop");
			tooltip.setSelected(-1);
			menu = new BattleMenu(this);

		} catch (SlickException e) {
			e.printStackTrace();
			throw new NullPointerException();
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if (!active.isEmpty()) {
			Iterator<Event> it = active.iterator();
			it.next().paralelupdate(container, delta, null);
			it.remove();
		}
		switch (stance) {
		case playerturn:
			menu.paralelupdate(container, delta, null);
			break;
		case enemyturn:
			ArrayList<CommonOption> options = new ArrayList<CommonOption>();
			enemyattack = Attack.values()[new Random().nextInt(Attack.values().length-(enemyhp==enemymaxhp?1:0))];
			options.add(new CommonOption("You used " + playerattack.toString() + ". Enemy used " + enemyattack.toString()));

			int healed = playerattack.hpHealed();
			if (healed > 0) {
				options.add(new CommonOption("Player healed " + healed + " HP"));
			}
			nextplayerhp = (int) (playerhp + healed);
			if (nextplayerhp > playermaxhp) {
				nextplayerhp = playermaxhp;
			}
			healed = enemyattack.hpHealed();
			if (healed > 0) {
				options.add(new CommonOption("Enemy healed " + healed + " HP"));
			}
			nextenemyhp = (int) (enemyhp + healed);
			if (nextenemyhp > enemymaxhp) {
				nextenemyhp = enemymaxhp;
			}

			if (!playerattack.negates(enemyattack)) {
				int dealt = enemyattack.hpDealt(playerattack);
				if (dealt > 0) {
					options.add(new CommonOption("Enemy dealt " + dealt + " HP"));
				}
				nextplayerhp -= dealt;
				if (nextplayerhp < 0) {
					nextplayerhp = 0;
				}
			} else {
				options.add(new CommonOption("Enemy attack negated"));
			}
			if (!enemyattack.negates(playerattack)) {
				int dealt = playerattack.hpDealt(enemyattack);
				if (dealt > 0) {
					options.add(new CommonOption("Player dealt " + dealt + " HP"));
				}
				nextenemyhp -= dealt;
				if (nextenemyhp < 0) {
					nextenemyhp = 0;
				}
			} else {
				options.add(new CommonOption("Player attack negated"));
			}

			stance = Stance.calculatenewhealth;

			// System.out.println("Player: "+nextplayerhp+"\t\tEnemy:
			// "+nextenemyhp);
			// System.out.println();
			for(int i=options.size();i<3;i++) {
				options.add(new CommonOption(""));
			}
			options.add(new CommonOption("Player: "));
			text.setOptions(options.toArray(new FlowEventInterface[] {}));
			playerattack=null;
			enemyattack=null;
			break;
		case calculatenewhealth:
			if (playerhp != nextplayerhp) {
				if (playerhp > nextplayerhp) {
					playerhp -= delta * 0.02;
					if (playerhp < nextplayerhp) {
						playerhp = nextplayerhp;
					}
				} else {
					playerhp += delta * 0.02;
					if (playerhp > nextplayerhp) {
						playerhp = nextplayerhp;
					}
				}
			}
			if (enemyhp != nextenemyhp) {
				if (enemyhp > nextenemyhp) {
					enemyhp -= delta * 0.02;
					if (enemyhp < nextenemyhp) {
						enemyhp = nextenemyhp;
					}
				} else {
					enemyhp += delta * 0.02;
					if (enemyhp > nextenemyhp) {
						enemyhp = nextenemyhp;
					}
				}
			}
			text.getOptions()[text.getOptions().length - 1] = new CommonOption(
					"Player: ");

			if (playerhp == nextplayerhp && enemyhp == nextenemyhp) {
				if (playerhp == 0 && enemyhp != 0) {
					stance = Stance.loose;
				} else if (playerhp != 0 && enemyhp == 0) {
					stance = Stance.win;
				} else if (playerhp == 0 && enemyhp == 0) {
					stance = Stance.draw;
				} else {
					stance = Stance.playerturn;
				}
			}
			break;
		case draw:
			// Shouldn't happen with the battle system created.
			stance = Stance.loose;
		case loose:
			GameCore.getInstance().setActive(next);
			break;
		case win:
			GameCore.getInstance().setActive(next);
			break;
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		
		// Dialogs
		menu.postrender(container, g, 0, 0);
		text.postrender(container, g, 0, 0);
		if(stance.equals(Stance.playerturn)) {
			tooltip.setOptions(new CommonOption(((BattleOption)menu.getOptions()[menu.getSelected()]).getToolTip()));
			tooltip.postrender(container, g, 0, 0);
		}
		// Enemy
		if(enemy != null) {
			int maxw = container.getWidth();
			int maxh = container.getHeight() - menu.getWindow().getWindow().getHeight() - tooltip.getWindow().getWindow().getHeight()-text.getWindow().getContain().getGraphics().getFont().getLineHeight();
			float wratio = maxw / (float)enemy.getWidth();
			float hratio = maxh / (float)enemy.getHeight();
			float ratio = Math.min(wratio, hratio);
			float finalw = enemy.getWidth()*ratio;
			float finalh = enemy.getHeight()*ratio;
			
			float x = maxw/2-finalw/2;
			float y = maxh/2-finalh/2 + tooltip.getWindow().getWindow().getHeight();
			
			if(!stance.equals(Stance.calculatenewhealth) || nextenemyhp >= enemyhp || System.currentTimeMillis() % 400 < 200) {
				Color c = Color.white;
				if(nextenemyhp > enemyhp && System.currentTimeMillis() % 400 < 200) {
					c = Color.green;
				}
				g.drawImage(enemy, x, y, x+finalw, y+finalh, 0, 0, enemy.getWidth(), enemy.getHeight(),c);
			}
		}

		// Health bars
		Image healthbar = Database.instance().getImages().get("healthbar");
		int w = text.getWindow().getContain().getGraphics().getFont().getWidth("                                          ");
		int h = text.getWindow().getContain().getGraphics().getFont().getLineHeight();
		
		// Enemy health bar
		int x = container.getWidth()/2-w/2;
		int y = container.getHeight()-h-text.getWindow().getWindow().getHeight();
		
		g.setColor(Color.red);
		g.fillRect(x+w/100, y, w*98/100*enemyhp/enemymaxhp, h);
		
		g.drawImage(healthbar, x, y, x+w, y+h, 0, 0, healthbar.getWidth(), healthbar.getHeight());
		
		g.setColor(Color.white);
		g.drawString(Integer.toString((int)enemyhp), x+w+2, y);
		g.drawString(" / "+Integer.toString((int)enemymaxhp), x+w+2+g.getFont().getWidth(Integer.toString((int)enemymaxhp)), y);
	
		// Player health bar
		x = menu.getWindow().getWindow().getWidth()+menu.getWindow().getModel().getImage("left").getWidth()+text.getWindow().getContain().getWidth()/20+(int) (((float) (text.getWindow().getModel().getImage("rightArrow").getWidth())) * 1.5f)+text.getWindow().getContain().getGraphics().getFont().getWidth(" Player: ");
		y = (int) (container.getHeight()-text.getWindow().getWindow().getHeight()+text.getWindow().getModel().getImage("top").getHeight()+text.getWindow().getContain().getGraphics().getFont().getLineHeight()*6.5f);
	
		g.setColor(Color.red);
		g.fillRect(x+w/100, y, w*98/100*playerhp/playermaxhp, h);
		
		g.drawImage(healthbar, x, y, x+w, y+h, 0, 0, healthbar.getWidth(), healthbar.getHeight());
		
		g.setColor(Color.white);
		g.drawString(Integer.toString((int)playerhp), x+w+2, y);
		g.drawString(" / "+Integer.toString((int)playermaxhp), x+w+2+g.getFont().getWidth(Integer.toString((int)playermaxhp)), y);
	}

	@Override
	public Set<Event> getActiveEvents() {
		return active;
	}

	@Override
	public void init(GameContainer container) throws Exception {
		

	}

	public void setPlayerAttack(Attack attack) {
		this.playerattack = attack;
		this.enemyattack = null;
		this.stance = Stance.enemyturn;
	}
	
	public Stance getResult() {
		return stance;
	}

}
