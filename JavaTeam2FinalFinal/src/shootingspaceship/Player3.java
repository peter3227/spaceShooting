package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Player3 extends Player {

    private Image playerImage;

    public Player3(int x, int y, int min_x, int max_x, float shotSpeed , float playerLeftSpeed,float playerRightSpeed ,int playerLives) { //플레이어 1의 시작위치
        super(x, y, min_x, max_x,shotSpeed , playerLeftSpeed, playerRightSpeed, playerLives );
        this.shotSpeed = -5;
        this.playerLeftSpeed = -5;
        this.playerRightSpeed = 5;
        this.playerLives = 6;
    }
    
    public void drawPlayer(Graphics g) {
        // 플레이어 위치
        int startX = getX() - 6; // 플레이어 X 위치의 시작점
        int startY = getY() - 7; // 플레이어 Y 위치의 시작점
        
        // 도트 크기
        int dotSize = 2;

        //플레이어의 모양
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
            drawDots(g, whiteMatrix, startX, startY, Color.BLUE, dotSize);
            drawDots(g, redMatrix, startX, startY, Color.WHITE, dotSize);
            drawDots(g, blueMatrix, startX, startY, Color.RED, dotSize);
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