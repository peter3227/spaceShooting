/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Player 클래스는 게임에서 플레이어가 조종하는 우주선을 나타냅니다.
 */
public class Player {
    // 플레이어 위치 변수
    private int x_pos; // 플레이어의 X 좌표
    private int y_pos; // 플레이어의 Y 좌표

    // 플레이어 이동을 제한하기 위한 최소 및 최대 X 위치
    private int min_x; // 플레이어 이동의 최소 X 좌표 제한
    private int max_x; // 플레이어 이동의 최대 X 좌표 제한

    /**
     * 초기 위치와 이동 제한을 가지고 플레이어 객체를 초기화하는 생성자입니다.
     * @param x 플레이어의 초기 X 좌표
     * @param y 플레이어의 초기 Y 좌표
     * @param min_x 플레이어 이동 제한의 최소 X 좌표
     * @param max_x 플레이어 이동 제한의 최대 X 좌표
     */
    public Player(int x, int y, int min_x, int max_x) {
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
    }

    /**
     * X 좌표를 이동하는 메서드입니다.
     * @param speed X 축으로의 이동 속도
     */
    public void moveX(int speed) {
        x_pos += speed;
        // 최소 X 좌표 미만으로 이동하는 경우 최소 X 좌표로 설정
        if( x_pos < min_x) x_pos = min_x;
        // 최대 X 좌표 초과로 이동하는 경우 최대 X 좌표로 설정
        if( x_pos > max_x) x_pos = max_x;
    }

    /**
     * 현재 X 좌표를 반환합니다.
     * @return 현재 플레이어의 X 좌표
     */
    public int getX() {
        return x_pos;
    }

    /**
     * 현재 Y 좌표를 반환합니다.
     * @return 현재 플레이어의 Y 좌표
     */
    public int getY() {
        return y_pos;
    }

    /**
     * 플레이어가 발사할 Shot을 생성합니다.
     * @return 생성된 Shot 객체
     */
    public Shot generateShot() {
        // 현재 플레이어 위치에서 Shot 객체 생성
        Shot shot = new Shot(x_pos, y_pos);
        return shot;
    }

    /**
     * 플레이어를 그리는 메서드입니다.
     * @param g Graphics 객체
     */
    public void drawPlayer(Graphics g) {
        g.setColor(Color.red);
        // 플레이어를 그리기 위한 다각형의 X 좌표 배열
        int[] x_poly = {x_pos, x_pos - 10, x_pos, x_pos + 10};
        // 플레이어를 그리기 위한 다각형의 Y 좌표 배열
        int[] y_poly = {y_pos, y_pos + 15, y_pos + 10, y_pos + 15};
        // 플레이어를 다각형으로 채워서 그립니다.
        g.fillPolygon(x_poly, y_poly, 4);
    }
}

