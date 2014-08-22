package mapMaker;

import java.awt.Color;
import java.io.File;

public final class MapObjectList {
	private static final int h = 5, w = 5;
	private static final Color brown = new Color(160, 82, 45);
	
	public static final String dir = (new File("")).getAbsolutePath() + "/imgs/";
	public static final String str_Brick = dir + "mario_Brick.jpg";
	public static final String str_Door = dir +  "mario_Door.jpg";
	public static final String str_Grass = dir + "mario_Grass.jpg";
	public static final String str_Ground = dir + "mario_Ground.jpg";
	public static final String str_Key = dir + "mario_Key.png";
	public static final String str_Ice = dir + "mario_Ice.png";
	public static final String game_Door = dir + "Door.png";
	public static final String game_Floor = dir + "SchoolFloor.png";
	public static final String game_Locker = dir + "Locker.png";
	public static final String str_house = dir + "House.png";
	public static final String str_house1  = dir + "House1.png";
	public static final String str_house2  = dir + "House2.png";
	public static final String str_house3 = dir + "House3.png";
	public static final String str_house4 = dir + "House4.png";
	public static final String str_house5 = dir + "House5.png";
	
	public static final MapObject seeThrough =new GroundMapObject(h,w,'t',"imgs/eraseImg.png", Color.WHITE);
	public static final MapObject doorObj = new ObstacleMapObject(h, w, 'D', str_Door, Color.RED);
	public static final MapObject grassObj = new GroundMapObject(h, w, 'R', str_Grass, Color.green);
	public static final MapObject wallObj = new ObstacleMapObject(h, w, 'W', str_Brick, Color.BLACK);
	public static final MapObject groundObj = new ObstacleMapObject(h, w, 'G', str_Ground, brown);
	public static final MapObject iceObj = new GroundMapObject(h, w, 'I', str_Ice, Color.cyan);
	public static final MapObject keyObj = new ObstacleMapObject(h, w, 'K', str_Key, Color.orange);
	public static final MapObject gameDoor = new GroundMapObject(h,w, 'd', game_Door, new Color(150,150,150));
	public static final MapObject gameFloor = new GroundMapObject(h,w, 'f', game_Floor, Color.GRAY);
	public static final MapObject gameLocker = new ObstacleMapObject(h,w, 'l', game_Locker, Color.LIGHT_GRAY);
	public static final MapObject gameVoid = new ObstacleMapObject(h,w);
	public static final MapObject house = new ObstacleMapObject(h,w, '1', str_house, Color.LIGHT_GRAY);
	public static final MapObject house1 = new ObstacleMapObject(h,w, '2', str_house1, Color.LIGHT_GRAY);
	public static final MapObject house2 = new ObstacleMapObject(h,w, '3', str_house2, Color.LIGHT_GRAY);
	public static final MapObject house3 = new ObstacleMapObject(h,w, '4', str_house3, Color.LIGHT_GRAY);
	public static final MapObject house4 = new ObstacleMapObject(h,w, '5', str_house4, Color.LIGHT_GRAY);
	public static final MapObject house5 = new ObstacleMapObject(h,w, '6', str_house5, Color.LIGHT_GRAY);
	public static final MapObject deleteObj = new GroundMapObject(h, w, ' ', Color.white);
	
	public static final MapObject[] iterateMap = {doorObj, grassObj, gameVoid, wallObj, groundObj, iceObj, keyObj, deleteObj, gameFloor, gameLocker, gameDoor, house, house1, house2, house3, house4, house5, seeThrough};

}
