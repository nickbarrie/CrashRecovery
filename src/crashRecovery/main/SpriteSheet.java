package crashRecovery.main;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SpriteSheet {
	private BufferedImage sprite;
	private HashMap<Image,BufferedImage> sprites = new HashMap<>();
	public SpriteSheet(BufferedImage ss) {
		this.sprite = ss;
BufferedImage image;
		
		image = grabImage(3, 5, 48, 64);// ship
		sprites.put(Image.CrashedShip, image);
		
		image = grabImage(1, 5, 16, 16);// cone
		sprites.put(Image.Cone, image);
		
		image = grabImage(2, 5, 16, 16);// launcher
		sprites.put(Image.Launcher, image);
		
		image = grabImage(7, 4, 16, 16);// battery
		sprites.put(Image.Battery, image);
		
		image = grabImage(1, 7, 16, 16);// tailwing
		sprites.put(Image.Wing, image);
	
		image = grabImage(4, 3, 16, 16);// up arrow
		sprites.put(Image.Up, image);
		
		image = grabImage(5, 3, 16, 16);// down arrow
		sprites.put(Image.Down, image);
		
		image = grabImage(6, 3, 16, 16);// up left arrow
		sprites.put(Image.UpLeft, image);
		
		image = grabImage(7, 3, 16, 16);// down right arrow
		sprites.put(Image.DownRight, image);

		image = grabImage(9, 3, 16, 16);// left arrow
		sprites.put(Image.Left, image);
		
		image = grabImage(10, 3, 16, 16);// right arrow
		sprites.put(Image.Right, image);
		
		image = grabImage(11, 1, 48, 48);// open cave
		sprites.put(Image.CaveOpen, image);
		
		image = grabImage(14, 1, 48, 48);// closed cave
		sprites.put(Image.CaveClosed, image);
		
		image = grabImage(9, 4, 16, 16);// Drill
		sprites.put(Image.Drill, image);
		
		image = grabImage(2, 7, 16, 16);// flamethrower
		sprites.put(Image.FlameThrower, image);
		
		image = grabImage(9, 5, 16, 16);// keycard
		sprites.put(Image.Card, image);
		
		image = grabImage(11, 5, 16, 16);// blocked ladder
		sprites.put(Image.RockLadder, image);
		
		image = grabImage(12, 5, 16, 16);// ladder
		sprites.put(Image.Ladder, image);
		
		image = grabImage(6, 4, 16, 16);// crater 
		sprites.put(Image.Crater, image);
	
		image = grabImage(1, 6, 16, 16);// flame1
		sprites.put(Image.Flame1, image);
		
		image = grabImage(2, 6, 16, 16);// flame2 
		sprites.put(Image.Flame2, image);
		
		image = grabImage(10, 6, 112, 48);// title
		sprites.put(Image.Title, image);
		
		image = grabImage(6, 5, 48, 64);// ship
		sprites.put(Image.Ship, image);

		image = grabImage(1, 1, 16, 16);// Player
		sprites.put(Image.PlayerRight1, image);
		
		image = grabImage(2, 1, 16, 16);// Player
		sprites.put(Image.PlayerRight2, image);

		image = grabImage(4, 1, 16, 16);// Player
		sprites.put(Image.PlayerRight3, image);
		
		image = grabImage(3, 1, 16, 16);// Player
		sprites.put(Image.PlayerRight4, image);

		image = grabImage(8, 1, 16, 16);// Player
		sprites.put(Image.PlayerLeft1, image);
		
		image = grabImage(7, 1, 16, 16);// Player
		sprites.put(Image.PlayerLeft2, image);

		image = grabImage(5, 1, 16, 16);// Player
		sprites.put(Image.PlayerLeft3, image);
		
		image = grabImage(6, 1, 16, 16);// Player
		sprites.put(Image.PlayerLeft4, image);

		image = grabImage(1, 2, 16, 16);// Player
		sprites.put(Image.PlayerUp1, image);
		
		image = grabImage(2, 2, 16, 16);// Player
		sprites.put(Image.PlayerUp2, image);

		image = grabImage(3, 2, 16, 16);// Player
		sprites.put(Image.PlayerDown1, image);

		image = grabImage(4, 2, 16, 16);// Player
		sprites.put(Image.PlayerDown2, image);
		
		image = grabImage(6, 2, 16, 16);// Player
		sprites.put(Image.PlayerPunchLeft, image);

		image = grabImage(5, 2, 16, 16);// Player
		sprites.put(Image.PlayerPunchRight, image);
		
		image = grabImage(2, 4, 16, 16);// Floor
		sprites.put(Image.Floor, image);

		image = grabImage(10, 4, 16, 16);// Dark Floor
		sprites.put(Image.DarkFloor, image);
	
		image = grabImage(9, 6, 16, 16);// Flame ammo
		sprites.put(Image.FlameDrop, image);
		
		image = grabImage(3, 4, 16, 16);//  ammo
		sprites.put(Image.BulletDrop, image);

		image = grabImage(4, 4, 16, 16);//  missile
		sprites.put(Image.MissileDrop, image);
		
		image = grabImage(5, 4, 16, 16);//  health
		sprites.put(Image.HealthDrop, image);
	
		image = grabImage(14,4,16,16);
		sprites.put(Image.TankIdle, image);
		
		image = grabImage(3,3,16,16);
		sprites.put(Image.Tank, image);

		image = grabImage(10,1,16,16);
		sprites.put(Image.PlayerIdle1, image);
		
		image = grabImage(9,1,16,16);
		sprites.put(Image.PlayerIdle2, image);

		image = grabImage(9,7,16,16);
		sprites.put(Image.BossDrop, image);
		
		image = grabImage(9,8,16,16);
		sprites.put(Image.Vision, image);
		
		image = grabImage(13,5,16,16);
		sprites.put(Image.Hole, image);
		
		image = grabImage(15,4,32,32);
		sprites.put(Image.BossHover, image);
		
		image = grabImage(1,9,32,32);
		sprites.put(Image.BossHover2, image);

		image = grabImage(8,9,32,32);
		sprites.put(Image.BossLow, image);
		
		image = grabImage(3,9,16,16);
		sprites.put(Image.Tank2, image);
	
		image = grabImage(14,5,16,16);
		sprites.put(Image.LazerBotShoot, image);
		
		image = grabImage(15,9,16,16);
		sprites.put(Image.Fiery, image);
	
		image = grabImage(4,10,32,16);
		sprites.put(Image.HealthBar, image);
		
		image = grabImage(6,10,32,16);
		sprites.put(Image.DodgeBar, image);
	
		image = grabImage(10,9,32,32);
		sprites.put(Image.BossIdle, image);
		
		image = grabImage(12,9,16,16);
		sprites.put(Image.EnemyBullet, image);

		image = grabImage(13,9,16,16);
		sprites.put(Image.PlayerBullet, image);
		
		image = grabImage(14,9,16,16);
		sprites.put(Image.BossBullet, image);

		image = grabImage(12,10,16,16);
		sprites.put(Image.RollOneR, image);
		
		image = grabImage(13,10,16,16);
		sprites.put(Image.RollTwoR, image);

		image = grabImage(14,10,16,16);
		sprites.put(Image.RollThreeR, image);
		
		image = grabImage(15,10,16,16);
		sprites.put(Image.RollFourR, image);
	
		image = grabImage(1,11,16,16);
		sprites.put(Image.RollOneD, image);
		
		image = grabImage(2,11,16,16);
		sprites.put(Image.RollTwoD, image);

		image = grabImage(3,11,16,16);
		sprites.put(Image.RollThreeD, image);
		
		image = grabImage(4,11,16,16);
		sprites.put(Image.RollFourD, image);

		image = grabImage(5,11,16,16);
		sprites.put(Image.RollOneU, image);
		
		image = grabImage(6,11,16,16);
		sprites.put(Image.RollTwoU, image);

		image = grabImage(7,11,16,16);
		sprites.put(Image.RollThreeU, image);
		
		image = grabImage(8,11,16,16);
		sprites.put(Image.RollFourU, image);

		image = grabImage(15,11,16,16);
		sprites.put(Image.RollOneL, image);
		
		image = grabImage(14,11,16,16);
		sprites.put(Image.RollTwoL, image);

		image = grabImage(13,11,16,16);
		sprites.put(Image.RollThreeL, image);
		
		image = grabImage(12,11,16,16);
		sprites.put(Image.RollFourL, image);
		
		image = grabImage(1,12,16,16);
		sprites.put(Image.Rock1, image);
		
		image = grabImage(1,13,16,16);
		sprites.put(Image.Rock2, image);
		
		image = grabImage(2,12,16,16);
		sprites.put(Image.Rock3, image);
		
		image = grabImage(2,13,16,16);
		sprites.put(Image.Rock4, image);
		
		image = grabImage(3,12,16,16);
		sprites.put(Image.Dish, image);
		
		image = grabImage(4,12,16,16);
		sprites.put(Image.Sattelite, image);
		
		image = grabImage(16,9,16,16);
		sprites.put(Image.Shimmer, image);
		
		image = grabImage(3,10,16,16);
		sprites.put(Image.Missile, image);
		
		image = grabImage(9	,11,16,16);
		sprites.put(Image.CheatMode, image);
		
		image = grabImage(10 ,11,16,16);
		sprites.put(Image.EasyMode, image);
		
		image = grabImage(11 ,11,16,16);
		sprites.put(Image.HardMode, image);
		
		image = grabImage(5 ,12,16,16);
		sprites.put(Image.Plaque, image);
		
		image = grabImage(3,13,16,16);
		sprites.put(Image.TileFloor, image);
		
		image = grabImage(4,13,16,16);
		sprites.put(Image.SandFloor, image);
		
		image = grabImage(1,3,16,16);
		sprites.put(Image.BasicEnemy1, image);
		
		image = grabImage(2,3,16,16);
		sprites.put(Image.BasicEnemy2, image);
		
		image = grabImage(5,13,16,16);
		sprites.put(Image.Torch, image);
		
		image = grabImage(6,13,16,16);
		sprites.put(Image.Torch2, image);
		
		image = grabImage(7,13,80,64);
		sprites.put(Image.Help, image);
		
		image = grabImage(8,4,16,16);
		sprites.put(Image.Gun, image);
		
		image = grabImage(6,12,16,16);
		sprites.put(Image.BasicHead, image);
		
		image = grabImage(7,12,16,16);
		sprites.put(Image.BasicArm, image);
		
		image = grabImage(8,12,16,16);
		sprites.put(Image.BasicBody, image);
		
		image = grabImage(9,12,16,16);
		sprites.put(Image.BasicLeg, image);
		
		image = grabImage(10,12,16,16);
		sprites.put(Image.TankHead, image);
		
		image = grabImage(11,12,16,16);
		sprites.put(Image.TankBlade, image);
		
		image = grabImage(12,12,16,16);
		sprites.put(Image.TankCog, image);
		
		image = grabImage(13,12,16,16);
		sprites.put(Image.TankBody, image);
		
		image = grabImage(11,13,16,16);
		sprites.put(Image.LazerHead, image);
		
		image = grabImage(12,13,16,16);
		sprites.put(Image.LazerHead, image);
		
		image = grabImage(13,13,16,16);
		sprites.put(Image.LazerBoost, image);
		
		image = grabImage(14,13,16,16);
		sprites.put(Image.LazerBoostDead, image);
		
		image = grabImage(7,2,16,16);
		sprites.put(Image.BasicHit, image);
		
		image = grabImage(8,2,16,16);
		sprites.put(Image.LazerHit, image);
		
		image = grabImage(9,2,16,16);
		sprites.put(Image.TankHit, image);
		
		image = grabImage(1,14,32,32);
		sprites.put(Image.BossHit, image);
		
		image = grabImage(16,10,16,16);
		sprites.put(Image.Lazer, image);
		
		image = grabImage(3,14,16,16);
		sprites.put(Image.Book, image);
		
		image = grabImage(4,14,16,16);
		sprites.put(Image.Fire, image);
		
		image = grabImage(14,12,16,16);
		sprites.put(Image.BrownWall, image);
		
	}

	public BufferedImage grabImage(int col, int row, int width, int height) {
		BufferedImage img = sprite.getSubimage((col * 16) - 16, (row * 16) - 16, width, height);
		return img;
	
	}

	public BufferedImage grabImage(Image image) {
		return sprites.get(image);
	}
}
