package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Trap extends GameObject{

	public Trap(double x, double y, Game game) {
		super(x, y, game);
		img = new ImageView(new Image("/img/trap.png", Configs.TRAP_SIZE, Configs.TRAP_SIZE, true, true));
		size = Configs.TRAP_SIZE;
	}
	public void hide() {
		this.img.setVisible(false);
	}
	

}
