package crashRecovery.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Flamethrower extends GameObject {

	private float alpha = 1;
	private float life;
	private Handler handler;
	private static int bulletSpeed = 4;
	private long lastFlame = System.currentTimeMillis();
	private long lastDamage = System.currentTimeMillis();
	public Flamethrower(float x, float y, int mx, int my, ID id, Game game) {
		super(x, y, id, game);
		this.handler = game.getHandler();
		this.life = 0.015f;
		double angle = Math.atan2(mx - x, my - y);
		velX = (float) (bulletSpeed * Math.sin(angle));
		velY = (float) (bulletSpeed * Math.cos(angle));
		AudioPlayer.getSound("flamethrower").play(getFreq(),Game.effectVolume);
	}

	public void tick() {
		if (alpha > life) {
			alpha -= life;
		} else {
			handler.removeObject(this);
		}
		x += velX;
		y += velY;
		collision();
		if(Game.particleLevel != 0) {
		if (System.currentTimeMillis() - lastFlame > 10/Game.particleLevel) {
			if (!getBounds().intersects(handler.getPlayer().getBounds())) {
				handler.addObject(new FlameParticle(x, y, ID.Flamethrower, 30, 30, 0.03f, 1, game));
				handler.addObject(new Trail(x, y, ID.Trail, Image.Fiery, 0.01f, game));
				lastFlame = System.currentTimeMillis();
			}
		}
		}
		if (System.currentTimeMillis() - lastDamage > 100) {
			if (!getBounds().intersects(handler.getPlayer().getBounds())) {
				handler.addObject( new DamageArea(x-16,y-16,ID.Flamethrower,32,32,0.03f,0,game));
				lastDamage = System.currentTimeMillis();
			}
		}

	}

	public void render(Graphics g) {
	}

	private void collision() {
		for (int i = 0; i < handler.Objects().size(); i++) {

			GameObject tempObject = handler.Objects().get(i);

			if (getBounds().intersects(tempObject.getBounds())) {
				if (tempObject.getId() == ID.Wall) {// Collision with wall
					Wall platform = (Wall) tempObject;
					if (platform.getType() == 0) {
						velX = 0;
						velY = 0;
					}
				}

			}
		}
	}


	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 20, 20);
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
