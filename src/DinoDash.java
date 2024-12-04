// this class is created to create JPanel
// JPanel allows us to draw and render graphics in frame

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class DinoDash extends JPanel implements ActionListener, KeyListener{
    int width = 750;
    int heigth = 250;

    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;



    class Block{
        int x,y,width,heigth;
        Image img;

        public Block(int x, int y, int width, int heigth, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.heigth = heigth;
            this.img = img;
        }
    }

    // dinosaur
    int dinosaurWidth = 88;
    int dinosaurHeigth = 94;
    int dinosaurX = 50;
    int dinosaurY = heigth - dinosaurHeigth;

    Block dinosaur;

    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = heigth - cactusHeight;
    ArrayList<Block> cactusArray;

    int velovityX = -12; // cactus moving left speed
    int velocityY=0;     // dinosaur jump speed
    int gravity =1;

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeCactusTimer;

    public DinoDash() {
        setPreferredSize(new Dimension(width,heigth));
        setBackground(Color.lightGray);

        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        dinosaur = new Block(dinosaurX,dinosaurY,dinosaurWidth,dinosaurHeigth,dinosaurImg);

        cactusArray = new ArrayList<>();

        gameLoop = new Timer(1000/60,this);
        gameLoop.start();

        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    void placeCactus(){
        if (gameOver) return;
        double placeCactusChance = Math.random();
        if (placeCactusChance > .90){ // 10% you get cactus3
            Block cactus = new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus3Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .70) {  // 20% you get cactus2
            Block cactus = new Block(cactusX,cactusY,cactus2Width,cactusHeight,cactus2Img);
            cactusArray.add(cactus);
        }else if (placeCactusChance > .50) {  // 40% you get cactus2
            Block cactus = new Block(cactusX,cactusY,cactus1Width,cactusHeight,cactus1Img);
            cactusArray.add(cactus);
        }

        if (cactusArray.size() >10) cactusArray.remove(0);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // dinosaur
        g.drawImage(dinosaur.img,dinosaur.x,dinosaur.y,dinosaur.width,dinosaur.heigth,null);

        // cactus
        for (int i=0;i<cactusArray.size();i++){
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img,cactus.x,cactus.y,cactus.width,cactus.heigth,null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Courier",Font.PLAIN,32));
        if (gameOver)  g.drawString("Game Over : "+String.valueOf(score),10,35);
        else g.drawString(String.valueOf(score),10,35);

    }

    public void move(){
        //dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurY ){
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        //cactus
        for (int i=0;i<cactusArray.size();i++){
            Block cactus = cactusArray.get(i);
            cactus.x += velovityX;

            if (collision(dinosaur,cactus)){
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        //score
        score++;
    }

    boolean collision(Block a,Block b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.heigth &&
                a.y + a.heigth > b.y ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//            System.out.println("JUMP!");
            if (dinosaur.y == dinosaurY){
                velocityY = -17;
                dinosaur.img = dinosaurJumpImg;
            }

            if (gameOver){
                //restart game by resetting condition
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY =0;
                cactusArray.clear();
                score=0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }

        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
