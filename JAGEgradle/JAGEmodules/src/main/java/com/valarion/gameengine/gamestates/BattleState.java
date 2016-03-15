package com.valarion.gameengine.gamestates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.GameState;
import com.valarion.gameengine.events.menu.battlemenu.BattleMenu;
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
	
	public static enum Attack {
		
		thrust,feintandsidecut,shielddefense,heal;
		
		/**
		 * Returns whether this attack negates the effect of the contrary attack.
		 * @param attack
		 * @return
		 */
		public boolean negates(Attack attack) {
			return (this.equals(thrust)        && attack.equals(feintandsidecut)) ||
				   (this.equals(shielddefense) && attack.equals(thrust));
		}
		
		public int hpDealt() {
			switch(this) {
			case feintandsidecut:
				return new Random().nextInt(20)+60;
			case thrust:
				return new Random().nextInt(10)+50;
			default:
				return 0;
			}
			
		}
		
		public int hpHealed() {
			switch(this) {
			case heal:
				return new Random().nextInt(10)+55;
			default:
				return 0;
			}
		}
	}
	
	public static enum Stance {
		playerturn,enemyturn,calculatenewhealth,win,loose,draw
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
			text = new BattleText(this);
			menu = new BattleMenu(this);
			
		} catch (SlickException e) {
			e.printStackTrace();
			throw new NullPointerException();
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(!active.isEmpty()) {
			Iterator<Event> it = active.iterator();
			it.next().paralelupdate(container, delta, null);
			it.remove();
		}
		switch(stance) {
		case playerturn:
			menu.paralelupdate(container, delta, null);
			break;
		case enemyturn:
			ArrayList<CommonOption> options = new ArrayList<CommonOption>();
			enemyattack = Attack.values()[new Random().nextInt(Attack.values().length)];
			options.add(new CommonOption("You used "+playerattack.name()+". Enemy used "+enemyattack.name()));

			int healed = playerattack.hpHealed();
			if(healed > 0) {
				options.add(new CommonOption("Player healed "+healed+" HP"));
			}
			nextplayerhp = (int) (playerhp+healed);
			if(nextplayerhp > playermaxhp) {
				nextplayerhp = playermaxhp;
			}
			healed = enemyattack.hpHealed();
			if(healed > 0) {
				options.add(new CommonOption("Enemy healed "+healed+" HP"));
			}
			nextenemyhp = (int) (enemyhp+healed);
			if(nextenemyhp > enemymaxhp) {
				nextenemyhp = enemymaxhp;
			}
			
			if(!playerattack.negates(enemyattack)) {
				int dealt = enemyattack.hpDealt();
				if(dealt > 0) {
					options.add(new CommonOption("Enemy dealt "+dealt+" HP"));
				}
				nextplayerhp -= dealt;
				if(nextplayerhp < 0) {
					nextplayerhp = 0;
				}
			}
			else {
				options.add(new CommonOption("Enemy attack negated"));
			}
			if(!enemyattack.negates(playerattack)) {
				int dealt = playerattack.hpDealt();
				if(dealt > 0) {
					options.add(new CommonOption("Player dealt "+dealt+" HP"));
				}
				nextenemyhp -= dealt;
				if(nextenemyhp < 0) {
					nextenemyhp = 0;
				}
			}
			else {
				options.add(new CommonOption("Player attack negated"));
			}
			
			
			
			stance = Stance.calculatenewhealth;
			
			//System.out.println("Player: "+nextplayerhp+"\t\tEnemy: "+nextenemyhp);
			//System.out.println();
			options.add(new CommonOption("Player: "+playerhp+"\t\tEnemy: "+enemyhp));
			text.setOptions(options.toArray(new FlowEventInterface[]{}));
			text.setSelected(-1);
			break;
		case calculatenewhealth: 
			if(playerhp != nextplayerhp) {
				if(playerhp > nextplayerhp) {
					playerhp -= delta*0.02;
					if(playerhp < nextplayerhp) {
						playerhp = nextplayerhp;
					}
				}
				else {
					playerhp += delta*0.02;
					if(playerhp > nextplayerhp) {
						playerhp = nextplayerhp;
					}
				}
			}
			if(enemyhp != nextenemyhp) {
				if(enemyhp > nextenemyhp) {
					enemyhp -= delta*0.02;
					if(enemyhp < nextenemyhp) {
						enemyhp = nextenemyhp;
					}
				}
				else {
					enemyhp += delta*0.02;
					if(enemyhp > nextenemyhp) {
						enemyhp = nextenemyhp;
					}
				}
			}
			text.getOptions()[text.getOptions().length-1] = new CommonOption("Player: "+(int)playerhp+"   Enemy: "+(int)enemyhp);
			
			if(playerhp == nextplayerhp && enemyhp == nextenemyhp) {
				if(playerhp == 0 && enemyhp != 0) {
					stance = Stance.loose;
				}
				else if(playerhp != 0 && enemyhp == 0) {
					stance = Stance.win;
				}
				else if(playerhp == 0 && enemyhp == 0) {
					stance = Stance.draw;
				}
				else {
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
		menu.postrender(container, g, 0, 0);
		text.postrender(container, g, 0, 0);
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Event> getActiveEvents() {
		return active;
	}

	@Override
	public void init(GameContainer container) throws Exception {
		// TODO Auto-generated method stub

	}

	public void setPlayerAttack(Attack attack) {
		this.playerattack = attack;
		this.enemyattack = null;
		this.stance = Stance.enemyturn;
	}

}
