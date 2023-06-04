package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import crashRecovery.main.Game.STATE;

public class Handler {

	LinkedList<GameObject> renderFore = new LinkedList<GameObject>();
	LinkedList<GameObject> renderMid = new LinkedList<GameObject>();
	LinkedList<GameObject> renderBack = new LinkedList<GameObject>();
	ArrayList<LinkedList<GameObject>> areaObjects = new ArrayList<LinkedList<GameObject>>();
	LinkedList<GameObject> collisionObjects = new LinkedList<GameObject>();
	Player player;
	int cLvl = 0;
	private EndAnimation end = new EndAnimation();
	HashMap<Integer, Integer> playCoordsx = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> playCoordsy = new HashMap<Integer, Integer>();
	private Game game;
	private boolean blackScreen = false;
	private long timeBlack = 0;
	private ColorFilter filter;

	public Handler(Game game) {
		this.game = game;
		for (int level = 0; level < 9; level++) {

			LinkedList<GameObject> temp = new LinkedList<GameObject>();

			this.areaObjects.add(level, temp);
		}
	}

	public void tick() {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			try {
			GameObject tempObject = areaObjects.get(cLvl).get(i);
				
				tempObject.tick();
			} catch (Exception e) {

			}
		}
	}
	public void clearLevel(int level) {
		areaObjects.get(level).clear();
	}
	public void visibleObjectsRendered() {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);

			if (player.sight(tempObject)) {

				if (!renderFore.contains(tempObject) && !renderBack.contains(tempObject)
						&& !renderMid.contains(tempObject)) {
					addRenderObject(tempObject);
				}
			} else {
				if (renderFore.contains(tempObject) || renderBack.contains(tempObject)
						|| renderMid.contains(tempObject)) {
					removeRenderObject(tempObject);
				}
			}
			if (tempObject.getId() == ID.Filter) {
				addRenderObject(tempObject);
			}

		}
	}

	public void blackScreen(Graphics g, int time) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		if (System.currentTimeMillis() - timeBlack > time * 2000) {
			setBlackScreenSatus(false);
		}

	}

	public void render(Graphics g) {
		for (int i = 0; i < renderBack.size(); i++) {
			try {
				GameObject tempObject = renderBack.get(i);

				tempObject.render(g);
			} catch (Exception e) {

			}

		}
		for (int i = 0; i < renderMid.size(); i++) {
			try {
				GameObject tempObject = renderMid.get(i);

				tempObject.render(g);
			} catch (Exception e) {

			}

		}
		for (int i = 0; i < renderFore.size(); i++) {
			try {
				GameObject tempObject = renderFore.get(i);

				tempObject.render(g);
			} catch (Exception e) {

			}
		}
		if (filter != null) {
			filter.render(g);
		}
	}

	public void clearEnemys() {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);
			if (tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.TankEnemy || tempObject.getId() == ID.LazerBot || tempObject.getId() == ID.BossEnemy) {
				tempObject.explode();
				removeObject(tempObject);

			}
		}
	}

	public void clearBots() {
		for (int k = 0; k < areaObjects.size(); k++) {
			for (int i = 0; i < areaObjects.get(k).size(); i++) {
				GameObject tempObject = areaObjects.get(k).get(i);
				if (tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.TankEnemy) {
					removeObject(tempObject);

				}
			}
		}
	}

	public GameObject getWall(int image) {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);
			if (tempObject.getId() == ID.Wall) {
				Wall plat = (Wall) tempObject;
				if (plat.getImage() == image) {
					return plat;
				}
			}
		}
		return null;
	}

	public boolean collisionObject(GameObject obj) {
		if (obj.getId() != ID.Particle && obj.getId() != ID.Floor && obj.getId() != ID.Trail
				&& obj.getId() != ID.MissileParticle && obj.getId() != ID.MenuParticle && obj.getId() != ID.Decor
				&& obj.getId() != ID.Rock && obj.getId() != ID.Gun && obj.getId() != ID.Bullet
				&& obj.getId() != ID.BossBullet) {
			return true;
		}
		return false;
	}

	public void addObject(GameObject object) {
		if (collisionObject(object)) {
			collisionObjects.add(object);
		}
		this.areaObjects.get(cLvl).add(object);
		if (checkFore(object)) {
			renderFore.add(object);
		}
		if (checkMid(object)) {
			renderMid.add(object);
		} else {
			renderBack.add(object);
		}
	}

	public void addRenderObject(GameObject object) {
		if (object.getId() == ID.Filter) {
			filter = (ColorFilter) object;
		}
		if (checkFore(object)) {
			renderFore.add(object);
		}
		if (checkMid(object)) {
			renderMid.add(object);
		} else {
			renderBack.add(object);
		}
	}

	public boolean checkEnemy() {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);
			if (tempObject.getId() == ID.BasicEnemy) {
				return false;
			}

		}
		return true;
	}

	public boolean checkSpawner() {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);

			if (tempObject.getId() == ID.ObjectSpawner) {
				ObjectSpawner temp = (ObjectSpawner) tempObject;
				if (!temp.itemSpawn()) {
					return false;
				}

			}

		}
		return true;
	}

	public GameObject getCheckpoint(int area, Image image) {
		for (int i = 0; i < areaObjects.get(area).size(); i++) {
			GameObject tempObject = areaObjects.get(area).get(i);
			if (tempObject.getId() == ID.CheckPoint) {
				CheckPoint temp = (CheckPoint) tempObject;
				if (temp.getImage() == image) {
					return temp;
				}
			}
		}
		return null;
	}

	public void createPlayer(int x, int y, Game game) {
		if (player == null) {
			this.player = new Player(x, y, ID.Player, this, game);
		} else {
			this.player.setX(x);
			this.player.setY(y);
		}
	}
	public void createPlayer(int x, int y,int level, Game game) {
		if (player == null) {
			this.player = new Player(x, y, ID.Player, this, game);
		} else {
			playCoordsx.put(level, x);
			playCoordsy.put(level, y);
		}
	}
	public Player getPlayer() {
		return player;
	}

	public int enemyCount() {
		int enemies = 0;
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);
			if (tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.TankEnemy
					|| tempObject.getId() == ID.LazerBot || tempObject.getId() == ID.BossEnemy) {
				enemies++;

			}
		}
		return enemies;
	}

	public void levelComp() {
		for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
			GameObject tempObject = areaObjects.get(cLvl).get(i);
			if (tempObject.getId() == ID.AreaFlag) {
				AreaFlag flag = (AreaFlag) tempObject;
				flag.enableFlag();
			}
		}
	}

	

	public boolean checkFore(GameObject object) {
		return (object.getId() == ID.BasicEnemy || object.getId() == ID.TankEnemy || object.getId() == ID.Player
				|| object.getId() == ID.BossEnemy || object.getId() == ID.LazerBot || object.getId() == ID.Particle);
	}

	public boolean checkMid(GameObject object) {
		return (object.getId() == ID.CheckPoint || object.getId() == ID.FlameDrop || object.getId() == ID.BulletDrop
				|| object.getId() == ID.MissileDrop || object.getId() == ID.HealthDrop || object.getId() == ID.Decor);
	}

	public void foreBack(GameObject object) {
		if (collisionObject(object)) {
			collisionObjects.add(object);
		}
		if (checkFore(object)) {
			renderFore.add(object);
		}
		if (checkMid(object)) {
			renderMid.add(object);
		} else {
			renderBack.add(object);
		}
	}

	public int size() {
		return areaObjects.get(cLvl).size();
	}

	public ColorFilter getFilter(int level) {
		for (int i = 0; i < areaObjects.get(level).size(); i++) {
			GameObject tempObject = areaObjects.get(level).get(i);
			if (tempObject.getId() == ID.Filter) {
				return (ColorFilter) tempObject;
			}

		}
		return null;
	}

	public void loadArea(int level) {
		int tempLvl = cLvl;
		if(cLvl != 5 && level != 6) {
		if (AudioPlayer.getMusic(tempLvl).playing()) {
			AudioPlayer.getMusic(tempLvl).stop();
		}
		if(level == 1) {
			game.textbox.text(0);
		}
		if(level == 8) {
			game.textbox.text(9);
		}
		if (level == 3) {
			AudioPlayer.getSound("doorSlam").play();
		}
		if (Game.gameState == STATE.End) {
			AudioPlayer.getMusic("lose").loop(1f, Game.musicVolume / 2);
		} else if (Game.gameState != STATE.Survival) {
			AudioPlayer.getMusic(level).loop(1, Game.musicVolume);
		}
		}
		renderFore.clear();
		renderMid.clear();
		renderBack.clear();
		collisionObjects.clear();
		filter = null;
		for (int i = 0; i < areaObjects.get(level).size(); i++) {
			GameObject tempObject = areaObjects.get(level).get(i);
			if (tempObject.getId() == ID.Wall) {
				tempObject.updateObject(level);
			}
			if (tempObject.getId() == ID.FloorDecor) {
				tempObject.updateObject(level);
			}

		}
		for (int i = 0; i < areaObjects.get(level).size(); i++) {
			GameObject tempObject = areaObjects.get(level).get(i);
			foreBack(tempObject);

		}

		filter = getFilter(level);

		cLvl = level;

	}

	public int getLvl() {
		return cLvl;
	}

	public void setBlackScreenSatus(boolean set) {
		if (set) {
			timeBlack = System.currentTimeMillis();
		}
		blackScreen = set;
	}

	public boolean getBlackScreenSatus() {
		return blackScreen;
	}

	public LinkedList<GameObject> Objects() {
		return areaObjects.get(cLvl);
	}

	public LinkedList<GameObject> getCollisionObjects() {

		return collisionObjects;
	}

	public void clear() {
		for (int i = 1; i < areaObjects.size(); i++) {
			areaObjects.get(i).clear();
		}

	}

	public void addPlayer(int level) {
		for (int i = 1; i < areaObjects.get(level).size(); i++) {
			GameObject tempObject = areaObjects.get(level).get(i);
			if (tempObject.getId() == ID.Player) {
				return;
			}

		}

		areaObjects.get(level).add(player);

	}

	public void playerCoords(int level, int x, int y) {
		playCoordsx.put(level, x);
		playCoordsy.put(level, y);
		addPlayer(level);
	}

	public void storeLevel(int level, GameObject obj) {
		if (obj.getId() == ID.Filter) {
			filter = (ColorFilter) obj;
		}
		if (level != cLvl) {

			this.areaObjects.get(level).add(obj);
		} else {
			addObject(obj);
		}
	}

	public void endAnimation() {
		end.endAnimation(game);
	}

	public void endAnimationTick() {
		end.endAnimationTick(this);
	}

	public void removeObject(GameObject object) {
		
		if (object == null) {
			
			return;
		}
		this.areaObjects.get(cLvl).remove(object);
		if (renderFore.contains(object)) {
			renderFore.remove(object);
		}
		if (renderMid.contains(object)) {
			renderMid.remove(object);
		}
		if (renderBack.contains(object)) {
			renderBack.remove(object);
		}
		if (collisionObjects.contains(object)) {
			collisionObjects.remove(object);
			if(object.getId() == ID.Wall) {
				for (int i = 0; i < areaObjects.get(cLvl).size(); i++) {
					GameObject tempObject = areaObjects.get(cLvl).get(i);
					if (tempObject.getId() == ID.Wall) {
						tempObject.updateObject(cLvl);
					}

				}
			}
		}
		if (object.getId() == ID.Filter) {
			filter = null;
		}
	}

	public void removeRenderObject(GameObject object) {
		if (renderFore.contains(object)) {
			renderFore.remove(object);
		}
		if (renderMid.contains(object)) {
			renderMid.remove(object);
		}
		if (renderBack.contains(object)) {
			renderBack.remove(object);
		}

		if (object.getId() == ID.Filter) {
			filter = null;
		}
	}

	public void removeObject(GameObject object, int level) {
		this.areaObjects.get(level).remove(object);
		if (renderFore.contains(object)) {
			renderFore.remove(object);
		}
		if (renderMid.contains(object)) {
			renderMid.remove(object);
		}
		if (renderBack.contains(object)) {
			renderBack.remove(object);
		}
		if (collisionObjects.contains(object)) {
			collisionObjects.remove(object);
		}
		if (object.getId() == ID.Filter) {
			filter = null;
		}
	}
}
