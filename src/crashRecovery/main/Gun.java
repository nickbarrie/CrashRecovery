package crashRecovery.main;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Gun extends GameObject {

	private Handler handler;
	private HUD hud;
	private GameObject player;
	private Image pic;
	private SpriteSheet ss;
	private int imageDirection = 40;
	private int imageOffset;
	private Boolean shotRight;
	public Gun(float x, float y, ID id, Image weapon,Boolean shotRight, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.pic = weapon;
		this.shotRight = shotRight;
		player = handler.getPlayer();
		ss = new SpriteSheet(Game.sprite_Sheet);
		if(shotRight) {
			imageDirection = 40;
			imageOffset = 10;
		}else {
			imageDirection = -40;
			imageOffset = -10;
		}
		
		this.image  = ss.grabImage(weapon);
		
		tickWait = 20;
	}

	public void tick() {
		x = player.getX();
		y = player.getY();
		if(System.currentTimeMillis() - tickWait > 40) {
			tickWait = System.currentTimeMillis();
			if(shotRight) {
			 if(imageDirection  > 30 && !maxGrowth) {
					imageDirection-=6;
				}
			 else if(imageDirection <= 60) {
				 maxGrowth = true;
			 }
			  if(maxGrowth) {
				imageDirection+=4;
			}
			}else {
				 if(imageDirection  <-30 && !maxGrowth) {
						imageDirection+=6;
					}
				 else if(imageDirection >= -60) {
					 maxGrowth = true;
				 }
				  if(maxGrowth) {
					imageDirection-=4;
				}
			}
		}
	
	}
	
	public void render(Graphics g) {
		g.drawImage(image, (int) x+40 + imageOffset, (int) y+8, imageDirection, 40, null);
		
	}
	

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, imageDirection, 64);
	}
	public AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		
		
	}

}
