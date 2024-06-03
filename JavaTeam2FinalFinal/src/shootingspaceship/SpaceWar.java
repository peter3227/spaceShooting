package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class SpaceWar extends Shootingspaceship implements Runnable {

	private Thread th; // 게임 루프를 담당하는 스레드
	private Player player; // 플레이어 객체
	private Shot[] shots; // 플레이어가 발사한 총알 배열
	private ExplosiveShot[] Eshots; // 플레이어가 발사한 폭발성 총알 배열
	private ArrayList<Item> items; // 아이템 리스트
	private Vector<Enemy> enemies; // 적들의 벡터
	private ArrayList<enemyShot> enemyShots; // 적의 발사한 총알 리스트

	private final int width = 500; // 게임 화면의 너비
	private final int height = 500; // 게임 화면의 높이
	private final int playerMargin = 15; // 플레이어의 여백
	private int enemyMaxDownSpeed = 1; // 적의 최대 하강 속도
	private int enemyMaxHorizonSpeed = 1; // 적의 최대 수평 속도
	private int enemyTimeGap = 2000; // 적 생성 간격 (단위: 밀리초)
	private final float enemyDownSpeedInc = 0.3f; // 적의 하강 속도 증가량
	private final float ItemDropRate = 0.2f; // 아이템 드롭 확률
	private final int maxEnemySize = 1000; // 최대 적 개수
	private int enemySize; // 현재 적 개수
	private int score; // 점수
	private boolean paused = false; // 일시정지 상태

	private javax.swing.Timer timer; // 주기적으로 게임을 업데이트하는 타이머
	private javax.swing.Timer difficultyTimer; // 적의 속도를 증가시키는 타이머
	private boolean playerMoveLeft; // 플레이어가 왼쪽으로 이동하는지 여부
	private boolean playerMoveRight; // 플레이어가 오른쪽으로 이동하는지 여부
	private Random rand; // 랜덤 객체
	private int maxShotNum = 10; // 최대 총알 개수
	private int maxEShotNum = 1; // 최대 폭발성 총알 개수
	public int enemyHP; // 적의 체력
	private boolean gameOver; // 게임 종료 여부
	private JFrame parentFrame; // 부모 프레임

	private Player[] players; // 플레이어 배열
	private JPanel buttonPanel; // 버튼 패널
	private JFrame frame; // 게임 프레임

	private float playerLeftSpeed = -2.0f; // 플레이어의 왼쪽 이동 속도
	private float playerRightSpeed = 2.0f; // 플레이어의 오른쪽 이동 속도
	private float shotSpeed = -2.0f; // 총알 속도
	private int playerLives ; // 플레이어의 생명
    
    public SpaceWar(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setOpaque(false);
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player(width / 2, (int) (height * 0.85), playerMargin, width - playerMargin, shotSpeed , playerLeftSpeed, playerRightSpeed, playerLives);

        players = new Player[4];
        players[0] = new Player1(width / 2 , (int) (height * 0.85), playerMargin, width - playerMargin, shotSpeed , playerLeftSpeed, playerRightSpeed, playerLives);
        players[1] = new Player2(width / 2, (int) (height * 0.85), playerMargin, width - playerMargin, shotSpeed , playerLeftSpeed, playerRightSpeed, playerLives);
        players[2] = new Player3(width / 2 , (int) (height * 0.85), playerMargin, width - playerMargin, shotSpeed , playerLeftSpeed, playerRightSpeed, playerLives);
        players[3] = new Player(width / 2, (int) (height * 0.85), playerMargin, width - playerMargin, shotSpeed , playerLeftSpeed, playerRightSpeed, playerLives);

        shots = new Shot[maxShotNum];
        Eshots = new ExplosiveShot[maxEShotNum] ;
        items = new ArrayList<>();
        enemies = new Vector<>();
        enemyShots = new ArrayList<>();
        enemySize = 0;
        score = 0;
        rand = new Random(1);
        
        addKeyListener(new ShipControl());
        setFocusable(true);
        gameOver = false;
    }

    // 플레이어 버튼 생성
    public void createPlayerSelectionButtons(JFrame frame) {
        this.frame = frame;
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.black);
        buttonPanel.setLayout(new FlowLayout());

        JButton player1Button = new JButton("HARD");
        player1Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPlayer(0);
            }
        });
        buttonPanel.add(player1Button);

        JButton player2Button = new JButton("NORMAL");
        player2Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPlayer(1);
            }
        });
        buttonPanel.add(player2Button);

        JButton player3Button = new JButton("SUPEREASY");
        player3Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPlayer(2);
            }
        });
        buttonPanel.add(player3Button);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void selectPlayer(int index) {
        synchronized (this) {
        	player = players[index];
            shotSpeed = players[index].getShotSpeed();
            playerLeftSpeed = players[index].getplayerLeftSpeed();
            playerRightSpeed = players[index].getplayerRightSpeed();
            playerLives = players[index].getplayerLives();
        }
        buttonPanel.setVisible(false);
        frame.revalidate();
        frame.repaint();
        startIfPlayerSelected();
    }

    // 플레이어 버튼을 눌러야 게임 실행가능 
    public void startIfPlayerSelected() {
        synchronized (this) {
            timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy());
            timer.start();
            difficultyTimer = new javax.swing.Timer(5000, new IncreaseDifficulty());
            difficultyTimer.start();
            start();
        }
    }

    public void start() {
        synchronized (this) {
            th = new Thread(this);
            th.start();
        }
    }
    	
    public void restart() {
        synchronized (this) {
            shots = new Shot[maxShotNum];
            Eshots = new ExplosiveShot[maxEShotNum];
            maxEShotNum = 1;
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
            gameOver = false;
            th = new Thread(this);
            th.start();
            
            // Display player selection buttons again
            buttonPanel.setVisible(true);
            frame.revalidate();
            frame.repaint();
        }
    }
    
    private void startTimers() {
        synchronized (this) {
            timer.start();
            difficultyTimer.start();
        }
    }
    private void stopTimers() {
        synchronized (this) {
            timer.stop();
            difficultyTimer.stop();
        }
    }

    private class addANewEnemy implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            synchronized (enemies) {
                if (++enemySize <= maxEnemySize) {
                    float downspeed;
                    do {
                        downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                    } while (downspeed == 0);

                    float horspeed = rand.nextFloat() * 2 * enemyMaxHorizonSpeed - enemyMaxHorizonSpeed;
                    System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);

                    Enemy newEnemy;
                    if (rand.nextBoolean()) {
                        newEnemy = new ShootingEnemy((int) (rand.nextFloat() * width), 10, 2, 0, width - 20 , height, enemyDownSpeedInc);
                    } else {
                        newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width - 20, height, enemyDownSpeedInc);
                    }

                    enemies.add(newEnemy);
                    enemies.notify();
                } else {
                    timer.stop();
                }
            }
        }
    }


    private class IncreaseDifficulty implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            synchronized (this) {
                if (enemyTimeGap > 500) {
                    enemyTimeGap -= 100;
                    timer.setDelay(enemyTimeGap);
                }
                enemyMaxDownSpeed += 1;
                enemyMaxHorizonSpeed += 1;
            }
        }
    }

    private class ShipControl implements KeyListener {
    	public void keyPressed(KeyEvent e) {
    	    synchronized (player) {
    	        switch (e.getKeyCode()) {
    	            case KeyEvent.VK_SPACE:
    	                paused = !paused;
    	                if (paused) {
    	                    stopTimers(); // 타이머 중지
    	                } else {
    	                    startTimers(); // 타이머 재시작
    	                }
    	                break;
    	            case KeyEvent.VK_X:
    	                System.exit(0);
    	                break;
    	            case KeyEvent.VK_LEFT:
    	                playerMoveLeft = true;
    	                break;
    	            case KeyEvent.VK_RIGHT:
    	                playerMoveRight = true;
    	                break;
    	            case KeyEvent.VK_UP:
    	                synchronized (shots) {
    	                    for (int i = 0; i < shots.length; i++) {
    	                        if (shots[i] == null) {
    	                            shots[i] = player.generateShot();
    	                            SoundPlayer.playSoundWithVolume("src/resources/shootingBgm.wav",-10);
    	                            break;
    	                        }
    	                    }
    	                }
    	                break;
    	            case KeyEvent.VK_R: // Add this case for firing explosive shot
                        synchronized (Eshots) {
                            for (int i = 0; i < Eshots.length; i++) {
                                if (Eshots[i] == null) {
                                	Eshots[i] = player.generateExplosiveShot(); // Generate explosive shot
                                    SoundPlayer.playSoundWithVolume("src/resources/shootingBgm.wav",-10);
                                    break;
                                }
                            }
                        }
                        break;
    	        }
    	    }
    	}

    	public void keyReleased(KeyEvent e) {
    	    synchronized (player) {
    	        switch (e.getKeyCode()) {
    	            case KeyEvent.VK_LEFT:
    	                playerMoveLeft = false;
    	                break;
    	            case KeyEvent.VK_RIGHT:
    	                playerMoveRight = false;
    	                break;
    	        }
    	    }
    	}

		@Override
		public void keyTyped(KeyEvent e) {
		
		}
    }


    public void run() {
        // 현재 실행 중인 스레드의 우선순위를 최소 우선순위로 설정
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        // 게임이 종료될 때까지 반복
        while (!gameOver) {
            // 게임이 일시 중지되지 않은 경우
            if (!paused) {
                // 총알 업데이트
                updateShots();
                // 플레이어 움직임 업데이트
                updatePlayerMovement();
                // 적 업데이트
                updateEnemies();
                // 적의 총알 업데이트
                updateEnemyShots();
                // 아이템 업데이트
                updateItems();
                // 화면 다시 그리기
                repaint();
            }

            // 스레드를 잠시 멈춤
            sleepThread();
            // 현재 실행 중인 스레드의 우선순위를 최대 우선순위로 설정
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    private void updateShots() {
        // 플레이어의 총알을 업데이트
        synchronized (shots) {
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    // 총알을 이동
                    shots[i].moveShot(shotSpeed);
                    // 총알이 화면 밖으로 나가면 null로 설정
                    if (shots[i].getY() < 0) {
                        shots[i] = null;
                    }
                }
            }
        }

        // 적의 총알을 업데이트
        synchronized (Eshots) {
            for (int i = 0; i < Eshots.length; i++) {
                if (Eshots[i] != null) {
                    // 총알을 이동
                    Eshots[i].moveShot(shotSpeed);
                    // 총알이 화면 밖으로 나가면 null로 설정
                    if (Eshots[i].getY() < 0) {
                        Eshots[i] = null;
                    }
                }
            }
        }
    }

    private void updatePlayerMovement() {
        // 플레이어의 움직임을 업데이트
        synchronized (player) {
            if (playerMoveLeft) {
                // 왼쪽으로 이동
                player.moveX(playerLeftSpeed);
            } else if (playerMoveRight) {
                // 오른쪽으로 이동
                player.moveX(playerRightSpeed);
            }
        }
    }

    private void updateEnemies() {
        // 제거할 적 리스트 생성
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        synchronized (enemies) {
            // 적 리스트를 순회하면서 업데이트
            Iterator<Enemy> enemyList = enemies.iterator();
            while (enemyList.hasNext()) {
                Enemy enemy = enemyList.next();
                if (enemy instanceof ShootingEnemy) {
                    // 적이 발사형 적인 경우 총알을 발사
                    ((ShootingEnemy) enemy).move(enemyShots);
                } else {
                    // 일반 적인 경우 이동
                    enemy.move();
                }
                // 적이 충돌했는지 확인
                if (handleEnemyCollisions(enemy)) {
                    enemiesToRemove.add(enemy);
                }
            }
            // 충돌한 적을 제거
            enemies.removeAll(enemiesToRemove);
        }
    }

    private boolean handleEnemyCollisions(Enemy enemy) {
        boolean enemyRemoved = false;

        // 총알과 적의 충돌을 확인
        synchronized (shots) {
            if (enemy.isCollidedWithShot(shots) || enemy.isCollidedWithShot(Eshots)) {
                enemyRemoved = true;
                // 적의 종류에 따라 점수를 추가
                if (enemy instanceof ShootingEnemy) score += 20;
                else score += 10;
                // 아이템을 드롭
                dropItem(enemy);
            }
        }

        // 플레이어와 적의 충돌을 확인
        synchronized (player) {
            if (enemy.isCollidedWithPlayer(player)) {
                enemyRemoved = true;
                // 플레이어의 생명을 감소
                playerLives--;
                // 충돌 소리 재생
                SoundPlayer.playSoundWithVolume("src/resources/hpDown.wav", -10);
                // 플레이어의 생명이 0 이하가 되면 게임 오버
                if (playerLives <= 0) {
                    gameOver = true;
                    showGameOverDialog();
                }
            }
        }

        return enemyRemoved;
    }

    private void dropItem(Enemy enemy) {
        // 아이템이 드롭될 확률을 확인
        if (Math.random() < ItemDropRate) {
            Item newItem;
            // 45% 확률로 플레이어 속도 증가 아이템 생성
            if (Math.random() < 0.45f) {
                newItem = new PlayerSpeedUpItem(enemy.getX(), enemy.getY());
            // 45% 확률로 일반 아이템 생성
            } else if (Math.random() < 0.45f) {
                newItem = new Item(enemy.getX(), enemy.getY());
            // 나머지 확률로 체력 회복 아이템 생성
            } else {
                newItem = new HpUpItem(enemy.getX(), enemy.getY());
            }
            synchronized (items) {
                // 아이템을 리스트에 추가
                items.add(newItem);
            }
        }
    }

    private void updateEnemyShots() {
        // 제거할 적의 총알 리스트 생성
        ArrayList<enemyShot> shotsToRemove = new ArrayList<>();
        synchronized (enemyShots) {
            // 적의 총알 리스트를 복제하여 순회
            ArrayList<enemyShot> clonedShots = new ArrayList<>(enemyShots);
            for (enemyShot eshot : clonedShots) {
                // 총알을 이동]
            	if (eshot != null) {
                eshot.move();
                // 총알이 화면 아래로 나가면 제거 리스트에 추가
                if (eshot.getY() > height) {
                    shotsToRemove.add(eshot);
                }
                synchronized (player) {
                    // 총알이 플레이어와 충돌했는지 확인
                    if (eshot.isCollidedWithPlayer(player)) {
                        shotsToRemove.add(eshot);
                        // 플레이어의 생명을 감소
                        playerLives--;
                        // 충돌 소리 재생
                        SoundPlayer.playSoundWithVolume("src/resources/hpDown.wav", -10);
                        // 플레이어의 생명이 0 이하가 되면 게임 오버
                        if (playerLives <= 0) {
                            gameOver = true;
                            showGameOverDialog();
                        }
                    }
                }
            }
            }
            // 충돌한 총알을 제거
            enemyShots.removeAll(shotsToRemove);
        }
    }



    private void updateItems() {
        // 제거할 아이템 리스트 생성
        ArrayList<Item> itemsToRemove = new ArrayList<>();
        synchronized (items) {
            // 아이템 리스트를 순회하면서 업데이트
            Iterator<Item> itemList = items.iterator();
            while (itemList.hasNext()) {
                Item item = itemList.next();
                // 아이템을 이동
                item.moveItem(-2);
                // 아이템이 화면 아래로 나가면 제거 리스트에 추가
                if (item.getY() > height) {
                    itemsToRemove.add(item);
                }
                synchronized (player) {
                    // 아이템이 플레이어와 충돌했는지 확인
                    if (item.isCollidedWithPlayer(player)) {
                        itemsToRemove.add(item);
                        // 아이템 효과를 적용
                        applyItemEffect(item);
                    }
                }
            }
            // 충돌한 아이템을 제거
            items.removeAll(itemsToRemove);
        }
    }

    private void applyItemEffect(Item item) {
        // 플레이어 속도 증가 아이템 효과 적용
        if (item instanceof PlayerSpeedUpItem) {
            if (playerRightSpeed <= 10) {
                playerLeftSpeed -= 1f;
                playerRightSpeed += 1f;
            }
        // 체력 회복 아이템 효과 적용
        } else if (item instanceof HpUpItem) {
            playerLives += 1;
        // 일반 아이템 효과 적용 (총알 속도 감소)
        } else {
            shotSpeed -= 0.5f;
        }
    }

    private void sleepThread() {
        try {
            // 스레드를 10밀리초 동안 잠재움
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            // 예외 처리
        }
    }




    private void showGameOverDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameOverDialog gameOverDialog = new GameOverDialog(parentFrame, SpaceWar.this, score);
                stopTimers();
                gameOverDialog.setVisible(true);
            }
        });
    }

    public void paintComponent(Graphics g) {
        player.drawPlayer(g);
        
        //스페이스바를 눌렀을 때
        if (paused) {
        	g.setColor(Color.WHITE);
            int buttonSize = 30;
            g.fillRect(width/2 - 15, height/2 - 60, buttonSize/2, buttonSize*2);
            g.fillRect(width/2 + 15, height/2 - 60, buttonSize/2, buttonSize*2);
        } 
        
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

        synchronized (shots) {
        	int remainingShots = maxShotNum;
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    shots[i].drawShot(g);
                    remainingShots--;
                }
            }
            g.setColor(Color.YELLOW);
            g.fillRect(width - 100, height - 50, remainingShots * 10, 20);
        }
        
        synchronized (Eshots) {
            for (int i = 0; i < Eshots.length; i++) {
                if (Eshots[i] != null) {
                    Eshots[i].drawShot(g);
                }
            }
        }

        synchronized (enemyShots) {
            Iterator<enemyShot> shotList = enemyShots.iterator();
            while (shotList.hasNext()) {
                enemyShot eshot = shotList.next();
                if (eshot != null) {
                    eshot.draw(g); // <-- NullPointerException 방지
                }
            }
        }

        // Draw the score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("playerspeed:" + playerRightSpeed, 370, 20);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString(" shotspeed:" + -(shotSpeed), 380, 40);
        
        
        // Draw the player lives using images
        int imgWidth = 15; // Width of the image
        int imgHeight = 15; // Height of the image
        for (int i = 0; i < playerLives; i++) {
            // Load the image
            ImageIcon icon = new ImageIcon("src/resources/hp.png");
            Image image = icon.getImage();

            // Draw the image
            g.drawImage(image, 10 + (i * (imgWidth + 2)), height - imgHeight -50, imgWidth, imgHeight, this);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SpaceWar");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Opening opening = new Opening(frame);
        frame.getContentPane().add(opening);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,500); //프레임의 크기 설정
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