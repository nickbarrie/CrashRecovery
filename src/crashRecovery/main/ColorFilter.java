package crashRecovery.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class ColorFilter extends GameObject{
	private Color color;
	private int width,height;
	private float trans = .6f;
	public ColorFilter(float x, float y,int width,int height, ID id, Game game,Color color) {
		super(x, y, id, game);
		this.color = color;
		this.width = width;
		this.height = height;
	}

	public ColorFilter(float x, float y,int width,int height, ID id, Game game,Color color,Float trans) {
		super(x, y, id, game);
		this.color = color;
		this.width = width;
		this.height = height;
		this.trans = trans;
	}

	public void tick() {
		
		
	}


	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(trans));
		g.setColor(color);
		g.fillRect((int)0,(int) 0, width,height);
		g2d.setComposite(makeTransparent(1));
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(0,0,0,0);
	}


	public void updateObject(int level) {
		// TODO Auto-generated method stub
		
	}
	

}
