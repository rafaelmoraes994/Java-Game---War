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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Controller.ControladorJogadores;
import Model.Mensageiro;
import Model.UtilitariosJanelas;

public class JanelaOrdemJogadores extends JFrame {

	public final int LARG_DEFAULT=730;
	public final int ALT_DEFAULT = 150+70*(ControladorJogadores.getInstance().vPlayers.size()+1);
	int i, j;
	JPanel p = new JPanel();
	Border bblack = BorderFactory.createLineBorder(Color.black, 2);

	public JanelaOrdemJogadores(){

		add(p);

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

		//Label
		JLabel l = new JLabel();
		l.setText("Ordem dos Jogadores:");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(new Font("Courier", Font.BOLD, 20));
		l.setBounds(0, 50, 700, 30);
		p.add(l);
		l.setVisible(true);

		//Create Player Labels
		for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++)
		{
			int j = i+1;
			JLabel label = new JLabel();
			label.setOpaque(true);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setText("Player"+ " " + j + ":" + " " + ControladorJogadores.getInstance().vPlayers.get(i).getName());
			label.setBorder(bblack);
			label.setBackground(ControladorJogadores.getInstance().vPlayers.get(i).getColor());
			UtilitariosJanelas.verifyForegroundColor(label);
			label.setBounds(20, 50+70*(i+1), 580, 30);
			label.setFont(new Font("Courier", Font.BOLD, 20));
			label.setVisible(true);
			p.add(label);
		}
		
		//Goal Buttons
		JButton bObj = new JButton("Objetivo");
		for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
			if(ControladorJogadores.getInstance().vPlayers.get(i).getName().equals(ControladorJogadores.getInstance().currentCliente.player.getName())){
				break;
			}
		}
		bObj.setBounds(620, 50+70*(i+1), 80, 30);
		p.add(bObj);
		bObj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ControladorJogadores.getInstance().currentCliente.player.getObjetivo().isAlvoIncorreto() == true){
					JOptionPane.showMessageDialog(null, ControladorJogadores.getInstance().currentCliente.player.getObjetivo().getDescricao() + "\n" + 
							"Como seu objetivo é destruir você mesmo, seu novo objetivo agora sera: Conquistar 24 TERRITÓRIOS à sua escolha");
				}
				else{
					JOptionPane.showMessageDialog(null, ControladorJogadores.getInstance().currentCliente.player.getObjetivo().getDescricao() + "\n");
				}	
			}
		});	

		//Button OK
		JButton bOK = new JButton("OK");
		bOK.setBounds(630, 50+70*(ControladorJogadores.getInstance().vPlayers.size()+1), 60, 30);
		p.add(bOK);
		bOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				try {
					Mensageiro.enviaMensagem("inicializa territorios", ControladorJogadores.getInstance().currentCliente.cliente);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});	
	}
}
