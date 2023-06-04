package crashRecovery.main;

import java.awt.Color;
import java.util.Random;

public class Spawn {

	private Handler handler;
	private HUD hud;
	private Player player;
	private Random rand = new Random();
	private int scoreKeep = 0;
	private long lastSpawn = System.currentTimeMillis();
	private boolean spawnedDrops = false;
	private long waveCoolDown = System.currentTimeMillis();
	private int currentWave = 0;
	Game game;
	private int waveKills = 0;
	private int[] waveMobCap = { 3, 3, 6, 3, 5, 1 };
	private int[] waveSpawnRate = { 4000, 2000, 1000, 2000, 2000, 6000 };

	public Spawn(Handler handler, HUD hud, Game game) {
		this.handler = handler;
		this.hud = hud;
		this.game = game;
		this.player = handler.getPlayer();

	}

	public void spawnDecor(int level) {
		for (int i = 0; i < 10; i++) {
			int decorX = rand.nextInt(18 * 64);
			int decorY = rand.nextInt(18 * 64);
			handler.storeLevel(level, new MapDecor(decorX, decorY, ID.FloorDecor, game, Image.Crater));
		}
		for (int i = 0; i < 10; i++) {
			int decorX = rand.nextInt(18 * 64);
			int decorY = rand.nextInt(18 * 64);
			int rock = rand.nextInt(4);
			if (rock == 0) {
				handler.storeLevel(level, new MapDecor(decorX, decorY, ID.FloorDecor, game, Image.Rock1));
			}
			if (rock == 1) {
				handler.storeLevel(level, new MapDecor(decorX, decorY, ID.FloorDecor, game, Image.Rock2));
			}
			if (rock == 2) {
				handler.storeLevel(level, new MapDecor(decorX, decorY, ID.FloorDecor, game, Image.Rock3));
			}
			if (rock == 3) {
				handler.storeLevel(level, new MapDecor(decorX, decorY, ID.FloorDecor, game, Image.Rock4));
			}

		}
	}

	public void setPlayer(Player set) {
		player = set;
	}

	public void spawnEnemy(GameObject obj, int side) {
		if (side == 0) {
			obj.setX(Game.WIDTH / 2 - 64 + rand.nextInt(64));
			obj.setY(0);
		}
		if (side == 1) {
			obj.setX(Game.WIDTH - 100);
			obj.setY(Game.HEIGHT / 2 - 64 + rand.nextInt(64));
		}
		if (side == 2) {
			obj.setX(Game.WIDTH / 2 - 64 + rand.nextInt(64));
			obj.setY(Game.HEIGHT - 100);
		}
		if (side == 3) {
			obj.setX(0);
			obj.setY(Game.HEIGHT / 2 - 64 + rand.nextInt(64));
		}
		if(obj.getId() == ID.BossEnemy) {
			obj.setX(Game.WIDTH/2-128);
			obj.setY(-80);
		}
		handler.addObject(obj);
		lastSpawn = System.currentTimeMillis();
	}

	public void spawnSurvival() {

		hud.drawn = true;
		handler.createPlayer(Game.HEIGHT / 2+128, Game.HEIGHT / 2-64, game);
		handler.storeLevel(1, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.TileFloor, Image.SandFloor, 1, game));
		handler.playerCoords(1, Game.HEIGHT / 2, Game.HEIGHT / 2);

		
		// Walls
		new Wall(0, 0, ID.Wall, Game.WIDTH / 2 - 128, 64, 1, 3, game);
		new Wall(Game.WIDTH / 2 + 128, 0, ID.Wall, Game.WIDTH / 2 - 128, 64, 1, 3, game);
		new Wall(0, Game.HEIGHT - 64, ID.Wall, Game.WIDTH / 2 - 128, 64, 1, 3, game);
		new Wall(Game.WIDTH / 2 + 128, Game.HEIGHT - 64, ID.Wall, Game.WIDTH / 2 - 128, 64, 1, 3, game);

		new Wall(0, 64, ID.Wall, 64, Game.HEIGHT / 2 - 192, 1, 3, game);
		new Wall(0, Game.HEIGHT / 2 + 100, ID.Wall, 64, Game.HEIGHT / 2 - 128, 1, 3, game);
		new Wall(Game.WIDTH - 64, 0, ID.Wall, 64, Game.HEIGHT / 2 - 128, 1, 3, game);
		new Wall(Game.WIDTH - 64, Game.HEIGHT / 2 + 100, ID.Wall, 64, Game.HEIGHT / 2 - 128, 1, 3, game);

