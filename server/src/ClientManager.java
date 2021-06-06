import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager {
	private List<Client> clients;
	private ExecutorService clientExecutor = Executors.newCachedThreadPool();

	private static final ClientManager SINGLETON = new ClientManager();

	private ClientManager() {
		clients = new ArrayList<Client>();
	}

	public static ClientManager singleton() {
		return SINGLETON;
	}

	public void acceptClient(Client c) {
		clientExecutor.execute(c);
	}

	public synchronized boolean addClient(Client c) {
		if (!hasClient(c)) {
			clients.add(c);
			return true;
		}
		return false;
	}

	public void removeClient(Client c) {
		clients.remove(c);
	}

	public void removeClientByName(String name) {
		Client c = findClientByName(name);
		removeClient(c);
	}

	public boolean hasClient(Client c) {
		return clients.contains(c);
	}

	public Client findClientByName(String name) {
		return clients.stream().filter(client -> client.getName().equals(name)).findFirst().orElse(null);
	}
}
