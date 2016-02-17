package View;

import Controller.ControladorDeFrames;


public class MainSelecaoJogadores {
	public static void main(String[] args) 
	{
		JanelaInicial t = new JanelaInicial();
		ControladorDeFrames.getInstance().setAtual(t);
		t.setVisible(true);
	}

}
