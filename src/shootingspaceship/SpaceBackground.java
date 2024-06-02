package shootingspaceship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 별을 나타내는 클래스
class Star {
    private int x; // x 좌표
    private int y; // y 좌표
    private int size; // 별의 크기
    private int speed; // 이동 속도

    // 별 객체 생성자
    Star(int x, int y, int size, int speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    // 별 이동 메서드
    void move() {
        y += speed;
        // 별이 화면을 벗어나면 다시 위로 이동하여 랜덤한 x 좌표에서 생성
        if (y > SpaceBackground.HEIGHT) {
            y = 0;
            x = new Random().nextInt(SpaceBackground.WIDTH);
        }
    }

    // 별 그리기 메서드
    void draw(Graphics g) {
        g.setColor(Color.white);
        // 원 모양으로 별 그리기
        g.fillOval(x, y, size, size);
    }
}

// 우주 배경을 나타내는 JPanel 클래스
public class SpaceBackground extends JPanel {
    static final int WIDTH = 500; // 화면 너비
    static final int HEIGHT = 500; // 화면 높이
    private List<Star> stars; // 별 객체를 담는 리스트

    // 우주 배경 생성자
    SpaceBackground() {
        setBackground(Color.black); // 배경색 설정
        stars = new ArrayList<>(); // 별 객체 리스트 초기화
        Random random = new Random();
        // 초기에 랜덤하게 별 객체 생성하여 리스트에 추가
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            int size = random.nextInt(4) + 1;
            int speed = random.nextInt(5) + 1;
            stars.add(new Star(x, y, size, speed));
        }

        // 타이머를 이용하여 별 이동과 화면 갱신을 주기적으로 처리
        Timer timer = new Timer(10, e -> {
            for (Star star : stars) {
                star.move();
            }
            repaint(); // 화면 갱신 요청
        });
        timer.start(); // 타이머 시작
    }

    // JPanel의 그림 메서드 재정의
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 리스트에 있는 모든 별을 그림
        for (Star star : stars) {
            star.draw(g);
        }
    }
}

