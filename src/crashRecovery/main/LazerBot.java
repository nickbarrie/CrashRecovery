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
import java.awt.image.BufferedImage;
import java.util.Random;

public class LazerBot extends GameObject {
	// Game class objects
	private Handler handler;
	private HUD hud;
	private Player player;
	private Game game;
	private int fireChance = 50;
	// Random
	Random rand = new Random();
	private int r;// determines what is dropped upon death
	// Health
	private int health = 50;// when below 0 enemy will be deleted
	// image utilites
	private BufferedImage enemy;
	SpriteSheet ss;

	private ObjectSpawner spawn;

	public LazerBot(int x, int y, ID id, Game game, ObjectSpawner spawn) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.game = game;
		this.spawn = spawn;// if created by a spawner
		ss = new SpriteSheet(Game.sprite_Sheet);
		enemy = ss.grabImage(13, 4, 16, 16);
	
		player = handler.getPlayer();

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
	if (r >= 2) {
		handler.addObject(new Drop(x, y, ID.BulletDrop, game,Image.BulletDrop));// pistol ammo
	}
	if (r == 1) {
		handler.addObject(new Drop(x, y, ID.HealthDrop, game, Image.HealthDrop));// health
	}
	if (r == 4) {
		handler.addObject(new Drop(x, y, ID.FlameDrop, game, Image.FlameDrop));// flame
	}
	if (handler.checkEnemy() && handler.getLvl() == 2) {// when stage 2 battery drops from last robot
		handler.addObject(new CheckPoint((int) x, (int) y, ID.CheckPoint, game, Image.Battery));// battery
	}
}
	// tick method
	public void tick() {
		x += velX / 2;// updates enemy position
		y += velY / 2;
		enemyMovment(vision(player));
		if(vision(player)) {
			if(game.diff == 1) {
				fireChance = 30;
			}
			if(rand.nextInt(fireChance) == 1) {
			handler.addObject(new Bullet((int)x+36,(int)y+16,(int)player.getX()+rand.nextInt(64)-32,(int)player.getY()+rand.nextInt(64)-32,ID.Ray,Image.EnemyBullet,game));
			image = ss.grabImage(Image.LazerBotShoot);
			AudioPlayer.getSound("shot").play(getFreq(),Game.effectVolume);
			}
			
		}
		x = Game.clamp(x, 7, Game.WIDTH - 48);// contain within the bounds
		y = Game.clamp(y, 7, Game.HEIGHT - 77);
		collision();
		if (health <= 0) {// when dead
			death();
		}
		if (velX != 0 || velY != 0) {// when moving
			enemy = ss.grabImage(12, 4, 16, 16);
			}
		else {
			enemy = ss.grabImage(13, 4, 16, 16);
		}
	}

	public void adjustHealth(int adjust) {
		health -= adjust;
		handler.addObject(new BasicParticle(x + 16, y + 30, ID.Particle, Color.RED, 6, 6, 0.05f, game));// blood
		// particle
		enemy = ss.grabImage(Image.LazerHit);
	}

	// Collision method (runs) within tick
	private void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);

			if (tempObject.getId() == ID.Wall) {// Collision with wall
				if (getBounds().intersects(tempObject.getBounds())) {// robot is against a wall
					setWall(true);
					if (velX > 0) {
						x -= 5;
						setWall(false);
					}
					if (velX < 0) {
						x += 5;
						setWall(false);
					}
					if (velY > 0) {
						y -= 5;
						setWall(false);
					}
					if (velY < 0) {
						y += 5;
						setWall(false);
					} // prevent movement through wall
					velX = 0;
					velY = 0;
				}
			}
		}
	}
	public void deathAni() {
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.LazerHead,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.LazerBoost,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.LazerBoostDead,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.LazerTop,0.02f,game));

	
	}
	// render method
	public void render(Graphics g) {
			g2d = (Graphics2D) g;
			g2d.setComposite(makeTransparent(0.5f));
			g.setColor(Color.BLACK);
			g.fillOval((int)x-4, (int)y+50,70, 20);
			g2d.setComposite(makeTransparent(1f));
			g.drawImage(enemy, (int) x, (int) y, 64,64, null);
			drawHealthBar(g);
			g2d.setComposite(makeTransparent(1));

		}

	// boundrys
	public Rectangle getBounds() {
		return new Rectangle((int) x + 24, (int) y + 16, 20, 50);

	}

	// check is player can be seen
	public boolean vision() {

		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempWall = handler.getCollisionObjects().get(i);
			if (tempWall.getId() == ID.Wall) {
				Line2D line = player.getLine(x, y);// get line from player

				if (line.intersects(tempWall.getBounds())) {// if line intersects wall player cant be seen
					return false;
				}
			}

		}

		return true;
	}

	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
}
