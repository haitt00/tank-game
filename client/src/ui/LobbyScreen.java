package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LobbyScreen extends GeneralScreen{
	
	public LobbyScreen() {}

	protected void addMenuBar() {
		
		super.addMenuBar();
		
		Container cp = getContentPane();
		
		JMenu newRoom = new JMenu("New Room");
		newRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//send new room message
				new LobbyScreen();
			}
		});
		
		addMenuOption(newRoom);
	}
	
	@Override
	protected void initMainPanel() {
		mainPanel = new JPanel();
//		mainPanel.setLayout(new Ancho(2, 1));
		mainPanel.add(new JTextField("RoomID"));
		mainPanel.add(new JTextField("Enter your name"));
	}
	
	public static void main(String[] args) {
		new LobbyScreen();
	}
}
