package cs1302.arcade;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
    private int score; /** value all of tiles on the board */
    private Text textScore; /** displays the score on the scene */
    private Rectangle scoreBox; /** rectangle used as a background for score */
    private Alert loseAlert; /** alert that notifies user that they lost */
    private Alert winAlert; /** alert that notifies user they got a 2048 */

    /**
     * Default constructor that is used to create a new game of 2048. Each
     * game begins with an empty board filled with empty {@code Tile}. Then
     * two random tiles will be give a value of 2 or 4.
     */
    public Board2048() {
	super();
	score = 0;
	board = new Tile[4][4];
	for(int row = 0; row < board.length; row++) {
	    for(int col = 0; col < board[row].length; col++) {
		board[row][col] = new Tile(row, col);
		this.getChildren().add(board[row][col]);
	    }//for - goes through columns
	}//for - goes through rows
	Text current = new Text(450, 30, "Current Score:");
	current.setFill(Color.WHITE);
	current.setFont(new Font(18));
	textScore = new Text(450, 90, "" + score);
	textScore.setFill(Color.WHITE);
	textScore.setFont(new Font(40));
	textScore.setTextAlignment(TextAlignment.CENTER);
	scoreBox = new Rectangle(440, 0, 150, 100);
	scoreBox.setFill(Color.GRAY);
	scoreBox.setArcWidth(20);
	scoreBox.setArcHeight(20); //all of this creates a neat space for display the score
	this.getChildren().addAll(scoreBox, current, textScore);
	this.randTile();
	this.randTile(); //creates two random tiles on the board
	this.setOnKeyPressed(this.createKeyEvent());
	Platform.runLater(() -> loseAlert = new Alert(AlertType.INFORMATION, "You lose!"));
	Platform.runLater(() -> winAlert = new Alert(AlertType.INFORMATION, "You win! You can continue" +
						     " playing to see how high of a score you can get!"));
    }//board constructor

    /**
     * Used to create a new game of 2048 if the player desires to start
     * over or has lost. The score will be reset but the score of the last
     * run will still be display until the user makes another move.
     */
    public void newGame() {
	score = 0;
	for(int row = 0; row < board.length; row++) {
	    for(int col = 0; col < board[row].length; col++) {
		board[row][col].setNum(0);
	    }//for - goes through columns and resets Tiles
	}//for - goes through rows
	this.randTile();
	this.randTile();
    }//newGame

    /**
     * A helper method that is used to create an {@code Event} used for
     * keyboard controls.
     * @return Event for using directional keys for movement
     */
    private EventHandler<? super KeyEvent> createKeyEvent() {
	return event -> {
	    Runnable r = () -> {
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
	    }; //implementation of Runnable for thread
	    Thread t = new Thread(r);
	    t.setDaemon(true);
	    t.start();
	};//returns an event used for keyboard controls
    }//createKeyEvent

    /**
     * Method that is used to move all tiles on the board up
     */
    public void up() {
	for(int row = 0; row < board.length; row++) {
	    for(int col = 1; col < board[row].length; col++) {
		for(int i = col; i > 0; i--) {
		    this.move(row, i, row, i - 1);
		}//for - moves a singular tile up
	    }//for - goes through column low to high
	}//for - goes through rows
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
		}//for - moves a singular tile down
	    }//for - goes through columns high to low
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
		}//for - moves a singular tile right
	    }//for - goes through rows high to low
	}//for - goes through columns
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
		}//for - moves a singular tile left
	    }//for - goes through rows low to high
	}//for - goes through columns
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
	    board[x1][y1].setNum(0); //if tile destination is empty, then
	    emptyMove = false;       //the tile will move to empty slot  
	}else if(!board[x1][y1].isEmpty() && !board[x2][y2].isEmpty() &&
		 board[x1][y1].getNum() == board[x2][y2].getNum() &&
		 board[x1][y1].getNum() < 2048) {
	    board[x2][y2].setNum(board[x2][y2].getNum() +
				 board[x1][y1].getNum());
	    board[x1][y1].setNum(0); //joins two numbers of same value
	    if(board[x2][y2].getNum() == 2048) {
		Platform.runLater(() -> winAlert.showAndWait());
	    }//if - 2048 tile is created, a win message will be displayed
	    score += board[x2][y2].getNum(); //updates score
	    Platform.runLater(() -> textScore.setText("" + score));
	    emptyMove = false;
	}//if-else - checks to see if a move is "swap" or a "join"
    }//move

    /**
     * Spawns a random {@code Tile} IF IT IS NOT AN EMPTY MOVE!!! Will
     * also check to see if a player has lost if the board fills up with
     * a spawned tile.
     */
    public void spawn() {
	if(!checkEmptyMove()) {
	    this.randTile();
	    this.checkLose(); //checks if the player has lost
	}//checks to see if move is NOT empty
    }//spawn

    /** 
     * Spawns a random Tile regardless of the circumstances. Done
     * by picking either a 2 or 4 and finding a {@code Tile} on
     * the board that is empty.
     */
    public void randTile() {
	int rand = 2 * ((int)(Math.random() * 2 + 1));
	int x; //x coord
	int y; //y coord
	do {
	    x = (int)(4 * Math.random());
	    y = (int)(4 * Math.random());
	}while(!board[x][y].isEmpty()); //finds empty spot
	board[x][y].setNum(rand); //sets empty tile to either 2 or 4
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
     * Checks to see if the player has lost the game. This means that the
     * board is full and there are no possible moves left. Makes use
     * of a another method {@code checkPossible} to see if board is possible
     * if full.
     */
    private void checkLose() {
	boolean fullBoard = true;
	for(int r = 0; r < board.length; r++) {
	    for(int c = 0; c < board[r].length; c++) {
		if(board[r][c].getNum() == 0) {
		    fullBoard = false;
		}//if - tile is empty then board isn't full
	    }//for - checks columns
	}//for - checks rows
	if(fullBoard){
	    if(!this.checkPossible()) {
		Platform.runLater(() -> loseAlert.showAndWait());
	    }//if - impossible, then prompt lose alert
	}//if - board is full of tiles
    }//checkLose

    /**
     * Checks to see if a board has any possible moves ASSUMING the board
     * is full (since this is a helper method for {@code checkLose}. This is
     * done by checking each row and column and their "neighbors" inside them.
     * "Neighbors" means the tiles directly next to the tile being checked.
     * @return true if possible, false if impossible
     */
    private boolean checkPossible() {
	for(int r = 0; r < board.length; r++) {
	    for(int c = 0; c < board[r].length - 1; c++) {
		if(board[r][c].getNum() == board[r][c+1].getNum()) {
		    return true;
		}//if - all column neighbors are different from each other
	    }//for - checks columns
	}//for - checks rows
	for(int c = 0; c < board[0].length; c++) {
	    for(int r = 0; r < board.length - 1; r++) {
		if(board[r][c].getNum() == board[r+1][c].getNum()) {
		    return true;
		}//if - all row neighbors are different from each other
	    }//for - checks rows
	}//for - checks columns
	return false;
    }//checkPossible

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
	    number = 0; //sets tile to empty and coord based on array location
	    this.setFill(Color.GRAY);
	    this.setArcHeight(20); //creates curved edges
	    this.setArcWidth(20);
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
		this.setFill(new ImagePattern(i)); //uses value to find image
	    }//if-else - 0 for empty tile and other numbers for image files
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
}//Board2048
