package shootingspaceship;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Item {
    private int x_pos;
    private int y_pos;
    private boolean alive;
    private final int collision_distance = 10; // 충돌 거리
    
    // 아이템 생성자
    Item(int x, int y) {
        x_pos = x;
        y_pos = y;
        alive = true;
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
        if (!alive) { 
            return; 
        }
        g.setColor(Color.MAGENTA); 
        g.fillRect(x_pos, y_pos, 10, 10); 
    }
   
    public void collided() {
        alive = false; // 생존 상태를 false로 변경하여 Item을 비활성화
    }
    
    public boolean isCollidedWithPlayer(Player player) {
        if (-collision_distance <= (y_pos - player.getY()) && (y_pos - player.getY() <= collision_distance)) { // y 좌표 차이가 충돌 거리 이내인지 확인
            if (-collision_distance <= (x_pos - player.getX()) && (x_pos - player.getX() <= collision_distance)) { // x 좌표 차이가 충돌 거리 이내인지 확인
                // 충돌 발생
                return true;
            }
        }
        return false;
    }
}
