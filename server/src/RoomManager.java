import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomManager {
	private Map<String, Room> rooms = new HashMap<String, Room>();
	private ExecutorService roomExecutor = Executors.newCachedThreadPool();
	private int incRoomId = 0;

	private static final RoomManager SINGLETON = new RoomManager();

	private RoomManager() {
	}

	public static RoomManager singleton() {
		return SINGLETON;
	}

	public synchronized Room generateRoom() {
		Room r = new Room(String.valueOf(incRoomId++));
		addRoom(r);
		acceptRoom(r);
		return r;
	}

	private void addRoom(Room r) {
		rooms.put(r.getId(), r);
	}
	
	private void acceptRoom(Room r) {
		roomExecutor.execute(r);
	}

	public void removeRoom(String roomId) {
		rooms.remove(roomId);
	}
	
	public boolean hasRoom(Room r) {
		return rooms.containsKey(r.getId());
	}

	public Room findRoom(String roomId) {
		return rooms.get(roomId);
	}
}
