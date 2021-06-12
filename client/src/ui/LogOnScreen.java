package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import network.Client;
public class LogOnScreen extends GeneralScreen{
	JTextField name;
	public LogOnScreen() {
		super();
	}

	@Override
	protected void initMainPanel() {
		mainPanel = new JPanel();
		
		name = new JTextField("");
		name.setColumns(10);
		mainPanel.add(name);
		
		JButton goBtn = new JButton("Go");
		JFrame screen = this;
		goBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {


				
//				System.out.println(screen.name.getText());
//				Client.getInstance().sendName(screen.name.getText());
				Client.getInstance().sendName(name.getText());
			}
		});
		mainPanel.add(goBtn);
	}
	
	public static void main(String[] args) {
		new LogOnScreen();
	}
	
	
}
