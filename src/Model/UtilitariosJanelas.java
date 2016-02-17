package Model;

import java.awt.Color;

import javax.swing.JLabel;

public class UtilitariosJanelas {
	
	public UtilitariosJanelas(){
		
	}
	
	public static void verifyForegroundColor(JLabel label)
	{
		if(label.getBackground()==Color.white || label.getBackground()==Color.yellow){
			label.setForeground(Color.black);
		} else {
			label.setForeground(Color.white);
		}
		
	}

}
