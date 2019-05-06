package cs1302.arcade;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This a class that is used to create a board representative of the
 * game 2048. 2048 is a game in which tiles are slide across the board
 * using the directional keys and join together and add up when the tiles
 * next to each other collide and are the same number. {@code Tile} represents
 * these tiles that interact with each other throughout the game. This
 * class only is used for the actual "game" portion for the overall {@code Scene}.
 * Other features like high score listing and changing between scenes can
 * be looked at in the class {@code Arcade2048}.
 */
public class Board2048 extends Group {
    private Tile[][] board; /** Array that holds all the Tiles in the game */
    private boolean emptyMove; /** Used to check if a move is "empty" */

    /**
     * Default constructor that is used to create a new game of 2048. Each
     * game begins with an empty board filled with empty {@code Tile}. Then
     * two random tiles will be give a value of 2 or 4.
     */
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

    /**
     * A helper method that is used to create an {@code Event} used for
     * keyboard controls.
     * @return Event for using directional keys for movement
     */
    private EventHandler<? super KeyEvent> createKeyEvent() {
	return event -> {
	    Runnable r = () -> {
		System.out.println(event);
		if(event.getCode() == KeyCode.LEFT) {
		    this.left();
		}//if - left key to move left
		if(event.getCode() == KeyCode.RIGHT) {
		    this.right();
		}//if - right key to move right
		if(event.getCode() == KeyCode.UP) {
		    this.up();
		}//if - up key to move up
		if(event.getCode() == KeyCode.DOWN) {
		    this.down();
		}//if - down key to move down
	    };
	    Thread t = new Thread(r);
	    t.setDaemon(true);
	    t.start();
	};
    }//createKeyEvent

    /**
     * Method that is used to move all tiles on the board up
     */
    public void up() {
	for(int row = 0; row < board.length; row++) {
	    for(int col = 1; col < board[row].length; col++) {
		for(int i = col; i > 0; i--) {
		    this.move(row, i, row, i - 1);
		}
	    }
	}//for
	this.spawn();
    }//left

    /**
     * Method that is used to move all tiles on the board down
     */
    public void down() {
	for(int row = 0; row < board.length; row++) {
	    for(int col = board[row].length - 2; col >= 0; col--) {
		for(int i = col; i < board.length - 1; i++) {
		    this.move(row, i, row, i + 1);
		}
	    }
	}//for
	this.spawn();
    }//right

    /**
     * Method that is used to move all tiles on the board right
     */
    public void right() {
	for(int col = 0; col < board[0].length; col++) {
	    for(int row = board.length - 2; row >= 0; row--) {
		for(int i = row; i < board.length - 1; i++) {
		    this.move(i, col, i + 1, col);
		}
	    }
	}//for
	this.spawn();
    }//up

    /**
     * Method that is used to move all tiles on the board left
     */
    public void left() {
	for(int col = 0; col < board[0].length; col++) {
	    for(int row = 1; row < board.length; row++) {
		for(int i = row; i > 0; i--) {
		    this.move(i, col, i - 1, col);
		}
	    }
	}//for
	this.spawn();
    }//down

    /**
     * This is a method that used for all of the movement throughout the
     * game. This method doesn't necessarily "move" the tiles so to speak,
     * but rather "swap" if the first coordinates aren't an empty {@code Tile}.
     * This method also "joins" two {@code Tile} together if they are
     * the same number. If a move is succesfully done, then it IS NOT an empty
     * move.
     * @param x1 x coordinate for Tile to be moved
     * @param y1 y coordinate for Tile to be moved
     * @param x2 x coordinate for movement location
     * @param y2 y coordinate for movement location
     */
    public void move(int x1, int y1, int x2, int y2) {
	if(board[x2][y2].isEmpty() && !board[x1][y1].isEmpty()) {
	    board[x2][y2].setNum(board[x1][y1].getNum());
	    board[x1][y1].setNum(0);
	    System.out.println("moved");
	    emptyMove = false;
	}else if(!board[x1][y1].isEmpty() && !board[x2][y2].isEmpty() &&
		 board[x1][y1].getNum() == board[x2][y2].getNum()) {
	    board[x2][y2].setNum(board[x2][y2].getNum() +
				 board[x1][y1].getNum());
	    board[x1][y1].setNum(0);
	    System.out.println("joined");
	    emptyMove = false;
	}
    }//move

    /**
     * Spawns a random {@code Tile} IF IT IS NOT AN EMPTY MOVE!!!
     */
    public void spawn() {
	if(!checkEmptyMove()) {
	    this.randTile();
	}else {
	    System.out.println("empty move");
	}
    }//spawn

    /** 
     * Spawns a random Tile regardless of the circumstances. Done
     * by picking either a 2 or 4 and finding a {@code Tile} on
     * the board that is empty.
     */
    public void randTile() {
	int rand = 2 * ((int)(Math.random()) * 2 + 1);
	int x;
	int y;
	do {
	    x = (int)(4 * Math.random());
	    y = (int)(4 * Math.random());
	}while(!board[x][y].isEmpty());
	board[x][y].setNum(rand);
    }//randTile

    /**
     * Checks to see if a move is "empty". An empty move is when the
     * player of the game presses a button for a move and no tiles
     * move when pressed. For example, if there are 4 tiles at the top row
     * of the board, and the player presses up, then it is an empty move
     * since none of the tiles are able to go any further up. This is
     * used to prevent a player making an empty move and spawning a
     * random tile because of it.
     * @return true if it is empty and false if not.
     */
    private boolean checkEmptyMove() {
	if(emptyMove == false) {
	    emptyMove = true;
	    return false;
	}//if - a successful move is done (not empty)
	return true;
    }//checkEmptyMove

    /**
     * A subclass used directly for {@code Board2048}. A tile is really only
     * a {@code Rectangle} with a specific number value associated with it.
     * This number value is used to find an image for its fill and for the join
     * functionality of the game.
     */
    class Tile extends Rectangle {
	private int number; /** Number value of Tile */

	/**
	 * Constructor that creates an empty Tile. The parameters passed in are
	 * only the x and y coordinates from the 2-D array, not the overall
	 * {@code Group}. These coordinates are translated into x and y coordinates
	 * used for the group layout.
	 * @param x x coordinate from array
	 * @param y y coordinate from array
	 */
	public Tile(int x, int y) {
	    super((x * 100) + (x * 10), (y * 100) + (y * 10), 100, 100);
	    number = 0;
	    this.setFill(Color.GRAY);
	}//empty Tile constructor

	/**
	 * Sets the value of the number for the tile. Will also change the image
	 * depending on the number value passed inside.
	 * @param num int value used set new number value
	 */
	public void setNum(int num) {
	    number = num;
	    if(num == 0) {
		this.setFill(Color.GRAY);
	    }else {
		Image i = new Image("2048/" + num + ".jpg");
		this.setFill(new ImagePattern(i));
	    }
	}//setNum

	/**
	 * Returns the number value of tile.
	 * @return int value
	 */
	public int getNum() {
	    return number;
	}//getNum

	/**
	 * Checks if tile is empty. A tile is empty if the value
	 * associated with it is the number '0'. Empty tiles are
	 * representative of empty spaces on the board.
	 * @return true if empty and false if not
	 */
	public boolean isEmpty() {
	    if(number == 0) {
		return true;
	    }//if - Tile is empty if holding 0
	    return false;
	}//isEmpty
    }//Tile
}
