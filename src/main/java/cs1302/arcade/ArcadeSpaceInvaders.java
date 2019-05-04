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
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Arrays;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.*;
import javafx.*;
import java.awt.geom.Area;

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
//     Hitbox[][] hitbox = new Hitbox[6][6];
     ImageView player; // player
      Rectangle dotR = new Rectangle(); // for the pew pew
     ArrayList<Rectangle> mag = new ArrayList<Rectangle>();
     boolean toRight = true; 
    Text lives; // the lives
    Text points; // the points
    int numPoints = 0; // the number of points
    int numLives = 10; // the number of lives
     boolean moveleft;
      boolean movedown =false;
     int pressed = 0;
     Shell[] shell = new Shell[4];
     /**
      *This creates my stage that holds all the magic  
      */
    public ArcadeSpaceInvaders() {
        super(new Pane(), 600, 500);
        this.setRoot(root);
        // root.setStyle("-fx-background-img: url(\"http://tinypic.com/r/2m691xt/9\")");
         root.setStyle("-fx-background-color: black;");
//    Image img = new Image("https://www.pixilart.com/images/art/3803661b5dce163.png");
    //  g = img.getGraphics();
//life and points       
    lives = new Text("Lives: 10");
    lives.setLayoutX(20);
    lives.setLayoutY(30);
    lives.setFont(Font.font("verdana", FontWeight.MEDIUM,FontPosture.REGULAR, 20));
    lives.setFill(Color.GREEN);
    points = new Text("Points: 0");
    points.setLayoutX(450);
    points.setLayoutY(30);
    points.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 20));
    points.setFill(Color.GOLD);
    root.getChildren().addAll(lives, points);
	
    //dot that regulates moving of monsters
