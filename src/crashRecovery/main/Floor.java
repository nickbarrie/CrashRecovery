package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Floor extends GameObject {
	private Image image;
	private Handler handler;;
	private BufferedImage floor;
	private SpriteSheet ss;
	private int level;
	private int width;
	private int height;

	public Floor(int x, int y, int width, int height, ID id, Image image, int level, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.image = image;
		this.level = level;
		this.width = width;
		this.height = height;
		ss = new SpriteSheet(Game.sprite_Sheet);
		floor = ss.grabImage(image);

	}



	public void tick() {

	}

	public void render(Graphics g) {
		for (int i = 0; i < width + 64; i += 64) {
			for (int j = 0; j < height + 64; j += 64) {
				g.drawImage(floor, (int) x + i, (int) y + j, 64, 64, null);
		 }
		 }
//		g.drawImage(floor, (int) x, (int) y, 64, 64, null);
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
