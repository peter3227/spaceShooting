package shootingspaceship;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Item {
    private int x_pos;
    private int y_pos;
    private final int collision_distance = 15; // 충돌 거리
    
    // 아이템 생성자
    Item(int x, int y) {
        x_pos = x;
        y_pos = y;
    }

    public int getY() {
    	return y_pos;
    }
    
    public int getX() {
    	return x_pos;
    }
    
    public void moveItem(int speed) {
    	y_pos -= speed;
    }
    
    public void drawItem(Graphics g) {
        
        g.setColor(Color.MAGENTA); 
        g.fillRect(x_pos, y_pos, 10, 10); 
    }
    
    public boolean isCollidedWithPlayer(Player player) {
        if (-collision_distance <= (y_pos - player.getY()) && (y_pos - player.getY() <= collision_distance)) { // y 좌표 차이가 충돌 거리 이내인지 확인
            if (-collision_distance <= (x_pos - player.getX()) && (x_pos - player.getX() <= collision_distance)) { // x 좌표 차이가 충돌 거리 이내인지 확인
            	SoundPlayer.playSound("src/resources/shotIsCollidedWithPlayer.wav");
                return true;
            }
        }
        return false;
    }
}
