import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Thread {
	private int serverPort;
	private String serverName = "Dummy";
	private int maxRooms = 100;
	private int maxPlayers = 100 * Room.CLIENTS_PER_ROOM;
	private ClientManager clientManager;
	private RoomManager roomManager;
	private ServerSocket listener;
	
	public GameServer(int port) {
		this.serverPort = port;
		this.clientManager = new ClientManager();
		this.roomManager = new RoomManager();
	}
	
	public GameServer(int port, String serverName) {
		this(port);
		this.serverName = serverName;
	}
	
	public GameServer(int port, int maxRooms) {
		this(port);
		this.maxRooms = maxRooms;
		this.maxPlayers = maxRooms * Room.CLIENTS_PER_ROOM;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getServerName() {
		return serverName;
	}

	public int getMaxRooms() {
		return maxRooms;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public ClientManager getClientManager() {
		return clientManager;
	}

	public RoomManager getRoomManager() {
		return roomManager;
	}

	@Override
	public void run() {
		System.out.println("Server [" + serverName + ":" + serverPort + "] is running ...");
		try {
			listener = new ServerSocket(serverPort);
		} catch (IOException e) {
			System.out.println("Error starting server [" + serverName + ":" + serverPort +"]");
		}

		while (true) {
			try {
				Socket clientSocket = listener.accept();
				clientManager.executeClientListener(new ClientListener(clientSocket, this));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
