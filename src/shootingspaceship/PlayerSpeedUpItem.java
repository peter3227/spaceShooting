package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerSpeedUpItem extends Item {


    public PlayerSpeedUpItem(int x, int y) {
        super(x, y);
    }
    @Override
    public void drawItem(Graphics g) {
        g.setColor(Color.BLUE); // PlayerSpeedUpItem은 파란색으로 그립니다.
        g.fillRect(getX(), getY(), 10, 10);
    }
}
