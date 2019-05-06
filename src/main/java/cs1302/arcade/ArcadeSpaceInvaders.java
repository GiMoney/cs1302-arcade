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
import java.util.*;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;




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
    Rectangle dotR1 = new Rectangle(); // for the pew pew
    Rectangle dotR2 = new Rectangle();
    int round = 1;
    ArrayList<Rectangle> mag = new ArrayList<Rectangle>();
    ArrayList<Rectangle> mag1 = new ArrayList<Rectangle>();
    boolean toRight = true; 
    Text lives; // the lives
    Text points; // the points
    Text rounds;
    int numPoints,hit = 0; // the number of points
    int numLives = 2; // the number of lives
    int highscore =numPoints;
    boolean moveleft;
    boolean movedown,playerhit =false;
    int pressed,mpress = 0;
    Shell[] shell = new Shell[4];
    int killed =0;
    double enemyspeed = 2.0;
    boolean game = true;
    Timeline timeline;
/**
 *This creates my stage that holds all the magic  
 */
    public ArcadeSpaceInvaders() {
        
        super(new Pane(), 600, 500);
        if(game){
            
            this.setRoot(root);
            
            root.setStyle("-fx-background-color: black;");
            
            
            //life and points       
            lives = new Text("Lives: 2");
            lives.setLayoutX(20);
            lives.setLayoutY(30);
            lives.setFont(Font.font("verdana", FontWeight.MEDIUM,FontPosture.REGULAR, 20));
            lives.setFill(Color.GREEN);
            points = new Text("Points: 0");
            points.setLayoutX(470);
            points.setLayoutY(30);
            points.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 20));
            points.setFill(Color.GOLD);
            rounds = new Text("Wave: 1");
            rounds.setLayoutX(250);
            rounds.setLayoutY(30);
            rounds.setFont(Font.font("verdana", FontWeight.MEDIUM,FontPosture.REGULAR, 20));
            rounds.setFill(Color.BLUE);
            root.getChildren().addAll(lives, points,rounds);
            //player
            player = player();
  
            root.getChildren().add(player);
            for(int i =0;i < 6;i++){
                for(int j = 0;j<6;j++){
            
                    monsters[i][j] = new Monster(i,j);
                    root.getChildren().add(monsters[i][j]);
                }
        
            }

            for(int i = 0; i<4;i++){
                shell[i] = new Shell(i,375);
                root.getChildren().add(shell[i]);
            }
    
            /*ArrayList bullets = mag;
            for (int i = 0; i < bullets.size(); i++) {
                Bullet laser = (Bullet) bullets.get(i);
                if (laser.isVisible() == true) {
                    laser.update();
            
                } else {
                    bullets.remove(i);
                }
            }
    
            */
    
            timer = new AnimationTimer() {
                    @Override
                    public void handle(long arg0) {
                        gameUpdate();
                    }
                };
            timer.start();
       
            //timeline for making monsters shoots every few seconds
       
            timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        
            //moving player
            this.setOnKeyPressed(e->{
                     
                     
                    if(e.getCode() == KeyCode.RIGHT) {
                        if(player.getLayoutX() >= 540){
                            player.setLayoutX(player.getLayoutX());
                        }
                        else{ 
                            player.setLayoutX(player.getLayoutX() + 15);
                        }
                        
                    }
                    if(e.getCode() == KeyCode.LEFT) {
                        if(player.getLayoutX() <= 0){
                            player.setLayoutX(player.getLayoutX());
                        }
                        else{
                            player.setLayoutX(player.getLayoutX() - 15);
                        }
                        
                    }
                    if((e.getCode() == KeyCode.SPACE )){
                        shoot();
                        shoot1();
                        //dotR = new Rectangle(player.getLayoutX()+20,player.getLayoutY()+5,4,10);
                        for(int i =pressed; i < mag.size();i++){
                            root.getChildren().add(mag.get(i));
                            // if(dotR!=null){
                            update(mag.get(i));
                            //   }
                            
                            try{
                                if(mag1.get(i) ==null){
                                }
                                else{
                                    root.getChildren().add(mag1.get(i));
                                }
                                if(dotR1 !=null){
                            
                                    update1(mag1.get(i));
                                }
                            }
                            catch(Exception o){
                            }
                            pressed++;
                        }
                    }
                
                });
        }
    }

    public void update(Rectangle dotR){
        // if(dotR == null){

        // }
        // else{
        boolean visible = true;
        double speed = 7.0;
        double y = dotR.getY();
        y = y - speed;
        dotR.setY(y);
        if(y <100){
            visible =false;
            // mag.remove(dotR);
        }
        hit(dotR);
        //  for(Monster[] u:monsters){
        //  for(Monster p:u){
        // }
    }

    
    public void hit(Rectangle dotR) throws NullPointerException{
        boolean killedOnce = false;
        if(dotR.getY() < 100){
            root.getChildren().remove(dotR);
        }
         
        for(int j =0;j<6;j++){
            // if(killedOnce){
            //  break;
            //}
            for(int k =0;k<6;k++){
                if(monsters[j][k] == null || dotR == null){
                    
                }
                else if(//dotR.getX() == monsters[j][k].getX() && dotR.getY() == monsters[j][k].getY())
                    ((dotR.getX() > monsters[j][k].getX())
                     && ((dotR.getX() < monsters[j][k].getX()+30))
                     && ((dotR.getY() > monsters[j][k].getY())
                         && ((dotR.getY() < monsters[j][k].getY()+30 )))))
                    
                {
                    dotR = null;
                    root.getChildren().remove(dotR); 
                     
                      
                    root.getChildren().remove(monsters[j][k]);
                    monsters[j][k] = null;
                    dotR1=null;
                    //enemyspeed=enemyspeed +.01;
                    killedOnce = true;
                    killed++;
                    numPoints += 10;
                    points.setText("Points: " + String.valueOf(numPoints));
                }
            }
             
        }
    }
    
    
    public void shoot(){
        dotR = new Rectangle(player.getLayoutX()+20,player.getLayoutY()+5,4,10);
        dotR.setStroke(Color.YELLOW);
        dotR.setFill(Color.YELLOW);
        //  Bullet bullet = new Bullet((int)player.getX()-7,(int)player.getY()+18);
        hit(dotR);
        mag.add(dotR);
         
    }
     
    public void shoot1(){
        Random r = new Random();
        int num = r.nextInt((5 - 0) + 1) + 0;
       
        if((monsters[num][num] == null)){
            // dotR1=null;
            mag1.add(dotR1);
          
        }
        else{
            //monsters[num][num].setLayoutX(getX());
            dotR1 = new Rectangle(monsters[num][num].getX(),monsters[num][num].getY(),4,10);
          
          
          
            if(monsters[num][num].getX() >=560){
                moveleft = true;
            }
            if(monsters[num][num].getX() <= 0){
                moveleft= false;
            }
            
            dotR1.setStroke(Color.RED);
            dotR1.setFill(Color.RED);
            hit1(dotR1);
            mag1.add(dotR1);
       

        }
    }
    
    public void update1(Rectangle dotR1){
        if(dotR1 == null){
            root.getChildren().remove(dotR1);
        }
        else{
            if(dotR1.getY() >=500){
                hit1(dotR1);
                root.getChildren().remove(dotR1);
            }else{
                double speed1 = 3.0;
                double y1 = dotR1.getY();
                y1 = y1 + speed1;
                dotR1.setY(y1);
                dotR1.setX(dotR1.getX());
                hit1(dotR1);
            }

        }
    }
    
    public void hit1(Rectangle dotR1) throws NullPointerException{
        if(dotR1 ==null){
            root.getChildren().remove(dotR1);
            playerhit=false;
        }
        else{
            try{
                if(!playerhit){
                    // for(Rectangle d: mag1){
                    if(dotR1.getY() >=500){
                        root.getChildren().remove(dotR1);
                        dotR1 = null;
                    }
                
                    else if( !player.isDisable() && ((dotR1.getX() > player.getLayoutX())
                              && ((dotR1.getX() < player.getLayoutX()+50))
                              && ((dotR1.getY() > player.getLayoutY())
                                  && ((dotR1.getY() < player.getLayoutY()+50 )))))
                    {
                    
                        //System.out.println("TRUE1");
                        playerhit= true;
                        player.setDisable(true);
                       
                        
                        dotR1 = null;
                        root.getChildren().remove(dotR1);
                        
                        player.setLayoutX(250);
                        
                        System.out.println("TRUE1");
                        
                        numLives--;
                        if(player.isDisable()){
                            Thread.sleep(2);
                            player.setDisable(false);
                            
                        }
                        lives.setText("Lives: " + String.valueOf(numLives));
                        
                    }
                }
                /*for(Shell u: shell){
                if((( dotR1.getX() > u.getX())
                    && ((dotR1.getX() < u.getX()+50))
                    && ((dotR1.getY() > u.getY())
                        && ((dotR1.getY() < u.getY()+50 )))))
                {
                    if(hit < 3){
                    dotR1=null;
                    root.getChildren().remove(dotR1);
                    hit++;
                    }
                    else{
                        u=null;
                        root.getChildren().remove(u);
                    }
                }
                }*/
                
                //  if(player.isDisable()){
                // Thread.sleep(10);
                     // player.setDisable(false);
                    // playerhit = false;
                    // }
            }catch(Exception p){};
        }
    }
             
 
    public ArrayList getMag(){
        return mag;
    }

    public void gameUpdate() {
       
        monstersMove(monsters);
        for(int i = 0; i < mag.size();i++){
            update(mag.get(i));
            hit(mag.get(i));
            update1(mag1.get(i));
            hit1(mag1.get(i));
        }
        //is Player win
        isWin();
        //is Player lost
        isLost();        
    }
    
 
    
    public void monstersMove(Monster[][] monsters) throws NullPointerException{
        if(killed ==0){
            for (Monster[] u: monsters) {
                Arrays.stream(u)
                    .filter(val -> val != null)
                    .toArray();
                for (Monster mon: u) {
                    if(mon.getX() >=560){
                        moveleft=true;
                    }
                    if(mon.getX() <=0){
                        moveleft=false;
                    }
                    if(mon ==null){
                    }
                    if ((!moveleft) && mon !=null){            
                        mon.moveRight();
                    }
                    if ((moveleft) && mon!=null){
                        mon.moveLeft();
                    }
                }
            }
        }
        else{
            for(int i =0;i<6;i++){
                for(int j= 0;j<6;j++){
                    if(monsters[i][j] ==null){
                    }

                    else if(monsters[i][j].getX() >=560){
                        moveleft=true;
                    }
                    else if(monsters[i][j].getX() <=0){
                        moveleft=false;
                    }
                    
                    if(monsters[i][j] ==null){
                    }
                    if ((!moveleft) && monsters[i][j] !=null){
                        monsters[i][j].moveRight();
                    }
                    if ((moveleft) && monsters[i][j] !=null){
                        monsters[i][j].moveLeft();                  }
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
        if(killed == 36 * round) {
            round++;
            timer.stop();
            timeline.pause();
            root.getChildren().remove(player);
            for(int i=0;i<4;i++){
                root.getChildren().remove(shell[i]);
            }
            for(Rectangle r:mag){
                r = null;
                root.getChildren().remove(r);
            }
            for(Rectangle r1:mag1){
                r1= null;
                root.getChildren().remove(r1);
            }
            for(int i =0;i < 6;i++){
                for(int j = 0;j<6;j++){
                    root.getChildren().remove(monsters[i][j]);
                    monsters[i][j] = new Monster(i,j);
                    root.getChildren().add(monsters[i][j]);
                }  
            }
            root.getChildren().add(player);
            for(int i = 0; i<4;i++){
                shell[i] = new Shell(i,375);
                root.getChildren().add(shell[i]);
            }
            
            timer.start();
            timeline.play();
            highscore = numPoints;
            points.setText("Points: " + String.valueOf(numPoints));                
            rounds.setText("Round: "+ String.valueOf(round));

        }
    }


    
    public void isLost(){
        if(numLives <=0) {
            Text text = new Text();
            text.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 50));
            text.setX(180);
            text.setY(250);    
            text.setFill(Color.WHITE);
            text.setStrokeWidth(2);
            text.setStroke(Color.RED);        
            text.setText("YOU LOST");
            root.getChildren().add(text);
            timer.stop();
            game = false;
        }
    }

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

             
        }

       
        void moveLeft(){
            if(getX() == 0){
                moveleft = false;
            }
            movedown = false;
            setX(getX()-enemyspeed);
        }
         
        void moveRight(){
            if(getX() == 530){
                moveleft = true;
                 
            }
            movedown = false;
            setX(getX()+enemyspeed);
        }
        void moveDown(){
            setLayoutY(getLayoutY() +10);
        }
    }
     
    /*class Bullet extends Rectangle{
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

public int getSpeed(){
return speed;
}
/*
public boolean isVisible(){
return visible;
}

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
    */
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
