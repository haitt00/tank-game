import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager {
	private Map<String, Client> clients = new HashMap<String, Client>();
	private ExecutorService clientExecutor = Executors.newCachedThreadPool();

	private static final ClientManager SINGLETON = new ClientManager();

	private ClientManager() {
	}

	public static ClientManager singleton() {
		return SINGLETON;
	}

	public void acceptClient(Client c) {
		clientExecutor.execute(c);
	}

	public synchronized boolean addClient(Client c) {
		if (!hasClient(c)) {
			clients.put(c.getName(), c);
			return true;
		}
		return false;
	}

	public void removeClient(String clientName) {
		clients.remove(clientName);
	}

	public boolean hasClient(Client c) {
		return clients.containsKey(c.getName());
	}

	public Client findClient(String name) {
		return clients.get(name);
	}
}
