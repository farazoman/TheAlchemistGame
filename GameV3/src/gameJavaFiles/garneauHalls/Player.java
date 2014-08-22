package garneauHalls;

import java.awt.Image;
import java.awt.Point;

public class Player {

	protected Image west;// = loadImage("Char Pics/left 1.png");
	protected Image east;// =  loadImage("Char Pics/Right 1.png");
	protected Image north;// =  loadImage("Char Pics/Up 1.png");
	protected Image south;// =  loadImage("Char Pics/Front 1.png");
	protected Image westL;// =  loadImage("Char Pics/left 2.png");
	protected Image westR;// =  loadImage("Char Pics/left 3.png");
	protected Image eastL;// =  loadImage("Char Pics/Right 2.png");
	protected Image eastR;// = loadImage("Char Pics/Right 3.png");
	protected Image northL;// = loadImage("Char Pics/Up 2.png");
	protected Image northR;// = loadImage("Char Pics/Up 3.png");
	protected Image southL;// = loadImage("Char Pics/Front 2.png");
	protected Image southR;// = loadImage("Char Pics/Front 3.png");
	protected Image punchN, punchS, punchE, punchW;
	protected Image currentImage;
	
	public boolean up, down, left, right; //used for velocity
	public boolean u, d, l, r; //used for direction facing
	
	public double x, y;
	protected Point mapLoc;	
	protected StatPoints stats;
	protected Item [] inventory;
	
	public Player(Image north,Image south,Image east, Image west, //constructor 
			Image northL,Image northR,Image southL,Image southR,Image eastL,Image eastR,Image westL,Image westR,
			Point loc) {
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		this.northL = northL;
		this.northR = northR;
		this.southL = southL;
		this.southR = southR;
		this.eastL = eastL;
		this.eastR = eastR;
		this.westL = westL;
		this.westR = westR;
		this.mapLoc = loc;
		
		up = false; down = false; left = false; right = false; 
		u = false; d = true; l = false; r = false;//set the player facing down
		
		dirFacing();

		stats = new StatPoints(20,20);
		inventory = new Item [20];
		
	}
	
	public Player (Image north, Image south, Image east, Image west, Point loc){ //used in friend class
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		this.mapLoc = loc;
		up = false; down = false; left = false; right = false;
		dirFacing();
	}
	
	public Player(Image north,Image south,Image east, Image west, //final constructor with all imgaes and llocatin
			Image northL,Image northR,Image southL,Image southR,Image eastL,Image eastR,Image westL,Image westR,
			Image punchN,  Image punchS,  Image punchE,  Image punchW,
			Point loc) {
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		this.northL = northL;
		this.northR = northR;
		this.southL = southL;
		this.southR = southR;
		this.eastL = eastL;
		this.eastR = eastR;
		this.westL = westL;
		this.westR = westR;
		this.punchN = punchN;
		this.punchS = punchS;
		this.punchE = punchE;
		this.punchW = punchW;
		this.mapLoc = loc;
		
		up = false; down = false; left = false; right = false;
		u = false; d = true; l = false; r = false;
		
		dirFacing();

		stats = new StatPoints(100,20);
		inventory = new Item [20];
		
	}
	
	public Player(Player that){ //copies object
		this.north = that.north;
		this.south = that.south;
		this.east = that.east;
		this.west = that.west;
		this.northL = that.northL;
		this.northR = that.northR;
		this.southL = that.southL;
		this.southR = that.southR;
		this.eastL = that.eastL;
		this.eastR = that.eastR;
		this.westL = that.westL;
		this.westR = that.westR;
		this.mapLoc = that.mapLoc;

		up = false; down = false; left = false; right = false;
		u = false; d = true; l = false; r = false;
		
		dirFacing();

		stats = new StatPoints(100,100);
		inventory = new Item [20];
	}
	
	public void dirFacing(){ //sets current image to dir facing
		if(d){
			currentImage = south;
		}
		else if(u){
			currentImage = north;
		}
		else if(r){
			currentImage = east;
		}
		else if(l){
			currentImage = west;
		}
	}
	
	public boolean isAlive(){ //returns boolean if is alive
		if(stats.isAlive()){
			currentImage = null;
		}
		return !stats.isAlive();
	}
	
	public Point getDir(){ //gets the direction as a point object
		int x = 0, y = 0;
		if(d){
			y = 1;
		}
		else if(u){
			y = -1;
		}
		else if(r){
			x = 1;
		}
		else if(l){
			x = -1;
		}
		return new Point(x,y);
	}
	
	public void punch(){ //changes the punch image based on the direction
		if(d){
			currentImage = punchS;
		}
		else if(u){
			currentImage = punchN;
		}
		else if(r){
			currentImage = punchE;
		}
		else if(l){
			currentImage = punchW;
		}
	}
	
	public StatPoints getStats(){ // returns statpoints object used for giveinng to the enemy
		return stats;
	}
	
	public void step(boolean walkLeft){//steps changes the left foot or right foot
		
		if(up)
		{
			u = true;
			d = false;
			r = false;
			l = false;
			if(walkLeft)
				currentImage = northL;
			else
				currentImage = northR;
		}
		else if(down)
		{
			d = true;
			u = false;
			l = false;
			r = false;
			if(walkLeft)
				currentImage = southL;
			else 
				currentImage = southR;
		}
		else if(right)
		{
			r = true;
			l = false;
			u = false;
			d = false;
			if(walkLeft)
				currentImage = eastL;
			else
				currentImage = eastR;
		}	
		else if(left)
		{
			l = true;
			r = false;
			d = false;
			u = false;
			if(walkLeft)
				currentImage = westL;
			else
				currentImage = westR;
		}
	}
	
	public Point getLoc(){ 
		return mapLoc;
	}
	
	public void setLoc(Point p){
		mapLoc = p;
	}
	
	public Image getCurrentImage(){
		return currentImage;
	}

	
}
