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
import ui.LobbyScene;
import ui.LogOnScene;
import ui.WaitingScene;

public class Client implements Runnable{
	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private String name = "user3";
	private String roomId;
	private ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList("user1", "user2", "user3", "user4"));
	private static Client client;
	
	public static final int SERVER_PORT = 5588;
	public static final String SERVER_IP = "127.0.0.1";
	
	public static Client getInstance() {
		if(client==null) {
			client = new Client();
		}
		boolean check = (client == null);
		return client;
	}
	
	private void parseOpcode(String input) throws IOException {
		String[] params = input.split(" ");
		String opcode = params[0];

		switch (opcode) {
		
			case Opcode.NEW_CLIENT:
				Main.changeScene(new LogOnScene());
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
			case Opcode.START_GAME:
				receiveStartGame(params);
				break;
			case Opcode.ROOM_TIMEOUT:
				receiveRoomTimeout(params);
				break;
			case Opcode.MOVE:
				receiveMove(params);
				break;
			case Opcode.SHOOT:
				receiveShoot(params);
				break;
			case Opcode.SET_TRAP:
				receiveSetTrap(params);
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
		Main.changeScene(new LobbyScene());
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
		Main.changeScene(new WaitingScene());
	}
	private void receiveJoinMember(String[] params) {
		System.out.println("receiveJoinMember");
		String newName = params[1];
		System.out.println("newName: "+newName);	
		this.playerNames.add(newName);
		((WaitingScene) Main.getCurrentScene()).updateContent();
		
	}
	private void receiveLeaveMember(String[] params) {
		System.out.println("receiveLeaveMember");
		String leftName = params[1];
		System.out.println("leftName: "+leftName);
		this.playerNames.remove(leftName);
		((WaitingScene) Main.getCurrentScene()).updateContent();
	}
	private void receiveStartGame(String[] params) {
		System.out.println("receiveStartGame");
		for (int i = 0; i < params.length; i++) {
			System.out.println(params[i]);
		}
	}
	private void receiveRoomTimeout(String[] params) {
		System.out.println("receiveRoomTimeout");
	}
	private void receiveMove(String[] params) {
		System.out.println("receiveMove");
	}
	private void receiveShoot(String[] params) {
		System.out.println("receiveShoot");
	}
	private void receiveSetTrap(String[] params) {
		System.out.println("receiveSetTrap");
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
		
		System.out.println("Client is running...");
		while(true) {
			String input = null;
			try {
				input = reader.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				System.out.println("Input: "+input);
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
