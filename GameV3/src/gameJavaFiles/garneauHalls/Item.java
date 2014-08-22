package garneauHalls;

import java.awt.Image;

public class Item {

	@SuppressWarnings("unused")
	private Image pic;
	private String item, discription;
	private double cost;
	
	public Item(Image pic, String itemName, String discription, double cost) {
		this.pic = pic;
		this.item = itemName;
		this.discription = discription;
		this.cost = cost;
		
	}

	public String getItem(){
		return item;
	}
	
	public String getDiscription(){
		return discription;
	}
	
	public double getCost(){
		return cost;
	}
	
	public void setCost(double newCost){
		if(newCost < 0){
			this.cost = 0;
		}
		else if(cost > 1000000){
			this.cost = 10000;
		}
		else
			this.cost = newCost;
	}

}
