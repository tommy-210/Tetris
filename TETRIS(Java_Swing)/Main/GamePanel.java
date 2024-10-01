package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    
    public static final int WIDTH = 680;
    public static final int HEIGHT = 650;
    final int FPS = 60;
    Thread GameThread;
    PlayManager PM = new PlayManager();

    public GamePanel() {
        //setting
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);
    }

    public void lunchGame() {
        GameThread = new Thread(this);
        GameThread.start();
    }

    public void run() {

        //game loop
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(GameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        if(KeyHandler.pausePressed == false && PlayManager.gameOver == false) {
            PM.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        PM.draw(g2D);
    }
}
