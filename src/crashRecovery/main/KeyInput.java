package crashRecovery.main;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import crashRecovery.main.Game.STATE;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	private boolean[] keyDown = new boolean[4];
	private boolean exist;
	Game game;
	private Player player;
	private int posSpeed = 2;
	private int negSpeed = -2;

	public KeyInput(Handler handler, Game game) {
		this.handler = handler;
		this.game = game;

		keyDown[0] = false;
		keyDown[1] = false;
		keyDown[2] = false;
		keyDown[3] = false;

	}

	private double xComponent(double angle) {
		return Math.sin(Math.toRadians(angle));

	}

	private double yComponent(double angle) {

		return Math.cos(Math.toRadians(angle));

	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		player = handler.getPlayer();

		if (key == KeyEvent.VK_W) {
			player.setVelY(negSpeed);
			keyDown[0] = true;
		}
		if (key == KeyEvent.VK_S) {
			player.setVelY(posSpeed);
			keyDown[1] = true;
		}
		if (key == KeyEvent.VK_D) {
			player.setVelX(posSpeed);
			keyDown[2] = true;
		}
		if (key == KeyEvent.VK_A) {
			player.setVelX(negSpeed);
			keyDown[3] = true;
		}
		if (key == KeyEvent.VK_SHIFT) {
			player.dodge();
			
		}
		if (key == KeyEvent.VK_X) {
			game.textbox.clear();
		}
		if(key == KeyEvent.VK_J) {
		
			
		}

		if (key == KeyEvent.VK_SPACE) {
			exist = false;

			if (player.getPunched() == null)
				if (!exist) {
					AudioPlayer.getSound("slap").play();
					handler.addObject(new Punch(player.getX() + 32, player.getY() + 32, player.getDirection(), ID.Punch,
							Color.ORANGE, 0.05f, game));
				}
		}

		if (key == KeyEvent.VK_1) {
			player.setPistol(true);
			player.setMissile(false);
			player.setFlame(false);
		}
		if (key == KeyEvent.VK_2) {
			player.setPistol(false);
			player.setMissile(true);
			player.setFlame(false);
		}
		if (key == KeyEvent.VK_3) {
			player.setPistol(false);
			player.setMissile(false);
			player.setFlame(true);
		}

		if (key == KeyEvent.VK_ESCAPE)

		{

			if (Game.gameState == STATE.Story ) {
				if (Game.paused) {
					Game.gameState = STATE.Story;
					Game.paused = false;
				} else {
					Game.paused = true;
					Game.gameState = STATE.Paused;
					game.getMenu().getBox(0).changeText("Resume");
					game.getMenu().getBox(1).changeText("Help");
					game.getMenu().getBox(2).changeText("Menu");
					game.getMenu().getBox(3).changeText("Quit");
					game.getMenu().survivalMode(false);
				}
			}
			if (Game.gameState == STATE.Survival ) {
				if (Game.paused) {
					Game.gameState = STATE.Survival;
					Game.paused = false;
				} else {
					Game.paused = true;
					Game.gameState = STATE.Paused;
					game.getMenu().getBox(0).changeText("Resume");
					game.getMenu().getBox(1).changeText("Help");
					game.getMenu().getBox(2).changeText("Menu");
					game.getMenu().getBox(3).changeText("Quit");
					game.getMenu().survivalMode(true);
				}
			}

		}
		if (key == KeyEvent.VK_Q && Game.paused )
			System.exit(1);
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < handler.Objects().size(); i++) {
			GameObject tempObject = handler.Objects().get(i);
			if (tempObject.getId() == ID.Player) {
				if (key == KeyEvent.VK_W)
					keyDown[0] = false;// tempObject.setVelY(0);
				if (key == KeyEvent.VK_S)
					keyDown[1] = false; // tempObject.setVelY(0);
				if (key == KeyEvent.VK_D)
					keyDown[2] = false;// tempObject.setVelX(0);
				if (key == KeyEvent.VK_A)
					keyDown[3] = false;// tempObject.setVelX(0);
			
					

				// vertical movement
				if (!keyDown[0] && !keyDown[1])
					tempObject.setVelY(0);
				// horizontal
				if (!keyDown[2] && !keyDown[3])
					tempObject.setVelX(0);
			}
		}
	}
}
