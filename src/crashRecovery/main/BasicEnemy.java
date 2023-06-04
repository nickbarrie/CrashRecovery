/*
 * July 17th 2022
 * Nick Barrie 
 * Simply Enemy robot damages player when in contact tracks the player with a line of sight
 * All Code was written long before July 17th 2022 but comments started then
 */
package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import crashRecovery.main.Game.STATE;

public class BasicEnemy extends GameObject {
	// Game class objecys
	private HUD hud;
	private Player player;
	// Random
	private int r;// determines what is dropped upon death
	// image utilite
	private int ani = 0;// animation selection
	private int frame = 0;// animation timer
	private SpriteSheet ss;
	private ObjectSpawner spawn;
	private int size = 64;
	public BasicEnemy(int x, int y, ID id, Game game, ObjectSpawner spawn) {
		super(x, y, id, game);
		this.hud = game.getHud();
		this.game = game;
		this.spawn = spawn;// if created by a spawner
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.BasicEnemy1);
		damageFlash = ss.grabImage(Image.BasicHit);
		player = handler.getPlayer();
		xBound = 64;
		yBound = 64;
	}
	public void death() {
		handler.removeObject(this);
		hud.kill();
		deathAni();
		AudioPlayer.getSound("bossDeath").play(getFreq(),Game.effectVolume);
		if (spawn != null) {
			spawn.spawnTick();
		}
		r = rand.nextInt(6);
		if (r == 3) {
			handler.addObject(new Drop(x, y, ID.MissileDrop, game, Image.MissileDrop));// Missile ammo
		}
		if (r == 2) {
			handler.addObject(new Drop(x, y, ID.BulletDrop, game, Image.BulletDrop));// pistol ammo
		}
		if (r == 1) {
			handler.addObject(new Drop(x, y, ID.HealthDrop, game, Image.HealthDrop));// health
		}
		if (handler.checkEnemy() && handler.getLvl() == 2) {// when stage 2 battery drops from last robot
			handler.addObject(new CheckPoint((int) x, (int) y, ID.CheckPoint, game, Image.Battery));// battery
		}
	}
	public void deathAni() {
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.BasicHead,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.BasicBody,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.BasicArm,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.BasicLeg,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.BasicArm,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.BasicLeg,0.02f,game));
		
	
	}
	// tick method
	public void tick() {
		
		if(game.diff == 1) { // Any other Mode
			x += velX*1.4;// updates enemy position
			y += velY*1.4;
		}
		else {// Hard Mode
			x += velX;// updates enemy position
			y += velY;
		}
		
		enemyMovment(vision(player));
		if( Game.gameState != STATE.Beat) {

			x = Game.clamp(x, 7, Game.WIDTH - 48);// contain within the bounds
			y = Game.clamp(y, 7, Game.HEIGHT - 77);
		}
		if(System.currentTimeMillis() - tickWait > 40) {
			tickWait = System.currentTimeMillis();
			collision();
			
		}
		if (health <= 0) {// when dead
			death();
		}
		if (velX != 0 || velY != 0) {// when moving
			frame++;
			if (frame > 40) {
				ani++;
				frame = 0;
			}
		}
		
		if (ani % 2 == 0 && !image.equals(ss.grabImage(Image.BasicEnemy1))) {
			size = 64;
			image = ss.grabImage(Image.BasicEnemy1);
			walkSounds();
		} if (ani % 2 == 1&& !image.equals(ss.grabImage(Image.BasicEnemy2))) {
			size = 64;
			image = ss.grabImage(Image.BasicEnemy2);
			walkSounds2();
		}
		if (System.currentTimeMillis() - lastHit < immunityLength) {
			image = ss.grabImage(Image.BasicHit);
			shrinkAni();
		}
		}

	


	public void shrinkAni() {
		
			 if(size  > 40 && !maxGrowth) {
					size-=8;
				}
			 if(size <40) {
				 maxGrowth = true;
			 }
			  if(maxGrowth) {

				size+=8;
			}
			
		
	
	}
	// Collision method (runs) within tick
	private void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);

			
			if (tempObject.getId() == ID.Wall) {// Collision with wall
				if (getBounds().intersects(tempObject.getBounds())) {// robot is against a wall
					if (velX > 0) {
						x -= 5;
					}
					if (velX < 0) {
						x += 5;
					}
					if (velY > 0) {
						y -= 5;
					}
					if (velY < 0) {
						y += 5;
					} // prevent movement through wall
					velX = 0;
					velY = 0;
				}
			}
		}
	}
	public void walkSounds() {
		if(!AudioPlayer.getSound("basicEnemyWalk").playing()) {
			AudioPlayer.getSound("basicEnemyWalk").play(getFreq(),Game.effectVolume);
		}
		
	}
	public void walkSounds2() {
		if(!AudioPlayer.getSound("basicEnemyWalk2").playing()) {
			AudioPlayer.getSound("basicEnemyWalk2").play(getFreq(),Game.effectVolume);
		}
		
	}
	// render method
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(0.5f));
		g.setColor(Color.BLACK);
		g.fillOval((int)x+15, (int)y+50,40, 20);
		g2d.setComposite(makeTransparent(1f));
		g.drawImage(image, (int) x, (int) y, size, size, null);
		drawHealthBar(g);
		g2d.setComposite(makeTransparent(1));
	}


	// boundrys
	public Rectangle getBounds() {
		return new Rectangle((int) x + 24, (int) y + 16, xBound, yBound);

	}

	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
	public Rectangle getCollision() {
		return new Rectangle((int) x, (int) y, xBound, yBound);
		
	}
}
