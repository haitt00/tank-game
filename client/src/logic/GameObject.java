package logic;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class GameObject {
	
	Game game;
	
	double x, y;
	double v;
	double a; //maybe not used yet
	
	protected JLabel img;
	
	public GameObject(Game game) {
		this.game = game;
		BufferedImage bfim = null;
		try {
			bfim = ImageIO.read(this.getClass().getResource("/img/"+getImgString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		img = new JLabel(new ImageIcon(bfim));
	}
	
	protected abstract String getImgString();

	public JLabel getImg() {
		return img;
	}
	
}
