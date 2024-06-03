package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class ExplosiveShot extends Shot {
	protected final int doubleRadius;
    private ExplosionAnimation explosionAnimation;
    private final int explosionRadius;
    protected final int radius = 3; //총알의 크기

    /**
     * ExplosiveShot 생성자
     * @param x 발사체의 초기 x 좌표
     * @param y 발사체의 초기 y 좌표
     */
    public ExplosiveShot(int x, int y) {
        super(x, y);  // 상위 클래스의 생성자 호출
        this.doubleRadius = radius * 5;
        this.explosionRadius = 100; //폭발 반경 70으로 설정
        String[] explosionImages = {"src/explosion1.jpg"};
        this.explosionAnimation = new ExplosionAnimation(x, y, explosionImages);
    }

    /**
     * 발사체가 충돌했을 때 폭발을 시뮬레이션합니다.
     */
    @Override
    public void collided() {
        super.collided();  // 상위 클래스의 collided 메소드 호출
        explode();  // 폭발 효과 호출
    }

    /**
     * 폭발 효과를 시뮬레이션합니다.
     */
    public void explode() {
        // 폭발 효과를 시뮬레이션하는 코드 (예: 폭발 애니메이션, 범위 내 객체에 피해 등)
        System.out.println("ExplosiveShot exploded at (" + x_pos + ", " + y_pos + ")");
    }

    public void drawShot(Graphics g) {
        if (!alive) {
        	explosionAnimation.draw(g);
            return; // 총알이 살아있지 않으면 그리지 않음
        }
        g.setColor(Color.orange);  // 발사체의 색상을 오렌지색으로 설정
        g.fillOval(x_pos, y_pos, doubleRadius, doubleRadius);  // 발사체를 원형으로 그림
    }


public void checkCollision(List<Enemy>enemies) {
	 for (Enemy enemy : enemies) {
         if (isCollidingWith(enemy)) {
             enemy.hit();  // 적을 맞췄을 때 죽임
             collided();  // 발사체 충돌 처리
             // 폭발 범위 내 다른 적들에게 피해를 줌
             for (Enemy e : enemies) {
                 if (isInExplosionRange(e)) {
                     e.hit();  // 폭발 범위 내의 적을 죽임
                 }
             }
             break;  // 첫 번째 충돌 후 종료
         }
     }

}

private boolean isCollidingWith(Enemy enemy) {
    int enemyX = enemy.getX();
    int enemyY = enemy.getY();
    int enemySize = enemy.getSize();
    return x_pos < enemyX + enemySize && x_pos + radius > enemyX &&
           y_pos < enemyY + enemySize && y_pos + radius > enemyY;
}

private boolean isInExplosionRange(Enemy enemy) {
    int dx = enemy.getX() - x_pos;
    int dy = enemy.getY() - y_pos;
    double distance = Math.sqrt(dx * dx + dy * dy);
    return distance <= explosionRadius;
}
}


