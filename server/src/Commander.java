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
	
	public void requestRegisterClientListener(ClientListener cl) {
		gameServer.getClientManager().registerClientListener(cl);
	}
	
	public ClientListener requestFindClientListener(String clientName) {
		return gameServer.getClientManager().getClientListener(clientName);
	}

	public boolean requestAddClient(Client c) {
		this.client = c;
		return gameServer.getClientManager().addClient(c);
	}

	public void requestRemoveClient(String clientName) {
		gameServer.getClientManager().removeClient(clientName);
		gameServer.getClientManager().unregisterClientListener(clientName);
		client.setRoomId(null);
		client.setTeamId(null);
	}

	public void requestRemoveRoom(String roomId) {
		gameServer.getRoomManager().removeRoom(roomId);
	}

	public Room requestFindRoom(String roomId) {
		return gameServer.getRoomManager().findRoom(roomId);
	}

	public String requestCreateRoom() {
		Room room = gameServer.getRoomManager().generateRoom();
		System.out.println("Client#" + client.getName() + " creates new room#" + room.getId());
		return toSyntax(Opcode.ROOM_CREATED, room.getId());
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

		ClientListener newListener = requestFindClientListener(client.getName());
		room.addClientListener(newListener);

		System.out.println("Client#" + client.getName() + " joins room#" + roomId);
		return toSyntax(Opcode.ROOM_ACCEPTED, roomId + " " + currentMembers);
	}

	public String requestExitRoom() throws IOException {
		Room room = requestFindRoom(client.getRoomId());
		if (room == null || !room.hasClient(client)) {
			return toErrorSyntax("Not in any room");
		}

		room.removeClient(client.getName());
		client.setRoomId(null);
		client.setTeamId(null);

		room.broadcast(Opcode.LEAVE_MEMBER, client.getName());
		System.out.println("Client#" + client.getName() + " left room#" + client.getRoomId());
		synchronized (this) {
			if (room.isEmpty())
				requestRemoveRoom(client.getRoomId());
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
			room.broadcast(command);
			return null;
		}
		
		System.out.println("Invalid packet: " + command);
		return toErrorSyntax("Invalid packet: " + command);
	}
}
