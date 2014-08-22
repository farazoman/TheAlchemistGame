package garneauHalls;

import java.awt.Image;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Player {
	
	Timer t1, t2;
	Movement ttStill;
	Chase ttChase;
	boolean t2Run;
	Point playerLoc;
	long sleepNano;
	GameMap map;
	Enemy [] enemies; //used for not walking on each other
	int index;
	StatPoints bobStats; //used to lower bob's health
	
	public boolean walkLeft;
	
	public Enemy(Image north, Image south, Image east, Image west,
			Image northL, Image northR, Image southL, Image southR,
			Image eastL, Image eastR, Image westL, Image westR, Point loc,
			int health, int strength, int coins, Item spoils, long sleep, GameMap map) {
		
		super(north, south, east, west, northL, northR, southL, southR, eastL,
				eastR, westL, westR, loc);
		
		x = mapLoc.x;
		y = mapLoc.y;
		stats = new StatPoints(health, coins, strength);
		inventory[0] = spoils;
		sleepNano = sleep;
		this.map = map;
		
		ttChase = new Chase();
		ttStill = new Movement();
		t1 = new Timer("StandStill");
		t2 = new Timer("Chase");
		t2Run = false;

		t1.schedule(ttStill, 0, 700);
	}
	 //main constructor used for creation of the eneny
	public Enemy(Image north, Image south, Image east, Image west,
			Image northL, Image northR, Image southL, Image southR,
			Image eastL, Image eastR, Image westL, Image westR, 
			Image punchN, Image punchS, Image punchE, Image punchW, Point loc,
			int health, int strength, int coins, Item spoils, long sleep, GameMap map) {
		
		super(north, south, east, west, northL, northR, southL, southR, eastL,
				eastR, westL, westR, punchN, punchS, punchE, punchW, loc);
		
		x = mapLoc.x;
		y = mapLoc.y;
		stats = new StatPoints(health, coins, strength);
		inventory[0] = spoils;
		sleepNano = sleep;
		this.map = map;
		
		ttChase = new Chase();
		ttStill = new Movement();
		t1 = new Timer("StandStill");
		t2 = new Timer("Chase");
		t2Run = false;

		t1.schedule(ttStill, 0, 700);
	}
	
	public Enemy(Player that, long sleep, GameMap map){ //copy constructor not used
		super(that);
		
		this.map = map;
		sleepNano = sleep;
		ttChase = new Chase();
		ttStill = new Movement();
		t1 = new Timer("StandStill");
		t2 = new Timer("Chase");
		t1.schedule(ttStill, 0, 700);
		t2Run = false;
	}
	
	public void setEnemies(Enemy [] e){ //used to check where the enemys are sets reference to the array
		enemies = e;
	}
	
	public void setIndex(int i){ //sets index, used in game logic
		index = i;
	}
	
	public void IsPlayerThere(Point p){ //looks for player to be tere and if sees the player then chaeses them by running the toher timer
		playerLoc = p;
		int maxY, minY, maxX, minX;
		int i = 4;
		maxY = mapLoc.y + i;
		minY = mapLoc.y - i;
		maxX = mapLoc.x + i;
		minX = mapLoc.x - i;
		if((maxY > p.y && minY < p.y)&&(maxX > p.x && minX < p.x)){
			t1.cancel();
			if(!t2Run){
				t2Run = true;
				t2.schedule(ttChase, 0, 10);
			}
		}
	}
	
	@Override
	public void setLoc(Point p){ //sets loc
		mapLoc = p;
		x = mapLoc.x;
		y = mapLoc.y;
	}
	
	public void getPunched(){ //when the enemy gets punched their heeealth goes down
		stats.changeHealth(-bobStats.getStrength());
		if(stats.changeHealth(-bobStats.getStrength())){
			bobStats.XPUp(stats.getXP());
			t2.cancel();
			currentImage = null;
			mapLoc = new Point(-3,-3);
		}
		
	}
	
	public void getStats(StatPoints s){
		bobStats = s;
	}
	
	private boolean isEnemy(Point charLoc){ //checks to see if ther eis another enemy there and doesn't walk on them
		boolean rtn = false;
		try{
			for(int i = 0; i < enemies.length && !rtn; i++){
				if(i != index){
					Enemy temp = enemies[i];
					if((charLoc.x == temp.getLoc().x) && (charLoc.y == temp.getLoc().y)){
						rtn =  true;
					}
				}
			}
		}catch(Exception e){}
		return rtn;
	}
	
	private boolean canStep(Point charLoc){ //checks if enemy cna step
		return !(charLoc.x < 0 || charLoc.y < 0 || charLoc.x >= map.currentMap.map.getRow() || 
				charLoc.y >= map.currentMap.map.getCol() || !map.mainMapAction(charLoc) || isEnemy(charLoc)); 
	}
	
	private void sleep(long l){ //to sleep and pause time for animations
		long startTime = System.nanoTime();
		long endTime = System.nanoTime() - startTime;
		while(endTime < l){
			endTime = System.nanoTime() - startTime;
		}
	}
	
	class Movement extends TimerTask{
		public Movement(){
		}
		
		public void turn(){
			int x =(int)( Math.random()*4);
			switch (x){
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
		public void run() { //randomly turns around until sees the player
			turn();
			
		}
		
	}
	
	class Chase extends TimerTask{
		double vx = 0, vy = 0, speed = .05;
		int x1, y1, x2, y2;
		boolean isChasing = false;
		
		public Chase(){
		}

		public void setVector(){ //sets the direction and speed of where to chase the player
			x1 = Math.abs(playerLoc.x - mapLoc.x);
			y1 = Math.abs(playerLoc.y - mapLoc.y);
			x2 = (playerLoc.x - mapLoc.x);
			y2 = (playerLoc.y - mapLoc.y);
			
			
			vx = 0;
			vy = 0;
			
			up = false;
			down = false;
			left = false;
			right = false;
			
			if(x1 > y1){
				if(x2 > 0){
					u = false;
					d = false;
					r = true;
					l = false;
					vx = speed;
					right = true;
				}
				else{
					u = false;
					d = false;
					r = false;
					l = true;
					vx = -speed;
					left = true;
				}
			}
			else if(y1 > x1){
				if(y2 > 0){
					u = false;
					d = true;
					r = false;
					l = false;
					vy = speed;
					down = true;
				}
				else{
					u = true;
					d = false;
					r = false;
					l = false;
					vy = -speed;
					up = true;
				}
			}
			else{
				int rand = (int)(Math.random()*2);
				if(rand == 0){
					vx = Math.copySign(speed, x2);
					if(x2 > 0){
						u = false;
						d = false;
						r = true;
						l = false;
						right = true;
					}
					else{
						u = false;
						d = false;
						r = false;
						l = true;
						left = true;
					}
				}
				else{
					vy = Math.copySign(speed, y2); 
					if(x2 > 0){
						u = false;
						d = true;
						r = false;
						l = false;
						down = true;
					}
					else{
						u = true;
						d = false;
						r = false;
						l = false;
						up = true;
					}
				}
			}
		}
		
		public void chase(){ //the same actions are done here as in Game class for walking just as the player does just this is automatically done
			isChasing = true;
			setVector();
			dirFacing();
			double xx = vx, yy = vy;
			
			Point charLoc = mapLoc;
			Point temp = new Point(charLoc);
			
			charLoc = new Point(charLoc.x + (int)(Math.ceil(xx)) + (int)(Math.floor(xx)), 
					charLoc.y + (int)(Math.ceil(yy)) + (int)(Math.floor(yy)));
			if(!canStep(charLoc)){
				xx = 0;
				yy = 0;
				charLoc = temp;
				step(walkLeft);
				walkLeft = !walkLeft;
				
			}
			mapLoc = new Point(charLoc);
			
			boolean xsd = true;
			for(int i = 0; i < ((int)(1/speed)); i ++){
				if((x != 0 || y != 0) && xsd){
					step(walkLeft);
					walkLeft = !walkLeft;
					xsd = false;
				}
				x += xx;
		    	y += yy;
				sleep(sleepNano);
				if(i == ((int)(1/speed)/2)) xsd = true;
			}
			isChasing = false;
			dirFacing();
			
		}
		
		public void facePlayer(){ //faces the player where ever he may be
			x1 = Math.abs(playerLoc.x - mapLoc.x);
			y1 = Math.abs(playerLoc.y - mapLoc.y);
			x2 = (playerLoc.x - mapLoc.x);
			y2 = (playerLoc.y - mapLoc.y);
			
			up = false;
			down = false;
			left = false;
			right = false;
			
			if(x1 > y1){
				if(x2 > 0){
					u = false;
					d = false;
					r = true;
					l = false;
				}
				else{
					u = false;
					d = false;
					r = false;
					l = true;
				}
			}
			else if(y1 > x1){
				if(y2 > 0){
					u = false;
					d = true;
					r = false;
					l = false;
				}
				else{
					u = true;
					d = false;
					r = false;
					l = false;
				}
			}
		}
		
		private void attack(){ //attacks the plyaer with their strenght
			if( Math.abs(playerLoc.x - mapLoc.x) == 0 && Math.abs(playerLoc.y - mapLoc.y) == 1 || Math.abs(playerLoc.x - mapLoc.x) == 1 && Math.abs(playerLoc.y - mapLoc.y) == 0 ){
				punch();
				bobStats.changeHealth(-stats.getStrength(), null);
				sleep(100000000);
			} 
			else{
				
			}
		}
		
		@Override
		public void run() { //timer for chasing the player
			if(Math.abs(playerLoc.x - mapLoc.x) >= 3 || Math.abs(playerLoc.y - mapLoc.y) >= 3){
				if(!isChasing){
					chase();
				}
			}
			else{
				vx = 0; //other wise turns towards player 
				vy = 0;
				facePlayer();
				dirFacing();
				attack();
			}
			
		}
	
		
	}
	

}
