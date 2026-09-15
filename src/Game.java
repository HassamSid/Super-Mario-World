import static java.lang.Thread.sleep;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jaco.mp3.player.MP3Player; // please install the jaco mp3 library

class Game extends JFrame implements KeyListener, ComponentListener, ActionListener {
    boolean killCheck=true;
    
    Level1.animationCloud a1 = new Level1.animationCloud();
    Level1.animationMob mob1 = new Level1.animationMob();
    MainMenu m1 = new MainMenu();
    Level1 lvl1 = new Level1();
    Credits c1=new Credits();

    JButton fallArea;

    Rectangle marioHitbox = new Rectangle(0, 0, 50, 60);

    Rectangle fallHitbox = new Rectangle(200, 520, 90, 60);
    int highscore, score = 0, animationCount = 0, jumpCount = 0, levelpart = 0, switchScreen = 0;
    int checkForDiagonal = 0, checkForJump = 1;

    public static final int SCREEN_WIDTH = 900, SCREEN_HEIGHT = 630;

    //adding stuff to the game window
    Game() {
        mob1.start();
        a1.start();
        fall();
        readHighScore();
        frame();
    }
    //packing the frame with components
    public void frame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setLayout(null);
        this.setTitle("Super Mario World");
        this.setIconImage(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingRight)).getImage());
        this.setLocationRelativeTo(null);
        this.add(m1.panel);
        this.add(c1.panel);
        this.add(lvl1.panel);
        Switch();
        lvl1.m1.mario.addComponentListener(this);
        this.setVisible(true);
    }

    //read last highscore from the txt file
    public void readHighScore(){
        //Reading HighScore
        Scanner read = null;
        try {
            read = new Scanner(new File(getClass().getResource("/GameAssets/Other/HighScore.txt").getFile()));
            highscore = Integer.parseInt(read.nextLine());
            System.out.println("high: " +highscore);

        } catch (Exception e) {
            System.out.println(e);
            highscore = 0;
        }

    }

    //fall = the fall area or the water pit
    public void fall() {
        fallArea = new JButton();
        fallArea.setBounds(180, 470, 92, 60);
        fallArea.setFocusable(false);
        fallArea.setOpaque(false);
        fallArea.setContentAreaFilled(false);
        fallArea.setVisible(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keyCheck = e.getKeyCode();

        switch (keyCheck) {
            //UP
            case KeyEvent.VK_UP:
            case 32:
            case 87:
                movementUP m2 = new movementUP();
                jumpCount++;
                m2.start();
                break;
            //DOWN
            case KeyEvent.VK_DOWN:
            case 83:
                lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioDuck)));
                break;
            //RIGHT
            case KeyEvent.VK_RIGHT:
            case 68:
                right();
                break;
            //LEFT
            case KeyEvent.VK_LEFT:
            case 65:
                left();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case 32:
            case 87:
                break;
            case KeyEvent.VK_RIGHT:
            case 68:
                checkForDiagonal = 0;
                if (jumpCount == 0) {
                    lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingRight)));
                }
                if (jumpCount == 1 || jumpCount == 2) {
                    lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioJumpRight)));
                }
                break;
            case KeyEvent.VK_LEFT:
            case 65:
                checkForDiagonal = 0;
                lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingLeft)));
                break;
            case KeyEvent.VK_DOWN:
            case 83:
                lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingRight)));
                break;
        }
    }


    @Override
    public void componentMoved(ComponentEvent e) {

        marioHitbox.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY());
        //If mario goes full to the right,
        if (lvl1.m1.mario.getX() > 870) {
            if (lvl1.m1.mario.getX() > 870)
                screenChange(levelpart, 0);
            lvl1.background.setLocation(-900, 0); //change screen
            lvl1.endStick.setVisible(true);

            lvl1.m1.mario.setLocation(0, lvl1.m1.mario.getY()); //relocate mario to the start
            levelpart = 1; //update screen state
        }

        //New coin collect logic
        if (coinCheck(lvl1.m1.mario, lvl1.coin1)) {
            updateScore(lvl1.scoreDisplay);
            System.out.println("coin1 collected");
            lvl1.coin1.setVisible(false);
        }
        if (coinCheck(lvl1.m1.mario, lvl1.coin2)) {
            updateScore(lvl1.scoreDisplay);
            System.out.println("coin1 collected");
            lvl1.coin2.setVisible(false);
        }
        //UP
        //Hit first question block
        if (qblockCheck(marioHitbox, lvl1.qblock1)){
            updateScore(lvl1.scoreDisplay, "qblock");
            lvl1.qblock1.setVisible(false);
        }

        //DOWN

        //If mario hits the goomba
        if(fallCollideCheck(marioHitbox, lvl1.g1.Goomba)){

            System.out.println("killed");
            if(killCheck==true) {
                updateScore(lvl1.scoreDisplay, "enemy");
                killCheck=false;
                lvl1.g1.Goomba.setIcon(new ImageIcon(getClass().getResource(lvl1.g1.goombaPress))); //Change to stomped image
                lvl1.g1.Sound();

                //lvl1.g1.Goomba.setVisible(false); //and then remove it from the screen
                jumpCount = 0;
            }
            return;
        }

        //Left
        if (levelpart == 0 && lvl1.m1.mario.getX() <= 0)
            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY());

        //If mario goes full to the left
        if (lvl1.m1.mario.getX() < -30) {
            screenChange(levelpart, 1);


            if (levelpart == 1) {
                lvl1.background.setLocation(0, 0); //change screen
                //lvl1.endStick.setVisible(true);
                lvl1.qblock1.setVisible(true);
                lvl1.qblock2.setVisible(true);
                lvl1.m1.mario.setLocation(870, lvl1.m1.mario.getY()); //relocate mario to the start
                levelpart = 0; //update screen state
                lvl1.endStick.setVisible(false);
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }
    @Override
    public void componentHidden(ComponentEvent e) {
    }

    //MainMenu Button Actions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == m1.start) { //When start button is pressed
            System.out.println("a");
            //l1.panel.setVisible(true);
            m1.start.setFocusable(false);
            this.requestFocus(true);
            switchScreen = 1;
            m1.Sound();
            Switch();
        }
        if (e.getSource() == m1.credit) { //When credits button is pressed
            switchScreen = 2;
            this.removeKeyListener(this);
            m1.Sound();
            Switch();
        }
        if (e.getSource() == m1.exit) { //When exit button is pressed
            try {
                m1.Sound();
                sleep(500);
                System.exit(0);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //Right movement mechanics
    public void right() {
        System.out.println(checkForJump);
        checkForJump = 1;

        //move right
        if (levelpart == 0) {
            lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioWalkingRight)));
            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX() + 10, lvl1.m1.mario.getY());
            animationCount += 1;
        }

        //If mario reaches the goal post in levelpart2 = win
        if (levelpart == 1 && lvl1.m1.mario.getX() >= 820) {
            this.removeKeyListener(this);
            writeHighScore();
            lvl1.cleared.setVisible(true);

            levelCleared l2=new levelCleared();
            l2.start();

            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY());

        }

        //If mario hasnt reached the goal post in levelpart2
        if (levelpart == 1 && lvl1.m1.mario.getX() <= 820) {
            lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioWalkingRight)));
            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX() + 10, lvl1.m1.mario.getY());
            animationCount += 1;
        }

        //if left or right key is pressed after the jump key
        if (jumpCount == 1 || jumpCount == 2) {
            checkForDiagonal = 1;
            System.out.println("diagonal");
            diagonal d1 = new diagonal();
            d1.start();
        }

        //Mario falling down the water pit
        if (levelpart == 1) {
            System.out.println("check");
            if (lvl1.m1.mario.getX() == fallArea.getX() && lvl1.m1.mario.getY() == fallArea.getY()) {
                this.removeKeyListener(this);
                m1.start.requestFocus(true);

                fall f1 = new fall();
                f1.start();
            }
        }
    }

    //File Handling
    public void writeHighScore(){
        if (score > highscore){ //If our score exceeded the HighScore

            PrintWriter write = null;
            try {
                write = new PrintWriter("/GameAssets/Other/HighScore.txt");
                write.println(score);
                write.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void left() {
        checkForJump = 2;
        lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioWalkingLeft))); //set walking image

        if (jumpCount == 1 || jumpCount == 2) {
            checkForDiagonal = 2;
            System.out.println("diagonal");
            diagonal d1 = new diagonal();
            d1.start();
            this.addKeyListener(null);
        }
        if (levelpart == 1) {
            System.out.println("check");
            if (lvl1.m1.mario.getX() == fallArea.getX() && lvl1.m1.mario.getY() == fallArea.getY()) {
                this.removeKeyListener(this);
                fall f1 = new fall();
                f1.start();
            }
        }
        //Dont let mario move left from the first starting area
        if (levelpart == 0 && lvl1.m1.mario.getX() <= 0) {
            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY());
        }
        else {
            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX() - 10, lvl1.m1.mario.getY());
        }
    }

    //jump mechanics
    public class movementUP extends Thread {
        public void run() {
            //If player isn't already between a 2nd jump (can only do 2 jumps at most)
            //Right Jump
            if (checkForJump == 1) {
                if (jumpCount < 3) { //no more than a double jump allowed
                    System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                    MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.jump_s).getFile()));
                    sfx.play();
                    try {
                        setPriority(6);
                        lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioJumpRight)));

                        //multiple loops to simulate varying gravity
                        for (int i = 0; i < 8; i++) {
                            sleep(15);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() - 5);
                            System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                        }
                        for (int i = 0; i < 16; i++) {
                            sleep(10);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() - 5);
                            System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                        }
                        for (int i = 0; i < 8; i++) {
                            if (groundCheck(lvl1.m1.mario)) {
                                System.out.println("grounnd");
                                jumpCount = 0;
                                return;
                            }
                            sleep(15);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() + 5);
                        }
                        for (int i = 0; i < 16; i++) {
                            if (groundCheck(lvl1.m1.mario)) {
                                System.out.println("grounnd");
                                jumpCount = 0;
                                return;
                            }
                            sleep(10);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() + 5);
                        }
                        lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingRight)));
                    } catch (InterruptedException f) {
                        System.out.println(f);
                    }
                    System.out.println(jumpCount);
                    jumpCount = 0; //resetting jump limit
                }
            }
            //Left jump
            if (checkForJump == 2) {
                if (jumpCount < 3) {
                    System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                    MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.jump_s).getFile()));
                    sfx.play();
                    try {
                        setPriority(6);
                        lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioJumpLeft)));
                        for (int i = 0; i < 8; i++) {
                            sleep(15);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() - 5);
                            System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                        }
                        for (int i = 0; i < 16; i++) {
                            sleep(10);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() - 5);
                            System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                        }
                        for (int i = 0; i < 8; i++) {
                            if (groundCheck(lvl1.m1.mario)) {
                                System.out.println("ground");
                                jumpCount = 0;
                                return;
                            }
                            sleep(15);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() + 5);
                        }
                        for (int i = 0; i < 16; i++) {
                            if (groundCheck(lvl1.m1.mario)) {
                                System.out.println("ground");
                                jumpCount = 0;
                                return;
                            }
                            sleep(10);
                            lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() + 5);
                        }
                        lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingLeft)));
                    } catch (InterruptedException f) {
                        System.out.println(f);
                    }
                    jumpCount = 0; //resetting jump limit
                }
            }
        }
    }

    //for jumping diagonally, right or left + jump
    public class diagonal extends Thread {
        public void run() {
            try {
                if(checkForDiagonal==1) {
                    lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioJumpRight)));
                    for (int i = 0; i < 22; i++) {
                        System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                        lvl1.m1.mario.setLocation(lvl1.m1.mario.getX() + 5, lvl1.m1.mario.getY());

                        Thread.sleep(22);
                    }
                    lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingRight)));
                }
                if(checkForDiagonal==2) {
                    lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioJumpLeft)));
                    for (int i = 0; i < 22; i++) {
                        System.out.println(lvl1.m1.mario.getX() + "+" + lvl1.m1.mario.getY());
                        lvl1.m1.mario.setLocation(lvl1.m1.mario.getX() - 5, lvl1.m1.mario.getY());

                        Thread.sleep(22);
                    }
                    lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressMarioStandingLeft)));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }

    //mario falling in the water and dying
    public class fall extends Thread {
        public void run() {
            try {
                setPriority(1);

                //change mario icon, fall down and jump
                for (int i = 0; i < 60; i++) {
                    sleep(4);
                    lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() + 1);
                }
                lvl1.m1.mario.setIcon(new ImageIcon(getClass().getResource(lvl1.m1.addressDMario)));
                for (int i = 0; i < 60; i++) {
                    sleep(4);
                    lvl1.m1.mario.setLocation(lvl1.m1.mario.getX(), lvl1.m1.mario.getY() - 2);
                }

                MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.dead_s).getFile()));
                sfx.play();
                sleep(300);
                lvl1.gameOver.setVisible(true);
                gameOverScreen g1=new gameOverScreen();
                g1.start();
            } catch (InterruptedException f) {
                System.out.println(f);
            }
        }
    }

    //the credits screen is to be shown for 5 seconds after the button is pressed, then switch to main menu
    public class credit extends Thread {
        public void run() {
            try {
                setPriority(1);
                sleep(5000);
                switchScreen=4;
                System.out.println(switchScreen);
                Switch();

            } catch (InterruptedException f) {
                System.out.println(f);
            }
        }
    }

    //the level cleared screen falling down from the top
    public class levelCleared extends Thread {

        public void run() {
            try {
                setPriority(1);
                for(int i=0;i<600;i++) {
                    lvl1.cleared.setLocation(lvl1.cleared.getX(), lvl1.cleared.getY()+1);
                    sleep(5);

                    ///when it reaches the bottom, stop
                    if(lvl1.cleared.getY()>=600){
                        break;
                    }
                }

                //if the player's score was greater than the highscore present in the txt
                if(score>highscore){
                    lvl1.highScore.setText("Your Score is High Score! :"+score);
                    lvl1.highScore.setVisible(true);
                }

                MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.levelClear_s).getFile()));
                sfx.play();
                //sleep(1000);
                //readHighScore();
                //l1.highScore.setText("High Score :"+highscore);

            } catch (InterruptedException f) {
                System.out.println(f);
            }
        }

    }

    //The game over screen appearing from the bottom
    public class gameOverScreen extends Thread {

        public void run() {
            try {
                setPriority(1);
                for(int i=0;i<600;i++) {
                    lvl1.gameOver.setLocation(lvl1.gameOver.getX(), lvl1.gameOver.getY()-1);
                    sleep(5);

                    //when it reaches the top of the screen, stop going upwards
                    if(lvl1.cleared.getY()>=600){
                        break;
                    }
                }
                MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.gameOver_s).getFile()));
                sfx.play();

                //If our score was less than the highscore present in the text file
                if(score<highscore){
                    lvl1.gameOver.setForeground(Color.BLUE);
                    lvl1.highScore.setText("Your Score :"+score);
                    lvl1.highScore.setVisible(true);
                }
            } catch (InterruptedException f) {
                System.out.println(f);
            }
        }
    }

