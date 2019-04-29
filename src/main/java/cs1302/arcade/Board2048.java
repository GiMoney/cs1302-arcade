package cs1302.arcade;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
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
	this.randTile();
	this.randTile();
	this.setOnKeyPressed(this.createKeyEvent());
    }//board constructor

    public EventHandler<? super KeyEvent> createKeyEvent() {
	return event -> {
	    System.out.println(event);
	    if(event.getCode() == KeyCode.LEFT) {
		this.left();
	    }
	    if(event.getCode() == KeyCode.RIGHT) {
		this.right();
	    }
	    if(event.getCode() == KeyCode.UP) {
		this.up();
	    }
	    if(event.getCode() == KeyCode.DOWN) {
		this.down();
	    }
	};
    }//createKeyEvent

    public void up() {
	for(int row = 0; row < board.length; row++) {
	    for(int col = 1; col < board[row].length; col++) {
		for(int i = col; i > 0; i--) {
		    this.move(row, i, row, i - 1);
		}
	    }
	}//for
	this.randTile();
    }//left

    public void down() {
	for(int row = 0; row < board.length; row++) {
	    for(int col = board[row].length - 2; col >= 0; col--) {
		for(int i = col; i < board.length - 1; i++) {
		    this.move(row, i, row, i + 1);
		}
	    }
	}//for
	this.randTile();
    }//right

    public void right() {
	for(int col = 0; col < board[0].length; col++) {
	    for(int row = board.length - 2; row >= 0; row--) {
		for(int i = row; i < board.length - 1; i++) {
		    this.move(i, col, i + 1, col);
		}
	    }
	}//for
	this.randTile();
    }//up

    public void left() {
	for(int col = 0; col < board[0].length; col++) {
	    for(int row = 1; row < board.length; row++) {
		for(int i = row; i > 0; i--) {
		    this.move(i, col, i - 1, col);
		}
	    }
	}//for
	this.randTile();
    }//down

    private void move(int x1, int y1, int x2, int y2) {
	if(board[x2][y2].isEmpty() && !board[x1][y1].isEmpty()) {
	    board[x2][y2].setNum(board[x1][y1].getNum());
	    board[x1][y1].setNum(0);
	    System.out.println("moved");
	}else if(!board[x1][y1].isEmpty() && !board[x2][y2].isEmpty() &&
		 board[x1][y1].getNum() == board[x2][y2].getNum()) {
	    board[x2][y2].setNum(board[x2][y2].getNum() +
				 board[x1][y1].getNum());
	    board[x1][y1].setNum(0);
	    System.out.println("joined");
	}
    }//move

    public void randTile() {
	int rand = 2 * ((int)(Math.random()) * 2 + 1);
	int x;
	int y;
	do {
	    x = (int)(4 * Math.random());
	    y = (int)(4 * Math.random());
	}while(!board[x][y].isEmpty());
	board[x][y].setNum(2);
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
	    if(num == 0) {
		this.setFill(Color.GRAY);
	    }else {
		Image i = new Image("2048/" + num + ".jpg");
		this.setFill(new ImagePattern(i));
	    }
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
