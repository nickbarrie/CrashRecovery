
/*
 * July 17th 2022
 * Nick Barrie 
 * Area Flag is a point where a different map area can be loaded using similar code to the checkpoint
 * All Code was written long before July 17th 2022 but comments started then
 */
package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class AreaFlag extends GameObject {
	// Game class objects
	private Handler handler;
	private HUD hud;
	private Player player;
	private Game game;
	// image
	private BufferedImage flag;
	private SpriteSheet ss;
	private Image image; // represents which image is shown
	private int ani = 0;// animation frame ticker
	private int frame = 0;// frame timer
	private int level = 0;// which area it is located
	// triggers
	private boolean enable = true;// when true area flag will function
	private boolean intentionally = false;
	// attributes
	private Rectangle bounds;
	

	public AreaFlag(int x, int y, ID id, Game game, Image image, int level, int from, boolean enable) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.level = level;
		this.enable = enable;
		this.game = game;
		this.image = image;
		player = handler.getPlayer();
		ss = new SpriteSheet(Game.sprite_Sheet);
		flag = ss.grabImage(image);
		xBound = 64;
		yBound = 64;
		bounds = new Rectangle(x,y,64,64);
		if(image == Image.Up) {
			bounds = new Rectangle(x-128,y-64,256,64);
		}
		if(image == Image.Down) {
			bounds = new Rectangle(x-128,y+64,256,64);
		}
		if(image == Image.Left) {
			bounds = new Rectangle(x-64,y-128,64,256);
		}
		if(image == Image.Right) {
			bounds = new Rectangle(x+64,y-128,64,256);
		}
		if(image == Image.CaveClosed) {
			yBound = 192;
			xBound = 192;
			bounds = new Rectangle(x,y,192,192);
		}
	}
	public AreaFlag(int x, int y, ID id, Game game, Image image, int level, int from) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.level = level;
		this.game = game;
		this.image = image;
		player = handler.getPlayer();
		ss = new SpriteSheet(Game.sprite_Sheet);
		flag = ss.grabImage(image);
		if(image == Image.CaveClosed) {
			yBound = 192;
			xBound = 192;
		}
		bounds = new Rectangle(x,y,64,64);
		if(image == Image.Up) {
			bounds = new Rectangle(x-128,y-64,256,64);
		}
		if(image == Image.Down) {
			bounds = new Rectangle(x-128,y+64,256,64);
		}
		if(image == Image.Left) {
			bounds = new Rectangle(x-64,y-128,64,256);
		}
		if(image == Image.Right) {
			bounds = new Rectangle(x+64,y-128,64,256);
		}

	}
	// tick method
	public void tick() {
		frame++;
		if (frame > 40) {// time taken for animation to change
			frame = 0;
			ani++;
		}
		
		if (health <= 0) {
			enable = true;
			flag = ss.grabImage(Image.CaveOpen);// open cave
		}
		collision();
	}

	// collision method
	private void collision() {
		// Player touching 
		if (getBounds().intersects(player.getBounds()) && intentionally) {
			
			if (enable) {
				hud.lastLevel = level;
				int tempX = (int) player.getX();
				int tempY = (int) player.getY();
					if(image == Image.Up) {
						tempX = (int) player.getX();
						tempY = (int) player.getY()+64;
					}
					if(image == Image.Down) {
						tempX = (int) player.getX();
						tempY = (int) player.getY()-64;
					}
					if(image == Image.Left) {
						tempX = (int) player.getX()+64;
						tempY = (int) player.getY();
					}
					if(image == Image.Right) {
						tempX = (int) player.getX()-64;
						tempY = (int) player.getY();
					}
					player.setX(handler.playCoordsx.get(level));
					player.setY(handler.playCoordsy.get(level));
					handler.playCoordsx.put(handler.cLvl, tempX);
					handler.playCoordsy.put(handler.cLvl, tempY);

			
				handler.loadArea(level);// load area
				intentionally = false;
				if(image == Image.Wing && handler.cLvl == 1) {
					
					flag = ss.grabImage(Image.DownRight);
				}
				if(image == Image.Wing && handler.cLvl == 3) {
					
					flag = ss.grabImage(Image.UpLeft);
				}
			}
		}
		if (!getBounds().intersects(player.getBounds())) {
			intentionally = true;
		}
		//Cave being opened by lazer
		if (image == Image.CaveClosed) {
			for (int i = 0; i < handler.Objects().size(); i++) {
				GameObject tempObject = handler.Objects().get(i);
				if (tempObject.getId() == ID.Lazer) {
					
					Lazer lazer = (Lazer) tempObject;
					Line2D line = lazer.getLine();// get line from lazer
					if (line.intersects(getBounds())) {
						adjustHealth(2);
						
					}
				}

			}
		}
		if (image == Image.RockLadder) {
			if (getBounds().intersects(player.getBounds())){
				game.textbox.text(8);// display rock text
			}
		}
		// Ladder being revealed by explosion
		if (image == Image.RockLadder) {
			for (int i = 0; i < handler.Objects().size(); i++) {
				GameObject tempObject = handler.Objects().get(i);
				if (getBounds().intersects(tempObject.getBounds())){
				if (tempObject.getId() == ID.Missile) {
					Missile missile = (Missile) tempObject;
					missile.missileHit();
					enable = true;
					flag = ss.grabImage(12, 5, 16, 16);//ladder
				
					
				}
				}
			}
		}

	}

	public void adjustHealth(int damage) {
		health = health - damage;
	}

	// rendering method
	public void render(Graphics g) {
		if (image != Image.CaveClosed && image != Image.CaveOpen) {
			g.drawImage(flag, (int) x, (int) y, 64,64, null);
		} else {
			g.drawImage(flag, (int) x, (int) y, 192,192, null);
		}
	}

	// Boundary method
	public Rectangle getBounds() {
		return bounds;

	}


	// enable usage
	public void enableFlag() {
		enable = true;
	}
	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}


}
