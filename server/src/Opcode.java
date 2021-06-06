public class Opcode {
	// Client sends
	public static final String NEW_ROOM = "NEW_ROOM";
	public static final String JOIN_ROOM = "JOIN_ROOM";
	public static final String EXIT_ROOM = "EXIT_ROOM";
	public static final String EXIT_GAME = "EXIT_GAME";
	
	// Server sends
	public static final String ERROR = "ERROR";
	public static final String NEW_CLIENT = "NEW_CLIENT";
	public static final String CLIENT_ACCEPTED = "CLIENT_ACCEPTED";
	public static final String ROOM_CREATED = "ROOM_CREATED";
	public static final String JOIN_MEMBER = "JOIN_MEMBER";
	public static final String ROOM_ACCEPTED = "ROOM_ACCEPTED";
	public static final String LEAVE_MEMBER = "LEAVE_MEMBER";
	
	// Both sends
	public static final String MOVE = "MOVE";
	public static final String SHOOT = "SHOOT";
	public static final String SET_TRAP = "SET_TRAP";
}
