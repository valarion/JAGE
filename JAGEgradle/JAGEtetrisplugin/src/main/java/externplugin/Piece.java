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

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.valarion.gameengine.core.tiled.SubTiledMap;
import com.valarion.gameengine.core.tiled.TileSet;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;

/**
 * Class containing the states machine that works for every piece of a tetris game.
 * @author Rubén Tomás Gracia
 *
 */
public class Piece extends FlowEventClass {
	protected int piecetype;
	protected int pieceblock;
	
	protected int rotation = 0;
	
	protected int x = 0;
	protected int y = 0;
	protected int newx = 0;
	protected int newy = 0;
	protected int newrotation;
	
	protected static int piecesdefinitions[][][][] = new int[][][][]{
			/*I*/{{{0,-2},{0,-1},{0,0},{0,1}},{{-1,0},{0,0},{1,0},{2,0}}},
			/*O*/{{{0,0},{0,1},{1,0},{1,1}}},
			/*S*/{{{0,1},{0,0},{1,0},{1,-1}},{{-1,-1},{0,-1},{0,0},{1,0}}},
			/*Z*/{{{0,-1},{0,0},{1,0},{1,1}},{{-1,1},{0,1},{0,0},{1,0}}},
			/*T*/{{{1,0},{0,0},{0,1},{0,-1}},{{0,-1},{0,0},{-1,0},{1,0}},{{-1,0},{0,0},{0,-1},{0,1}},{{0,1},{0,0},{1,0},{-1,0}}},
			/*L*/{{{1,0},{0,0},{0,1},{0,2}},{{0,1},{0,0},{-1,0},{-2,0}},{{-1,0},{0,0},{0,-1},{0,-2}},{{0,-1},{0,0},{1,0},{2,0}}},
			/*J*/{{{0,-2},{0,-1},{0,0},{1,0}},{{2,0},{1,0},{0,0},{0,1}},{{0,2},{0,1},{0,0},{-1,0}},{{-2,0},{-1,0},{0,0},{0,-1}}},
};

	protected SubTiledMap map;

	protected Image tile;
	
	protected int state = 0;
	
	public Piece(int piecetype, int pieceblock, int centerx, int centery, SubTiledMap map) {
		this.piecetype = piecetype;
		this.pieceblock = pieceblock;
		this.map = map;
		
		this.x = centerx+piecesdefinitions[piecetype][rotation][pieceblock][1];
		this.y = centery+piecesdefinitions[piecetype][rotation][pieceblock][0];
		
		Database.instance().getContext().getGlobalVars()[Player.piececountregister]++;
		
		int tileId = piecetype+1;
		TileSet tileset = map.getTileSetByGID(tileId);
		tile = tileset.tiles.getSubImage(tileset.getTileX(tileId), tileset.getTileY(tileId));
		if(map.isBlocked(this.x, this.y)) {
			Database.instance().getContext().getGlobalVars()[Player.endgameregister] = 1;
		}
	}

	@Override
	public void paralelupdate(GameContainer container, int delta, SubTiledMap map) throws SlickException {
		switch(state) {
		case 0:
			newx = x;
			newy = y;
			newrotation = rotation;
			switch((int)Database.instance().getContext().getGlobalVars()[Player.stateregister]) {
			case Player.moveleftrightstate:
				newx = x+(int)Database.instance().getContext().getGlobalVars()[Player.movedirectionregister];
				break;
			case Player.movedownstate:
				newy = y+1;
				break;
			case Player.turnstate:
				newrotation = (rotation+1)%piecesdefinitions[piecetype].length;
				newx = x-piecesdefinitions[piecetype][rotation][pieceblock][1]+piecesdefinitions[piecetype][newrotation][pieceblock][1];
				newy = y-piecesdefinitions[piecetype][rotation][pieceblock][0]+piecesdefinitions[piecetype][newrotation][pieceblock][0];
				break;
			}
			if(newx != x || newy != y || newrotation != rotation) {
				if(map.isBlocked(newx, newy)) {
					Database.instance().getContext().getGlobalVars()[Player.cantmoveregister]++;
				}
				else {
					Database.instance().getContext().getGlobalVars()[Player.canmoveregister]++;
				}
				state = 1;
			}
			break;
		case 1:
			switch((int)Database.instance().getContext().getGlobalVars()[Player.stateregister]) {
			case Player.canmovestate:
				setXPos(newx);
				setYPos(newy);
				Database.instance().getContext().getGlobalVars()[Player.deletedlinesregister[pieceblock]] = getYPos();
				rotation = newrotation;
				state = 0;
				Database.instance().getContext().getGlobalVars()[Player.movedregister]++;
				break;
			case Player.cantmovestate:
				Database.instance().getContext().getGlobalVars()[Player.movedregister]++;
				state = 2;
				break;
			case Player.controlstate:
				state = 0;
				break;
			}
			break;
		case 2:
			if(Database.instance().getContext().getGlobalVars()[Player.stateregister] == Player.linedeletedstate) {
				int inity = y;
				int endy = inity;
				
				for(int i=0;i<4;i++) {
					if(Database.instance().getContext().getGlobalVars()[Player.deletedlinesregister[i]] > inity) {
						endy++;
					}
					else if(Database.instance().getContext().getGlobalVars()[Player.deletedlinesregister[i]] == inity) {
						Database.instance().getContext().getGlobalVars()[Player.piececountregister]--;
						endy = 0;
						map.remove(this);
						break;
					}
				}
				
				
				if(endy != 0) {
					Database.instance().getContext().getGlobalVars()[Player.updatedcountregister]++;
					setYPos(endy);
					state = 3;
				}
			}
			break;
		case 3:
			if(Database.instance().getContext().getGlobalVars()[Player.stateregister] != Player.linedeletedstate) {
				state = 2;
			}
			break;
		}

	}
	
	@Override
	public void stop() {
		state = 4;
	}

	@Override
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
		if(tile != null) {
			Color filter = (state != 4 ? Color.white : Color.darkGray);
			g.drawImage(tile, getXDraw(tilewidth), getYDraw(tileheight),filter);
		}
	}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public float getXDraw(int tileWidth) {
		return (getXPos() * tileWidth);
	}

	@Override
	public float getYDraw(int tileHeight) {
		return (getYPos() * tileHeight);
	}

	@Override
	public int getXPos() {
		return x;
	}

	@Override
	public int getYPos() {
		return y;
	}

	@Override
	public void setXPos(int newPos) {
		map.getEvents(x, y).remove(getEvent());
		x = newPos;
		map.add(getEvent());
	}

	@Override
	public void setYPos(int newPos) {
		map.getEvents(x, y).remove(getEvent());
		y = newPos;
		map.add(getEvent());
	}

	@Override
	public int getWidth() {
		if(tile != null){
			return tile.getWidth();
		}
		else {
			return 0;
		}
	}

	@Override
	public int getHeight() {
		if(tile != null){
			return tile.getHeight();
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean isBlocking() {
		return state>=2;
	}

	@Override
	public boolean isWorking() {
		return true;
	}

	@Override
	public String getId() {
		return "piece"+pieceblock;
	}
	
	public static final int centerx=14;
	public static final int centery=3;

	/**
	 * Create a piece of the type saved in the next piece global variable.
	 * @param container
	 * @param map
	 * @throws SlickException
	 */
	public static void generatePiece(GameContainer container, SubTiledMap map) throws SlickException {
		for(int i=0;i<4;i++) {
			Piece p = new Piece((int)Database.instance().getContext().getGlobalVars()[Player.nextregister], i,centerx,centery,map);
			map.add(p);
		}
	}

}
