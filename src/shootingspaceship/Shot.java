package shootingspaceship; 

import java.awt.Graphics; 
import java.awt.Color; 

public class Shot {

    protected int x_pos; // 총알의 x 좌표
    private int y_pos; // 총알의 y 좌표
    private boolean alive; // 총알의 생존 여부

    public Shot(int x, int y) {
        x_pos = x; // x 좌표 초기화
        y_pos = y; // y 좌표 초기화
        alive = true; // 생존 여부 초기화
    }

    public int getY() {
        return y_pos;
    }

    public int getX() {
        return x_pos;
    }

    public void moveShot(float speed) {
        y_pos += speed; // y 좌표를 주어진 속도만큼 증가시킴
    }
    
    public boolean isActive() {
        return alive;
    }

    public void drawShot(Graphics g) {
        if (!alive) { // 총알이 생존 상태가 아니라면
            return; // 아무것도 하지 않고 메소드 종료
        }
        g.setColor(Color.gray); // 그래픽스 색상을 노란색으로 설정
        g.fillRect(x_pos, y_pos, 3, 8); 
        g.setColor(Color.yellow); // 그래픽스 색상을 노란색으로 설정
        g.fillRect(x_pos, y_pos, 3, 4); 
        
    }

    public void collided() {
        alive = false; // 생존 상태를 false로 변경하여 총알을 비활성화
    }
}
