package Pieces;

import java.awt.Color;
import java.awt.Graphics2D;
import Main.KeyHandler;
import Main.PlayManager;

public class Mino {

    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;
    public boolean deactivating;
    int deactivateCounter = 0;
    public boolean active = true;
    //collision
    boolean leftCollision, rightCollision, bottonCollision;
    
    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {}

    public void updateXY(int direction) {

        checkRotationCollision();

        if(leftCollision == false && rightCollision == false && bottonCollision == false) {
            this.direction = direction;

            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }

    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}

    public void checkMovementCollision() {
        //set collision false
        leftCollision = false;
        rightCollision = false;
        bottonCollision = false;
        
        //check collision with static block
        checkStaticBlockCollision();

        //check left collision
        for(int i = 0; i < b.length; i++) {
            if(b[i].x == PlayManager.left_x) {
                leftCollision = true;
            }  
        }

        //check right collision
        for(int i = 0; i < b.length; i++) {
            if((b[i].x + Block.SIZE) == PlayManager.right_x) {
                rightCollision = true;
            }
        }

        //check botton collision
        for(int i = 0; i < b.length; i++) {
            if((b[i].y + Block.SIZE) == PlayManager.botton_y) {
                bottonCollision = true;
            }
        }
    }

    public void checkRotationCollision() {
        //set collision false
        leftCollision = false;
        rightCollision = false;
        bottonCollision = false;

        //check collision with static block
        checkStaticBlockCollision();

        //check left collision
        for(int i = 0; i < b.length; i++) {
            if(tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }  
        }

        //check right collision
        for(int i = 0; i < b.length; i++) {
            if((tempB[i].x + Block.SIZE) > PlayManager.right_x) {
                rightCollision = true;
            }
        }

        //check botton collision
        for(int i = 0; i < b.length; i++) {
            if((tempB[i].y + Block.SIZE) > PlayManager.botton_y) {
                bottonCollision = true;
            }
        }
    }

    public void checkStaticBlockCollision() {
        //check every block
        for(int i = 0; i < PlayManager.staticBlock.size(); i++) {

            int targetX = PlayManager.staticBlock.get(i).x;
            int targetY = PlayManager.staticBlock.get(i).y;

            //check down collision
            for(int j = 0; j < b.length; j++) {
                if(b[j].y + Block.SIZE == targetY && b[j].x == targetX) {
                    bottonCollision = true;
                }
            }

            //check left collision
            for(int j = 0; j < b.length; j++) {
                if(b[j].x - Block.SIZE == targetX && b[j].y == targetY) {
                    leftCollision = true;
                }
            }
            
            //check right collision
            for(int j = 0; j < b.length; j++) {
                if(b[j].x + Block.SIZE == targetX && b[j].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }

    public void update() {

        if(deactivating) {
            deactivating();
        }

        checkMovementCollision();

        if(KeyHandler.upPressed) {

            //change direction
            switch(direction){
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }

            //reset flase upPressed
            KeyHandler.upPressed = false;

        }else if(KeyHandler.downPressed) {

            //move down the piece
            if(bottonCollision == false) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }

            //reset flase downPressed
            KeyHandler.downPressed = false;

        }else if(KeyHandler.leftPressed) {

            //move left the piece
            if(leftCollision == false) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
                autoDropCounter = 0;
            }

            //reset flase leftPressed
            KeyHandler.leftPressed = false;

        }else if(KeyHandler.rightPressed) {
            
            //move right the piece
            if(rightCollision == false) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
                autoDropCounter = 0;
            }
            
            //reset flase rightPressed
            KeyHandler.rightPressed = false;
        }

        if(bottonCollision) {
            deactivating = true;
        
        }else {
            autoDropCounter++; // increment for any frame
            if(autoDropCounter == PlayManager.dropInterval) {
                //move the piece
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }

    public void deactivating() {
        deactivateCounter++;

        //wait 45 frame to check and activate
        if(deactivateCounter == 45) {
            //reset counter
            deactivateCounter = 0;
            checkMovementCollision();//check if collision is true

            if(bottonCollision) {
                //disactive block for next mino
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2D) {
        
        int margin = 2;
        g2D.setColor(b[0].c);
        g2D.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2D.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2D.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2D.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
    }
}
