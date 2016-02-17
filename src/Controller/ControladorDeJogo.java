package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

import javax.swing.JOptionPane;

import Model.Continente;
import Model.Objetivo;
import Model.Territorio;

public class ControladorDeJogo extends Observable{

	private static ControladorDeJogo instance;

	public enum Fase{
		Inicial,
		Distribuicao,
		Ataque,
		Redistribuicao
	}

	public int rodada = 1;
	public int distArmy;
	public Fase fases = Fase.Inicial;
	public Territorio atacante;
	public Territorio defensor;
	public Territorio distribuidor;
	public Territorio recebedor;
	public int qtdDadosAtacante = 0;
	public int qtdDadosDefensor = 0;
	public int ganhou = 0;
	public int troca = 1;
	public int exercitosTroca = 4;
	public boolean servidorOn = false;
	public ArrayList<Objetivo> vObjetivos = new ArrayList<Objetivo>();

	private ControladorDeJogo() {

	}

	public static ControladorDeJogo getInstance() {
		if (instance == null)
			instance = new ControladorDeJogo();
		return instance;
	}

	public void verificarEstado(Territorio t){
		switch(fases){
		case Inicial:
			if(!(ControladorJogadores.getInstance().currentPlayer.getName().equals(ControladorJogadores.getInstance().currentCliente.player.getName()))){
				JOptionPane.showMessageDialog(null, "Esta na vez do(a): " + ControladorJogadores.getInstance().currentPlayer.getName());
			}
			else{
				distArmy = ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size()/2;
				verificaBonus();
				JOptionPane.showMessageDialog(null, "Número de territorios: " + ControladorJogadores.getInstance().currentPlayer.playerTerritories.size() + "\n" + 
						"Você possui " + ControladorDeJogo.getInstance().distArmy + " exército(s) para serem distribuidos");
				fases = Fase.Distribuicao;
				setChanged();
				notifyObservers();
			}
			
			break;
		case Distribuicao:
			if(!(ControladorJogadores.getInstance().currentPlayer.getName().equals(ControladorJogadores.getInstance().currentCliente.player.getName()))){
				JOptionPane.showMessageDialog(null, "Esta na vez do(a): " + ControladorJogadores.getInstance().currentPlayer.getName());
			}
			else{
				ControladorJogadores.getInstance().currentCliente.player.getObjetivo().verificaObjetivo(ControladorJogadores.getInstance().currentCliente.player);
				distributionFase(t);
			}
			break;
		case Ataque:
			attackFase(t);
			break;
		case Redistribuicao:
			organizeFase(t);
			break;
		}
	}

	private void distributionFase(Territorio t){	
		if(ControladorJogadores.getInstance().currentCliente.player.getName().equals(t.getPlayerId().getName())){
			if(distArmy == 0){
				JOptionPane.showMessageDialog(null, "Você não possui mais exército(s) para serem distribuidos");
			}
			else{
				t.incrementContador();
				distArmy--;
				setChanged();
				notifyObservers();
			}	
		}
		else{
			JOptionPane.showMessageDialog(null, "Este pais não te pertence");
		}
	}

