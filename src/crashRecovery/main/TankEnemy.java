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

public class TankEnemy extends GameObject {

	private Handler handler;
	private HUD hud;
	private Game game;
	private Player player;
	Random rand = new Random();
	private BufferedImage enemyImage;
	SpriteSheet ss;
	private int ani = 0;
	private int frame = 0;
	private int r;
	private boolean motion = false;
	private ArrayList<BufferedImage> images = new ArrayList<>();
	private int frameCount = 0;
	private Graphics2D g2d;

	private ObjectSpawner spawn;
	public TankEnemy(int x, int y, ID id, Game game, ObjectSpawner spawn) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.spawn = spawn;
		this.game = game;
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.TankIdle);
		player = handler.getPlayer();
		images.add(ss.grabImage(Image.Tank));
		images.add(ss.grabImage(Image.Tank2));
		damageFlash = ss.grabImage(Image.TankHit);
	}
	public void sawSounds() {
		if(!AudioPlayer.getSound("saw").playing()) {
			AudioPlayer.getSound("saw").play(getFreq(),Game.effectVolume);
		}
		
	}
	public void stopSawSounds() {
		if(AudioPlayer.getSound("saw").playing()) {
			AudioPlayer.getSound("saw").stop();
		}
		
	}
	public void deathAni() {
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.TankHead,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.TankBody,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.TankCog,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.TankCog,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.TankBlade,0.02f,game));
		handler.addObject(new BodyParticle(x,y,ID.Particle,Image.TankBlade,0.02f,game));
		
	
	}
	public void death() {
		deathAni();
		handler.removeObject(this);
		hud.kill();
		AudioPlayer.getSound("bossDeath").play(getFreq(),Game.effectVolume);
		stopSawSounds();
		if(rand.nextInt(5) == 2) {
			handler.addObject(new Drop(x, y, ID.MissileDrop, game ,Image.MissileDrop));
		}
		if(rand.nextInt(5) == 4) {
			handler.addObject(new Drop(x, y, ID.BulletDrop, game ,Image.BulletDrop));
		}
		if(rand.nextInt(5) == 3) {
			handler.addObject(new Drop(x, y, ID.HealthDrop, game ,Image.HealthDrop));
		}
			if(spawn != null) {
				spawn.spawnTick();
			}
	}
	public void movement() {
		float diffX = x - player.getX() - 1;
		float diffY = y - player.getY() - 1;
		float distance = (float) Math
				.sqrt((x - player.getX()) * (x - player.getX()) + (y - player.getY()) * (y - player.getY()));

		velX = ((-2 / distance) * diffX);
		velY = ((-2 / distance) * diffY);
		motion = true;
	}
	public void tick() {
		if(outOfBounds(Game.WIDTH,Game.HEIGHT)) {
			handler.removeObject(this);
			stopSawSounds();
		}
		if(game.diff == 1) {// HARD
			x += velX*1.4;
			y += velY*1.4;
		}else {// Other Modes
			x += velX;
			y += velY;
		}
		
		if (vision() && !motion) {
			movement();
			
		}
		if (velX == 0 && velY == 0) {
			motion = false;
			enemyImage = ss.grabImage(Image.TankIdle);
			stopSawSounds();
		}else {
			sawSounds();
			enemyImage = ss.grabImage(Image.Tank);
		}
		
	
		collision();
		if (health <= 0) {
		death();
		}
		if (velX != 0 || velY != 0) {
			imageAnimation(images,frameCount);
			
		}
		else {
			image = ss.grabImage(Image.TankIdle);
		}
	}
	private void collision() {
		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);
				if (getBounds().intersects(tempObject.getBounds())) {
					if (tempObject.getId() == ID.Wall) {// Collision with wall
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

	public void render(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(0.5f));
		g.setColor(Color.BLACK);
		g.fillOval((int)x-4, (int)y+50,70, 20);
		g2d.setComposite(makeTransparent(1f));
		g.drawImage(image, (int) x , (int) y, 64,64, null);
		drawHealthBar(g);
		frameCount++;
		if (frameCount > 10) {// once per second
			frameCount = 0;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);

	}

	public boolean vision() {

		for (int i = 0; i <  handler.getCollisionObjects().size(); i++) {
			GameObject tempWall = handler.getCollisionObjects().get(i);
			if (tempWall.getId() == ID.Wall) {
				Line2D line = player.getLine(x+32, y+32);// get line from player

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
