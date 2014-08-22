package garneauHalls;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class StatPoints {
	private int maxHealth, currentHealth;
	private int totalXP, level;
	private int coins;
	private int strength;
	private BufferedImage statPic;
	private boolean levelUp = false;
	
	
	public StatPoints(int maxH, int maxE) { //For the player
		maxHealth = 35;
		currentHealth = maxHealth;
		level = 1;
		totalXP = 0;
		strength = 5;
		coins = 0;
		paintToBuffer();
	}
	
	public StatPoints(int maxH, int coins,int strength) {  //For the enemy
		maxHealth = maxH;
		currentHealth = maxHealth;
		this.strength = strength;
		this.coins = coins;
		this.level = (maxHealth + strength + 15)/6;
		totalXP = XPFormula() / 2;
	}
	
	public int getXP(){ //returns the total xp used for giving xp to player from enemy
		return totalXP;
	}
	
	private int XPFormula(){ //random fomula to calculate xp
		return (int) (Math.pow(2.0, (double)level) + Math.abs(Math.sin((double)level + Math.log((double)level))));
	}
	
	public void XPUp(int n){
		if(n < 0)
			n = 0;
		totalXP += n;
		while(totalXP > XPFormula()){ //used to increase player xp and finds if they leveled up and increases stats
			level ++;
			strength += 5;
			maxHealth += 5;
			currentHealth = maxHealth;
			levelUp = true;
			paintToBuffer();
			//TODO Graphics? for leveling up!!!
		}
	}
	
	public boolean isAlive(){
		return (currentHealth <= 0);
	}
	
	public boolean changeHealth(int n){//lowers health based on parameter
		int temp = currentHealth + n;
		if(temp < 0)
			currentHealth = 0;
		else if(temp > maxHealth)
			currentHealth = maxHealth;
		else
			currentHealth = temp;
		return isAlive();	
	}
	public boolean changeHealth(int n, Integer x){  //looks same as above except for paint to buffer
		int temp = currentHealth + n;
		if(temp < 0)
			currentHealth = 0;
		else if(temp > maxHealth)
			currentHealth = maxHealth;
		else
			currentHealth = temp;
		paintToBuffer();
		return isAlive();	
	}
	
	public void changeMoney(int m){ //not used
		coins += m;
		if(coins < 0)
			coins = 0;
	}
	
	public int getStrength(){ //getter methods.
		return strength;
	}
	
	public int getHealth(){
		return currentHealth;
	}
	
	public int checkHealth(){
		return currentHealth;
	}

	public int checkMoney(){
		return coins;
	}
	
	public BufferedImage getImage(){
		return statPic;
	}
	
	public void paintToBuffer(){ //paints the health bar to a buffered image
		statPic = new BufferedImage(200, 85, BufferedImage.TYPE_INT_RGB);
		Font font = new Font(Font.SANS_SERIF,Font.BOLD,13);
		Font font2 = new Font(Font.SANS_SERIF,Font.BOLD,16);
		Color c;
		int i = (int)((currentHealth/(double)maxHealth) * 150);
		if(i < 40){
			c = new Color(180,12,12);
		}
		else{
			c = new Color(90,235,70);
		}

		Graphics2D g2d = statPic.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, 150, 100);
		g2d.setColor(c);
		g2d.fillRect(20,20, i,30);
		g2d.setColor(Color.gray);
		g2d.fillRect(10, 10, 10, 40);
		g2d.fillRect(10, 40, 160, 20);
		g2d.setColor(Color.red);
		g2d.setFont(font);
		g2d.drawString("HP", 20, 70);
		if(levelUp){ //if player has leved up then draws "level up" to let them know they leveld up
			levelUp = false;
			g2d.setFont(font2);
			g2d.setColor(Color.yellow);
			g2d.drawString("*Level Up*", 50, 36);
		}
		g2d.setFont(font);
		g2d.setColor(Color.gray);
		g2d.drawString("L" + level, 45, 70);
		
	}

}
