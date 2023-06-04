package crashRecovery.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import crashRecovery.main.Game.STATE;

public class HUD {

	public static float HEALTH = 100;
	public static int dodge = 100;
	public static boolean dodging = false;
	public static int pistolAmmo;
	public static int missileAmmo;
	public static int flameAmmo;
	public static int lastLevel = 1;// level play button will load
	boolean drawn;
	private Font font = new Font("Silkscreen", Font.TRUETYPE_FONT, 50);
	private int score = 0;
	private static int level = 0;
	public static boolean haslauncher = false;
	public static boolean hasflame = false;
	public static int kills = 0;
	private int playerx = 0;
	private int playery = 0;
	private Game game;
	private Player player;
	SpriteSheet ss = new SpriteSheet(Game.sprite_Sheet);
	private BufferedImage launcher = ss.grabImage(2, 5, 16, 16);
	private BufferedImage pistol = ss.grabImage(8, 4, 16, 16);
	private BufferedImage heart = ss.grabImage(5, 4, 16, 16);
	private BufferedImage flame = ss.grabImage(2, 7, 16, 16);
	private BufferedImage healthBar = ss.grabImage(Image.HealthBar);
	private BufferedImage dodgeBar = ss.grabImage(Image.DodgeBar);
	
	public HUD(Game game) {
		this.game = game;
		player = game.getHandler().getPlayer();
	}
	public void tick() {
		pistolAmmo = (int) Game.clamp(pistolAmmo, 0, 200);
		missileAmmo = (int) Game.clamp(missileAmmo, 0, 200);
		flameAmmo = (int) Game.clamp(flameAmmo, 0, 200);
		HEALTH = (int) Game.clamp(HEALTH, 0, 100);
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// Health Bar
		g2d.setComposite(makeTransparent(0.8f));
		g.drawImage(healthBar,15,10,256,128,null);
		g.setColor(Color.RED);
		g.fillRect(103, 34, (int) (HEALTH*1.44), 24);
		//Dodge Bar
		g.drawImage(dodgeBar,15,90,256,128,null);
		g.setColor(Color.CYAN);
		g.fillRect(103, 114, (int) (dodge*1.44), 24);
		
		//Top Right HUD
		g2d.setComposite(makeTransparent(0.6f));
		g.setColor(Color.DARK_GRAY);
		g.fillRect(Game.WIDTH-350, 15, 330, 100);
		g.setColor(Color.BLACK);
		g.drawRect(Game.WIDTH-350, 15, 330, 100);
		g2d.setComposite(makeTransparent(1));
		
		//Recharge dodge by frame rate
		if(!dodging) {
			dodge+=2;
		}
		dodge = (int) Game.clamp(dodge, 0, 100);
		
		g.setFont(font);
		g.setColor(Color.white);
		if(Game.gameState == STATE.Story) {
		g.drawString("Level: " + (level + 1), Game.WIDTH - 350 , 50);
		g.drawString("Time: " + game.getTimer(), Game.WIDTH-350 , 110);
		g.setFont(font);
		}
		if(Game.gameState == STATE.Survival) {
			g.drawString("Wave: " + (kills/10 + 1), Game.WIDTH - 350 , 50);
			g.drawString("Score: " + kills, Game.WIDTH-350 , 110);
			g.setFont(font);
			}
	
		if(Game.pistol){
			g2d.setComposite(makeTransparent(0.6f));
			g.setColor(Color.DARK_GRAY);
			g.fillRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			g.setColor(Color.black);
			g.drawRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			
			g2d.setComposite(makeTransparent(1));
		
			g.drawImage(pistol, Game.WIDTH-100, Game.HEIGHT-120, 64,64, null);
			g2d.setComposite(makeTransparent(0.8f));
			if(haslauncher) {
			g.drawImage(launcher, Game.WIDTH-60, Game.HEIGHT-80, 32,32, null);
			}
			g2d.setComposite(makeTransparent(1));
			g.setColor(Color.white);
			g.drawString(Integer.toString(pistolAmmo), Game.WIDTH-175 , Game.HEIGHT-60);
		}
		else if(Game.missile && haslauncher) {
			g2d.setComposite(makeTransparent(0.6f));
			g.setColor(Color.DARK_GRAY);
			g.fillRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			g.setColor(Color.black);
			g.drawRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			
			g2d.setComposite(makeTransparent(1));
			g.drawImage(launcher, Game.WIDTH-100, Game.HEIGHT-130, 64,64, null);
			g2d.setComposite(makeTransparent(0.8f));
			g.drawImage(pistol, Game.WIDTH-120, Game.HEIGHT-80, 32,32, null);
			if(hasflame) {
			
			g.drawImage(flame, Game.WIDTH-60, Game.HEIGHT-80, 32,32, null);
			}
			g2d.setComposite(makeTransparent(1));
			g.setColor(Color.white);
			g.drawString(Integer.toString(missileAmmo), Game.WIDTH-175 , Game.HEIGHT-60);
		}
		else if(Game.flame && hasflame){
			g2d.setComposite(makeTransparent(0.6f));
			g.setColor(Color.DARK_GRAY);
			g.fillRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			g.setColor(Color.black);
			g.drawRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			
			g2d.setComposite(makeTransparent(1));
			g.drawRect(Game.WIDTH- 180, Game.HEIGHT-105, 160, 60);
			g.drawImage(flame, Game.WIDTH-100, Game.HEIGHT-130, 64,64,null);
			g2d.setComposite(makeTransparent(0.8f));
			g.drawImage(launcher, Game.WIDTH-120, Game.HEIGHT-80, 32,32, null);
		
			g2d.setComposite(makeTransparent(1));
			g.setColor(Color.white);
			g.drawString(Integer.toString(flameAmmo), Game.WIDTH-175 , Game.HEIGHT-60);
		}
		
	}
	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
	public void score(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean getLauncher() {
		return haslauncher;
	}
	public void setLauncher(boolean state) {
		haslauncher = state;
	}
	public void kill() {
		if(Game.gameState == STATE.Survival)
		game.getSpawn().checkNewWave();
		kills++;
	}

	public int getKills() {
		return kills;
	}

	public boolean getFlame() {
		return hasflame;
	}
	public void setFlame(boolean state) {
		hasflame = state;
	}
	public void setPlayerCoords(int x, int y) {
		playerx = x;
		playery = y;
				
	}
	public int getPlayerX() {
		return playerx;
	}
	public int getPlayerY() {
		return playery;
	}
}
