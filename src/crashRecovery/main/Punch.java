package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Punch extends GameObject {

	private float alpha = 1;
	private float life;
	private Handler handler;
	private int dir;
	private static int punchSpeed = 4;
	Player player;

	public Punch(float x, float y, int dir, ID id, Color color, float life, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		player = handler.getPlayer();
		player.punched(this);
		this.dir = dir;
		this.life = life;
		setDirection();
		
	}

	public void tick() {
		if (alpha > life) {
			alpha -= life;
		} else {
			player.punched(null);
			handler.removeObject(this);
		}
		x += velX;
		y += velY;
		collision();
	}

	public void setDirection() {
		if (dir == 0) {
			velX = 0;
			velY = -5;
		} else if (dir == 1) {
			velX = 4;
			velY = -4;
		} else if (dir == 2) {
			velX = 5;
			velY = 0;
		} else if (dir == 3) {
			velX = 4;
			velY = 4;
		} else if (dir == 4) {
			velX = 0;
			velY = 5;
		} else if (dir == 5) {
			velX = -4;
			velY = 4;
		} else if (dir == 6) {
			velX = -5;
			velY = 0;
		} else if (dir == 7) {
			velX = -4;
			velY = -4;
		}
	}

	public void render(Graphics g) {
		//g.drawRect((int)x,(int) y, 20, 20);


	}

	public void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);
			if (getBounds().intersects(tempObject.getBounds())) {
				if (tempObject.getId() == ID.Wall) {// Collision with wall
					Wall platform = (Wall) tempObject;
					platform.adjustHealth(10);
					platform.deathCheck();
					for(int l = 0; l < 3*Game.particleLevel; l++) {
					handler.addObject(new BasicParticle(x, y,-velX,-velY, ID.Particle, Color.BLACK, 3, 3, 0.1f, game));
					}
				}
				else if (tempObject.getId() == ID.ObjectSpawner) {// Collision with wall
					ObjectSpawner spawn = (ObjectSpawner) tempObject;
					spawn.adjustHealth(100);// do damage to robot
					spawn.deathCheck();
					for(int l = 0; l < 3*Game.particleLevel; l++) {
					handler.addObject(new BasicParticle(x, y,-velX,-velY, ID.Particle, Color.CYAN, 3, 3, 0.1f, game));
					}
																														
				}
				else if (tempObject.getId() == ID.BasicEnemy) {// Collision with wall
					tempObject.adjustHealth(30);
					tempObject.knockBack(this);
					for(int l = 0; l < 3*Game.particleLevel; l++) {
						handler.addObject(new BasicParticle(x, y,-velX,-velY, ID.Particle, Color.RED, 3, 3, 0.1f, game));
						}
				}
				else if (tempObject.getId() == ID.TankEnemy) {// Collision with wall
					
					tempObject.adjustHealth(20);
					for(int l = 0; l < 3*Game.particleLevel; l++) {
						handler.addObject(new BasicParticle(x, y,-velX,-velY, ID.Particle, Color.RED, 3, 3, 0.1f, game));
						}
					
				}
				else if(tempObject.getId() == ID.Switch) {
					Switch trigger = (Switch) tempObject;
					trigger.hit(this);
				}
				else if (tempObject.getId() == ID.LazerBot) {// Collision with wall
					tempObject.adjustHealth(30);
					tempObject.knockBack(this);
					for(int l = 0; l < 3*Game.particleLevel; l++) {
						handler.addObject(new BasicParticle(x, y,-velX,-velY, ID.Particle, Color.RED, 3, 3, 0.1f, game));
						}
					
				}

			}

		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 10, 40);
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
