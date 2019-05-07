package cs1302.arcade;

import javafx.application.Platform;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;




/**
 *This is Arcade game called Space Invaders
 */
public class ArcadeSpaceInvaders extends Scene{
    AnimationTimer timer; // animationt imer
    Pane root = new Pane(); // makes a pane
    Monster[][]  monsters = new Monster[8][6];
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

    int killed =0;
    double enemyspeed = 2.0;
    boolean game = true;
    Timeline timeline;
    
/**
 *This is the main constructor of the game
 *@param main Scene
 */
    public ArcadeSpaceInvaders(Scene main) {
        super(new Pane(), 650, 500);
        if(game){ // plays the game
            this.setRoot(root);
            
            MenuBar menubar = new MenuBar();
            Menu options = new Menu("Options");
            menubar.getMenus().add(options);
            MenuItem close = new MenuItem("Close");
            
            close.setOnAction(e -> {
                    Thread t = new Thread(() -> {
                            Stage swap = (Stage) this.getWindow();
                            Platform.runLater(() -> swap.setScene(main));
                    });
                    t.setDaemon(true);
                    t.start();
                });
            options.getItems().addAll(close);
            root.getChildren().add(menubar);
            root.setStyle("-fx-background-color: black;");
            text();//makes the texts
            player = player();
            root.getChildren().add(player);
            create();
            press();
        }
    }
    /**
     *This method controls what each key does
     */
    public void press(){
        this.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.RIGHT) { // if right is pressed
                    if(player.getLayoutX() >= 600){ 
                        player.setLayoutX(player.getLayoutX());
                    }  // makes it move to the right
                    else{
                        player.setLayoutX(player.getLayoutX() + 25);
                    }
                }
                if(e.getCode() == KeyCode.LEFT) {//if left is pressed
                    if(player.getLayoutX() <= 0){
                        player.setLayoutX(player.getLayoutX());
                    } // makes it move to the left
                    else{
                        player.setLayoutX(player.getLayoutX() - 25);
                    }
                }
                if((e.getCode() == KeyCode.SPACE )){ // if space is pressed
                    shoot();
                    shoot1(); // both the enemy and the player will shoot
                    for(int i = pressed; i < mag.size();i++){
                        root.getChildren().add(mag.get(i));
                        update(mag.get(i)); // adds rounds to the magazine
                        try{
                            if(mag1.get(i) ==null){
                            }
                            else{
                                root.getChildren().add(mag1.get(i));
                            }
                            if(dotR1 !=null){
                                update1(mag1.get(i)); // adds rounds the the mag
                            }
                        }
                        catch(Exception o){
                        }
                        pressed++;
                    }
                }
            });
    }
    /**
     *This helper method creates the monsters
     */
    public void create(){
        for(int i =0;i < 8;i++){
            for(int j = 0;j<6;j++){
                if(i>=6){
                    monsters[i][j] = new Monster(i,j); // buffer monsters
                    monsters[i][j].setDisable(true);
                }
                else{
                    monsters[i][j] = new Monster(i,j); // regular monsters
                    root.getChildren().add(monsters[i][j]);
                }
            }

        }

        timer = new AnimationTimer() { // animation timer for bullets
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


    }
    /**
     *This helper method creates the texts you see on the top
     */
    public void text(){
        lives = new Text("Lives: 2"); // give out lives
        lives.setLayoutX(20);
        lives.setLayoutY(50);
        lives.setFont(Font.font("verdana", FontWeight.MEDIUM,FontPosture.REGULAR, 20));
        lives.setFill(Color.GREEN);
        points = new Text("Points: 0"); // tells you the points
        points.setLayoutX(470);
        points.setLayoutY(50);
        points.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 20));
        points.setFill(Color.GOLD);
        rounds = new Text("Wave: 1"); // tells you the wave
        rounds.setLayoutX(250);
        rounds.setLayoutY(50);
        rounds.setFont(Font.font("verdana", FontWeight.MEDIUM,FontPosture.REGULAR, 20));
        rounds.setFill(Color.BLUE);
        root.getChildren().addAll(lives, points,rounds);
        
    }
    /**
     *This method controls the players bullets speed
     @param dotR Rectangle
    */
    public void update(Rectangle dotR){
        boolean visible = true;
        double speed = 7.0;
        double y = dotR.getY();
        y = y - (speed +enemyspeed); // speed increases as the rounds go by
        dotR.setY(y); 
        if(y <100){
            visible =false; 
        }
        hit(dotR); // checks if it hit something
    }

    /**
     *This method checks if the bullet hit the enemy
     *@param dotR Rectangle
     *@throw NullPointer Exception - null enemys
     */
    public void hit(Rectangle dotR) throws NullPointerException{
        boolean killedOnce = false; // if havent killed yet
        if(dotR.getY() < 100){
            root.getChildren().remove(dotR); // removes bullet if goes too far
        }
        for(int j =0;j<6;j++){
            for(int k =0;k<6;k++){
                if(monsters[j][k] == null || dotR == null){
                }
                else if(
                    ((dotR.getX() > monsters[j][k].getX())
                     && ((dotR.getX() < monsters[j][k].getX()+30))
                     && ((dotR.getY() > monsters[j][k].getY())
                         && ((dotR.getY() < monsters[j][k].getY()+30 )))))
                    
                { // if the bullet hits the enemy
                    dotR = null; // dead bullet
                    root.getChildren().remove(dotR); 
                    root.getChildren().remove(monsters[j][k]);
                    monsters[j][k] = null; // removes the bullet and monster and make them null
                    dotR1=null;
                    killedOnce = true; // killed
                    killed++; // add killed
                    numPoints += 10; // adds points
                    points.setText("Points: " + String.valueOf(numPoints));
                }
            }
            
        }
    }
    
    /**
     *This method will shoot the actual rectangle bullet
     */
    public void shoot(){
        dotR = new Rectangle(player.getLayoutX()+12,player.getLayoutY()+5,4,10);
        dotR.setStroke(Color.YELLOW); // makes the bullet yellow
        dotR.setFill(Color.YELLOW);
        hit(dotR); //hecks if the bullet hit something
        mag.add(dotR);// adds the bullet to the mag
         
    }
    /**
     *This method will shoot the enemy rectangle at random locations
     *depending on how fast the player is shooting
     */
    public void shoot1(){
        Random r = new Random();
        int num = r.nextInt((5 - 0) + 1) + 0;
        if((monsters[num][num] == null)){
            mag1.add(dotR1); // adds the bullet
        }
        else{
            dotR1 = new Rectangle(monsters[num][num].getX(),monsters[num][num].getY(),4,10);
            if(monsters[num][num].getX() >=700) {
                moveleft = true; // if the monster is too far to the right
                System.out.println("1");
            }
            if(monsters[num][num].getX() <= 0){
                moveleft= false;//if the monster is too far to the left
                System.out.println("A");
            }
            dotR1.setStroke(Color.RED); // sets the bullets color to red
            dotR1.setFill(Color.RED);
            hit1(dotR1); // checks if hit
            mag1.add(dotR1); // adds the bullet
        }
    }
    /**
     *This method updates the enemybullet and moves it
     *@param dotR1 Rectangle
     */
    public void update1(Rectangle dotR1){
        if(dotR1 == null){
            root.getChildren().remove(dotR1);
        } // removes if the null bullet
        else{
            if(dotR1.getY() >=500){
                hit1(dotR1); // if past a point checks for hit
                root.getChildren().remove(dotR1); // else remove cause too far
            }else{
                double speed1 = 3.0;
                double y1 = dotR1.getY();
                y1 = y1 + speed1+enemyspeed; // adds speed based on round
                dotR1.setY(y1); 
                dotR1.setX(dotR1.getX());
                hit1(dotR1); // checks if it hit
            }

        }
    }
    /**
     *This mthod checks if the enemy bullet hit the player
     *@param dotR1 Rectangle
     *@throw NullPointerException
     */
    public void hit1(Rectangle dotR1) throws NullPointerException{
        if(dotR1 ==null){
            root.getChildren().remove(dotR1);
            playerhit=false; // players isnt hit cause bulet is null
        }
        else{
            try{
                if(!playerhit){
                    if(dotR1.getY() >=500){
                        root.getChildren().remove(dotR1);
                        dotR1 = null; // if the bullet strays past the enemy
                    }
                    else if( !player.isDisable()
                             && ((dotR1.getX() > player.getLayoutX())
                                 && ((dotR1.getX() < player.getLayoutX()+30))
                                 && ((dotR1.getY() > player.getLayoutY())
                                     && ((dotR1.getY() < player.getLayoutY()+30 )))))
                    { // if the bullet hits the player
                        playerhit= true;
                        player.setDisable(true); // gain invinsibility
                        dotR1 = null;
                        root.getChildren().remove(dotR1); // removes bullet
                        player.setLayoutX(275);
                        if(player.isDisable()){
                            Thread.sleep(1); // stays invincsible for a second
                            numLives--; // decreases lives to 1
                            player.setDisable(false); // no longer invincsible
                        }
                        lives.setText("Lives: " + String.valueOf(numLives));
                    }
                }
            }catch(Exception p){};
        }
    }
             
    /**
     *Returns the mag of the rectangle bullets
     */
    public ArrayList getMag(){
        return mag;
    }
    /**
     *The main update that controls the enemymovement and checks if the win or lose
     */
    public void gameUpdate() {
        player.setDisable(false);
        monstersMove(monsters); // moves the monsters
        for(int i = 0; i < mag.size();i++){
            update(mag.get(i)); // updates bullets
            hit(mag.get(i)); // checks if hit
            update1(mag1.get(i)); // updates enemy bullets
            hit1(mag1.get(i)); // checks it hit
        }
        isWin(); // goes to next round
        isLost(); // you lost
    }
    
 
    /**
     *This method will move the 2D array of monsters
     @param monsters Monster[][]
     @throws NullPointerException
    */
    public void monstersMove(Monster[][] monsters) throws NullPointerException{
        if(killed ==0){ // if havent kiled yet
            for (Monster[] u: monsters) {
                Arrays.stream(u)
                    .filter(val -> val != null)
                    .toArray(); // filters out all of the null monsters
                for (Monster mon: u) {
                    if(mon == null){
                    }
                    else if(mon.getX() >=750){
                        moveleft=true;
                    }//move to the left
                    else if(mon.getX() <=0){
                        moveleft=false;
                    }//move to the right
                    if(mon ==null){
                    }
                    if ((!moveleft) && mon !=null){            
                        mon.moveRight();
                    }//move to the right
                    if ((moveleft) && mon!=null){
                        mon.moveLeft();
                    }//move to the left
                }
            }
        }
        else{
            killedMove(monsters); // move with killed monsters
        }
    }
    /**
     *This helper method will move the monsters even after some of them have died
     @param monsters Monster[][]
     @throws NullPointerException
    */
    public void killedMove(Monster[][] monsters) throws NullPointerException{
        for(int i =0;i<8;i++){
            for(int j= 0;j<6;j++){
                if(monsters[i][j] ==null){
                } // dont do anything
                else if(monsters[i][j].getX() >=750){
                    moveleft=true;
                }//move to the left
                else if(monsters[i][j].getX() <=0){
                    moveleft=false;
                }//move to the right
                if(monsters[i][j]==null){
                }
                if ((!moveleft) && monsters[i][j]!=null){
                    monsters[i][j].moveRight();
                } //move tot the right
                if ((moveleft) && monsters[i][j] !=null){
                    monsters[i][j].moveLeft();
                } //move to the left
            }
        }
    }
    
    /**
     *This will create the player
     *@return i ImageView
     */
    public ImageView player() {
        ImageView i = new ImageView(new Image("space/Spaceship-PNG-File.png"));
        i.setLayoutX(225); // self explanitory
        i.setLayoutY(450);// set the player to a coordinate
        i.setFitHeight(30);
        i.setFitWidth(30);
        return i;
    }
    /**
     *This will create the monster at a coordinate
     *@param x double
     *@param y double
     *@return i ImageView
     */
    public ImageView monster(double x, double y) {
        ImageView i = new ImageView(new Image("space/Space-Invaders-PNG-Free-Download.png"));
        i.setLayoutX(x); // self explanitory
        i.setLayoutY(y); // makes the player at the coordinate
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i; 
    }
    /**
     *Method to restart for the next, more difficult round
     */
    public void isWin(){
        if(killed == 36 * round) {
            round++; // adds the next round
            timer.stop(); 
            timeline.pause(); // stops everything to remove and add
            root.getChildren().remove(player); //removes player
            for(Rectangle r:mag){
                r = null;
                root.getChildren().remove(r);  // empties the mag
            }
            for(Rectangle r1:mag1){
                r1= null;
                root.getChildren().remove(r1); //empties the enemy mag
            }
            for(int i =0;i < 8;i++){
                for(int j = 0;j<6;j++){
                    root.getChildren().remove(monsters[i][j]);//removes monsters
                    monsters[i][j] = new Monster(i,j); // adds the monsters agian
                    if(i !=6 && i!=7){
                        root.getChildren().add(monsters[i][j]); // hides the buffer monsters
                    }
                }
            }  
            root.getChildren().add(player); //adds the player
            player.setDisable(true); // disables the player for a bit
            timer.start();
            timeline.play(); // start the timer again
            highscore = numPoints; // makes high score equal the current
            points.setText("Points: " + String.valueOf(numPoints));                
            rounds.setText("Wave: "+ String.valueOf(round));
            enemyspeed=enemyspeed +.45; // increases the enemy speed each round
        }
    }


    /**
     *Method to check if the player lost
     */
    public void isLost(){
        if(numLives <=0) { // if they dead
            Text text = new Text();
            text.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 25));
            text.setX(180);
            text.setY(250);    
            text.setFill(Color.WHITE);
            text.setStrokeWidth(2);
            text.setStroke(Color.RED);        
            text.setText("YOU LOST" + "\n" + "close the game and play again");
            root.getChildren().add(text);
            timer.stop(); // stops game
            game = false; // your dead gg close 
        }
    }
    /**
     *This class is of the monsters
     */
    class Monster extends ImageView{
        Image img = new Image("space/Space-Invaders-PNG-Free-Download.png");
        public Monster(int x,int y){
            super(); // makes the monsters spaced out in the 2d array
            this.setX(100+(x*50) +(x*5));
            this.setY(100+(y*25) +(y*5));
            this.setImage(img); // self explanatory
            this.setFitWidth(25);
            this.setFitHeight(25);

             
        }

        /**
         *This method moves the monster to the left
         */
        void moveLeft(){
            if(getX() == 0){
                moveleft = false; // if it reaches the end stp and move right
            }
            movedown = false;
            setX(getX()-enemyspeed); // moves the left faster and faster each round
            
        }

        /**
         *This method moves the monster to the left
         */
        void moveRight(){
            if(getX() == 800){
                moveleft = true; // if it reaches to far, moves to the left
            }
            movedown = false; 
            setX(getX()+enemyspeed);//moves the right faster and faster each round
        }
    }
}
