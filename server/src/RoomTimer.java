import java.io.IOException;

public class RoomTimer extends Thread {
	private Room room;
	private int seconds;
	private RoomManager roomManager;
	
	public RoomTimer(Room room, int seconds, RoomManager roomManager) {
		this.room = room;
		this.seconds = seconds;
		this.roomManager = roomManager;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < seconds; i++) {
				if (room.isFull()) {
					return;
				}
				System.out.println("Count: " + i);
				Thread.sleep(1000);
			}
			room.broadcast(Opcode.ERROR, "Room time out");
			room.endWaiting();
			roomManager.removeRoom(room.getId());
		} catch (InterruptedException e) {

		} catch (IOException e) {
			System.out.println("Error broadcasting at room#" + room.getId());
		} finally {
			System.out.println("Timer for room#" + room.getId() + " stops");
		}
	}
}
