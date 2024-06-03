package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;

public class enemyShot {
    private float x;
    private float y;
    private final int speed;

    public enemyShot(float x, float y, int speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void move(){
        y += speed;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void draw(Graphics g){
        g.setColor(Color.RED);
        g.fillRect((int)x,(int)y,4,8);
        g.setColor(Color.GRAY); 
        g.fillRect((int)x,(int)y,4,6); 
    }

    public boolean isCollidedWithPlayer(Player player) {
        int collisionDistance = 15;
        if (Math.abs(player.getX() - x) < collisionDistance && Math.abs(player.getY() - y) < collisionDistance) {
            return true;
        }
        return false;
    }
}
