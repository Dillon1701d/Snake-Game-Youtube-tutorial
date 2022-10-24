import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size=25;
    static final int game_units=(screen_width*screen_height)/unit_size;
    static final int delay = 75;
    final int x[]= new int[game_units];
    final int y[]= new int[game_units];
    int body_parts= 6;
    int apples_eaten;
    int apple_x;
    int apple_y;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
        GamePanel(){
            random = new Random();
            this.setPreferredSize(new Dimension(screen_width,screen_height));
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());
            startGame();


        }
    public void startGame(){
        addApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){

        if(running){
            for (int i = 0; i<screen_height/unit_size;i++){
                g.drawLine(i*unit_size,0,i*unit_size,screen_height);
                g.drawLine(0,i*unit_size,screen_width,i*unit_size);
            }

            g.setColor(Color.red);
            g.fillOval(apple_x,apple_y,unit_size,unit_size);

            for(int i = 0;i<body_parts;i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],unit_size,unit_size);
                }
                else{
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i],y[i],unit_size,unit_size);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Comic Sans",Font.BOLD,35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ apples_eaten,(screen_height - metrics.stringWidth("Score: "+ apples_eaten))/2,g.getFont().getSize());
        }
        else{
                gameOver(g);
        }
    }
    public void addApple(){
        apple_x=random.nextInt((int)(screen_width/unit_size))*unit_size;
        apple_y=random.nextInt((int)(screen_height/unit_size))*unit_size;
    }
    public void move(){
        for(int i = body_parts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case'U':
                y[0] = y[0] - unit_size;
                break;
            case'D':
                y[0] = y[0] + unit_size;
                break;
            case'L':
                x[0] = x[0] - unit_size;
                break;
            case'R':
                x[0] = x[0] + unit_size;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == apple_x)&&(y[0]==apple_y)){
            body_parts++;
            apples_eaten++;
            addApple();
        }
    }
    public void checkCollisions(){
            //checks if head hits body == game over
        for(int i=body_parts;i>0;i--){
            if((x[0]==x[i])&&(y[0]==y[i])){
                running =false;
            }

        }
        // checks if head hits left border
        if(x[0]<0){
            running = false;
        }// checks if head hits right border
        if(x[0]>screen_width){
            running = false;
        }
        // checks if head hits top border
        if(y[0]<0){
            running = false;
        }
        // checks if head hits bottom border
        if(y[0]>screen_height){
                running = false;
        }
        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //Gameover texts
        g.setColor(Color.red);
        g.setFont( new Font("Comic Sans",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(screen_height - metrics1.stringWidth("Game Over"))/2,screen_height/2);
        g.setColor(Color.red);
        g.setFont( new Font("Comic Sans",Font.BOLD,35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ apples_eaten,(screen_height - metrics2.stringWidth("Score: "+ apples_eaten))/2,g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();

        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e ){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction!='R'){
                        direction   ='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction   ='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction   ='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction   ='D';
                    }
                    break;
            }
        }
    }
}
