package ui;



import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.Constants;
import logic.Game;
import network.Client;

public class GameScene extends GeneralScene{
	Game game;
	Pane gamePane;
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


	@Override
	public void addContent() {
		Pane contentPane = new AnchorPane();
		
		gamePane = new Pane();
		gamePane.setPrefSize(Constants.GAME_SIZE, Constants.GAME_SIZE);
		AnchorPane.setLeftAnchor(gamePane, (double) 10);
		AnchorPane.setTopAnchor(gamePane, (double) 10);
		ImageView bg = new ImageView(new Image("/img/background.jpg", Constants.GAME_SIZE, Constants.GAME_SIZE, true, true));
		gamePane.getChildren().addAll(bg);
		
		VBox scorePane = new VBox();
		scorePane.getChildren().addAll(new Label("This is the score pane"));
		AnchorPane.setLeftAnchor(scorePane, (double) (Constants.GAME_SIZE + 10 * 2));
		AnchorPane.setTopAnchor(scorePane, (double) 10);
		
		contentPane.getChildren().addAll(gamePane, scorePane);
		root.setCenter(contentPane);
		
	}
	
	public void addImageView(ImageView img, double d, double e) {
		gamePane.getChildren().add(img);
		img.relocate(d, e);
	}
	public void removeImageView(ImageView img) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				gamePane.getChildren().remove(img);
				
			}
		});
		
	}
	

}
