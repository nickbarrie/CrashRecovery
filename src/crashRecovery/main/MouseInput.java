package crashRecovery.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import crashRecovery.main.Game.STATE;

public class MouseInput extends MouseAdapter {

	private Handler handler;
	private Game game;
	private HUD hud;
	private int x, y;
	private Player player;
	private boolean exist = false;
	private boolean pressed = false;

	public MouseInput(Handler handler, Game game, HUD hud) {
		this.handler = handler;
		this.game = game;
		this.hud = hud;

	}

	public void mousePressed(MouseEvent e) {
		if (Game.gameState == STATE.Story || Game.gameState == STATE.Survival) {
			int mx = e.getX();
			int my = e.getY();
			pressed = true;
			
			 player = handler.getPlayer();
			if (player != null && !Game.paused) {
				if (HUD.pistolAmmo > 0 && player.getPistol()) {
					
					
					HUD.pistolAmmo = HUD.pistolAmmo - Game.ammoUse;
					if(mx > player.getX()) {
						player.shoot(Image.Gun,true);
						handler.addObject(new Bullet(player.getX() + 64, player.getY() + 32, mx, my, ID.Bullet,Image.PlayerBullet, game));
					}else {
						player.shoot(Image.Gun,false);
						handler.addObject(new Bullet(player.getX() - 20, player.getY() + 32, mx, my, ID.Bullet,Image.PlayerBullet, game));
					}
					
					
				} 
				if (HUD.missileAmmo > 0 && player.getMissile() && hud.getLauncher() == true) {
					HUD.missileAmmo = HUD.missileAmmo - Game.ammoUse;
					
					if(mx > player.getX()) {
						player.shoot(Image.Launcher,true);
						handler.addObject(new Missile(player.getX() + 64, player.getY() + 48, mx, my, ID.Missile, game));
					}else {
						player.shoot(Image.Launcher,false);
						handler.addObject(new Missile(player.getX() -28 , player.getY() + 48, mx, my, ID.Missile, game));
					}
				
				}
				if (HUD.flameAmmo > 0 && player.getFlame() && hud.getFlame() == true) {
					 exist = false;
					for (int j = 0; j < handler.Objects().size(); j++) {
						if (handler.Objects().get(j).getId() == ID.Flamethrower) {
							exist = true;
						}
					}
						if(!exist) {
							if(mx > player.getX()) {
								player.shoot(Image.FlameThrower,true);
							}else {
								player.shoot(Image.FlameThrower,false);
							}
							HUD.flameAmmo = HUD.flameAmmo - Game.ammoUse ;
						handler.addObject(new Flamethrower(player.getX() + 32, player.getY() + 32, mx, my,
								ID.Flamethrower, game));
						
						
						}
					
				}
			}
		}
	}
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if (mx > x && mx < x + width) {
			if (my > y && my < y + height) {
				return true;
			}
		}
		return false;
	}

	public void mouseMotion(MouseEvent e) {
		if (Game.gameState == STATE.Story) {
			int mx = e.getX();
			int my = e.getY();
			
		}
	}
}