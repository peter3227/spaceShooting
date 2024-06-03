package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class ShootingEnemy extends Enemy {
    private float x_pos;
    private float y_pos;
    private float delta_x;
    private float delta_y;
    private int max_x;
    private int max_y;
    private float delta_y_inc;
    private final int collision_distance = 10;
    private Random rand = new Random();
    private int health = 2; // Add health property

    public ShootingEnemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc) {
        super(x, y, delta_x, delta_y, max_x, max_y, delta_y_inc);
        x_pos = x;
        y_pos = y;
        this.delta_x = delta_x;
        this.delta_y = delta_y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.delta_y_inc = delta_y_inc;
    }

    public void move(ArrayList<enemyShot> enemyShots) {
        x_pos += delta_x;
        y_pos += delta_y;

        // Shooting logic
        if (rand.nextInt(100) < 1) { // 1% chance to shoot each frame
            enemyShots.add(generateEnemyShot());
        }

        if (x_pos < 0) {
            x_pos = 0;
            delta_x = -delta_x;
        } else if (x_pos > max_x) {
            x_pos = max_x;
            delta_x = -delta_x;
        }
        if (y_pos > max_y) {
            y_pos = 0;
            delta_y += delta_y_inc;
        }
    }

    public boolean isCollidedWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot != null && shot.isActive()) { // Check if the shot is active
                if (-15 <= (y_pos - shot.getY()) && (y_pos - shot.getY() <= 15)) {
                    if (-15 <= (x_pos - shot.getX()) && (x_pos - shot.getX() <= 15)) {
                        // Collision detected
                        shot.collided(); // Deactivate the shot immediately
                        SoundPlayer.playSound("src/resources/isCollidedWithShot.wav");
                        health--; // Decrement health
                        if (health <= 0) {
                            return true; // Return true only if health is zero or less
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isCollidedWithShot(ExplosiveShot[] Eshots) {
        for (ExplosiveShot Eshot : Eshots) {
            if (Eshot != null && Eshot.isActive()) { // 총알이 활성화되어 있을 때만 체크
                if (-20<= (y_pos - Eshot.getY()) && (y_pos - Eshot.getY() <= 20)) {
                    if (-20<= (x_pos - Eshot.getX()) && (x_pos - Eshot.getX() <= 20)) {
                        // 충돌 발생
                    	Eshot.explode();
                        Eshot.collided(); // 총알 즉시 비활성화
                        SoundPlayer.playSound("src/resources/isCollidedWithShot.wav");
                        health--;
                        if (health <= 0) {
                            return true; 
                        }
                    }
                }
            }
        }
        return false;
    }

    public enemyShot generateEnemyShot() {
        return new enemyShot(x_pos, y_pos, 3);
    }

    @Override
    public void draw(Graphics g) {
        if (health == 1) {
            g.setColor(Color.blue); // Change color to blue if health is 1
        } else {
            g.setColor(Color.red); // Default color is red
        }

        int[][] dotPattern1 = {
            {0,0,1,0,0,0,0,0,1,0,0},
            {1,0,0,1,0,0,0,1,0,0,1},
            {1,0,1,1,1,1,1,1,1,0,1},
            {1,1,1,0,1,1,1,0,1,1,1},
            {0,1,1,0,1,1,1,0,1,1,0},
            {0,0,1,1,1,1,1,1,1,0,0},
            {0,0,1,0,0,0,0,0,1,0,0},
            {0,0,0,1,1,0,1,1,0,0,0}
        };

        int[][] dotPattern2 = {
            {0,0,0,1,0,0,0,0,0,1,0,0,0},
            {1,0,0,0,1,0,0,0,1,0,0,0,1},
            {1,1,0,1,1,1,1,1,1,1,0,1,1},
            {0,1,1,1,0,1,1,1,0,1,1,1,0},
            {0,0,1,1,0,1,1,1,0,1,1,0,0},
            {0,0,0,1,1,1,1,1,1,1,0,0,0},
            {0,0,0,1,0,0,0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0,0,1,0,0,0,0},
            {0,0,0,0,1,0,0,0,1,0,0,0,0}
        };

        int[][] patternToDraw = (System.currentTimeMillis() / 100) % 10 < 5 ? dotPattern1 : dotPattern2;

        // Dot size
        int dotSize = 2;

        // Draw dots
        for (int i = 0; i < patternToDraw.length; i++) {
            for (int j = 0; j < patternToDraw[i].length; j++) {
                if (patternToDraw[i][j] == 1) {
                    int dotX = (int) x_pos - patternToDraw[0].length / 2 * dotSize + j * dotSize;
                    int dotY = (int) y_pos - patternToDraw.length / 2 * dotSize + i * dotSize;
                    g.fillRect(dotX, dotY, dotSize, dotSize);
                }
            }
        }
    }
}

