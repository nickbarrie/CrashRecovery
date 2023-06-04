/*
 * July 21st 2022
 * Nick Barrie 
 * particle used for effects
 */
package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BodyParticle extends GameObject{

	private float alpha = 1;// used for life of particle
	private float life;// life left in particle
	private Handler handler;

	private BufferedImage image;
	private SpriteSheet ss;
	private Graphics2D g2d;
	public BodyParticle(float x, float y, ID id,Image pic, float life, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.life = life;
		
		setDirection();
		ss = new SpriteSheet(Game.sprite_Sheet);
		this.image = ss.grabImage(pic);
		
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
		g.drawImage(image,(int) x, (int) y, 64, 64,null);
		g2d.setComposite(makeTransparent(1));
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
	

}
