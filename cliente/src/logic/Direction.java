package logic;

public class Direction {
	double angle;
	
	public Direction(double angle) {
		super();
		this.angle = angle;
	}
	
	public double getAngle() {
		return angle;
	}

	public static final Direction UP = new Direction(0);
	public static final Direction DOWN = new Direction(180);
	public static final Direction RIGHT = new Direction(90);
	public static final Direction LEFT = new Direction(270);
	
	public static Direction getDirectionFromAngle(double angle) {
		if(angle==0) {
			return Direction.UP;
		}
		if(angle==180) {
			return Direction.DOWN;
		}
		if(angle==90) {
			return Direction.RIGHT;
		}
		if(angle==270) {
			return Direction.LEFT;
		}
		return null;
	}
}
