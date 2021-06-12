package main;
import javax.swing.JFrame;

import javafx.application.Application;
import javafx.stage.Stage;
import network.Client;
import ui.LogOnScreen;

public class Main extends Application{
	private static JFrame currentScreen;
	public static void main(String[] args) {
		new Thread(Client.getInstance()).start();
		System.out.println("Client is running...");
	}
	public static JFrame getCurrentScreen() {
		return currentScreen;
	}
	public static void setCurrentScreen(JFrame currentScreen) {
		Main.currentScreen = currentScreen;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
