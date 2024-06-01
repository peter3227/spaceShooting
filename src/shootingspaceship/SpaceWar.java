package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class SpaceWar extends Shootingspaceship implements Runnable {

    private Thread th;
    private Player player;
    private Shot[] shots;
    private ArrayList<Item> items;
    private ArrayList<Enemy> enemies;
    private ArrayList<enemyShot> enemyShots;
    private float shotSpeed = -2;
    private float playerLeftSpeed = -2;
    private float playerRightSpeed = 2;
    private final int width = 500;
    private final int height = 500;
    private final int playerMargin = 10;
    private int enemyMaxDownSpeed = 1;
    private int enemyMaxHorizonSpeed = 1;
    private int enemyTimeGap = 2000; // unit: msec
    private final float enemyDownSpeedInc = 0.3f;
    private final float ItemDropRate = 1f;
    private final int maxEnemySize = 1000;
    private int enemySize;
    private int score;
    private javax.swing.Timer timer;
    private javax.swing.Timer difficultyTimer;
    private boolean playerMoveLeft;
    private boolean playerMoveRight;
    private Image dbImage;
    private Graphics dbg;
    private Random rand;
    private int maxShotNum = 5;
    public int enemyHP;
    private boolean gameOver;
    private int playerLives = 4; // New field to track player lives
    private JFrame parentFrame;

    public SpaceWar(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setOpaque(false);
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width - playerMargin);
        shots = new Shot[maxShotNum];
        items = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyShots = new ArrayList<>();
        enemySize = 0;
        score = 0;
        rand = new Random(1);
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy());
        timer.start();
        difficultyTimer = new javax.swing.Timer(5000, new IncreaseDifficulty());
        difficultyTimer.start();
        addKeyListener(new ShipControl());
        setFocusable(true);
        gameOver = false;
    }
    
    public void start() {
        th = new Thread(this);
        th.start();
    }

    public void restart() {
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width - playerMargin);
        shots = new Shot[maxShotNum];
        playerMoveLeft = false;
        playerMoveRight = false;
        playerLeftSpeed = -2;
        playerRightSpeed = 2;
        shotSpeed = -2;
        items.clear();
        enemies.clear();
        enemyShots.clear();
        enemySize = 0;
        score = 0;
        playerLives = 4; // Reset player lives on restart
        enemyMaxDownSpeed = 1;
        enemyMaxHorizonSpeed = 1;
        enemyTimeGap = 2000;
        timer.setDelay(enemyTimeGap);
        timer.start();
        difficultyTimer.start();
        gameOver = false;
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

                Enemy newEnemy;
                if (rand.nextBoolean()) {
                    newEnemy = new ShootingEnemy((int) (rand.nextFloat() * width), 10, 2, 0, width, height, enemyDownSpeedInc);
                } else {
                    newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width, height, enemyDownSpeedInc);
                }

                synchronized (enemies) {
                    enemies.add(newEnemy);
                }
            } else {
                timer.stop();
            }
        }
    }

    private class IncreaseDifficulty implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (enemyTimeGap > 500) {
                enemyTimeGap -= 100;
                timer.setDelay(enemyTimeGap);
            }
            enemyMaxDownSpeed += 1;
            enemyMaxHorizonSpeed += 1;
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
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot();
                            SoundPlayer.playSoundWithVolume("src/resources/shootingBgm.wav",-10);
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

        while (!gameOver) {
            // Update shots
            synchronized (shots) {
                for (int i = 0; i < shots.length; i++) {
                    if (shots[i] != null) {
                        shots[i].moveShot(shotSpeed);
                        if (shots[i].getY() < 0) {
                            shots[i] = null;
                        }
                    }
                }
            }

            // Update player movement
            synchronized (player) {
                if (playerMoveLeft) {
                    player.moveX(playerLeftSpeed);
                } else if (playerMoveRight) {
                    player.moveX(playerRightSpeed);
                }
            }

            // Update enemies
            synchronized (enemies) {
                Iterator<Enemy> enemyList = enemies.iterator();
                while (enemyList.hasNext()) {
                    Enemy enemy = enemyList.next();
                    if (enemy instanceof ShootingEnemy) {
                        ((ShootingEnemy) enemy).move(enemyShots);
                    } else {
                        enemy.move();
                    }
                    if (enemy.isCollidedWithShot(shots)) {
                        enemyList.remove();
                        score += 10; // Increase score by 10 for each enemy killed
                        if (Math.random() < ItemDropRate) {
                            Item newItem;
                            if (Math.random() < 0.5) { // 50% chance of creating PlayerSpeedUpItem
                                newItem = new PlayerSpeedUpItem(enemy.getX(), enemy.getY());
                            } else {
                                newItem = new Item(enemy.getX(), enemy.getY());
                            }
                            synchronized (items) {
                                items.add(newItem);
                            }
                        }
                    }
                    if (enemy.isCollidedWithPlayer(player)) {
                        enemyList.remove();
                        playerLives--; // Decrease player lives
                        SoundPlayer.playSoundWithVolume("src/resources/hpDown.wav",-10);
                        if (playerLives <= 0) {
                            gameOver = true;
                            stopTimers(); // Stop timers when game is over
                            showGameOverDialog();
                        }
                    }
                }
            }

            // Update enemy shots
            synchronized (enemyShots) {
                Iterator<enemyShot> shotList = enemyShots.iterator();
                while (shotList.hasNext()) {
                    enemyShot eshot = shotList.next();
                    eshot.move();
                    if (eshot.getY() > height) {
                        shotList.remove();
                    }
                    if (eshot.isCollidedWithPlayer(player)) {
                        shotList.remove();
                        playerLives--; // Decrease player lives
                        SoundPlayer.playSoundWithVolume("src/resources/hpDown.wav",-10);
                        if (playerLives <= 0) {
                            gameOver = true;
                            stopTimers(); // Stop timers when game is over
                            showGameOverDialog();
                        }
                    }
                }
            }

            // Update items
            synchronized (items) {
                Iterator<Item> itemList = items.iterator();
                while (itemList.hasNext()) {
                    Item item = itemList.next();
                    item.moveItem(-2);
                    if (item.getY() > height) {
                        itemList.remove();
                    }
                    if (item.isCollidedWithPlayer(player)) {
                        itemList.remove();
                        if (item instanceof PlayerSpeedUpItem) {
                            playerLeftSpeed -= 0.2f;
                            playerRightSpeed += 0.2f;
                        } else {
                            shotSpeed -= 0.2f;
                        }
                    }
                }
            }

            // Redraw
            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // Do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    private void stopTimers() {
        timer.stop();
        difficultyTimer.stop();
    }

    private void showGameOverDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameOverDialog gameOverDialog = new GameOverDialog(parentFrame, SpaceWar.this, score);
                gameOverDialog.setVisible(true);
            }
        });
    }

    public void paintComponent(Graphics g) {
        player.drawPlayer(g);

        synchronized (enemies) {
            Iterator<Enemy> enemyList = enemies.iterator();
            while (enemyList.hasNext()) {
                Enemy enemy = enemyList.next();
                enemy.draw(g);
            }
        }

        synchronized (items) {
            Iterator<Item> itemList = items.iterator();
            while (itemList.hasNext()) {
                Item item = itemList.next();
                item.drawItem(g);
            }
        }

        for (int i = 0; i < shots.length; i++) {
            if (shots[i] != null) {
                shots[i].drawShot(g);
            }
        }

        synchronized (enemyShots) {
            Iterator<enemyShot> shotList = enemyShots.iterator();
            while (shotList.hasNext()) {
                enemyShot eshot = shotList.next();
                eshot.draw(g);
            }
        }

        // Draw the score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);

     // Draw the player lives using images
        int imgWidth = 15; // Width of the image
        int imgHeight = 15; // Height of the image
        for (int i = 0; i < playerLives; i++) {
            // Load the image
            ImageIcon icon = new ImageIcon("src/resources/hp.png");
            Image image = icon.getImage();

            // Draw the image
            g.drawImage(image, 10 + (i * (imgWidth + 2)), height - imgHeight - 10, imgWidth, imgHeight, this);
            }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("SpaceWar");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpaceBackground spaceBackground = new SpaceBackground();
        frame.getContentPane().add(spaceBackground);
        SpaceWar war = new SpaceWar(frame);
        spaceBackground.add(war);
        frame.pack();
        frame.setVisible(true);
        war.start();
    }
}

//게임 오버 팝업창
class GameOverDialog extends JDialog {
  public GameOverDialog(JFrame parent, SpaceWar game, int finalScore) {
      super(parent, "Game Over", true);
      setLayout(new BorderLayout());
      JLabel label = new JLabel("GAME OVER", SwingConstants.CENTER);
      label.setFont(new Font("Serif", Font.BOLD, 36));
      add(label, BorderLayout.CENTER);

      JLabel scoreLabel = new JLabel("Score: " + finalScore, SwingConstants.CENTER);
      scoreLabel.setFont(new Font("Serif", Font.PLAIN, 24));
      add(scoreLabel, BorderLayout.NORTH);

      JButton retryButton = new JButton("Retry");
      retryButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              game.restart();
              setVisible(false);
          }
      });
      add(retryButton, BorderLayout.SOUTH);

      setSize(300, 200);
      setLocationRelativeTo(parent);
  }
}