package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.ControladorDeFrames;
import Controller.ControladorDeJogo;
import Controller.ControladorJogadores;
import Model.Mensageiro;
import Model.Servidor;

public class JanelaTipoAplicacao extends JFrame {
	public final int LARG_DEFAULT=400;
	public final int ALT_DEFAULT=200;
	JButton bServidor = new JButton();
	JButton bCliente = new JButton();

	JPanel p = new JPanel();

	public JanelaTipoAplicacao(){

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

		//Servidor
		bServidor.setText("Servidor");
		bServidor.setOpaque(false);
		bServidor.setBounds(90, 30, 200, 30);
		bServidor.setHorizontalAlignment(SwingConstants.CENTER);
		p.add(bServidor);

		bServidor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				if(ControladorDeJogo.getInstance().servidorOn == true){
					JOptionPane f = new JOptionPane("Erro");
					f.setVisible(true);
					JOptionPane.showMessageDialog(f,"Servidor ja está conectado ");
				}
				else{
					JanelaSelecaoJogadores j;
					try {
						new Thread(Servidor.getInstance()).start();
						ControladorJogadores.getInstance().currentCliente  = createClient();
						j = new JanelaSelecaoJogadores();
						ControladorDeFrames.getInstance().mudarDeFrames(j);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				dispose();
			}
		});


		//Cliente
		bCliente.setText("Cliente");
		bCliente.setOpaque(false);
		bCliente.setBounds(90, 100, 200, 30);
		bCliente.setHorizontalAlignment(SwingConstants.CENTER);
		p.add(bCliente);


		bCliente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JanelaSelecaoJogadores j;
				try {
					ControladorJogadores.getInstance().currentCliente  = createClient();
					Mensageiro.enviaMensagem((String)"verifica vetor de players", ControladorJogadores.getInstance().currentCliente.cliente);
					j = new JanelaSelecaoJogadores();
					ControladorDeFrames.getInstance().mudarDeFrames(j);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	private Cliente createClient(){
		int i;
		Cliente c;
		c = new Cliente();
		c.setVisible(true);
		return c;

	}

}
