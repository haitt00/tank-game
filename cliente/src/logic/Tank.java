package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tank extends GameObject{
	String name;
	String teamId;
	int lives;
	public Tank(double x, double y, Game game, String name, String teamId) {
		super(x, y, game);
		img = new ImageView(new Image("/img/tank1.png", Configs.TANK_SIZE, Configs.TANK_SIZE, true, true));
		size = Configs.TANK_SIZE;
		this.name = name;
		this.teamId = teamId;
		this.lives = Configs.MAX_LIVES;
		System.out.println(x+" "+y+" "+teamId+" "+name);
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
		if(d == Direction.UP) {
			y = game.checkCollision(this, x, y - Configs.TANK_SPEED, d).getNewPos();
		}
		else if (d == Direction.DOWN){
			y = game.checkCollision(this, x, y + Configs.TANK_SPEED, d).getNewPos();
		}
		else if (d == Direction.RIGHT){
			x = game.checkCollision(this, x + Configs.TANK_SPEED, y , d).getNewPos();
		}
		else {
			x = game.checkCollision(this, x - Configs.TANK_SPEED, y, d).getNewPos();
		}
		relocate(x, y);
		
	}
	public void setTrap() {
		Trap t = new Trap(this.x, this.y, this.game);
		game.addGameObject(t);
	}
	public void fire() {
		Missile m = new Missile(this.x, this.y, this.game, Direction.getDirectionFromAngle(this.img.getRotate()), this.teamId);
		game.addGameObject(m);
	}
	public void takeDam() {
		this.lives --;
		if(lives == 0) {
			game.removeGameObject(this);
		}
	}
}
