package Pieces;

import java.awt.Color;

public class O_Block extends Mino{

    public O_Block() {
        create(Color.YELLOW);
    }

    public void setXY(int x, int y) {
        // * °
        // ° °
        b[0].x = x; 
        b[0].y = y;
        b[1].x = b[0].x; 
        b[1].y = b[0].y + Block.SIZE;
        b[2].x = b[0].x + Block.SIZE; 
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE; 
        b[3].y = b[0].y + Block.SIZE;
    }
    
    public void getDirection1() {
        // * °
        // ° °
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y + Block.SIZE;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(1);
    }
    
    public void getDirection2() {
        getDirection1();
    }
    
    public void getDirection3() {
        getDirection1();
    }

    public void getDirection4() {
        getDirection1();
    }
}
