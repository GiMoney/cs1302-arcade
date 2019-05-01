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

//Some of this code was used as reference will not use in final submission
// Just used to test to see if code worked.
/**
 *This is Arcade game called Space Invaders
 * probably will change dramatically
 */
 public class ArcadeSpaceInvaders extends Scene{
     AnimationTimer timer; // animationt imer
     Pane root = new Pane(); // makes a pane
     // List<ImageView> monsters = new ArrayList<ImageView>();
     Monster[][]  monsters = new Monster[6][6];
     ImageView player; // player
     Circle dotR = new Circle(); // for the pew pew
    boolean toRight = true; 
    Text lives; // the lives
    Text points; // the points
    int numPoints = 0; // the number of points
    int numLives = 10; // the number of lives

     /**
      *This creates my stage that holds all the magic  
      */
    public ArcadeSpaceInvaders() {
	super(new Pane(), 600, 500);
	this.setRoot(root);
	this.setFill(Color.BLACK);
       
        //life and points       
        lives = new Text("Lives: 10");
        lives.setLayoutX(20);
        lives.setLayoutY(30);
        lives.setFont(Font.font("verdana", FontWeight.MEDIUM,FontPosture.REGULAR, 20));
        lives.setFill(Color.GREEN);
        points = new Text("Points: 0");
        points.setLayoutX(350);
        points.setLayoutY(30);
        points.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 20));
        points.setFill(Color.GOLD);
        root.getChildren().addAll(lives, points);
	
        //dot that regulates moving of monsters
        dotR.setLayoutX(0);
       
        //player
        player = player();
        root.getChildren().add(player);
        for(int i =0;i < 6;i++){
            for(int j = 0;j<6;j++){
                monsters[i][j] = new Monster(i,j);
                root.getChildren().add(monsters[i][j]);
            }
            
        }

       
 
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
       
        //moving player
        this.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.RIGHT) {
                player.setLayoutX(player.getLayoutX() + 8);
            }
            if(e.getCode() == KeyCode.LEFT) {
                player.setLayoutX(player.getLayoutX() - 8);
            }
           
        });
    }
   
    public void gameUpdate() {
     
        monstersMove();
        //is Player win
        isWin();
        //is Player lost
        isLost();
    }
   
 

     public void monstersMove(){
         

     }
   
    public ImageView player() {
        //make sure to change later because image is ugly
        String url = "http://www.pngmart.com/files/3/Spaceship-PNG-File.png";
        ImageView i = new ImageView(new Image(url));
        i.setLayoutX(225);
        i.setLayoutY(450);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }

    public ImageView monster(double x, double y) {
  
        String url1 = "http://www.pngmart.com/files/4/Space-Invaders-PNG-Free-Download.png";
        ImageView i = new ImageView(new Image(url1));
        i.setLayoutX(x);
        i.setLayoutY(y);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }
 
public void isWin(){
    if(false) {
          Text text = new Text();
          text.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 50));
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
          text.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 50));
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

     class Monster extends ImageView{
         Image img = new Image("http://www.pngmart.com/files/4/Space-Invaders-PNG-Free-Download.png");

         public Monster(int x,int y){
             super();
             this.setX(100+(x*50) +(x*5));
             this.setY(100+(y*25) +(y*5));
             this.setImage(img);
             this.setFitWidth(25);
             this.setFitHeight(25);
     }

     }
}
