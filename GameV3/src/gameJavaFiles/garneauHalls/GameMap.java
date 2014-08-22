package garneauHalls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import mapMaker.Map; 
import mapMaker.MapConnector;

public class GameMap {
	
	private final String mainPath = "Maps/Game1.txt", main2Path = "Maps/Game2.txt", main3Path = "Maps/Game3.txt",
					main4Path = "Maps/Game4.txt", main5Path = "Maps/Game5.txt", main6Path = "Maps/Game6.txt", 
					main7Path = "Maps/Game7.txt", main8Path = "Maps/Game8.txt";
	private int size;
	
	protected Map bkMap, m_One, m_Two, m_Three, m_Four, 
				m_Five, m_Six, m_Seven, m_Eight;
	protected ConnectingMap mainMap, main2Map, main3Map, main4Map,
							 main5Map, main6Map, main7Map, main8Map, currentMap;
	private MapConnector [] mc_One, mc_Two, mc_Three, mc_Four, mc_Five, mc_Six, mc_Seven, mc_Eight;
	
	private boolean isChanged;
	
	private BufferedImage bi;
	
	public GameMap(int s){
		size = s;
		bkMap = new Map();
		isChanged = true; //made true because when game starts this will allow the ai to be loaded
		
		m_One = new Map(bkMap.getText(new File(mainPath), new JFrame()), size); //loads all the maps
		m_Two = new Map(bkMap.getText(new File(main2Path), new JFrame()), size);
		m_Three = new Map(bkMap.getText(new File(main3Path), new JFrame()), size);
		m_Four = new Map(bkMap.getText(new File(main4Path), new JFrame()), size);
		m_Five = new Map(bkMap.getText(new File(main5Path), new JFrame()), size);
		m_Six = new Map(bkMap.getText(new File(main6Path), new JFrame()), size);
		m_Seven = new Map(bkMap.getText(new File(main7Path), new JFrame()), size);
		m_Eight = new Map(bkMap.getText(new File(main8Path), new JFrame()), size);
		
		makeConnectors(); //makes all of the map connectors for the game
		
		mainMap = new ConnectingMap(m_One,mc_One, "AI/friendTest.txt",1 ); //creates a connecting map object based on the map and map connectors made and Ai file
		main2Map = new ConnectingMap(m_Two, mc_Two, "AI/map1.txt",2 );
		main3Map = new ConnectingMap(m_Three, mc_Three, "AI/map2.txt",3);
		main4Map = new ConnectingMap(m_Four, mc_Four, "AI/map3.txt",4);
		main5Map = new ConnectingMap(m_Five, mc_Five, "AI/map4.txt",5);
		main6Map = new ConnectingMap(m_Six, mc_Six, "AI/map6.txt",6);
		main7Map = new ConnectingMap(m_Seven, mc_Seven, "AI/map7.txt",7);
		main8Map = new ConnectingMap(m_Eight, mc_Eight, "AI/map8.txt",8);
		
		currentMap = mainMap; //sets the current map to the mainMap
		
		setMapSize(); //sets the map size

		paintToBuffer(); //draws the current map to the buffered image
	}
	
	
	private void makeConnectors(){ //creates the map conenctos for each map
		int n = 0;
		
		mc_One = new MapConnector [2]; //Make size of connector then the MapConnecter to be on the map
		mc_One[0] = new MapConnector(m_One,m_Two, new Point(4,8), new Point(8,0));
		mc_One[1] = new MapConnector(m_One,m_Two, new Point(5,8), new Point(9,0));
		
		mc_Two = new MapConnector [4];
		mc_Two[0] = new MapConnector(m_Two,m_One, new Point(8,1), new Point(4,9));
		mc_Two[1] = new MapConnector(m_Two,m_One, new Point(9,1), new Point(5,9));
		mc_Two[2] = new MapConnector(m_Two,m_Three, new Point(3,38), new Point(4,0));
		mc_Two[3] = new MapConnector(m_Two,m_Three, new Point(4,38), new Point(5,0));
		
		mc_Three = new MapConnector [4];
		mc_Three[0] = new MapConnector(m_Three,m_Two, new Point(4,1), new Point(3,39));
		mc_Three[1] = new MapConnector(m_Three,m_Two, new Point(5,1), new Point(4,39));
		mc_Three[2] = new MapConnector(m_Three,m_Four, new Point(9,48), new Point(3,0));
		mc_Three[3] = new MapConnector(m_Three,m_Four, new Point(40,48), new Point(33,0));
		
		mc_Four = new MapConnector [102];
		mc_Four[0] = new MapConnector(m_Four,m_Three, new Point(3,1), new Point(9,49));
		mc_Four[1] = new MapConnector(m_Four,m_Three, new Point(32,1), new Point(46,49));
		n = 2;
		for(int i = 0; i < 50; i++){ //sets map connectors on each sides of the map
			mc_Four[n] = new MapConnector(m_Four,m_Five, new Point(48,i), new Point(0,i));
			n++;
		}
		for(int i = 0; i < 50; i++){
			mc_Four[n] = new MapConnector(m_Four,m_Six, new Point(1,i), new Point(49,i));
			n++;
		}
		
		mc_Five = new MapConnector[50+18];
		n = 0;
		for(int i = 0; i < 50; i++){
			mc_Five[n] = new MapConnector(m_Five,m_Four, new Point(1,i), new Point(49,i));
			n++;
		}
		for(int i = 0; i < 18; i++){
			mc_Five[n] = new MapConnector(m_Five,m_Seven, new Point(14,28), new Point(46-i,0));
			n++;
		}
		
		n = 0;
		mc_Six = new MapConnector[50+12];
		for(int i = 0; i < 50; i++){
			mc_Six[n] = new MapConnector(m_Six,m_Four, new Point(48,i), new Point(0,i));
			n++;
		}
		for(int i = 0; i < 12; i++){
			mc_Six[n] = new MapConnector(m_Six,m_Seven, new Point(14,28), new Point(11+i,0));
			n++;
		}
		
		mc_Seven = new MapConnector[2];
		Map m; Point p;
		int x = (int)(Math.random()*2);
		if(x == 0){
			m = m_Six;
			p = new Point(11,1);
		}
		else{
			m = m_Five;
			p = new Point(46,1);
		}
		mc_Seven[0] = new MapConnector(m_Seven, m, p, new Point(14,29));
		mc_Seven[1] = new MapConnector(m_Seven, m_Eight, new Point(7,13), new Point(14,0));
		
		mc_Eight = new MapConnector[1];
		mc_Eight[0] = new MapConnector(m_Eight, m_Seven, new Point(14,1), new Point(7,14));
		
	}
	
