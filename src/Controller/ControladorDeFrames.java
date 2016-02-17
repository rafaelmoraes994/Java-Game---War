package Controller;

import javax.swing.JFrame;

public class ControladorDeFrames {
	
	private JFrame atual;
	private static ControladorDeFrames instance;
	
	public ControladorDeFrames(){
		
	}
	
	public static ControladorDeFrames getInstance() {
		if (instance == null)
			instance = new ControladorDeFrames();
		return instance;
	}
	
	public void mudarDeFrames(JFrame f){
		atual.dispose();
		atual = f;
		atual.setVisible(true);
		
	}
	
	public void setAtual(JFrame f){
		this.atual = f;
	}
	
	public JFrame getAtual(){
		return atual;
	}
	
}
