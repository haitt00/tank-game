package ui;



import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import logic.Configs;
import logic.Constants;
import logic.Game;
import logic.Tank;
import network.Client;

public class GameScene extends GeneralScene{
	Game game;
	Pane gamePane;
	Pane scorePane;
	HashMap<String, HBox> hbHearts;
	EventHandler<KeyEvent> keyHandler;
	public GameScene() {
		super();
		keyHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				String selfName = Client.getInstance().getName();
				game.handleInput(selfName, event.getCode());
				
			}
		};
		this.setOnKeyPressed(keyHandler);
	}

	
	public void setGame(Game game) {
		this.game = game;
	}
	

	public Game getGame() {
		return game;
	}


	@Override
	public void addContent() {
		Pane contentPane = new AnchorPane();
		
		gamePane = new Pane();
		gamePane.setPrefSize(Constants.GAME_SIZE, Constants.GAME_SIZE);
		AnchorPane.setLeftAnchor(gamePane, (double) 10);
		AnchorPane.setTopAnchor(gamePane, (double) 10);
		ImageView bg = new ImageView(new Image("/img/background.jpg", Constants.GAME_SIZE, Constants.GAME_SIZE, true, true));
		gamePane.getChildren().addAll(bg);
		
		scorePane = new VBox(10);
		scorePane.setPadding(new Insets(10));
		ArrayList<String> names = Client.getInstance().getPlayerNames();
		this.hbHearts = new HashMap<String, HBox>();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);

			HBox hbHeart = new HBox(5);			
			this.hbHearts.put(name, hbHeart);
			for (int j = 0; j < Configs.MAX_LIVES; j++) {
				hbHeart.getChildren().add(new ImageView(
						new Image("/img/heart.png")));
			}
			
			HBox hb = new HBox(20);
			Label nameLabel = new Label(name);
			Client client = Client.getInstance();
			if(name.contentEquals(client.getName())) {
				nameLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, nameLabel.getFont().getSize()));
			}
			String teamId = client.getTeamId(client.getName());
			if(client.getTeamId(name).equals(teamId)) {
				nameLabel.setTextFill(Color.web("#ff0000"));
			}
			hb.getChildren().addAll(nameLabel, hbHeart);
			scorePane.getChildren().add(hb);
		}
		AnchorPane.setLeftAnchor(scorePane, (double) (Constants.GAME_SIZE + 10 * 2));
		AnchorPane.setTopAnchor(scorePane, (double) 10);
		
		contentPane.getChildren().addAll(gamePane, scorePane);
		root.setCenter(contentPane);
		
	}
	
	public void addImageView(ImageView img, double d, double e) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				gamePane.getChildren().add(img);
				img.relocate(d, e);
				
			}
		});
	}
	public void removeImageView(ImageView img) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				gamePane.getChildren().remove(img);
				
			}
		});
		
	}
	public void updateScoreBoard() {
		ArrayList<String> names = Client.getInstance().getPlayerNames();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);

			HBox hbHeart = this.hbHearts.get(name);
			
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					int count = 0;
					Tank tank = game.getTank(name);
					if(tank!=null) {
						count = tank.getLives();
					}
					hbHeart.getChildren().clear();
					for (int j = 0; j < count; j++) {
						hbHeart.getChildren().add(new ImageView(
								new Image("/img/heart.png")));
					}
					
				}
			});
			
		}
	}
	

}
