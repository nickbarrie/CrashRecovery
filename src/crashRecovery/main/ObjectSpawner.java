package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class ObjectSpawner extends GameObject {
	private int maxObjects;
	private int held;
	private ID spawn;
	private Handler handler;
	private BufferedImage image;
	private HUD hud;
	private Game game;
	private int health = 1000;
	private Random rand = new Random();
	private ArrayList<GameObject> obj = new ArrayList<GameObject>();
	private GameObject temp;
	SpriteSheet ss;
	private int level;

	public ObjectSpawner(float x, float y, ID id, int maxObjects, int held, ID spawn, int level, Game game) {
		super(x, y, id,game);
		this.maxObjects = maxObjects;
		this.held = held;
		this.spawn = spawn;
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.game = game;
		this.level = level;
		ss = new SpriteSheet(Game.sprite_Sheet);
		if (spawn == ID.BasicEnemy || spawn == ID.TankEnemy) {
			image = ss.grabImage(1, 8, 16, 16);// active spawner
		}
		for (int i = 0; i < maxObjects; i++) {
			held--;
			if (spawn == ID.BasicEnemy) {
				temp = new BasicEnemy((int) x + rand.nextInt(256) - 128, (int) y + rand.nextInt(256) - 128,
						ID.BasicEnemy, game, this);

			} else if (spawn == ID.TankEnemy) {
				temp = new TankEnemy((int) x + rand.nextInt(256) - 128, (int) y + rand.nextInt(256) - 128, ID.TankEnemy,
						game, this);
			} else if (spawn == ID.BulletDrop) {
				temp = new Drop((int) x, (int) y, ID.BulletDrop, game,Image.BulletDrop ,this);
			} else if (spawn == ID.MissileDrop) {
				temp = new Drop((int) x, (int) y, ID.MissileDrop, game,Image.MissileDrop, this);
			}
			handler.storeLevel(level, temp);
			obj.add(temp);
		}

	}

	public void tick() {

	}

	public void deathCheck() {
		if (health <= 0) {

			handler.removeObject(this);
			if (spawn == ID.BasicEnemy) {
				
					handler.addObject(new CheckPoint((int) x, (int) y, ID.CheckPoint, game, Image.Drill));// drill
				
			}
		}
	}

	public void adjustHealth(int adjust) {

		health -= adjust;

	}

	public void spawnTick() {
		obj.remove(0);
		if (held > 0 && obj.size() < maxObjects && health>0) {
			held--;
			if (spawn == ID.BasicEnemy) {
				temp = new BasicEnemy((int) x + rand.nextInt(256) - 128, (int) y + rand.nextInt(256) - 128,
						ID.BasicEnemy, game, this);

			} else if (spawn == ID.TankEnemy) {
				temp = new TankEnemy((int) x + rand.nextInt(256) - 128, (int) y + rand.nextInt(256) - 128, ID.TankEnemy,
						game, this);
			} else if (spawn == ID.BulletDrop) {
				temp = new Drop((int) x, (int) y, ID.BulletDrop, game,Image.BulletDrop ,this);
			} else if (spawn == ID.MissileDrop) {
				temp = new Drop((int) x, (int) y, ID.MissileDrop, game,Image.MissileDrop, this);
				;
			}
			handler.addObject(temp);
			obj.add(temp);
		}
		if (held == 0) {
			if (spawn == ID.BasicEnemy || spawn == ID.TankEnemy) {
				image = ss.grabImage(2, 8, 16, 16);// inactive spawner
			} else {
				handler.removeObject(this);
			}
		}
	}

	public boolean itemSpawn() {
		if (spawn != ID.BasicEnemy || spawn != ID.TankEnemy) {
			return true;
		}
		return false;
	}

	public void render(Graphics g) {
		g.drawImage(image, (int) x, (int) y, 64,64, null);
	}

	public Rectangle getBounds() {
		if (spawn == ID.BasicEnemy || spawn == ID.TankEnemy) {
			return new Rectangle((int) x, (int) y, 64, 64);
		} else {
			return new Rectangle(-1, -1, 1, 1);
		}
	}

	public Line2D getLine(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean vision() {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}

}
