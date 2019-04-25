package cs1302.arcade;

import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ArcadeApp extends Application {

    HBox hbox = new HBox();

    Group group = new Group();           // main container
    Random rng = new Random();           // random number generator
    Rectangle r = new Rectangle(20, 20); // some rectangle

    /**
     * Return a mouse event handler that moves to the rectangle to a random
     * position any time a mouse event is generated by the associated node.
     * @return the mouse event handler
     */
    private EventHandler<? super MouseEvent> createMouseHandler() {
	return event -> {
	    System.out.println(event);
	    r.setX(rng.nextDouble() * (640 - r.getWidth()));
	    r.setY(rng.nextDouble() * (480 - r.getHeight()));
	};
    } // createMouseHandler

    /**
     * Return a key event handler that moves to the rectangle to the left
     * or the right depending on what key event is generated by the associated
     * node.
     * @return the key event handler
     */
    private EventHandler<? super KeyEvent> createKeyHandler() {
	return event -> {
	    System.out.println(event);
	    if (event.getCode() == KeyCode.LEFT)  r.setX(r.getX() - 20.0);
	    if (event.getCode() == KeyCode.RIGHT) r.setX(r.getX() + 20.0);
	    // TODO bounds checking
	};
    } // createKeyHandler

    /** {@inheritdoc} */
    @Override
    public void start(Stage stage) {

        /* You are allowed to rewrite this start method, add other methods,
         * files, classes, etc., as needed. This currently contains some
         * simple sample code for mouse and keyboard interactions with a node
         * (rectangle) in a group.
         */

        //r.setX(300);                                // 50px in the x direction (right)
        //r.setY(440);                                // 50ps in the y direction (down)

	/*
        r.setX(50);                                // 50px in the x direction (right)
        r.setY(50);                                // 50ps in the y direction (down)
        group.getChildren().add(r);                // add to main container
        r.setOnMouseClicked(createMouseHandler()); // clicks on the rectangle move it randomly
        group.setOnKeyPressed(createKeyHandler()); // left-right key presses move the rectangle
	*/
	//hbox
        Button play2048 = new Button("Play 2048!");
        Button playSpace = new Button("Play Space Invaders!");
        hbox.getChildren().addAll(play2048, playSpace);
        Scene sceneArcade = new Scene(hbox, 640, 480);

        Board2048 group = new Board2048();
	//Scene scene2048 = new Scene(group, 500, 500);
	Arcade2048 scene2048 = new Arcade2048(new Board2048(), sceneArcade);
	play2048.setOnAction(e -> {
		stage.setScene(scene2048);
		stage.setTitle("2048!");
		stage.sizeToScene();
		scene2048.getBoard().requestFocus();
	    });

	/*
	ArcadeSpaceInvaders gameSpace = new ArcadeSpaceInvaders();
	Scene sceneSpace = new Scene(gameSpace, 640, 480);
	*/
	
        stage.setTitle("Arcade!");
        stage.setScene(sceneArcade);
        stage.sizeToScene();
        stage.show();

        // the group must request input focus to receive key events
        // @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
        //group.requestFocus();

    } // start

} // ArcadeApp
