package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import crashRecovery.main.Game.STATE;

public class Player extends GameObject {

	Handler handler;
	Game game;
	int ani = 0;
	int frame = 0;
	private BufferedImage player_image;
	private boolean shooting = false;
	private boolean shotRight;
	private Image weapon;
	private long timeShot;
	Punch punch;
	int speed = 0;
	private SpriteSheet ss;
	private long timeDodged;
	private long animationTime = System.currentTimeMillis();
	private int animationGap = 200;
	private int lastDir;

	private int damage;
	private int hit = 0;
	private boolean battery = false;
	private boolean wrench = false;
	private boolean cone = false;
	private boolean wing = false;
	private boolean card = false;
	private boolean bossDrop = false;
	private int bound = 30;
	private Gun gun;
	private Graphics2D g2d;

	public Player(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, id, game);
		this.handler = handler;
		this.game = game;
		ss = new SpriteSheet(Game.sprite_Sheet);
		player_image = ss.grabImage(1, 1, 16, 16);
		if (Game.gameState == STATE.Story) {
			if (game.diff == 0) {// easy
				HUD.missileAmmo = 3;
				HUD.pistolAmmo = 8;
				HUD.flameAmmo = 6;
				hit = 1;
			}
			if (game.diff == 1) {// hard
				HUD.missileAmmo = 1;
				HUD.pistolAmmo = 4;
				HUD.flameAmmo = 4;
				hit = 2;
			}
			if (game.diff == 2) {// cheats
				HUD.missileAmmo = 200;
				HUD.pistolAmmo = 200;
				HUD.flameAmmo = 200;
				HUD.haslauncher = true;
				HUD.hasflame = true;
				hit = 0;
			}
		}
		if (Game.gameState == STATE.Survival) {
			if (game.diff == 0) {// easy
				HUD.missileAmmo = 3;
				HUD.pistolAmmo = 8;
				HUD.flameAmmo = 6;
				HUD.haslauncher = true;
				HUD.hasflame = true;
				hit = 1;
			}
			if (game.diff == 1) {// hard
				HUD.missileAmmo = 1;
				HUD.pistolAmmo = 4;
				HUD.flameAmmo = 4;
				HUD.haslauncher = true;
				HUD.hasflame = true;
				hit = 2;
			}
			if (game.diff == 2) {// cheats
				HUD.missileAmmo = 200;
				HUD.pistolAmmo = 200;
				HUD.flameAmmo = 200;
				HUD.haslauncher = true;
				HUD.hasflame = true;
				hit = 0;
			}
		}

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x + bound / 2, (int) y + bound / 2, bound, bound);
	}

	public Rectangle getCollision() {
		return new Rectangle((int) x + 20, (int) y + 10, 40, 50);
	}

	public boolean getCheck(int checkpoint) {
		if (checkpoint == 0) {
			return battery;
		}
		if (checkpoint == 1) {
			return wrench;

		}
		if (checkpoint == 2) {
			return cone;
		}
		if (checkpoint == 3) {
			return wing;
		}
		if (checkpoint == 4) {
			return card;
		}
		if (checkpoint == 5) {
			return bossDrop;
		} else {
			return false;
		}
	}

	public void setCheck(boolean point, int checkpoint) {// set state and which image
		if (checkpoint == 0) {
			battery = point;
		}
		if (checkpoint == 1) {
			wrench = point;

		}
		if (checkpoint == 2) {
			cone = point;

		}
		if (checkpoint == 3) {
			wing = point;

		}
		if (checkpoint == 4) {
			card = point;

		}
		if (checkpoint == 5) {
			bossDrop = point;
		}
	}

	public void punchImage() {
		if (lastDir >= 1 && lastDir <= 3) {
			player_image = ss.grabImage(Image.PlayerPunchRight);
		}
		if (lastDir > 4) {
			player_image = ss.grabImage(Image.PlayerPunchLeft);
		}
		if (lastDir == 0) {
			player_image = ss.grabImage(Image.PlayerUp1);
		}
		if (lastDir == 4) {
			player_image = ss.grabImage(Image.PlayerDown2);
		}
	}

	public void tick() {
		x += velX;
		y += velY;

		x = Game.clamp(x, -32, Game.WIDTH - 32);
		y = Game.clamp(y, -32, Game.HEIGHT - 64);
		collision();
		endDodge();
		endShot();
		footSteps();
		// if(!shooting) {
		if (velX != 0 || velY != 0) {
			upWalk();
			downWalk();
			rightWalk();
			leftWalk();
		} else {
			idle();
		}
		// }
		if (punch != null) {
			punchImage();
		}
		if (HUD.dodging) {
			dodgeAnimation();
		}
		if (y > Game.HEIGHT) {
			HUD.HEALTH -= 5;
		}
		direction();
		if (System.currentTimeMillis() - animationTime > animationGap) {
			animationTime = System.currentTimeMillis();
			ani++;
		}

	}

	public void idle() {
		if (ani % 4 == 0) {
			player_image = ss.grabImage(Image.PlayerIdle1);

		} else if (ani % 4 == 2) {
			player_image = ss.grabImage(Image.PlayerIdle2);
		}
	}

	public void punched(Punch punched) {
		punch = punched;
	}

	public Punch getPunched() {
		return punch;
	}

	public void damage(int delt) {
		damage += delt;
	}

	public int getDamage() {
		return damage;
	}

	public void damageSounds() {
		if (!AudioPlayer.getSound("hurt").playing()) {
			AudioPlayer.getSound("hurt").play(getFreq(), Game.effectVolume);
		}

	}

	public void footSteps() {
		if (velX != 0 || velY != 0) {
			if (ani % 4 == 0) {
				handler.addObject(new BasicParticle(x + 32, y + 64, ID.Particle, Color.DARK_GRAY, 6, 6, 0.1f, game));// blood
			}
			if (ani % 4 == 1) {
				handler.addObject(new BasicParticle(x + 48, y + 64, ID.Particle, Color.DARK_GRAY, 6, 6, 0.1f, game));// blood
			}
			if (ani % 4 == 2) {
				handler.addObject(new BasicParticle(x + 32, y + 64, ID.Particle, Color.DARK_GRAY, 6, 6, 0.1f, game));// blood
			}
			if (ani % 4 == 3) {
				handler.addObject(new BasicParticle(x + 48, y + 64, ID.Particle, Color.DARK_GRAY, 6, 6, 0.1f, game));// blood
			}

		}

	}

	private void collision() {
		for (int i = 0; i < handler.getCollisionObjects().size(); i++) {
			GameObject tempObject = handler.getCollisionObjects().get(i);
			if (getCollision().intersects(tempObject.getBounds())) {
				if (tempObject.getId() == ID.Wall) {// collsion with wall
					if (velX > 0) {
						x -= 5;
					}
					if (velX < 0) {
						x += 5;
					}
					if (velY > 0) {
						y -= 5;
					}
					if (velY < 0) {
						y += 5;
					}

					double tempVel = Math.sqrt(Math.pow(velY, 2) + Math.pow(velX, 2));
					if (tempVel > 0) {// create particles when hitting the wall
						for (int j = (int) (tempVel / 3); j > 0; j--) {
							handler.addObject(
									new BasicParticle(x + 32, y + 30, ID.Particle, Color.DARK_GRAY, 4, 4, 0.1f, game));
						}
					}

				}

				else if (tempObject.getId() == ID.BasicEnemy) {// Collision with enemy

					HUD.HEALTH -= hit;
					damageSounds();
					handler.addObject(new BasicParticle(x + 16, y + 30, ID.Particle, Color.RED, 3, 3, 0.1f, game));

				} else if (tempObject.getId() == ID.TankEnemy) {// Collision with enemy

					HUD.HEALTH -= hit;
					damageSounds();
					handler.addObject(new BasicParticle(x + 16, y + 30, ID.Particle, Color.RED, 3, 3, 0.1f, game));

				} else if (tempObject.getId() == ID.BossEnemy) {// Collision with enemy

					// player is on platform
					HUD.HEALTH -= hit;
					damageSounds();
					handler.addObject(new BasicParticle(x + 16, y + 30, ID.Particle, Color.RED, 3, 3, 0.1f, game));

				} else if (tempObject.getId() == ID.Ray) {
					damageSounds();
					HUD.HEALTH -= hit;
				}

				else if (tempObject.getId() == ID.BossBullet) {

					HUD.HEALTH -= hit;
				}
			}
		}
	}

	public int direction() {
		if (velY < 0 && velX == 0) {
			lastDir = 0;
			return 0;
		} else if (velY < 0 && velX > 0) {
			lastDir = 1;
			return 1;
		} else if (velY == 0 && velX > 0) {
			lastDir = 2;
			return 2;
		} else if (velY > 0 && velX > 0) {
			lastDir = 3;
			return 3;
		} else if (velY > 0 && velX == 0) {
			lastDir = 4;
			return 4;
		} else if (velY > 0 && velX < 0) {
			lastDir = 5;
			return 5;
		} else if (velY == 0 && velX < 0) {
			lastDir = 6;
			return 6;
		} else if (velY < 0 && velX < 0) {
			lastDir = 7;
			return 7;
		}
		return lastDir;
	}

	public void setMovement(int speed) {
		if (lastDir == 0) {
			velX = 0;
			velY = -speed;
		} else if (lastDir == 1) {
			velX = speed;
			velY = -speed;
		} else if (lastDir == 2) {
			velX = speed;
			velY = 0;
		} else if (lastDir == 3) {
			velX = speed;
			velY = speed;
		} else if (lastDir == 4) {
			velX = 0;
			velY = speed;
		} else if (lastDir == 5) {
			velX = -speed;
			velY = speed;
		} else if (lastDir == 6) {
			velX = -speed;
			velY = 0;
		} else if (lastDir == 7) {
			velX = -speed;
			velY = -speed;
		}
	}

	public void setDirection(int dir) {
		if (dir == 0) {
			velX = 0;
			velY = -speed;
		} else if (dir == 1) {
			velX = speed;
			velY = -speed;
		} else if (dir == 2) {
			velX = speed;
			velY = 0;
		} else if (dir == 3) {
			velX = speed;
			velY = speed;
		} else if (dir == 4) {
			velX = 0;
			velY = speed;
		} else if (dir == 5) {
			velX = -speed;
			velY = speed;
		} else if (dir == 6) {
			velX = -speed;
			velY = 0;
		} else if (dir == 7) {
			velX = -speed;
			velY = -speed;
		}
	}

	public void dodge() {
		if (HUD.dodge >= 100) {
			HUD.dodge = 0;
			AudioPlayer.getSound("dodge").play(getFreq(), Game.effectVolume);
			timeDodged = System.currentTimeMillis();
			HUD.dodging = true;
			setMovement(4);
			bound = 0;

		}
	}

	private void dodgeAnimation() {
		if (System.currentTimeMillis() - timeDodged > 0) {
			if (velY > 0) {
				player_image = ss.grabImage(Image.RollOneD);
			}
			if (velY < 0) {
				player_image = ss.grabImage(Image.RollOneU);
			}
			if (velX > 0) {
				player_image = ss.grabImage(Image.RollOneR);
			}
			if (velX < 0) {
				player_image = ss.grabImage(Image.RollOneL);
			}

		}
		if (System.currentTimeMillis() - timeDodged > 125) {
			if (velY > 0) {
				player_image = ss.grabImage(Image.RollTwoD);
			}
			if (velY < 0) {
				player_image = ss.grabImage(Image.RollTwoU);
			}
			if (velX > 0) {
				player_image = ss.grabImage(Image.RollTwoR);
			}
			if (velX < 0) {
				player_image = ss.grabImage(Image.RollTwoL);
			}
		}
		if (System.currentTimeMillis() - timeDodged > 250) {
			if (velY > 0) {
				player_image = ss.grabImage(Image.RollThreeD);
			}
			if (velY < 0) {
				player_image = ss.grabImage(Image.RollThreeU);
			}
			if (velX > 0) {
				player_image = ss.grabImage(Image.RollThreeR);
			}
			if (velX < 0) {
				player_image = ss.grabImage(Image.RollThreeL);
			}
		}
		if (System.currentTimeMillis() - timeDodged > 375) {
			if (velY > 0) {
				player_image = ss.grabImage(Image.RollFourD);
			}
			if (velY < 0) {
				player_image = ss.grabImage(Image.RollFourU);
			}
			if (velX > 0) {
				player_image = ss.grabImage(Image.RollFourR);
			}
			if (velX < 0) {
				player_image = ss.grabImage(Image.RollFourL);
			}
		}
	}

	public void endDodge() {
		if (System.currentTimeMillis() - timeDodged > 500 && HUD.dodging) {
			HUD.dodging = false;
			setMovement(2);
			bound = 40;
		}
	}

	public void endShot() {
		if (System.currentTimeMillis() - timeShot > 500 && shooting) {
			shooting = false;
			handler.removeObject(gun);
		}

	}

	public void shoot(Image weapon, boolean right) {
		handler.removeObject(gun);
		this.weapon = weapon;
		if (right) {
			player_image = ss.grabImage(Image.PlayerRight1);
			shotRight = true;
		} else {
			player_image = ss.grabImage(Image.PlayerLeft1);
			shotRight = false;
		}
		shooting = true;
		timeShot = System.currentTimeMillis();
		gun = new Gun(x, y, ID.Gun, weapon, shotRight, game);
		handler.addObject(gun);
	}

	public void render(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(0.5f));
		g.setColor(Color.BLACK);
		g.fillOval((int) x + 20, (int) y + 50, 40, 20);
		g2d.setComposite(makeTransparent(1f));
		g.drawImage(player_image, (int) x, (int) y, 64, 64, null);

	}

	public boolean rightWalk() {
		if (ani % 4 == 0 && velX > 0) {
			player_image = ss.grabImage(Image.PlayerRight1);
			return true;
		} else if (ani % 4 == 1 && velX > 0) {
			player_image = ss.grabImage(Image.PlayerRight2);
			return true;
		} else if (ani % 4 == 2 && velX > 0) {
			player_image = ss.grabImage(Image.PlayerRight3);
			return true;
		} else if (ani % 4 == 3 && velX > 0) {
			player_image = ss.grabImage(Image.PlayerRight4);
			return true;
		} else {
			return false;
		}
	}

	public boolean leftWalk() {
		if (ani % 4 == 0 && velX < 0) {
			player_image = ss.grabImage(Image.PlayerLeft1);
			return true;
		} else if (ani % 4 == 1 && velX < 0) {
			player_image = ss.grabImage(Image.PlayerLeft2);
			return true;
		} else if (ani % 4 == 2 && velX < 0) {
			player_image = ss.grabImage(Image.PlayerLeft3);
			return true;
		} else if (ani % 4 == 3 && velX < 0) {
			player_image = ss.grabImage(Image.PlayerLeft4);
			return true;
		} else {
			return false;
		}
	}

	public boolean upWalk() {
		if (ani % 2 == 0 && velY < 0) {
			player_image = ss.grabImage(Image.PlayerUp1);
			return true;
		} else if (ani % 2 == 1 && velY < 0) {
			player_image = ss.grabImage(Image.PlayerUp2);
			return true;
		} else {
			return false;
		}

	}

	public boolean downWalk() {
		if (ani % 2 == 0 && velY > 0) {
			player_image = ss.grabImage(Image.PlayerDown1);
			return true;
		} else if (ani % 2 == 1 && velY > 0) {
			player_image = ss.grabImage(Image.PlayerDown2);
			return true;
		} else {
			return false;
		}

	}

	public boolean sight(GameObject temp) {

		Rectangle vision = new Rectangle((int) x - 160, (int) y - 160, 448, 448);

		if (temp.getBounds().intersects(vision)) {
			return true;
		}
		if (temp.getId() == ID.Player) {
			return true;
		}
		return false;
	}

	public boolean vision() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPistol(boolean state) {
		Game.pistol = state;
	}

	public void setMissile(boolean state) {
		Game.missile = state;
	}

	public boolean getMissile() {
		return Game.missile;
	}

	public int getDirection() {
		return lastDir;
	}

	public boolean getPistol() {
		return Game.pistol;
	}

	public void setFlame(boolean state) {
		Game.flame = state;
	}

	public boolean getFlame() {
		return Game.flame;
	}

	public void updateObject(int level) {
		// TODO Auto-generated method stub

	}

}
