package Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Controller.ControladorDeJogo;
import Controller.ControladorDeTerritorios;
import Controller.ControladorDeJogo.Fase;
import Controller.ControladorJogadores;

public class Objetivo implements Serializable {
	
	enum tipoObjetivo {
		ConquistarTerritorio,
		ConquistarContinente,
		DestruirJogador		
	}
	
	private tipoObjetivo tipoObj;
	private int qtdTerritorio;
	private boolean ocuparDoisExercitos;
	private boolean terceiroContinente;
	private boolean alvoIncorreto;
	private ArrayList<Continente> vContinente;
	private Player playerAlvo;
	private String descricao;
	
	public Objetivo(int qtdTerritorio, boolean ocuparDoisExercitos, String descricao) {
		super();
		this.qtdTerritorio = qtdTerritorio;
		this.ocuparDoisExercitos = ocuparDoisExercitos;
		this.descricao = descricao;
		this.tipoObj = tipoObjetivo.ConquistarTerritorio;
	}

	public Objetivo(ArrayList<Continente> vContinente, String descricao, boolean terceiroContinente) {
		super();
		this.vContinente = vContinente;
		this.descricao = descricao;
		this.terceiroContinente = terceiroContinente;
		this.tipoObj = tipoObjetivo.ConquistarContinente;
	}
	
	public Objetivo(Player playerAlvo, String descricao, boolean alvoIncorreto) {
		super();
		this.playerAlvo = playerAlvo;
		this.descricao = descricao;
		this.tipoObj = tipoObjetivo.DestruirJogador;
		this.alvoIncorreto = alvoIncorreto;
	}

	public void verificaObjetivo(Player playerAtual) {
		switch(tipoObj){
		case ConquistarTerritorio:
			verificaConquistaTerritorio(playerAtual);
			break;
		case ConquistarContinente:
			verificaConquistaContinente(playerAtual);
			break;
		case DestruirJogador:
			verificaDestruicaoJogador(playerAtual);
			break;
		}	
	}
	
	private void verificaConquistaTerritorio(Player playerAtual){
		int i;
		if(playerAtual.playerTerritories.size() >= qtdTerritorio){
			if(ocuparDoisExercitos == true){
				for(i=0; i<playerAtual.playerTerritories.size(); i++){
					if(playerAtual.playerTerritories.get(i).getQtdExercito() < 2){
						return;
					}
				}
				JOptionPane.showMessageDialog(null, "Parabéns você venceu!!");
				try {
					Mensageiro.enviaMensagem("close", ControladorJogadores.getInstance().currentCliente.cliente);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Parabéns você venceu!!");
				try {
					Mensageiro.enviaMensagem("close", ControladorJogadores.getInstance().currentCliente.cliente);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void verificaConquistaContinente(Player playerAtual){
		int i, j, contTotal, contPlayer;	
		for(i=0; i<vContinente.size(); i++){
			contTotal = 0;
			contPlayer = 0;
			for(j=0; j<ControladorDeTerritorios.getInstance().lstTerritorios.size(); j++){
				if(ControladorDeTerritorios.getInstance().lstTerritorios.get(j).getContinente().nome.equals(vContinente.get(i).nome)){
					contTotal++;
				}
			}
			for(j=0; j<playerAtual.playerTerritories.size(); j++){
				if(playerAtual.playerTerritories.get(j).getContinente().getNome().equals(vContinente.get(i).getNome())){
					contPlayer++;
				}
			}
			if(contPlayer != contTotal){
				return;
			}
		}
		if(terceiroContinente == true){
			ArrayList<Continente> ContinenteRestantes = new ArrayList<Continente>();
			for(i=0; i<ControladorDeTerritorios.getInstance().vContinente.size(); i++){
				ContinenteRestantes.add(ControladorDeTerritorios.getInstance().vContinente.get(i));
			}
			
			for(i=0; i<vContinente.size(); i++){
				for(j=0; j<ContinenteRestantes.size(); j++){
					if(vContinente.get(i) == ContinenteRestantes.get(j)){
						ContinenteRestantes.remove(j);
						j--;
					}
				}
			}
			
			for(i=0; i<ContinenteRestantes.size(); i++){
				contTotal = 0;
				contPlayer = 0;
				for(j=0; j<ControladorDeTerritorios.getInstance().lstTerritorios.size(); j++){
					if(ControladorDeTerritorios.getInstance().lstTerritorios.get(j).getContinente().nome.equals( ContinenteRestantes.get(i).nome)){
						contTotal++;
					}
				}
				for(j=0; j<playerAtual.playerTerritories.size(); j++){
					if(playerAtual.playerTerritories.get(j).getContinente().nome.equals(ContinenteRestantes.get(i).nome)){
						contPlayer++;
					}
				}
				if(contPlayer == contTotal){
					JOptionPane.showMessageDialog(null, "Parabéns você venceu!!");
					try {
						Mensageiro.enviaMensagem("close", ControladorJogadores.getInstance().currentCliente.cliente);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		}
		else{
			JOptionPane.showMessageDialog(null, "Parabéns você venceu!!");
			try {
				Mensageiro.enviaMensagem("close", ControladorJogadores.getInstance().currentCliente.cliente);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void verificaDestruicaoJogador(Player playerAtual){
		if(isAlvoIncorreto() == true){
			qtdTerritorio = 24;
			verificaConquistaTerritorio(playerAtual);
		}
		else{
			if(ControladorDeJogo.getInstance().fases == Fase.Ataque){
				if(playerAlvo.playerTerritories.size() == 0){
					JOptionPane.showMessageDialog(null, "Parabéns você venceu!!");
					try {
						Mensageiro.enviaMensagem("close", ControladorJogadores.getInstance().currentCliente.cliente);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else{
				if(playerAlvo.playerTerritories.size() == 0){
					playerAtual.setObjetivo(new Objetivo(24, false, "Conquistar 24 TERRITÓRIOS à sua escolha"));
				}
				
			}
		}
		
	}

	public String getDescricao() {
		return descricao;
	}
	
	public Player getPLayerAlvo() {
		return playerAlvo;
	}

	public void setAlvoIncorreto(boolean alvoIncorreto) {
		this.alvoIncorreto = alvoIncorreto;
	}

	public boolean isAlvoIncorreto() {
		return alvoIncorreto;
	}

	public int getQtdTerritorio() {
		return qtdTerritorio;
	}
	
	

}
