import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientWriter {
	private DataOutputStream writer;

	public ClientWriter(Socket socket) throws IOException {
		this.writer = new DataOutputStream(socket.getOutputStream());
	}

	public void close() throws IOException {
		writer.close();
	}

	public void sendPacket(String message) throws IOException {
		writer.writeBytes(message + "\n");
	}

	public void sendPacket(Opcode opcode) throws IOException {
		writer.writeBytes(opcode + "\n");
	}

	public void sendPacket(Opcode opcode, String message) throws IOException {
		writer.writeBytes(opcode + " " + message + "\n");
	}
}
