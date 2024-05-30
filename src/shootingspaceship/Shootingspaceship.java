package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th;
    private Player player;
    private Shot[] shots;
    private ArrayList<Item> items;
    private ArrayList<Enemy> enemies;
    private int shotSpeed = -2;
    private final int playerLeftSpeed = -2;
    private final int playerRightSpeed = 2;
    private final int width = 500;
    private final int height = 500;
    private final int playerMargin = 10;
    private final int enemyMaxDownSpeed = 1;
    private final int enemyMaxHorizonSpeed = 1;
    private final int enemyTimeGap = 2000; //unit: msec
    private final float enemyDownSpeedInc = 0.3f;
    private final int maxEnemySize = 100;
    private int enemySize;
    private javax.swing.Timer timer;
    private boolean playerMoveLeft;
    private boolean playerMoveRight;
    private Image dbImage;
    private Graphics dbg;
    private Random rand;
    private int maxShotNum = 5;

    public Shootingspaceship() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width - playerMargin);
        shots = new Shot[maxShotNum];
        items = new ArrayList<>();
        enemies = new ArrayList<>();
        enemySize = 0;
        rand = new Random(1);
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy());
        timer.start();
        addKeyListener(new ShipControl());
        setFocusable(true);
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }

    private class addANewEnemy implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (++enemySize <= maxEnemySize) {
                float downspeed;
                do {
                    downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                } while (downspeed == 0);

                float horspeed = rand.nextFloat() * 2 * enemyMaxHorizonSpeed - enemyMaxHorizonSpeed;
                System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);

                Enemy newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width, height, enemyDownSpeedInc);
                enemies.add(newEnemy);
            } else {
                timer.stop();
            }
        }
    }

    private class ShipControl implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = true;
                    break;
                case KeyEvent.VK_UP:
                    // generate new shot and add it to shots array
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot();
                            SoundPlayer.playSound("src/resources/shootingBgm.wav"); // 추가
                            break;
                        }
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = false;
                    break;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        while (true) {
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    // shot 이동
                    shots[i].moveShot(shotSpeed);

                    // shot이 화면 밖으로 나갔는지 테스트
                    if (shots[i].getY() < 0) {
                        // shots 배열에서 제거
                        shots[i] = null;
                    }
                }
            }

            if (playerMoveLeft) {
                player.moveX(playerLeftSpeed);
            } else if (playerMoveRight) {
                player.moveX(playerRightSpeed);
            }

            Iterator<Enemy> enemyList = enemies.iterator();
            while (enemyList.hasNext()) {
                Enemy enemy = enemyList.next();
                enemy.move();
                if (enemy.isCollidedWithShot(shots)) {
                    enemyList.remove();
                    Item newItem = new Item(enemy.getX(), enemy.getY());
                    items.add(newItem);
                }
                if (enemy.isCollidedWithPlayer(player)) {
                    enemyList.remove();
                    System.exit(0);
                }
            }

            Iterator<Item> itemList = items.iterator();
            while (itemList.hasNext()) {
                Item item = itemList.next();
                item.moveItem(-1); // 아이템이 천천히 아래로 이동
                if (item.getY() > height) {
                    itemList.remove();
                }
                if (item.isCollidedWithPlayer(player)) {
                    itemList.remove();
                    shotSpeed -= 1;
                }
            }

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 플레이어 그리기
        player.drawPlayer(g);

        // 적 그리기
        Iterator<Enemy> enemyList = enemies.iterator();
        while (enemyList.hasNext()) {
            Enemy enemy = enemyList.next();
            enemy.draw(g);
        }

        // 아이템 그리기
        Iterator<Item> itemList = items.iterator();
        while (itemList.hasNext()) {
            Item item = itemList.next();
            item.drawItem(g);
        }

        // shot 그리기
        for (int i = 0; i < shots.length; i++) {
            if (shots[i] != null) {
                shots[i].drawShot(g);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Shooting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Shootingspaceship ship = new Shootingspaceship();
        frame.getContentPane().add(ship);
        frame.pack();
        frame.setVisible(true);
        ship.start();
    }
}
