package crashRecovery.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.event.MouseInputAdapter;

import crashRecovery.main.Game.STATE;

public class Menu extends MouseInputAdapter {

	private Game game;
	private Handler handler;
	private HUD hud;
	private Font font = new Font("Silkscreen", Font.PLAIN, 40);
	private Font smallfont = new Font("Silkscreen", Font.PLAIN, 20);
	Font hFont = new Font("SilkScreen", Font.TRUETYPE_FONT, 30);

	private Color box = Color.DARK_GRAY;
	Random rand = new Random();
	private BufferedImage ship;
	private BufferedImage title;
	private BufferedImage mode;
	private BufferedImage plaque;
	private BufferedImage helpImage;
	private BufferedImage story;
	private BufferedImage survival;

	private BufferedImage battery;
	private BufferedImage drill;
	private BufferedImage cone;
	private BufferedImage wing;
	private BufferedImage items;
	private BufferedImage card;
	private BufferedImage pause;
	private BufferedImage icon;
	private BufferedImage icon2;
	private BufferedImage wrench;
	private BufferedImage item;
	
	private boolean showHelp = false;
	private boolean survivalMode = false;
	
	private String one = 
			"Welcome to crash recovery. The goal is to repair the ship and leave the planet. "
			+ "Collect ship parts and fight enemies. WASD will allow movement SPACE will punch in"
			+ "the direction of movement. Use 1 2 and 3 to choose a weapon but it is up to you to"
			+ " collect more weapons for your defence. The mouse will aim your weapons. See a weapon icon below." ;

	private String two = "The robots on this planet have valuable resources and tools which can be "
			+"collected by killing robots. Using ammo efficiently is the key. Spawners "
			+"will be disabled after enough enemies are killed. See the spawner below. ";
	
	private String three =  "The ships weapon system can be activated by pressing space. "
			+"Watch out for danger or things that can not be seen. "
			+"Enemies may have dropped important items around the map";
	private String boss =  "Defeat the robot boss. "
			+ "Dodge enemy attacks and use items dropped to help defeat the giant. " 
			+ "Hiding may be helpful but be careful when entering vision again.";
	
	private String killLvl;
	private int x, y;
	private int mx, my;
	private int velX, velY;
	private int tick = 0;
	private Spawn spawner;
	private Player player;
	private SpriteSheet ss;
	private Box top, mid1, mid2, bottom; // main menu buttons
	private Box effectUp, effectDown, musicUp, musicDown, particleUp,particleDown; // main menu buttons
	private int buttonX = 20;
	private int buttonY = 20;
	private int soundButtonX = 1000;
	private int soundButtonY = 20;

