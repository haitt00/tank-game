import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
	private Socket socket;
	private BufferedReader reader;
	private ClientWriter writer;
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
	
	public ClientWriter getWriter() {
		return writer;
	}

	public void closeConnection() {
		try {
			reader.close();
			writer.close();
			socket.close();

			commander.requestDisconnectClient();

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
			writer = new ClientWriter(socket);

			while (true) {
				writer.sendPacket(Opcode.NEW_CLIENT);
				String name = reader.readLine();
				System.out.println("Client name: " + name);

				if (name == null)
					return;

				if (name.isBlank())
					continue;

				running = commander.requestAddClient(new Client(name));

				if (running) {
					writer.sendPacket(Opcode.CLIENT_ACCEPTED, name);
					commander.requestRegisterClientWriter(name, writer);
					break;
				}
			}

			while (running) {
				String input = reader.readLine();
				String result = commander.parseCommand(input);
				
				if (result == null)
					continue;
				
				if (result.equals(Opcode.EXIT_GAME.name())) {
					running = false;
					break;
				}
			
				writer.sendPacket(result);
			}

		} catch (Exception e) {
			System.out.println("Error handling client#" + commander.getClient().getName() + ": " + e.getMessage());
		} finally {
			closeConnection();
		}
	}
}
