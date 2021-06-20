package ui;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import logic.Constants;
import main.Main;
import network.Client;

public class WaitingScene extends GeneralScene{
	Label lblRoom;
	ArrayList<TextField> tfPlayers;
	public WaitingScene() {	
		super();
		
		Menu leaveRoom = new Menu("Leave Room");
		MenuHelper.onAction(leaveRoom);
		leaveRoom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Client.getInstance().sendExitRoom();
				Main.changeScene(new LobbyScene());
			}
		});
		addMenuOption(leaveRoom);
	}

	@Override
	public void addContent() {
		VBox contentPane = new VBox(100);
		contentPane.setAlignment(Pos.TOP_CENTER);
		contentPane.setPadding(new Insets(50));
		
		lblRoom = new Label("Players in room "+ Client.getInstance().getRoomId()+":");
		
		VBox playersGroup = new VBox(10);
		playersGroup.setAlignment(Pos.CENTER);
		playersGroup.setFillWidth(false);
		
		tfPlayers = new ArrayList<TextField>();
		for (int i = 0; i < Constants.PLAYERS_IN_GAME; i++) {
			tfPlayers.add(new TextField());
			tfPlayers.get(i).setEditable(false);
			playersGroup.getChildren().add(tfPlayers.get(i));
		}
		
		contentPane.getChildren().addAll(lblRoom, playersGroup);
		root.setCenter(contentPane);
		
		updateContent();
	}
	
	public void updateContent() {
		for (int i = 0; i < Constants.PLAYERS_IN_GAME; i++) {
			TextField tf = tfPlayers.get(i);
			if(i >= Client.getInstance().getPlayerNames().size()){
				tf.setText("");
			}
			else {
				
				String name = Client.getInstance().getPlayerNames().get(i);
				tf.setText(name);
				if(name.contentEquals(Client.getInstance().getName())) {
					tf.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, tf.getFont().getSize()));
				}
				
			}
		}
	}

}
