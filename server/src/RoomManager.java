import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomManager {
	private List<Room> rooms;
	private int incrementId = 0;
	private ExecutorService roomExecutor = Executors.newCachedThreadPool();

	private static final RoomManager SINGLETON = new RoomManager();

	private RoomManager() {
		rooms = new ArrayList<Room>();
	}

	public static RoomManager singleton() {
		return SINGLETON;
	}

	public synchronized Room generateRoom() {
		Room r = new Room(String.valueOf(incrementId++));
		addRoom(r);
		startRoom(r);
		return r;
	}

	private void addRoom(Room r) {
		rooms.add(r);
	}
	
	private void startRoom(Room r) {
		roomExecutor.execute(r);
	}

	public void removeRoom(Room r) {
		rooms.remove(r);
	}

	public void removeRoom(String roomId) {
		Room r = findRoom(roomId);
		removeRoom(r);
	}

	public Room findRoom(String roomId) {
		return rooms.stream().filter(r -> roomId.equals(r.getId())).findFirst().orElse(null);
	}
}
