package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerSpeedUpItem extends Item {

    public PlayerSpeedUpItem(int x, int y) {
        super(x, y);
    }

    @Override
    public void drawItem(Graphics g) {
        g.setColor(Color.BLUE); // 그래픽스 색상을 노란색으로 설정

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
}

