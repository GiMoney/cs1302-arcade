package cs1302.arcade;

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
	super(g, 500, 500);
	this.setRoot(vbox);
	board = g;
	menubar = new MenuBar();
	Menu options = new Menu("Options");
	menubar.getMenus().add(options);
	MenuItem close = new MenuItem("Close");
	close.setOnAction(e -> {
		Stage swap = (Stage) this.getWindow();
		swap.setScene(main);
	    });
	options.getItems().add(close);
	vbox.getChildren().addAll(menubar, board);
    }

    public Board2048 getBoard() {
	return board;
    }
}
