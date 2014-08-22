package mapMaker;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1322601482964166485L; //Constants for color and file name of images
	private final Color ubuntuOrange = new Color(242,149,116);
	private final Color ubuntuPurple = new Color(216,141,181);
	@SuppressWarnings("unused")
	private final Color brown = new Color(160, 82, 45);
	private final String dir = (new File("")).getAbsolutePath() + "/imgs";
	private final String str_Brick = dir + "/mario_Brick.jpg";
	@SuppressWarnings("unused")
	private final String str_Door =dir + "/mario_Door.jpg";
	private final String str_Grass = dir + "/mario_Grass.jpg";
	private final String str_Ground = dir + "/mario_Ground.jpg";
	@SuppressWarnings("unused")
	private final String str_Key = dir + "/mario_Key.png";
	private final String str_Ice = dir + "/mario_Ice.png";
	
	static Mapmaker map;  //Main panels declaration
	private DrawArea jp_Drawing;
	private JPanel jp_Frame;
	private JPanel jp_ObjectMenu;

	private JMenuBar m_Bar; //declation of the menubar menu and menu items
	private JMenu m_Files, m_Edit, m_View;
	private JMenuItem sm_Save, sm_Open, sm_ClearMap, sm_Undo, sm_Redo, sm_Search;
	private JCheckBoxMenuItem sm_ShowGrid;
	private JButton jb_Wall, jb_Ground, jb_Door, jb_Grass, jb_Ice, jb_Key, jb_Delete,
	one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirT, fourT;
	private JFileChooser fc;
	private JSlider js_ChangeSize;
	
	protected MapObject addMapObj;//map Objects that can be added to the map
	protected MapObject wallObj;
	protected MapObject groundObj, grassObj, iceObj; 
	protected MapObject doorObj; 
	protected MapObject keyObj;
	protected MapObject deleteObj; 
	protected JLabel showChoice;
	
	private BufferedImage bi;
	
	protected boolean showGrid; //If the show grid function is on or off
	protected int size = 10; 
	protected int heightInt = 0, widthInt = 0; //Used to set the width and height spacing depending on how much the user scrolled for the paint compoenet
	
	//********************constructor**********************************************************************************
	
	public GUI(int row, int col, int w, int h){
		deleteObj = new GroundMapObject(h, w, ' ', Color.white); //the following are creation of the objects that the user will be able to add
		
		doorObj = new ObstacleMapObject(h, w, 'd', MapObjectList.game_Door, new Color(150,150,150));
		grassObj = new GroundMapObject(h, w, 'R', str_Grass, Color.gray);
		wallObj = new ObstacleMapObject(h, w);
		groundObj = new GroundMapObject(h, w, 'f', MapObjectList.game_Floor, Color.GRAY);
		iceObj = new GroundMapObject(h, w, 'I', str_Ice, Color.cyan);
		keyObj = new ObstacleMapObject(h, w, 'l', MapObjectList.game_Locker, Color.LIGHT_GRAY);
		
		//Will show what object the user has selected
		showChoice = new JLabel(" <--- Currently Selected     ", getIcon(str_Brick, 15,15), SwingConstants.CENTER);
		
		showGrid = true; //for showing the grid 
		addMapObj = wallObj; //sets the global variable of addMapObj to be wallObj at first (changes when button is pressed)
		
		map = new Mapmaker(row,col,w,h); //Creates the mapmaker object
		bi = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
		fc = new JFileChooser(); //creates a jfilechooer for open and saving files
		jp_Frame = new JPanel(new BorderLayout()); //Frame for content pane of JFrame
		jp_Drawing = new DrawArea(map.getWidth(),map.getHeight()); //The area that the map is drawn on is created
		jp_ObjectMenu = new JPanel(new GridLayout(7,3)); //Menu of the buttons to change the selection
		m_Bar = new JMenuBar(); //creates the JMenu Bar
		js_ChangeSize = new JSlider(JSlider.HORIZONTAL,5, 100,map.width); //creates a jslider for zoom/ scaling
		
		createObjMenu(); //calls method to make the buttons that change the selection of object to draw/find
		createMenuBar(); //Calls method to make the JMenu bar and all its items
		
		addMouseListener(jp_Drawing); //adds mouselistener to drwaing panel
	    addMouseMotionListener(jp_Drawing);
	    	
	    addActions(); //calls a mehtod to create all the actions for each button, jmenuitem, slider ... ect
	    paintToBuffer();
	    
	    jp_Frame.add(jp_ObjectMenu, BorderLayout.WEST); //places the different components of the interface onto the frame JPanel
	    jp_Frame.add (jp_Drawing, BorderLayout.CENTER); //sets the panels in the frame
	    jp_Frame.add(showChoice, BorderLayout.NORTH);
	    jp_Frame.add(js_ChangeSize, BorderLayout.SOUTH);
		setContentPane(jp_Frame); //frame is used as content pane
		setJMenuBar(m_Bar); //Sets the JMenuBar to the JFrame

		pack ();
		setTitle ("Map Maker"); //if not a txt file then not valid either
		
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null); //when screen launches it it is set in the center

			
	}
		
	@SuppressWarnings("serial")
	private void addActions(){ //adds actions to each button / slider / menu /... ect
		
		sm_ShowGrid.setAccelerator(KeyStroke.getKeyStroke( //creates an acelerator for quick access cntl + r calls the listeneer
		        KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		sm_ShowGrid.addItemListener (new ItemListener () //Listner for shwo grid menu item
		{
		    @Override
			public void itemStateChanged(ItemEvent arg0) {
		    	map.unSearch();
		    	showGrid = !showGrid; //changes the global variable for showing the grid or not (boolean)
		    	repaint(); //shows map with change
				
			}
		}  );
		
		sm_Search.setAccelerator(KeyStroke.getKeyStroke( //shortcut, cntl + f (finds the object selected)
		        KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		sm_Search.setToolTipText("Searches for object selected to draw"); //tool tip for search item to tell user about function
		
		sm_Search.addActionListener(new AbstractAction() {// adds listener to the search menu item
		    public void actionPerformed(ActionEvent ae) {
		    	map.search(addMapObj, map.getSearchObj()); //calls the map's search method
		    	repaint(); //shows the grid with the items to be searched highlighted
		    }
		}  );
		
		sm_Undo.setAccelerator(KeyStroke.getKeyStroke( //shortcut, cntl + z for undo method
		        KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		
		sm_Undo.addActionListener(new AbstractAction() { //adds listener that calls map's undo method then shows "undid" map
		    public void actionPerformed(ActionEvent ae) {
		    	map.undo();
		    	repaint();
		    }
		}  );
		
		sm_Redo.setAccelerator(KeyStroke.getKeyStroke( //short cut, cntl + y
		        KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		
		sm_Redo.addActionListener(new AbstractAction() { //adds action to the undo item
		    public void actionPerformed(ActionEvent ae) {
		    	map.redo(); //redoes action by calling mthod from map class and shows new map
		    	repaint();
		    }
		}  );
		
		sm_ClearMap.addActionListener(new AbstractAction() { //clearmap action
		    public void actionPerformed(ActionEvent ae) {
		    	map.clearMap(); //calls map's class clear map option
		    	repaint(); //shows new clear map
		    }
		}  );
		
		sm_Save.setAccelerator(KeyStroke.getKeyStroke( //short cut, cntl + s for save
		        KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		sm_Save.addActionListener(new AbstractAction() { //adds listener to save map
		    public void actionPerformed(ActionEvent ae) {
		    	save(); //calls save method and saves the map
		    	repaint();
		    }
		}  );
		
		sm_Open.setAccelerator(KeyStroke.getKeyStroke( //shortcut cntl + o, open saved map
		        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		sm_Open.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent ae) {
		    	open(); //calls open method to open a file
		    	js_ChangeSize.setValue(5); // sets the size of the slider to 5
		    	repaint(); //shows map that was just loaded
		    }
		}  );
		
		//*****************JButtons********************************************************
		//The following listeners calls the map's unsearch method then sets the global variabel of addMapObj to 
		//The mapobject the button corressponds with. It also changes the "Current selection" JLabel to show icon of the
		//currently selected map Object
		
		jb_Wall.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = wallObj;
		    	showChoice.setIcon(getIcon("dsaf" , 15, 15));
		    }
		}  );
		
		jb_Ground.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = groundObj;
		    	showChoice.setIcon(getIcon(MapObjectList.game_Floor, 15, 15));
		    }
		}  );
		
		jb_Door.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = doorObj;
		    	showChoice.setIcon(getIcon(MapObjectList.game_Door, 15, 15));
		    }
		}  );
		
		jb_Grass.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = grassObj;
		    	showChoice.setIcon(getIcon(str_Grass, 15, 15));
		    }
		}  );
		
		jb_Ice.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = iceObj;
		    	showChoice.setIcon(getIcon(str_Ice, 15, 15));
		    }
		}  );
		
		jb_Key.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = keyObj;
		    	showChoice.setIcon(getIcon(MapObjectList.game_Locker, 15, 15));
		    }
		}  );
		
		jb_Delete.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = deleteObj;
		    	showChoice.setIcon(getIcon("eraser.png", 15, 15));
		    }
		}  );
		one.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon(MapObjectList.str_house, 15, 15));
		    }
		}  );
		two.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house1;
		    	showChoice.setIcon(getIcon(MapObjectList.str_house1, 15, 15));
		    }
		}  );
		three.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house2;
		    	showChoice.setIcon(getIcon("house2.png", 15, 15));
		    }
		}  );
		four.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house3;
		    	showChoice.setIcon(getIcon("house3.png", 15, 15));
		    }
		}  );
		five.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house4;
		    	showChoice.setIcon(getIcon("house4.png", 15, 15));
		    }
		}  );
	    six.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house5;
		    	showChoice.setIcon(getIcon("house5.png", 15, 15));
		    }
		}  );
		seven.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		eight.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		nine.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		ten.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		eleven.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		twelve.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		thirT.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.house;
		    	showChoice.setIcon(getIcon("house.png", 15, 15));
		    }
		}  );
		fourT.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	map.unSearch();
		    	addMapObj = MapObjectList.groundObj;
		    	showChoice.setIcon(getIcon(str_Ground, 15, 15)); 
		    }
		}  );
		
		
		//Adds listener to the jslider 
		
		js_ChangeSize.setToolTipText("Change Size (Very Slow)"); //shows a tool tip to let the user know that it changes the size and is very slow
		
		js_ChangeSize.addChangeListener(new ChangeListener(){ //adds the listener to the slider

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size  = js_ChangeSize.getValue();
				changeMapObjSize();
				map.setWidth(size); //sets the width, height and size of the map object
				map.setHeight(size);
				map.setSize(size);
				bi = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
				paintToBuffer();
				repaint(); //repaints the new sized map (the more objects on the map the slower it is
			}
			
		});
		
		
		jp_Drawing.addMouseWheelListener(new MouseWheelListener(){ //adds a mouse wheel listener to the drawing aread

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				 int totalScrollAmount = e.getUnitsToScroll() * 4; //gets an increment value to change the position of the map by
				 if(e.isControlDown()){ //if contorl is down then it scrolls to the left and right
					 widthInt -= totalScrollAmount;
				 }
				 else if(e.isAltDown()){ //if alt is down then it zooms in and out
					 totalScrollAmount /= 4;
					 js_ChangeSize.setValue(js_ChangeSize.getValue()-totalScrollAmount);
				 }
				 else{ //other wise change the vertical position of the map
					 heightInt -= totalScrollAmount;
				 }
				 repaint(); //show the map at its new position
				
			}
			
		});
		
	}
	
	private void createObjMenu(){ // creates the obj menu for selection of objects to draw
		ImageIcon imgIcon;
		
		imgIcon = getIcon("");
		if(imgIcon != null){ //if loading the image fails the will catch it and instead of only showing icon it will only shwo text (for all buttons)
			jb_Wall = new JButton(imgIcon);
		}
		else{
			jb_Wall = new JButton("Wall");
		}
		jb_Wall.setBackground(Color.black); //sets the back ground color to the color of the corresponding object
		jb_Wall.setOpaque(true);
		jb_Wall.setToolTipText("Wall Tool"); //tool tip to let the user know what object they are about to choose
		//same as above is done below for every button
		imgIcon = getIcon(MapObjectList.game_Floor);
		if(imgIcon != null){
			jb_Ground = new JButton(imgIcon);
		}
		else{
			jb_Ground = new JButton("Ground");
		}
		jb_Ground.setBackground(Color.gray);
		jb_Ground.setOpaque(true);
		jb_Ground.setToolTipText("Ground Tool");
		
		imgIcon = getIcon(MapObjectList.game_Door);
		if(imgIcon != null){
			jb_Door = new JButton(imgIcon);
		}
		else{
			jb_Door = new JButton("Door");
		}
		jb_Door.setBackground(Color.RED);
		jb_Door.setOpaque(true);
		jb_Door.setToolTipText("Door Tool");
		
		
		imgIcon = getIcon(str_Grass);
		if(imgIcon != null){
			jb_Grass = new JButton(imgIcon);
		}
		else{
			jb_Grass = new JButton("Grass");
		}
		jb_Grass.setBackground(Color.green);
		jb_Grass.setOpaque(true);
		jb_Grass.setToolTipText("Grass Tool");
		
		imgIcon = getIcon(str_Ice);
		if(imgIcon != null){
			jb_Ice = new JButton(imgIcon);
		}
		else{
			jb_Ice = new JButton("Ice");
		}
		jb_Ice.setBackground(Color.cyan);
		jb_Ice.setOpaque(true);
		jb_Ice.setToolTipText("Ice Tool");
		
		imgIcon = getIcon(MapObjectList.game_Locker);
		if(imgIcon != null){
			jb_Key = new JButton(imgIcon);
		}
		else{
			jb_Key = new JButton("Key");
		}
		jb_Key.setBackground(Color.orange);
		jb_Key.setOpaque(true);
		jb_Key.setToolTipText("Key Tool");
		
		
		imgIcon = getIcon("imgs/eraser.png");
		if(imgIcon != null){
			jb_Delete = new JButton(imgIcon);
		}
		else{
			jb_Delete = new JButton("Eraser");
		}
		jb_Delete.setBackground(Color.white);
		jb_Delete.setOpaque(true);
		jb_Delete.setToolTipText("Eraser Tool");
		
		one = new JButton("house1");
		two = new JButton("house2");
		three = new JButton("house3");
		four = new JButton("house4");
		five = new JButton("house5");
		six = new JButton("house6");
		seven = new JButton("house7");
		eight = new JButton("house8");
		nine = new JButton("house9");
		ten = new JButton("house10");
		eleven = new JButton("house11");
		twelve = new JButton("house12");
		thirT = new JButton("house13");
		fourT = new JButton("hard Grnd");
		
		
		//the following adds all the buttons to the object menu
		jp_ObjectMenu.add(jb_Wall);	
		jp_ObjectMenu.add(jb_Ground);	
		jp_ObjectMenu.add(jb_Door);
		jp_ObjectMenu.add(jb_Grass);
		jp_ObjectMenu.add(jb_Ice);
		jp_ObjectMenu.add(jb_Key);
		jp_ObjectMenu.add(jb_Delete);
		jp_ObjectMenu.add(one);
		jp_ObjectMenu.add(two);
		jp_ObjectMenu.add(three);
		jp_ObjectMenu.add(four);
		jp_ObjectMenu.add(five);
		jp_ObjectMenu.add(six);
		jp_ObjectMenu.add(seven);
		jp_ObjectMenu.add(eight);
		jp_ObjectMenu.add(nine);
		jp_ObjectMenu.add(ten);
		jp_ObjectMenu.add(eleven);
		jp_ObjectMenu.add(twelve);
		jp_ObjectMenu.add(thirT);
		jp_ObjectMenu.add(fourT);
		
	}
	
	private void createMenuBar(){ //creates the menus and menu items for the jmenubar and adds them to the bar

		m_Files = new JMenu("File"); //the  three menus
		m_Edit = new JMenu("Edit");
		m_View = new JMenu("View");
		
		sm_Save = new JMenuItem("Save"); //the menu items, in order they will be added
		sm_Open = new JMenuItem("Open");
		sm_ClearMap = new JMenuItem("Clear Map");
		sm_Undo = new JMenuItem("Undo");
		sm_Redo = new JMenuItem("Redo");
		sm_ShowGrid = new JCheckBoxMenuItem("Hide Grid ");
		sm_Search = new JMenuItem("Search for Map Objects");
		
		m_Files.add(sm_Save); //adds the items to the menus
		m_Files.add(sm_Open);
		m_Files.add(sm_ClearMap);
		m_Edit.add(sm_Undo);
		m_Edit.add(sm_Redo);
		m_View.add(sm_ShowGrid);
		m_View.add(sm_Search);
		
		m_Bar.add(m_Files); //adds the menus to the menubar
		m_Bar.add(m_Edit);
		m_Bar.add(m_View);

	}
	
	private void paintToBuffer(){
		 Graphics2D g2d = bi.createGraphics();
		 
		 map.display(g2d, bi.getWidth(), bi.getHeight(null));
		 
		 g2d.dispose();
	}
	
	private ImageIcon getIcon(String fn){ //gets icon which is used for loading the icons for the buttons and for the jlabel
		ImageIcon icon = null;
		Image img;
		try //gets the image then scales it then sets it as the icon
		{
		    img = ImageIO.read (new File(fn)); // load file into Image object
			img = img.getScaledInstance(50,50, Image.SCALE_SMOOTH);  
			icon = new ImageIcon(img);
		}
		catch (IOException e) 
		{
		    System.out.print("ERR");
		}
		catch(NullPointerException e){
			
		}
		
		return icon; //returns the scaled icon image
		
	}
	
	private ImageIcon getIcon(String fn, int w, int h){ ////gets icon but with a specified width and height
		ImageIcon icon = null;
		Image img;
		try
		{
		    img = ImageIO.read (new File(fn)); // load file into Image object
			img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);  
			icon = new ImageIcon(img);
		}
		catch (IOException e)
		{
		    System.out.print("ERR");
		}
		catch(NullPointerException e){
			
		}
		
		return icon;
		
	}
	
	private void open(){ //opens a file 
		boolean valid;
		
		fc.showOpenDialog(this); //jfilechooser pops up and lets the user pick their file to open
		File f = fc.getSelectedFile(); //sets the file choen as f
		valid = map.open(f, this); //checks to see if the map is in a proper format or not
		if(valid){
			map = new Mapmaker(map.getText(f,this)); //creates the map using the constructor that accepts a file name 
		}
		bi = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
		paintToBuffer();
	}
	
	private void save(){ //saves the file
		String temp;
		
		fc.showOpenDialog(this); //pops open a jfilechooser to choose where to save the file
		File f = fc.getSelectedFile(); //gets the file name from the filechooser
		temp = f.getAbsolutePath() + ".txt"; //sets the extention as .txt 
		f = new File(temp); //sets the file ase the new name with the extsention
		map.save(f); //calls save method from the map and saves the map in text format
	}
	
	public void changeMapObjSize(){ //changes the size of the object global variables for a clear drawing depending on the scale factor
		doorObj.setSize(size);// = new ObstacleMapObject(h, w, 'D', str_Door, Color.RED);
		grassObj.setSize(size);// = new GroundMapObject(h, w, 'R', str_Grass, Color.green);
		wallObj.setSize(size);// = new ObstacleMapObject(h, w, 'W', str_Brick, Color.BLACK);
		groundObj.setSize(size);// = new GroundMapObject(h, w, 'G', str_Ground, brown);
		iceObj.setSize(size);// = new GroundMapObject(h, w, 'I', str_Ice, Color.cyan);
		keyObj.setSize(size);// = new Obst
	}
	
	
	class DrawArea extends JPanel implements MouseMotionListener, MouseListener{ //class drawarea that extends JPanel and overrides the paint component
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 5117291406320925906L; //data field for the class
        int rule = AlphaComposite.SRC_OVER;
        float alpha = 0.3F;
        Point startPoint;
		private int startX, startY, endX, endY;
		private Point start = new Point(0,0),end = new Point (0,0);
		
		public DrawArea(int x, int y){ //Constructor for the graphics Panel 
			setPreferredSize(new Dimension (x,y));
			addMouseListener(this);
	        addMouseMotionListener(this);
		}
		
		@Override
		public void paintComponent(Graphics g){ //paint componete to paint the map and gird and selection box if applicible
			super.paintComponent(g); //clears the screen for a repaint to happen
			//map.display(g, getWidth() + widthInt, getHeight() + heightInt); //drwas the map
			g.drawImage(bi, (getWidth()+ widthInt - map.getWidth())/2 , (getHeight() + heightInt - map.getHeight())/2 , null);
			if(showGrid)
				map.drawGrid(g, getWidth() + widthInt, getHeight() + heightInt); //draws grid if showGrid is true
			drawSelectionBox(g); //draws selection box

		}
		
		private void drawSelectionBox(Graphics g){ //draws the selection box based on the global variables endX,Y and startX,Y from the mouse listener
			//The following below will draw the selection rectangle if one is created
			((Graphics2D) g).setStroke(new BasicStroke(2));
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(rule, 1));
			
			//if the user drags the box to the left then orange colour is shown and if the the right then purple
			//if its to the right then the boxes inside the orange area are chosen and if it is purple then
			//what ever the purple selection box touches is changed
			
			if(endY < startY && endX < startX){
				g.setColor(ubuntuOrange);
				g.drawRect(endX , endY, startX - endX, startY - endY);
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(rule, alpha));
				g.fillRect(endX + 1, endY + 1, startX - endX - 1, startY - endY - 1);
			}
			else if(endX < startX){
				g.setColor(ubuntuOrange);
				g.drawRect(endX + 1, startY + 1, startX - endX - 1, endY - startY - 1);
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(rule, alpha));
				g.fillRect(endX, startY, startX - endX, endY - startY);
			}
			else if(endY < startY){
				g.setColor(ubuntuPurple);
				g.drawRect(startX, endY, endX - startX, startY - endY);
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(rule, alpha));
				g.fillRect(startX + 1, endY + 1, endX - startX - 1, startY - endY -1);
			}
			else{
				g.setColor(ubuntuPurple);
				g.drawRect(startX, startY, endX - startX, endY - startY);
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(rule, alpha));
				g.fillRect(startX + 1, startY + 1, endX - startX - 1, endY - startY - 1);
			}
		}
		
        
        public Point [] findMapCoords(){ //gets the corrdinates of the map to be changed based on the selection box

        	int comp;
        	int fOfx;
        	int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        	int fW = (this.getWidth() + widthInt - (map.width * map.getCol()))/2 ;
        	int fH = (this.getHeight() + heightInt - (map.height * map.getRow())) /2;
        	boolean leaveLoop = false;
        	
        	comp = startX - map.getWidth(); //gets the start point and finds a point one width distance away to find the grid line inbetween these two points
        	for(int i = 0; i < map.getCol() || !leaveLoop;i++){ //loops thourhg the coords of the grid lines and finds when the grid line lies inbetween comp and startX
        		fOfx = widthFnc(i) + fW;
        		if(fOfx > comp && fOfx < startX || fOfx == comp){
        			x1 = i;
        			leaveLoop = true;
        		}
        	}
        	
        	comp = endX + map.getWidth(); //save as above 
        	leaveLoop = false;
        	for(int i = 0; i < map.getCol() + 1 && !leaveLoop;i++){
        		fOfx = widthFnc(i) +fW;
        		if((fOfx > endX && fOfx < comp) || fOfx == endX){
        			x2 = i;
        			leaveLoop = true;
        		}
        	}
        	
        	if(x2 == 0){ //the following are conditions that fixes bugs in the program such as if the user selects outside the grid, ect
        		x2 = map.getCol();
        	}
        	if(startX > endX && x2 == 30){
        		x2 = 0;
        	}
        	
        	comp = startY - map.getHeight(); // same as above but for the y coords
        	leaveLoop = false;
        	for(int i = 0; i < map.getRow() || !leaveLoop; i++){
        		fOfx = heightFnc(i) + fH;
        		if(fOfx > comp && fOfx < startY || fOfx == comp){
        			y1 = i; 
        			leaveLoop = true;
        		}
        	}
        		
        	if(startX > endX){ //compenstates for if an orange box was made or purple so it can draw on the map accordingly
        		if(startY < endY){
        			y1++;
        		}
        	}
        	else if(startX < endX){ 
        		if(startY > endY){
        			y1++;
        		}
        	}
        	
        	comp = endY - map.getHeight(); //same as above for the y2
        	leaveLoop = false;
        	for(int i = 0; i < map.getRow() || !leaveLoop; i++){
        		fOfx = heightFnc(i) + fH;
        		if(fOfx > comp && fOfx < endY || fOfx == comp){
        			y2 = i; 
        			leaveLoop = true;
        		}
        	}
        	
        	if(startX > endX){
        		if(startY > endY){
        			y2++;
        		}
        	}
        	else if(startX < endX){
        		if(startY < endY){
        			y2++;
        		}
        	}
        	
        	if(y2 > map.getRow()){
        		y2 = 0;
        	}
        	if(endY > (map.height * map.getRow()) && startX > endX){
        		y2++;
        	}
        		
        	start = new Point(x1,y1); //sets the global variables to the x and y values gained from this method
        	end = new Point(x2,y2);
			
        	return getPointArr(start, end); //returns based on the method called but a Point object array
        	
        }
        
        private Point [] getPointArr(Point start, Point end){ //gets the points that will need to be changed on teh map based on the selection area
        	Point [] change = new Point [map.getRow()*map.getCol()];
        	int n;
        	boolean error = false;
        	
        	if(end.x < start.x){
        		int temp = end.x; //sets the greater x and y values into the same point object by doing a swap method (not actual method)
        		end.x = start.x;
        		start.x = temp;	
        	}
        	if(end.y < start.y){
        		int temp = end.y;
        		end.y = start.y;
        		start.y = temp;
        	}
        	
        	n = 0;
        	for(int i = start.x; i < end.x && !error; i ++){ //Loops thorugh the map and adds a pount object to where ever they chose
        		for(int j = start.y; j < end.y && !error; j++){
        			try{
        				change[n] = new Point(j,i); //adds the point to the array
        			}
        			catch(ArrayIndexOutOfBoundsException e){ //if the user starts their selection outside the grid a problem occurs so the user is notified about this
        				JOptionPane.showMessageDialog(this, "Error! Do not start your selection outside of the grid area.");
        				error = true; //so the loop ends
        			}
        			n++;
        		}
        	}
        	
        	return change; //returns the point array 
        }
        
        private int widthFnc(int x){ //used for calculations in where the selection box should change the points on the map
        	return x*map.width;
        }
        
       private int heightFnc(int x){ //same as method for obove
        	return x*map.height;
        }
                   

        @Override
        public void mousePressed(MouseEvent e) { //if mouse is pressed then it gets the start points and sets it to a global variablee which is used to draw the sleection box and do calculations
        	map.unSearch();
            startPoint = e.getPoint();
            setOpaque(true);
            start = new Point(0,0);end = new Point (0,0);
            if(startPoint.x < (getWidth() + widthInt -  map.width * map.getCol()) / 2){ //if the start points lies outside the grid it will set the start to the grid
            	startPoint.x =  (getWidth() + widthInt -  map.width * map.getCol()) / 2;
            }
            startX = startPoint.x; //sets start and end points x, and y
            startY = startPoint.y;
            endX = e.getPoint().x;
            endY = e.getPoint().y;
            repaint(); //repaints with selection box
            }
        
        @Override
        public void mouseDragged(MouseEvent e) { //sets the values for the selection box to be drawn and draws it as the mouse is dragged
        	
        	 startX = startPoint.x;
             startY = startPoint.y;
             endX = e.getPoint().x;
             endY = e.getPoint().y;
             repaint();
        }
       
        ///when reaslsed it unSearches any searched items then sets the points once again
        //then calls the addmethod from the map class
        //then resets the points that was used to draw the selection box and shows the new map
        @Override
        public void mouseReleased(MouseEvent e) 
        {
        	 map.unSearch(); //
        	 
             endX = e.getPoint().x;
             endY = e.getPoint().y;	
             
             map.add(addMapObj,findMapCoords(),e.isControlDown());
        		
        	 startX = 0;//startPoint.x;
             startY = 0;//startPoint.y;
             endX = 0;// e.getPoint().x;
             endY = 0;//e.getPoint().y;
             paintToBuffer();
             repaint();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {} //nothing
        public void mouseClicked(MouseEvent e) { //when the mouse is clicked then it gets the coords of the point it is on and chnages the map with the object
        	map.unSearch();
        	int comp;
        	int fOfx;
        	int x1 = 0, y1 = 0;
        	int startX = e.getPoint().x;
        	int startY = e.getPoint().y;
        	int fW = (this.getWidth() + widthInt - (map.width * map.getCol()))/2;
        	int fH = (this.getHeight() + heightInt - (map.height * map.getRow())) /2;
        	boolean leaveLoop = false;
        	
        	comp = startX - map.getWidth();
        	for(int i = 0; i < map.getCol() || !leaveLoop;i++){
        		fOfx = widthFnc(i) + fW;
        		if(fOfx > comp && fOfx < startX || fOfx == comp){
        			x1 = i;
        			leaveLoop = true;
        		}
        	}
        	
        	comp = startY - map.getHeight();
        	leaveLoop = false;
        	for(int i = 0; i < map.getRow() || !leaveLoop; i++){
        		fOfx = heightFnc(i) + fH;
        		if(fOfx > comp && fOfx < startY || fOfx == comp){
        			y1 = i; 
        			leaveLoop = true;
        		}
        	}
        		
        	if(startX > endX){
        		if(startY < endY){
        			y1++;
        		}
        	}
        	else if(startX < endX){
        		if(startY > endY){
        			y1++;
        		}
        	}
        	try{
        		map.add(addMapObj, new Point(y1,x1), e.isControlDown());
        	}
        	catch(ArrayIndexOutOfBoundsException w){
        		JOptionPane.showMessageDialog(this, "Error! Do not start your selection outside of the grid area");
        	}
        	 paintToBuffer();
        }
        @Override
        public void mouseEntered(MouseEvent arg0) {} //nothing
        @Override
        public void mouseExited(MouseEvent arg0) {} //nothing

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
		
		StartDialog start;
		GUI newGui;
		
		start = new StartDialog(); //shows strat dialog for the 
		while(!start.isDone){
			System.out.print("");
		}
		
		
		newGui = new GUI(start.getRow(), start.getCol(), start.getW(), start.getH()); //creates the gui for the map maker
		start.setVisible(false);
		newGui.setVisible(true);
		
		System.out.print(GUI.map.toString());
	}
}
