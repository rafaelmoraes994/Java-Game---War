package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Controller.ControladorDeJogo;

public class JanelaDados extends JDialog{
	public int LARG_DEFAULT = 30+100*(ControladorDeJogo.getInstance().qtdDadosAtacante + ControladorDeJogo.getInstance().qtdDadosDefensor);
	public int ALT_DEFAULT = 420;
	int i, id = 0;
	Random randomizer = new Random();
	ArrayList<String> vDicesOrder = new ArrayList<String>(Arrays.asList("Primeiro Dado de Ataque","Segundo Dado de Ataque", "Terceiro Dado de Ataque", 
			"Primeiro Dado de Defesa", "Segundo Dado de Defesa","Terceiro Dado de Defesa"));
	ArrayList<BufferedImage> vPicAttackDices = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> vPicDefenseDices = new ArrayList<BufferedImage>();
	Vector<Integer> attackDices = new Vector<>();
	Vector<Integer> defenseDices = new Vector<>();
	JPanel dicePanel = new JPanel();
	JButton bOK = new JButton("OK");

	public JanelaDados(JFrame owner){

		super(owner, true);
		//Frame
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		int sl=screenSize.width;
		int sa=screenSize.height;
		int x=sl/2-LARG_DEFAULT/2;
		int y=sa/2-ALT_DEFAULT/2;
		setBounds(x,y,LARG_DEFAULT,ALT_DEFAULT);
		add(dicePanel);

		//Panel
		dicePanel.setLayout(null);

		// Insert Images
		for(i=0; i<12; i++){
			id++;
			if(i<6){
				try {                
					vPicAttackDices.add(ImageIO.read(new File("images/dados/dado_ataque_"+ id +".png")));
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
					System.exit(1);
				}	
			}
			else {
				if(id>6){
					id=1;
				}
				try {                
					vPicDefenseDices.add(ImageIO.read(new File("images/dados/dado_defesa_"+ id +".png")));
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
					System.exit(1);
				}
			}
		}

		//Labels
		for(i=0; i<ControladorDeJogo.getInstance().qtdDadosAtacante; i++){
			attackDices.add(randomizer.nextInt(vPicAttackDices.size()));
			JLabel diceAttackLabel = new JLabel(new ImageIcon(vPicAttackDices.get(attackDices.get(i))));
			dicePanel.add(diceAttackLabel);
			diceAttackLabel.setBounds(0+100*i, 80, 100, 140);
		}
		for(i=0; i<ControladorDeJogo.getInstance().qtdDadosDefensor; i++){
			defenseDices.add(randomizer.nextInt(vPicDefenseDices.size()));
			JLabel diceDefenseLabel = new JLabel(new ImageIcon(vPicDefenseDices.get(defenseDices.get(i))));
			dicePanel.add(diceDefenseLabel);
			diceDefenseLabel.setBounds(100*(ControladorDeJogo.getInstance().qtdDadosAtacante-1)+100*(i+1), 80, 100, 140);
		}
		JLabel attackLabel = new JLabel();
		attackLabel.setText("Dados de Ataque/Dados de Defesa");
		attackLabel.setBounds(0, 50, LARG_DEFAULT, 30);
		attackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dicePanel.add(attackLabel);

		Collections.sort(attackDices);
		Collections.reverse(attackDices);
		Collections.sort(defenseDices);
		Collections.reverse(defenseDices);

		int aLoose = 0;
		int dLoose = 0;
		int size = 0;
		if(attackDices.size() > defenseDices.size()){
			size = defenseDices.size();
		}
		else{
			size = attackDices.size();
		}
		for(i=0; i<size; i++){
			if(attackDices.get(i) > defenseDices.get(i)){
				dLoose++;
				ControladorDeJogo.getInstance().defensor.decrementContador();
			}
			else{
				aLoose++;
				ControladorDeJogo.getInstance().atacante.decrementContador();
			}
		}

		JTextArea resultLabel = new JTextArea();
		resultLabel.setText("O território atacante perdeu: " + aLoose + " exército(s)" + "\n" + 
		"O território defensor perdeu: " + dLoose + " exército(s)");
		resultLabel.setEditable(false);
		resultLabel.setLineWrap(true); 
		resultLabel.setBackground(new Color(0,0,0,0));
		resultLabel.setBounds(20, 230, LARG_DEFAULT-60, 50);
		dicePanel.add(resultLabel);


		//bOK
		bOK.setBounds(LARG_DEFAULT-100, 330, 60, 30);
		bOK.setVisible(true);
		dicePanel.add(bOK);

		bOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControladorDeJogo.getInstance().verificarEstado(null);
				dispose();
			}
		});


	}
}

