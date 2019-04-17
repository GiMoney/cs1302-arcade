package cs1302.arcade;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Arcade2048 extends VBox {

    public Arcade2048(Board2048 g) {
	super();
	Text t = new Text("2048");
	this.getChildren().addAll(t, g);
    }
}
