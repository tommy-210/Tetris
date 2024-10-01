package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import Pieces.Block;
import Pieces.I_Block;
import Pieces.J_Block;
import Pieces.L_Block;
import Pieces.Mino;
import Pieces.O_Block;
import Pieces.S_Block;
import Pieces.T_Block;
import Pieces.Z_Block;

public class PlayManager {

    //Main Play area
    final int WIDTH = 420;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int botton_y;

    //Mimo setting
    Mino currentMimo;
    Mino nextMino;

    //coordinate Mino
    final int MIMO_START_X;
    final int MIMO_START_Y;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;

    //ofther
    public static ArrayList<Block> staticBlock = new ArrayList<>();
    public static int dropInterval = 60;
    static boolean gameOver;

    //effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    //score level and line delete
    int level = 1;
    int lines;
    int score;

    public PlayManager() {

        //set coordinate X Y
        left_x = 25;
        right_x = left_x + WIDTH;
        top_y = 25;
        botton_y = top_y + HEIGHT;

        MIMO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MIMO_START_Y = top_y + Block.SIZE;

        NEXT_MINO_X = right_x + 100;
        NEXT_MINO_Y = botton_y - 100;

        //create a mimo
        currentMimo = pickMino();
        currentMimo.setXY(MIMO_START_X, MIMO_START_Y);

        //create a next mino
        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    public Mino pickMino() {

        //random mino generate
        Mino mino = null;
        int index = new Random().nextInt(7);

        switch (index) {
            case 0:
                mino = new I_Block();
                break;
            case 1:
                mino = new J_Block();
                break;
            case 2:
                mino = new L_Block();
                break;
            case 3:
                mino = new O_Block();
                break;
            case 4:
                mino = new S_Block();
                break;
            case 5:
                mino = new T_Block();
                break;
            case 6:
                mino = new Z_Block();
                break;
        }
        return mino;
    }

    public void update() {

        //check if current mino is active
        if(currentMimo.active == false) {

            //put current mino in staic block
            staticBlock.add(currentMimo.b[0]);
            staticBlock.add(currentMimo.b[1]);
            staticBlock.add(currentMimo.b[2]);
            staticBlock.add(currentMimo.b[3]);

            //check if game over
            if(currentMimo.b[0].x == MIMO_START_X && currentMimo.b[0].y == MIMO_START_Y) {
                gameOver = true;
            }

            //reset deactivating false
            currentMimo.deactivating = false;

            //replace current mino with next mino
            currentMimo = nextMino;
            currentMimo.setXY(MIMO_START_X, MIMO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

            //check if one line is complete for delete
            checkDelete();
        }else {
            currentMimo.update();
        }
    }

    public void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCounter = 0;
        int lineCount = 0;

        while(x < right_x && y < botton_y) {

            for(int i = 0; i < staticBlock.size(); i++) {
                if(staticBlock.get(i).x == x && staticBlock.get(i).y == y) {
                    //increment block counter
                    blockCounter++;
                }
            }

            //increment X
            x += Block.SIZE;

            if(x == right_x) {

                //if block counter is 14 which is the width
                if(blockCounter == 14) {

                    //effect on
                    effectCounterOn = true;
                    effectY.add(y);

                    //remove all block in a complete line
                    for(int i = staticBlock.size() - 1; i > -1; i--) {
                        if(staticBlock.get(i).y == y) {
                            staticBlock.remove(i);
                        }
                    }

                    //increment line
                    lineCount++;
                    lines++;

                    //increment speed of drop
                    if(lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if(dropInterval > 10) {
                            dropInterval -= 10;
                        }else {
                            dropInterval -= 1;
                        }
                    }

                    //move all static block down
                    for(int i = 0; i < staticBlock.size(); i++) {
                        if(staticBlock.get(i).y < y) {
                            staticBlock.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCounter = 0;
                x = left_x;
                y += Block.SIZE;
            }

        }

        //add score
        if(lineCount > 0) {
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }

    public void draw(Graphics2D g2D) {

        //draw title
        g2D.setColor(Color.CYAN);
        g2D.setFont(new Font("Arial", Font.BOLD, 46));
        g2D.drawString("TETRIS", right_x + 40, top_y + 60);

        //draw Main Play area
        g2D.setColor(Color.white);
        g2D.setStroke(new BasicStroke(4));
        g2D.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        //draw next poligon area
        int x = right_x + 30;
        int y = botton_y - 180;
        g2D.drawRect(x, y, 184, 184);

        //draw NEXT piecies
        g2D.setFont(new Font("Arial", Font.PLAIN, 26));
        g2D.drawString("NEXT", x + 55, y + 40);

        //draw score, line area
        x = right_x + 30;
        y = top_y + 120;
        g2D.drawRect(x, y, 180, 240);

        //draw element in score area
        g2D.setColor(Color.WHITE);
        g2D.setFont(g2D.getFont().deriveFont(22f));
        x = right_x + 50;
        y = top_y + 160;
        g2D.drawString("LEVEL: " + level, x, y);
        g2D.drawString("LINES: " + lines, x, y + 80);
        g2D.drawString("SCORE: " + score, x, y + 160);


        //draw current mimmo
        if(currentMimo != null) {
            currentMimo.draw(g2D);
        }

        //draw next mino in his area
        nextMino.draw(g2D);

        //draw mino in static bock
        for(int i = 0; i < staticBlock.size(); i++) {
            staticBlock.get(i).draw(g2D);
        }

        //effect delete line
        if(effectCounterOn) {
            effectCounter++;

            //draw effect on display
            g2D.setColor(Color.RED);
            for(int i = 0; i < effectY.size(); i++) {
                g2D.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            //if lines delete is 10 increment level
            if(effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        //draw PAUSE mode
        g2D.setColor(Color.YELLOW);
        g2D.setFont(g2D.getFont().deriveFont(50f));
        if(KeyHandler.pausePressed) {
            x = left_x + 110;
            y = GamePanel.HEIGHT / 2 - 50;
            g2D.drawString("PAUSED", x, y);
        }

        //draw GAME OVER
        g2D.setFont(new Font("Reboto", Font.BOLD, 50));
        if(gameOver) {
            x = left_x + 50;
            y = GamePanel.HEIGHT / 2 - 50;
            g2D.setColor(new Color(0f, 0f, 0f, 0.7f));
            g2D.fillRect(x - 20, y - 70, 300, 150);
            g2D.setColor(Color.RED);
            g2D.drawString("GAME OVER", x, y);
        }
    }
}
