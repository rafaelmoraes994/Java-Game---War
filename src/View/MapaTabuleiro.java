package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Controller.ControladorDeFrames;
import Controller.ControladorDeJogo;
import Controller.ControladorDeTerritorios;
import Controller.ControladorDeJogo.Fase;
import Controller.ControladorJogadores;
import Model.Cartas;
import Model.EstadoDoJogo;
import Model.Mensageiro;
import Model.Mensagem;
import Model.Servidor;
import Model.Territorio;

public class MapaTabuleiro extends JFrame implements Observer, Serializable
{
	public final int LARG_DEFAULT=1040;
	public final int ALT_DEFAULT=800;
	int i;
	JLabel lPlayerColor = new JLabel();
	JLabel currentPlayer = new JLabel();
	JLabel lRodada = new JLabel();
	JLabel lFase = new JLabel();
	JLabel lDistrArmy = new JLabel();
	JButton rollDices = new JButton();
	JButton passTurn = new JButton();
	JButton passFase = new JButton();
	JButton bTrade = new JButton("Troca de Cartas");
	Random randomizer = new Random();
	Border bblack = BorderFactory.createLineBorder(Color.black, 2);
	MapPanel mapa;


	public MapaTabuleiro()
	{
		mapa = new MapPanel();
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		int sl=screenSize.width;
		int sa=screenSize.height;
		int x=sl/2-LARG_DEFAULT/2;
		int y=sa/2-ALT_DEFAULT/2;
		setBounds(x,y,LARG_DEFAULT,ALT_DEFAULT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(mapa);

		//Panel
		mapa.setLayout(null);

		//Label Player
		currentPlayer.setText("Player Atual:" + "   " + ControladorJogadores.getInstance().currentPlayer.getName());
		currentPlayer.setBounds(10, 10, 600, 21);
		currentPlayer.setForeground(Color.white);
		currentPlayer.setFont(new Font("Courier", Font.BOLD, 18));
		mapa.add(currentPlayer);

		//Label Rodada
		lRodada.setText("Rodada: " + ControladorDeJogo.getInstance().rodada);
		lRodada.setBounds(10, 31, 100, 21);
		lRodada.setForeground(Color.white);
		lRodada.setFont(new Font("Courier", Font.BOLD, 18));
		mapa.add(lRodada);

		//Label Fase
		lFase.setText("Fase: Distribuicao");
		lFase.setBounds(750, 10, 600, 21);
		lFase.setForeground(Color.white);
		lFase.setFont(new Font("Courier", Font.BOLD, 20));
		mapa.add(lFase);

		//Label Army Distribution
		lDistrArmy.setText("Exército(s) restantes: " + ControladorDeJogo.getInstance().distArmy);
		lDistrArmy.setBounds(750, 31, 600, 21);
		lDistrArmy.setForeground(Color.white);
		lDistrArmy.setFont(new Font("Courier", Font.BOLD, 17));
		mapa.add(lDistrArmy);

		//Label Player Color
		lPlayerColor.setOpaque(true);
		lPlayerColor.setBounds(155, 10, 20, 21);
		lPlayerColor.setBorder(bblack);
		lPlayerColor.setBackground(ControladorJogadores.getInstance().currentPlayer.getColor());
		mapa.add(lPlayerColor);

		//Card Button
		JButton bCarta = new JButton("Cartas");
		bCarta.setBounds(350, 10, 80, 30);
		mapa.add(bCarta);
		bCarta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JanelaCartas jCarta = new JanelaCartas(MapaTabuleiro.this);
				jCarta.setVisible(true);
			}
		});

		//Goal Button
		JButton bObj = new JButton("Objetivo");
		bObj.setBounds(430, 10, 80, 30);
		mapa.add(bObj);
		bObj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ControladorJogadores.getInstance().currentCliente.player.getObjetivo().isAlvoIncorreto() == true){
					JOptionPane.showMessageDialog(null, ControladorJogadores.getInstance().currentCliente.player.getObjetivo().getDescricao() + "\n" + 
							"Como seu objetivo é destruir você mesmo, seu novo objetivo agora sera: Conquistar 24 TERRITÓRIOS à sua escolha");
				}
				else{
					JOptionPane.showMessageDialog(null, ControladorJogadores.getInstance().currentCliente.player.getObjetivo().getDescricao());	
				}
			}
		});	

		//Trade Button
		mapa.add(bTrade);
		bTrade.setBounds(510, 10, 140, 30);
		bTrade.setVisible(false);
		bTrade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JanelaDeTrocas jTrocas = new JanelaDeTrocas(MapaTabuleiro.this);
				jTrocas.setVisible(true);
			}
		});	

		//PassTurn Button
		mapa.add(passTurn);
		passTurn.setText("Passar Vez");
		passTurn.setBorder(bblack);
		passTurn.setHorizontalAlignment(SwingConstants.CENTER);
		passTurn.setBounds(910, 690, 100, 40);
		passTurn.setVisible(false);
		passTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int idNovo;
				if(ControladorDeJogo.getInstance().ganhou == 1){
					if(ControladorJogadores.getInstance().currentCliente.player.playerCards.size() == 5){
						JanelaDeTrocas jTrocas = new JanelaDeTrocas(MapaTabuleiro.this);
						jTrocas.setVisible(true);
					}
					else{
						if(ControladorJogadores.getInstance().vCartas.size() == 0){
							ControladorJogadores.getInstance().vCartas = new ArrayList<Cartas>(ControladorJogadores.getInstance().vCartasUsadas);
							ControladorJogadores.getInstance().vCartasUsadas = new ArrayList<Cartas>();
						}
						int numero = randomizer.nextInt(ControladorJogadores.getInstance().vCartas.size());
						ControladorJogadores.getInstance().currentCliente.player.playerCards.add(ControladorJogadores.getInstance().vCartas.get(numero));
						ControladorJogadores.getInstance().vCartas.remove(numero);
						ControladorDeJogo.getInstance().ganhou = 0;
						JOptionPane.showMessageDialog(null, "Você ganhou uma carta de territorio");
					}	
				}
				ControladorJogadores.getInstance().currentCliente.player.getObjetivo().verificaObjetivo(ControladorJogadores.getInstance().currentCliente.player);
				verifyPlayerLost();
				ControladorDeJogo.getInstance().fases = Fase.Distribuicao;
				
				for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
					if(ControladorJogadores.getInstance().currentPlayer == ControladorJogadores.getInstance().vPlayers.get(i)){
						idNovo = i+1;
						if(idNovo == ControladorJogadores.getInstance().vPlayers.size()){
							idNovo = 0;
							ControladorDeJogo.getInstance().rodada++;
						}
						if(ControladorDeJogo.getInstance().rodada > 1){
							bTrade.setVisible(true);
						}
						
						ControladorJogadores.getInstance().currentPlayer = ControladorJogadores.getInstance().vPlayers.get(idNovo);
						lFase.setText("Fase: " + ControladorDeJogo.getInstance().fases);
						setPassTurnVisible(false);
						
						//Salva Jogo
						EstadoDoJogo estadoJogo = new EstadoDoJogo();
						try {
							Mensageiro.enviaMensagem(estadoJogo, ControladorJogadores.getInstance().currentCliente.cliente);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						break;
					}
				}
			}
		});

		//PassFase Button
		mapa.add(passFase);
		passFase.setText("Passar Fase");
		passFase.setBorder(bblack);
		passFase.setHorizontalAlignment(SwingConstants.CENTER);
		passFase.setBounds(910, 690, 100, 40);
		passFase.setVisible(false);
		passFase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDiceVisible(false);
				setPassFaseVisible(false);
				setTradeCardVisible(false);
				ControladorJogadores.getInstance().currentCliente.player.getObjetivo().verificaObjetivo(ControladorJogadores.getInstance().currentCliente.player);
				ControladorDeJogo.getInstance().fases = Fase.Redistribuicao;
				JOptionPane.showMessageDialog(null, "Agora comeca a fase de redistribuicao");
				lFase.setText("Fase: " + ControladorDeJogo.getInstance().fases);
				if(ControladorDeJogo.getInstance().fases != Fase.Distribuicao){
					setArmyLabelVisible(false);
				}
				ControladorDeJogo.getInstance().distribuidor = null;
				ControladorDeJogo.getInstance().recebedor = null;
				setPassTurnVisible(true);
			}
		});


		//Attack
		rollDices.setText("Atacar");
		rollDices.setBorder(bblack);
		rollDices.setHorizontalAlignment(SwingConstants.CENTER);
		rollDices.setBounds(15, 690, 100, 40);
		rollDices.setVisible(false);
		mapa.add(rollDices);
		rollDices.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean signal = false;
				while(signal == false){
					String s;
					try{
						s = JOptionPane.showInputDialog(null, "Quantos exército(s) gostaria de utilizar no ataque?");
						if(s != null){
							if(Integer.parseInt(s) > 3){
								JOptionPane.showMessageDialog(null, "Só é possivel utilizar no máximo 3 exercito(s) por ataque");
								continue;
							}
							else if(Integer.parseInt(s) == ControladorDeJogo.getInstance().atacante.getQtdExercito()){
								JOptionPane.showMessageDialog(null, "Não é possivel utilizar todo o exército de um território" + "\n"+ "Deve-se deixar pelo menos 1 exercito no territorio");
							}
							else if(Integer.parseInt(s) == 0){
								JOptionPane.showMessageDialog(null, "É necessário utilizar no minimo 1 exército no ataque");
							}
							else{
								signal = true;
								ControladorDeJogo.getInstance().qtdDadosAtacante = Integer.parseInt(s);
								if(ControladorDeJogo.getInstance().defensor.getQtdExercito() >= 3){
									ControladorDeJogo.getInstance().qtdDadosDefensor = 3;
								}
								else{
									ControladorDeJogo.getInstance().qtdDadosDefensor = ControladorDeJogo.getInstance().defensor.getQtdExercito();
								}
							}
						}
						else{
							return;
						}

					}catch (Exception ex){
						JOptionPane.showMessageDialog(null, "Utilize apenas números");
					}
				}

				JanelaDados jDados = new JanelaDados(MapaTabuleiro.this);
				jDados.setVisible(true);
				setDiceVisible(false);
				ControladorDeJogo.getInstance().atacante = null;
				ControladorDeJogo.getInstance().defensor = null;
				ControladorJogadores.getInstance().currentCliente.player.getObjetivo().verificaObjetivo(ControladorJogadores.getInstance().currentCliente.player);
			}
		});

		ControladorDeJogo.getInstance().addObserver(this);
	}

	protected void atualizaTerritorios(){
		for(i=0; i<mapa.getComponents().length; i++){
			if(mapa.getComponent(i) instanceof JLabel){
				if(mapa.getComponent(i).getName() != null){
					if(mapa.getComponent(i).getName().equals("territorio")){
						mapa.remove(mapa.getComponent(i));
						i--;	
					}

				}
			}
		}

		for(i=0; i<ControladorDeTerritorios.getInstance().lstTerritorios.size(); i++){
			mapa.add(ControladorDeTerritorios.getInstance().lstTerritorios.get(i).getContador());
			mapa.repaint();
		}
	}

	protected void atualizaTabuleiro(){
		currentPlayer.setText("Player Atual:" + "   " + ControladorJogadores.getInstance().currentPlayer.getName());
		lPlayerColor.setBackground(ControladorJogadores.getInstance().currentPlayer.getColor());
		lRodada.setText("Rodada: " + ControladorDeJogo.getInstance().rodada);
		JOptionPane pNextTurn = new JOptionPane("Proximo Jogador");
		pNextTurn.setVisible(true);
		JOptionPane.showMessageDialog(pNextTurn, "Jogador:" + " " + ControladorJogadores.getInstance().currentPlayer.getName() + "\n" 
				+ "Cor:" + " " + ControladorJogadores.getInstance().currentPlayer.getColorName());
		
		if(ControladorJogadores.getInstance().currentPlayer.getName().equals(ControladorJogadores.getInstance().currentCliente.player.getName())){
			ControladorDeJogo.getInstance().distArmy = ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size()/2;
			if(ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size() <= 6){
				ControladorDeJogo.getInstance().distArmy = 3;
			}
			ControladorDeJogo.getInstance().verificaBonus();
			lDistrArmy.setText("Exército(s) restantes: " + ControladorDeJogo.getInstance().distArmy);
			setArmyLabelVisible(true);
			if(ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size() == 1){
				setPassFaseVisible(true);
			}
		}

		atualizaTerritorios();
	}

	protected void setDiceVisible(boolean visible){
		rollDices.setVisible(visible);
	}

	protected void setPassFaseVisible(boolean visible){
		passFase.setVisible(visible);
	}

	protected void setPassTurnVisible(boolean visible){
		passTurn.setVisible(visible);
	}

	protected void setArmyLabelVisible(boolean visible){
		lDistrArmy.setVisible(visible);
	}

	protected void setTradeCardVisible(boolean visible){
		bTrade.setVisible(visible);
	}

	protected void verifyPlayerLost(){
		int i;
		for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
			if(ControladorJogadores.getInstance().vPlayers.get(i).playerTerritories.size() == 0){
				ControladorJogadores.getInstance().vPlayers.remove(i);
			}
		}
	}

	protected void derrotaJogador(Territorio a, Territorio d){
		int i;
		for(i=0; i<d.getPlayerId().playerCards.size(); i++){
			a.getPlayerId().playerCards.add(d.getPlayerId().playerCards.get(i));
			d.getPlayerId().playerCards.remove(i);
			i--;
		}
		while(a.getPlayerId().playerCards.size() > 5){
			JanelaDeTrocas jTrocas = new JanelaDeTrocas(MapaTabuleiro.this);
			jTrocas.setVisible(true);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		switch(ControladorDeJogo.getInstance().fases){
		case Inicial:
			lFase.setText("Fase: " + ControladorDeJogo.getInstance().fases);
			break;
		case Distribuicao:
			lDistrArmy.setText("Exército(s) restantes: " + ControladorDeJogo.getInstance().distArmy);
			if(ControladorDeJogo.getInstance().distArmy == 0 && ControladorDeJogo.getInstance().rodada == 1){
				setPassTurnVisible(true);
			}
			else if(ControladorDeJogo.getInstance().distArmy == 0 && ControladorDeJogo.getInstance().rodada != 1){
				ControladorDeJogo.getInstance().fases = Fase.Ataque;
				JOptionPane.showMessageDialog(null, "Agora comeca a fase de ataque");
				lFase.setText("Fase: " + ControladorDeJogo.getInstance().fases);
				setArmyLabelVisible(false);
				setPassFaseVisible(true);
				setTradeCardVisible(false);
				ControladorDeJogo.getInstance().atacante = null;
				ControladorDeJogo.getInstance().defensor = null;	
			}
			break;
		case Ataque:
			if(arg == null){
				derrotaJogador(ControladorDeJogo.getInstance().atacante, ControladorDeJogo.getInstance().defensor);
			}
			else{
				setDiceVisible((boolean) arg);
			}
			break;
		case Redistribuicao:
			break;
		}


	}

}

