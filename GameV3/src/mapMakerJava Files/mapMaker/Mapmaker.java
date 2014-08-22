package mapMaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Stack;

public class Mapmaker extends Map { //mapmaker class to distinguish it from map class

	private final MapObject searchObj = new GroundMapObject(width, height, 's', Color.yellow); //search object to temporary replace found objects
	
    Stack<UndoItem> unDO = new Stack<UndoItem>(); //stacks for the undo and redo functions
    Stack<UndoItem> reDO = new Stack<UndoItem>();
 	
    public Mapmaker(int rows, int cols, int blockwidth, int blockheight) { //comsturtor same as map class
		super(rows, cols, blockwidth, blockheight);
	}
    
    public Mapmaker(String s){ //same as map class
    	super(s);
    }

	public void drawGrid(Graphics g, int p_W, int p_H){ //draws grid
    	int w = (p_W - width * getCol())/ 2;
        int h = (p_H - height * getRow()) /2;
        
        int x1,x2,y1,y2;
        g.setColor(Color.cyan);
    	
        x1 = w;
    	x2 = w + width * getCol();
        
        for(int i = 1;i < map.length; i++){ //ever distance of the width it draws a new line for the grid
        	y1 = h + i * height;
        	y2 = y1;
        	
        	g.drawLine(x1, y1, x2, y2);	
        }
        
    	y1 = h;
    	y2 = h+height*getRow();
    	
        for(int i = 1;i < map[0].length; i++){ //same as for loop abive
        	x1 = w + i * width;
        	x2 = x1;
     	
        	g.drawLine(x1, y1, x2, y2);	
        }
        
    }
	
	public void undo(){ //undoes the last action
		if(!unDO.isEmpty()){ //will only undo if there are items on the stack
			UndoItem undo = unDO.pop(); //pops from stack
			reDO.push(new UndoItem(undo.points, add(undo.mapObjs, undo.points))); //pushes this undo onto the redo stack and undos the action
		}
	}
	
	public void redo(){ //redoes the last move
		if(!reDO.isEmpty()){
			UndoItem redo = reDO.pop(); //pops from stack
			unDO.push(new UndoItem(redo.points,add(redo.mapObjs, redo.points))); //pushes the redo onto the undo stack and redoes the action
		}
	}
	
	public MapObject [] add(MapObject [] m, Point [] p){ //adds an array of map objects at an array of points to the grid  ( used for undo and redo fncs)
		MapObject [] mapObjs = new MapObject [p.length];
		for(int i = 0; i < p.length; i++){
			try{
				mapObjs[i] = map[p[i].x][p[i].y];
				map[p[i].x][p[i].y] = m[i];
			}
			catch(NullPointerException e){
			}
		}
		
		return mapObjs;
	}
    
    public void add(MapObject m, Point p, boolean b){ //adds a map object at point p
    	Point [] points = {p};
    	MapObject [] objs = {map[p.x][p.y]};
    	if(!b)
    		map[p.x][p.y] = m; //changes the object at the poiunt
    	else{
    		houseItemLayer[p.x][p.y] = m;
    	}
    	if(p != null){
    		unDO.push(new UndoItem(points, objs)); //pushes the undo onto the stack for undoing the action when user initaites it
    	}
    }
    
    public void add(MapObject m, Point [] coords, boolean isLayer2){ //adds a map object at every point in the array of points
    	MapObject [] mapObjs = new MapObject [coords.length]; //used for the undo
    	if(!isLayer2){
    		for(int i =0; i < coords.length && coords[i] != null; i++){
    			try{
    				mapObjs[i] = map[coords[i].x][coords[i].y];
    				map[coords[i].x][coords[i].y] = m; //changes the object at the point
    			}
    			catch(ArrayIndexOutOfBoundsException e){ //if something goes wrong with the parameters an eror doesnt occur
    			
    			}
    		}
    	}
    	else{
    		for(int i =0; i < coords.length && coords[i] != null; i++){
    			try{
    				mapObjs[i] = map[coords[i].x][coords[i].y];
    				houseItemLayer[coords[i].x][coords[i].y] = m; //changes the object at the point
    			}
    			catch(ArrayIndexOutOfBoundsException e){ //if something goes wrong with the parameters an eror doesnt occur
    			
    			}
    		}
    	}
    	
    	unDO.push(new UndoItem(coords, mapObjs)); //pushes onto the undo stack
    }
    
    public void clearMap(){ //sets every point on the maap to a white  map object
    	MapObject clrBlk = new GroundMapObject(width,height,' ',Color.white);
    	for(int i = 0; i < map.length; i++){
    		for(int j = 0; j < map[0].length; j++){
    			map[i][j] = clrBlk;
    		}
    	}
    		
    }

    public MapObject getSearchObj() {
		return this.searchObj; //returns the searched object
	}
	
	class UndoItem{ //class undo item for pushing onto the stack and for easy accessing
		Point [] points; //an array of points where changes were made
		MapObject [] mapObjs; //an array of objects where changes were made
		
		public UndoItem(Point [] p, MapObject[] m){ //constructor
			points = new Point [p.length];
			mapObjs = new MapObject [m.length];
			for(int i = 0; i < m.length; i++){ //copies the parameter arrays into the data field of this class
				points[i] = p[i];
				mapObjs[i] = m[i];
			}
			
		}
	}
}
