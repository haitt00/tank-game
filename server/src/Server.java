import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int SERVER_PORT = 5588;

	public static void main(String[] args) throws IOException {
		System.out.println("Server is running ...");
		try (ServerSocket listener = new ServerSocket(SERVER_PORT)) {
			while (true) {
				Socket clientSocket = listener.accept();
				ClientManager.singleton().acceptClient(new Client(clientSocket));
			}
		}
	}
}
