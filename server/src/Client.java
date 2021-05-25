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
	private String currentRoomId = null;

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

	public void sendMessage(String message) throws IOException {
		writer.writeBytes(message + "\n");
	}

	public void sendMessage(String opcode, String message) throws IOException {
		writer.writeBytes(opcode + " " + message + "\n");
	}

	public void sendError(String errorMessage) throws IOException {
		sendMessage("ERROR", errorMessage);
	}

	private void parseOpcode(String input) throws IOException {
		String[] params = input.split(" ");
		String opcode = params[0];
		String roomId = null;
		Room room = null;

		switch (opcode) {

		case "NEW_ROOM":
			room = RoomManager.singleton().generateRoom();
			sendMessage("ROOM_CREATED", String.valueOf(room.getId()));
			Server.log("Client#" + name + " creates new room#" + room.getId());
			break;

		case "JOIN_ROOM":
			if (params.length == 1) {
				sendError("Please enter room id");
				Server.log("Invalid opcode: " + input + ", missing room_id");
				break;
			}

			roomId = params[1];
			room = RoomManager.singleton().findRoomById(roomId);

			if (room == null) {
				sendError("Invalid room id " + roomId);
				Server.log("Client#" + name + " fails to join room#" + roomId + ", no room found");
				break;
			}

			if (room.isFull()) {
				sendError("Room#" + roomId + " is full");
				Server.log("Client#" + name + " fails to join room#" + roomId + ", room is full");
				break;
			}

			if (currentRoomId != null || room.hasClient(this)) {
				sendError("You're already in room#" + currentRoomId);
				Server.log("Client#" + name + " fails to join room#" + roomId + ", already in room#" + currentRoomId);
				break;
			}

			currentRoomId = roomId;
			room.broadcast("JOIN_MEMBER " + name);

			String currentMembers = "";
			for (Client client : room.getClients()) {
				currentMembers += client.getName() + ",";
			}
			currentMembers += name;

			sendMessage("ROOM_ACCEPTED", roomId + " " + currentMembers);
			room.addClient(this);

			Server.log("Client#" + name + " joins room#" + roomId);
			break;

		case "LEAVE_ROOM":
			room = RoomManager.singleton().findRoomById(currentRoomId);

			if (room == null || !room.hasClient(this)) {
				sendError("Not in any room");
				Server.log("Client#" + name + " fails to leave room#" + currentRoomId + ", not in room#" + roomId);
				break;
			}

			room.removeClient(this);
			room.broadcast("LEAVE_MEMBER " + name);
			Server.log("Client#" + name + " left room#" + currentRoomId);

			synchronized (this) {
				if (room.isEmpty()) {
					RoomManager.singleton().removeRoom(room);
					Server.log("Destroy room#" + currentRoomId);
				}

				currentRoomId = null;
			}
			break;

		case "MOVE":
			// TODO
			break;
		case "SHOOT":
			// TODO
			break;
		case "SET_TRAP":
			// TODO
			break;
		default:
			sendError("Invalid opcode " + input);
			Server.log("Invalid opcode: " + input);
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

	@Override
	public void run() {
		try {
			Server.log(
					"New client connection from: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());

			while (true) {
				sendMessage("NEW_CLIENT");
				name = reader.readLine();
				Server.log("Client name: " + name);

				if (name == null)
					return;

				if (!name.isBlank())
					running = ClientManager.singleton().addClient(this);

				if (running) {
					sendMessage("CLIENT_ACCEPTED", name);
					break;
				}
			}

			while (running) {
				String input = reader.readLine();
				parseOpcode(input);
			}

		} catch (Exception e) {
			Server.log("Error handling client#" + name + ": " + e.getMessage());
		} finally {
			try {
				Server.log("Client#" + name + " has left");
				reader.close();
				writer.close();
				socket.close();
				ClientManager.singleton().removeClient(this);

				if (currentRoomId == null)
					return;

				synchronized (this) {
					Room currentRoom = RoomManager.singleton().findRoomById(currentRoomId);
					if (currentRoom != null) {
						currentRoom.removeClient(this);

						if (currentRoom.isEmpty()) {
							RoomManager.singleton().removeRoom(currentRoom);
							Server.log("Destroy room#" + currentRoomId);
						}
					}
				}

				currentRoomId = null;

			} catch (IOException e) {
				Server.log(e.getMessage());
			}
		}
	}
}
