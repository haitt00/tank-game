package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Trap extends GameObject{
	Tank owner;
	public Trap(double x, double y, Game game, Tank owner) {
		super(x, y, game);
		this.owner = owner;
		owner.canSetTrap = false;
		img = new ImageView(new Image("/img/trap.png", Configs.TRAP_SIZE, Configs.TRAP_SIZE, true, true));
		size = Configs.TRAP_SIZE;
		
	}
	public void hide() {
		this.img.setVisible(false);
	}
	
	public void activate() {
		this.owner.canSetTrap = true;
		game.removeGameObject(this);
	}
}
