import java.io.IOException;

public class Commander {
	private Client client;
	private GameServer gameServer;

	public Commander(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	public Client getClient() {
		return client;
	}

	public String toSyntax(String message) {
		return message;
	}

	public String toSyntax(Opcode opcode, String message) {
		return opcode + " " + message;
	}

	public String toErrorSyntax(String errorMessage) {
		return Opcode.ERROR + " " + errorMessage;
	}

	public boolean requestRegisterClientWriter(Client c, ClientWriter cw) {
		boolean status = gameServer.getClientManager().registerWriter(c.getName(), cw);
		if (status)
			this.client = c;

		return status;
	}

	public ClientWriter requestFindClientWriter(String clientName) {
		return gameServer.getClientManager().getClientWriter(clientName);
	}

	public void requestDisconnectClient() {
		gameServer.getClientManager().unregisterWriter(client.getName());
		client.setRoomId(null);
		client.setTeamId(null);
	}

	public String requestCreateRoom() {
		Room room = gameServer.getRoomManager().generateRoom();
		if (room == null)
			return toErrorSyntax("Server overload");

		System.out.println("Client#" + client.getName() + " creates new room#" + room.getId());
		return toSyntax(Opcode.ROOM_CREATED, room.getId());
	}

	public void requestRemoveRoom(String roomId) {
		gameServer.getRoomManager().removeRoom(roomId);
	}

	public Room requestFindRoom(String roomId) {
		return gameServer.getRoomManager().findRoom(roomId);
	}

	public String requestJoinRoom(String roomId) throws IOException {
		Room room = requestFindRoom(roomId);
		if (room == null) {
			System.out.println("Client#" + client.getName() + " fails to join room#" + roomId + ", no room found");
			return toErrorSyntax("Invalid room id " + roomId);
		}

		if (room.isFull()) {
			System.out.println("Client#" + client.getName() + " fails to join room#" + roomId + ", room is full");
			return toErrorSyntax("Room#" + roomId + " is full");
		}

		if (room.hasClient(client)) {
			System.out.println(
					"Client#" + client.getName() + " fails to join room#" + roomId + ", already in room#" + roomId);
			return toErrorSyntax("You're already in room#" + roomId);
		}

		client.setRoomId(roomId);
		room.broadcast(Opcode.JOIN_MEMBER, client.getName());
		String currentMembers = "";
		for (String clientName : room.getClientNames()) {
			currentMembers += clientName + ",";
		}
		currentMembers += client.getName();

		room.addClient(client);
		room.addClientWriter(client.getName(), requestFindClientWriter(client.getName()));

		System.out.println("Client#" + client.getName() + " joins room#" + roomId);
		return toSyntax(Opcode.ROOM_ACCEPTED, roomId + " " + currentMembers);
	}

	public String requestExitRoom() throws IOException {
		Room room = requestFindRoom(client.getRoomId());
		if (room == null || !room.hasClient(client)) {
			return toErrorSyntax("Not in any room");
		}

		room.removeClient(client.getName());
		room.broadcast(Opcode.LEAVE_MEMBER, client.getName());
		System.out.println("Client#" + client.getName() + " left room#" + client.getRoomId());

		synchronized (this) {
			if (room.isEmpty())
				requestRemoveRoom(client.getRoomId());
		}

		client.setRoomId(null);
		client.setTeamId(null);
		return null;
	}
	
	public String requestEndGame() {
		Room room = requestFindRoom(client.getRoomId());
		if (room == null || !room.hasClient(client)) {
			return null;
		}
		
		synchronized (this) {
			if (room.isRunning()) {
				room.endGame();
				gameServer.getRoomManager().removeRoom(room.getId());
			}
		}
		return null;
	}

	public String parseCommand(String command) throws IOException {
		String[] params = command.split(" ");
		String opcode = params[0];

		if (Opcode.NEW_ROOM.name().equals(opcode))
			return requestCreateRoom();

		if (Opcode.JOIN_ROOM.name().equals(opcode)) {
			if (params.length == 1) {
				System.out.println("Invalid packet: " + command + ", missing room_id");
				return toSyntax(Opcode.ERROR, "Please enter room id");
			}
			return requestJoinRoom(params[1]);
		}

		if (Opcode.EXIT_ROOM.name().equals(opcode))
			return requestExitRoom();

		if (Opcode.EXIT_GAME.name().equals(opcode))
			return opcode;

		if (Opcode.MOVE.name().equals(opcode) || Opcode.SHOOT.name().equals(opcode)
				|| Opcode.SET_TRAP.name().equals(opcode)) {
			Room room = gameServer.getRoomManager().findRoom(client.getRoomId());
			System.out.println("Room#" + client.getRoomId() + ": " + client.getName() + " " + command);
			room.broadcastExcept(client.getName(), command + " " + client.getName());
			return null;
		}
		
		if (Opcode.END_GAME.name().equals(opcode))
			return requestExitRoom();

		System.out.println("Invalid packet: " + command);
		return toErrorSyntax("Invalid packet: " + command);
	}
}
