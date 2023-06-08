/*
 * July 17th 2022
 * Nick Barrie 
 * Audio Player class contains sound effects and music
 * All Code was written long before July 17th 2022 but comments started then
 */
package crashRecovery.main;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class AudioPlayer {

	public static Map<String, Sound> soundMap = new HashMap<String, Sound>();
	public static Map<String, Music> musicMap = new HashMap<String, Music>();
	public static HashMap<Integer, String> levelMusic = new HashMap<Integer, String>();
	public static void load() {
		try {
			soundMap.put("click", new Sound("/res/blipSelect.ogg"));//menu noise
			soundMap.put("shot", new Sound("res/laserShoot.ogg"));// pistol shoot
			soundMap.put("explosion", new Sound("res/explosion.ogg"));// rpg
			soundMap.put("bossDeath", new Sound("res/explosion2.ogg"));// rpg
			soundMap.put("bossLazerShoot", new Sound("res/bossLazerShoot.ogg"));// rpg
			soundMap.put("bossTankSpawn", new Sound("res/bossTankSpawn.ogg"));// rpg
			soundMap.put("dodge", new Sound("res/dodge.ogg"));// rpg
			soundMap.put("enemyDeath", new Sound("res/enemydeath.ogg"));// rpg
			soundMap.put("fixingSpaceshipSfx", new Sound("res/fixingSpaceshipSfx.ogg"));
			soundMap.put("flamethrower", new Sound("res/flamethrower.ogg"));
			soundMap.put("hurt", new Sound("res/hurt.ogg"));
			soundMap.put("itemPickUp", new Sound("res/itemPickupNew.ogg"));
			soundMap.put("weaponPickUp", new Sound("res/weaponPickup.ogg"));
			soundMap.put("saw", new Sound("res/sawEnemySfx.ogg"));
			soundMap.put("slap", new Sound("res/slap.ogg"));
			soundMap.put("ammoPickUp", new Sound("res/ammoPickUp.ogg"));
			soundMap.put("hover", new Sound("res/bossHoverSfx.ogg"));
			soundMap.put("lazer", new Sound("res/spaceshipLaser.ogg"));
			soundMap.put("basicEnemyWalk", new Sound("res/basicEnemyWalk_01.ogg"));
			soundMap.put("basicEnemyWalk2", new Sound("res/basicEnemyWalk_02.ogg"));
			soundMap.put("cardScan", new Sound("res/cardScan.ogg"));
			soundMap.put("doorSlam", new Sound("res/doorSlamSfx.ogg"));
			soundMap.put("missileLaunch", new Sound("res/missile.ogg"));
			
			
			musicMap.put("lobbyIntro", new Music("res/lobbyMusicIntro.ogg"));// music
			musicMap.put("lobby", new Music("res/lobbyMusic.ogg"));// music
			musicMap.put("areaOne", new Music("res/area1music.ogg"));// music
			musicMap.put("areaTwo", new Music("res/area2music.ogg"));// music
			musicMap.put("areaThree", new Music("res/area3music.ogg"));// music
			musicMap.put("areaBoss", new Music("res/bossMusic.ogg"));// music
			musicMap.put("areaCave", new Music("res/caveMusic.ogg"));// music
			musicMap.put("buildUp", new Music("res/bossMusicBuildUP.ogg"));// music
			musicMap.put("endMusic", new Music("res/endingMusic.ogg"));// music
			musicMap.put("lose", new Music("res/youLoseMusic.ogg"));
			
			levelMusic.put(0, "lobbyIntro");
			levelMusic.put(1, "areaOne");
			levelMusic.put(2, "areaTwo");
			levelMusic.put(4, "areaThree");
			levelMusic.put(3, "areaBoss");
			levelMusic.put(5, "areaCave");
			levelMusic.put(6, "areaCave");
			levelMusic.put(7, "buildUp");
			levelMusic.put(8, "lobby");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get music
	public static Music getMusic(String key) {
		return musicMap.get(key);
	}
	public static Music getMusic(int level) {
		
		return musicMap.get(levelMusic.get(level));
	}
	//get sound effect
	public static Sound getSound(String key) {
		return soundMap.get(key);
	}
}
