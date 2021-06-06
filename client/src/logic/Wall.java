package logic;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Wall extends GameObject{
	public Wall() {}

	@Override
	protected String getImgString() {
		return "wall.png";
	}
	
	
}
