package logic;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.input.KeyCode;
import network.Client;
import ui.GameScene;

public class Game {
	GameScene gameScene;
	HashMap<String, Tank> tanks = new HashMap<String, Tank>();
	int[][] wallMatrix;
	boolean over;

	public Game() {

		wallMatrix = getFakeWallMatrix();

		gameScene = new GameScene();
		gameScene.setGame(this);

		generateWalls();
		generateTanks();
		
	}

	public GameScene getGameScene() {
		return gameScene;
	}

	public Tank getSelfTank() {
		return this.tanks.get(Client.getInstance().getName());
	}
	public void generateTanks() {
		
		double padding = Configs.TANK_SIZE / 2 + Configs.WALL_SIZE;
		ArrayList<String> names = Client.getInstance().getPlayerNames();
		int count = 0;
		Tank t = null;
		for(String name: names){
			switch(count) {
			case 0:
				t = new Tank(padding, padding, this, name, "1");
				break;
			case 1:
				t = new Tank(Constants.GAME_SIZE - padding, padding, this, name, "1");
				break;
			case 2:
				t = new Tank(Constants.GAME_SIZE - padding, Constants.GAME_SIZE - padding, this, name, "2");
				break;
			case 3:
//				return;
				t = new Tank(padding, Constants.GAME_SIZE - padding, this, name, "2");
				break;
			}
			
			this.tanks.put(name, t);
			this.addGameObject(t);
			count++;
		}
			
	}
	public void addGameObject(GameObject gameObject) {
		double topLeftXCoordinate = gameObject.getX() - gameObject.getSize() / 2;
		double topLeftYCoordinate = gameObject.getY() - gameObject.getSize() / 2;
//		System.out.println("set at: " + topLeftXCoordinate + " " + topLeftYCoordinate);

		gameScene.addImageView(gameObject.getImg(), topLeftXCoordinate, topLeftYCoordinate);
	}
	
	public void removeGameObject(GameObject gameObject) {
		gameScene.removeImageView(gameObject.getImg());
	}

	public void handleInput(String name, KeyCode keyCode) {
		Tank tank = this.tanks.get(name);
		if (keyCode == Constants.KEY_UP) {
			tank.move(Direction.UP);
		} else if (keyCode == Constants.KEY_DOWN) {
			tank.move(Direction.DOWN);
		} else if (keyCode == Constants.KEY_RIGHT) {
			tank.move(Direction.RIGHT);
		} else if (keyCode == Constants.KEY_LEFT) {
			tank.move(Direction.LEFT);
		} else if (keyCode == Constants.KEY_FIRE) {
			tank.fire();
		} else if (keyCode == Constants.KEY_TRAP) {
			tank.setTrap();
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
	
	public CollisionDetail checkCollision(GameObject object, double newX, double newY, Direction direction){
//		System.out.println("before check: "+newX+" "+newY);
		int boundCellStart, boundCellEnd;
		int boundSweepStart, boundSweepEnd;
		if(direction == Direction.UP) {
			boundCellStart = getGridIndex(object.x - object.size / 2, true);
			boundCellEnd = getGridIndex(object.x + object.size / 2, false);
			boundSweepStart = getGridIndex(object.y - object.size / 2, false);
			boundSweepEnd = getGridIndex(newY - object.size / 2, true);
			for (int i = boundSweepStart; i >= boundSweepEnd; i--) {
				for(int j = boundCellStart; j <= boundCellEnd; j++) {
					GameObject tankCollided = this.checkGridHasTank(j, i);
					if(wallMatrix[j][i] == 1 || (tankCollided!=null)) {
						double newPos = getPosEdge(j, i, Direction.getOpposite(direction)) + object.size / 2;
						return new CollisionDetail(newPos, tankCollided);
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
					GameObject tankCollided = this.checkGridHasTank(j, i);
					if(wallMatrix[j][i] == 1 || (tankCollided!=null)) {
						double newPos = getPosEdge(j, i, Direction.getOpposite(direction)) - object.size / 2;
						return new CollisionDetail(newPos, tankCollided);
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
					GameObject tankCollided = this.checkGridHasTank(i, j);
					if(wallMatrix[i][j] == 1 || (tankCollided!=null)) {
						double newPos = getPosEdge(i, j, Direction.getOpposite(direction)) - object.size / 2;
						return new CollisionDetail(newPos, tankCollided);
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
					GameObject tankCollided = this.checkGridHasTank(i, j);
					if(wallMatrix[i][j] == 1 || (tankCollided!=null)) {
						double newPos = getPosEdge(i, j, Direction.getOpposite(direction)) + object.size / 2;
						return new CollisionDetail(newPos, tankCollided);
					}
				}	
			}
		}
		
		if(direction==Direction.UP||direction==Direction.DOWN) {
			return new CollisionDetail(newY, null);
		}
		else {
			return new CollisionDetail(newX, null);
		}
		
	}
	private int[] getGridCell(int x, int y, boolean nextX, boolean nextY) {
		int[] result = new int[2];
		
		result[0] = getGridIndex(x, nextX);
		result[1] = getGridIndex(y, nextY);
		return result;
	}
	private Tank checkGridHasTank(int x, int y) {
		double xPoint = Configs.WALL_SIZE * (x+0.5);
		double yPoint = Configs.WALL_SIZE * (y+0.5);
		for(Tank tank: this.tanks.values()) {
			if((Math.abs(xPoint - tank.x) <= tank.size/2)&&(Math.abs(yPoint - tank.y) <= tank.size/2)) {
				return tank;
			}
		}
		return null;
	}
	private double getPosEdge(int x, int y, Direction direction) {
//		System.out.println("x: "+x);
//		System.out.println("y: "+y);
//		System.out.println("direction: "+direction.angle);
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
