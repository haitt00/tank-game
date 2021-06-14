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
		
		selfTank = new Tank(Configs.TANK_SIZE/2, Configs.TANK_SIZE/2);
		addGameObject(selfTank);
		
	}
		
	public GameScene getGameScene() {
		return gameScene;
	}
	public void addGameObject(GameObject gameObject) {
		double topLeftXCoordinate = gameObject.getX() - gameObject.getSize()/2;
		double topLeftYCoordinate = gameObject.getY() - gameObject.getSize()/2;
		System.out.println("set at: "+topLeftXCoordinate+" "+topLeftYCoordinate);
		
		gameScene.addImageView(gameObject.getImg(),  topLeftXCoordinate, topLeftYCoordinate);
	}
	
	public void handleInput(KeyCode keyCode) {
		if(keyCode==Constants.KEY_UP) {
			selfTank.move(Direction.UP);
		}
		else if(keyCode==Constants.KEY_DOWN) {
			selfTank.move(Direction.DOWN);
		}
		else if(keyCode==Constants.KEY_RIGHT) {
			System.out.println("right");
			selfTank.move(Direction.RIGHT);
		}
		else if(keyCode==Constants.KEY_LEFT) {
			selfTank.move(Direction.LEFT);
		}
		else if(keyCode==Constants.KEY_FIRE) {
			//todo: handle fire
		}
		else if(keyCode==Constants.KEY_TRAP) {
			selfTank.setTrap();
		}
	}
	private void generateWalls() {
		for (int i = 0; i < Configs.GRID_EDGE; i++) {
			for (int j = 0; j < Configs.GRID_EDGE; j++) {
				if(wallMatrix[i][j]==1) {
					addGameObject(new Wall(i * Configs.WALL_SIZE + Configs.WALL_SIZE/2, j * Configs.WALL_SIZE + Configs.WALL_SIZE/2));
				}
			}
		}
	}
	private int[][] getFakeWallMatrix(){
		int[][] wallMatrix = new int[Configs.GRID_EDGE][Configs.GRID_EDGE];
		for (int i = 0; i < Configs.GRID_EDGE; i++) {
			for (int j = 0; j < Configs.GRID_EDGE; j++) {
				if(i == 0 || i == Configs.GRID_EDGE - 1 || j == 0 || j == Configs.GRID_EDGE - 1) {
					
				}
				else {
					wallMatrix[i][j] = 0;
				}
			}
		}
//		for (int i = 0; i <= 4; i++) {
//			for (int j = 0; j <=4 ; j++) {
//				if(i == j + 1) {
//					wallMatrix[i][j] = 1;
//				}
//				else {
//					wallMatrix[i][j] = 0;
//				}
//			}
//		}
//		for (int i = 5; i <= 9; i++) {
//			for (int j = 5; j <= 9; j++) {
//				if(i == j - 1) {
//					wallMatrix[i][j] = 1;
//				}
//			}
//		}
		return wallMatrix;
	}
}
