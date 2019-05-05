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
        Button play2048 = new Button("Play 2048!");
        Button playSpace = new Button("Play Space Invaders!");
        hbox.getChildren().addAll(play2048, playSpace);
        Scene sceneArcade = new Scene(hbox, 640, 480);

        Board2048 group = new Board2048();
	Arcade2048 scene2048 = new Arcade2048(new Board2048(), sceneArcade);
	play2048.setOnAction(e -> {
		Thread t = new Thread(() -> {
			Platform.runLater(() -> stage.setScene(scene2048));
			Platform.runLater(() -> stage.setTitle("2048!"));
			Platform.runLater(() -> stage.sizeToScene());
			scene2048.getBoard().requestFocus();
		});
		t.setDaemon(true);
		t.start();
	    });

	
	ArcadeSpaceInvaders sceneSpace = new ArcadeSpaceInvaders();
	playSpace.setOnAction(e -> {
		Thread t = new Thread(() -> {
			stage.setScene(sceneSpace);
			stage.setTitle("Space Invaders!");
			stage.sizeToScene();
		});
		t.setDaemon(true);
		t.start();
	    });
	
        stage.setTitle("Arcade!");
        stage.setScene(sceneArcade);
        stage.sizeToScene();
        stage.show();

        // the group must request input focus to receive key events
        // @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
        //group.requestFocus();

    } // start

} // ArcadeApp
