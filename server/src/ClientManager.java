import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager {
	private Map<String, Client> clients;
	private Map<String, ClientListener> clientListeners;
	private ExecutorService clientExecutor;

	public ClientManager() {
		this.clients = new HashMap<String, Client>();
		this.clientListeners = new HashMap<String, ClientListener>();
		this.clientExecutor = Executors.newCachedThreadPool();
	}

	public void executeClientListener(ClientListener cl) {
		clientExecutor.execute(cl);
	}
	
	public void registerClientListener(ClientListener cl) {
		clientListeners.put(cl.getClient().getName(), cl);
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
		System.out.println("Client#" + clientName + " has left");
	}

	public boolean hasClient(Client c) {
		return clients.containsKey(c.getName());
	}

	public Client findClient(String name) {
		return clients.get(name);
	}
	
	public ClientListener getClientListener(String clientName) {
		return clientListeners.get(clientName);
	}
}
