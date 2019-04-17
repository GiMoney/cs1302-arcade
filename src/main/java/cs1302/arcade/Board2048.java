package cs1302.arcade;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Board2048 extends Group {
    private Tile[][] board;
    
    public Board2048() {
	super();
	board = new Tile[4][4];
	for(int row = 0; row < board.length; row++) {
	    for(int col = 0; col < board[row].length; col++) {
		board[row][col] = new Tile(row, col);
		this.getChildren().add(board[row][col]);
	    }//for - goes through columns
	}//for - goes through rows
    }//board constructor

    public void randTile() {
	int rand = 2 * ((int)(Math.random()) * 2 + 1);
	int x;
	int y;
	do {
	    x = (int)(4 * Math.random());
	    y = (int)(4 * Math.random());
	}while(!board[x][y].isEmpty());
	Tile t = new Tile(x * 100, y * 100, rand);
	board[x][y] = t;
    }//randTile

    class Tile extends Rectangle {
	private int number;

	public Tile(int x, int y) {
	    super((x * 100) + (x * 10), (y * 100) + (y * 10), 100, 100);
	    number = 0;
	    this.setFill(Color.GRAY);
	}//empty Tile constructor

	public Tile(int x, int y, int num) {
	    super(x, y, 100, 100);
	    number = num;
	    Image i = new Image("2048/" + num + ".jpg");
	    this.setFill(new ImagePattern(i));
	}//numbered Tile constructor

	public void setNum(int num) {
	    number = num;
	    Image i = new Image("2048/" + num + ".jpg");
	}//setNum

	public int getNum() {
	    return number;
	}//getNum

	public boolean isEmpty() {
	    if(number == 0) {
		return true;
	    }//if - Tile is empty if holding 0
	    return false;
	}
    }//Tile
}
