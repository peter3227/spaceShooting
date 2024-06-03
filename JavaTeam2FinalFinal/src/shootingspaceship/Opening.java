package shootingspaceship;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import java.util.Timer;
import java.util.TimerTask;

public class Opening extends JPanel{
	public static ArrayList<String> sentence = new ArrayList<String>();
    static JLabel text = new JLabel();
    JPanel Option = new JPanel();
    JLabel Character = new JLabel();
    static JButton next =new JButton("Next");
    private Timer timer =new Timer();
    private StartScreen s9n = new StartScreen();
    JButton create = new JButton("generate");
    String[]character  = {"name:", "spacecraft name:"}; 
    ArrayList<JLabel> name = new ArrayList<>();
    ArrayList<JTextField> Input_n = new ArrayList<>();
    String player = new String();
    String ship = new String();
    public JButton startButton;
    public JPanel screen;
    public JPanel mainScreen;
    public static int grade;
    JFrame frame;
    
    public Opening(JFrame frame) {
    	
    	this.frame = frame;
    	
    	setBackground(Color.BLACK); //frame 배경
    	setBounds(0,0,500,500); //화면의 크기
    	setLayout(null);  //컴포넌크의 위치와 크기 직접 지정
        set(); //frame에 들어갈 요소 초기화
        setSize(500, 500); //크기
        setVisible(true); 
        addKeyListener(new key()); //key 입력
        textList(); //오프닝 문장
        Thread td = new TimerThread(sentence.get(0),70,text,sentence); //타이핑 효과
        td.start();
        next.addActionListener(new ActionListener() { //오프닝의 next 버튼, 오프닝에서 시작 화면으로 넘기기
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel(); //메소드 호출
			}
        	
        });
        
    }
    
    public void set() { //화면에 들어갈 컴포넌트 생성
    	text.setBounds(50,50,400,300); //오프닝 문장 크기와 위치
        text.setForeground(new Color(250, 250, 250)); //글자 색
        text.setHorizontalAlignment(JLabel.CENTER); //텍스트의 중앙 정렬
        add(text,BorderLayout.CENTER); //창 중앙에 텍스트 넣기
        setFocusable(true);
        
        next.setBounds(200, 400, 100, 50); //next 버튼 위치, 크기
        add(next);
        next.setVisible(false);
        
        Option.setBackground(Color.WHITE); //캐릭터 생성 창의 배경 색
        Option.setBounds(75,75,350,350); //크기와 위치
        Option.setLayout(null); 
        
        for(int i=0; i<2; ++i) { //생성창에 들어갈 Label 생성
        	JLabel ch = new JLabel(character[i]);
        	name.add(ch);
        	name.get(i).setForeground(new Color(0, 0, 0));
        	name.get(i).setSize(150,150);
            Option.add(name.get(i));
        }

        name.get(0).setLocation(50,50); //각 Label의 위치를 정함
        
        name.get(1).setLocation(50,150);
        
        create.setLocation(150,300); //생성버튼
        create.setSize(100,30);
        create.setEnabled(false); //컴포넌트 비활성화
        Option.add(create);
        
        TextListener tListen = new TextListener();
        for(int i=0; i<2; ++i) { //입력창 생성
        	JTextField Input = new JTextField("");
        	Input_n.add(Input);
        	Input_n.get(i).setSize(100,50);
        	Input_n.get(i).setForeground(new Color(0, 0, 0));
        	Option.add(Input_n.get(i));
        	Input_n.get(i).addActionListener(tListen);
        }
        Input_n.get(0).setLocation(150,100); //입력창의 위치를 정함
        Input_n.get(1).setLocation(180,200);
        
        Option.setVisible(false);
        
    }
    
    private void textList() { //오프닝 문장
    	sentence.add("<html>3324년 O월 O일<br>"
                + " 드디어 인류가 살아갈 수 있는 행성을 찾았다.<br>"
                + " 우리는 그곳을 인공지능을 사용하여 유지하였다.<br>"
                + " 우리는 그 행성을 '낙원'이라 불렀다.</html>");;
        sentence.add("<html>3326년 @월 @일<br>"
                + " 이틀 전 '낙원'으로부터 사진 하나가 왔다.<br>"
                + " 모든 것이 붉은 사진이었다.<br>"
                + " 이게 뭐지?</html>");
        sentence.add("<html>3327년 O월 O일<br>"
                + " 여기도 더 이상 안전하지 않다.<br>"
                + " 처음엔 소란스러웠지만,<br>"
                + " 지금은 기계 소리와 동료가 떠는 소리만 들린다.</html>");
        sentence.add("<html>3327년 @월 %일<br>"
        		+ " 사방이 조용하다.<br>"
    			+ " 아직도 기계 소리가 들린다.<br>"
        		+ " 더이상 먹을 수 있는 것이 남지 않았다.<br>");
        sentence.add("<html><font color='red'>죽기 싫어 죽기 싫어 죽기 싫어 죽기 싫어 죽기 싫어<br>"
        	    + " 죽기 싫어 죽기 싫어 죽기 싫어 죽기 싫어 죽기 싫어<br>"
        	    + " 죽기 싫어 죽기 싫어 죽기 싫어 죽기 싫어 죽기 싫어</font></html>");
    }
    
    public static synchronized void nextText(String s7e, int delay, JLabel jl, ArrayList<String> set) {
        int index = set.indexOf(s7e);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    Thread td = new TimerThread(set.get(index + 1), (index == 4) ? 10 : 70, jl, set);
                    td.start();
                });
            }
        }, delay);
    }
    
    private void mainPanel() { //시작 화면
    	timer.cancel();
    	setVisible(false);
    	frame.add(screen);
    	screen.setBounds(0,0,500,500);
    	screen.setVisible(true);
    	startButton.setVisible(true); 
    	screen.requestFocusInWindow();
    	
    }
    
    public void gamestart() { //게임 시작
    	
        removeAll();
        SpaceBackground spaceBackground = new SpaceBackground();
        frame.getContentPane().add(spaceBackground);
        SpaceWar war = new SpaceWar(frame);
        war.createPlayerSelectionButtons(frame);
        spaceBackground.add(war);
    }
    
    class key extends KeyAdapter{
    	public void keyPressed(KeyEvent e) {
    		switch(e.getKeyCode()) {
    		case KeyEvent.VK_SPACE: //SPACE를 누르면 오프닝 스킵
    			if (!Option.isVisible()) {
                    mainPanel();
                }
    	    	break;
    		case KeyEvent.VK_ENTER: //ENTER를 누르면 spacecraft name의 입력에 Focus를 맞춤
    			Input_n.get(1).requestFocus();
    	    	break;
    		}
    	}
    }
    
    public void OptionPanel() {//캐릭터 생성창
    	screen.add(Option);
    	startButton.setVisible(false);
		Option.setVisible(true);
		Option.requestFocusInWindow();
		Input_n.get(0).requestFocus(); //생성창이 나오면 name에 Focus를 맞춤
    	Input_n.get(0).addKeyListener(new key());
	
		create.addActionListener(new ActionListener() { //생성 버튼을 누르면 입력한 값들이 저장
			public void actionPerformed(ActionEvent e) {
				player =Input_n.get(0).getText();
				ship = Input_n.get(1).getText();
				screen.setVisible(false);			
				Option.setVisible(false);
				gamestart();
			}
		});
		
    }
    
    class StartScreen extends JPanel { //시작 화면
    	private ImageIcon normal = new ImageIcon("src/shootingspaceship/1.jpg"); //그냥
    	private ImageIcon press = new ImageIcon("src/shootingspaceship/3.jpg"); //눌렀을 때
    	private ImageIcon roll = new ImageIcon("src/shootingspaceship/2.jpg"); //버튼 위에 마우스가 올라갔을 때
        private Image startImage = new ImageIcon("src/shootingspaceship/startScreen.jpg").getImage(); //배경
    	public StartScreen(){
    		
    		screen = new JPanel() {
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g);
    	    		g.drawImage(startImage, 0,0, 500, 500, this); //그려질 이미지, 위치와 크기, 현재 패널에 이미지를 넣음
    	    		
    	    	}
    		};
    		
    		screen.setLayout(null);
    		setOpaque(false);//패널의 배경을 투명하게 설정
    		startButton =new JButton(normal);
    		startButton.setPressedIcon(press);
    		startButton.setRolloverIcon(roll);
    		startButton.setBounds(170, 170, 150,100);//버튼의 크기와 위치 설정
    		screen.add(startButton);
    		
    		startButton.setVisible(false);
    		startButton.addActionListener(new ActionListener() {//Start 버튼을 누르면 캐릭터 생성 창이 뜸
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				OptionPanel();
 
    			}
            	
            });
    	}
    }
    
    class TextListener implements ActionListener{ //입력받은 문장에 대한 이벤트 처리
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			String IN = Input_n.get(0).getText();
		    String IS = Input_n.get(1).getText();
		    if(IN.length()>= 5 && IS.length() >=5) {//7글자 이상 입력할 때만 버튼 클릭 가능
	    		create.setEnabled(true);
	    		}
		}
    	
    }

}

class TimerThread extends Thread{
	private JLabel timerLabel;
	private int delay;
	private String timerString;
	private ArrayList<String> stringList = new ArrayList<>();
	public TimerThread(String timerString, int delay, JLabel timerLabel, ArrayList<String> stringList) {
		this.timerLabel = timerLabel;
		this.delay = delay;
		this.timerString = timerString;
		this.stringList =stringList;
	}
	
	@Override
    public void run() {
        printString();
    }
	
	public void printString() {
		StringBuffer bf = new StringBuffer("");
		for(int i=0; i<timerString.length(); ++i) {
			bf.append(timerString.charAt(i));
			String temp = bf.toString();
			timerLabel.setText(temp);
			
			try {
				Thread.sleep(delay);
			}
			catch(InterruptedException e){
				return;
			}
			
		}
		
		int n = stringList.size()-1; 
		if(stringList.indexOf(timerString) < n) {//
			Opening.nextText(timerString, delay,timerLabel, stringList);
		}
		else {
			Opening.next.setVisible(true); //남은 문장이 없을 때 버튼이 보이게 함
		}
	}
}