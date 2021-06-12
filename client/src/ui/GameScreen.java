package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Game;

public class GameScreen extends GeneralScene{
	
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
			public void keyTyped(KeyEvent e) {	}

			@Override
			public void keyPressed(KeyEvent e) {
				game.handleInput(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
//	protected void addMenuBar() {
//		
//		super.addMenuBar();
//		
//		JMenu leaveRoom = new JMenu("Leave Room");
//		leaveRoom.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//send leaves room message
//				new LobbyScreen();
//			}
//		});
//		
//		super.addMenuOption(leaveRoom);
//	}
	
	protected void initMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setLayout(null); 
		
		BufferedImage bg = null;
		try {
			bg = ImageIO.read(this.getClass().getResource("/img/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image scaled = bg.getScaledInstance(736, 736, Image.SCALE_SMOOTH);
		JLabel bkg = new JLabel(new ImageIcon(scaled));
		Dimension size = bkg.getPreferredSize();
		System.out.println(size);
		bkg.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		bkg.setBounds(7, 7, size.width, size.height);
		mainPanel.add(bkg);
		
	}
	
	public static void main(String[] args) {
		new GameScreen(null);
	}
	public void addGraphics(JComponent comp, int x, int y) {
		comp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension size = comp.getPreferredSize();
		comp.setBounds(x, y, size.width, size.height);
		
		mainPanel.add(comp, 0);
		repaint();
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
