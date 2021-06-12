public class Server {
	public static void main(String[] args) {
		GameServer server = new GameServer(5588);
		server.start();
	}
}
