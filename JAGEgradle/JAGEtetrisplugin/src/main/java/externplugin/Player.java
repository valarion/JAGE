/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Rubén Tomás Gracia
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package externplugin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.Event;
import com.valarion.gameengine.core.GameCore;
import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventInterface;
import com.valarion.gameengine.gamestates.Controls;
import com.valarion.gameengine.gamestates.Database;
import com.valarion.gameengine.gamestates.InGameState;
import com.valarion.gameengine.gamestates.SubState;
import com.valarion.pluginsystem.ClassOverrider;

/**
 * Class Overwriting the player class in the modules to have a player that
 * doesn't render and that controlls a tetris game.
 * 
 * @author Rubén Tomás Gracia
 *
 */
public class Player extends com.valarion.gameengine.events.Player {
	private static final long serialVersionUID = -1220851568209641413L;

	InGameState state;

	Map<String, Boolean> pressed = new ConcurrentHashMap<String, Boolean>();

	public Player() {

	}

	public Map<String, Boolean> getPressed() {
		return pressed;
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {

	}

	protected static final int nextregister = 0;
	protected static final int stateregister = 60;
	protected static final int movedirectionregister = 61;
	protected static final int canmoveregister = 62;
	protected static final int cantmoveregister = 63;
	protected static final int movedregister = 64;
	protected static final int piececountregister = 65;
	protected static final int updatedcountregister = 66;
	protected static final int deletedlinesregister[] = new int[] { 67, 68, 69, 70 };
	protected static final int endgameregister = 71;
	protected static final int clearedlinesregister = 72;
	protected static final int punctuationregister = 73;
	protected static final int levelregister = 74;

	protected static final int controlstate = 0;
	protected static final int movedownstate = 1;
	protected static final int moveleftrightstate = 2;
	protected static final int turnstate = 3;
	protected static final int canmovestate = 4;
	protected static final int cantmovestate = 5;
	protected static final int linedeletedstate = 6;
	protected static final int generatenewpiecestate = 7;
	protected static final int endgamestate = 8;

	protected static final long maximum = 1000;
	protected static final int minimum = 50;

	protected long timecount;
	protected long timelimit = maximum;
	protected long previouslimit;

	protected static final int startx = 9, endx = 18;
	protected static final int starty = 1, endy = 22;

	protected int x, y;

	protected int lastleft, lastright;
	public static final int replimit = 250;

	@Override
	public void update(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		Input input = container.getInput();
		lastleft += delta;
		lastright += delta;
		if (input.isKeyPressed(Controls.cancel)) {
			((SubState) GameCore.getInstance().getActive()).getActiveEvents()
					.add(new PauseMenu(((SubState) GameCore.getInstance().getActive())));
		} else {
			switch ((int) Database.instance().getContext().getGlobalVars()[stateregister]) {
			case controlstate:
				// control
				previouslimit = (long) ((maximum - minimum)
						* Math.pow(0.99, Database.instance().getContext().getGlobalVars()[levelregister]) + minimum);

				if (timelimit > 0) {
					if (input.isKeyDown(Controls.moveDown)) {
						timelimit = minimum;
					} else {
						timelimit = previouslimit;
					}
				}

				if ((int) Database.instance().getContext().getGlobalVars()[endgameregister] != 0) {
					x = startx;
					y = endy;
					timecount = 0;
					Database.instance().stopMusic("tetris");
					Database.instance().getContext().getGlobalVars()[stateregister] = endgamestate;
				} else if (input.isKeyPressed(Controls.moveLeft)
						|| (input.isKeyDown(Controls.moveLeft) && lastleft > replimit)) {
					lastleft = 0;
					Database.instance().getContext().getGlobalVars()[stateregister] = moveleftrightstate;
					Database.instance().getContext().getGlobalVars()[movedirectionregister] = -1;
				} else if (input.isKeyPressed(Controls.moveRight)
						|| (input.isKeyDown(Controls.moveRight) && lastright > replimit)) {
					lastright = 0;
					Database.instance().getContext().getGlobalVars()[stateregister] = moveleftrightstate;
					Database.instance().getContext().getGlobalVars()[movedirectionregister] = 1;
				} else if (input.isKeyPressed(Controls.moveUp)) {
					Database.instance().getContext().getGlobalVars()[stateregister] = turnstate;
					Database.instance().getContext().getGlobalVars()[movedirectionregister] = 2;
				} else if (input.isKeyPressed(Controls.accept)) {
					timelimit = 0;
				} else {
					if (timecount > timelimit) {
						Database.instance().getContext().getGlobalVars()[stateregister] = 1;
						Database.instance().getContext().getGlobalVars()[movedirectionregister] = 0;
						timecount = 0;
					} else {
						timecount += delta;
					}
				}
				Database.instance().getContext().getGlobalVars()[canmoveregister] = 0;
				break;
			case movedownstate:
				// move down
			case moveleftrightstate:
				// move left/right
			case turnstate:
				// turn
				if (Database.instance().getContext().getGlobalVars()[canmoveregister] >= 4) {
					Database.instance().getContext().getGlobalVars()[stateregister] = canmovestate;
					Database.instance().getContext().getGlobalVars()[movedregister] = 0;
				} else if (Database.instance().getContext().getGlobalVars()[cantmoveregister] > 0
						&& Database.instance().getContext().getGlobalVars()[cantmoveregister]
								+ Database.instance().getContext().getGlobalVars()[canmoveregister] >= 4) {
					if (Database.instance().getContext().getGlobalVars()[stateregister] == movedownstate) {
						Database.instance().getContext().getGlobalVars()[stateregister] = cantmovestate;
						Database.instance().getContext().getGlobalVars()[movedregister] = 0;
					} else {
						Database.instance().getContext().getGlobalVars()[stateregister] = controlstate;
					}
				}
				break;
			case canmovestate:
				// can move
				if (Database.instance().getContext().getGlobalVars()[movedregister] >= 4) {
					Database.instance().getContext().getGlobalVars()[stateregister] = controlstate;
				}
				break;
			case cantmovestate:
				// can't move
				if (Database.instance().getContext().getGlobalVars()[movedregister] >= 4) {
					for (int i = 0; i < deletedlinesregister.length; i++) {
						for (int j = i + 1; j < deletedlinesregister.length; j++) {
							if (Database.instance().getContext().getGlobalVars()[deletedlinesregister[i]] == Database
									.instance().getContext().getGlobalVars()[deletedlinesregister[j]]) {
								Database.instance().getContext().getGlobalVars()[deletedlinesregister[j]] = 0;
							}
						}
					}

					for (int i = 0; i < deletedlinesregister.length; i++) {
						if (Database.instance().getContext().getGlobalVars()[deletedlinesregister[i]] > 0) {
							for (int j = startx; j <= endx; j++) {
								if (!map.isBlocked(j, (int) Database.instance().getContext()
										.getGlobalVars()[deletedlinesregister[i]])) {
									Database.instance().getContext().getGlobalVars()[deletedlinesregister[i]] = 0;
									break;
								}
							}
						}
					}

					int cleared = 0;

					for (int i = 0; i < deletedlinesregister.length; i++) {
						if (Database.instance().getContext().getGlobalVars()[deletedlinesregister[i]] > 0) {
							cleared++;
						}
					}

					int punctuation = 0;

					switch (cleared) {
					case 1:
						Database.instance().playSound("line", 400.f, 1.f);
						punctuation = 40;
						break;
					case 2:
						Database.instance().playSound("twolines", 400.f, 1.f);
						punctuation = 100;
						break;
					case 3:
						Database.instance().playSound("threelines", 400.f, 1.f);
						punctuation = 300;
						break;
					case 4:
						Database.instance().playSound("fourlines", 400.f, 1.f);
						punctuation = 1200;
						break;
					}
					punctuation *= Database.instance().getContext().getGlobalVars()[levelregister] + 1;
					Database.instance().getContext().getGlobalVars()[punctuationregister] += punctuation;
					Database.instance().getContext().getGlobalVars()[clearedlinesregister] += cleared;
					Database.instance().getContext().getGlobalVars()[levelregister] = Database.instance().getContext()
							.getGlobalVars()[clearedlinesregister] / 10;

					Database.instance().getContext().getGlobalVars()[updatedcountregister] = 0;
					Database.instance().getContext().getGlobalVars()[stateregister] = linedeletedstate;
				}
				break;
			case linedeletedstate:
				// line deleted
				if (Database.instance().getContext().getGlobalVars()[piececountregister] == Database.instance()
						.getContext().getGlobalVars()[updatedcountregister]) {
					Database.instance().getContext().getGlobalVars()[stateregister] = generatenewpiecestate;
				}
				break;
			case generatenewpiecestate:
				// Generate new piece
				Piece.generatePiece(container, map);
				Database.instance().getContext().getGlobalVars()[stateregister] = controlstate;
				timecount = 0;
				if (timelimit < previouslimit) {
					timelimit = previouslimit;
				}
				generateNextPiece();
				break;
			case endgamestate:
				if (timecount > 100) {
					timecount = 0;
					if (y >= starty) {
						boolean stop = false;
						while (!stop) {
							for (Event e : map.getEvents(x, y)) {
								if (e instanceof FlowEventInterface) {
									((FlowEventInterface) e).stop();
									stop = true;
								}
							}

							if (y % 2 == 0) {
								x++;
								if (x > endx) {
									x = endx;
									y--;
								}
							} else {
								x--;
								if (x < startx) {
									x = startx;
									y--;
								}
							}

							if (y < starty) {
								stop = true;
							}
						}
						Database.instance().playSound("beep");
					} else {
						GameCore.getInstance().setActive(new GameOver("Game over",
								(int) Database.instance().getContext().getGlobalVars()[punctuationregister], true));
					}
				} else {
					timecount += delta;
				}
				break;
			}
		}
	}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public void prerender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {

	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
	}

	@Override
	public void postrender(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {

	}

	@Override
	public float getXDraw(int tilewidth) {
		return (getXPos() * tilewidth + tilewidth / 2 - getWidth() / 2 + xOff);
	}

	@Override
	public float getYDraw(int tileheight) {
		return ((getYPos() + 1) * tileheight - getHeight() + yOff);
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void onMapLoad(GameContainer container, SubTiledMap map) {
	}

	@Override
	public void onMapSetAsActive(GameContainer container, SubTiledMap map) {
		Database.instance().getContext().getGlobalVars()[endgameregister] = 0;
		Database.instance().getContext().getGlobalVars()[clearedlinesregister] = 0;
		Database.instance().getContext().getGlobalVars()[punctuationregister] = 0;
		Database.instance().getContext().getGlobalVars()[levelregister] = 0;
		Database.instance().loopMusic("tetris",0.1f,1.f);
		Database.instance().getContext().getGlobalVars()[stateregister] = generatenewpiecestate;
		state.getCamera().focusAt(map);
		generateNextPiece();
	}

	protected static void generateNextPiece() {
		Random rng = new Random();
		int type = rng.nextInt(10);
		switch (type) {
		case 0:
		case 1:
			type = 0;
			break;
		case 2:
		case 3:
			type = 1;
			break;
		case 4:
			type = 2;
			break;
		case 5:
			type = 3;
			break;
		case 6:
		case 7:
			type = 4;
			break;
		case 8:
			type = 5;
			break;
		case 9:
			type = 6;
			break;
		}
		Database.instance().getContext().getGlobalVars()[Player.nextregister] = type;
	}

	@Override
	public void onEventActivation(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void onEventTouch(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void onBeingTouched(GameContainer container, SubTiledMap map, Event e) {
	}

	@Override
	public void loadEvent(Element node, Object context) throws SlickException {
		if (context instanceof InGameState) {
			state = (InGameState) context;
		}
	}

	@Override
	public int getXPos() {
		if (state != null && state.getActive() != null) {
			return state.getActive().getWidth() / 2;
		}
		return 0;
	}

	@Override
	public int getYPos() {
		if (state != null && state.getActive() != null) {
			return state.getActive().getHeight() / 2;
		}
		return 0;
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public int getDirection() {
		return 0;
	}

	@Override
	public void setDirection(int direction) {

	}

	@Override
	public boolean isWorking() {
		return true;
	}

	@Override
	public void performAction(GameContainer container, SubTiledMap map, Event e) throws SlickException {

	}

	@Override
	public void onMapSetAsInactive(GameContainer container, SubTiledMap map) throws SlickException {
		Database.instance().stopMusic("tetris");
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@ClassOverrider
	public static Class<?> override(Class<?> c) {
		return Player.class;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
	}
}
