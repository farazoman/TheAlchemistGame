package mapMaker;

import java.awt.Color;

public abstract class ActionMapObject extends MapObject {

	public ActionMapObject(int h, int w, char ch_Rep, Color o_C) {
		super(h, w, ch_Rep, o_C);
		type = ACTION_OBJECT;
	}

	public ActionMapObject(int h, int w, char ch_Rep, String str_ImgFile, Color c) {
		super(h, w, ch_Rep, str_ImgFile, c);
		type = ACTION_OBJECT;
	}
	
	protected ActionMapObject(ActionMapObject that){
		super(that);
	}

	@Override
	abstract public boolean mapAction();

}
