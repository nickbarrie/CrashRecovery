package crashRecovery.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import crashRecovery.main.Game.STATE;

public class Render implements Runnable {
	private Thread thread;
	private boolean running = false;

	private Game game;
	private Handler handler;
	private HUD hud;
	private TextBoxes textbox;
	Player player;
	private Menu menu;
	private Font font = new Font("Silkscreen", Font.PLAIN, 40);
	Font hFont = new Font("SilkScreen", Font.TRUETYPE_FONT, 30);
	private SpriteSheet ss;
	private BufferedImage vision;

	private String[] credits = { "CRASH RECOVERY", "Code by Nick Barrie", "Sound and Music by Jihwon Seo",
			"Thank you for playing " };

	private int fps = 60;
	private int targetTime = 1000000000 / fps;
	Color back = new Color(30, 40, 80);

	public Render(Game game) {
		this.game = game;
		this.handler = game.getHandler();
		this.hud = game.getHud();
		this.textbox = game.getTextBox();
		this.menu = game.getMenu();
		this.player = handler.getPlayer();
		ss = new SpriteSheet(Game.sprite_Sheet);
		vision = ss.grabImage(Image.Vision);
	}

	public synchronized void start() {
		if(thread == null) {
			thread = new Thread(this);
		}
		thread.start();
		running = true;
	}

	public synchronized void resume() {
		if(thread == null) {
			thread = new Thread(this);
		}
		this.notify();
		running = true;
	}



	

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (running)
				
				render();
				

			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS Render: " + frames);
				frames = 0;
			}
			long totalTime = System.nanoTime() - now;
			if (totalTime < targetTime) {

				try {
					Thread.sleep((targetTime - totalTime) / 1000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		stop();

	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public synchronized void pause() {
		try {
			this.wait();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void blackScreen(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		try {
			
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void render() {
		BufferStrategy bs = game.getBufferStrategy();
		if (bs == null) {
			game.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(back);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
	
		if (Game.gameState == STATE.Story && !Game.paused) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setFont(hFont);

			handler.render(g);
			if (handler.cLvl == 5 || handler.cLvl == 6) {
				g.drawImage(vision, (int) handler.player.getX() - 224, (int) handler.player.getY() - 224, 576, 576,
						null);
			}
			hud.render(g);
			textbox.render(g);

		} else if (Game.gameState == STATE.Survival && !Game.paused) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setFont(hFont);

			handler.render(g);
			hud.render(g);

		}
		else if (Game.gameState == STATE.Menu || Game.gameState == STATE.End || Game.gameState == STATE.Select || Game.gameState == STATE.Help ||Game.gameState == STATE.Mode) {
			handler.render(g);
			menu.render(g);
		} else if (handler.getBlackScreenSatus()) {
			handler.blackScreen(g, 3);
		}
		else if (Game.gameState == STATE.Beat) {
			handler.render(g);
			
			handler.endAnimationTick();
			g.setFont(hFont);
			g.setColor(Color.white);
			for (int i = 0; i < credits.length; i++) {
				g.drawString(credits[i], Game.WIDTH / 2 - 400, 400 + i * 40);
			}
			g.drawString("Time beaten in: "+game.getTimer() / 60 + " minutes & " + game.getTimer() % 60 + " seconds", Game.WIDTH / 2 - 400, 560);
		}
		else if(Game.gameState == STATE.Paused) {
			handler.render(g);
			menu.render(g);
			
		}

	
	
		g.dispose();
		bs.show();
	}

}
