package crashRecovery.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public abstract class GameObject {

	protected float x, y;
	protected ID id;
	protected float velX, velY;
	protected boolean air;
	protected boolean down;
	protected Player player;
	protected int xBound = 64;
	protected int yBound = 64;
	protected int xCollision;
	protected int yCollision;
	protected long lastShimmerSpawn = 0;
	protected Game game;
	protected Handler handler;
	protected BufferedImage image;
	protected int health = 64;
	protected int selector = 0;
	protected BufferedImage damageFlash;
	protected Random rand = new Random();
	protected int xParticleOffset;
	protected int yParticleOffset;
	protected long lastHit = System.currentTimeMillis();
	protected long immunityLength = 250;
	protected long tickWait = 40;
	protected float freq;
	protected Graphics2D g2d;
	protected boolean maxGrowth = false;
	public GameObject(float x, float y, ID id, Game game) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.game = game;
		handler = game.getHandler();
		player = handler.getPlayer();

	}

	public void knockBack(GameObject obj) {
	//	if (System.currentTimeMillis() - lastHit > immunityLength) {
		setVelX((int) (obj.getVelX()*10));
		setVelY((int) (obj.getVelY()*10));
		//}
	}

	public abstract void tick();

	public abstract void updateObject(int level);
	public float getFreq() {
		freq = 0.7f + rand.nextFloat();
		return freq;
	}
	public void drawHealthBar(Graphics g) {
		g2d = (Graphics2D) g;
		if (System.currentTimeMillis() - lastHit-300 < immunityLength) {
			
			
		g.setColor(Color.BLACK);
		g.fillRect((int)x-1, (int)y+66, 66, 12);
		g.setColor(Color.RED);
		g2d.setComposite(makeTransparent(0.8f));
		g.fillRect((int)x, (int)y+67,(int) health, 10);
		g2d.setComposite(makeTransparent(1));
		}
	}
	protected AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
	public void adjustHealth(int damage) {
		damage = rand.nextInt(damage/2)+damage/2;
		if (System.currentTimeMillis() - lastHit > immunityLength) {
		lastHit = System.currentTimeMillis();
		health -= damage;
		image = damageFlash;
		}
	}

	public abstract void render(Graphics g);

	public Rectangle getCollision() {
			return new Rectangle((int) x, (int) y, xBound, yBound);
	}

	public abstract Rectangle getBounds();

	public void shimmer(int width, int height) {
		if (System.currentTimeMillis() - lastShimmerSpawn > 500) {
			lastShimmerSpawn = System.currentTimeMillis();
			xParticleOffset = rand.nextInt(width) - width / 2;
			yParticleOffset = rand.nextInt(height) - height / 2;
			handler.addObject(new BasicParticle(x + width / 2 + xParticleOffset, y + height / 2 + yParticleOffset,
					ID.Shimmer, Color.WHITE, 48, 48, 0.05f, game));
		}

	}

	public Line2D getLine(float Ex, float Ey) {

		Line2D line = new Line2D.Float(x + 16, y + 32, Ex, Ey);
		return line;
	}

	public boolean vision(GameObject target) {

		for (int j = 0; j < handler.Objects().size(); j++) {
			GameObject tempWall = handler.Objects().get(j);
			if (tempWall.getId() == ID.Wall) {
				Line2D line = target.getLine(x+xBound/2, y+yBound/2);// get line from player

				if (line.intersects(tempWall.getBounds())) {// if line intersects wall player cant be seen
					return false;
				}
			}

		}

		return true;
	}

	public void imageAnimation(ArrayList<BufferedImage> images, int frameCount) {
		if (frameCount == 0) {
			image = images.get(selector);
			selector++;
			if (selector >= images.size()) {
				selector = 0;
			}
		}

	}
	public void explode() {
	for (int j = 0; j < 3*Game.particleLevel; j++) {
			handler.addObject(
					new FlameParticle(x+xBound/2, y+yBound/2, ID.MissileParticle, 100, 100, 0.03f, 0, game));
		}
		AudioPlayer.getSound("explosion").play(getFreq(),1);
	}
	public void enemyMovment(boolean see) {
		if (see) {// when the player is in sight

			float diffX = x + 32 - player.getX();// difference in x
			float diffY = y + 32 - player.getY();// difference in y
			float distance = (float) Math.sqrt((diffX) * (diffX) + (diffY) * (diffY));

			velX = ((-1 / distance) * diffX);
			velY = ((-1 / distance) * diffY);

		} else {

			int move = rand.nextInt(100);// when the player cant be seen random movements will be performed
			if (move == 1) {
				velY = (rand.nextInt(2));
				velX = (rand.nextInt(2));
			}
			if (move == 2) {
				velY = (-rand.nextInt(2));
				velX = (-rand.nextInt(2));
			}
			if (move > 98) {
				velX = 0;
				velY = 0;
			}
			if (move == 3) {
				velX = 0;
			}
			if (move == 4) {
				velY = 0;
			}

		}
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public ID getId() {
		return id;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}

	public float getVelX() {
		return velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setWall(boolean state) {
		this.air = state;
	}

	public boolean getWall() {
		return this.air;
	}

	public boolean outOfBounds(int xMax, int yMax) {
		if (x < 0 || x > xMax) {
			return true;
		}
		if (y < 0 || y > yMax) {
			return true;
		}
		return false;
	}

}
