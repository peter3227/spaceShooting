package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class SpaceWar extends Shootingspaceship {
    
    // Constructor
    public SpaceWar() {
        super();
        setOpaque(false);
    }
    
    // Main method to run the SpaceWar game
    public static void main(String[] args) {
    	 JFrame frame = new JFrame("Shooting");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로그램 종료 설정
         SpaceBackground spaceBackground = new SpaceBackground(); //추가
         frame.getContentPane().add(spaceBackground); //추가
         SpaceWar war = new SpaceWar(); // Shootingspaceship 객체 생성
         spaceBackground.add(war); // 프레임에 패널 추가   변경
         frame.pack(); // 컴포넌트 크기에 맞게 프레임 크기 조정
         frame.setVisible(true); // 프레임 표시
         war.start(); // 게임 시작
    }
}

