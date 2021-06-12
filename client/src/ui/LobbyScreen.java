package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import network.Client;

public class LobbyScreen extends GeneralScreen{
	private JLabel namelbl;
	private JTextField roomId;
	public LobbyScreen() {
		super();
	}
	
	@Override
	protected void initMainPanel() {
		mainPanel = new JPanel();
//		mainPanel.setLayout(new FlowLayout());
		
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		namelbl = new JLabel();
		String name = Client.getInstance().getName();
		namelbl.setText("WELCOME "+name+"!!");
		mainPanel.add(namelbl);
		
//		mainPanel.add(Box.createVerticalGlue());
		roomId = new JTextField("");
		roomId.setColumns(10);
		mainPanel.add(roomId); //room id
		JButton join = new JButton("Join room");
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.getInstance().sendJoinRoom(roomId.getText());
			}
		});
		mainPanel.add(join);
		
		JButton create = new JButton("New room");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.getInstance().sendNewRoom();
				
			}
		});
		mainPanel.add(create);
		
	}
	
	public static void main(String[] args) {
		new LobbyScreen();
	}
}
