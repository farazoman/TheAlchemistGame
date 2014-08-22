package mapMaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public abstract class MapObject {
	
	protected char ch_ObjChar; //Data field for the map object
	protected Color o_Colour;
	protected Image o_Icon;
	protected int height, width;
	protected int type;
	protected String url;
	static final int ACTION_OBJECT = 1;
	static final int OBSTACLE = -1;
	static final int GROUND_OBJECT = 0;
	static final int CONNECTING_OBJ = 5;

	protected MapObject(int h, int w){ //Constructor accepting height and width making a black colour object with no image
		ch_ObjChar = 'w';
		o_Colour = Color.BLACK;
		o_Icon = null;
		height = h;
		width = w;
	}
	
	protected MapObject(int h, int w, char ch_Rep, Color o_C){ //acccepts height, width, a character representating and a color no image
		ch_ObjChar = ch_Rep;
		o_Colour = o_C;
		o_Icon = null;
		height = h;
		width = w;
	}
	
	protected MapObject(int h, int w, char ch_Rep, String str_ImgFile, Color c){ //same as above but take an image as well with the colour as a backup
		ch_ObjChar = ch_Rep;
		o_Colour = c;
		height = h;
		width = w;
		o_Icon = loadImage(str_ImgFile);
		url = str_ImgFile;
	}
	
	protected MapObject(MapObject that){ //copies a mapobject
		this.ch_ObjChar = that.ch_ObjChar;
		this.o_Colour = that.o_Colour;
		this.o_Icon = that.o_Icon;
		this.height = that.height;
		this.width = that.width;
		this.type = that.type;
	}
	
	protected Image loadImage (String name){ //loads an image
		Image img = null;
		File file = new File(name); //uses string and creates a file object
		try
		{
		    img = ImageIO.read (file); // load file into Image object
			img = img.getScaledInstance(width, height, Image.SCALE_FAST);  //scales the image to the height and width of the object
		}
		catch (IOException e)
		{
		}
	
		return img; //returns the Image
	}
	
	abstract public boolean mapAction(); //used for when making game returns true if you can walk on this obj and false if you can't and contians actions to take if activated
	
	public boolean isObject(char ch){ //returns if the character parameter is equal the the objects character
		return ch == ch_ObjChar;
	}
	
	public int getType(){ //returns type of object
		return type;
	}
	
	public void setH(int h){ //sets height and re creaetes image
		height = h;
		if(o_Icon != null){
			o_Icon = o_Icon.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}
	}
	
	public void setW(int w){ //same as above
		width = w;
		if(o_Icon != null){
			o_Icon = o_Icon.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}
	}
	
	public void setSize(int s){ //same as above
		width = s;
		height = s;
		if(o_Icon != null){
			o_Icon = loadImage(url);
		}
	}
	public void makeObstacle(){
		type = MapObject.OBSTACLE;
	}
	
	public void show(Graphics g, int xPos, int yPos){ //shows the object
		if(o_Icon == null){ //if there is not a valid image then show the color
			g.setColor(o_Colour);
			g.fillRect(xPos, yPos, width, height);
		}
		else{ //otherwise show the image
			g.drawImage(o_Icon, xPos, yPos, null);
			
		}
	}
	
	public boolean equals(MapObject m){ //compares two map objects
		return this.ch_ObjChar == m.ch_ObjChar;
	}
	
	public String toString(){ //comverts object to string(returns the character representation of the object)
		return ch_ObjChar+"";
	}
}
