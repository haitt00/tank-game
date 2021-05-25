import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room implements Runnable {
	private String id;
	private List<Client> team1 = new ArrayList<Client>();
	private List<Client> team2 = new ArrayList<Client>();
	private List<Client> clients = new ArrayList<Client>();
	private boolean running = false;

	public static final int MAX_CLIENTS_PER_ROOM = 4;

	public Room(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public List<Client> getClients() {
		return clients;
	}

	public List<Client> getTeam1() {
		return team1;
	}

	public List<Client> getTeam2() {
		return team2;
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
		return clients.size() == MAX_CLIENTS_PER_ROOM;
	}

	public boolean isEmpty() {
		return clients.size() == 0;
	}

	public void startGame() throws IOException {
		Collections.shuffle(clients);
		for (int i = 0; i < clients.size(); i++) {
			Client c = clients.get(i);
			if (i % 2 == 0)
				team1.add(c);
			else
				team2.add(c);
			c.sendMessage("START_GAME", String.valueOf(i % 2 + 1));
		}
		running = true;
		Server.log("Room#" + id + " starts game");
	}

	public void endGame() {
		running = false;
	}

	public void sendFromClientToOthers(Client client, String message) throws IOException {
		for (Client c : clients) {
			if (c.equals(client))
				continue;
			c.sendMessage(message);
		}
	}

	public void sendToClient(Client client, String message) throws IOException {
		for (Client c : clients) {
			if (c.equals(client)) {
				c.sendMessage(message);
				break;
			}
		}
	}

	public void broadcast(String message) throws IOException {
		for (Client c : clients) {
			c.sendMessage(message);
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
		Server.log("Room#" + id + " runs");
		while (true) {
			synchronized(this) {
				if (isFull()) {
					try {
						startGame();
						break;
					} catch (IOException e) {
						Server.log(e.getMessage());
					}
				}
			}
			
		}

		while (running) {

		}
	}
}
