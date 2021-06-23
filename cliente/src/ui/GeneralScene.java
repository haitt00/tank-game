package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import logic.Constants;

public class GeneralScene extends Scene {
	MenuBar menuBar;
	BorderPane root;

	public GeneralScene(int width, int height) {
		super(new BorderPane(), width, height);
		root = new BorderPane();
		this.setRoot(root);

		addMenuBar();
		addContent();
	}

	public GeneralScene() {
		this(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
	}

	public void addContent() {
	}

	protected void addMenuBar() {

		Menu help = new Menu("Help");
		help.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
//				Main.setCurrentScreen(new HelpScreen());
			}

		});
		menuBar = new MenuBar();
		menuBar.getMenus().add(help);
		root.setTop(menuBar);
	}

	public void addMenuOption(Menu menu) {
		menuBar.getMenus().add(menu);
	}

}
