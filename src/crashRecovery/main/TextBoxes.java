package crashRecovery.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class TextBoxes {

	boolean drawn;
	
	private Font font = new Font("Silkscreen", 1,15);
	private boolean display = true;
	private int stage = 0;
	private int frame = 0;
	private int letter = 0;
	private boolean bounds = true;
	private int sentence = 0;
	private int level;
	private String[][] script = {{"Why did they shoot me down?", "I must do what I can to get off this planet.", "Press X to close text."},
			{"Its the nose cone from the ship.","I'll collect it to repair the ship."},
			{"This battery can power the ships interface"},
			{"Ship report:","Time of landing - 2067 January 18", "Planet -Unknown","Parts malfunctioning","Tail elevator, Ailerons, nose cone , weapon system"},
			{"This drill can help with repairs on the ship"},
			{"The ships weapon system is now functioning","Takeoff remains impossible","Press space on the ship to fire"},
			{"This card may still work on that scanner. "},
			{"A ratchet will help make the final repairs on the ship."},
			{"A Boulder is blocking the ladder"},
			{"When returning home after an exploration mission","the ship suddenly received fire from a nearby planet"}};
	;
	private String[] [] write = {{"", "",""},
			{"",""},
			{""},
			{"","","","",""},
			{""},
			{"","",""},
			{""},
			{""},
			{""},
			{"",""}};
	
	
	public void tick() {
		frame++;
		if(frame > 2 && bounds) {
			frame = 0;
			write[stage][sentence] += script[stage][sentence].charAt(letter);
			
			letter ++;
		
		}
		if(letter == script[stage][sentence].length()) {
			letter = 0;
			sentence ++;
		}
		if(sentence == script[stage].length) {
			bounds = false;
			sentence--;

		}
	}
	
	public void render(Graphics g) {
		g.setFont(font);
		if(display) {
			
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(Game.WIDTH/2-300, Game.HEIGHT-160 , 600, 20  + (sentence * 20) ,10,10);
			int line = 0;
			for(line = 0; line < sentence+1; line++) {
				g.setColor(Color.white);
				g.drawString(write[stage][line],  Game.WIDTH/2-290, Game.HEIGHT -145 + (line * 20));
			
			}
			g.drawString("(X)",  Game.WIDTH/2+270,Game.HEIGHT -162 + (line * 20));
		}
		
	}
	public void newText() {
		stage++;
		display = true;
		sentence = 0;
		letter = 0;
		bounds = true;
		
	}
	public void text(int level) {
		if(this.level != level) {
		this.level = level;
		for(int i = 0; i < write[level].length;i++) {
			write[level][i] = "";
		}
		stage = level;
		display = true;
		sentence = 0;
		letter = 0;
		bounds = true;
		}
	}
	public void clear() {
		display = false;
	}

}
