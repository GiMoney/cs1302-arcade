package cs1302.arcade;

import java.util.ArrayList;
import java.util.List; 
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *This is Arcade game called Space Invaders
 * probably will change dramatically
 */
 public class ArcadeSpaceInvaders extends Application{
    AnimationTimer timer; // animationt imer
    Pane root = new Pane(); // makes a pane
    List<ImageView> monsters = new ArrayList<ImageView>();
    ImageView player; // player
    Circle dotR = new Circle(); // for the pew pew
    boolean toRight = true; 
    Text lives; // the lives
    Text points; // the points
    int numPoints = 0; // the number of points
    int numLives = 10; // the number of lives

     /**
      *This is the main
      */
    public static void main(String[] args) {
        launch(); // launches the game
    }

     /**
      *This creates my stage that holds all the magic  
      */
    @Override
    public void start(Stage stage) throws Exception {
       
        //life and points       
        lives = new Text("Lives: 10");
        lives.setLayoutX(20);
        lives.setLayoutY(30);
        lives.setFont(Font.font("verdana", FontWeight.ITALIC,FontPosture.REGULAR, 20));
        lives.setFill(Color.GREEN);
        points = new Text("Points: 0");
        points.setLayoutX(350);
        points.setLayoutY(30);
        points.setFont(Font.font("verdana", FontWeight.ITALIC, FontPosture.REGULAR, 20));
        points.setFill(Color.GOLD);
        root.getChildren().addAll(lives, points);
       
        //dot that regulates moving of monsters
        dotR.setLayoutX(0);
       
        //player
        player = player();
        root.getChildren().add(player);
       
 
        timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                gameUpdate();
            }
        };
        timer.start();
       
        //timeline for making monsters shoots every few seconds
       
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
       
        //setting up stage
        Scene scene = new Scene(root, 500, 700);
        scene.setFill(Color.BLACK);
        //moving player
        scene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.RIGHT) {
                player.setLayoutX(player.getLayoutX() + 8);
            }
            if(e.getCode() == KeyCode.LEFT) {
                player.setLayoutX(player.getLayoutX() - 8);
            }
           
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Space Invaders");
        primaryStage.show();
       
    }
   
    public void gameUpdate() {
     
        monstersMove();
        //is Player win
        isWin();
        //is Player lost
        isLost();
    }
   
 
 
   
    public ImageView player() {
        //make sure to change later because image is ugly
        String url = "http://www.pngmart.com/files/3/Spaceship-PNG-File.png";
        ImageView i = new ImageView(new Image(url));
        i.setLayoutX(225);
        i.setLayoutY(0);
        i.setFitHeight(100);
        i.setFitWidth(100);
        return i;
    }

    public ImageView monster(double x, double y) {
  
        String url1 = "http://www.pngmart.com/files/4/Space-Invaders-PNG-Free-Download.png";
        ImageView i = new ImageView(new Image(url1));
        i.setLayoutX(x);
        i.setLayoutY(y);
        i.setFitHeight(100);
        i.setFitWidth(100);
        return i;
    }
 
public void isWin(){
    if(monsters.isEmpty()) {
          Text text = new Text();
          text.setFont(Font.font("verdana", FontWeight.ITALIC, FontPosture.REGULAR, 50));
          text.setX(180);
          text.setY(300);    
          text.setFill(Color.GOLD);
          text.setStrokeWidth(2);
          text.setStroke(Color.WHITE);        
          text.setText("YOU WIN");
          root.getChildren().add(text);
          timer.stop();
    }
}
 
public void isLost(){
    if(numLives <= 0) {
          Text text = new Text();
          text.setFont(Font.font("verdana", FontWeight.ITALIC, FontPosture.REGULAR, 50));
          text.setX(180);
          text.setY(300);    
          text.setFill(Color.WHITE);
          text.setStrokeWidth(2);
          text.setStroke(Color.RED);        
          text.setText("YOU LOST");
          root.getChildren().add(text);
          timer.stop();
    }
}
 
}
