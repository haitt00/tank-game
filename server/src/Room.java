import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Room implements Runnable {
	private String id;
	private Map<String, Client> clients = new HashMap<String, Client>();
	private Map<String, ClientWriter> clientWriters = new HashMap<String, ClientWriter>();
	private boolean running = false;

	public static final int CLIENTS_PER_ROOM = 4;
	public static final int TEAMS_PER_ROOM = 2;

	public Room(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Set<String> getClientNames() {
		return clientWriters.keySet();
	}

	public boolean isRunning() {
		return running;
	}

	public Client findClient(String clientName) {
		return clients.get(clientName);
	}
	
	public synchronized void addClient(Client c) {
		clients.put(c.getName(), c);
	}

	public synchronized void addClientWriter(String clientName, ClientWriter cw) {
		clientWriters.put(clientName, cw);
	}

	public void removeClient(String clientName) {
		clients.remove(clientName);
		clientWriters.remove(clientName);
	}

	public boolean hasClient(Client c) {
		return clients.containsKey(c.getName());
	}

	public boolean isFull() {
		return clients.size() == CLIENTS_PER_ROOM;
	}

	public boolean isEmpty() {
		return clients.size() == 0;
	}

	public void startGame() throws IOException {
		String teamId = null;
		List<String> keys = new ArrayList<String>(clientWriters.keySet());
		Collections.shuffle(keys);

		for (int i = 0; i < keys.size(); i++) {
			ClientWriter cw = clientWriters.get(keys.get(i));
			teamId = String.valueOf(i % TEAMS_PER_ROOM + 1);
			clients.get(keys.get(i)).setTeamId(teamId);
			cw.sendPacket(Opcode.START_GAME, teamId);
		}
		running = true;
		System.out.println("Room#" + id + " starts game");
	}

	public void endGame() {
		running = false;
	}

	public void broadcast(String message) throws IOException {
		for (String key : clientWriters.keySet()) {
			clientWriters.get(key).sendPacket(message);
		}
	}

	public void broadcast(Opcode opcode, String message) throws IOException {
		for (String key : clientWriters.keySet()) {
			clientWriters.get(key).sendPacket(opcode, message);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Room))
			return false;

		Room r = (Room) o;
		return id.equals(r.getId());
	}

	@Override
	public void run() {
		System.out.println("Room#" + id + " runs");
		while (true) {
			synchronized (this) {
				if (isFull()) {
					try {
						startGame();
						break;
					} catch (IOException e) {
						System.out.println("Error starting game at room#" + id);
					}
				}
			}
		}

		while (running) {

		}
	}
}
