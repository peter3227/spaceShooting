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
        return new Shot(x_pos, y_pos - 15);
    }

    public void drawPlayer(Graphics g) {
        g.setColor(Color.red);

        // 우주선 몸체
        g.fillRect(x_pos - 5, y_pos - 4, 10, 8);
        g.fillRect(x_pos - 15, y_pos, 30, 5);

        // 엔진
        g.setColor(Color.orange);
        g.fillRect(x_pos - 2, y_pos - 15, 4, 30);

        // 캐노피
        g.setColor(Color.gray);
        g.fillRect(x_pos - 8, y_pos + 5, 16, 5);
    }


}