	public boolean isEight(){
		return currentMap.n == 8;
	}
	
	private void setMapSize(){
		m_One.setSize(size);
		m_Two.setSize(size);
		m_Three.setSize(size);
		m_Four.setSize(size);
		m_Five.setSize(size);
		m_Six.setSize(size);
		m_Seven.setSize(size);
		m_Eight.setSize(size);
	}
	
	public void changed(){ //when the new map AI is loaded then them map is officiall changed
		isChanged = false;
	}
	
	public boolean isChanged(){ //checked to see if the map has been changed
		return isChanged;
	}
	
	public void restart(){ //restarts the map by setting the current map to the mainMap
		currentMap = mainMap;
		isChanged = true;
		paintToBuffer();
	}
	
	public String getAIPath(){ //returns the AI file path of the curretn map
		return currentMap.filePath;
	}
	
	public void paintToBuffer(){ //paints the map to the bufferend image

		bi = new BufferedImage(size*currentMap.map.getCol(), size*currentMap.map.getRow(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bi.createGraphics();
			
		currentMap.map.display(g2d, bi.getWidth(), bi.getHeight(null));
		 
		g2d.dispose();
	}
	
	private void changeCurrentMap(Map cm){ //checks to see which map needs to be changed
		Map test = cm;
		if(m_One.equalsTo(test))
			currentMap = mainMap;
		else if(m_Two.equalsTo(test))
			currentMap = main2Map;
		else if(m_Three.equalsTo(test))
			currentMap = main3Map;
		else if(m_Four.equalsTo(test))
			currentMap = main4Map;
		else if(m_Five.equalsTo(test))
			currentMap = main5Map;
		else if(m_Six.equalsTo(test))
			currentMap = main6Map;
		else if(m_Seven.equalsTo(test))
			currentMap = main7Map;
		else if(m_Eight.equalsTo(test))
			currentMap = main8Map;
	}//changes the current map to the map to be changed to
	
	
	
	public Point display(Graphics g,double x, double y, int width, int height, Point p){ //displays the map
		int ix, iy, xx, yy;
		ix = (int)((x - (int)x) * size); //finds out the exact pixles to draw picture at based on character's location
		iy = (int)((y - (int)y) * size);
		xx = (int)x;
		yy = (int)y;
		
		for(int i = 0; i < currentMap.connectors.length; i++){ //checks to see if player landed on a map connector if so then map changes
			MapConnector mc = currentMap.connectors[i];
			if(currentMap.connectors[i].isEqual(p)){
				p = mc.getStart();
				changeCurrentMap(mc.getMap());
				paintToBuffer();
				xx = p.x;
				yy = p.y;
				ix = 0;
				iy = 0;
				isChanged = true;
			}
		}
		
		g.drawImage(bi, -size*xx + width/2 - ix, -size*yy + height/2 - iy, null); //draws the map
		return p; //reutrns the point and only is different if the map changed
	}
	
	public boolean mainMapAction(Point p){
		return currentMap.map.mapAction(p);
	}
	
	class ConnectingMap{ //connecting map object to hold a map, an array of MapConnter objects and filepath for the ai
		public final Map map;
		public final MapConnector [] connectors;
		public final String filePath;
		public final int n;
		
		ConnectingMap(Map m, MapConnector [] mc, String filePath, int n){
			this.map = m;
			this.filePath = filePath;
			this.connectors = mc;
			this.n = n;
		}
	}

}
