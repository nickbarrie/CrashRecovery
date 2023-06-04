/*
 * July 23st 2022
 * Nick Barrie 
 * checkpoint which when in contact with allows progress to next stage
 */
package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import crashRecovery.main.Game.STATE;

public class CheckPoint extends GameObject {

	private Handler handler;
	private HUD hud;
	private Game game;

	private Player player;
	private boolean checkPoint;
	private BufferedImage flag;
	private SpriteSheet ss;
	private Image image;
	private int frame = 0;;
	private int xbound = 64;
	private int ybound = 64;
	private int ani = 0;
	private boolean shimmer = false;
	public CheckPoint(int x, int y, ID id, Game game, Image image, boolean checkPoint) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.image = image;
		this.game = game;
		this.checkPoint = checkPoint;
		player = handler.getPlayer();
		ss = new SpriteSheet(Game.sprite_Sheet);
		flag = ss.grabImage(image);

		if (image == Image.CrashedShip) {
			xbound = 192;
			ybound = 256;
		}
		if (image == Image.Cone || image == Image.Battery || image == Image.Drill || image == Image.BossDrop) {
			shimmer = true;
		}
	}

	public CheckPoint(int x, int y, ID id, Game game, Image image) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.game = game;
		this.checkPoint = true;
		this.image = image;
		player = handler.getPlayer();
		ss = new SpriteSheet(Game.sprite_Sheet);
		flag = ss.grabImage(image);

		if (this.image == Image.CrashedShip) {
			xbound = 192;
			ybound = 256;
		}
		if (image == Image.Cone || image == Image.Battery || image == Image.Drill || image == Image.BossDrop) {
			shimmer = true;
		}
	}

	public void tick() {
		if(shimmer) {
			shimmer(xbound,ybound);
		}
		frame++;
		if (frame > 40) {
			frame = 0;
			ani++;
		}
		collision();

	}
	public void pointSounds() {
		if(!AudioPlayer.getSound("itemPickUp").playing() ) {
			AudioPlayer.getSound("itemPickUp").play(getFreq(),Game.effectVolume);
		}
		
	}
	private void collision() {

		if (getBounds().intersects(player.getCollision()) && checkPoint) {
			
			if (image == Image.CrashedShip && HUD.HEALTH != 100) {
				HUD.HEALTH = 100;
				pointSounds();
			}
			// Nose cone
			if (image == Image.Cone) {
				
				player.setCheck(true, 2);
				handler.storeLevel(1, new AreaFlag(400, 20, ID.AreaFlag, game, Image.Up, 2, 1));// up arrow
				game.textbox.text(1);// display area 2 text
				handler.removeObject(this);
				// Wing
				pointSounds();
			}
			if (image == Image.Wing) {
				player.setCheck(true, 3);
				hud.setLevel(3);
				handler.storeLevel(1, new AreaFlag(1164, Game.HEIGHT - 196, ID.AreaFlag, game, Image.DownRight, 3, 1));// tail
				handler.removeObject(this);
				pointSounds();
			}
			// Rocket launcher
			if (image == Image.Launcher) {
				hud.setLauncher(true);
				handler.removeObject(this);
				AudioPlayer.getSound("weaponPickUp").play();
			} // Battery Dropped Level 1
			if (image == Image.Battery) {// battery
				game.textbox.text(2);
				player.setCheck(true, 0);
				handler.storeLevel(2, new AreaFlag(500, Game.HEIGHT - 120, ID.AreaFlag, game, Image.Down, 1, 2));// down
				pointSounds();																				// arrow
				handler.removeObject(this);

			} // Ship Level 1
			if (image == Image.CrashedShip && player.getCheck(0)) {// if ship and player has battery
				game.textbox.text(3);
				hud.setLevel(1);
				player.setCheck(false, 0);
				handler.addObject(new AreaFlag(10, Game.HEIGHT / 2 - 128, ID.AreaFlag, game, Image.Left, 4, 1));// left
				pointSounds();																				// arrow

			} // Ship Level 2
			if (image == Image.CrashedShip && player.getCheck(1)) {// if ship and player has drill
				game.textbox.text(5);
				hud.setLevel(2);
				player.setCheck(false, 1);
				handler.addObject(new Switch(x, y, ID.Switch, 0, game));
				pointSounds();
			}
			if (image == Image.CrashedShip && player.getCheck(5)) {// if ship and player has bossDrop
			Game.gameState = STATE.Beat;
			pointSounds();
			handler.endAnimation();
			}// Drill Dropped level 2
			if (image == Image.Drill) {
				player.setCheck(true, 1);
				game.textbox.text(4);
				handler.storeLevel(4,
						new AreaFlag(Game.WIDTH - 80, Game.HEIGHT / 2, ID.AreaFlag, game, Image.Right, 1, 2));// right
																												// arrow
				handler.removeObject(this);

			}
			if (image == Image.FlameThrower) {
				hud.setFlame(true);
				handler.removeObject(this);
				AudioPlayer.getSound("weaponPickUp").play();
			}
			if (image == Image.Card) {
				player.setCheck(true, 4);
				handler.removeObject(this);
				game.textbox.text(6);
				pointSounds();
			}
			if (image == Image.BossDrop) {
				player.setCheck(true, 5);
				game.textbox.text(7);
				handler.removeObject(this);
				pointSounds();
			}
		}

	}

	public void render(Graphics g) {
		if (xbound == 64 && ybound == 64) {
			g.drawImage(flag, (int) x, (int) y, 64, 64, null);
		} else {
			g.drawImage(flag, (int) x, (int) y, 192, 256, null);
		}

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, xbound, ybound);

	}

	public Image getImage() {
		return image;
	}

	public Line2D getLine(float x, float y) {
		return null;
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
	


}
