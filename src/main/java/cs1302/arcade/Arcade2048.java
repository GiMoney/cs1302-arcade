package cs1302.arcade;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Arcade2048 extends Scene {
    private VBox vbox = new VBox();
    private MenuBar menubar;
    private Board2048 board;
    
    public Arcade2048(Board2048 g, Scene main) {
	super(g, 600, 470);
	this.setRoot(vbox);
	board = g;
	menubar = new MenuBar();
	Menu options = new Menu("Options");
	menubar.getMenus().add(options);
	MenuItem close = new MenuItem("Close");
	close.setOnAction(e -> {
		Thread t = new Thread(() -> {
			Stage swap = (Stage) this.getWindow();
			Platform.runLater(() -> swap.setScene(main));
		});
		t.setDaemon(true);
		t.start();
	    });
	MenuItem newGame = new MenuItem("New Game");
	newGame.setOnAction(e -> {
		Thread t = new Thread(() -> {
			Platform.runLater(() -> board.newGame());
		});
		t.setDaemon(true);
		t.start();
	    });
	options.getItems().addAll(newGame, close);
	vbox.getChildren().addAll(menubar, board);
    }

    public Board2048 getBoard() {
	return board;
    }
}
