package crashRecovery.main;

import java.util.Random;

import crashRecovery.main.Game.STATE;

public class EndAnimation {
	private Random rand = new Random();
	private MapDecor ship;
	private int heightAdjust =4;
	private Handler handler;
	 
	public void endAnimation(Game game) {
		
		handler = game.getHandler();
		handler.setBlackScreenSatus(true);
		handler.clearBots();
		AudioPlayer.getSound("fixingSpaceshipSfx").play();
		AudioPlayer.getMusic(HUD.lastLevel).stop();
		AudioPlayer.getMusic("endMusic").loop();;
		// Add area 2 objects above area 1 into handler	
		for (int i = 0; i < handler.areaObjects.get(2).size(); i++) {
			GameObject tempObject = handler.areaObjects.get(2).get(i);
			tempObject.setY(tempObject.getY()-Game.HEIGHT-64);
			handler.addObject(tempObject);
		}
		handler.clearBots();
		// Empty floor above area 2
		handler.addObject(new Floor(0, -2*Game.HEIGHT-64, 0, 0, ID.Floor, Image.Floor, 1, game));
		ship = new MapDecor(Game.WIDTH/2-128, 600, ID.Ship, game,  Image.Ship,false);
		handler.addObject(ship);
		handler.removeObject(handler.getPlayer());
		handler.removeObject(handler.getPlayer(),2);
		for (int i = 0; i < handler.areaObjects.get(1).size(); i++) {
			GameObject tempObject = handler.areaObjects.get(1).get(i);
			if(tempObject.getId() == ID.CheckPoint) {
				CheckPoint point = (CheckPoint) tempObject;
				if(point.getImage() == Image.CrashedShip) {
					handler.removeObject(point);
				}
			}
		}
		for (int i = 0; i < 30; i++) {
			handler.addObject( new MenuParticle(rand.nextInt(Game.WIDTH), rand.nextInt(Game.HEIGHT),
					ID.MenuParticle, game));
		}
		for (int i = 0; i < 70; i++) {
			handler.storeLevel(0, new ParalaxParticle(rand.nextInt(Game.WIDTH), rand.nextInt(Game.HEIGHT),
					ID.MenuParticle, game));
		}
		
		
	}
	public void endAnimationTick(Handler handler) {
		for (int i = 0; i < handler.areaObjects.get(handler.cLvl).size(); i++) {
			GameObject tempObject = handler.areaObjects.get(handler.cLvl).get(i);
			if(tempObject.getId() != ID.Ship && tempObject.getId() != ID.MenuParticle) {
			tempObject.setY(tempObject.getY()+heightAdjust);
				if(tempObject.getY() > Game.HEIGHT * 2) {
					handler.removeObject(tempObject);
				}
			}
			else {
				if(rand.nextInt(20) == 1) {
					
				tempObject.setVelX(rand .nextInt(5)-2);
				}
				
			}
		}
		if(handler.Objects().size() < 100) {
			handler.removeObject(ship);
			Game.gameState = STATE.Menu;
		}
	}
}
