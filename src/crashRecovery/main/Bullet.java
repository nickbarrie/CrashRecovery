/*
 * July 21st 2022
 * Nick Barrie 
 * Bullet used by weapon 1, pierces enemy's despawns when outside map and when impacted wall
 */package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Bullet extends GameObject {

	private Handler handler;
	//private int mx, my;// mouse x and y
	private static int bulletSpeed = 5;
	private SpriteSheet ss;
	private int size = 64;
	private Image pic;
	private long lastTrail = System.currentTimeMillis();
	public Bullet(float x, float y, int mx, int my, ID id, Image image, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		pic = image;
		double angle = Math.atan2(mx - x, my - y);
		ss = new SpriteSheet(Game.sprite_Sheet);
		velX = (float) (bulletSpeed * Math.sin(angle));// get appropriate x and y speed using 9th grade math!
		velY = (float) (bulletSpeed * Math.cos(angle));
		this.image = ss.grabImage(image);
		xBound = 16;
		yBound = 16;
		AudioPlayer.getSound("shot").play(getFreq(),Game.effectVolume);
		if(image == Image.PlayerBullet) {
			for(int h = 0; h < 2*Game.particleLevel; h ++) {
				handler.addObject(new BasicParticle(x, y,(int)velX,(int)velY, ID.Particle, Color.CYAN, 6, 6, 0.05f,  game));// blood	
			}
		}
		if(image == Image.EnemyBullet) {
			for(int h = 0; h < 2*Game.particleLevel; h ++) {
				handler.addObject(new BasicParticle(x, y,(int)velX,(int)velY, ID.Particle, Color.PINK, 6, 6, 0.05f,  game));// blood	
			}
		}
		
	}
	public Bullet(int x, int y, int velX, int velY, ID id, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();

		ss = new SpriteSheet(Game.sprite_Sheet);
		this.velX = velX;
		this.velY = velY;
		pic = Image.BossBullet;
		image = ss.grabImage(Image.BossBullet);
		xBound = 16;
		yBound = 16;
		AudioPlayer.getSound("shot").play(getFreq(),Game.effectVolume);
		for(int h = 0; h < 2*Game.particleLevel; h ++) {
			handler.addObject(new BasicParticle(x, y,(int)velX,(int)velY, ID.Particle, Color.PINK, 6, 6, 0.05f,  game));// blood	
		}
	}

	public void tick() {
		
		x += velX;
		y += velY;
		if (outOfBounds(Game.WIDTH, Game.HEIGHT)) {// when out of bounds remove object
			handler.removeObject(this);
		}
		if(System.currentTimeMillis() - lastTrail > 40) {
			handler.addObject(new Trail(x,y,ID.Trail,pic,0.05f,game));
			lastTrail = System.currentTimeMillis();
			collision();
			if(rand.nextInt(10) <5) {
				size+=6;
			}
			if(rand.nextInt(10) >5) {
				size-=6;
			}else {
				size = 64;
			}
		}


	}

	public void render(Graphics g) {

		g.drawImage(image, (int)x,(int)y,size,size,null);

	}

	private void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);
			if (getBounds().intersects(tempObject.getCollision())) {
				if (tempObject.getId() == ID.Wall) {// Collision with wall
					handler.removeObject(this);
					if(id == ID.Bullet)
					for(int h = 0; h < 6*Game.particleLevel; h ++) {
						handler.addObject(new BasicParticle(x, y ,(int)-velX,(int)-velY, ID.Particle, Color.CYAN, 6, 6, 0.05f,  game));// blood	
					}
					else {
						for(int h = 0; h < 6*Game.particleLevel; h ++) {
							handler.addObject(new BasicParticle(x, y ,(int)-velX,(int)-velY, ID.Particle, Color.PINK, 6, 6, 0.05f,  game));// blood	
						}
					}
					
					
				}
				else if (tempObject.getId() == ID.BasicEnemy) {// Collision with wall
					tempObject.adjustHealth(30);
					for(int h = 0; h <3*Game.particleLevel; h ++) {
						handler.addObject(new BasicParticle(x, y,(int)-velX,(int)-velY, ID.Particle, Color.RED, 6, 6, 0.05f,  game));// blood	
					}
				}
				else if (tempObject.getId() == ID.BossEnemy && id == ID.Bullet) {// Collision with wall
					tempObject.adjustHealth(100);
					for(int h = 0; h < 3*Game.particleLevel; h ++) {
						handler.addObject(new BasicParticle(x, y,(int)-velX,(int)-velY, ID.Particle, Color.RED, 6, 6, 0.05f,  game));// blood	
					}
				}
				else if (tempObject.getId() == ID.ObjectSpawner) {// Collision with wall
					ObjectSpawner spawn = (ObjectSpawner) tempObject;
					tempObject.adjustHealth(30);
					for(int h = 0; h < 3*Game.particleLevel; h ++) {
					handler.addObject(new BasicParticle(x + 16, y + 30,(int)-velX,(int)-velY, ID.Particle, Color.CYAN, 3, 3, 0.1f,  game));
					}
					spawn.deathCheck();																						
				}
				else if (tempObject.getId() == ID.TankEnemy && id == ID.Bullet) {// Collision with wall
					tempObject.adjustHealth(30);		
					for(int h = 0; h <  3*Game.particleLevel; h ++) {
						handler.addObject(new BasicParticle(x, y,(int)-velX,(int)-velY, ID.Particle, Color.RED, 6, 6, 0.05f,  game));// blood	
					}
				}
				else if (tempObject.getId() == ID.LazerBot && id == ID.Bullet) {// Collision with wall
					tempObject.adjustHealth(30);
					for(int h = 0; h <  3*Game.particleLevel; h ++) {
						handler.addObject(new BasicParticle(x, y,(int)-velX,(int)-velY, ID.Particle, Color.RED, 6, 6, 0.05f,  game));// blood	
					}
				
				}
		
				
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, xBound,yBound);
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
