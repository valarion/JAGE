package com.valarion.gameengine.gamestates;

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
		
		public int newOponentHp(int oponenthp) {
			switch(this) {
			case feintandsidecut:
				oponenthp -= new Random().nextInt(20)+60;
				break;
			case thrust:
				oponenthp -= new Random().nextInt(10)+50;
				break;
			default:
				break;
			}
			if(oponenthp < 0) {
				oponenthp = 0;
			}
			return oponenthp;
		}
		
		public int newSelfHp(int selfhp, int maxhp) {
			switch(this) {
			case heal:
				selfhp += new Random().nextInt(10)+55;
				break;
			default:
				break;
			}
			if(selfhp > maxhp) {
				selfhp = maxhp;
			}
			return selfhp;
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
			enemyattack = Attack.values()[new Random().nextInt(Attack.values().length)];
			
			nextplayerhp = playerattack.newSelfHp((int)playerhp, playermaxhp);
			nextenemyhp = enemyattack.newSelfHp((int)enemyhp, enemymaxhp);
			
			System.out.println("Player healed "+(nextplayerhp-playerhp));
			System.out.println("Enemy healed "+(nextenemyhp-enemyhp));
			if(!enemyattack.negates(playerattack)) {
				nextenemyhp = playerattack.newOponentHp((int)enemyhp);
			}
			else {
				System.out.println("Player attack negated");
			}
			if(!playerattack.negates(enemyattack)) {
				nextplayerhp = enemyattack.newOponentHp((int)enemyhp);
			}
			else {
				System.out.println("Enemy attack negated");
			}
			
			
			stance = Stance.calculatenewhealth;
			System.out.println("You used "+playerattack.name()+". Enemy used "+enemyattack.name());
			System.out.println("Player: "+nextplayerhp+"\t\tEnemy: "+nextenemyhp);
			break;
		case calculatenewhealth: 
			if(playerhp != nextplayerhp) {
				if(playerhp > nextplayerhp) {
					playerhp -= delta*0.01;
					if(playerhp < nextplayerhp) {
						playerhp = nextplayerhp;
					}
				}
				else {
					playerhp += delta*0.01;
					if(playerhp > nextplayerhp) {
						playerhp = nextplayerhp;
					}
				}
			}
			if(enemyhp != nextenemyhp) {
				if(enemyhp > nextenemyhp) {
					enemyhp -= delta*0.01;
					if(enemyhp < nextenemyhp) {
						enemyhp = nextenemyhp;
					}
				}
				else {
					enemyhp += delta*0.01;
					if(enemyhp > nextenemyhp) {
						enemyhp = nextenemyhp;
					}
				}
			}
			if(playerhp == 0 && enemyhp != 0) {
				stance = Stance.loose;
			}
			else if(playerhp != 0 && enemyhp == 0) {
				stance = Stance.win;
			}
			else if(playerhp == 0 && enemyhp == 0) {
				stance = Stance.draw;
			}
			else if(playerhp == nextplayerhp && enemyhp == nextenemyhp) {
				stance = Stance.playerturn;
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
