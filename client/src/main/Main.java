package main;
import javax.swing.JFrame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Constants;
import network.Client;
import ui.GeneralScene;
import ui.LogOnScreen;

public class Main extends Application{
	private static Scene currentScreen;
	public static void main(String[] args) {
		launch(args);
//		new Thread(Client.getInstance()).start();
//		System.out.println("Client is running...");
	}
	public static Scene getCurrentScreen() {
		return currentScreen;
	}
	public static void setCurrentScreen(Scene currentScreen) {
		Main.currentScreen = currentScreen;
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new GeneralScene());
		primaryStage.setTitle(Constants.GAME_TITLE);
		primaryStage.show();
		
	}
	
}
