package logic;

import javafx.scene.image.ImageView;

public class GameObject {
	double x, y;
	ImageView img;
	double size;
	public GameObject(double x, double y) {
		this.x = x;
		this.y = y;
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
	
}
