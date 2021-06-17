package logic;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class GameObject {
	double x, y;
	ImageView img;
	double size;
	Game game;
	public GameObject(double x, double y, Game game) {
		this.x = x;
		this.y = y;
		this.game = game;
	}
	public ImageView getImg() {
		return img;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getSize() {
		return size;
	}
	protected void relocate(double x, double y) {
		double topLeftXCoordinate = x - size/2;
		double topLeftYCoordinate = y - size/2;
//			System.out.println("relocate: "+topLeftXCoordinate+" "+topLeftYCoordinate);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				img.relocate(topLeftXCoordinate, topLeftYCoordinate);
			}
		});
	}
	public double getUp() {
		return this.y - this.size / 2;
	}
	public double getDown() {
		return this.y + this.size / 2;
	}
	public double getRight() {
		return this.x + this.size / 2;
	}
	public double getLeft() {
		return this.x - this.size / 2;
	}
	
}
