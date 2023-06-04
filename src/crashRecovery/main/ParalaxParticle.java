package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class ParalaxParticle extends GameObject {

	private Handler handler;
	private Color col;
	private SpriteSheet ss;
	private int size = rand.nextInt(64);
	private boolean twinkle = false;
	private Graphics2D g2d;
	private long twinkleTime = 6000;
	private float shade = 0.4f;

	public ParalaxParticle(int x, int y, ID id, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		velY = rand.nextInt(1) + 1;
		ss = new SpriteSheet(Game.sprite_Sheet);
		image = ss.grabImage(Image.Shimmer);
	}

	public void tick() {
		y += velY;
		if (y > Game.HEIGHT) {
			y = -100;
			x = rand.nextInt(Game.WIDTH);
		}
		if (rand.nextInt(200) == 1) {
			twinkle = true;
			twinkleTime = System.currentTimeMillis();

		}
		
		if (System.currentTimeMillis() - twinkleTime > rand.nextInt(1000) + 1000) {
			twinkle = false;
		}
		
		
		
		if (twinkle && shade < 0.78) {

			shade += 0.02;
		} 
		if(!twinkle && shade > 0.42){
			shade -= 0.02;
		}
	}

	public void render(Graphics g) {
		g2d = (Graphics2D) g;
	
		g2d.setComposite(makeTransparent(shade));
		g.drawImage(image, (int) x, (int) y, size, size, null);

		g2d.setComposite(makeTransparent(1f));
	}



	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);

	}

	public boolean vision() {
		return false;
	}

	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub

	}
}
