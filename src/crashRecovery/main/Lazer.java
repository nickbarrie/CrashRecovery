/*
 * July 21st 2022
 * Nick Barrie 
 * Lazer which destroys all
 */
package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Random;

public class Lazer extends GameObject {

	private Handler handler;
	private float ex, ey;
	private double spawny;
	private double rise ,run, slope;
	private double velEY, velEX;
	private Random rand = new Random();
	private GameObject obj;
	private double xDiff;
	private double yDiff;
	private double xOffset;
	private double yOffset;
	private double  lineLength;
	private boolean aim = false;
	private long lastTrail = System.currentTimeMillis();

	
	public Lazer(int x, int y, int ex, int ey, ID id, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.ey = ey;
		this.ex = ex;
		rise = ey - y;
		run = ex - x;
		slope = run / rise;
		spawny = y;
		AudioPlayer.getSound("lazer").play(getFreq(),Game.effectVolume);
	}
	public Lazer(int x, int y, int ex, int ey, ID id, Game game, GameObject obj) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.ey = ey;
		this.ex = ex;
		rise = ey - y;
		run = ex - x;
		slope = run / rise;
		spawny = y;
		this.obj = obj;
		aim = true;
		AudioPlayer.getSound("lazer").play(getFreq(),Game.effectVolume);
			
		
	}
	public void aim() {
		
			

			float diffX = x - player.getX() - +32;// difference in x
			float diffY = y - player.getY() - +32;// difference in y
			float distance = (float) Math
					.sqrt((diffX) * (diffX) + (diffY) * (diffY));
			
				velX = ((-1 / distance) * diffX);
				velY = ((-1 / distance) * diffY);
				ex = (int) obj.getX() + 128;
				ey = (int) obj.getY()+64;

	}
	public void tick() {
		x += velX*2;
		y += velY*2;
		if(aim) {
			aim();
		}
	
		ey += velEY*3;
		ex += velEX*3;
		
		collision();
		
		 lineLength =0 ;
		 xDiff = (int) -((int) ex-x);
		 yDiff = (int) -((int) ey-y);
		 xOffset = 0;
		 yOffset = 0;
	
		lineLength =(int) Math.sqrt(Math.pow(xDiff,2)+Math.pow(yDiff, 2));
		xOffset = (xDiff/lineLength);
		yOffset =(yDiff/lineLength);
		
		if(System.currentTimeMillis() - lastTrail > 50) {
			lastTrail = System.currentTimeMillis();
		for(int i = 0; i < lineLength/5*Game.particleLevel; i +=15) {
		
				handler.addObject(new BasicParticle((int)(ex+xOffset*i),(int)(ey+yOffset*i),ID.Lazer,Color.RED,64,64,0.1f,game));
				if(rand.nextInt(10) == 1) {
					handler.addObject(new BasicParticle((int)(ex+xOffset*i),(int)(ey+yOffset*i),ID.Particle,Color.red,10,10,0.3f,game));
				}
			}
			
			}
		
		}
		
	

	public void render(Graphics g) {

	//	g.setColor(Color.red);

	//	g.drawLine((int) x, (int) y,(int) ex, (int)ey);

	}

	private void collision() {
		for (int i = 0; i < handler.Objects().size(); i++) {
			GameObject tempObject = handler.Objects().get(i);
			if (getLine().intersects(tempObject.getBounds())) {
				if (tempObject.getId() == ID.Wall) {// Collision with wall
					Wall platform = (Wall) tempObject;
					platform.adjustHealth(15);
					platform.deathCheck();
				} else if (tempObject.getId() == ID.BasicEnemy) {// Collision with wall
					tempObject.adjustHealth(20);
				} else if (tempObject.getId() == ID.ObjectSpawner) {// Collision with wall
					ObjectSpawner spawn = (ObjectSpawner) tempObject;
					spawn.adjustHealth(100);// do damage to robot
				} else if (tempObject.getId() == ID.TankEnemy) {// Collision with wall
					tempObject.adjustHealth(20);
				}else if (tempObject.getId() == ID.Player) {// Collision with wall
					tempObject.adjustHealth(1);
				}
				else if (tempObject.getId() == ID.LazerBot) {// Collision with wall
					tempObject.adjustHealth(20);
				}
				

			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 6, 6);
	}

	public Line2D getLine() {
		Line2D line = new Line2D.Float(x, y, ex, ey);
		return line;
	}


	@Override
	public Line2D getLine(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}

}
