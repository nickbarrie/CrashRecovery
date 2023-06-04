/*
 * July 21st 2022
 * Nick Barrie 
 * particle used for effects
 */
package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BasicParticle extends GameObject{

	private float alpha = 1;// used for life of particle
	private float life;// life left in particle
	private Handler handler;
	private Color color;
	Random rand = new Random();
	private int width, height;
	private boolean shimmer = false;
	private BufferedImage image;
	private SpriteSheet ss;
	private Graphics2D g2d;
	public BasicParticle(float x, float y, ID id,Color color, int width, int height,float life, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.color = color;
		this.width = rand.nextInt(width);
		this.height = width;
		this.life = life;
		setDirection();
		
		if(id == ID.Shimmer) {
			ss = new SpriteSheet(Game.sprite_Sheet);
			image = ss.grabImage(Image.Shimmer);
			shimmer = true;
		}
		if(id == ID.Lazer) {
			ss = new SpriteSheet(Game.sprite_Sheet);	
			image = ss.grabImage(Image.Lazer);
			shimmer = true;
			this.width = width;
			this.height = height;
			
		velX = 0;
		velY = 0;
		}
	}
	public BasicParticle(float x, float y,float velX,float velY, ID id,Color color, int width, int height,float life, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.color = color;
		this.width = rand.nextInt(width);
		this.height = width;
		this.life = life;
		
		if(id == ID.Shimmer) {
			ss = new SpriteSheet(Game.sprite_Sheet);
			image = ss.grabImage(Image.Shimmer);
			shimmer = true;
		}
		if(id == ID.Lazer) {
			ss = new SpriteSheet(Game.sprite_Sheet);
			image = ss.grabImage(Image.Lazer);
			shimmer = true;
			this.width = width;
			this.height = height;
			
		
		}
		this.velX = velX/2;
		this.velY = velY/2;
		this.velX += rand.nextInt(3)-1;
		this.velY += rand.nextInt(3)-1;
		this.velX += rand.nextFloat()-.5;
		this.velY += rand.nextFloat()-.5;
	}
// tick method
	public void tick() {
		if(alpha > life) {
			alpha -= life -0.001f;
		}else {
			handler.removeObject(this);
		}// life time for particle
		x += velX;
		y += velY;
	}
// random direction for particle
	public void setDirection() {
		velX = rand.nextInt(3)-1;
		velY= rand.nextInt(3)-1;
	}
	
	public void render(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		if(shimmer) {
			g.drawImage(image,(int) x,(int)y,width,width, null);
		}else {
			g.setColor(color);
			g.fillRect((int)x, (int)y, width, height);
		}
		
		
		g2d.setComposite(makeTransparent(1));
	}

	
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
	

}
