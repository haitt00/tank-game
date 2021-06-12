package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import logic.Constants;
import main.Main;

public class GeneralScene extends Scene{
	MenuBar menuBar;
	Pane root;
	public GeneralScene() {
		super(null, Constants.WIDTH, Constants.HEIGHT);
		root = new BorderPane();
		this.setRoot(root);
		
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
		
//		Container cp = getContentPane();
//		cp.setLayout(new BorderLayout());
		
		addMenuBar();
		
//		addMainPanel();
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setTitle(Constants.GAME_TITLE);
//		setSize(Constants.WIDTH, Constants.HEIGHT);
//		setVisible(true);
		
//		if(Main.getCurrentScreen()!=null) {
//			Main.getCurrentScreen().dispose();
//		}
//		Main.setCurrentScreen(this);
	}
//	public void addMainPanel() {
//		initMainPanel();
////		Container cp = getContentPane();
////		cp.add(mainPanel, BorderLayout.CENTER);
//	}
//	protected void initMainPanel();
	protected void addMenuBar() {
		
//		Container cp = getContentPane();
		
		Menu help = new Menu("Help");
		help.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
//				Main.setCurrentScreen(new HelpScreen());
				
			}
			
		});
		menuBar = new MenuBar();
//		menuBar.setOpaque(true);
		menuBar.getMenus().add(help);
		root.getChildren().add(menuBar);
	}
	
	public void addMenuOption(Menu menu) {
		menuBar.getMenus().add(menu);
	}
}
