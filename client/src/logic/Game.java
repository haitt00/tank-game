package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ui.GameScreen;

public class Game {
	Tank selfTank;
	ArrayList<Tank> otherTanks;
	
	GameScreen gameScreen;
	
	public Game() {
		gameScreen = new GameScreen(this);
		
		selfTank = new Tank();
//		BufferedImage tank = null;
//		try {
////			tank = ImageIO.read(this.getClass().getResource("/img/background.jpg"));
//			tank = ImageIO.read(this.getClass().getResource("/img/tank1.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////		Image scaled = bg.getScaledInstance(736, 736, Image.SCALE_SMOOTH);
//		JLabel bkg = new JLabel(new ImageIcon(tank));
//		Dimension size = bkg.getPreferredSize();
//		System.out.println(size);
//		bkg.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//		bkg.setBounds(7, 7, size.width, size.height);
//		gameScreen.getMainPanel().add(bkg);
		gameScreen.addGraphics(selfTank.getImg(), 7, 7);
		
	}
	
	public void handleInput(int keyCode) {
		switch(keyCode) {
			case Constants.KEY_UP:
				selfTank.move(Direction.UP);
				break;
			case Constants.KEY_DOWN:
				selfTank.move(Direction.DOWN);
				break;
			case Constants.KEY_RIGHT:
				selfTank.move(Direction.RIGHT);
				break;
			case Constants.KEY_LEFT:
				selfTank.move(Direction.LEFT);
				break;
			case Constants.KEY_FIRE:
				//todo: handle fire
				break;
			case Constants.KEY_TRAP:
				selfTank.setTrap();
				break;
				
		}
	}
	
	public void addObject(GameObject obj, int x, int y) {
		gameScreen.addGraphics(obj.getImg(), x, y);
	}
	public static void main(String[] args) {
		new Game();
	}
}
