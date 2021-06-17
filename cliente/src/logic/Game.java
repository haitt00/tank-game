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
		selfTank = new Tank(padding, padding, this);
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
							j * Configs.WALL_SIZE + Configs.WALL_SIZE / 2, this));
				}
			}
		}
	}

	private int[][] getFakeWallMatrix() {
		return Configs.getFakeMap(2);
//		return Map.getRandomMap();
		
	}
	
	public double checkCollision(GameObject object, double newX, double newY, Direction direction){
		System.out.println("before check: "+newX+" "+newY);
		int boundCellStart, boundCellEnd;
		int boundSweepStart, boundSweepEnd;
		if(direction == Direction.UP) {
			boundCellStart = getGridIndex(object.x - object.size / 2, true);
			boundCellEnd = getGridIndex(object.x + object.size / 2, false);
			boundSweepStart = getGridIndex(object.y - object.size / 2, false);
			boundSweepEnd = getGridIndex(newY - object.size / 2, true);
			for (int i = boundSweepStart; i >= boundSweepEnd; i--) {
				for(int j = boundCellStart; j <= boundCellEnd; j++) {
					if(wallMatrix[j][i] == 1) {
						return getPosEdge(j, i, Direction.getOpposite(direction)) + object.size / 2;
					}
				}	
			}
		}
		else if(direction == Direction.DOWN) {
			boundCellStart = getGridIndex(object.x - object.size / 2, true);
			boundCellEnd = getGridIndex(object.x + object.size / 2, false);
			boundSweepStart = getGridIndex(object.y + object.size / 2, true);
			boundSweepEnd = getGridIndex(newY + object.size / 2, false);
			for (int i = boundSweepStart; i <= boundSweepEnd; i++) {
				for(int j = boundCellStart; j <= boundCellEnd; j++) {
					if(wallMatrix[j][i] == 1) {
						System.out.println("obstable at: "+j+" "+i);
						
						return getPosEdge(j, i, Direction.getOpposite(direction)) - object.size / 2;
					}
				}	
			}
		}
		else if(direction == Direction.RIGHT) {
			boundCellStart = getGridIndex(object.y - object.size / 2, true);
			boundCellEnd = getGridIndex(object.y + object.size / 2, false);
			boundSweepStart = getGridIndex(object.x + object.size / 2, true);
			boundSweepEnd = getGridIndex(newX + object.size / 2, false);
			for (int i = boundSweepStart; i <= boundSweepEnd; i++) {
				for(int j = boundCellStart; j <= boundCellEnd; j++) {
					if(wallMatrix[i][j] == 1) {
						System.out.println("obstable at: "+i+" "+j);
						return getPosEdge(i, j, Direction.getOpposite(direction)) - object.size / 2;
					}
				}	
			}
		}
		else {
			boundCellStart = getGridIndex(object.y - object.size / 2, true);
			boundCellEnd = getGridIndex(object.y + object.size / 2, false);
			boundSweepStart = getGridIndex(object.x - object.size / 2, false);
			boundSweepEnd = getGridIndex(newX - object.size / 2, true);
			for (int i = boundSweepStart; i >= boundSweepEnd; i--) {
				for(int j = boundCellStart; j <= boundCellEnd; j++) {
					if(wallMatrix[i][j] == 1) {
						System.out.println("obstable at: "+i+" "+j);
						return getPosEdge(i, j, Direction.getOpposite(direction)) + object.size / 2;
					}
				}	
			}
		}
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(wallMatrix[i][j]);
			}
			System.out.println();
		}
		
		if(direction==Direction.UP||direction==Direction.DOWN) {
			return newY;
		}
		else {
			return newX;
		}
		
	}
	private int[] getGridCell(int x, int y, boolean nextX, boolean nextY) {
		int[] result = new int[2];
		
		result[0] = getGridIndex(x, nextX);
		result[1] = getGridIndex(y, nextY);
		return result;
	}
	private double getPosEdge(int x, int y, Direction direction) {
		System.out.println("x: "+x);
		System.out.println("y: "+y);
		System.out.println("direction: "+direction.angle);
		if(direction == Direction.UP) {
			return Configs.WALL_SIZE * y;
		}
		else if(direction == Direction.DOWN) {
			return Configs.WALL_SIZE * (y+1);
		}
		else if(direction == Direction.RIGHT) {
			return Configs.WALL_SIZE * (x+1);
		}
		else if(direction == Direction.LEFT) {
			return Configs.WALL_SIZE * x;
		}
		return -1;
	}
	private int getGridIndex(double position, boolean next) {
		double beforeRound = position / Configs.WALL_SIZE;
		if(Math.abs(beforeRound - Math.floor(beforeRound)) < 0.0001) {
			if(!next) {
				return (int) Math.floor(position / Configs.WALL_SIZE) - 1;
			}
		}
		return (int) Math.floor(position / Configs.WALL_SIZE);
	}
}