	private void attackFase(Territorio t){
		int encontrou = 0;
		String s;
		if(t != null){
			if(ControladorJogadores.getInstance().currentCliente.player.getName().equals(t.getPlayerId().getName())){
				if(atacante != null){
					if(atacante == t){
						return;
					}
					if(JOptionPane.showConfirmDialog(null, "Gostaria de trocar de pais atacante?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
						if(t.getQtdExercito() == 1){
							JOptionPane.showMessageDialog(null, "Somente territorios com 2 ou mais exercitos podem atacar");
						}
						else{
							atacante = t;
							defensor = null;
							setChanged();
							notifyObservers(false);
							JOptionPane.showMessageDialog(null, "Atacante: " + atacante.getNome());
						}
					}
				}
				else if(t.getQtdExercito() == 1){
					JOptionPane.showMessageDialog(null, "Somente territorios com 2 ou mais exército(s) podem atacar");
				}
				else{
					atacante = t;
					JOptionPane.showMessageDialog(null, "Atacante: " + atacante.getNome());
				}

			}
			else if(atacante == null){
				JOptionPane.showMessageDialog(null, "Você não possui este territorio");
			}
			else{
				int i;
				for(i=0; i<atacante.getFronteiras().size(); i++){
					if(t == atacante.getFronteiras().get(i)){
						if(defensor == t){
							return;
						}
						if(defensor != null){
							if(JOptionPane.showConfirmDialog(null, "Gostaria de trocar de pais defensor?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
								defensor = t;
								encontrou = 1;
								setChanged();
								notifyObservers(true);
								JOptionPane.showMessageDialog(null, "Defensor: " + defensor.getNome());
								break;
							}
							else{
								encontrou = 1;
							}
						}
						else{
							defensor = t;
							encontrou = 1;
							setChanged();
							notifyObservers(true);
							JOptionPane.showMessageDialog(null, "Defensor: " + defensor.getNome());
							break;
						}
					}
				}
				if(defensor == null || defensor != null && encontrou == 0){
					JOptionPane.showMessageDialog(null, "Esse pais não faz fronteira com: " + atacante.getNome());
				}
			}
		}
		else{
			boolean signal = false;
			if(defensor.getQtdExercito() == 0){
				JOptionPane.showMessageDialog(null, "O jogador: " + atacante.getPlayerId().getName() + " conquistou o territorio " +
						defensor.getNome());
				if(ganhou == 0){
					ganhou = 1;
				}
				defensor.getPlayerId().playerTerritories.remove(defensor);
				atacante.getPlayerId().playerTerritories.add(defensor);

				if(defensor.getPlayerId().playerTerritories.size() == 0){
					setChanged();
					notifyObservers(null);
				}
				defensor.setPlayerId(atacante.getPlayerId());
				ControladorJogadores.getInstance().currentCliente.player = atacante.getPlayerId();
				
				while(signal == false){
					try{
						s = JOptionPane.showInputDialog(null, "Quantos exército(s) gostaria de deslocar para o território conquistado?" +
								"\n" + "Min(1) & " + "Max(" + qtdDadosAtacante + ")");
						if(s != null){
							if(Integer.parseInt(s) > qtdDadosAtacante){
								JOptionPane.showMessageDialog(null, "Só é possivel passar no máximo " + qtdDadosAtacante + " exercito(s)");
							}
							else if(Integer.parseInt(s) == 0){
								JOptionPane.showMessageDialog(null, "É necessário passar no minimo 1 exército");
							}
							else{
								signal = true;
								atacante.setQtdExercito(atacante.getQtdExercito()-Integer.parseInt(s));
								defensor.setQtdExercito(Integer.parseInt(s));
							}
						}
					}catch (Exception ex){
						JOptionPane.showMessageDialog(null, "Utilize apenas números");
					}
				}
			}
			verificaContinente(defensor);
			if(signal == true){
				defensor = null;
				atacante = null;
				setChanged();
				notifyObservers(false);
			}
		}
	}

	private void organizeFase(Territorio t){
		int encontrou = 0;
		if(ControladorJogadores.getInstance().currentCliente.player.getName().equals(t.getPlayerId().getName())){
			if(distribuidor == null){
				if(t.getQtdExercito() == 1){
					JOptionPane.showMessageDialog(null, "Somente territorios com 2 ou mais exército(s) podem distribuir");
				}
				else{
					distribuidor = t;
					JOptionPane.showMessageDialog(null, "Distribuidor: " + distribuidor.getNome());
				}
			}
			else{
				int i;
				encontrou = 1;
				for(i=0; i<distribuidor.getFronteiras().size(); i++){
					if(t == distribuidor.getFronteiras().get(i) && t.getPlayerId() == distribuidor.getPlayerId()){
						recebedor = t;
						JOptionPane.showMessageDialog(null, "Recebedor: " + recebedor.getNome());
						boolean signal = false;
						while(signal == false){
							String s;
							try{
								s = JOptionPane.showInputDialog(null, "Quantos exército(s) gostaria de distribuir?");
								if(s != null){
									if(Integer.parseInt(s) > distribuidor.getQtdExercito()-distribuidor.getExercitosRecebidos()){
										JOptionPane.showMessageDialog(null, "Só é possivel distrubuir no máximo " + (distribuidor.getQtdExercito()-distribuidor.getExercitosRecebidos()) + " exercito(s)");
										continue;
									}
									else if(Integer.parseInt(s) == distribuidor.getQtdExercito()){
										JOptionPane.showMessageDialog(null, "Não é possivel distribuir todo exército de um território" + "\n"+ "Deve-se deixar pelo menos 1 exercito no territorio");
									}
									else{
										signal = true;
										distribuidor.setQtdExercito(distribuidor.getQtdExercito()-Integer.parseInt(s));
										recebedor.setQtdExercito(recebedor.getQtdExercito()+Integer.parseInt(s));
										recebedor.setExercitosRecebidos(Integer.parseInt(s));
										distribuidor.setExercitosRecebidos(0);
										distribuidor = null;
										recebedor = null;
										encontrou = 0;
									}
								}
								else{
									return;
								}
							}catch (Exception ex){
								JOptionPane.showMessageDialog(null, "Utilize apenas números");
							}
						}
						break;
					}
				}
				if(recebedor == null && encontrou == 1){
					JOptionPane.showMessageDialog(null, "Esse pais não faz fronteira com: " + distribuidor.getNome());
				}
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Você não possui este territorio");
		}
	}

	public void verificaBonus(){
		int i, j, cont = 0;
		for(i=0; i<ControladorDeTerritorios.getInstance().vContinente.size(); i++){
			for(j=0; j<ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size(); j++){
				if(ControladorDeTerritorios.getInstance().vContinente.get(i).getNome().equals(ControladorJogadores.getInstance().currentCliente.player.playerTerritories.get(j).getContinente().getNome())){
					cont++;
				}	
			}
			if(cont == ControladorDeTerritorios.getInstance().vContinente.get(i).getQtdExercitos()){
				distArmy = distArmy + ControladorDeTerritorios.getInstance().vContinente.get(i).getBonusExercitos();
			}
			cont = 0;
		}
	}

	public void verificaContinente(Territorio t){
		int i, cont = 0;
		for(i=0; i<ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size(); i++){
			if(ControladorJogadores.getInstance().currentCliente.player.playerTerritories.get(i).getContinente().getNome().equals(t.getContinente().getNome())){
				cont++;
			}	
		}
		if(t.getContinente().getQtdExercitos() == cont){
			JOptionPane.showMessageDialog(null, "Parabéns você conquistou o continente: " + t.getContinente().getNome());
		}
	}

	public void trocaCartas(){
		if(troca <= 5){
			if(troca > 1){
				exercitosTroca += 2;
			}
			distArmy = distArmy + exercitosTroca;
		}
		else if(troca == 6){
			exercitosTroca += 3;
			distArmy = distArmy + exercitosTroca;
		}
		else{
			exercitosTroca += 5;
			distArmy = distArmy + exercitosTroca;
		}
		JOptionPane.showMessageDialog(null, "Você ganhou: " + exercitosTroca + " pela troca");
		if(fases == Fase.Distribuicao){
			setChanged();
			notifyObservers();
		}
		troca++;
	}

	public void criacaoObjetivos(){
		int i,j;
		Random randomizer = new Random();
		//Goals
		ArrayList<Continente> vCont;

		vCont = new ArrayList<Continente>(Arrays.asList(ControladorDeTerritorios.getInstance().vContinente.get(2), ControladorDeTerritorios.getInstance().vContinente.get(5)));
		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(vCont,"Conquistar na totalidade a EUROPA, a OCEANIA e mais um terceiro", true));

		vCont = new ArrayList<Continente>(Arrays.asList(ControladorDeTerritorios.getInstance().vContinente.get(4), ControladorDeTerritorios.getInstance().vContinente.get(1)));
		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(vCont,"Conquistar na totalidade a ASIA e a AMÉRICA DO SUL", false));

		vCont = new ArrayList<Continente>(Arrays.asList(ControladorDeTerritorios.getInstance().vContinente.get(2), ControladorDeTerritorios.getInstance().vContinente.get(1)));
		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(vCont, "Conquistar na totalidade a EUROPA, a AMÉRICA DO SUL e mais um terceiro", true));

		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(18, true, "Conquistar 18 TERRITÓRIOS e ocupar cada um deles com pelo menos dois exércitos"));

