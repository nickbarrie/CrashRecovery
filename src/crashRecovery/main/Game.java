package crashRecovery.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -57310827855302706L;

	public static final int WIDTH = 1280, HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean running = false;
	private Render render;
	public static boolean paused = false;
	public int diff = 0;
	private int gameTimer = 0;
	private Handler handler;
	private HUD hud;
	private Spawn spawn;
	public TextBoxes textbox;
	public static boolean pistol = false;
	public static boolean missile = false;
	public static boolean flame = false;
	public static float effectVolume = 1;
	public static float musicVolume = 1;
	public static int particleLevel = 5;
	public static int ammoUse = 1;
	private static Menu menu;
	private MouseInput mouseIn;
	Color back = new Color(10, 20, 50);
	Random rand = new Random();
	private int tick = 2000;
	private int targetTime = 1000000000 / tick;

	public enum STATE {
		Menu, Story, Help, End, Select, Beat, Mode, Survival, Paused;
	};

	public static STATE gameState = STATE.Menu;

	public static BufferedImage sprite_Sheet = null;

	public Game() {
		BufferedImageLoader loader = new BufferedImageLoader();

		sprite_Sheet = loader.loadImage("/res/small_sprite.png");

		handler = new Handler(this);
		hud = new HUD(this);
		spawn = new Spawn(handler, hud, this);
		textbox = new TextBoxes();
		menu = new Menu(this, handler, hud);
		render = new Render(this);
		this.addKeyListener(new KeyInput(handler, this));
		this.addMouseListener(menu);
		this.addMouseListener(new MouseInput(handler, this, hud));

		AudioPlayer.load();

		new Window(WIDTH, HEIGHT, "Crash Recovery", this);

		SpriteSheet ss = new SpriteSheet(Game.sprite_Sheet);

		if (gameState == STATE.Story) {

		}
		if (STATE.Menu == gameState) {
			for (int i = 0; i < 6 * particleLevel; i++) {
				handler.storeLevel(0,
						new MenuParticle(rand.nextInt(Game.WIDTH), rand.nextInt(Game.HEIGHT), ID.MenuParticle, this));
			}
			for (int i = 0; i < 10 * particleLevel; i++) {
				handler.storeLevel(0, new ParalaxParticle(rand.nextInt(Game.WIDTH), rand.nextInt(Game.HEIGHT),
						ID.MenuParticle, this));
			}
			handler.loadArea(0);
		}

	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		render.start();
		running = true;
	}

	public Thread getThread() {
		return thread;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		this.requestFocus();
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
			while (delta >= 1) {
				
				tick();
				
				delta--;

			}
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				if (!paused && gameState == STATE.Story) {
					gameTimer++;
				}
				//System.out.println("Tick: " + frames);
				frames = 0;
			}

		}
		stop();

	}

	private void tick() {
	
		if (gameState == STATE.Story) {
			if (!paused) {
				handler.tick();
				hud.tick();
				textbox.tick();
				if (handler.cLvl == 5 || handler.cLvl == 6) {
					handler.visibleObjectsRendered();
				}
				if (HUD.HEALTH <= 0) {
					HUD.HEALTH = 100;
					HUD.missileAmmo = 3;
					HUD.pistolAmmo = 12;
					HUD.flameAmmo = 4;
					hud.setLevel(0);
					gameState = STATE.End;
					handler.loadArea(0);
				

				}
			}

		} else if (gameState == STATE.Survival) {
			if (!paused) {
				handler.tick();
				hud.tick();
				spawn.tickSurvival();
				if (HUD.HEALTH <= 0) {
					HUD.HEALTH = 100;
					HUD.missileAmmo = 3;
					HUD.pistolAmmo = 12;
					HUD.flameAmmo = 4;
					hud.setLevel(0);
					gameState = STATE.End;
					handler.loadArea(0);
					
			
			
				}
			} else {
				menu.tick();
				handler.tick();
			}
		} else if (gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select
				|| gameState == STATE.Help || gameState == STATE.Mode) {
			handler.tick();
			menu.tick();
		} else if (gameState == STATE.Beat) {

			handler.tick();

		} else if (gameState == STATE.Paused) {

			menu.tick();

		}
		musicVolume = clamp(musicVolume, 0, 1);
		effectVolume = clamp(effectVolume, 0, 1);
		particleLevel = (int) clamp(particleLevel, 0, 100);
	}

	public Render getRender() {
		return render;
	}

	public void setPause(boolean in) {
		paused = in;
	}

	public static float clamp(float var, float min, float max) {
		if (var >= max) {
			return var = max;
		} else if (var <= min) {
			return var = min;
		} else
			return var;
	}

	public Handler getHandler() {
		return handler;
	}

	public HUD getHud() {
		return hud;
	}

	public Menu getMenu() {
		return menu;
	}

	public Spawn getSpawn() {
		return spawn;
	}

	public TextBoxes getTextBox() {
		return textbox;
	}

	public boolean getPaused() {
		return paused;
	}

	public int getTimer() {
		return gameTimer;
	}

	public static void main(String args[]) {
		new Game();
	}
}
