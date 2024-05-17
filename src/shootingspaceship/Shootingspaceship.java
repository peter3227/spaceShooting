package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Shootingspaceship 클래스를 JPanel과 Runnable 인터페이스를 구현하여 정의한다.
public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th; // 쓰레드 객체
    private Player player; // 플레이어 객체
    private Shot[] shots; // Shot 배열
    private ArrayList enemies; // 적들을 담는 ArrayList
    private ScoreBoard scoreBoard; // 점수판
    
    private final int shotSpeed = -10; // 총알의 이동 속도
    private final int playerLeftSpeed = -2; // 플레이어가 왼쪽으로 이동하는 속도
    private final int playerRightSpeed = 2; // 플레이어가 오른쪽으로 이동하는 속도
    private final int width = 500; // 게임 화면의 너비
    private final int height = 500; // 게임 화면의 높이
    private final int playerMargin = 10; // 플레이어의 가장자리 여백
    private final int enemyMaxDownSpeed = 1; // 적의 최대 하강 속도
    private final int enemyMaxHorizonSpeed = 1; // 적의 최대 수평 속도
    private final int enemyTimeGap = 2000; // 적을 추가하는 간격 (밀리초 단위)
    private final float enemyDownSpeedInc = 0.3f; // 적의 하강 속도 증가량
    private final int maxEnemySize = 10; // 최대 적의 수
    
    private int enemySize; // 현재 적의 수
    private javax.swing.Timer timer; // 타이머 객체
    private boolean playerMoveLeft; // 플레이어가 왼쪽으로 이동 중인지 여부
    private boolean playerMoveRight; // 플레이어가 오른쪽으로 이동 중인지 여부
    private Image dbImage; // 더블 버퍼링을 위한 이미지
    private Graphics dbg; // 더블 버퍼링을 위한 그래픽스 객체
    private Random rand; // 난수 생성기
    private int maxShotNum = 100; // 최대 총알 개수

    // Shootingspaceship 클래스의 생성자
    public Shootingspaceship() {
        setOpaque(false); ///////////////////////////////////////////////////////////////////////////Shootingspaceship 패널의 배경 투명화
        setPreferredSize(new Dimension(width, height)); // 패널의 크기 설정
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width - playerMargin); // 플레이어 객체 생성
        shots = new Shot[maxShotNum]; // Shot 배열 초기화
        enemies = new ArrayList(); // 적들을 담는 ArrayList 초기화
        enemySize = 0; // 초기 적의 수 0으로 설정
        rand = new Random(1); // 시드가 1인 난수 생성기 초기화
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy()); // 타이머 초기화 및 시작
        timer.start(); // 타이머 시작
        addKeyListener(new ShipControl()); // 키 이벤트 처리를 위한 리스너 추가
        scoreBoard = new ScoreBoard();
        setFocusable(true); // 포커스 설정
    }

    // 게임 시작 메서드
    public void start() {
        th = new Thread(this); // 쓰레드 생성
        th.start(); // 쓰레드 시작
    }

    // 타이머에 의해 호출되는 내부 클래스
    private class addANewEnemy implements ActionListener {

        // 타이머 이벤트 발생 시 수행되는 메서드
        public void actionPerformed(ActionEvent e) {
            if (++enemySize <= maxEnemySize) { // 최대 적의 수에 도달하지 않은 경우
                float downspeed;
                // 랜덤하게 적의 하강 속도 결정
                do {
                    downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                } while (downspeed == 0);

                // 랜덤하게 적의 수평 속도 결정
                float horspeed = rand.nextFloat() * 2 * enemyMaxHorizonSpeed - enemyMaxHorizonSpeed;

                // 새로운 적 객체 생성 및 ArrayList에 추가
                Enemy newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width, height, enemyDownSpeedInc);
                enemies.add(newEnemy);
            } else { // 최대 적의 수에 도달한 경우
                timer.stop(); // 타이머 중지
            }
        }
    }

    // 플레이어 조작을 위한 KeyListener 내부 클래스
    private class ShipControl implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = true; // 왼쪽 화살표 키가 눌렸음을 설정
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = true; // 오른쪽 화살표 키가 눌렸음을 설정
                    break;
                case KeyEvent.VK_UP:
                    // 위쪽 화살표 키가 눌리면 새로운 총알 생성
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot(); // 플레이어의 위치에서 총알 생성
                            SoundPlayer.playSound("src/resources/shootingBgm.wav"); // 총알 발사 효과음 재생
                            break;
                        }
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = false; // 왼쪽 화살표 키가 떼졌음을 설정
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = false; // 오른쪽 화살표 키가 떼졌음을 설정
                    break;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    // 게임 루프
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // 현재 쓰레드의 우선순위 설정

        while (true) {
            // 총알 이동 및 충돌 처리
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    shots[i].moveShot(shotSpeed); // 총알 이동
                    if (shots[i].getY() < 0) {
                        shots[i] = null; // 총알이 화면을 벗어난 경우 삭제
                    }
                }
            }

            // 플레이어 이동 처리
            if (playerMoveLeft) {
                player.moveX(playerLeftSpeed); // 왼쪽으로 이동
            } else if (playerMoveRight) {
                player.moveX(playerRightSpeed); // 오른쪽으로 이동
            }

            // 적 이동 처리
            Iterator enemyList = enemies.iterator();
            while (enemyList.hasNext()) {
                Enemy enemy = (Enemy) enemyList.next();
                enemy.move(); // 적 이동
            }

            repaint(); // 화면 다시 그리기

            try {
                Thread.sleep(10); // 일시 정지
            } catch (InterruptedException ex) {
                // 예외 처리
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY); // 현재 쓰레드의 우선순위 설정
        }
    }

    // 더블 버퍼링을 위한 이미지 초기화 메서드
    public void initImage(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height); // 더블 버퍼링 이미지 생성
            dbg = dbImage.getGraphics(); // 더블 버퍼링을 위한 그래픽스 객체 생성
        }

        setOpaque(false); // 배경색 설정
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height); // 사각형으로 채우기

