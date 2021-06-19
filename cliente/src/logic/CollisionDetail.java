package logic;

public class CollisionDetail {
	double newPos;
	GameObject coolisionTarget;
	
	public CollisionDetail(double newPos, GameObject coolisionTarget) {
		super();
		this.newPos = newPos;
		this.coolisionTarget = coolisionTarget;
	}
	public double getNewPos() {
		return newPos;
	}
	public GameObject getCoolisionTarget() {
		return coolisionTarget;
	}
	
}
