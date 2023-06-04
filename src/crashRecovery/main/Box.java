package crashRecovery.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import crashRecovery.main.Game.STATE;

public class Box {
	//Rectangle coords and size
	private int x,y,width, height;
	//Text within rectangle
	private  String word;
	//Rectangler color and text color
	private Color box,text;

	private Font font;

	public Box(int x, int y, int width, int height, String word, Color box, Color text,Font font) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.box = box;
		this.text = text;
		this.font = font;
		this.word = word;
		
	}
	// If the cursor is over a certain area
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if (mx > x && mx < x + width) {
			if (my > y && my < y + height) {
				return true;
			}
		}
		return false;
	}
	// Used when the mouse is over the box to change color
	public void setColors (int mx, int my) {
		
			 if (mouseOver(mx, my,x, y, width, height)) {
				box = Color.DARK_GRAY;
				text = Color.yellow;
			} else {
				box = Color.GRAY;
				text = Color.white;
			}
			 
	}
	// Allows for buttons to be reused with different text
	public void changeText(String change) {
		word = change;
	}
	// Draw rectangle and text
	public void render(Graphics g) {
	//	g.setColor(box);
	//	g.fillRect(x,y, width, height);
		g.setColor(text);
		
		g.drawString(word, x + 20, y + height/2);
	
	}
}
