package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Controller.ControladorJogadores;
import Model.Mensageiro;
import Model.Player;
import Model.UtilitariosJanelas;



public class JanelaSelecaoJogadores extends JFrame {
	public final int LARG_DEFAULT=1040;
	public final int ALT_DEFAULT=300;
	int i;

	JPanel playersPanel = new JPanel ();

	//Colors
	ArrayList<Color> vColors = new ArrayList<Color>(Arrays.asList(new Color(255,0,0), new Color(0,0,255), new Color(0,255,0), 
			Color.yellow, new Color(0,0,0), Color.white, new Color(255,128,0), new Color(255,0,255), new Color(128,64,0)));
	//ColorNames
	ArrayList<String> vColorNames = new ArrayList<String>(Arrays.asList("Vermelho","Azul", "Verde", "Amarelo",
			"Preto", "Branco", "Laranja", "Rosa", "Marrom"));
	//Jogadores
	ArrayList<Player> vAux = new ArrayList<Player>();

	//Borders
	Border bblack = BorderFactory.createLineBorder(Color.black, 2);

	//Label
	JLabel playerColor = new JLabel();

	//PlayerName
	JTextField playerName = new JTextField();

	public JanelaSelecaoJogadores() throws IOException {

		//Buttons
		JButton bOK = new JButton("OK");

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
		playersPanel.setLayout(null);
		add(playersPanel);	

		//SelecPlayer
		JLabel title = new JLabel("Escolha seu nome");
		title.setFont(new Font("TimesRoman", Font.BOLD, 60)); 
		title.setBounds(270, 0, 1000, 100);
		playersPanel.add(title);

		//Label
		playerColor.setOpaque(true);
		playerColor.setHorizontalAlignment(SwingConstants.CENTER);
		playerColor.setBounds(490, 125, 415, 50);
		playerColor.setBackground(vColors.get(0));
		playerColor.setText(vColorNames.get(0));
		UtilitariosJanelas.verifyForegroundColor(playerColor);
		playerColor.setBorder(bblack);
		playerColor.setVisible(true);
		playersPanel.add(playerColor);

		final JLabel label = playerColor;
		JButton leftButton = new JButton("<");
		leftButton.setBounds(410, 130, 45, 40);
		playersPanel.add(leftButton);

		leftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeColor(label, -1);
			}
		});

		JButton rightButton = new JButton(">");
		rightButton.setBounds(940, 130, 45, 40);
		playersPanel.add(rightButton);

		rightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeColor(label, 1);
			}
		});

		playerName.setBounds(45, 125, 300, 50);
		playersPanel.add(playerName);


		//Button OK
		bOK.setBounds(930, 200, 60, 30);
		playersPanel.add(bOK);
		bOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Player p = new Player(playerName.getText(), playerColor.getBackground(), playerColor.getText(), null);
				try {
					Mensageiro.enviaMensagem(p, ControladorJogadores.getInstance().currentCliente.cliente);
				} catch (IOException e1) {
					System.out.println("Aqui7");
					e1.printStackTrace();
				}
			}

		});

	}

	private void changeColor(JLabel label, int direction){
		int indCor = 0;
		for(i=0; i<vColors.size(); i++){
			if(label.getBackground() == vColors.get(i))
			{
				indCor = i;
				break;
			}
		}
		int nextColor=indCor+direction;
		while(true)
		{
			if(nextColor == vColors.size())
			{
				nextColor=0;
			}
			else if(nextColor < 0)
			{
				nextColor=vColors.size()-1;
			}
			boolean found = false;
			for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++)
			{
				if(ControladorJogadores.getInstance().vPlayers.get(i).getColor() == vColors.get(nextColor)){
					found = true;
					break;
				}
			}
			if(found){
				nextColor+=direction;
			} else {
				label.setBackground(vColors.get(nextColor));
				label.setText(vColorNames.get(nextColor));
				UtilitariosJanelas.verifyForegroundColor(label);
				break;
			}	
		}
	}

}