package garneauHalls;

import java.awt.Color;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Game2 extends JFrame implements KeyListener, MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int mapSize = 60;
	private final long milliTimeOut = 25L;
	private final long nanoTimeOut = milliTimeOut * 1000000;
	private final Image west = loadImage("Char Pics/left 1.png",1); //pics for the player
	private final Image east =  loadImage("Char Pics/Right 1.png",1);
	private final Image north =  loadImage("Char Pics/Up 1.png",1);
	private final Image south =  loadImage("Char Pics/Front 1.png",1);
	private final Image westL =  loadImage("Char Pics/left 2.png",1);
	private final Image westR =  loadImage("Char Pics/left 3.png",1);
	private final Image eastL =  loadImage("Char Pics/Right 2.png",1);
	private final Image eastR = loadImage("Char Pics/Right 3.png",1);
	private final Image northL = loadImage("Char Pics/Up 2.png",1);
	private final Image northR = loadImage("Char Pics/Up 3.png",1);
	private final Image southL = loadImage("Char Pics/Front 2.png",1);
	private final Image southR = loadImage("Char Pics/Front 3.png",1);
	protected final Image punchN = loadImage("Char Pics/punchN.png",1);
	protected final Image punchS = loadImage("Char Pics/punchS.png",1);
	protected final Image punchE = loadImage("Char Pics/punchE.png",1);
	protected final Image punchW = loadImage("Char Pics/punchW.png",1);
	protected final Image menu = loadImage("imgs/Menu.png",2); //pics for the menu and stuff
	protected final Image death = loadImage("imgs/Death.png",2);
	protected final Image endGame = loadImage("imgs/EndGame.png",2);
	protected final Image win = loadImage("imgs/Win.png",2);
	private Image [][] enemyImgs; //array for all enemy imgaes
	private Image [][] friendImgs;//array for all friend imgaes

	
	private GameMap map; //Creats a game map
	private DrawArea drawPanel; //Draw panel for graphics
	private JPanel panel;
	private JTextArea friendTalking; //For when talking to friends
	
	private Timer timer; //timer for the user
	private HelloRunnable t; //timertask
	
	private double vx,vy; //speed for player
	private double speed; //speed
	private boolean walkLeft; //boolean for stepping
	private boolean dirTmpUp; //boolean for something
	private boolean dirTmpDown;
	private Player bob; //player object
	private Enemy [] enemies = new Enemy [0]; //array of enemies tobe on map
	private Friend [] friends = new Friend [0]; //same as above for friends
	
	private boolean gameStart; //boolean for if gameStarted yet
	public boolean isDead; //boolean for if player is dead
	private boolean isGameOver = false; //boolean for if the game ended 
	
	private SplashScreen s; //Splash screen so impatient people (Such as myself) don't open the file too many times beccause they think its not working
	private int loaded; //for the loading bar in the splash screen
	private String loadTxt; //text for the splash screen
	private boolean winner = false;
	
	public Game2(){ //TODO music
		
		loaded = 0; //how much is loaded so far, not precise 
		loadTxt = "Loading character images"; //changes the string for the splash screen
		s = new SplashScreen();
		s.repaint();
		
		gameStart = false;
		isDead = false;
		loadEnemyImages(getText("EnemyLoader.txt")); //loads the enemy imgaes from the text file
		loadPlayerImages(getText("FriendLoader.txt")); //loads the firned files from the text file
		loaded = 30; 
		loadTxt = "Loading Maps";
		s.repaint(); //repaints the splash sscreen after each change

		
		map = new GameMap(mapSize); //creates and loads the maps from text files
		loaded = 70;
		loadTxt = "Loading Graphics";
		s.repaint();

		drawPanel = new DrawArea(); //creates the draw panel for graphics
		loaded = 75;
		s.repaint();

		setUpText(); //sets up the text for when there are friends on the current map
		loaded = 77;
		loadTxt = "Loading Struture";
		s.repaint();

		drawPanel.add(panel, BorderLayout.SOUTH); //adds the text to there
		loaded = 80; 
		loadTxt = "Making Main Character";
		s.repaint();
		
		bob = new Player(north,south, east,  west,
				northL,northR,southL, southR, eastL, eastR, westL, westR,
				punchN, punchS, punchE, punchW,
				new Point(9,13)); //creates the player
		loaded = 83;
		loadTxt = "Just bored now";
		s.repaint();

		
		bob.x = 9 ; bob.y = 13;
		walkLeft = true;

		loaded = 89;
		loadTxt = "Loading logic";
		s.repaint();
		
		speed = 0.1;
		
		timer = new Timer("GameLogic"); //Starts the timer
		t = new HelloRunnable();
		
		
		this.addKeyListener(this);
		this.addMouseListener(this);

		loaded = 94;
		loadTxt = "Finishing the Display";
		s.repaint();
		
		setContentPane(drawPanel); //frame is used as content pane
		pack ();
		setTitle ("Game Test V1"); //if not a txt file then not valid either
		
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null); //when screen launches it it is set in the center
		setPreferredSize(new Dimension(610,450));
		loaded = 100;
		loadTxt = "LOADED!";
		s.repaint();
		sleep(2*100000000);
		
		s.DESTROY(); //gets rid of the Splash Screen
		this.repaint();  //repaints the game screen
		drawPanel.repaint(); //repaints the draw panel
	}
	
	private void setUpText(){ //Sets up the text stuff for the 
		panel = new JPanel(new BorderLayout()); 
		friendTalking  = new JTextArea();
		friendTalking.setEditable(false);
		friendTalking.setBounds(0,0,drawPanel.getWidth(), 110);
		friendTalking.setBackground(Color.black);
		friendTalking.setCaretColor(Color.black);
		friendTalking.setForeground(Color.white);
		friendTalking.setLineWrap(true);
		friendTalking.setWrapStyleWord(true);
		panel.setBackground(Color.black);
		panel.add(friendTalking, BorderLayout.SOUTH);
		panel.setPreferredSize( new Dimension( drawPanel.getWidth(), 110 ) );
		panel.setVisible(false);
	}
	
	protected Image loadImage (String name){ //loads an image
		Image img = null;
		File file = new File(name); //uses string and creates a file object
		try
		{
		    img = ImageIO.read (file); // load file into Image object
			img = img.getScaledInstance(mapSize, mapSize, Image.SCALE_SMOOTH);  //scales the image to the height and width of the object
		}
		catch (IOException e)
		{
		}
	
		return img; //returns the Image
	}
	
	protected Image loadImage (String name, int x){ //loads an image
		Image img = null;
		File file = new File(name); //uses string and creates a file object
		try
		{
		    img = ImageIO.read (file); // load file into Image object
		}
		catch (IOException e)
		{
		}
	
		return img; //returns the Image
	}
	
	 public String getText(String f){  //gets the text from a text file and returns it as a string
	    	String line, s = "";
			try{
				FileReader fr = new FileReader (new File(f));
				BufferedReader filein = new BufferedReader (fr);

				while ((line = filein.readLine ()) != null) // file has not ended
			    s+= line;
				filein.close (); // close file
			}catch(IOException e){ //if there is an IOException then a dialog window shows an error message
				JOptionPane.showMessageDialog(this, "Error! Text file could not be read"); //if file doesn't load show err msg
			}
			
			return s; //returns the string 
	   }
	
	private void DESTROY(){ //destroys and terminates the program
		this.setVisible(false);
		this.dispose();
		System.exit(0);
	}
	 
	private void update(){ //updates the velocity based on key pressed
		vx = 0;
	    vy = 0;
	    if(bob.down) vy = speed;
	    else if(bob.up) vy = -speed;
	    else if(bob.left) vx = -speed;
	    else if(bob.right) vx = speed;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {  //this is called when a key is pressed
		int keyPressed = e.getKeyCode();
		switch(keyPressed){
		
		case KeyEvent.VK_DOWN: //this will set the player's boolean variable for what directuion the velocity should be in 
			bob.down = true; //Its based on what key is pressed i.e up, down, left or right arrow
			break;
		case KeyEvent.VK_LEFT:
			bob.left = true;
			if(bob.up){
				bob.up = false;
				dirTmpUp = true;
			}
			if(bob.down){
				bob.down = false;
				dirTmpDown = true;
			}
			break;
		case KeyEvent.VK_UP:
			bob.up = true;
			break;
		case KeyEvent.VK_RIGHT:
			bob.right = true;
			if(bob.up){
				bob.up = false;
				dirTmpUp = true;
			}
			if(bob.down){
				bob.down = false;
				dirTmpDown = true;
			}
			break;
		case KeyEvent.VK_Z:  //if the user presses z then it finds out if it should punch or talk to another person
			Point temp;
			int tempX = bob.getLoc().x , tempY = bob.getLoc().y;
			if(bob.u){
				tempY --;
			}
			else if(bob.d){
				tempY ++;
			}
			else if(bob.l){
				tempX --;
			}
			else if(bob.r){
				tempX ++;
			}
			temp = new Point(tempX, tempY);
			
			
			if(isFriend(temp)){ 
				for(int i = 0; i < friends.length; i++){
					if(friends[i].getLoc().x == temp.x && friends[i].getLoc().y == temp.y){
						System.out.print(1);
						friends[i].talk(bob.getLoc());
						System.out.print(2);
						repaint();
						System.out.print(3);
						talk(i);
						System.out.println(4);
					}
				}
			}
			else{
				punch(); //calls punch method and does logic there
			}
			
			break;
		}
			
		if(map.isChanged()){ //if the map is changed then the varibles for setting velocity are set to false so the maps are transitioned nicely
			bob.up = false;
			bob.down = false;
			bob.right = false;
			bob.left = false;
		}
		update();
		
	}

	@Override
	public void keyReleased(KeyEvent e) { //when key is released then the velocities are set to false so it doesn't keep going when key is released
		int keyPressed = e.getKeyCode();
		
		
		switch(keyPressed){ //finds what key is released
		
		case KeyEvent.VK_DOWN:
			dirTmpDown = false;
			bob.down = false;
			break;
		case KeyEvent.VK_UP:
			dirTmpUp = false;
			bob.up = false;
			break;
		case KeyEvent.VK_LEFT:
			if(dirTmpUp){
				bob.up = true;
			}
			if(dirTmpDown){
				bob.down = true;
				dirTmpDown =false;
			}
			bob.left = false;
			break;
		case KeyEvent.VK_RIGHT:
			bob.right = false;
			if(dirTmpUp){
				bob.up = true;
			}
			if(dirTmpDown){
				bob.down = true;
				dirTmpDown =false;
			}
			break;
		}
		update();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { //this is used for starting the game continuing when you die and for choosing playagain or quit
		repaint();
		if(!gameStart){ //uses booleans to figure out what action should be done, is not used during gameplay
			gameStart = true;
			timer.schedule(t, 0, milliTimeOut);

		}
		else if(isGameOver){ 
			boolean playAgain = false, quit = false;
			Point p = e.getPoint();
			
			if(p.y > 190 && p.y < 270){ //to find out where the user clicked and if they clicked the "button" then variable is set to true
				if(p.x > 80 && p.x < 320){
					playAgain = true;
				}
				else if(p.x > 410 && p.x < 550){
					quit = true;
				}
			}
			
			
			if(playAgain){ //if play again then the whole game resets only player, map  and boolean variables need to be reset (and AI)
				bob = new Player(north,south, east,  west,
						northL,northR,southL, southR, eastL, eastR, westL, westR,
						punchN, punchS, punchE, punchW,
						new Point(9,13));
				bob.x = 9;bob.y = 13;
				isDead = false;
				isGameOver = false;
				map.restart();
				repaint();
			}
			else if(quit){ //if they want to quit then the prgram is terminated
				this.DESTROY();
			}
		}
		else if(isDead){
			isGameOver = true;
				
			}
		else if(winner){
			isGameOver = true;
		}
		repaint();
	}

	//following are unused methods from the interface
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void talk(int j){ //talk method is called for when player presses 'z' when talking to a friend
		String dialoge = friends[j].talk();
		int test, len = dialoge.length();
		panel.setVisible(true);
		
		for(int i = 0; i < len; i+= 20){
			test = i + 20;
			if(test > len){
				test = len + i;
			}
			friendTalking.setText(dialoge);
		}
	}
	
	public void punch(){ //finds what enemy to punch and punches them (i.e method in Enemy class and their health gets lowered)
		Point charLoc = bob.getLoc();
		Point temp;
		Point dir = bob.getDir();
		temp = new Point(charLoc.x + dir.x, charLoc.y + dir.y);
		if(isEnemy(temp)){
			for(int i = 0; i < enemies.length; i++){
				if(enemies[i].getLoc().x == temp.x && enemies[i].getLoc().y == temp.y){
					enemies[i].getPunched(); //lowers enemy's health
					repaint();
				}
			}
		}
		if(map.isEight()){
			if(isAllDead()){
				winner  = true;
			}
		}
		
		bob.punch(); //shows the punching picture
		
	}
	
	public boolean isAllDead(){
		boolean rtn = true;
		
		for(int i = 0; i < enemies.length; i++){
			if(enemies[i].isAlive()){
				rtn = false;
			}
		}
		return rtn;
	}
	
	public void loadMusic(){ //loads music but doesn't work yet so does nothing
		try{
		
			Clip clip = AudioSystem.getClip();
			// getAudioInputStream() also accepts a File or InputStream
			AudioInputStream ais = AudioSystem.
			getAudioInputStream( new File(new File("").getAbsolutePath()+"/Music/Music3.wav") );
			clip.open(ais);
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	
	}
	
	public void loadPlayerImages(String s){ //loads all the player images from different directories specified in the parameter
		//loads it into an array
		final String imageName[] = {"Up 1.png",  "Front 1.png", "Right 1.png", "left 1.png"};
		final int imgLen = imageName.length;
		
		friendImgs = new Image [3][imgLen];
		Image [] temp;
		String dir[] = new String[3];
		int x =0, start = 0;
		for(int i = 0; i < s.length(); i++){ //finds the names of the different directories where the pics are stored
			if(s.charAt(i) == ','){
				dir[x] = s.substring(start, i);
				x++;
				start = i+1;
			}
		}
		
		for(int i = 0; i < friendImgs.length; i++){
			temp = new Image[imgLen];
			for(int j = 0; j < temp.length; j++){
				temp[j] = loadImage(dir[i] + "/" + imageName[j]);
			}
			friendImgs[i] = temp;
		}
	}
	
	public void loadEnemyImages(String s){ //same as above method but for enemy
		final String imageName[] = {"Up 1.png",  "Front 1.png", "Right 1.png", "left 1.png",  
				"Up 2.png", "Up 3.png", "Front 2.png", "Front 3.png", "Right 2.png", "Right 3.png", "left 2.png", "left 3.png",
				"punchN.png", "punchS.png", "punchE.png", "punchW.png",};  
		final int imgLen = imageName.length;
		enemyImgs = new Image [3][imgLen];
		Image [] temp;
		String dir[] = new String[3];
		int x =0, start = 0;
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == ','){
				dir[x] = s.substring(start, i);
				x++;
				start = i+1;
			}
		}
		
		for(int i = 0; i < enemyImgs.length; i++){
			temp = new Image[imgLen];
			for(int j = 0; j < temp.length; j++){
				temp[j] = loadImage(dir[i] + "/" + imageName[j]);
			}
			enemyImgs[i] = temp;
		}
	}
	
	public void loadEnemy(String s){ //loades the enemy based on text file from the currentMap 
		String loc, health, strength, coins, spoils;
		Point locP;
		int healthI, strengthI, coinsI, arrS;
		Item spoil;
		int x,y;
		
		arrS = Integer.parseInt(s.substring(0, s.indexOf('(')));
		enemies = new Enemy[arrS];
		
		for(int i = 0; i < enemies.length; i++){ //splits the string into the different parts to load the enemies
			loc = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
			health = s.substring(s.indexOf(')') + 1, s.indexOf(' '));
			strength = s.substring(s.indexOf(' ')+1, s.indexOf('!'));
			coins = s.substring(s.indexOf('!')+1, s.indexOf('@'));
			spoils = s.substring(s.indexOf('@')+1, s.indexOf('>'));
			x = Integer.parseInt(loc.substring(0, loc.indexOf(',')));
			y = Integer.parseInt(loc.substring(loc.indexOf(',')+1));
			locP = new Point(x, y);
			
			healthI = Integer.parseInt(health);
			strengthI = Integer.parseInt(strength);
			coinsI = Integer.parseInt(coins);
			spoil = new Item(loadImage(spoils.substring(spoils.indexOf('&'))), spoils.substring(0,spoils.indexOf('^')), spoils.substring(spoils.indexOf('^')+1, spoils.indexOf('*')), 0);
		
			int e = (int)(Math.random() *3);
			enemies[i] = new Enemy(enemyImgs[e][0],enemyImgs[e][1],enemyImgs[e][2],enemyImgs[e][3],enemyImgs[e][4],enemyImgs[e][5],enemyImgs[e][6],
					enemyImgs[e][7],enemyImgs[e][8],enemyImgs[e][9],enemyImgs[e][10],enemyImgs[e][11],
					enemyImgs[e][12], enemyImgs[e][13], enemyImgs[e][14], enemyImgs[e][15],
					locP, healthI, strengthI, coinsI, spoil, nanoTimeOut, map);
					
			s = s.substring(s.indexOf('>') + 1);
		}
		
		for(int i = 0; i < enemies.length; i++){ //adds additional fields to the enemy for calculations
			enemies[i].setEnemies(enemies);
			enemies[i].setIndex(i);
			enemies[i].getStats(bob.getStats());
		}
		
	}
	
	public void loadPlayer(String s){ //same as above but simpler because friend has less fields to load
		String loc, dialoge;
		Point locP;
		int arrS;
		int x,y;
		
		arrS = Integer.parseInt(s.substring(0, s.indexOf('(')));
		friends = new Friend[arrS];
		
		for(int i = 0; i < friends.length; i++){
			loc = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
			dialoge = s.substring(s.indexOf(')') + 1, s.indexOf('>'));
			
			x = Integer.parseInt(loc.substring(0, loc.indexOf(',')));
			y = Integer.parseInt(loc.substring(loc.indexOf(',')+1));
			locP = new Point(x, y);
			System.out.print(locP.toString());

			int e = (int)(Math.random() *3);
			friends[i] = new Friend(friendImgs[e][0],friendImgs[e][1],friendImgs[e][2],
							friendImgs[e][3], locP, dialoge);
			s = s.substring(s.indexOf('>') + 1);
		}
	}
	
	public void loadAI(String s){ //loads either the friends or the enemies depending on text file
		// is only called when the map changes
		char type = s.charAt(0);
		if(type == 'E'){
			friends = new Friend [0];
			loadEnemy(s.substring(1));
		}
		else if(type == 'F'){
			enemies = new Enemy [0];
			loadPlayer(s.substring(1));
		}
	}
	
	private boolean isEnemy(Point charLoc){ //used to see if enemy is on the given point and returns a boolean for it
		boolean rtn = false;
		for(int i = 0; i < enemies.length; i++){
			Enemy temp = enemies[i];
			try{
				if((charLoc.x == temp.getLoc().x) && (charLoc.y == temp.getLoc().y)){
					rtn =  true;
				}
			}catch(Exception e){
			}
		}
		return rtn;
	}
	
	private boolean isFriend(Point charLoc){ //same as above isEnemy method
		boolean rtn = false;

		for(int i = 0; i < friends.length; i++){
			Friend temp = friends[i];
			if((charLoc.x == temp.getLoc().x) && (charLoc.y == temp.getLoc().y)){
				rtn =  true;
			}
		}
		return rtn;
	}
	
	private boolean canStep(Point charLoc){ //checks to see if the spot can be stepped on and returns a boolean
		return !(charLoc.x < 0 || charLoc.y < 0 || charLoc.x >= map.currentMap.map.getRow() || charLoc.y >= map.currentMap.map.getCol() || !map.mainMapAction(charLoc) || isEnemy(charLoc) || isFriend(charLoc));
	}
	
	private void sleep(long l){ //sleep method used to pause when needed
		long startTime = System.nanoTime();
		long endTime = System.nanoTime() - startTime;
		while(endTime < l){
			endTime = System.nanoTime() - startTime;
		}
	}
	
	
	class HelloRunnable extends TimerTask{ //timer task for the timer
		double x,y;
		Timer timer1;
		Thread it;
		
		public HelloRunnable(){
			super();
			x = 0;
			y = 0;
		}
		
		@Override
	    public void run() { //method called in timer and does all character animations and decisions and mostly contains the game logic
			update();
			if(!isDead || winner){
				x = vx; y = vy;
				Point charLoc = bob.getLoc();
				Point temp = new Point(charLoc);
			
				charLoc = new Point(charLoc.x + (int)(Math.ceil(x)) + (int)(Math.floor(x)), charLoc.y + (int)(Math.ceil(y)) + (int)(Math.floor(y)));
				if(!canStep(charLoc)){ //if the player can't step then velocity is set to zero
					x = 0;
					y = 0;
					charLoc = temp;
					bob.step(walkLeft);
					walkLeft = !walkLeft;
				}
				bob.setLoc(charLoc); //sets the new locatino for bob
				try{
					for(int i = 0; i < enemies.length; i++){
					enemies[i].IsPlayerThere(bob.getLoc()); //this is used for the enemy to check if the player and if is enenmy chases player
					}
				}
				catch(Exception e){ //used if this is called before the array is generated 
				
				}
			
				boolean xsd = true;
				for(int i = 0; i < ((int)(1/speed)); i ++){ //this loops showing smooth animation fo the player walking
					if((x != 0 || y != 0) && xsd){
						bob.step(walkLeft);
						walkLeft = !walkLeft; //changes step from left to right
						xsd = false;
					}
					bob.x += x;
					bob.y += y;
					repaint();
					sleep(nanoTimeOut);
					if(i == ((int)(1/speed)/2)) xsd = true;
				}
				bob.dirFacing(); //sets player to dir facing
			
				if(!bob.isAlive()){ //iif player died then isDead is set to true then according actions will occur
					isDead = true;
				}
				repaint(); 
			}
		}

	}
	
	
	class DrawArea extends JPanel{ //class for showing the graphics
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DrawArea(){ //constructor default
			setPreferredSize(new Dimension(610,450));
			setBackground(Color.BLACK);
			this.setLayout(new BorderLayout());
		}
		
		@Override
		public void paintComponent(Graphics g){ //override paintComponent for graphics
			if(gameStart && (!isDead && !winner)){ //this paints the game if game has started and the player isn't dead
				Point temp = bob.getLoc();
				super.paintComponent(g);
			
				//displays the map and updates bob's location if map is changed
				bob.setLoc(map.display(g, bob.x, bob.y, this.getWidth(), this.getHeight(), bob.getLoc())); 

				if(map.isChanged()){ //if the map is changed then following needs to happen
					String path = map.getAIPath(); //this is used to load the ai in the game
					vx = 0;//following is done so map is drawn in correct place if player has the key pressed
					vy = 0;
					bob.up = false;
					bob.left = false;
					bob.right = false;
					bob.down = false;
					bob.x = bob.getLoc().x;
					bob.y = bob.getLoc().y;
					bob.setLoc(map.display(g, bob.x, bob.y, this.getWidth(), this.getHeight(), bob.getLoc()));
				
					loadAI(getText(path)); //loads ai from the text file
					if(friends.length > 0){ //if on the map there are people to talk to then the textarea is set to visible
						friendTalking.setVisible(true);
						friendTalking.setText("Welcome to the world of Mountain Land! Recently there" +
								" has been a disturbance around our village and we don't know what to do." +
								" These evil creatures disturb us and start attacking us but it's not that" +
								" bad near the village, it gets worse as you approach the top of the mountian." +
								" Their boss the giant monster controls them all and if he's destroyed then" +
								" our village will be at peace, could you please help us. Talk with the villagers" +
								" by pressing 'z' and you can find out more. We all really appreciate this" +
								" thank you Bob!"); 
						panel.setVisible(true);

					}
					else{
						friendTalking.setVisible(false); //if map has no friends then can't see text area
						panel.setVisible(false);
					}
				
					map.changed(); //sets boolean variable to true that map is changed in map 
					repaint(); //repaints the map
				}
			
			
				outOfBounds(temp); //checks to see if its out of bounds
				for(int i = 0; i < enemies.length; i ++){ //draws all enemies
					Enemy tempE = enemies[i];
					g.drawImage(tempE.getCurrentImage(), (this.getWidth()/2) - (int)(mapSize*(bob.x-tempE.x)),
							(this.getHeight()/2) - (int)(mapSize*(bob.y-tempE.y)), null);	
				}
				for(int i = 0; i < friends.length; i++){ //draws all friendss
					Friend tempF = friends[i];
					g.drawImage(tempF.getCurrentImage(), (this.getWidth()/2) - (int)(mapSize*(bob.x-tempF.x)),
							(this.getHeight()/2) - (int)(mapSize*(bob.y-tempF.y)), null);	
				}
				g.drawImage(bob.getCurrentImage(),this.getWidth()/2,this.getHeight()/2 ,null); //draws player
				g.drawImage(bob.getStats().getImage(), 0, 0, null); //draws stats
			}
			else if(!gameStart){ //if game hasn't started
				g.drawImage(menu,0,0,null);
			}
			else if(isGameOver){ //if game is over
				g.drawImage(endGame, 0, 0, null);
			}
			else if(isDead){ //if died
				g.drawImage(death, 0, 0, null);
			}
			else if(winner){
				g.drawImage(win, 0, 0, null);
			}
		}
		
		public void outOfBounds(Point temp){
			if(bob.getLoc().x != temp.x  && bob.getLoc().x != temp.y){
				bob.x = (double)bob.getLoc().x;
				bob.y = (double)bob.getLoc().y;
				vx =0;
				vy =0;
				t.x =0;
				t.y=0;
			}	
		}
		
	}
	
	private class SplashScreen extends JFrame{ ///splash screen so user konws that the game is loading
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private SplashGraphics loadBar;
		
		public SplashScreen(){ //constructor
			loadBar = new SplashGraphics();
			this.setUndecorated(true);
			this.setPreferredSize(new Dimension(400,275));
			this.setContentPane(loadBar);
			this.pack();
			this.setTitle ("Our Game"); //if not a txt file then not valid either
			this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo (null);
			this.setVisible(true);
			repaint();
			
		}
		
		public void DESTROY(){ //method to destroys the screen when game starts
			this.setVisible(false);
			this.dispose();
		}
		
		
		class SplashGraphics extends JPanel{ //graphics for the loading bar
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Image title;
			int rule = AlphaComposite.SRC_OVER;
			final private Font f = new Font("consolas", Font.PLAIN,20);
			
			public SplashGraphics(){
				
				setSize(400,300);
				this.setVisible(true);
				title = loadImage("imgs/Splash.png",1);
			}
			
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(f);
				g2.setColor(new Color(9,36,16));
				g2.fillRect(0,0,400,300);
				g2.drawImage(title, 0, 0, null);
				g2.setComposite(AlphaComposite.getInstance(rule, 0.7f));
				g2.setColor(new Color(52,60,120));
				g2.fillRoundRect(50, 180, (int)((loaded/(double)100)*300) , 60, 60, 60);
				g2.setComposite(AlphaComposite.getInstance(rule, 1f));
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.black);
				g2.drawRoundRect(49, 179, 302, 63, 60, 60);
				g2.setColor(Color.white);
				g2.drawString(loadTxt, 135, 270);
				
				
			}
			
		}
	}
	
	
	public static void main(String args[]){
		 try {
	            // Set System L&F//
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       System.out.print("one");
	    }
	    catch (ClassNotFoundException e) {
	    	  System.out.print("one2");
	    }
	    catch (InstantiationException e) {
	    	  System.out.print("one3");
	    }
	    catch (IllegalAccessException e) {
	    	  System.out.print("one4");
	    }
		
		Game2 game = new Game2(); //creates the game
		game.setVisible(true);
		//game.loadMusic();
		
	}



}
