package ui;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;
import logic.Constants;
import logic.Game;
import main.Main;

public class ResultScene extends GeneralScene{
	public ResultScene() {
		super(Constants.RESULT_WIDTH, Constants.RESULT_HEIGHT);
		
		Menu leaveRoom = new Menu("Start new game");
		MenuHelper.onAction(leaveRoom);
		leaveRoom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.changeScene(new LobbyScene());
			}
		});
		addMenuOption(leaveRoom);
	}

	@Override
	public void addContent() {
		Game game = ((GameScene)Main.getCurrentScene()).getGame();
		ArrayList<String> winners = game.getWinners();
		
		String resultStr = "";
		for (int i = 0; i < winners.size(); i++) {
			if(i!=winners.size()-1) {
				resultStr+=winners.get(i)+" and ";
			}
			else {
				resultStr+=winners.get(i)+" are winners!!!";
			}
		}
		VBox contentPane = new VBox(100);
		contentPane.setAlignment(Pos.TOP_CENTER);
		contentPane.setPadding(new Insets(50));
		contentPane.getChildren().add(new Label(resultStr));
		root.setCenter(contentPane);
		
	}
	
}
