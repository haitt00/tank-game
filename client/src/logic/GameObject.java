package logic;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public abstract class GameObject {
	
	double x, y;
	double v;
	double a; //maybe not used yet
	
	protected JLabel img;
	
	public GameObject() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("/img/"+getImgString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract String getImgString();
}
