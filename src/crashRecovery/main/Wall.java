package crashRecovery.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {

	private Handler handler;
	private int width;
	private int height;
	private BufferedImage wall;
	private int image;
	private boolean shadowBelow = true;
	private boolean shadowRight = true;
	private Graphics2D g2d;
	private Image imageType;
	public Wall(int x, int y, ID id, int width, int height, int level, int image, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.width = width;
		this.height = height;
		health = 1000;
		this.image = image;
		for (int i = 0; i < width / 64; i++) {

			handler.storeLevel(level, new Wall(x + 64 * i, y, id, this.image, game));
		}
		for (int i = 0; i < height / 64; i++) {

			handler.storeLevel(level, new Wall(x, y + i * 64, id, this.image, game));

		}
	}

	public Wall(int x, int y, ID id, int image, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.image = image;
		this.id = id;
		health = 1000;
		SpriteSheet ss = new SpriteSheet(Game.sprite_Sheet);

		if (image == 0) {
			wall = ss.grabImage(1, 4, 16, 16);
		}
		if (image == 1) {
			wall = ss.grabImage(8, 3, 16, 16);
		}
		if (image == 2) {
			wall = ss.grabImage(11, 4, 16, 16);
			imageType = Image.DarkBrick;
		}
		if (image == 3) {
			wall = ss.grabImage(Image.BrownWall);
			imageType = Image.BrownWall;
		}

	}

	public int getImage() {
		return image;
	}

	public void tick() {
	
	}

	public void deathCheck() {
		if (health <= 0) {
			handler.removeObject(this);
		}
	}

	public void adjustHealth(int adjust) {
		health -= adjust;
	}

	public void render(Graphics g) {
		g.drawImage(wall, (int) x, (int) y, 64, 64, null);
		if(imageType != Image.DarkBrick) {
		
		if (shadowBelow) {
			shadowBelow(g);
		}
		if (shadowRight) {
			shadowRight(g);
		}
		
		}
	}

	public void shadowBelow(Graphics g) {
		g2d = (Graphics2D) g;
		g.setColor(Color.black);
		float shade = 0.8f;
		for (int i = 0; i < 40; i++) {
			g2d.setComposite(makeTransparent(shade));
			g.fillRect((int) x, (int) y + 64 + i, 64, 1);
			shade -= 0.02f;
		}
		g2d.setComposite(makeTransparent(1));
	}

	public void shadowRight(Graphics g) {
		g2d = (Graphics2D) g;
		g.setColor(Color.black);
		float shade = 0.8f;
		for (int i = 0; i < 40; i++) {
			g2d.setComposite(makeTransparent(shade));
			g.fillRect((int) x + 64 + i, (int) y, 1, 64);
			shade -= 0.02f;
		}

		g2d.setComposite(makeTransparent(1));
	}


	public void checkShadowRight(Wall temp) {
		int tempX = (int) (x+96);
		int tempY = (int) (y+32);
		if (temp.getBounds().contains(tempX, tempY)) {
			shadowRight = false;
		}
	}

	public void checkShadowBelow(Wall temp) {
		int tempX = (int) (x+32);
		int tempY = (int) (y+96);
		if (temp.getBounds().contains(tempX, tempY)) {
			shadowBelow = false;
		}
	}

	public void overlappingWall(Wall wall,int level) {
		Rectangle rect = new Rectangle((int) x+32, (int) y+32, 1,1);
		if (wall.getBounds().intersects(rect)) {
			handler.removeObject(this,level);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);
	}

	public int getType() {
		return image;
	}

	public boolean vision() {
		return false;
	}

	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		for (int i = 0; i < handler.areaObjects.get(level).size(); i++) {
			GameObject tempObject = handler.areaObjects.get(level).get(i);

			if (tempObject.getId() == ID.Wall && tempObject != this) {
				Wall temp = (Wall) tempObject;
				overlappingWall(temp,level);

			}

		}
		for (int i = 0; i < handler.areaObjects.get(level).size(); i++) {
			GameObject tempObject = handler.areaObjects.get(level).get(i);

			if (tempObject.getId() == ID.Wall && tempObject != this) {
				Wall temp = (Wall) tempObject;
				shadowRight = true;
				shadowBelow = true;
				checkShadowRight(temp);
				checkShadowBelow(temp);

			}

		}
	
		
		
		
		
		
	}
}