		vCont = new ArrayList<Continente>(Arrays.asList(ControladorDeTerritorios.getInstance().vContinente.get(4), ControladorDeTerritorios.getInstance().vContinente.get(3)));
		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(vCont, "Conquistar na totalidade a ASIA e a ÁFRICA", false));

		vCont = new ArrayList<Continente>(Arrays.asList(ControladorDeTerritorios.getInstance().vContinente.get(0), ControladorDeTerritorios.getInstance().vContinente.get(3)));
		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(vCont, "Conquistar na totalidade a AMÉRICA DO NORTE e a ÁFRICA", false));

		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(24, false, "Conquistar 24 TERRITÓRIOS à sua escolha"));

		vCont = new ArrayList<Continente>(Arrays.asList(ControladorDeTerritorios.getInstance().vContinente.get(0), ControladorDeTerritorios.getInstance().vContinente.get(5)));
		ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(vCont, "Conquistar na totalidade a AMÉRICA DO NORTE e a OCEANIA", false));

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Azul"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS AZUIS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Amarelo"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS AMARELOS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Vermelho"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS VERMELHOS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Preto"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS PRETOS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Branco"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS BRANCOS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Verde"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS VERDES", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Laranja"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS LARANJAS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Rosa"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS ROSAS", false));
			}
		}

		for(j=0; j<ControladorJogadores.getInstance().vPlayers.size(); j++){
			if(ControladorJogadores.getInstance().vPlayers.get(j).getColorName() == "Marrom"){
				ControladorDeJogo.getInstance().vObjetivos.add(new Objetivo(ControladorJogadores.getInstance().vPlayers.get(j), "Destruir totalmente OS EXÉRCITOS MARROMS", false));
			}
		}

		ArrayList<Objetivo> aux = new ArrayList<Objetivo>(ControladorDeJogo.getInstance().vObjetivos);

		for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
			int numero = randomizer.nextInt(aux.size());
			if(aux.get(numero).getPLayerAlvo() == ControladorJogadores.getInstance().vPlayers.get(i)){
				aux.get(numero).setAlvoIncorreto(true);
			}
			ControladorJogadores.getInstance().vPlayers.get(i).setObjetivo(aux.get(numero));
			aux.remove(numero);
		}

	}
	
	public void selectTerritory(){
		int i = 0;
		Random randomizer = new Random();
		ArrayList<Territorio> lstAux = new ArrayList<Territorio>(ControladorDeTerritorios.getInstance().lstTerritorios);
		while(!lstAux.isEmpty()){
			if(i==ControladorJogadores.getInstance().vPlayers.size()){
				i = 0;
			}
			int numero = randomizer.nextInt(lstAux.size());
			Territorio t = lstAux.remove(numero);
			t.setPlayerId(ControladorJogadores.getInstance().vPlayers.get(i));
			ControladorJogadores.getInstance().vPlayers.get(i).playerTerritories.add(t);
			i++;
		}
	}
	
}
