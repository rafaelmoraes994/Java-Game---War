package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Controller.ControladorJogadores;
import Model.Mensageiro;

public class JanelaEsperaJogadores extends JFrame {
	public final int LARG_DEFAULT=730;
	public final int ALT_DEFAULT = 150+70*(ControladorJogadores.getInstance().vPlayers.size()+1);
	JPanel p = new JPanel();
	Border bblack = BorderFactory.createLineBorder(Color.black, 2);
	JButton bOK = new JButton("Ok");

	public JanelaEsperaJogadores() {

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

		//Label
		JLabel l = new JLabel();
		l.setText("Aguardando jogadores se conectarem:");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(new Font("Courier", Font.BOLD, 20));
		l.setBounds(0, 50, 700, 30);
		p.add(l);
		l.setVisible(true);


		//Button OK
		bOK.setBounds(630, 50+70*(ControladorJogadores.getInstance().vPlayers.size()+1), 60, 30);
		bOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Mensageiro.enviaMensagem("sorteia", ControladorJogadores.getInstance().currentCliente.cliente);
					Mensageiro.enviaMensagem("objetivos", ControladorJogadores.getInstance().currentCliente.cliente);
					Mensageiro.enviaMensagem("distribui territorios", ControladorJogadores.getInstance().currentCliente.cliente);
					Mensageiro.enviaMensagem("cartas", ControladorJogadores.getInstance().currentCliente.cliente);
					Mensageiro.enviaMensagem("muda frame OrdemJogadores", ControladorJogadores.getInstance().currentCliente.cliente);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		p.add(bOK);
		bOK.setVisible(false);

		new Thread(){
			public void run(){
				try {
					Mensageiro.enviaMensagem("verifica vetor de players", ControladorJogadores.getInstance().currentCliente.cliente);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void setButton(){
		bOK.setVisible(true);
	}

}


