package externplugin;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TileSet;

import com.valarion.gameengine.core.SubTiledMap;
import com.valarion.gameengine.events.rpgmaker.FlowEventClass;
import com.valarion.gameengine.gamestates.Database;

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
			/*Z*/{{{-1,-1},{0,-1},{0,0},{1,0}},{{0,-1},{0,0},{-1,0},{-1,1}}},
			/*S*/{{{-1,1},{0,1},{0,0},{1,0}},{{0,1},{0,0},{-1,0},{-1,-1}}},
			/*T*/{{{1,0},{0,0},{0,1},{0,-1}},{{0,-1},{0,0},{-1,0},{1,0}},{{-1,0},{0,0},{0,-1},{0,1}},{{0,1},{0,0},{1,0},{-1,0}}},
			/*J*/{{{0,-2},{0,-1},{0,0},{1,0}},{{2,0},{1,0},{0,0},{0,1}},{{0,2},{0,1},{0,0},{-1,0}},{{-2,0},{-1,0},{0,0},{0,-1}}},
			/*L*/{{{1,0},{0,0},{0,1},{0,2}},{{0,1},{0,0},{-1,0},{-2,0}},{{-1,0},{0,0},{0,-1},{0,-2}},{{0,-1},{0,0},{1,0},{2,0}}},
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
	public void render(GameContainer container, Graphics g, int tilewidth, int tileheight) throws SlickException {
		if(tile != null) {
			g.drawImage(tile, getXDraw(tilewidth), getYDraw(tileheight));
		}
	}

	@Override
	public String getLayerName() {
		return "player";
	}

	@Override
	public int getXDraw(int tileWidth) {
		return (int) (getXPos() * tileWidth);
	}

	@Override
	public int getYDraw(int tileHeight) {
		return (int) (getYPos() * tileHeight);
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
	
	public static final int centerx=13;
	public static final int centery=7;

	public static void generatePiece(GameContainer container, SubTiledMap map) throws SlickException {
		
		
		for(int i=0;i<4;i++) {
			Piece p = new Piece((int)Database.instance().getContext().getGlobalVars()[Player.nextregister], i,centerx,centery,map);
			map.add(p);
		}
	}

}
