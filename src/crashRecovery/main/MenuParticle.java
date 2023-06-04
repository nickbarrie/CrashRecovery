package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Random;

public class MenuParticle extends GameObject {

	private Handler handler;
	Random rand = new Random();
	private Color col;

	public MenuParticle(int x, int y, ID id, Game game) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		velY = 10;
		

	}

	public void tick() {
		y += velY;
		if(y > Game.HEIGHT) {
			y = -100;
			x = rand.nextInt(Game.WIDTH);
		}
		
	}

	public void render(Graphics g) {

		g.setColor(Color.WHITE);
		g.fillRect((int) x, (int) y, 1, 20);
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
