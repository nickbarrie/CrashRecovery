package crashRecovery.main;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Random;

public class Trail extends GameObject{

	private float alpha = .5f;
	private float life;
	private Handler handler;
	private int sizeX = 64;
	private int sizeY = 64;
	private SpriteSheet ss;
	
	public Trail(float x, float y, ID id,Image image,float life, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		ss = new SpriteSheet(game.sprite_Sheet);
		this.image = ss.grabImage(image);
		this.life = life;
		if(image == Image.Fiery) {
			setDirection();
			sizeX = rand.nextInt(64);
			sizeY = rand.nextInt(64);
			
		}
	}


	public void tick() {
		if(alpha > life) {
			alpha -= life -0.001f;
		}else {
			handler.removeObject(this);
		}
		
	}

	// random direction for particle
		public void setDirection() {
			velX = rand.nextInt(3)-1;
			velY= rand.nextInt(3)-1;
		}
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		g.drawImage(image,(int)x, (int) y, sizeX, sizeY,null);
		g2d.setComposite(makeTransparent(1));
	}


	public Rectangle getBounds() {
		return new Rectangle(0,0,0,0);
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
