package mapMaker;

import java.awt.Point;

public class MapConnector {
	private Map mapToGO;
	@SuppressWarnings("unused")
	private Map originMap;
	private Point startAt, location;
	//contains the origin map, the map to goto and the coord to start on the new map at and the coord where the connector is on the origin map
	public MapConnector(Map originMap, Map mapToGO, Point srt, Point coord) { 
		this.mapToGO = mapToGO;
		this.originMap = originMap;
		startAt = srt;
		location = coord;
	}
	//getters...
	public Map getMap(){ 
		return mapToGO;
	}
	
	public Point getStart(){
		return startAt;
	}
	
	public boolean isEqual(Point p){ 
		if(p.x == location.x && p.y == location.y)
			return true;
		return false;
	}


}
