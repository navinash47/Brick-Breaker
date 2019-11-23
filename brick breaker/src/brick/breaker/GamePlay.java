/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brick.breaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author avinash
 */
public class GamePlay extends JPanel implements KeyListener,ActionListener {
    private boolean play= false;
    private int score=0;
    private int[] a={1,2,3,4,5,6,7,8};
    private int[] b={1,2,3,4,5,6,7,8};       
    private  Random random=new Random();
    int x=random.nextInt(8);
    int y=random.nextInt(8);
    
    private int totalbricks=a[x]*b[y];
    private boolean inair=false;
    private Timer timer;
    private int delay=8;
    private boolean gameover=false;
    
    private int playerX=310;
    
    private int ballposX=350;
    private int ballposY=530;
    private int ballXdir=0;
    private int ballYdir=0;
    
    private MapGenerator map;
    
    private ImageIcon brickball;
    private ImageIcon paddle;
    
    public GamePlay(){
        
        map= new MapGenerator(a[x],b[y]);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraveralKeyEnabled(false);
        timer= new Timer(delay,this);
        timer.start();
        
    }
    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",Font.PLAIN,14));
        g.drawString("Scores: "+score,580,20); 
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",Font.PLAIN,14));
        g.drawString("bricks remaining: "+totalbricks,520,40); 
        //drawing map
        map.draw((Graphics2D)g);
        
        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        
        paddle= new ImageIcon("/home/avinash/Desktop/snake/paddle.png");
        paddle.paintIcon(this,g,playerX,550);
        
        brickball=new ImageIcon("/home/avinash/Desktop/snake/brickball.png");
        brickball.paintIcon(this, g,ballposX, ballposY);
        if(totalbricks==0){
            g.setColor(Color.green);
            g.setFont(new Font("arial",Font.PLAIN,50));
            g.drawString("YOU WON!!",100,300);
             g.setColor(Color.white);
            g.setFont(new Font("arial",Font.PLAIN,20));
            g.drawString ("press the Enter to restart ",210,320);
            ballXdir=0;
            ballYdir=0;
            gameover=true;
            
            
        }
        else if(ballposY>=590)
        {
            g.setColor(Color.RED);
            g.setFont(new Font("arial",Font.PLAIN,50));
            g.drawString("GAMEOVER ",200,300);
             g.setColor(Color.white);
            g.setFont(new Font("arial",Font.PLAIN,20));
            g.drawString ("press the Enter to restart ",210,320);
            gameover=true;
        }
        g.dispose();
        
         
        
    }
    public void moveRight(){
        play=true;
        playerX+=20;
        if(!inair){
        ballposX+=20;
        }
    }
    public void moveLeft(){
        play= true;
        playerX-=20;
        if(!inair){
        ballposX-=20;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //To change body of generated methods, choose Tools | Templates.
       
        if(e.getKeyCode()==KeyEvent.VK_ENTER&&gameover){
                playerX=310;
                gameover=false;
                ballposX=350;
                ballposY=530;
                ballXdir=0;
                ballYdir=0;
                play= false;
                score=0;
                inair=false;
                x=random.nextInt(8);
                y=random.nextInt(8);
                totalbricks=a[x]*b[y];
                map= new MapGenerator(a[x],b[y]);
                repaint();
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE&&!inair){
            ballXdir=1;
            ballYdir=-2;
            inair=true;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT&&!gameover){
            if(playerX>=600){
                playerX=600;
            }
            else moveRight();
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT&&!gameover){
            if(playerX<=10){
                playerX=10;
            }
            else moveLeft();
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         //To change body of generated methods, choose Tools | Templates.
         timer.start();
         if(play){
             if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))){
                 ballYdir=-ballYdir;
             }
             A: for(int i=0;i<map.map.length;i++){
                 for(int j=0;j<map.map[0].length;j++){
                     
                     if(map.map[i][j]>0){
                         int brickX=j*map.brickwidth+80;
                         int brickY=i*map.brickheight+50;
                         int brickwidth=map.brickwidth;
                         int brickheight=map.brickheight;
                         
                         Rectangle rect= new Rectangle(brickX,brickY,brickwidth,brickheight);
                         Rectangle ballrect= new Rectangle(ballposX,ballposY,20,20);
                         if(ballrect.intersects(rect)){
                             map.setBrickValue(0, i, j);
                             totalbricks--;
                             score=score+5;
                             
                             if(ballposX+19<=rect.x||ballposX+1>=rect.x+rect.width){
                                 ballXdir=-ballXdir;
                             }
                             else{
                                 ballYdir=-ballYdir;
                             }
                             break A; 
                         }
                     }
                 }
             }
             ballposX+=ballXdir;
             ballposY+=ballYdir;
             if(ballposX<0){
                 ballXdir=-ballXdir;
             
             }
             if(ballposY<0){
                 ballYdir=-ballYdir;
             }
             if(ballposX>670){
                 ballXdir=-ballXdir;
             }
         }
         
         repaint();
    }

    private void setFocusTraveralKeyEnabled(boolean b) {
         //To change body of generated methods, choose Tools | Templates.
    }
    
}
