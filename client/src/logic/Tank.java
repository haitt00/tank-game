package logic;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tank extends GameObject{
	
	public Tank() {
		super();
	}

	@Override
	protected String getImgString() {
		return "tank1.png";
	}
	
	public void turn(Direction d) {
		ImageIcon ii = this.getImg().getIcon();
		switch(d) {
		case UP:
			this.getImg().(oldLoc.x, oldLoc.y - Configs.TANK_SPEED);
			break;
		case DOWN:
			this.getImg().setLocation(oldLoc.x, oldLoc.y + Configs.TANK_SPEED);
			break;
		case RIGHT:
			this.getImg().setLocation(oldLoc.x + Configs.TANK_SPEED, oldLoc.y);
			break;
		case LEFT:
			this.getImg().setLocation(oldLoc.x - Configs.TANK_SPEED, oldLoc.y);
			break;
	}
	}
	public void move(Direction d) {
		Point oldLoc = this.getImg().getLocation();
		switch(d) {
			case UP:
				this.getImg().setLocation(oldLoc.x, oldLoc.y - Configs.TANK_SPEED);
				break;
			case DOWN:
				this.getImg().setLocation(oldLoc.x, oldLoc.y + Configs.TANK_SPEED);
				break;
			case RIGHT:
				this.getImg().setLocation(oldLoc.x + Configs.TANK_SPEED, oldLoc.y);
				break;
			case LEFT:
				this.getImg().setLocation(oldLoc.x - Configs.TANK_SPEED, oldLoc.y);
				break;
		}
	}
	
	public void setTrap() {
		Trap t = new Trap();
		Point loc = this.getImg().getLocation();
		game.addO(t.getImg(), loc.x, loc.y);
	}
	
	
	
}
