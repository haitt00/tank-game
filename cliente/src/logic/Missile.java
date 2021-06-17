package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Missile extends GameObject{
	Direction direction;
	double imgDimension = Configs.WALL_SIZE/2;
	public Missile(double x, double y, Game game, Direction d) {
		super(x, y, game);
		this.direction = d;
		size = Configs.WALL_SIZE/2;
		
		img = new ImageView(new Image("/img/missile.png", imgDimension, imgDimension, true, true));
		img.setRotate(d.getAngle());
		this.addOffSet();
		this.startUpdate();
		
	}
	private void startUpdate() {
		GameObject missile = this;
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
//				System.out.println("border: "+(Constants.GAME_SIZE - 4 * Configs.MISSILE_SPEED - imgDimension));
				while(true) 
				{
					if(!updatePos()) {
						break;
					}
//					System.out.println("x: "+x);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				game.removeGameObject(missile);
			}
		});
		t.start();
	}
	private boolean updatePos() {
//		System.out.println("update pos");
		double intendedPos;
		double newX = this.x;
		double newY = this.y;
		if(direction == Direction.UP) {
			newY = newY - Configs.MISSILE_SPEED;
			intendedPos = newY;
		}
		else if(direction == Direction.DOWN) {
			newY = newY + Configs.MISSILE_SPEED;
			intendedPos = newY;
		}
		else if(direction == Direction.RIGHT) {
			newX = newX + Configs.MISSILE_SPEED;
			intendedPos = newX;
		}
		else {
			newX = newX - Configs.MISSILE_SPEED;
			intendedPos = newX;		
		}
		if(game.checkCollision(this, newX, newY, this.direction).getNewPos()==intendedPos) {
//			System.out.println("no coll");
			x = newX; y = newY;
			relocate(x, y);
			return true;
		}
		else {
//			System.out.println("coll");
			return false;
		}
		
//		System.out.println("end");
	}
	private void addOffSet() {
		double offSetX = 0;
		double offSetY = 0;
		
		if(direction == Direction.UP) {
			offSetY = - Configs.TANK_SIZE / 2 - size / 2;
		}
		else if(direction == Direction.DOWN) {
			offSetY = Configs.TANK_SIZE / 2 + size / 2;
		}
		else if(direction == Direction.RIGHT) {
			offSetX = Configs.TANK_SIZE / 2 + size / 2;
		}
		else {
			offSetX = - Configs.TANK_SIZE / 2 - size / 2;
		}
		this.x += offSetX;
		this.y += offSetY;
	}

}
