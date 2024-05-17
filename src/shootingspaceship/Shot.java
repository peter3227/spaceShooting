package shootingspaceship; // shootingspaceship 패키지 선언

import java.awt.Graphics; // 그래픽스 클래스 import
import java.awt.Color; // 색상 클래스 import

/**
 * Shot 클래스: 우주선이 발사한 총알을 나타내는 클래스
 */
public class Shot {

    private int x_pos; // 총알의 x 좌표
    private int y_pos; // 총알의 y 좌표
    private boolean alive; // 총알의 생존 여부
    private final int radius = 3; // 총알의 반지름 크기

    /**
     * Shot 클래스의 생성자
     * @param x 총알의 초기 x 좌표
     * @param y 총알의 초기 y 좌표
     */
    public Shot(int x, int y) {
        x_pos = x; // x 좌표 초기화
        y_pos = y; // y 좌표 초기화
        alive = true; // 생존 여부 초기화
    }

    /**
     * 총알의 y 좌표 반환
     * @return y 좌표
     */
    public int getY() {
        return y_pos;
    }

    /**
     * 총알의 x 좌표 반환
     * @return x 좌표
     */
    public int getX() {
        return x_pos;
    }

    /**
     * 총알을 이동시키는 메소드
     * @param speed 총알의 이동 속도
     */
    public void moveShot(int speed) {
        y_pos += speed; // y 좌표를 주어진 속도만큼 증가시킴
    }

    /**
     * 총알을 그리는 메소드
     * @param g Graphics 객체
     */
    public void drawShot(Graphics g) {
        if (!alive) { // 총알이 생존 상태가 아니라면
            return; // 아무것도 하지 않고 메소드 종료
        }
        g.setColor(Color.yellow); // 그래픽스 색상을 노란색으로 설정
        g.fillOval(x_pos, y_pos, radius, radius); // 지정된 위치에 원을 그림
    }

    /**
     * 총알이 충돌했을 때 호출되는 메소드
     */
    public void collided() {
        alive = false; // 생존 상태를 false로 변경하여 총알을 비활성화
    }
}
