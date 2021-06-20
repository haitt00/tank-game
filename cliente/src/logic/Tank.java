package logic;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.GameScene;

public class Tank extends GameObject{
	String name;
	String teamId;
	int lives;
	Trap justSetTrap;
	boolean canSetTrap = true;
	public Tank(double x, double y, Game game, String name, String teamId) {
		super(x, y, game);
		img = new ImageView(new Image("/img/tank1.png", Configs.TANK_SIZE, Configs.TANK_SIZE, true, true));
		size = Configs.TANK_SIZE;
		this.name = name;
		this.teamId = teamId;
		this.lives = Configs.MAX_LIVES;
		System.out.println(x+" "+y+" "+teamId+" "+name);
	}
	
	public int getLives() {
		return lives;
	}

	private void turn(Direction d) {
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
		updateStateHitTrap();
	}
	private void updateStateHitTrap() {
		ArrayList<Trap> traps = game.checkHitTrap(this);
		if(this.justSetTrap!=null) {
			if(traps==null||(!traps.contains(this.justSetTrap))) {
				this.justSetTrap = null;
			}
		}
		for (Trap trap: traps) {
			if(trap==this.justSetTrap) {
				continue;
			}
			System.out.println("HIT");
			trap.activate();
			this.takeDam();
			
		}
	}
	private void translate(Direction d) {
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
		if(this.canSetTrap) {
			Trap t = new Trap(this.x, this.y, this.game, this);
			if(!this.teamId.equals(game.getSelfTank().teamId)) {
				t.hide();
			}
			game.addGameObject(t);
			this.justSetTrap = t;
			System.out.println("TRAP SET");
		}
		
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
		game.gameScene.updateScoreBoard();
		if(game.checkEndGame()) {
			game.handleEndGame();
		}
	}
}
