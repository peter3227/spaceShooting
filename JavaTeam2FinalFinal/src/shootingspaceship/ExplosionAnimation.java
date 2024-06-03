package shootingspaceship;  // 클래스의 패키지 이름을 정의

import java.awt.Graphics;    // 이미지를 그리기 위해 Graphics 클래스를 임포트
import java.awt.Image;       // 이미지 객체를 다루기 위해 Image 클래스를 임포트
import javax.swing.ImageIcon; // 이미지를 로드하기 위해 ImageIcon 클래스를 임포트

public class ExplosionAnimation {  // ExplosionAnimation 클래스를 정의
    private int x, y;              // 화면에서 폭발의 좌표
    private int frame = 0;         // 현재 애니메이션의 프레임 인덱스
    private final int totalFrames; // 애니메이션의 총 프레임 수
    private final Image[] explosionImages; // 폭발 이미지를 저장할 배열

    // 생성자: 폭발 애니메이션의 위치와 이미지 경로를 초기화
    public ExplosionAnimation(int x, int y, String[] imagePaths) {
        this.x = x;  // x 좌표를 설정
        this.y = y;  // y 좌표를 설정
        this.explosionImages = new Image[imagePaths.length];  // 이미지 배열을 초기화
        for (int i = 0; i < imagePaths.length; i++) {  // 각 이미지 경로를 반복
            explosionImages[i] = new ImageIcon(imagePaths[i]).getImage(); // 이미지를 로드하여 배열에 저장
        }
        this.totalFrames = explosionImages.length; // 총 프레임 수를 설정
    }

    // 애니메이션이 완료되었는지 확인하는 메서드
    public boolean isCompleted() {
        return frame >= totalFrames; // 현재 프레임이 총 프레임 수 이상인지 확인
    }

    // 애니메이션을 그리는 메서드
    public void draw(Graphics g) {
        if (!isCompleted()) {  // 애니메이션이 완료되지 않았다면
            g.drawImage(explosionImages[frame], x, y, null); // 현재 프레임의 이미지를 그리기
            frame++;  // 다음 프레임으로 이동
        }
    }
}
