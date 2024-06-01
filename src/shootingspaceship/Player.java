package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;

public class Player {
    private int x_pos;
    private int y_pos;
    private int min_x;
    private int max_x;

    public Player(int x, int y, int min_x, int max_x) {
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
    }

    public void moveX(float speed) {
        x_pos += speed;
        if (x_pos < min_x) x_pos = min_x;
        if (x_pos > max_x) x_pos = max_x;
    }

    public int getX() {
        return x_pos;
    }

    public int getY() {
        return y_pos;
    }

    public Shot generateShot() {
        return new Shot(x_pos + 6 , y_pos - 7);
    }

    public void drawPlayer(Graphics g) {
        // 플레이어 위치
        int startX = x_pos - 6; // 플레이어 X 위치의 시작점
        int startY = y_pos - 7; // 플레이어 Y 위치의 시작점
        
        // 도트 크기
        int dotSize = 2;

        // 플레이어를 나타내는 행렬
        int[][] whiteMatrix = {
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1}
        };
        
        int[][] redMatrix = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0},               
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            
            int[][] blueMatrix = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            
            // 플레이어 행렬을 순회하며 도트를 찍기
            drawDots(g, whiteMatrix, startX, startY, Color.WHITE, dotSize);
            drawDots(g, redMatrix, startX, startY, Color.RED, dotSize);
            drawDots(g, blueMatrix, startX, startY, Color.BLUE, dotSize);
        }

        private void drawDots(Graphics g, int[][] matrix, int startX, int startY, Color color, int dotSize) {
            // 행렬을 순회하며 도트를 찍기
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] == 1) { // 행렬의 값이 1일 때 도트를 찍음
                        g.setColor(color);
                        g.fillRect(startX + j * dotSize, startY + i * dotSize, dotSize, dotSize); // 도트 찍기
                    }
                }
            }
        }



}

