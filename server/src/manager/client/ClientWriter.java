package manager.client;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import commander.opcode.Opcode;

public class ClientWriter {
	private DataOutputStream writer;
	private boolean closed = true;

	public ClientWriter(Socket socket) throws IOException {
		this.writer = new DataOutputStream(socket.getOutputStream());
		this.closed = false;
	}

	public DataOutputStream getWriter() {
		return writer;
	}
	
	public boolean isClosed(){
		return closed;
	}

	public void close() throws IOException {
		writer.close();
		this.closed = true;
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
