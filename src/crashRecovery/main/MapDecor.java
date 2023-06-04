package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class MapDecor extends GameObject {

	private Handler handler;
	private HUD hud;
	private Player player;
	private BufferedImage decor;
	private BufferedImage decor2;
	private Image image;
	private int ani = 0;
	private int frame = 0;;
	private int imageSizeX = 64;
	private int imageSizeY = 64;
	private SpriteSheet ss;
	private long timeMade = System.currentTimeMillis();
	private int shotTimer = 5000;
	private int timesShot = 0;
	private boolean shoot = true;
	public MapDecor(int x, int y, ID id, Game game, Image image) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.image = image;
		ss = new SpriteSheet(Game.sprite_Sheet);

		player = handler.getPlayer();
		decor = ss.grabImage(image);

		if (image == Image.Rock1 || image == Image.Rock2  || image == Image.Rock3 || image == Image.Rock4) {
			imageSizeX = rand.nextInt(32) + 32;
			imageSizeY =imageSizeX;
		}
		if (image == Image.Crater) {
			imageSizeX = rand.nextInt(192) - 96;
			imageSizeY = imageSizeX;
		}
		if (image == Image.Ship) {
			imageSizeX = 192;
			imageSizeY = 256;
			xBound = 192;
			yBound = 256;
			velX= 1;
		}

	}
	public MapDecor(int x, int y, ID id, Game game, Image image,boolean shoot) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.image = image;
		ss = new SpriteSheet(Game.sprite_Sheet);
		this.shoot = shoot;
		player = handler.getPlayer();
		decor = ss.grabImage(image);

		if (image == Image.Rock1 || image == Image.Rock2  || image == Image.Rock3 || image == Image.Rock4) {
			imageSizeX = rand.nextInt(32) + 32;
			imageSizeY =imageSizeX;
		}
		if (image == Image.Crater) {
			imageSizeX = rand.nextInt(192) - 96;
			imageSizeY = imageSizeX;
		}
		if (image == Image.Ship) {
			imageSizeX = 192;
			imageSizeY = 256;
			xBound = 192;
			yBound = 256;
			velX= 1;
		}

	}
	public void tick() {
		frame++;
		if (frame > 40) {
			frame = 0;
			ani++;
		}
		x += velX;
		y += velY;
		floorDecorCheck();
		if(image == Image.Torch) {
			torchAnimation();
		}
		if(image == Image.Ship) {
			if(x>600 || x < 500) {
				velX = -velX;
			}
			if(shoot) {
			if(System.currentTimeMillis() - timeMade > shotTimer) {
				timeMade = System.currentTimeMillis();
				shotTimer = rand.nextInt(500);
				timesShot ++;
				explode();
			}
			if(timesShot == 15) {
				handler.removeObject(this);
				handler.addObject( new ColorFilter(0, 0, Game.WIDTH, Game.HEIGHT, ID.Filter, game, Color.BLACK,1f));
				try {
					Thread.sleep(2000,0);
					handler.loadArea(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		}
	}
	public void torchAnimation() {
		if(rand.nextInt(100) == 1) {
			decor = ss.grabImage(Image.Torch);
		}if(rand.nextInt(100) == 2) {
			decor = ss.grabImage(Image.Torch2);
		}
	}
	public void render(Graphics g) {

		g.drawImage(decor, (int) x, (int) y, imageSizeX, imageSizeY, null);
		if(image == Image.Torch) {
			light(g);
		}
	}
	
	public void light(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		float shade = 0.1f;
		g.setColor(Color.yellow);
		for (int i = 0; i < 16; i++) {
			g2d.setComposite(makeTransparent(shade));
			g.fillOval((int)x + (32-i*4), (int)y-16+(32-i*4), i*8, i*8);
			
			shade -= 0.005f;
		}
		
	
		g2d.setComposite(makeTransparent(1));
	}
	public void floorDecorCheck() {
		if(id == ID.FloorDecor) {
			for (int i = 0; i < handler.Objects().size(); i++) {
				GameObject tempObject = handler.Objects().get(i);
				if (getBounds().intersects(tempObject.getBounds())) {
					if (tempObject.getId() == ID.Wall || tempObject.getId() == ID.TileFloor) {
						handler.removeObject(this);
					}
				}
			}
			}
	}
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);
	}

	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		
		
	}

}
