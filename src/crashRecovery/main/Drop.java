/*
 * July 23st 2022
 * Nick Barrie 
 * Bullet ammo drop. Spawned in by basic enemys when killed adds two bullets to ammo
 */
package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Drop extends GameObject {
	// Game class objects
	private Handler handler;
	private Game game;
	private HUD hud;
	//Random
	Random rand = new Random();
	//Image
	private BufferedImage icon;
	SpriteSheet ss;
	//GameObjects
	private Player player;
	private ObjectSpawner spawn;
	//Attraction
	private int gravity = 100000; 
	private float diffX;
	private float diffY;
	private float distance;
	// 0 equals bullet drop
	// 1 equals missile drop
	// 2 equals health drop
	// 3 equals flame drop
	
	//Created by spawner
	public Drop(float x, float y, ID id, Game game,Image image, ObjectSpawner spawn) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		ss =  new SpriteSheet(Game.sprite_Sheet);
		icon = ss.grabImage(image);
		player = handler.getPlayer();
	}
	// Manually spawned
	public Drop(float x, float y, ID id, Game game,Image image) {
		super(x, y, id,game);
		this.handler = game.getHandler();
		this.hud = game.getHud();
		ss =  new SpriteSheet(Game.sprite_Sheet);
		icon = ss.grabImage(image);
		player = handler.getPlayer();
	}
	public void tick() {
		x +=velX;
		y += velY;
		collision();
		moveToPlayer();
	}
	private void moveToPlayer() {
		if(vision(player)) {
		 diffX = x - player.getX();// difference in x
		 diffY = y - player.getY();// difference in y
		distance = (float) Math
				.sqrt((diffX * (diffX) + (diffY) * (diffY)));

		velX = ((-1 / distance) * diffX)*gravity/(distance * distance*64);
		velY = ((-1 / distance) * diffY)*gravity/(distance * distance*64);
		}
	}
	private void collision() {
		
				if (getBounds().intersects(player.getCollision())) {// when hit add type
					AudioPlayer.getSound("ammoPickUp").play(getFreq(),Game.effectVolume);
					if(id == ID.BulletDrop) {
					HUD.pistolAmmo+=4;
					}
					else if(id == ID.MissileDrop) {
						HUD.missileAmmo+=1;
						}
					else if(id == ID.HealthDrop) {
						HUD.HEALTH+=20;
						}
					else if(id == ID.FlameDrop) {
						HUD.flameAmmo+=1;
						}
					handler.removeObject(this);
				
				if(spawn != null) {
					spawn.spawnTick();
				}
				}
			}
	

	public void render(Graphics g) {
		g.drawImage(icon, (int) x , (int) y, 64,64, null);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 64, 64);
	}

	public Line2D getLine(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
	

}
