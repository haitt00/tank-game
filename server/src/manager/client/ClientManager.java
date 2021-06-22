package manager.client;
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

	public ClientWriter getClientWriter(String clientName) {
		return clientWriters.get(clientName);
	}

	public synchronized boolean registerWriter(String clientName, ClientWriter cw) {
		if (!hasClient(clientName)) {
			clientWriters.put(clientName, cw);
			return true;
		}
		return false;
	}

	public void unregisterWriter(String clientName) {
		clientWriters.remove(clientName);
		System.out.println("Client#" + clientName + " has left");
	}

	public boolean hasClient(String clientName) {
		return clientWriters.containsKey(clientName);
	}
}
