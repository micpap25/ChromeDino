import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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

    public static void main(String[] args){
        dinogame frame = new dinogame();
        String audioFilePath = "src\\Wii_Theme.wav";
        AudioPlayer player = new AudioPlayer();

        frame.setTitle("Dino McMan");
        frame.setSize(400, 400);
        frame.setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        player.play(audioFilePath);

    }
}

class Dino extends JPanel {
    cactus[] cactuslist={new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1),new cactus(5,1)};
    int speed = 1;
    int k = 0;
    int jump = 20;
    int timealoft;
    int score = 500;
    boolean haslost = false;
    int height = 20;
    boolean crouch=false;
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
                AudioPlayer player = new AudioPlayer();
                String audioFilePath1 = "src\\jump.wav";


                if (e.getKeyCode() == KeyEvent.VK_UP && jump < 21&&jump>10) {
                    timealoft = 25;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP && jump < 21&&jump>10) {player.play(audioFilePath1);}
                else if (e.getKeyCode() == KeyEvent.VK_DOWN && jump > 21)
                    timealoft = 0;
                if (e.getKeyCode() == KeyEvent.VK_DOWN && jump < 21 && jump > 10) {
                    crouch = true;
                }

            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    crouch = false;
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
            if(crouch) {
                height = 10;
                jump = 10;
            }
            else if (height-jump==0){
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
                int p = rand.nextInt(4);
                if(score<500&&(p==2||p==3))
                    p=rand.nextInt(2);
                cactuslist[19]=(new cactus(p,speed));
                k=0;


            }
            for(int i = 0;i<cactuslist.length;i++){
                if (getWidth()-30-cactuslist[i].x<getWidth()/10+15 && getWidth()-30-cactuslist[i].x+10>getWidth()/10)
                    if((cactuslist[i].type==0&&jump<40)||(cactuslist[i].type==1&&jump<30)||(cactuslist[i].type==2&&jump>25)||(cactuslist[i].type==3&&(jump<45&&jump>10))){
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
            repaint();

            }

        }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0,getHeight()/2,getWidth(),getHeight()/2);
        for(int i = 0;i<cactuslist.length;i++){
            cactuslist[i].paint(this,g);
        }
        try {
            if (crouch && score%2==1) {
                BufferedImage rex = ImageIO.read(new File("images\\ChromeDino_Duck2.png"));
                g.drawImage(rex, getWidth() / 10, getHeight() / 2 - jump, 15, height, null);
            } else if (crouch && score%2==0){
                BufferedImage rex = ImageIO.read(new File("images\\ChromeDino_Duck1.png"));
                g.drawImage(rex, getWidth() / 10, getHeight() / 2 - jump, 15, height, null);
            } else if (score%2==1) {
                BufferedImage rex = ImageIO.read(new File("images\\ChromeDino_Run2.png"));
                g.drawImage(rex, getWidth() / 10, getHeight() / 2 - jump, 15, height, null);
            } else {
                BufferedImage rex = ImageIO.read(new File("images\\ChromeDino_Run1.png"));
                g.drawImage(rex, getWidth() / 10, getHeight() / 2 - jump, 15, height, null);
            }

        }catch (IOException ex){};
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
    protected void paint(JPanel p ,Graphics g){
        try {
            if (type == 0) {
                final BufferedImage image = ImageIO.read(new File("images\\ChromeDino_CactusBunch1.png"));
                g.drawImage(image, p.getWidth() - 30 - x, p.getHeight() / 2 - 12, 25, 12, null);
            } else if (type == 1) {
                final BufferedImage image = ImageIO.read(new File("images\\ChromeDino_Cactus1.png"));
                g.drawImage(image, p.getWidth() - 30 - x, p.getHeight() / 2 - 20, 10, 20, null);
            } else if (type == 2) {
                final BufferedImage image = ImageIO.read(new File("images\\ChromeDino_Birb2.png"));
                g.drawImage(image, p.getWidth() - 30 - x, p.getHeight() / 2 - 45, 20, 20, null);
            } else if (type == 3) {
                final BufferedImage image = ImageIO.read(new File("images\\ChromeDino_Birb1.png"));
                g.drawImage(image, p.getWidth() - 30 - x, p.getHeight() / 2 - 25, 15, 15, null);
            }
        } catch (IOException ex){};



    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}

class AudioPlayer implements LineListener{
    boolean playCompleted;
    void play(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip .class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
            while(!playCompleted) {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            audioClip.close();
        } catch (UnsupportedAudioFileException ex) {
        } catch (LineUnavailableException ex) {
        } catch (IOException ex) {
        }
    }
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.STOP) {
            playCompleted = true;
        }
    }
}