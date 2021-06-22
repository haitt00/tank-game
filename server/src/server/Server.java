package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import manager.client.ClientListener;
import manager.client.ClientManager;
import manager.room.RoomManager;

public class Server extends Thread {
	private int serverPort;
	private String serverName = "Dummy";
	private ClientManager clientManager;
	private RoomManager roomManager;
	private ServerSocket listener;

	public Server(int port) {
		this.serverPort = port;
		this.clientManager = new ClientManager();
		this.roomManager = new RoomManager();
	}

	public Server(int port, String serverName) {
		this(port);
		this.serverName = serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getServerName() {
		return serverName;
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
			while (true) {
				try {
					Socket clientSocket = listener.accept();
					clientManager.executeListener(new ClientListener(clientSocket, this));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Error starting server [" + serverName + ":" + serverPort + "]");
		}
	}

}
