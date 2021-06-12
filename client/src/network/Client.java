package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import main.Main;
import ui.LobbyScreen;
import ui.LogOnScreen;
import ui.WaitingScreen;

public class Client implements Runnable{
	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private String name;
	private String roomId;
	private ArrayList<String> playerNames;
	private static Client client;
	
	public static final int SERVER_PORT = 5588;
	public static final String SERVER_IP = "127.0.0.1";
	
	public static Client getInstance() {
		if(client==null) {
//			System.out.println("create new");
			client = new Client();
		}
//		System.out.println("has old");
		boolean check = (client == null);
//		System.out.println("check: "+check);
		return client;
	}
	
//	private Client() {
//	}
	
	
	
	private void parseOpcode(String input) throws IOException {
		String[] params = input.split(" ");
		String opcode = params[0];

		switch (opcode) {
		
			case Opcode.NEW_CLIENT:
				new LogOnScreen();
				break;
			case Opcode.CLIENT_ACCEPTED:
				receiveClientAccepted(params);
				break;
				
			case Opcode.ROOM_CREATED:
				receiveRoomCreated(params);
				break;
				
			case Opcode.ROOM_ACCEPTED:
				receiveRoomAccepted(params);
				break;
	
			case Opcode.JOIN_MEMBER:
				receiveJoinMember(params);
				break;
			case Opcode.LEAVE_MEMBER:
				receiveLeaveMember(params);
				break;
	//		default:
	//			sendError("Invalid packet " + input);
	//			System.out.println("Client#" + name + " sends an invalid packet: " + input);
	//			break;
		}
	}
	
	//send
	public void sendPacket(String message) {
		try {
			writer.writeBytes(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacket(String opcode, String message) {
		sendPacket(opcode + " " + message);
	}
	
	public void sendName(String name) {
		sendPacket(name);
	}
	public void sendNewRoom() {
		System.out.println("sendNewRoom");
		sendPacket(Opcode.NEW_ROOM, "");
	}
	
	public void sendJoinRoom(String roomId) {
		System.out.println("sendJoinRoom");
		System.out.println("roomId: "+roomId);
		sendPacket(Opcode.JOIN_ROOM, roomId);
	}
	
	public void sendExitRoom() {
		System.out.println("sendExitRoom");
		sendPacket(Opcode.EXIT_ROOM, "");
	}
	
	
	//receive
	private void receiveClientAccepted(String[] params){
		
		System.out.println("receiveClientAccepted");
		String name = params[1];
		System.out.println("name: "+name);
		this.name = name; 
		new LobbyScreen();
	}
	private void receiveRoomCreated(String[] params) {
		System.out.println("receiveRoomCreated");
		String id = params[1];
//		System.out.println("id: "+id);
//		ArrayList<String> names = new ArrayList<String>();
//		names.add(name);
//		new WaitingScreen(id, names);
		this.roomId = id;
		sendJoinRoom(id);
	}
	
	private void receiveRoomAccepted(String[] params) {
		System.out.println("receiveRoomAccepted");
		String roomId = params[1];
		System.out.println("roomId: "+roomId);
		String members = params[2];
		System.out.println("members: "+members);
		String[] arrayMembers = members.split(",");
		this.playerNames = new ArrayList<String>(Arrays.asList(arrayMembers));
		new WaitingScreen();
	}
	private void receiveJoinMember(String[] params) {
		System.out.println("receiveJoinMember");
		String newName = params[1];
		System.out.println("newName: "+newName);	
		this.playerNames.add(newName);
		((WaitingScreen) Main.getCurrentScreen()).updatePlayerLabels();
		
	}
	private void receiveLeaveMember(String[] params) {
		System.out.println("receiveLeaveMember");
		String leftName = params[1];
		System.out.println("leftName: "+leftName);
		this.playerNames.remove(leftName);
		((WaitingScreen) Main.getCurrentScreen()).updatePlayerLabels();
	}
	

	@Override
	public void run() {
		//set up the socket
				try {
					socket =  new Socket(SERVER_IP, SERVER_PORT);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				//set up the output
				try {
					writer = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//set up the input
				try {
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				while(true) {
					String input = null;
					try {
						input = reader.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						parseOpcode(input);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
	}

	public String getName() {
		return name;
	}

	public String getRoomId() {
		return roomId;
	}

	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}
	
	
	
	
}
