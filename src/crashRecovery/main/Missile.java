package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Missile extends GameObject {

	private Handler handler;
	private static int bulletSpeed = 4;
	private SpriteSheet ss;
	public Missile(float x, float y, int mx, int my, ID id, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		AudioPlayer.getSound("missileLaunch").play(getFreq(),Game.effectVolume);
		double angle = Math.atan2(mx - x, my - y);
		velX = (float) (bulletSpeed * Math.sin(angle));
		velY = (float) (bulletSpeed * Math.cos(angle));
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.Missile);
		for(int h = 0; h < 2*Game.particleLevel; h ++) {
			handler.addObject(new BasicParticle(x, y,(int)velX,(int)velY, ID.Particle, Color.WHITE, 6, 6, 0.05f,  game));// blood	
		}
	}

	public void tick() {
		collision();
		x += velX;
		y += velY;
		if (outOfBounds(Game.WIDTH, Game.HEIGHT)) {
			handler.removeObject(this);
		}
		if(System.currentTimeMillis() - tickWait > 12*Game.particleLevel) {
			handler.addObject(new Trail(x-64,y-64,ID.Trail,Image.Missile,0.05f,game));
			tickWait = System.currentTimeMillis();
		}
	}

	public void render(Graphics g) {

		g.drawImage(image,(int)x-64, (int) y-64	, 64, 64, null);

	}

	public void missileHit() {
	
		for (int j = 0; j < 3*Game.particleLevel; j++) {
			
			handler.addObject(
					new FlameParticle(x, y, ID.MissileParticle, 100, 100, 0.03f, 0, game));
		
		}
		handler.addObject( new DamageArea(x-64,y-64,ID.MissileParticle,128,128,0.03f,0,game));
		AudioPlayer.getSound("explosion").play(getFreq(),1);
		handler.removeObject(this);
	}

	private void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);
			if (getBounds().intersects(tempObject.getBounds())) {
				if (tempObject.getId() == ID.Wall) {// Collision with wall
					missileHit();
				}

				else if (tempObject.getId() == ID.BasicEnemy) {// Collision with wall
					missileHit();
				}

				else if (tempObject.getId() == ID.BossEnemy) {// Collision with wall
					BossEnemy boss = (BossEnemy) tempObject;
					boss.stopLazer();
					missileHit();
					
				} 
			 else if (tempObject.getId() == ID.TankEnemy) {// Collision with wall
					missileHit();
			 }
			 else if (tempObject.getId() == ID.ObjectSpawner) {// Collision with wall
					missileHit();
			}
			 else if(tempObject.getId() == ID.LazerBot) {
					missileHit();
			 }
		}
	}
	}
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 10, 10);
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
