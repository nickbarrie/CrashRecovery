package crashRecovery.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;

import javax.swing.JFrame;

public class Window extends Canvas{

	private static final long serialVersionUID = 8068327555331429825L;
	
	public Window(int width, int height, String title, Game game) {
		JFrame frame = new JFrame(title);
		
		frame.setPreferredSize(new Dimension(width,height));
		frame.setMaximumSize(new Dimension(width,height));
		frame.setMinimumSize(new Dimension(width,height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();	
		}
}