//        dbg.setColor(getForeground()); // 전경색 설정
        //paint (dbg);

        g.drawImage(dbImage, 0, 0, this); // 이미지 그리기
    }

    // 화면을 그리는 메서드
    public void paintComponent(Graphics g) {
    	//이미지 초기화
//    	initImage(g);
        // 플레이어 그리기
        player.drawPlayer(g);
        scoreBoard.draw(g, getWidth()); // 점수판 그리기
        // 적 그리기 및 충돌 검사
        Iterator enemyList = enemies.iterator();
        while (enemyList.hasNext()) {
            Enemy enemy = (Enemy) enemyList.next();
            enemy.draw(g); // 적 그리기
            if (enemy.isCollidedWithShot(shots)) { // 적과 총알 충돌 검사
                enemyList.remove(); // 적 제거
                scoreBoard.increaseScore(); // 점수 증가
            }
            if (enemy.isCollidedWithPlayer(player)) { // 적과 플레이어 충돌 검사
                enemyList.remove(); // 적 제거
                System.exit(0); // 게임 종료
            }
        }

        // 총알 그리기
        for (int i = 0; i < shots.length; i++) {
            if (shots[i] != null) {
                shots[i].drawShot(g); // 총알 그리기
            }
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Shooting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로그램 종료 설정
        SpaceBackground spaceBackground = new SpaceBackground(); //추가
        frame.getContentPane().add(spaceBackground); //추가
        Shootingspaceship ship = new Shootingspaceship(); // Shootingspaceship 객체 생성
        spaceBackground.add(ship); // 프레임에 패널 추가   변경
        frame.pack(); // 컴포넌트 크기에 맞게 프레임 크기 조정
        frame.setVisible(true); // 프레임 표시
        ship.start(); // 게임 시작
    }
}

