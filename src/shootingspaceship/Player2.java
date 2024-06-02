package shootingspaceship;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Player2 extends Player {

    private Image playerImage;

    public Player2(int x, int y, int min_x, int max_x) {
        super(x, y, min_x, max_x);
        playerImage = new ImageIcon("src/우주선2.png").getImage(); // 이미지 경로를 수정하세요
    }
    
    @Override
    public Shot generateShot() {
        return new Shot(getX(), getY() - playerImage.getHeight(null) / 2);
    }

    @Override
    public void drawPlayer(Graphics g) {
        g.drawImage(playerImage, getX() - playerImage.getWidth(null) / 2, getY() - playerImage.getHeight(null) / 2, null);
    }
}