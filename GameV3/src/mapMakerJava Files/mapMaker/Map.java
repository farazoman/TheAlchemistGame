package mapMaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Map
{
    
	protected MapObject map [] [];
	protected MapObject houseItemLayer [] [];
	protected MapObject enemyLayer [] [];
	
    protected int width;
	protected int height;
	private int row;
	private int col;
    protected Point [] searchMem;
	protected MapObject typeMem;
	
	private final MapObject searchObj = new GroundMapObject(width, height, 's', Color.yellow);
	private final String acpEx = "txt"; //Only accepts this extension

	public Map(){
		
	}
	
    public Map (int rows, int cols, int blockwidth, int blockheight) //constructor sets the datafield
    {
    	
        width = blockwidth;  
        height = blockheight;
        setRow(rows);
        setCol(cols);
        map = new MapObject [rows] [cols]; // define 2-d array size
        houseItemLayer = new MapObject [rows] [cols];
        
        for (int row = 0 ; row < rows ; row++){ //for each spot in the array puts a blank spot
            for (int col = 0 ; col <cols ; col++)
            {
                 map [row] [col] = new GroundMapObject(height,width,' ', Color.WHITE); // blank space
            }
        }
        
        for (int row = 0 ; row < rows ; row++){ //for each spot in the array puts a blank spot
            for (int col = 0 ; col <cols ; col++)
            {
                 houseItemLayer [row] [col] = new GroundMapObject(height,width,'t',"imgs/eraseImg.png", Color.WHITE); // blank space
            }
        }
        houseItemLayer[5][5] = MapObjectList.gameFloor;
    }

    public Map (String mapStr){ //accepts a sttring and creates a map from the text given (only valid text format supported)

    	width = 5;
    	height = 5;
    	setRow(0);
    	setCol(mapStr.indexOf(',')); //index of the first , is the number of columnes
    	
    	for(int i = 0; i < mapStr.length()/2; i ++){ //the total number of , is the number of rows
    		if(mapStr.charAt(i) == ','){
    			setRow(getRow() + 1);
    		}
    	}
    	houseItemLayer = new MapObject [getCol()][getRow()]; //creates the map arra y
    	map = new MapObject [getCol()][getRow()]; //creates the map array
    	makeMapFromTxt(mapStr); //makes the map from the string given 
    }
    public Map (String mapStr, int size){ //accepts a sttring and creates a map from the text given (only valid text format supported)

    	width = size;
    	height = size;
    	setRow(0);
    	setCol(mapStr.indexOf(',')); //index of the first , is the number of columnes
    	
    	for(int i = 0; i < mapStr.length()/2; i ++){ //the total number of , is the number of rows
    		if(mapStr.charAt(i) == ','){
    			setRow(getRow() + 1);
    		}
    	}
    	
    	houseItemLayer = new MapObject [getCol()][getRow()]; //creates the map arra y
    	map = new MapObject [getCol()][getRow()];
    	makeMapFromTxt(mapStr); //makes the map from the string given 

//    	for(int i = 0; i < houseItemLayer.length; i++){ //loops thorugh the 2d map array
//			for(int j = 0; j < houseItemLayer[0].length; j++){
//				if(!houseItemLayer[i][j].mapAction()){
//					System.out.println(i + "," + j);
//					map[i][j].makeObstacle();
//				}	
//			}
//    	}

    	
    }
    
    private void makeMapFromTxt(String mapStr){ //makes the map form the string
    	boolean objFound = false;
    	int index = 0, l = MapObjectList.iterateMap.length;
    	
    	for(int i = 0; i < map.length; i++){ //loops thorugh the 2d map array
			for(int j = 0; j < map[0].length; j++){
				for(int k = 0; k < l && !objFound; k++){ //lloops until a valid object in the mapobject list is found
					if(MapObjectList.iterateMap[k].isObject(mapStr.charAt(index))){
						
						map[i][j] = MapObjectList.iterateMap[k];
						objFound = true;
					}
				}
				if(!objFound){ //if the object isn't found in the list then it is set as the white space
					map[i][j] = MapObjectList.iterateMap[l-1];
				}
				index++;
				objFound = false; //resets the variable
			}
			index++; //increments the index to skip the comma that appears
		}
    	
    	for(int i = 0; i < houseItemLayer.length; i++){ //loops thorugh the 2d map array
			for(int j = 0; j < houseItemLayer[0].length; j++){
				for(int k = 0; k < l && !objFound; k++){ //lloops until a valid object in the mapobject list is found
					if(MapObjectList.iterateMap[k].isObject(mapStr.charAt(index))){
						
						houseItemLayer[i][j] = MapObjectList.iterateMap[k];
						objFound = true;
					}
				}
				if(!objFound){ //if the object isn't found in the list then it is set as the white space
					houseItemLayer[i][j] = MapObjectList.iterateMap[l-1];
				}
				if(index == mapStr.length() - 1)
					index = mapStr.length() - 1;
				else
					index++;
				objFound = false; //resets the variable
			}
			index++; //increments the index to skip the comma that appears
		}
    	

    }
     
    public String getText(File f, JFrame frame){  //gets the text from a text file and returns it as a string
    	String line, s = "";
		try{
			FileReader fr = new FileReader (f);
			BufferedReader filein = new BufferedReader (fr);

			while ((line = filein.readLine ()) != null) // file has not ended
		    s+= line;
			filein.close (); // close file
		}catch(IOException e){ //if there is an IOException then a dialog window shows an error message
			JOptionPane.showMessageDialog(frame, "Error! Text file could not be read"); //if file doesn't load show err msg
		}
		
		return s; //returns the string 
    }
    
    public boolean open(File f, JFrame frame){ //opens a file
    	String fileN = f.getAbsolutePath(), mapStr; //gets a file
    	String extension = fileN.substring(fileN.length()-3); //gets the extension of the file to check if it is valid
    	int comaI, i_Row = 0, slashI;
    	
    	boolean valid = true;   
    	if(acpEx.equals(extension)){ //if the extension is txt then returns trueand continues
    		mapStr = getText(f, frame);
    		comaI = mapStr.indexOf(',');
    		slashI = mapStr.indexOf('/');

        	for(int i = 0; i < mapStr.length()/2; i ++){ //loops through the string looking for all commas which is number of rows
        		if(mapStr.charAt(i) == ','){
        			i_Row++;
        		}
        	}
        	
    		if(comaI == -1){
    			valid = false; //if there is no cmmma then it is not valid
    		}
    		if(i_Row == 0){
    			valid = false; //if there are zero rows it is not valid
    		}
    		else if((comaI % i_Row) != 0){ //if the  cols and rows aren't equally divisible then it is invalid
        		valid = false;
        	}
    		//load the second layer
    		i_Row = 0;
        	for(int i =  mapStr.length()/2; i < mapStr.length(); i ++){ //loops through the string looking for all commas which is number of rows
        		if(mapStr.charAt(i) == '/'){
        			i_Row++;
        		}
        	}
        	
    		if(slashI == -1){
    			valid = false; //if there is no cmmma then it is not valid
    		}
    		if(i_Row == 0){
    			valid = false; //if there are zero rows it is not valid
    		}
    		else if((slashI % i_Row) != 0){ //if the  cols and rows aren't equally divisible then it is invalid
        		valid = false;
        	}
    	}
    	else{
    		JOptionPane.showMessageDialog(frame, "Error! File type not supported"); //if not a txt file then not valid either
    		valid = false;
    	}
    	
    	if(!valid){
    		JOptionPane.showMessageDialog(frame, "Error! File not formatted properly"); //if its not valid then error message is shown
    	}
    	
    
    	return valid;		
    }
    
    public void save(File f){ //save file
      	 try{
      		 PrintWriter oSt = new PrintWriter(f);
      		 oSt.print(this.toString());
      		 oSt.close();
      		 System.out.print(this.toString()); //saves the file from the tostring method of the map
      	 }
      	 catch(FileNotFoundException e){
      		 e.printStackTrace();
      	 }
       }

    public void display (Graphics g, int p_W, int p_H)    // displays the map on the screen
    {
    	int w = (p_W - width * getCol())/ 2;
        int h = (p_H - height * getRow()) /2;
        for (int rows = 0 ; rows < map.length; rows++)
            for (int cols = 0 ; cols < map[0].length; cols++)
            {               
                map[rows][cols].show(g,cols * width + w, rows * height + h);
                //g.fillRect (cols * width + w, rows * height + h, width, height); // draw block
            }
        
        for (int rows = 0 ; rows < map.length; rows++)
            for (int cols = 0 ; cols < map[0].length; cols++)
            {               
                houseItemLayer[rows][cols].show(g,cols * width + w, rows * height + h);
                //g.fillRect (cols * width + w, rows * height + h, width, height); // draw block
            }
    }
    
    public void add(MapObject m, Point p){
    	map[p.x][p.y] = m;
    }    
    
    public void search(MapObject m, MapObject d){ //searches for objects in 2d array of map and changes the index to d

    	typeMem = m; //sets the global variable to the object that is being searched for
    	
    	for(int i = 0; i < map.length; i++){
    		for(int j = 0; j < map[0].length; j++){ 
    			if(map[i][j].equals(m)){ //if the object is found
    				map[i][j] = d; //change the index to d
    			}
    		}
    	}
    }
    
    public void unSearch(){
    	search(searchObj, typeMem);   	// calls the search method replaceing all yellow objs with typemem objects
    }
    @Override
    public String toString(){ //reutrns it as a string
		String s = "";
		
		for(int i =0;i< map.length; i++){
			for(int j = 0; j < map[0].length; j ++){
				s+=(map[i][j].toString());
			}
			s+=","; //adds a comma for counting purposes
		}
		for(int i =0;i< houseItemLayer.length; i++){
			for(int j = 0; j < houseItemLayer[0].length; j ++){
				s+=(houseItemLayer[i][j].toString());
			}
			s+="/"; //adds a comma for counting purposes
		}
    	System.out.print(s);
    	return s; 	
    }
    
    public boolean equalsTo(Map m){
    	boolean isEqual = true;
    	try{
    		for(int i =0; i < map.length && isEqual; i++){
    			for(int j =0; j < map[0].length && isEqual; j++){
    				isEqual = map[i][j].equals(m.map[i][j]);
    			}
    		}
    	}catch(Exception e){
    		isEqual = false;
    	}
    	System.out.println("Entered" + "    " + isEqual);
    	return isEqual;
    }
    
    public int getHeight(){
    	return getRow() * height;
    }
    
    public int getWidth(){
    	return getCol() * width;
    }
    
    public void setWidth(int x){
    	width = x;
    }
    
    public void setHeight(int x){
    	height = x;
    }
    
    public MapObject getSearchObj() {
		return searchObj;
	}
    
    public MapObject getMapObj(Point p){
    	return map[p.y][p.x];
    }
    
    public boolean mapAction(Point p){
    	return houseItemLayer[p.y][p.x].mapAction() && map[p.y][p.x].mapAction();
    }
    
    public void setSize(int s){ //sets the size of every object in the map;
    	for(int i = 0; i < map.length; i++){
    		for(int j = 0; j < map[0].length; j++){
    			map[i][j].setSize(s);
    		}
    	}
    	for(int i = 0; i < map.length; i++){
    		for(int j = 0; j < map[0].length; j++){
    			houseItemLayer[i][j].setSize(s);
    		}
    	}
    }
    
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}