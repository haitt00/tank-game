package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import network.Client;

public class LogOnScene extends GeneralScene{
	TextField tfName;
	Button btnGo;
	public LogOnScene() {	}

	@Override
	public void addContent() {
		HBox contentPane = new HBox(8);
		contentPane.setAlignment(Pos.CENTER);
		
		//set the text field
		tfName = new TextField();
		tfName.setPromptText("Enter your name");
		tfName.setFocusTraversable(false);
		
		//set the button
		btnGo = new Button("Go");
		btnGo.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Client.getInstance().sendName(tfName.getText());
				
			}
		});
		
		contentPane.getChildren().addAll(tfName, btnGo);
		root.setCenter(contentPane);
	}
	
	

}
