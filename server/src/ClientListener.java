import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private boolean running = false;
	private Commander commander;

	public ClientListener(Socket socket, GameServer server) {
		this.socket = socket;
		this.commander = new Commander(server);
	}

	public Socket getSocket() {
		return socket;
	}

	public Client getClient() {
		return commander.getClient();
	}

	public void sendPacket(String message) throws IOException {
		if (message == null)
			return;

		writer.writeBytes(message + "\n");
	}

	public void sendPacket(Opcode opcode) throws IOException {
		if (opcode == null)
			return;

		writer.writeBytes(opcode + "\n");
	}

	public void sendPacket(Opcode opcode, String message) throws IOException {
		if (opcode == null)
			return;

		writer.writeBytes(opcode + " " + message + "\n");
	}

	public void closeConnection() {
		try {
			reader.close();
			writer.close();
			socket.close();

			String clientName = commander.getClient().getName();
			commander.requestRemoveClient(clientName);

			String roomId = commander.getClient().getRoomId();
			if (roomId == null)
				return;

			Room currentRoom = commander.requestFindRoom(roomId);
			if (currentRoom != null)
				currentRoom.removeClient(commander.getClient().getName());

			synchronized (this) {
				if (currentRoom.isEmpty()) {
					commander.requestRemoveRoom(roomId);
				}
			}
		} catch (IOException e) {
			System.out.println("Error handling client#" + commander.getClient().getName() + ": close connection");
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
				String name = reader.readLine();
				System.out.println("Client name: " + name);

				if (name == null)
					return;

				if (name.isBlank())
					continue;

				running = commander.requestAddClient(new Client(name));

				if (running) {
					commander.requestRegisterClientListener(this);
					sendPacket(Opcode.CLIENT_ACCEPTED, name);
					break;
				}
			}

			while (running) {
				String input = reader.readLine();
				String result = commander.parseCommand(input);
				if (result.equals(Opcode.EXIT_GAME.name())) {
					running = false;
					break;
				}
			
				sendPacket(result);
			}

		} catch (Exception e) {
			System.out.println("Error handling client#" + commander.getClient().getName() + ": " + e.getMessage());
		} finally {
			closeConnection();
		}
	}
}
