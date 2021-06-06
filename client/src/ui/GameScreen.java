package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;

import logic.Game;

public class GameScreen extends GeneralScreen{
	
	KeyListener keyListener;
	Game game;
	
	public GameScreen(Game game) {
		super();
		
		initKeyListener();
		this.addKeyListener(keyListener);
		
		this.game = game;
	}
	private void initKeyListener() {
		keyListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	protected void addMenuBar() {
		
		super.addMenuBar();
		
		JMenu leaveRoom = new JMenu("Leave Room");
		leaveRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//send leaves room message
				new LobbyScreen();
			}
		});
		
		super.addMenuOption(leaveRoom);
	}
	
	protected void initMainPanel() {
		mainPanel = new JPanel();
		BufferedImage bg = null;
		try {
			bg = ImageIO.read(this.getClass().getResource("/img/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainPanel.add(new JLabel(new ImageIcon(bg)));
	}
	
	public static void main(String[] args) {
//		UIManager.put("MenuBar.background", Color.RED);
//        UIManager.put("Menu.background", Color.GREEN);
//        UIManager.put("MenuItem.background", Color.MAGENTA);
		new GameScreen(null);
	}
}
