package mapMaker;

import java.awt.Color;

public class GroundMapObject extends MapObject {

	public GroundMapObject(int h, int w, char ch_Rep, Color o_C) {
		super(h, w, ch_Rep, o_C);
		type = MapObject.GROUND_OBJECT;
	}

	public GroundMapObject(int h, int w, char ch_Rep, String str_ImgFile, Color c) {
		super(h, w, ch_Rep, str_ImgFile, c);
		type = MapObject.GROUND_OBJECT;
	}

	@Override
	public boolean mapAction() {
		if(type == MapObject.GROUND_OBJECT)
			return true;
		else if(type == MapObject.OBSTACLE)
			return false;
		return true;
	}


}
