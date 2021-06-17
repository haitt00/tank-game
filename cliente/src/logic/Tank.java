package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tank extends GameObject{
	public Tank(double x, double y, Game game) {
		super(x, y, game);
		img = new ImageView(new Image("/img/tank1.png", Configs.TANK_SIZE, Configs.TANK_SIZE, true, true));
		size = Configs.TANK_SIZE;
	}
	public void turn(Direction d) {
		img.setRotate(d.getAngle());
	}
	public void move(Direction d) {
		
		if(img.getRotate()!=d.getAngle()) {
			turn(d);
		}
		else {
			turn(d);
			translate(d);
		}
	}
	public void translate(Direction d) {
		System.out.println("pre: "+x+" "+y);
		if(d == Direction.UP) {
			y = game.checkCollision(this, x, y - Configs.TANK_SPEED, d);
			
//			y = y - Configs.TANK_SPEED;
//			System.out.println("pre: "+x+" "+y);
//			if(y < size/2) {
//				y = size/2;
//			}
//			System.out.println("post: "+x+" "+y);
		}
		else if (d == Direction.DOWN){
			y = game.checkCollision(this, x, y + Configs.TANK_SPEED, d);
		}
		else if (d == Direction.RIGHT){
			x = game.checkCollision(this, x + Configs.TANK_SPEED, y , d);
		}
		else {
			x = game.checkCollision(this, x - Configs.TANK_SPEED, y, d);
		}
//		if(d == Direction.DOWN) {
//			y = y + Configs.TANK_SPEED;
//			System.out.println("pre: "+x+" "+y);
//			if(y > Constants.GAME_SIZE - size/2) {
//				y = Constants.GAME_SIZE - size/2;
//			}
//			System.out.println("post: "+x+" "+y);
//		}
//		else if(d == Direction.RIGHT) {
//			x = x + Configs.TANK_SPEED;
//			System.out.println("pre: "+x+" "+y);
//			if(x > Constants.GAME_SIZE - size/2) {
//				x = Constants.GAME_SIZE - size/2;
//			}
//			System.out.println("post: "+x+" "+y);
//		}
//		else if(d == Direction.LEFT) {
//			x = x - Configs.TANK_SPEED;
//			System.out.println("pre: "+x+" "+y);
//			if(x < size/2) {
//				x = size/2;
//			}
//			System.out.println("post: "+x+" "+y);
//		}
		System.out.println("post: "+x+" "+y);
		relocate(x, y);
		
	}
	private void checkCollision() {
		
	}
	public void relocate(double x, double y) {
		double topLeftXCoordinate = x - size/2;
		double topLeftYCoordinate = y - size/2;
		System.out.println("relocate: "+topLeftXCoordinate+" "+topLeftYCoordinate);
		img.relocate(topLeftXCoordinate, topLeftYCoordinate);
	}
	public void setTrap() {
//		Trap t = new Trap();
//		Point loc = this.getImg().getLocation();
//		game.addO(t.getImg(), loc.x, loc.y);
	}
}
