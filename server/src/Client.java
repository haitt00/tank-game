import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable {
	private String name;
	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private boolean running = false;

	private String roomId = null;
	private String teamId = null;

	public Client(Socket socket) {
		this.socket = socket;
	}

	public String getName() {
		return name;
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public DataOutputStream getWriter() {
		return writer;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public void sendPacket(String message) throws IOException {
		writer.writeBytes(message + "\n");
	}

	public void sendPacket(String opcode, String message) throws IOException {
		writer.writeBytes(opcode + " " + message + "\n");
		System.out.println(opcode + " " + message + "\n");
	}

	public void sendError(String errorMessage) throws IOException {
		sendPacket(Opcode.ERROR, errorMessage);
	}

	public void createRoom() throws IOException {
		Room room = RoomManager.singleton().generateRoom();
		sendPacket(Opcode.ROOM_CREATED, room.getId());
		System.out.println("Client#" + name + " creates new room#" + room.getId());
	}

	public void joinRoom(String roomId) throws IOException {
		Room room = RoomManager.singleton().findRoom(roomId);
		if (room == null) {
			sendError("Invalid room id " + roomId);
			System.out.println("Client#" + name + " fails to join room#" + roomId + ", no room found");
			return;
		}

		if (room.isFull()) {
			sendError("Room#" + roomId + " is full");
			System.out.println("Client#" + name + " fails to join room#" + roomId + ", room is full");
			return;
		}

		if (room.hasClient(this)) {
			sendError("You're already in room#" + roomId);
			System.out.println("Client#" + name + " fails to join room#" + roomId + ", already in room#" + roomId);
			return;
		}

		this.roomId = roomId;
		room.broadcast(Opcode.JOIN_MEMBER, name);
		String currentMembers = "";
		for (String clientName : room.getClients()) {
			currentMembers += clientName + ",";
		}
		currentMembers += name;

		room.addClient(this);
		sendPacket(Opcode.ROOM_ACCEPTED, roomId + " " + currentMembers);
		System.out.println("Client#" + name + " joins room#" + roomId);
	}

	public void exitRoom() throws IOException {
		Room room = RoomManager.singleton().findRoom(roomId);
		if (room == null || !room.hasClient(this)) {
			sendError("Not in any room");
			System.out.println("Client#" + name + " fails to leave room#" + roomId + ", not in room#" + roomId);
			return;
		}

		room.removeClient(name);
		room.broadcast(Opcode.LEAVE_MEMBER, name);
		System.out.println("Client#" + name + " left room#" + roomId);
		synchronized (this) {
			if (room.isEmpty()) {
				RoomManager.singleton().removeRoom(roomId);
				System.out.println("Destroy room#" + roomId);
			}
			roomId = null;
			teamId = null;
		}
	}

	private void parseOpcode(String input) throws IOException {
		String[] params = input.split(" ");
		String opcode = params[0];

		switch (opcode) {

		case Opcode.NEW_ROOM:
			createRoom();
			break;

		case Opcode.JOIN_ROOM:
			if (params.length == 1) {
				sendError("Please enter room id");
				System.out.println("Client#" + name + " sends an invalid packet: " + input + ", missing room_id");
				break;
			}
			joinRoom(params[1]);
			break;

		case Opcode.EXIT_ROOM:
			exitRoom();
			break;
		case Opcode.EXIT_GAME:
			running = false;
			break;

		case Opcode.MOVE:
		case Opcode.SHOOT:
		case Opcode.SET_TRAP:
			Room room = RoomManager.singleton().findRoom(roomId);
			room.broadcast(input);
			break;

		default:
			sendError("Invalid packet " + input);
			System.out.println("Client#" + name + " sends an invalid packet: " + input);
			break;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Client))
			return false;

		Client c = (Client) o;
		return name.equals(c.getName());
	}

	public void closeConnection() {
		try {
			reader.close();
			writer.close();
			socket.close();
			ClientManager.singleton().removeClient(name);
			System.out.println("Client#" + name + " has left");

			if (roomId == null)
				return;

			Room currentRoom = RoomManager.singleton().findRoom(roomId);
			if (currentRoom != null)
				currentRoom.removeClient(name);

			synchronized (this) {
				if (currentRoom.isEmpty()) {
					RoomManager.singleton().removeRoom(roomId);
					System.out.println("Destroy room#" + roomId);
				}
			}

			roomId = null;
			teamId = null;

		} catch (IOException e) {
			System.out.println("Error handling client#" + name + ": close connection");
		}
	}

	@Override
	public void run() {
		try {
			System.out.println(
					"New client connection from: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());

			while (true) {
				sendPacket(Opcode.NEW_CLIENT);
				name = reader.readLine();
				System.out.println("Client name: " + name);

				if (name == null)
					return;

				if (name.isBlank())
					continue;

				running = ClientManager.singleton().addClient(this);

				if (running) {
					sendPacket(Opcode.CLIENT_ACCEPTED, name);
					break;
				}
			}

			while (running) {
				String input = reader.readLine();
				parseOpcode(input);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error handling client#" + name + ": " + e.getMessage());
		} finally {
			closeConnection();
		}
	}
}
