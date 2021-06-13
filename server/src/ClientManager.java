import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager {
	private Map<String, ClientWriter> clientWriters;
	private ExecutorService clientExecutor;

	public ClientManager() {
		this.clientWriters = new HashMap<String, ClientWriter>();
		this.clientExecutor = Executors.newCachedThreadPool();
	}

	public void executeListener(ClientListener cl) {
		clientExecutor.execute(cl);
	}
	
	public void registerWriter(String clientName, ClientWriter cw) {
		clientWriters.put(clientName, cw);
	}
	
	public void unregisterWriter(String clientName) {
		clientWriters.remove(clientName);
	}
	
	public ClientWriter getClientWriter(String clientName) {
		return clientWriters.get(clientName);
	}

	public synchronized boolean addClient(Client c) {
		if (!hasClient(c)) {
			clientWriters.put(c.getName(), null);
			return true;
		}
		return false;
	}

	public void removeClient(String clientName) {
		clientWriters.remove(clientName);
		System.out.println("Client#" + clientName + " has left");
	}

	public boolean hasClient(Client c) {
		return clientWriters.containsKey(c.getName());
	}

	public ClientWriter findClient(String name) {
		return clientWriters.get(name);
	}
}
