package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import Controller.ControladorDeFrames;
import Model.Servidor;

public class JanelaInicial extends JFrame {
	public final int LARG_DEFAULT=1040;
	public final int ALT_DEFAULT=800;
	JButton startGame = new JButton();
	JButton leaveGame = new JButton();
	
	PanelInicial p = new PanelInicial();

	public JanelaInicial(){
		
		//Frame
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		int sl=screenSize.width;
		int sa=screenSize.height;
		int x=sl/2-LARG_DEFAULT/2;
		int y=sa/2-ALT_DEFAULT/2;
		setBounds(x,y,LARG_DEFAULT,ALT_DEFAULT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Panel
		p.setLayout(null);
		add(p);
		
		//StartGame
		startGame.setText("Iniciar Jogo");
		startGame.setOpaque(false);
		startGame.setBounds(410, 530, 200, 30);
		startGame.setHorizontalAlignment(SwingConstants.CENTER);
		p.add(startGame);
		
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JanelaTipoAplicacao j;
				j = new JanelaTipoAplicacao();
				ControladorDeFrames.getInstance().mudarDeFrames(j);
			}
		});
		

		//LeaveGame
		leaveGame.setText("Sair");
		leaveGame.setOpaque(false);
		leaveGame.setBounds(410, 580, 200, 30);
		leaveGame.setHorizontalAlignment(SwingConstants.CENTER);
		p.add(leaveGame);
		
		
		leaveGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}

}
