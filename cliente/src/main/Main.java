package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Constants;
import logic.Game;
import network.Client;
import ui.LogOnScene;

public class Main extends Application{
	public static Scene currentScene;
	private static Stage primaryStage;
	public static void main(String[] args) {
		launch(args);
//		new Thread(Client.getInstance()).start();
//		System.out.println("Client is running...");
	}
	public static Scene getCurrentScene() {
		return currentScene;
	}
	public static void changeScene(Scene newScene) {
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				Main.currentScene = newScene;
				primaryStage.setScene(newScene);
				
			}	
		});	
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		new Thread(Client.getInstance()).start();
		Main.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest((ae) -> {
            Platform.exit();
            System.exit(0);
        });
		
		primaryStage.setScene(new LogOnScene());
		primaryStage.setTitle(Constants.GAME_TITLE);
		primaryStage.show();
//		Game game = new Game();
//		primaryStage.setScene(game.getGameScene());
//		primaryStage.setTitle(Constants.GAME_TITLE);
//		primaryStage.show();
		
	}

}
