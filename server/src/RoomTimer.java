
public class RoomTimer extends Thread {
	private Room room;
	private int seconds;
	private boolean running = false;

	public RoomTimer(Room room, int seconds) {
		this.room = room;
		this.seconds = seconds;
		this.running = true;
	}
	
	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < seconds; i++) {
//				System.out.println("Count: " + i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} finally {
			System.out.println("Timer for room#" + room.getId() + " stops");
			running = false;
		}
	}
}
