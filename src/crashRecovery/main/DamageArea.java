package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Random;

public class DamageArea extends GameObject{

	private float alpha = 1;
	private float life;
	private Handler handler;
	Random rand = new Random();
	private int width, height;
	private int type; 
	// 0 for missile 
	// 1 for flamethrower
	
	public DamageArea(float x, float y, ID id, int width, int height,float life, int type, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.width = width;
		this.height = height;
		this.life = life;
		this.type = type;
	
		
		this.width = width;
		this.height = height;
	}

	public void tick() {
		if(alpha > life) {
			alpha -= life;
		}else {
			handler.removeObject(this);
		}
		x += velX;
		y += velY;
		collision();
		
	}


	
	public void render(Graphics g) {
		//g.setColor(Color.magenta);
	//	g.fillRect((int)x, (int)y, width, width);
	}
	private void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);
			if (getBounds().intersects(tempObject.getBounds())) {
				
				if (tempObject.getId() == ID.BasicEnemy) {// Collision with wall
					tempObject.adjustHealth(30);
					
																														
				}
				else if (tempObject.getId() == ID.ObjectSpawner) {// Collision with wall
					ObjectSpawner spawn = (ObjectSpawner) tempObject;
					spawn.adjustHealth(100);// do damage to robot
					spawn.deathCheck();																						
				}
				else if (tempObject.getId() == ID.BossEnemy) {// Collision with wall
					BossEnemy boss = (BossEnemy) tempObject;
					boss.adjustHealth(100);// do damage to robot
																							
				}
				else if (tempObject.getId() == ID.TankEnemy) {// Collision with wall
					tempObject.adjustHealth(40);
				
				}
				else if (tempObject.getId() == ID.LazerBot) {// Collision with wall
					tempObject.adjustHealth(10);
				}
				else if (tempObject.getId() == ID.Wall) {// Collision with wall
					
					Wall platform = (Wall) tempObject;
					if(platform.getType() == type) {
					platform.adjustHealth(40);
					platform.deathCheck();
					}
					
			}
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
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
