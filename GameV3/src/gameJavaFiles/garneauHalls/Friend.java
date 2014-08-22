package garneauHalls;

import java.awt.Image;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class Friend extends Player { 
	
	Timer t; //timer for turning around
	Movement ttStill; //timer task
	String dialoge; //what the friend says when we talk to them
	Item item; //not used
	
	public Friend (Image north, Image south, Image east, Image west, Point loc, String dialoge){ //only constructor that is used
		super(north,south,east,west, loc);
		this.dialoge = dialoge;
		
		x = mapLoc.x;
		y = mapLoc.y;
		
		t = new Timer("StandStill");
		ttStill = new Movement();
		
		t.schedule(ttStill, 0, 800);
	}

	public Friend(Image north, Image south, Image east, Image west, //not used
			Image northL, Image northR, Image southL, Image southR,
			Image eastL, Image eastR, Image westL, Image westR, Point loc, Item item, String dialoge) {
		super(north, south, east, west, northL, northR, southL, southR, eastL,
				eastR, westL, westR, loc);
		
		this.dialoge = dialoge;
		this.item = item;
		
		x = mapLoc.x;
		y = mapLoc.y;
		
		t = new Timer("StandStill");
		ttStill = new Movement();
		
		t.schedule(ttStill, 0, 700);
	}

	public Friend(Player that, String dialoge) { //not used
		super(that);
		
		this.dialoge = dialoge;
		
		x = mapLoc.x;
		y = mapLoc.y;
		
		t = new Timer("StandStill");
		ttStill = new Movement();
		
		t.schedule(ttStill, 0, 700);
	}
	
	public void talk(Point p){ //this turns the friend to the direction of the person he's talking to
		int tempX = p.x , tempY = p.y;

		if(tempY > mapLoc.y){
			d = true;
			u = false;
			l = false;
			r = false;
		}
		else if(tempY < mapLoc.y){
			d = false;
			u = true;
			l = false;
			r = false;
		
		}
		else if(tempX < mapLoc.x){
			d = false;
			u = false;
			l = true;
			r = false;
		}
		else if(tempX < mapLoc.x){
			d = false;
			u = false;
			l = false;
			r = true;
		}
		dirFacing();
	}
	public String talk(){ //returns the players dialogs
		return dialoge;
	}
	
	@Override
	public void setLoc(Point p){ //sets location of friend including the x and y variable used for drawing
		mapLoc = p;
		x = mapLoc.x;
		y = mapLoc.y;
	}
	
	
	class Movement extends TimerTask{ //timer task to randomly turn around 
		public Movement(){
		}
		
		public void turn(){
			int x =(int)( Math.random()*4);
			switch (x){ //randomly turns
				case 0:
					currentImage = north;
					u = true;
					d = false;
					r = false;
					l = false;
					break;
				case 1:
					currentImage = south;
					u = false;
					d = true;
					r = false;
					l = false;
					break;
				case 2:
					currentImage = east;
					u = false;
					d = false;
					r = true;
					l = false;
					break;
				case 3:
					currentImage = west;
					u = false;
					d = false;
					r = false;
					l = true;
					break;
					
			}
		}

		@Override
		public void run() { //timer task calls turn mmethod
			turn();
		}

	}
}
