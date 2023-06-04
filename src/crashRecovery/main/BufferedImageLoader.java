/*
 * July 21st 2022
 * Nick Barrie 
 * Image loader class for textures
 */
package crashRecovery.main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {
	BufferedImage image;

	public BufferedImage loadImage(String path) {
		try {
			System.out.println(path);
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return image;
	}
}
