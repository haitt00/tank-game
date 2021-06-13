import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomManager {
	private int maxRooms = 3;
	private Map<String, Room> rooms = new HashMap<String, Room>();
	private ExecutorService roomExecutor = Executors.newCachedThreadPool();
	private int incRoomId = 0;

	public RoomManager() {
		this.rooms = new HashMap<String, Room>();
		this.roomExecutor = Executors.newCachedThreadPool();
	}

	public synchronized Room generateRoom() {
		if (rooms.size() < maxRooms) {
			Room r = new Room(String.valueOf(incRoomId++));
			addRoom(r);
			executeRoom(r);
			return r;
		}
		return null;
	}

	public void addRoom(Room r) {
		rooms.put(r.getId(), r);
	}
	
	public void executeRoom(Room r) {
		roomExecutor.execute(r);
	}

	public void removeRoom(String roomId) {
		rooms.remove(roomId);
		System.out.println("Destroy room#" + roomId);
	}
	
	public boolean hasRoom(Room r) {
		return rooms.containsKey(r.getId());
	}

	public Room findRoom(String roomId) {
		return rooms.get(roomId);
	}
}
