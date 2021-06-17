package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wall extends GameObject{

	public Wall(double x, double y, Game game) {
		super(x, y, game);
		img = new ImageView(new Image("/img/wall.png", Configs.WALL_SIZE, Configs.WALL_SIZE, true, true));
		size = Configs.WALL_SIZE;
	}

}
