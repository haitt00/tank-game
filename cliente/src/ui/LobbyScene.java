package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.Constants;
import network.Client;

public class LobbyScene extends GeneralScene {
	Label lblWelcome;
	TextField tfRoomId;
	Button btnJoinRoom;
	Button btnNewRoom;

	public LobbyScene() {
		super(Constants.LOBBY_WIDTH, Constants.LOBBY_HEIGHT);
	}

	@Override
	public void addContent() {
		VBox contentPane = new VBox(250);
		contentPane.setAlignment(Pos.TOP_CENTER);
		contentPane.setPadding(new Insets(50));

		lblWelcome = new Label("Welcome " + Client.getInstance().getName() + "!");

		VBox roomGroup = new VBox(8);
		roomGroup.setAlignment(Pos.CENTER);
		HBox joinRoomGroup = new HBox(8);
		joinRoomGroup.setAlignment(Pos.CENTER);

		btnJoinRoom = new Button("Join room");
		btnJoinRoom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Client.getInstance().sendJoinRoom(tfRoomId.getText());
			}
		});

		tfRoomId = new TextField();
		tfRoomId.setPromptText("Room ID");
		tfRoomId.setFocusTraversable(false);

		joinRoomGroup.getChildren().addAll(btnJoinRoom, tfRoomId);

		Label or = new Label("Or");

		btnNewRoom = new Button("Create room");
		btnNewRoom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Client.getInstance().sendNewRoom();

			}
		});

//		roomGroup.getChildren().addAll(joinRoomGroup, or, btnNewRoom);
//
//		contentPane.getChildren().addAll(lblWelcome, roomGroup);
		
		roomGroup.getChildren().addAll(lblWelcome,joinRoomGroup, or, btnNewRoom);

		contentPane.getChildren().addAll(roomGroup);
		root.setCenter(contentPane);
	}

}
