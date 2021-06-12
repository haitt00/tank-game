package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import network.Client;

public class WaitingScreen extends GeneralScreen{
	
	JLabel roomlbl;
	JLabel[] playerslbl;
	
	
	public WaitingScreen() {}


	@Override
	protected void initMainPanel() {
		mainPanel = new JPanel();
		
		String roomId = Client.getInstance().getRoomId();
		roomlbl = new JLabel("Players in room #"+roomId);
		mainPanel.add(roomlbl);
		
		playerslbl = new JLabel[4];
		for (int i = 0; i < playerslbl.length; i++) {
			playerslbl[i] = new JLabel();
			mainPanel.add(playerslbl[i]);
		}
				
		JButton exit = new JButton("Leave Room");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.getInstance().sendExitRoom();
				new LobbyScreen();
			}
		});
		mainPanel.add(exit);
		updatePlayerLabels();
	}
	
	public void updatePlayerLabels(){
//		System.out.println("updatePlayerLabels");
//		System.out.println("Length of names: "+playerNames.size());
		ArrayList<String> playerNames = Client.getInstance().getPlayerNames();
		for (int i = 0; i < playerslbl.length; i++) {
			if(playerNames.size() <= i) {
				playerslbl[i].setText("");
				continue;
			}
			playerslbl[i].setText(playerNames.get(i));
			if(playerNames.get(i).equals(Client.getInstance().getName())){
				System.out.println("equal "+ i);
				playerslbl[i].setForeground(Color.RED);
			}
		}
	}
}
