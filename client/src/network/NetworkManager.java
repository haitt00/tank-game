package network;

public class NetworkManager {
	private NetworkManager netMgr;
	
	private NetworkManager() {
		
	}
	
	public void sendNewRoom() {
		
	}
	
	public NetworkManager getInstance() {
		if(netMgr == null) {
			netMgr = new NetworkManager();
		}
		return netMgr;
	}
}
