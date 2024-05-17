/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship; // shootingspaceship 패키지 선언

import java.awt.Graphics; // 그래픽스 클래스 import
import java.awt.Color; // 색상 클래스 import

/**
 * Enemy 클래스: 적을 나타내는 클래스
 */
public class Enemy {

    private float x_pos; // 적의 x 좌표
    private float y_pos; // 적의 y 좌표
    private float delta_x; // x 좌표의 변화량
    private float delta_y; // y 좌표의 변화량
    private int max_x; // x 좌표의 최대값
    private int max_y; // y 좌표의 최대값
    private float delta_y_inc; // y 좌표의 변화량 증가량
    private final int collision_distance = 10; // 충돌 거리

    /**
     * Enemy 클래스의 생성자
     * @param x 적의 초기 x 좌표
     * @param y 적의 초기 y 좌표
     * @param delta_x x 좌표의 변화량
     * @param delta_y y 좌표의 변화량
     * @param max_x x 좌표의 최대값
     * @param max_y y 좌표의 최대값
     * @param delta_y_inc y 좌표의 변화량 증가량
     */
    public Enemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc) {
        x_pos = x; // x 좌표 초기화
        y_pos = y; // y 좌표 초기화
        this.delta_x = delta_x; // x 좌표의 변화량 초기화
        this.delta_y = delta_y; // y 좌표의 변화량 초기화
        this.max_x = max_x; // x 좌표의 최대값 초기화
        this.max_y = max_y; // y 좌표의 최대값 초기화
        this.delta_y_inc = delta_y_inc; // y 좌표의 변화량 증가량 초기화
    }

    /**
     * 적을 이동시키는 메소드
     */
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

    /**
     * 총알과 충돌했는지 여부를 확인하는 메소드
     * @param shots 총알 배열
     * @return 충돌 여부
     */
    public boolean isCollidedWithShot(Shot[] shots) {
        for (Shot shot : shots) { // 모든 총알에 대해 반복
            if (shot == null) { // 총알이 null이면 다음으로 넘어감
                continue;
            }
            if (-collision_distance <= (y_pos - shot.getY()) && (y_pos - shot.getY() <= collision_distance)) { // y 좌표 차이가 충돌 거리 이내인지 확인
                if (-collision_distance <= (x_pos - shot.getX()) && (x_pos - shot.getX() <= collision_distance)) { // x 좌표 차이가 충돌 거리 이내인지 확인
                    // 충돌 발생
                    shot.collided(); // 총알 비활성화
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 플레이어와 충돌했는지 여부를 확인하는 메소드
     * @param player 플레이어 객체
     * @return 충돌 여부
     */
    public boolean isCollidedWithPlayer(Player player) {
        if (-collision_distance <= (y_pos - player.getY()) && (y_pos - player.getY() <= collision_distance)) { // y 좌표 차이가 충돌 거리 이내인지 확인
            if (-collision_distance <= (x_pos - player.getX()) && (x_pos - player.getX() <= collision_distance)) { // x 좌표 차이가 충돌 거리 이내인지 확인
                // 충돌 발생
                return true;
            }
        }
        return false;
    }

    /**
     * 적을 그리는 메소드
     * @param g Graphics 객체
     */
    public void draw(Graphics g) {
        g.setColor(Color.yellow); // 그래픽스 색상을 노란색으로 설정
        int[] x_poly = {(int) x_pos, (int) x_pos - 10, (int) x_pos, (int) x_pos + 10}; // 다각형의 x 좌표 배열
        int[] y_poly = {(int) y_pos + 15, (int) y_pos, (int) y_pos + 10, (int) y_pos}; // 다각형의 y 좌표 배열
        g.fillPolygon(x_poly, y_poly, 4); // 지정된 다각형을 채워 그림
    }
}