	public Menu(Game game, Handler handler, HUD hud) {
		this.game = game;
		this.handler = handler;
		this.hud = hud;
		
		ss = new SpriteSheet(Game.sprite_Sheet);
		battery = ss.grabImage(7, 4, 16, 16);// battery
		drill = ss.grabImage(9, 4, 16, 16);// drill
		cone = ss.grabImage(1, 5, 16, 16);// cone
		wing = ss.grabImage(1, 7, 16, 16);// tailwing
		items = ss.grabImage(4, 9, 32, 16);// items
		pause = ss.grabImage(6, 9, 32, 16);// paused
		card = ss.grabImage(9, 5, 16, 16);// keycard
		wrench = ss.grabImage(Image.BossDrop);
		top = new Box(buttonX, buttonY, 300, 100, "Play", Color.gray, Color.white, font);
		mid1 = new Box(buttonX, buttonY+110, 300, 100, "Help", Color.gray, Color.white, font);
		mid2 = new Box(buttonX, buttonY+220, 300, 100, "Settings", Color.gray, Color.white, font);
		bottom = new Box(buttonX, buttonY+330, 300, 100, "Quit", Color.gray, Color.white, font);
		effectUp = new Box(soundButtonX+200, soundButtonY, 100, 100, "+", Color.gray, Color.white, font);
		effectDown = new Box(soundButtonX, soundButtonY, 100, 100, "-", Color.gray, Color.white, font);
		musicUp = new Box(soundButtonX+200, soundButtonY+100, 100, 100, "+", Color.gray, Color.white, font);
		musicDown = new Box(soundButtonX, soundButtonY+100, 100, 100, "-", Color.gray, Color.white, font);
		particleUp = new Box(soundButtonX+200, soundButtonY+200, 100, 100, "+", Color.gray, Color.white, font);
		particleDown = new Box(soundButtonX, soundButtonY+200, 100, 100, "-", Color.gray, Color.white, font);
		mode = ss.grabImage(Image.EasyMode);
		plaque = ss.grabImage(Image.Plaque);
		helpImage = ss.grabImage(Image.Help);
		story = ss.grabImage(Image.Book);
		survival = ss.grabImage(Image.Fire);
		spawner = game.getSpawn();
		x = 170;
		y = 300;
		game.addMouseMotionListener(this);
		player = handler.getPlayer();
		title = ss.grabImage(10, 6, 112, 48);
		ship = ss.grabImage(6, 5, 48, 64);
		velX = 2;
		velY = 1;
	}
	public Box getBox(int button) {
		if(button == 0) {
			return top;
		}
		if(button == 1) {
			return mid1;
		}
		if(button == 2) {
			return mid2;
		}
		if(button == 3) {
			return bottom;
		}
		return null;
	}
	public void toMainMenu() {
		Game.gameState = STATE.Menu;
		handler.loadArea(0);
		top.changeText("Play");
		mid1.changeText("Help");
		mid2.changeText("Settings");
		bottom.changeText("Quit");
		mouseClick();
		handler.player = null;
		return;
	}
	public void mouseClick() {
		AudioPlayer.getSound("click").play(1,Game.effectVolume);
	}
		public void mousePressed(MouseEvent e) {

		int mx = e.getX();
		int my = e.getY();
	
		
		if (Game.gameState == STATE.Menu) {
			if (mouseOver(mx, my, buttonX, buttonY, 300, 100)) {
				Game.gameState = STATE.Mode;
			
				top.changeText("Story");
				mid1.changeText("Survival");
				mid2.changeText("Back");
				mouseClick();
				return;
			}
			else if (mouseOver(mx, my,buttonX, buttonY+110, 300, 100)) {
				Game.gameState = STATE.Help;
				mouseClick();
				mid1.setColors(0, 0);
			
				top.changeText("Collect ship parts");
				mid1.changeText("Avoid and kill enemys");
				mid2.changeText("Return to space");
				bottom.changeText("Back");
				return;
			} else if (mouseOver(mx, my,buttonX, buttonY+ 220, 300, 100)) {
				Game.gameState = STATE.Select;
				mouseClick();
				mode = ss.grabImage(Image.EasyMode);
				top.changeText("Cheats");
				mid1.changeText("Easy");
				mid2.changeText("Hard");
				bottom.changeText("Back");
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY+ 330, 300, 100)) {
				System.exit(1);
			}
			
		}
		else if (Game.gameState == STATE.Mode) {
			if (mouseOver(mx, my, buttonX, buttonY, 300, 100)) {
			
				Game.gameState = STATE.Story;
			
				Game.paused = false;
				handler.clear();
				for(int i = 1; i < 9;i++) {
					spawner.spawnStory(i);
				}
				
				handler.loadArea(1);
				
				mouseClick();
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY+ 110, 300, 100)) {
				Game.gameState = STATE.Survival;
				handler.clear();
				Game.paused = false;
				spawner.spawnSurvival();
				spawner.setPlayer(handler.getPlayer());
				handler.loadArea(1);
				survivalMode = true;
				mouseClick();
				return;
			} else if (mouseOver(mx, my,buttonX, buttonY + 220, 300, 100)) {
				toMainMenu();
			} 
		}
		else if (Game.gameState == STATE.Paused) {
			if (mouseOver(mx, my, buttonX, buttonY, 300, 100)) {
				if(!survivalMode) {
					Game.gameState = STATE.Story;
				}else {
					Game.gameState = STATE.Survival;
				}
				
				Game.paused = false;
				mouseClick();
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY+ 110, 300, 100)) {
				if(!showHelp) {
					showHelp = true;
					mid1.changeText("Back");
				}else {
					mid1.changeText("Help");
					showHelp = false;
				}
				mouseClick();
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY+ 220, 300, 100)) {
				toMainMenu();
			}else if (mouseOver(mx, my,buttonX, buttonY + 330, 300, 100)) {
				System.exit(1);
			} 
		}
		else if (Game.gameState == STATE.Select) {
			// LEFT BUTTONS
			if (mouseOver(mx, my, buttonX, buttonY, 300, 100)) {
				game.diff = 2;
				mode = ss.grabImage(Image.CheatMode);
				Game.ammoUse = 0;
				mouseClick();
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY + 110, 300, 100)) {
				game.diff = 0;
				mode = ss.grabImage(Image.EasyMode);
				Game.ammoUse = 1;
				mouseClick();
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY + 220, 300, 100)) {
				game.diff = 1;
				Game.ammoUse = 1;
				mode = ss.grabImage(Image.HardMode);
				mouseClick();
				return;
			} else if (mouseOver(mx, my, buttonX, buttonY+ 330, 300, 100)) {
				toMainMenu();
			}
		}
			if(Game.gameState == STATE.Select || Game.gameState == STATE.Paused){//VOLUME 
			if (mouseOver(mx, my, soundButtonX,soundButtonY, 100, 100)) {
				Game.effectVolume =(( Game.effectVolume * 100)-5)/100;
				mouseClick();
			
			} 
			if (mouseOver(mx, my, soundButtonX+200,soundButtonY, 100, 100)) {
				Game.effectVolume =(( Game.effectVolume * 100)+5)/100;
				mouseClick();
				
			}
			if (mouseOver(mx, my, soundButtonX,soundButtonY+100, 100, 100)) {
				Game.musicVolume =(( Game.musicVolume * 100)-5)/100;
				AudioPlayer.getMusic(handler.getLvl()).setVolume(Game.musicVolume);
				mouseClick();
			} 
			if (mouseOver(mx, my, soundButtonX+200,soundButtonY+100, 100, 100)) {
				Game.musicVolume =(( Game.musicVolume * 100)+5)/100;
				AudioPlayer.getMusic(handler.getLvl()).setVolume(Game.musicVolume);
				mouseClick();
				
			}
			//Particles
			if (mouseOver(mx, my, soundButtonX,soundButtonY+200, 100, 100)) {
				Game.particleLevel --;
				
				
			} 
			if (mouseOver(mx, my, soundButtonX+200,soundButtonY+200, 100, 100)) {
				Game.particleLevel ++;
			
				
			}
			}
		
		else if (mouseOver(mx, my, buttonX, buttonY, 300, 100)
				&& Game.gameState == STATE.End) {
			if(!survivalMode) {
			Game.gameState = STATE.Story;
			HUD.HEALTH = 100;
			HUD.pistolAmmo -=2;
			HUD.missileAmmo -=1;
			HUD.flameAmmo -=1;
			handler.clearLevel(HUD.lastLevel);
			spawner.spawnStory(HUD.lastLevel);
			handler.loadArea(HUD.lastLevel);
			
			handler.player.setX(handler.playCoordsx.get(HUD.lastLevel));
			handler.player.setY(handler.playCoordsy.get(HUD.lastLevel));
			mouseClick();
			return;
			}else {
				Game.gameState = STATE.Survival;
				HUD.HEALTH = 100;
				HUD.kills = 0;
				handler.clear();
				Game.paused = false;
				spawner.spawnSurvival();
				spawner.setPlayer(handler.getPlayer());
				handler.loadArea(1);
				
				mouseClick();
				return;
			}
		} else if (mouseOver(mx, my, buttonX, buttonY+110, 300, 100)
				&& Game.gameState == STATE.End) {
			toMainMenu();
			return;
		} else if (mouseOver(mx, my,buttonX, buttonY+330, 300, 100)
				&& Game.gameState == STATE.Help) {
			toMainMenu();
		}
	}

	public void mouseReleased(MouseEvent e) {
	}

	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if (mx > x && mx < x + width) {
			if (my > y && my < y + height) {
				return true;
			}
		}
		return false;
	}

	public void mouseMoved(MouseEvent e) {

		mx = (int) e.getPoint().getX();
		my = (int) e.getPoint().getY();
	
		
	}

	public void tick() {
		if (tick == 380) {
			tick = 0;
			velX = -velX;
			

		}
		if(tick % 160 == 0) {
			velY = -velY;
		}
		x += velX;
		y += velY;
		tick++;
		
		if(Game.gameState == STATE.Paused) {
		top.setColors(mx, my);
		mid1.setColors(mx, my);
		mid2.setColors(mx, my);
		bottom.setColors(mx, my);
		effectUp.setColors(mx, my);
		effectDown.setColors(mx, my);
		musicUp.setColors(mx, my);
		musicDown.setColors(mx, my);
		particleUp.setColors(mx, my);
		particleDown.setColors(mx, my);
		}
		if(Game.gameState == STATE.End) {
			top.setColors(mx, my);
			mid1.setColors(mx, my);
			top.changeText("Re-Try");
			mid1.changeText("Menu");
			getKillLvl(hud.getKills());
			mid2.changeText(killLvl);
			bottom.changeText("Kills: " + hud.getKills());
			
		}
		if (mouseOver(mx, my, buttonX, buttonY, 300, 100) && Game.gameState == STATE.Mode) {
			mode = story;
		}
		if (mouseOver(mx, my, buttonX, buttonY+110, 300, 100) && Game.gameState == STATE.Mode) {
			mode = survival;
		}
		
	}
	public void getKillLvl(int kills) {
		if(kills < 50) {
			killLvl = "Dream Stan";
		}
		if(kills < 30) {
			killLvl = "Slayer";
		}
		if(kills < 20) {
			killLvl = "Mid";
		}
		if(kills < 10) {
			killLvl = "Noob";
		}
		
		
		
	}
	public void tips(Graphics g) {
		g.setFont(hFont);
		// Box on the right where tips and info go
		g.setColor(Color.DARK_GRAY);
		g.fillRect(Game.WIDTH - 460, 180, 440, 680);
		g.setColor(Color.gray);
		g.drawRect(Game.WIDTH - 460, 180, 440, 680);
		// Info and tips
		g.setColor(Color.white);
		g.drawString(("Info and Tips"), Game.WIDTH - 380, 210);
		g.drawString(("Kills: " + hud.getKills()), Game.WIDTH - 450, 250);
		// Items and pause
		g.drawImage(items, 102, 40, 256,128, null);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(5, 180, 455, 680);
		g.setColor(Color.gray);
		g.drawRect(5, 180, 455, 680);
		g.drawImage(pause, Game.WIDTH / 2 - 128, Game.HEIGHT / 2, 256,128, null);
		
	}
	public void write(int x, int y, String tip, int width, Graphics g) {
		String line = "";
		String word = "";
		char pointer;
		int loops = 0;
		for (int i = 0; i < tip.length(); i++) {
			pointer = tip.charAt(i);
			word += pointer;
			if (word.length() > 0 && pointer == ' ') {
				line += word;
				word = "";
				
			}
			if (line.length() * 12 / 16 * g.getFont().getSize() >= width) {
				g.drawString(line, x, y + loops * 20);
				line = "";
				word = "";
				loops++;
			}
		}
		line += word;
		g.drawString(line, x, y + loops * 20);
		line = "";
		word = "";
		loops++;
	}
	
	public void tip(int level, Graphics g) {
		
		Font sFont = new Font("SilkScreen", Font.TRUETYPE_FONT, 16);
		g.setFont(sFont);
		g.setColor(Color.white);
		if (level == 0) {
			icon = ss.grabImage(8, 4, 16, 16);
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(Game.WIDTH - 470, 560, 450, 350, 10, 10);
			g.setColor(Color.white);
			g.drawImage(icon, Game.WIDTH - 340, Game.HEIGHT - 200, 128, 128, null);
			write(Game.WIDTH - 440, 600, one, 420,g);

		}
		if (level == 1) {
			icon = ss.grabImage(1, 8, 16, 16);
			icon2 = ss.grabImage(2, 8, 16, 16);
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(Game.WIDTH - 470, 560, 450, 300, 10, 10);
			g.setColor(Color.white);
			g.drawImage(icon, Game.WIDTH - 340, Game.HEIGHT - 200, 64, 64, null);
			g.drawImage(icon2, Game.WIDTH - 240, Game.HEIGHT - 200, 64, 64, null);
			write(Game.WIDTH - 460, 600, two, 420,g);
		}
		if (level == 2) {
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(Game.WIDTH - 470, 560, 450, 150, 10, 10);
			g.setColor(Color.white);
			write(Game.WIDTH - 460, 600, three, 420,g);

		}
		if (level == 3) {
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(Game.WIDTH - 470, 560, 450, 150, 10, 10);
			g.setColor(Color.white);
			write(Game.WIDTH - 460, 600, boss, 420,g);

		}
	}

	public void items(int x, int y, int size, int space, Graphics g) {
		int xOffset = 0;
		for(int i = 0; i < 6; i ++) {
			if (handler.getPlayer().getCheck(i)) {
				if(i == 0) {
					item = battery;
				}
				if(i == 1) {
					item = drill;
				}
				if(i == 2) {
					item = cone;
				}
				if(i == 3) {
					item = wing;
				}
				if(i == 4) {
					item = card;
				}
				if(i == 5) {
					item = wrench;
				}
				g.setColor(Color.gray);
				g.fillRect(x +xOffset, y, size, size);
				g.setColor(Color.white);
				g.drawRect(x +xOffset, y, size, size);
				
				g.drawImage(item, x + xOffset,y+10, 64,64,null);
				xOffset += space+size;
					
				}
			}
			
		
	}
	public void survivalMode(boolean mode) {
		survivalMode = mode;
	}
	public void render(Graphics g) {

		if (Game.gameState == STATE.Menu) {
			top.setColors(mx, my);
			mid1.setColors(mx, my);
			mid2.setColors(mx, my);
			bottom.setColors(mx, my);
			g.drawImage(title, 330, 250, Game.WIDTH / 2,Game.HEIGHT / 2 - 100, null);
			// g.drawImage(ship, x, y, null);
			g.drawImage(ship, (int) x, (int) y, (int) x + 192, (int) y + 256, 0, 0, 64, 64, null);
			g.setColor(box);
			g.setFont(font);
			top.render(g);

			mid1.render(g);

			mid2.render(g);

			bottom.render(g);
		
		} else if (Game.gameState == STATE.Help) {
			bottom.setColors(mx, my);
			
			g.setFont(smallfont);
			top.render(g);
			
			//g.drawImage(plaque, Game.WIDTH/2-320,0,640,512,null);
			g.drawImage(helpImage, Game.WIDTH/2-320,200,576,448,null);
			mid1.render(g);

			mid2.render(g);
			g.setFont(font);
			bottom.render(g);
	
		} else if (Game.gameState == STATE.End) {
			//g.setColor(text);
			g.setFont(font);
			g.setFont(font);
			top.render(g);

			mid1.render(g);

			mid2.render(g);

			bottom.render(g);
			

		}
		else if (Game.gameState == STATE.Select) {
			top.setColors(mx, my);
			mid1.setColors(mx, my);
			mid2.setColors(mx, my);
			bottom.setColors(mx, my);
			g.drawImage(plaque, Game.WIDTH/2-128,200,256,256,null);
			g.drawImage(mode, Game.WIDTH/2-64,270,128,128,null);
			g.setFont(font);
			top.render(g);

			mid1.render(g);

			mid2.render(g);

			bottom.render(g);
			
			effectUp.render(g);
			effectDown.render(g);
			musicUp.render(g);
			musicDown.render(g);
			particleUp.render(g);
			particleDown.render(g);
			g.drawString(String.valueOf(Math.floor(Game.effectVolume*100)), soundButtonX+100, soundButtonY+50);
			g.drawString(String.valueOf(Math.floor(Game.musicVolume*100)), soundButtonX+100, soundButtonY+150);
			g.drawString(String.valueOf(Math.floor(Game.particleLevel)), soundButtonX+100, soundButtonY+250);
			g.setFont(smallfont);
			g.drawString("Effects", soundButtonX+100, soundButtonY+10);
			g.drawString("Music", soundButtonX+100, soundButtonY+110);
			g.drawString("Particles", soundButtonX+100, soundButtonY+210);
		}
		else if (Game.gameState == STATE.Mode) {
			top.setColors(mx, my);
			mid1.setColors(mx, my);
			mid2.setColors(mx, my);
			g.drawImage(plaque, Game.WIDTH/2-128,200,256,256,null);
			g.drawImage(mode, Game.WIDTH/2-64,270,128,128,null);
			g.setFont(font);
			top.render(g);

			mid1.render(g);

			mid2.render(g);

		//	bottom.render(g);
		}
		else if (Game.gameState == STATE.Paused) {
			
			g.setFont(font);
			if(showHelp) {
			g.drawImage(helpImage, Game.WIDTH/2-320,200,576,448,null);
			}else {
				g.drawImage(pause, Game.WIDTH / 2 - 128, Game.HEIGHT / 2-200, 256,128, null);
				if(!survivalMode) {
					items(220,45,80,10,g);
					tip(hud.getLevel(),g);
				}
			
			}
			g.setFont(font);
			top.render(g);

			mid1.render(g);

			mid2.render(g);

			bottom.render(g);
			
			effectUp.render(g);
			effectDown.render(g);
			musicUp.render(g);
			musicDown.render(g);
			particleUp.render(g);
			particleDown.render(g);
			g.setColor(Color.white);
			g.drawString(String.valueOf(Math.floor(Game.effectVolume*100)), soundButtonX+100, soundButtonY+50);
			g.drawString(String.valueOf(Math.floor(Game.musicVolume*100)), soundButtonX+100, soundButtonY+150);
			g.drawString(String.valueOf(Math.floor(Game.particleLevel)), soundButtonX+100, soundButtonY+250);
			g.setFont(smallfont);
			g.drawString("Effects", soundButtonX+100, soundButtonY+10);
			g.drawString("Music", soundButtonX+100, soundButtonY+110);
			g.drawString("Particles", soundButtonX+100, soundButtonY+210);
		}
	}

}
