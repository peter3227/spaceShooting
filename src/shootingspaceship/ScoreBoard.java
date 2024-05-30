package shootingspaceship;

import java.awt.*;

public class ScoreBoard {
    private int score;
    private Font font;

    public ScoreBoard() {
        score = 0;
        font = new Font("Arial", Font.BOLD, 20);
    }

    public void increaseScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void draw(Graphics g, int width) {
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("Score: " + score, width - 100, 30);
    }
}