//        dotR.setLayoutX(0);
    
    //player
    player = player();
    root.getChildren().add(player);
    for(int i =0;i < 6;i++){
        for(int j = 0;j<6;j++){
            
            monsters[i][j] = new Monster(i,j);
            root.getChildren().add(monsters[i][j]);
            //          hitbox[i][j] = new Hitbox(i,j);
//            root.getChildren().add(hitbox[i][j]);
        }
        
    }

    for(int i = 0; i<4;i++){
        shell[i] = new Shell(i,375);
        root.getChildren().add(shell[i]);
    }
    
    ArrayList bullets = mag;
    for (int i = 0; i < bullets.size(); i++) {
        Bullet laser = (Bullet) bullets.get(i);
        if (laser.isVisible() == true) {
            laser.update();
//            root.getChildren().add(laser);
        } else {
            bullets.remove(i);
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
                player.setLayoutX(player.getLayoutX() + 15);
                
            }
            if(e.getCode() == KeyCode.LEFT) {
                player.setLayoutX(player.getLayoutX() - 15);
            }
            if(e.getCode() == KeyCode.SPACE){
                shoot();
                // dotR = new Rectangle(player.getLayoutX()+20,player.getLayoutY()+5,4,10);
                for(int i =pressed; i < mag.size();i++){
                root.getChildren().add(mag.get(i));
                update(mag.get(i));
                }
                pressed++;
// update();
                
            }
        });

        
        
    }
     public void update(Rectangle dotR){
         boolean visible = true;
         double speed = 7.0;
         double y = dotR.getY();
         y = y - speed;
         dotR.setY(y);
         if(y <500){
             visible =false;
         }
         for(Monster[] u:monsters){
             for(Monster p:u){
                 if(overlaps(dotR,p.getrekt())){
                     System.out.println("True");
                     p = null;
             }
             }
     }
     }

   public static boolean overlaps(Rectangle shapeA, Rectangle shapeB) {
       // Area areaA = new Area(shapeA);
       //  areaA.intersect(new Area(shapeB));
       // return !areaA.isEmpty();
       return shapeA.getX() == shapeB.getX();
   }
     
     public void shoot(){
         dotR = new Rectangle(player.getLayoutX()+20,player.getLayoutY()+5,4,10);
         dotR.setStroke(Color.YELLOW);
         dotR.setFill(Color.YELLOW);
//  Bullet bullet = new Bullet((int)player.getX()-7,(int)player.getY()+18);
         mag.add(dotR);
         
     }
     public ArrayList getMag(){
         return mag;
     }

     public void paint(){
         ArrayList bullets = mag;
         for (int i = 0; i < bullets.size(); i++) {
             Bullet laser = (Bullet) bullets.get(i);
             laser.setStroke(Color.YELLOW); 
             //root.getChildren().add(laser);
         }
         

     }
    public void gameUpdate() {
     
        monstersMove(monsters);
         for(int i = 0; i < mag.size();i++){
             update(mag.get(i));
         }
//is Player win
        isWin();
        //is Player lost
        isLost();
    }
   
 

     public void monstersMove(Monster[][] monsters){
         for (Monster[] u: monsters) {
             for (Monster mon: u) {
                 if (!moveleft){            
                     mon.moveRight();
                     
                 }
                 
                 if (moveleft){
                     mon.moveLeft();
                 }
             }
         }

         
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
/*         @Override
         class Hitbox{
         public Hitbox(int x,int y){
             super();
             Rectangle rekt = new Rectangle(40, 40);
             this.setX(100+(x*50) +(x*5));
             this.setY(100+(y*25) +(y*5));
             this.setImage(img);
             this.setFitWidth(25);
             this.setFitHeight(25);
             
         }
         }*/
     class Monster extends ImageView{
         Image img = new Image("http://www.pngmart.com/files/4/Space-Invaders-PNG-Free-Download.png");
         Rectangle rekt = new Rectangle();
         public Monster(int x,int y){
             super();
             ImagePattern imagePattern = new ImagePattern(img);
             rekt.setFill(imagePattern);

            
             this.setX(100+(x*50) +(x*5));
             this.setY(100+(y*25) +(y*5));
             this.setImage(img);
             this.setFitWidth(25);
             this.setFitHeight(25);
             //ImagePattern imagePattern = new ImagePattern(img);
             //rekt.setFill(imagePattern);

             
         }

         Rectangle getrekt(){
             return rekt;
         }
         void moveLeft(){
             if(getX() == 0){
                 moveleft = false;
                 movedown = true;
             }
             
             
                 if(movedown){
                     setY(getY()+10);            
                 }
                 else{
                     movedown = false;
                     setX(getX()-2);
                 }
                
                 // movedown = false;
             
         }
         
         void moveRight(){
             if(getX() == 550){
                 moveleft = true;
                 movedown = true;
             }
                 if(movedown){
                     setY(getY()+10);
                     movedown = false;
                 }
                 else{
                     movedown = false;
                     setX(getX()+2);}
//                 setY(getY()+1);
                 // movedown =false;
             
                 
         }
         
         void moveDown(){
             setY(getY() +10);
             
             
         }
         
     }

     class Bullet extends Rectangle{
         int x,y,speed;
         boolean visible;
//         Image img = new Image(
         public Bullet(int x1,int y1){
             x=x1;
             y=y1;
             speed = 10;
             visible = true;
         }
         
         public void update(){
             y = y - speed;
             if(y <500){
                 visible =false;
             }

         }
/*
         public int getX(){
             return x;
         }

         public int getY(){
             return y;
         }
*/
         public int getSpeed(){
             return speed;
         }
/*
         public boolean isVisible(){
             return visible;
         }
         */
         public int setX(){
             this.x=x;
             return x;
         }
         public int setY(){
             this.y=y;
             return y;
         }
         public ArrayList getMag(){
          return mag;
      }

         
     }

     class Shell extends ImageView{
         Image shellimg = new Image("http://i.imgur.com/eAok2Eg.png");

         public Shell(int x, int y){
             super();
             this.setX(55 +(x*100) +(x*40));
             this.setY(y);
             this.setImage(shellimg);
             this.setFitWidth(75);
             this.setFitHeight(50);
         }
         
     }

             
     
}
