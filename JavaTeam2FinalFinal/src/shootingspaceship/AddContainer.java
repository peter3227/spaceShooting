package shootingspaceship;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class AddContainer {
	SpaceWar war;
	public JLabel[] printLabel = new JLabel[2];
	public String[] lalist = {"Game Over", "Pause"}; //화면에 보여질 문자들의 배열
	
	public AddContainer(SpaceWar war) {
		this.war = war;
	}
    public void setLabel() { //화면에 표시할 JLabel 만들기
	    
	    for(int i=0; i<lalist.length; ++i) {
	    	printLabel[i]= new JLabel(lalist[i]); 
	    	war.add(printLabel[i]);
	    	printLabel[i].setVisible(false); 
	    	printLabel[i].setFont(new Font("Arial", Font.BOLD, 40));
	    	printLabel[i].setForeground(Color.WHITE);
	    	printLabel[i].setBounds(180,150,300,100); 
	    }
	    printLabel[0].setBounds(150,100,300,100); 
	    
	}
	public JLabel[] getPrintLabel() {
        return printLabel;
    }

}