		new Wall(0, -64, ID.Wall, Game.WIDTH, 64, 1, 3, game);
		new Wall(0, Game.HEIGHT, ID.Wall, Game.WIDTH, 64, 1, 3, game);
		new Wall(-64, 0, ID.Wall, 64, Game.HEIGHT, 1, 3, game);
		new Wall(Game.WIDTH, 0, ID.Wall, 64, Game.HEIGHT, 1, 3, game);
		handler.storeLevel(1, new MapDecor(64 * 2, 64 * 2 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(1, new MapDecor(64 * 2, 64 * 13 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(1, new MapDecor(64 * 17, 64 * 2 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(1, new MapDecor(64 * 17, 64 * 13 - 32, ID.Decor, game, Image.Torch));
		checkNewWave();
		playMusic();
		
	}

	public void checkNewWave() {
		if (waveKills >= currentWave*3) {
			waveCoolDown = System.currentTimeMillis();
			currentWave++;
			ammoDrop(currentWave);
			handler.clearEnemys();
			AudioPlayer.getMusic(4).pause();
			waveKills = 0;
		}else {
			waveKills++;
		}
	}

	public void ammoDrop(int wave) {

		ID id;
		Image img;
		for (int i = 0; i < currentWave + 2; i++) {
			int type = rand.nextInt(4);
			
			if (type == 1) {
				id = ID.HealthDrop;
				img = Image.HealthDrop;
			}
			if (type == 2) {
				id = ID.MissileDrop;
				img = Image.MissileDrop;
			}
			if (type == 3) {
				id = ID.FlameDrop;
				img = Image.FlameDrop;
			}else {
				id = ID.BulletDrop;
				img = Image.BulletDrop;
			}
			handler.storeLevel(1, new Drop(Game.WIDTH / 2 + rand.nextInt(256) - 128,
					Game.HEIGHT / 2 + rand.nextInt(256) - 128, id, game, img));
		}
	}

	
	public void waveCaller(int wave) {
		if (System.currentTimeMillis() - lastSpawn > (rand.nextInt((10000 /currentWave))+1000)
				&& handler.enemyCount() < currentWave + 3) {
			GameObject temp = new BasicEnemy(0, 0, ID.BasicEnemy, game, null);
			if (currentWave >=5) {
				if (rand.nextInt(7) == 5) {
					temp = new TankEnemy(0, 0, ID.TankEnemy, game, null);
				}
			}
			if (currentWave >= 10) {
				if (rand.nextInt(10) == 5) {
					temp = new LazerBot(0, 0, ID.LazerBot, game, null);
				}
			}
			if (currentWave >=15) {
				if (rand.nextInt(15) == 5) {
					temp = new BossEnemy(0, 0, ID.BossEnemy, game,true);
				}
			}

			spawnEnemy(temp, rand.nextInt(4));
		}

	}

	public void playMusic() {
		if (!AudioPlayer.getMusic(4).playing()) {
			AudioPlayer.getMusic(4).loop(1f, Game.musicVolume);
		}
	}

	public void tickSurvival() {
		if (handler.cLvl == 1) {
			if (System.currentTimeMillis() - waveCoolDown > Game.clamp(1000 * currentWave, 0, 10000)) {
				waveCaller(currentWave);
				playMusic();
			}
			else {
				handler.clearEnemys();
			}

		}
	}
	
	public void spawnStory(int level) {
		if(level == 1) {
		hud.drawn = true;
		handler.createPlayer(180, Game.HEIGHT - 360,1, game);
		handler.storeLevel(1, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.Floor, 1, game));
		handler.storeLevel(1, new Floor(64 * 16, 64 * 11, 64 * 4, 64 * 5, ID.TileFloor, Image.SandFloor, 1, game));
		handler.playerCoords(1, 180, Game.HEIGHT - 360);
		spawnDecor(1);
		// Enemys

		handler.storeLevel(1,
				new BasicEnemy(rand.nextInt(200) + Game.WIDTH / 3, rand.nextInt(200), ID.BasicEnemy, game, null));

		// handler.storeLevel(1, new BasicEnemy(1164, Game.HEIGHT - 192, ID.BasicEnemy,
		// game, null));
		handler.storeLevel(1, new Drop(300+rand.nextInt(128), 200+rand.nextInt(128), ID.BulletDrop, game, Image.BulletDrop));
		if (game.diff == 2) {
			handler.storeLevel(1, new AreaFlag(10, Game.HEIGHT / 2 - 128, ID.AreaFlag, game, Image.Left, 4, 1, true));// left
																														// Arrow
			handler.storeLevel(1, new CheckPoint(300, Game.HEIGHT - 200, ID.CheckPoint, game, Image.Battery));// battery
			handler.storeLevel(1, new CheckPoint(400, Game.HEIGHT - 200, ID.CheckPoint, game, Image.Drill));// drill
			handler.storeLevel(1, new CheckPoint(500, Game.HEIGHT - 200, ID.CheckPoint, game, Image.Card));// card
			handler.storeLevel(1, new CheckPoint(600, Game.HEIGHT - 200, ID.CheckPoint, game, Image.BossDrop));// card
		}
		handler.storeLevel(1, new AreaFlag(Game.WIDTH - 500, 0, ID.AreaFlag, game, Image.CaveClosed, 5, 1, false));// cave
		// Walls
		new Wall(0, Game.HEIGHT - 516, ID.Wall, 832, 63, 1, 0, game);
		new Wall(0, Game.HEIGHT - 452, ID.Wall, 768, 63, 1, 0, game);
		new Wall(64 * 15, 64 * 10, ID.Wall, 64, 320, 1, 0, game);
		new Wall(64 * 15, 64 * 10, ID.Wall, 320, 64, 1, 0, game);

		// Points and decor
		handler.storeLevel(1, new CheckPoint(0, 650, ID.CheckPoint, game, Image.CrashedShip));
		handler.storeLevel(1, new CheckPoint(300+rand.nextInt(128), 200+rand.nextInt(128), ID.CheckPoint, game, Image.Cone));// nose cone
		handler.storeLevel(1, new MapDecor(64 * 16, 64 * 9, ID.FloorDecor, game, Image.Dish));// dish
		}
		if(level ==2) {
		// Area two objects
		handler.storeLevel(2, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.Floor, 2, game));
		handler.storeLevel(2, new Floor(64 * 5, 64 * 4, 64 * 11, 64 * 5, ID.TileFloor, Image.SandFloor, 2, game));
		handler.createPlayer(400, Game.HEIGHT - 200,2, game);
		handler.playerCoords(2, 400, Game.HEIGHT - 200);

		// Decor
		spawnDecor(2);
		handler.storeLevel(2, new MapDecor(64 * 5, 64 * 5 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(2, new MapDecor(64 * 5, 64 * 8 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(2, new MapDecor(64 * 15, 64 * 5 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(2, new MapDecor(64 * 15, 64 * 8 - 32, ID.Decor, game, Image.Torch));
		new Wall(256, 192, ID.Wall, 63, 448, 2, 0, game);
		new Wall(256, Game.HEIGHT - 316, ID.Wall, 768, 64, 2, 0, game);
		new Wall(256, 192, ID.Wall, 768, 64, 2, 0, game);// LEFT HERE

		new Wall(1024, 192, ID.Wall, 64, 192, 2, 0, game);
		new Wall(Game.WIDTH, 0, ID.Wall, 64, Game.HEIGHT, 2, 0, game);
		new Wall(1024, Game.HEIGHT - 448, ID.Wall, 64, 448, 2, 0, game);

		handler.storeLevel(2, new CheckPoint(100, Game.HEIGHT - 200, ID.CheckPoint, game, Image.Launcher));// launcher
		new Wall(0, 0, ID.Wall, 1280, 64, 2, 0, game);
		for (int i = 1; i < 3 * (game.diff + 1); i++) {
			handler.storeLevel(2, new TankEnemy(rand.nextInt(Game.WIDTH - 1024) + 320,
					rand.nextInt(Game.HEIGHT - 700) + 256, ID.TankEnemy, game, null));
			handler.storeLevel(2, new BasicEnemy(rand.nextInt(Game.WIDTH - 1024) + 320,
					rand.nextInt(Game.HEIGHT - 700) + 256, ID.BasicEnemy, game, null));
		}
		}
		if(level == 3) {
		// Boss Area objects
		handler.storeLevel(3, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.TileFloor, 3, game));
		handler.createPlayer((Game.WIDTH - 40) / 2, 150,3, game);
		handler.playerCoords(3, (Game.WIDTH - 40) / 2, 150);
		// Decor
		handler.storeLevel(3, new MapDecor(64 * 2, 64 * 2 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(3, new MapDecor(64 * 2, 64 * 13 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(3, new MapDecor(64 * 17, 64 * 2 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(3, new MapDecor(64 * 17, 64 * 13 - 32, ID.Decor, game, Image.Torch));

		// Walls

		new Wall(64 * 3, 256, ID.Wall, 832 + 64, 64, 3, 1, game);
		new Wall(0, 0, ID.Wall, Game.WIDTH, 64, 3, 1, game);
		new Wall(0, Game.HEIGHT - 64, ID.Wall, Game.WIDTH, 64, 3, 1, game);
		new Wall(0, 64*5, ID.Wall, 64, 64*12, 3, 1, game);
		new Wall(Game.WIDTH - 64, 0, ID.Wall, 64, Game.HEIGHT, 3, 1, game);
		// Map items
		handler.storeLevel(3, new BossEnemy(500, 500, ID.BossEnemy, game));
		}
		if(level == 4) {
		// Area 4 objects
		handler.storeLevel(4, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.Floor, 4, game));
		handler.createPlayer(Game.WIDTH - 20, Game.HEIGHT / 2 - 128,4, game);
		handler.playerCoords(4, Game.WIDTH - 20, Game.HEIGHT / 2 - 128);
		// Decor
		spawnDecor(4);
		// Spawner
		handler.storeLevel(4, new ObjectSpawner(Game.WIDTH / 8 + 128, Game.HEIGHT / 4, ID.ObjectSpawner, 5, 10,
				ID.BasicEnemy, 4, game));// robot
		handler.storeLevel(4, new ObjectSpawner(Game.WIDTH / 2 - 128, Game.HEIGHT / 4, ID.ObjectSpawner, 2, 4,
				ID.TankEnemy, 4, game));// tank
		handler.storeLevel(4, new ObjectSpawner(Game.WIDTH / 8 * 7, Game.HEIGHT / 4 * 3, ID.ObjectSpawner, 2, 2,
				ID.MissileDrop, 4, game));// Missile
		handler.storeLevel(4,
				new ObjectSpawner(Game.WIDTH / 8 * 7, Game.HEIGHT / 4, ID.ObjectSpawner, 5, 5, ID.BulletDrop, 4, game));// bullet
		// Walls
		new Wall(0, 0, ID.Wall, Game.WIDTH, 64, 4, 1, game);
		new Wall(0, 0, ID.Wall, 64, Game.HEIGHT, 4, 1, game);
		new Wall(0, Game.HEIGHT - 64, ID.Wall, Game.WIDTH, 64, 4, 1, game);
		new Wall(Game.WIDTH / 8 * 6, 0, ID.Wall, 64, Game.HEIGHT / 2 - 64, 4, 1, game);
		new Wall(Game.WIDTH / 8 * 6, Game.HEIGHT / 2 + 64, ID.Wall, 64, Game.HEIGHT / 2 - 64, 4, 1, game);
		new Wall(Game.WIDTH / 8 * 5 - 32, 0, ID.Wall, 64, Game.HEIGHT / 4 * 3, 4, 1, game);
		new Wall(0, Game.HEIGHT / 4 * 3 - 10, ID.Wall, Game.WIDTH / 4, 64, 4, 1, game);
		new Wall(Game.WIDTH / 4 + 192, Game.HEIGHT / 4 * 3 - 10, ID.Wall, Game.WIDTH / 4, 64, 4, 1, game);
		new Wall(192, Game.HEIGHT / 4 * 3 - 202, ID.Wall, 448, 64, 4, 1, game);
		// Tanks
		handler.storeLevel(4, new TankEnemy(864, 70, ID.TankEnemy, game, null));
		handler.storeLevel(4, new TankEnemy(864, Game.HEIGHT - 130, ID.TankEnemy, game, null));
		}
		if(level == 5) {
		// Area 5 Objects
		handler.storeLevel(5, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.DarkFloor, 5, game));
		handler.createPlayer(100, Game.HEIGHT - 100,5, game);
		handler.playerCoords(5, 100, Game.HEIGHT - 100);

		// Walls
		// new Platform(0, 0, ID.Platform, 64, Game.HEIGHT, 5, 1, handler);
		new Wall(0, 0, ID.Wall, Game.WIDTH, 64, 5, 2, game);
		new Wall(0, Game.HEIGHT - 186, ID.Wall, 256, 64, 5, 2, game);
		new Wall(192, Game.HEIGHT - 378, ID.Wall, 256, 64, 5, 2, game);
		new Wall(0, Game.HEIGHT - 570, ID.Wall, 256, 64, 5, 2, game);
		new Wall(448, 128, ID.Wall, 64, 896, 5, 2, game);
		new Wall(512, 448, ID.Wall, 512, 64, 5, 2, game);
		new Wall(896, 448, ID.Wall, 64, 512, 5, 2, game);
		new Wall(1152, 0, ID.Wall, 64, 512, 5, 2, game);
		// Tanks and Bots
		handler.storeLevel(5, new TankEnemy(640, 320, ID.TankEnemy, game, null));
		handler.storeLevel(5, new TankEnemy(896, 384, ID.TankEnemy, game, null));
		handler.storeLevel(5, new BasicEnemy(192, 672, ID.BasicEnemy, game, null));
		handler.storeLevel(5, new LazerBot(256, 256, ID.LazerBot, game, null));
		handler.storeLevel(5, new BasicEnemy(1152, 832, ID.BasicEnemy, game, null));
		// Area flag
		handler.storeLevel(5, new AreaFlag(20, Game.HEIGHT - 120, ID.AreaFlag, game, Image.Left, 1, 5, true));// left
																												// arrow
		handler.storeLevel(5,
				new AreaFlag(Game.WIDTH - 80, Game.HEIGHT - 180, ID.AreaFlag, game, Image.Right, 6, 5, true));// right
																												// arrow
		handler.storeLevel(5, new ColorFilter(0, 0, Game.WIDTH, Game.HEIGHT, ID.Filter, game, Color.BLACK));
		}
		if(level == 6) {
		// Area 6 Objects
		handler.createPlayer(100, Game.HEIGHT - 100,6, game);
		handler.storeLevel(6, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.DarkFloor, 6, game));
		handler.playerCoords(6, 100, Game.HEIGHT - 100);
		// Walls
		// new Platform(0, 0, ID.Platform, 64, Game.HEIGHT, 5, 1, handler);
		new Wall(0, 0, ID.Wall, 64, 64 * 12, 6, 2, game);
		new Wall(0, 0, ID.Wall, 64 * 12, 64, 6, 2, game);
		new Wall(64 * 4, 64 * 4, ID.Wall, 64, 64 * 12, 6, 2, game);
		new Wall(64 * 5, 64 * 8, ID.Wall, 64 * 7, 64, 6, 2, game);
		new Wall(64 * 12, 0, ID.Wall, 64, 64 * 3, 6, 2, game);
		new Wall(64 * 12, 64 * 6, ID.Wall, 64, 64 * 3, 6, 2, game);
		new Wall(64 * 5, 64 * 11, ID.Wall, 64 * 12, 64, 6, 2, game);
		new Wall(64 * 16, 64 * 6, ID.Wall, 64, 64 * 5, 6, 2, game);
		new Wall(64 * 12, 64 * 3, ID.Wall, 64 * 3, 64, 6, 2, game);
		new Wall(64 * 17, 64 * 3, ID.Wall, 64 * 3, 64, 6, 2, game);
		new Wall(64 * 15, 64 * 3, ID.Wall, 64 * 2, 64, 6, 1, game);
		new Wall(0, Game.HEIGHT, ID.Wall, Game.WIDTH, 64, 6, 2, game);
		// Tanks and Bots
		handler.storeLevel(6, new TankEnemy(64 * 8, 64 * 10 - 32, ID.TankEnemy, game, null));
		handler.storeLevel(6, new BasicEnemy(64 * 9, 64 * 9 + 1, ID.BasicEnemy, game, null));
		handler.storeLevel(6, new LazerBot(64 * 5, 64 * 6 - 1, ID.LazerBot, game, null));
		handler.storeLevel(6, new LazerBot(64 * 11, 64 * 2, ID.LazerBot, game, null));
		handler.storeLevel(6, new LazerBot(64 * 11, 64 * 6 - 1, ID.LazerBot, game, null));
		// Spawners
		handler.storeLevel(6, new TankEnemy(64 * 12, 64 * 13, ID.TankEnemy, game, null));
		// Flame thrower and drops
		handler.storeLevel(6, new CheckPoint(1152, 100, ID.CheckPoint, game, Image.FlameThrower));// Flamethrower
		handler.storeLevel(6, new Drop(64 * 6, 64 * 12, ID.MissileDrop, game, Image.MissileDrop));

		// Checkpoints and flags
		handler.storeLevel(6, new CheckPoint(64 * 7, 64 * 10 - 32, ID.CheckPoint, game, Image.Card));// launcher
		handler.storeLevel(6, new Switch(64 * 12, 64 * 5, ID.Switch, 1, game));
		handler.storeLevel(6, new AreaFlag(64 * 7, 64 * 12, ID.AreaFlag, game, Image.RockLadder, 7, 6, false));
		handler.storeLevel(6, new ColorFilter(0, 0, Game.WIDTH, Game.HEIGHT, ID.Filter, game, Color.BLACK));
		}
		if(level == 7) {
		// level 7
		handler.storeLevel(7, new Floor(0, 0, Game.WIDTH, Game.HEIGHT, ID.Floor, Image.TileFloor, 7, game));
		handler.createPlayer(Game.WIDTH / 2 - 40, 64,7, game);
		handler.playerCoords(7, Game.WIDTH / 2 - 40, 64);
		// Walls
		// new Platform(0, 0, ID.Platform, 64, Game.HEIGHT, 5, 1, handler);
		new Wall(0, 0, ID.Wall, 64, 64 * 12, 7, 1, game);
		new Wall(64 * 6, 0, ID.Wall, 64, 64 * 12, 7, 1, game);
		new Wall(64 * 19, 0, ID.Wall, 64, 64 * 12, 7, 1, game);
		new Wall(64 * 13, 0, ID.Wall, 64, 64 * 12, 7, 1, game);

		// Decor
		handler.storeLevel(7, new MapDecor(64 * 2, 64 * 3, ID.FloorDecor, game, Image.Sattelite));// dish
		handler.storeLevel(7, new MapDecor(64 * 3, 64 * 4, ID.FloorDecor, game, Image.Sattelite));// dish
		handler.storeLevel(7, new MapDecor(64 * 7, 64 * 2 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 12, 64 * 2 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 7, 64 * 5 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 7, 64 * 8 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 12, 64 * 5 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 12, 64 * 8 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 7, 64 * 11 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(64 * 12, 64 * 11 - 32, ID.Decor, game, Image.Torch));
		handler.storeLevel(7, new MapDecor(Game.WIDTH / 2 - 40, 64, ID.Decor, game, Image.Hole));
		handler.storeLevel(7, new ColorFilter(0, 0, Game.WIDTH, Game.HEIGHT, ID.Filter, game, Color.DARK_GRAY));
		// Items
		handler.storeLevel(7, new Drop(64 * 4, 64 * 12, ID.HealthDrop, game, Image.HealthDrop));
		handler.storeLevel(7, new Drop(64 * 2, 64 * 12, ID.HealthDrop, game, Image.HealthDrop));
		handler.storeLevel(7, new Drop(64 * 15, 64 * 12, ID.HealthDrop, game, Image.HealthDrop));
		handler.storeLevel(7, new Drop(64 * 17, 64 * 12, ID.HealthDrop, game, Image.HealthDrop));

		handler.storeLevel(7, new Drop(64 * 4, 64 * 8, ID.BulletDrop, game, Image.BulletDrop));
		handler.storeLevel(7, new Drop(64 * 2, 64 * 8, ID.BulletDrop, game, Image.BulletDrop));
		handler.storeLevel(7, new Drop(64 * 15, 64 * 8, ID.BulletDrop, game, Image.BulletDrop));
		handler.storeLevel(7, new Drop(64 * 17, 64 * 8, ID.BulletDrop, game, Image.BulletDrop));
		// Area Flag
		handler.storeLevel(7, new AreaFlag(Game.WIDTH / 2 - 40, 64 * 13, ID.AreaFlag, game, Image.Down, 3, 7));
		// Tanks and Bots
		// handler.storeLevel(7, new TankEnemy(64 * 8, 64 * 10 - 32, ID.TankEnemy, game,
		// null));
		}
		if(level == 8) {
			// level 8
			for (int i = 0; i < 6 * Game.particleLevel; i++) {
				handler.storeLevel(8,
						new MenuParticle(rand.nextInt(Game.WIDTH), rand.nextInt(Game.HEIGHT), ID.MenuParticle, game));
			}
			for (int i = 0; i < 10 * Game.particleLevel; i++) {
				handler.storeLevel(8, new ParalaxParticle(rand.nextInt(Game.WIDTH), rand.nextInt(Game.HEIGHT),
						ID.MenuParticle, game));
			}
			handler.storeLevel(8, new MapDecor(Game.WIDTH/2-64,Game.HEIGHT/2,ID.Decor,game,Image.Ship));
			}
	}

}
