
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
public class dinogame extends JFrame {
    public dinogame() {
        Dino p = new Dino();
        p.setFocusable(true);
        p.requestFocus();
        add(p);
    }

    public static void main(String[] args) {
        dinogame frame = new dinogame();
        frame.setTitle("Dino McMan");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}

class Dino extends JPanel {
    cactus[] cactuslist={new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1)};
    int speed = 1;
    int k = 0;
    int jump = 20;
    int timealoft;
    int score = 0;
    boolean haslost = false;
    int height = 20;
    int delay=5;
    BufferedReader in;
    public static PrintWriter out;
    JLabel score1= new JLabel("Score:");
    public Dino(){
        Timer timer = new Timer(10/speed, new TimerListener());
        timer.start();
        Border b = new LineBorder(Color.BLACK,2);
        score1.setBorder(b);
        add(score1);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && jump < 21&&jump>10||(e.getKeyCode() == KeyEvent.VK_SPACE && jump < 21&&jump>10)) {
                    timealoft = 25;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && jump > 30)
                    jump -=10;
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    delay=0;
                }

            }
        });
    }

    public class TimerListener  implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if (timealoft>0) {
                jump+=2;
                timealoft--;
            }
            else if (jump>20){
                jump-=1;
            }
            if(delay<5) {
                height = 10;
                jump = 10;
            }
            else if (delay>5&&height-jump==0){
                height = 20;
                jump = 20;
            }
            if(k%10==0)
                score+=1;
            score1.setText("Score:"+score);
            if (k >70) {
                Random rand = new Random();
                for(int i =0;i<19;i++){
                    cactuslist[i]=cactuslist[i+1];
                }
                int p = rand.nextInt(12);
                if(score<500&&(p==2||p==3))
                    p=rand.nextInt(1);
                cactuslist[19]=(new cactus(p,speed));
                k=0;


            }
            for(int i = 0;i<cactuslist.length;i++){
                if (getWidth()-30-cactuslist[i].x==getWidth()/10)
                    if((cactuslist[i].type==0&&jump<40)||(cactuslist[i].type==1&&jump<30)||(cactuslist[i].type==2&&jump>25)||(cactuslist[i].type==3&&(jump<40&&jump>10))){
                        haslost = true;
                    }
            }

            if(haslost){
                try {
                    out = new PrintWriter(new BufferedWriter(new FileWriter("scores.out")));
                    out.print("" + score);

                }
                catch (IOException ex){
                }
                score=0;
                for(int i =0;i<20;i++){
                    cactuslist[i]=new cactus(5,1);
                }
                haslost=false;

            }
            for(int i =0;i<20;i++){
                cactuslist[i].move();
            }
            k++;
            delay++;
            repaint();

            }

        }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0,getHeight()/2,getWidth(),getHeight()/2);
        for(int i = 0;i<cactuslist.length;i++){
            cactuslist[i].paint(this,g);
        }
        g.fillRect(getWidth()/10,getHeight()/2-jump,10,height);


    }

}
class cactus {
    int type;
    int x = 0;
    int speed=1;
    public cactus(int type,int speed) {
        this.type = type;
        this.x = 0;
        this.speed = speed;
    }

    public void move(){
        x+=speed;
    }
    protected void paint(JPanel p ,Graphics g) {

        if (type==0)
            g.fillRect(p.getWidth()-30-x,p.getHeight()/2-10,20,10);

        else if (type == 1)
            g.fillRect(p.getWidth()-30-x,p.getHeight()/2-20,10,20);
        else if (type==2)
            g.fillRect(p.getWidth()-30-x,p.getHeight()/2-35,10,10);
        else if (type == 3)
            g.fillRect(p.getWidth()-30-x,p.getHeight()/2-20,9,9);


    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


}



