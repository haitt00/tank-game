package logic;

import javafx.scene.input.KeyCode;
import ui.GameScene;

public class Game {
	GameScene gameScene;
	Tank selfTank;
	int[][] wallMatrix;

	public Game() {

		wallMatrix = getFakeWallMatrix();

		gameScene = new GameScene();
		gameScene.setGame(this);

		generateWalls();
		double padding = Configs.TANK_SIZE / 2 + Configs.WALL_SIZE;
		selfTank = new Tank(padding, padding);
		addGameObject(selfTank);

	}

	public GameScene getGameScene() {
		return gameScene;
	}

	public void addGameObject(GameObject gameObject) {
		double topLeftXCoordinate = gameObject.getX() - gameObject.getSize() / 2;
		double topLeftYCoordinate = gameObject.getY() - gameObject.getSize() / 2;
//		System.out.println("set at: " + topLeftXCoordinate + " " + topLeftYCoordinate);

		gameScene.addImageView(gameObject.getImg(), topLeftXCoordinate, topLeftYCoordinate);
	}

	public void handleInput(KeyCode keyCode) {
		if (keyCode == Constants.KEY_UP) {
			selfTank.move(Direction.UP);
		} else if (keyCode == Constants.KEY_DOWN) {
			selfTank.move(Direction.DOWN);
		} else if (keyCode == Constants.KEY_RIGHT) {
			System.out.println("right");
			selfTank.move(Direction.RIGHT);
		} else if (keyCode == Constants.KEY_LEFT) {
			selfTank.move(Direction.LEFT);
		} else if (keyCode == Constants.KEY_FIRE) {
			// todo: handle fire
		} else if (keyCode == Constants.KEY_TRAP) {
			selfTank.setTrap();
		}
	}

	private void generateWalls() {
		for (int i = 0; i < Configs.GRID_EDGE; i++) {
			for (int j = 0; j < Configs.GRID_EDGE; j++) {
				if (wallMatrix[i][j] == 1) {
					addGameObject(new Wall(i * Configs.WALL_SIZE + Configs.WALL_SIZE / 2,
							j * Configs.WALL_SIZE + Configs.WALL_SIZE / 2));
				}
			}
		}
	}

	private int[][] getFakeWallMatrix() {
		return Configs.getFakeMap(4);
//		return Map.getRandomMap();
		
	}
}
