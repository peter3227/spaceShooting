package shootingspaceship; 

import java.awt.Graphics; 
import java.awt.Color; 


public class Enemy {

    private float x_pos; // 적의 x 좌표
    private float y_pos; // 적의 y 좌표
    private float delta_x; // x 좌표의 변화량
    private float delta_y; // y 좌표의 변화량
    private int max_x; // x 좌표의 최대값
    private int max_y; // y 좌표의 최대값
    private float delta_y_inc; // y 좌표의 변화량 증가량
    private final int collision_distance = 15; // 충돌 거리

    public Enemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc) {
        x_pos = x; // x 좌표 초기화
        y_pos = y; // y 좌표 초기화
        this.delta_x = delta_x; // x 좌표의 변화량 초기화
        this.delta_y = delta_y; // y 좌표의 변화량 초기화
        this.max_x = max_x; // x 좌표의 최대값 초기화
        this.max_y = max_y; // y 좌표의 최대값 초기화
        this.delta_y_inc = delta_y_inc; // y 좌표의 변화량 증가량 초기화
    }

    public void move() {
        x_pos += delta_x; // x 좌표 이동
        y_pos += delta_y; // y 좌표 이동

        if (x_pos < 0) { // x 좌표가 화면 왼쪽을 벗어나면
            x_pos = 0; // x 좌표를 0으로 설정
            delta_x = -delta_x; // x 좌표의 변화량 반전
        } else if (x_pos > max_x) { // x 좌표가 화면 오른쪽을 벗어나면
            x_pos = max_x; // x 좌표를 최대값으로 설정
            delta_x = -delta_x; // x 좌표의 변화량 반전
        }
        if (y_pos > max_y) { // y 좌표가 화면 아래를 벗어나면
            y_pos = 0; // y 좌표를 0으로 설정
            delta_y += delta_y_inc; // y 좌표의 변화량 증가
        }
    }

    public boolean isCollidedWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot != null && shot.isActive()) { // 총알이 활성화되어 있을 때만 체크
                if (-collision_distance <= (y_pos - shot.getY()) && (y_pos - shot.getY() <= collision_distance)) {
                    if (-collision_distance <= (x_pos - shot.getX()) && (x_pos - shot.getX() <= collision_distance)) {
                        // 충돌 발생
                        shot.collided(); // 총알 즉시 비활성화
                        SoundPlayer.playSound("src/resources/isCollidedWithShot.wav");
                        return true;
                    }
                }
            }
        }
        return false;
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

    public void draw(Graphics g) {
        g.setColor(Color.yellow); // 그래픽스 색상을 노란색으로 설정
        int[] x_poly = {(int) x_pos, (int) x_pos - 10, (int) x_pos, (int) x_pos + 10}; // 다각형의 x 좌표 배열
        int[] y_poly = {(int) y_pos + 15, (int) y_pos, (int) y_pos + 10, (int) y_pos}; // 다각형의 y 좌표 배열
        g.fillPolygon(x_poly, y_poly, 4); // 지정된 다각형을 채워 그림
    }
    
    public int getX() {
        return (int) x_pos;
    }
    
    public int getY() {
        return (int) y_pos;
    }
}

