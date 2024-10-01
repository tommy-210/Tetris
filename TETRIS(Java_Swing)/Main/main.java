package Main;
import javax.swing.JFrame;

public class main{

    public static void main(String[] args) {

        JFrame window = new JFrame("TETRIS in Java Swing");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        //add component
        GamePanel GP = new GamePanel();
        window.add(GP);
        window.pack();
        
        //set visible window
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        GP.lunchGame();
    }
}