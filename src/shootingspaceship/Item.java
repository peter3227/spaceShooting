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
        g.setColor(Color.MAGENTA); // 그래픽스 색상을 노란색으로 설정

        int[][] dotPattern1 = {
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}
        };

        int[][] dotPattern2 = {
                {0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0}
        };

        int[][] currentPattern;
        // 도트 크기
        int dotSize = 2;

        int[][] patternToDraw = (System.currentTimeMillis() / 100) % 10 < 5 ? dotPattern1 : dotPattern2;

        // 도트 그리기
        for (int i = 0; i < patternToDraw.length; i++) {
            for (int j = 0; j < patternToDraw[i].length; j++) {
                if (patternToDraw[i][j] == 1) {
                    int dotX = (int) getX() - patternToDraw[0].length / 2 * dotSize + j * dotSize;
                    int dotY = (int) getY() - patternToDraw.length / 2 * dotSize + i * dotSize;
                    g.fillRect(dotX, dotY, dotSize, dotSize);
                }
            }
        }
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
