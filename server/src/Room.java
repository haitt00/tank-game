import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room implements Runnable {
	private String id;
	private List<Client> clients = new ArrayList<Client>();
	private boolean running = false;

	public static final int CLIENTS_PER_ROOM = 4;
	public static final int TEAMS_PER_ROOM = 2;

	public Room(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public List<Client> getClients() {
		return clients;
	}

	public boolean isRunning() {
		return running;
	}

	public Client findClientByName(String name) {
		return clients.stream().filter(client -> name.equals(client.getName())).findFirst().orElse(null);
	}

	public synchronized void addClient(Client client) {
		clients.add(client);
	}

	public void removeClient(Client client) {
		clients.remove(client);
	}

	public boolean hasClient(Client client) {
		return clients.contains(client);
	}

	public boolean isFull() {
		return clients.size() == CLIENTS_PER_ROOM;
	}

	public boolean isEmpty() {
		return clients.size() == 0;
	}

	public void startGame() throws IOException {
		Collections.shuffle(clients);
		for (int i = 0; i < clients.size(); i++) {
			Client c = clients.get(i);
			c.setTeam(i % TEAMS_PER_ROOM + 1);
			c.sendPacket("START_GAME", String.valueOf(i % TEAMS_PER_ROOM + 1));
		}
		running = true;
		System.out.println("Room#" + id + " starts game");
	}

	public void endGame() {
		running = false;
	}

	public void broadcast(String message) throws IOException {
		for (Client c : clients) {
			c.sendPacket(message);
		}
	}
	
	public void broadcast(String opcode, String message) throws IOException {
		for (Client c : clients) {
			c.sendPacket(opcode, message);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Room))
			return false;

		Room r = (Room) o;
		return id == r.getId();
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
						System.out.println("Error happens when starting game at room#" + id);
					}
				}
			}

		}

		while (running) {

		}
	}
}
