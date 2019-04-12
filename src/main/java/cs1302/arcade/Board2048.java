import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class Board2048 extends GridPane {
    private Rectangle[][] board = new Rectangle[4][4];
    
    public Board2048() {
	super();
    }

    public class Tile extends Rectangle {
	private int number;

	public Tile() {
	    super(20, 20);
	}
    }
}