//Check if mario came in contact with a coin, if so, play the sfx and collect it
    boolean coinCheck(JLabel player, JLabel coin) {
        if (coin.isVisible()) {
            if (player.getX() >= coin.getX() && player.getX() <= coin.getX() + 50) {
                if (player.getY() >= coin.getY() && player.getY() <= coin.getY() + 50) {
                    MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.coin_s).getFile()));
                    sfx.play();
                    return true;
                }
            }
        }
        return false;
    }

    //Check if 2 hitboxes are colliding with each other
    boolean collideCheck(Rectangle player, Rectangle entity){

        if(player.intersects(entity))
            return true;
        return false;
    }

    //Change the level screen
    void screenChange(int screen, int reverse){

        //question blocks disappear when screen changes
        //level part 0 = initial screen
        //level part 1 = 2nd screen where the goal post is


        if(levelpart == 0) {
            if (reverse == 0) {
                lvl1.background.setLocation(-900, 0); //change screen

                lvl1.m1.mario.setLocation(0, lvl1.m1.mario.getY()); //relocate mario to the start
                levelpart = 1; //update screen state

                lvl1.qblock1.setVisible(false);
                lvl1.qblock2.setVisible(false);

                lvl1.endStick.setVisible(false);
                lvl1.g1.Goomba.setVisible(false);

            }
        }
        //reverse 1 = mario is coming back to the levelpart 0 screen after going to levelpart2
        else if (reverse == 1) {
            lvl1.background.setLocation(0, 0); //change screen

            lvl1.m1.mario.setLocation(870, lvl1.m1.mario.getY()); //relocate mario to the start
            levelpart = 0; //update screen state

            lvl1.endStick.setVisible(false);
            //l1.qblock1.setVisible(false);
            lvl1.qblock2.setVisible(true);
        }
    }

    //Check if mario bumped innto a question block
    boolean qblockCheck(Rectangle player, JLabel qblock){

        if(qblock.isVisible()) {
            if (player.contains(qblock.getX() + 18, qblock.getY() + 35)) {
                MP3Player sfx = new MP3Player(new File(getClass().getResource(lvl1.supercoin_s).getFile()));
                sfx.play();
                return true;
            }
        }
        return false;
    }

    //Check if mario's hitbox collides with the goomba, if so, stop its movement
    boolean fallCollideCheck(Rectangle player, JLabel entity){

        if(entity.isVisible()){
            if(player.contains(entity.getX() + 25, entity.getY())){
                mob1.interrupt();

                return true;
            }
        }
        return false;
    }

    //Check if mario is on the ground
    boolean groundCheck(JLabel player){

        if(player.isVisible()){
            if((player.getY() + 60) >= 530)
                return true;
        }
        return false;
    }

    void updateScore(JLabel scoreDisp) {
        score += 100;
        scoreDisp.setText("Score: " + score);
    }

    void updateScore(JLabel scoreDisp, String entity){

        switch(entity){

            case "enemy":
                score+=200;
                break;
            case "coin":
                score+=100;
                break;
            case "supercoin":
                score+=400;
                break;
            case "level":
                score+=500;
                break;
            case "reset":
                score = 0;
            case "qblock":
                score+=100;
        }
        scoreDisp.setText("Score: " + score);
    }

    //Switch screen function
    public void Switch() {
        if (switchScreen == 0) {
            c1.panel.setVisible(false);
            lvl1.panel.setVisible(false);
            m1.panel.setVisible(true);

            m1.start.addActionListener(this);
            m1.credit.addActionListener(this);
            m1.exit.addActionListener(this);
        }
        if (switchScreen == 1) {
            c1.panel.setVisible(false);
            lvl1.panel.setVisible(true);
            m1.panel.setVisible(false);
            this.addKeyListener(this);
        }
        if(switchScreen==2){
            lvl1.panel.setVisible(false);
            m1.panel.setVisible(false);
            c1.panel.setVisible(true);

            credit c2=new credit();
            c2.start();

        }
        if(switchScreen==4){
            m1.panel.setVisible(true);
            c1.panel.setVisible(false);
            lvl1.panel.setVisible(false);
        }
    }
}