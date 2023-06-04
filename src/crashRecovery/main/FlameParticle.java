package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class FlameParticle extends GameObject{

	private float alpha = 1;
	private float life;
	private Handler handler;
	private int width, height;
	private SpriteSheet ss;
	// 0 for missile 
	// 1 for flamethrower
	
	public FlameParticle(float x, float y, ID id, int width, int height,float life, int type, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.width = width;
		this.height = height;
		this.life = life;
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.Fiery);
		setDirection();
		
		this.width = rand.nextInt(width);
		this.height = rand.nextInt(height);
		//x -= width;
	}


	public void tick() {
		if(alpha > life) {
			alpha -= life;
		}else {
			handler.removeObject(this);
		}
		x += velX;
		y += velY;
		
	}

	public void setDirection() {
		float r = rand.nextFloat();
		if(id == ID.MissileParticle) {
			velX = (rand.nextInt(7)-4) *r;
			velY= (rand.nextInt(7)-4) *r;
		}else {
			velX = (rand.nextInt(5)-2) *r;
			velY= (rand.nextInt(5)-2) *r;
		}
		
	}
	
	public void render(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		g.drawImage(image,(int)x,(int) y,width,height,null);
		g2d.setComposite(makeTransparent(1));
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
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
