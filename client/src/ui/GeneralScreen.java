package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logic.Constants;
import main.Main;

public abstract class GeneralScreen extends JFrame{
	JMenuBar menuBar;
	JPanel mainPanel;
	public GeneralScreen() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		addMenuBar();
		
		addMainPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(Constants.GAME_TITLE);
		setSize(Constants.WIDTH, Constants.HEIGHT);
		setVisible(true);
		if(Main.getCurrentScreen()!=null) {
			Main.getCurrentScreen().dispose();
		}
		Main.setCurrentScreen(this);
	}
	public void addMainPanel() {
		initMainPanel();
		Container cp = getContentPane();
		cp.add(mainPanel, BorderLayout.CENTER);
	}
	protected abstract void initMainPanel();
	protected void addMenuBar() {
		
		Container cp = getContentPane();
		
		JMenu help = new JMenu("Help");
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				new Help();
			}
		});
		menuBar = new JMenuBar();
		menuBar.setOpaque(true);
		menuBar.add(help);
		cp.add(menuBar, BorderLayout.NORTH);
	}
	
	public void addMenuOption(JMenu menu) {
		menuBar.add(menu);
	}
}
