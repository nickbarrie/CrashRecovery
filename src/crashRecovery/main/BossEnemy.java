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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class BossEnemy extends GameObject {
	// Game class objects
	private Handler handler;
	private HUD hud;
	private Game game;
	// player and this
	private Player player;

	// Random drop
	Random rand = new Random();
	private int chance;
	// image stuff
	private SpriteSheet ss;
	private ArrayList<BufferedImage> images = new ArrayList<>();
	private Graphics2D g2d;
	// attack stuff
	private boolean tankSpawn = false;
	private Lazer lazer;
	private boolean spray = false;
	private long lastAttack = System.currentTimeMillis();
	private long attackSpace = System.currentTimeMillis();
	private int frameCount = 0;
	private int attackFreq = 5000;
	private double bullX;
	private double bullY;
	private boolean story = true;
	private GameObject[] walls = { null, null, null, null, null };

	public BossEnemy(int x, int y, ID id, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.game = game;
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.BossLow);
		player = handler.getPlayer();
		images.add(ss.grabImage(Image.BossHover));
		images.add(ss.grabImage(Image.BossHover2));
		health = 150;// when below 0 enemy will be deleted
		damageFlash = ss.grabImage(Image.BossHit);
		xBound = 192;
		yBound = 192;
		walls[0] = new Wall(0, 64 * 0, ID.Wall, 1, game);
		walls[1] = new Wall(0, 64 * 1, ID.Wall, 1, game);
		walls[2] = new Wall(0, 64 * 2, ID.Wall, 1, game);
		walls[3] = new Wall(0, 64 * 3, ID.Wall, 1, game);
		walls[4] = new Wall(0, 64 * 4, ID.Wall, 1, game);
		for (int l = 0; l < walls.length; l++) {
			handler.storeLevel(3, walls[l]);
		}

	}

	public BossEnemy(int x, int y, ID id, Game game, boolean mode) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.game = game;
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.BossLow);
		player = handler.getPlayer();

		images.add(ss.grabImage(Image.BossHover));
		images.add(ss.grabImage(Image.BossHover2));
		health = 5000;// when below 0 enemy will be deleted
		damageFlash = ss.grabImage(Image.BossHit);
		xBound = 192;
		yBound = 192;
		story = false;
	}

	private void bulletSpray() {

		for (double i = 0; i < 360; i += 22.5) {
			bullX = xComponent(i) * 5;
			bullY = yComponent(i) * 5;
			handler.addObject(
					new Bullet((int) (x + 128), (int) (y + 64), (int) bullX, (int) bullY, ID.BossBullet, game));

		}
		AudioPlayer.getSound("bossLazerShoot").play(getFreq(), Game.effectVolume);
	}

	private double xComponent(double angle) {
		return Math.sin(Math.toRadians(angle));

	}

	private double yComponent(double angle) {

		return Math.cos(Math.toRadians(angle));

	}

	public void lazerAttack() {
		if (chance == 11 && lazer == null && !tankSpawn && !spray) {
			lazer = new Lazer((int) x + 128, (int) y + 128, (int) x + 128, (int) y + 128, ID.Lazer, game, this);
			handler.addObject(lazer);
		}
	}

	public void tankAttack() {
		if (chance == 12 && !spray && lazer == null) {
			tankSpawn = true;
		}
		if (tankSpawn && System.currentTimeMillis() - attackSpace > 1500) {
			AudioPlayer.getSound("bossTankSpawn").play(getFreq(), Game.effectVolume);
			handler.addObject(
					new TankEnemy((int) x + rand.nextInt(256), (int) y + rand.nextInt(256), ID.TankEnemy, game, null));
			attackSpace = System.currentTimeMillis();
		}
	}

	public void bulletAttack() {
		if (chance == 10 && lazer == null && !tankSpawn) {
			spray = true;

		}
		if (System.currentTimeMillis() - attackSpace > 1000 && spray) {

			bulletSpray();
			attackSpace = System.currentTimeMillis();
		}

	}

	public void itemSpawn() {
		chance = rand.nextInt(1000);
		if (chance == 2) {
			handler.addObject(new Drop((int) x + rand.nextInt(256), (int) y + rand.nextInt(256), ID.MissileDrop, game,
					Image.MissileDrop));
		}
		if (chance == 3) {
			handler.addObject(new Drop((int) x + rand.nextInt(256), (int) y + rand.nextInt(256), ID.HealthDrop, game,
					Image.HealthDrop));// health
		}
		if (chance == 4) {
			handler.addObject(new Drop((int) x + rand.nextInt(256), (int) y + rand.nextInt(256), ID.BulletDrop, game,
					Image.BulletDrop));
		}
		if (chance == 5) {
			handler.addObject(new Drop((int) x + rand.nextInt(256), (int) y + rand.nextInt(256), ID.FlameDrop, game,
					Image.FlameDrop));// health
		}
	}

	public void stopAttack(int chance) {
		if (chance < 4 && lazer != null) {
			handler.removeObject(lazer);
			lazer = null;
			stopLazerNoise();
			lastAttack = System.currentTimeMillis();
		}
		if (chance < 10 && tankSpawn) {
			tankSpawn = false;
			lastAttack = System.currentTimeMillis();
		}
		if (chance < 10 && spray) {
			spray = false;
			lastAttack = System.currentTimeMillis();
		}

	}

	public void stopLazer() {
		if (lazer != null) {
			handler.removeObject(lazer);
			stopLazerNoise();
			lazer = null;
		}
	}

	public void death() {
		for (int j = 0; j < 80; j++) {

			handler.addObject(new FlameParticle(x + rand.nextInt(64) - 32, y + rand.nextInt(64) - 32,
					ID.MissileParticle, 100, 100, 0.03f, 0, game));

		}
		stopLazer();
		AudioPlayer.getSound("bossDeath").play(getFreq(), Game.effectVolume);
		handler.removeObject(this);
		if (story) {
			handler.addObject(new AreaFlag(64, 64 * 2, ID.AreaFlag, game, Image.Left, 1, 7));
			for (int l = 0; l < walls.length; l++) {
				handler.removeObject(walls[l]);
			}
			handler.playCoordsx.put(1, Game.WIDTH-200);
			handler.playCoordsy.put(1, Game.HEIGHT-200);
			handler.clearEnemys();
			handler.addObject(new CheckPoint((int) x, (int) y, ID.CheckPoint, game, Image.BossDrop));// ratchet
		}
	}

	// tick method
	public void tick() {
		x += velX / 2;// updates enemy position
		y += velY / 2;
		if (health <= 0) {// when dead
			death();
		}
		if (health < 12500) {
			attackFreq = attackFreq / 2;
		}
		if (health < 3000) {
			attackFreq = 0;
		}
		// Spawning stuff when in sight
		enemyMovment(vision(player));
		if (vision(player)) {

			if (System.currentTimeMillis() - lastAttack > attackFreq) {
				if (game.diff == 1) {// HArd
					chance = rand.nextInt(health / 200);
				} else {// Normal modes
					chance = rand.nextInt(health / 100);
				}

				// Bad stuff Spawn
				lazerAttack();
				bulletAttack();
				tankAttack();
			}
			// Stop bad stuff
			chance = rand.nextInt(1000);
			stopAttack(chance);
			// Good stuff spawn
			itemSpawn();

			x = Game.clamp(x, 7, Game.WIDTH - 48);// contain within the bounds
			y = Game.clamp(y, 7, Game.HEIGHT - 77);
			collision();
			// Animation

		}
		if (velX != 0 || velY != 0) {// when moving
			imageAnimation(images, frameCount);
			hoverNoise();
		}

	}

	public void hoverNoise() {
		if (!AudioPlayer.getSound("hover").playing()) {
			AudioPlayer.getSound("hover").play(getFreq(), Game.effectVolume);
		}

	}

	public void stopHoverNoise() {
		if (AudioPlayer.getSound("hover").playing()) {
			AudioPlayer.getSound("hover").stop();
		}

	}

	public void stopLazerNoise() {
		if (AudioPlayer.getSound("lazer").playing()) {
			AudioPlayer.getSound("lazer").stop();
		}

	}

	// Collision method (runs) within tick
	private void collision() {
		for (int i = 0; i < handler.Objects().size(); i++) {
			GameObject tempObject = handler.Objects().get(i);

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

	// render method
	public void render(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(0.5f));
		g.setColor(Color.BLACK);
		g.fillOval((int) x, (int) y + 200, 256, 80);
		g2d.setComposite(makeTransparent(1f));
		if (story) {
			g.setColor(Color.GRAY);
			g.fillRect(Game.WIDTH / 4 - 34, 5, 676, 30);
			g.setColor(Color.red);
			g.fillRect(Game.WIDTH / 4 - 30, 10, health / 22, 20);
		}
		g.drawImage(image, (int) x, (int) y, 256, 256, null);

		frameCount++;
		if (frameCount > 10) {// once per second
			frameCount = 0;
		}

	}

	// boundrys
	public Rectangle getBounds() {
		return new Rectangle((int) x + 32, (int) y, xBound, yBound);

	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub

	}

}
