package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Switch extends GameObject{
	private int task;
	private Game game;
	private Handler handler;
	private Lazer lazer;
	private int xbound = 64;
	private int ybound = 64;
	private BufferedImage swich;
	private SpriteSheet ss;
	private Punch punch;
	public Switch(float x, float y, ID id, int task, Game game) {
		super(x, y, id,game);
		this.task = task;
		this.game = game;
		this.handler = game.getHandler();
		ss = new SpriteSheet(Game.sprite_Sheet);
		// Fires lazers out of ship into cave
		if(task == 0) {
			xbound = 192;
			ybound = 192;
		}
		if(task == 1 ) {
			swich = ss.grabImage(10, 5, 16, 16);//scanner
		}
	}
	public void tick() {
		
		
	}

	public void render(Graphics g) {
		if(task ==1 ) {
		g.drawImage(swich, (int) x, (int) y, (int) x + 64, (int) y + 64, 0, 0, 16, 16, null);
		}
	}
	public void hit(Punch newPunch) {
		
		if(newPunch != punch) {
			punch = newPunch;
			//Fire ship Lazer beam
		 if(task == 0) {
			 if(lazer == null) {
				 lazer = 	new Lazer(95,660,900,100,ID.Lazer,game);
				 handler.addObject(lazer);
			 }else {
				 handler.removeObject(lazer);
				 AudioPlayer.getSound("lazer").stop();
				 lazer = null;
			 }
			
		 }//Scan key Card
		 else if(task == 1 && handler.getPlayer().getCheck(4)) {
				 handler.getPlayer().setCheck(false, 4);
				 AudioPlayer.getSound("cardScan").play();
				 while(handler.getWall(1) != null) {
					 handler.removeObject(handler.getWall(1));
				 }
			
			 
		 }
		}
	}
	public Rectangle getBounds() {
		
		return new Rectangle((int) x,(int) y, xbound,ybound);
	}

	public Line2D getLine(float x, float y) {
		
		return null;
	}

	public boolean vision() {
		
		return false;
	}
	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}

}
