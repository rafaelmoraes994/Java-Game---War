package View;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Controller.ControladorDeCartas;
import Controller.ControladorDeFrames;
import Controller.ControladorDeJogo;
import Controller.ControladorDeTerritorios;
import Controller.ControladorJogadores;
import Model.EstadoDoJogo;
import Model.Mensageiro;
import Model.Mensagem;
import Model.Player;
import Model.Territorio;

public class Cliente extends JFrame implements Runnable{

	static int portNumber = 12348;
	static String host = "192.168.1.12";
	static BufferedReader teclado = null;
	static ObjectInputStream clientInput= null;
	public final int LARG_DEFAULT=590;
	public final int ALT_DEFAULT=530;
	public Player player;
	public Socket cliente;
	Mensagem mensagem;
	JScrollPane scrollPanel = new JScrollPane();
	JTextArea textArea = new JTextArea();
	JTextField textField = new JTextField("", 20);

	public Cliente() {

		try {
			cliente = new Socket(host, portNumber);
			teclado = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e) {
			System.out.println("AQUI1");
			e.printStackTrace();
		}


		new Thread(this).start();

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
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(null);

		//TextArea
		textArea.setColumns(20);
		textArea.setRows(5);
		textArea.setEditable(false);

		//ScrollPane
		scrollPanel.setBounds(15, 10, 545, 400);
		panel.add(scrollPanel);
		scrollPanel.setViewportView(textArea);

		//TextField
		textField.setBounds(15, 430, 350, 50);
		panel.add(textField);
		textField.setVisible(true);

		//Button
		JButton send = new JButton("Send");
		send.setBounds(400, 430, 150, 50);
		panel.add(send);
		send.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					mensagem = new Mensagem(textField.getText()+"\n", player.getName());
					Mensageiro.enviaMensagem(mensagem , cliente);
					textField.setText("");
				} catch (Exception ex) {
					System.out.println("AQUI4");
					System.out.println(ex);
				}
			}
		});

	}

	public void escreveMsg(String msg){
		textArea.append(msg +"\n");
	}

	public void setPlayer(Player player){
		this.player = player;
	}

	//thread que lê do servidor
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			clientInput = new ObjectInputStream(cliente.getInputStream());
			while(true){
				Object objInput = clientInput.readObject();
				if(objInput instanceof Player){
					player = (Player)objInput;
					ControladorDeFrames.getInstance().getAtual().dispose();
					JanelaEsperaJogadores j = new JanelaEsperaJogadores();
					ControladorDeFrames.getInstance().mudarDeFrames(j);
				}
				else if(objInput instanceof Mensagem){				
					int i;
					Mensagem msg = (Mensagem)objInput;					
					ControladorJogadores.getInstance().vPlayers = (ArrayList<Player>)msg.getObject();

					//AtualizaCurrentPlayer
					for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
						if(ControladorJogadores.getInstance().vPlayers.get(i).getName().equals(ControladorJogadores.getInstance().currentCliente.player.getName())){
							ControladorJogadores.getInstance().currentCliente.player = ControladorJogadores.getInstance().vPlayers.get(i);
						}
					}
					ControladorJogadores.getInstance().currentPlayer = ControladorJogadores.getInstance().vPlayers.get(0);
					if(msg.getMsg().equals("inicializa territorios")){
						ControladorDeTerritorios.getInstance().lstTerritorios = (ArrayList<Territorio>)msg.getObject2();
						MapaTabuleiro m = new MapaTabuleiro();
						for(i=0; i<ControladorDeTerritorios.getInstance().lstTerritorios.size(); i++){
							m.mapa.add(ControladorDeTerritorios.getInstance().lstTerritorios.get(i).getContador());
						}
						new Thread(){
							public void run(){
								ControladorDeJogo.getInstance().verificarEstado(null);
							}
						}.start();
						ControladorDeFrames.getInstance().mudarDeFrames(m);
					}
					else if(msg.getMsg().equals("distribui territorios")){
						ControladorDeTerritorios.getInstance().lstTerritorios = (ArrayList<Territorio>)msg.getObject2();
					}
				}
				else if(objInput instanceof EstadoDoJogo){
					EstadoDoJogo novoEstado = (EstadoDoJogo)objInput;
					novoEstado.carregaJogo();
					((MapaTabuleiro)ControladorDeFrames.getInstance().getAtual()).atualizaTabuleiro();
				}
				else{
					String str = (String)objInput;
					if(str.equals("cheio") || str.equals("close")){
						break;
					}
					else if(str.equals("cartas")){
						ControladorDeCartas.getInstance();
					}
					else if(str.equals("cor repetida")){
						JOptionPane f = new JOptionPane("Erro");
						f.setVisible(true);
						JOptionPane.showMessageDialog(f,"Ja existe jogador com esta cor");
					}
					else if(str.equals("muda frame OrdemJogadores")){
						JanelaOrdemJogadores j = new JanelaOrdemJogadores();
						ControladorDeFrames.getInstance().mudarDeFrames(j);
					}
					else if(str.equals("pronto para jogar") && ControladorDeFrames.getInstance().getAtual() instanceof JanelaEsperaJogadores){
						((JanelaEsperaJogadores)ControladorDeFrames.getInstance().getAtual()).setButton();
					}
					else{
						escreveMsg(str);	
					}
				}
			}
			clientInput.close();
			cliente.close();
			System.exit(1);

		} catch (IOException | ClassNotFoundException e) {
			System.out.println("AQUI5");
			System.err.println("IOException:  " + e);
		}
	}

}
