package mapMaker;

import java.awt.Color;

public class ObstacleMapObject extends MapObject {
	
	protected ObstacleMapObject(int h, int w) {
		super(h, w);
		type = OBSTACLE;
	}
	
	protected ObstacleMapObject(int h, int w, char letter, String str_File, Color c) {
		super(h,w,letter, str_File, c);
		type = OBSTACLE;
	}
	
	protected ObstacleMapObject(ObstacleMapObject that){
		super(that);
	}
	
	@Override
	public boolean mapAction() {
		if(type == MapObject.GROUND_OBJECT)
			return true;
		else if(type == MapObject.OBSTACLE)
			return false;
		return false;
	}

}
