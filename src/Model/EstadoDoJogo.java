package Model;

import java.io.Serializable;
import java.util.ArrayList;

import Controller.ControladorDeJogo;
import Controller.ControladorDeTerritorios;
import Controller.ControladorJogadores;

public class EstadoDoJogo implements Serializable {
	
	private ArrayList<Player> vPlayers = new ArrayList<Player>();
	private ArrayList<Cartas> vCartas = new ArrayList<Cartas>();
	private ArrayList<Cartas> vCartasUsadas = new ArrayList<Cartas>();
	private ArrayList<Territorio> lstTerritorios = new ArrayList<Territorio>();
	private Player currentPlayer;
	private int rodada;
	
	public EstadoDoJogo(){
		
		this.vPlayers = ControladorJogadores.getInstance().vPlayers;
		this.vCartas = ControladorJogadores.getInstance().vCartas;
		this.vCartasUsadas = ControladorJogadores.getInstance().vCartasUsadas;
		this.lstTerritorios = ControladorDeTerritorios.getInstance().lstTerritorios;
		this.currentPlayer = ControladorJogadores.getInstance().currentPlayer;
		this.rodada = ControladorDeJogo.getInstance().rodada;
	}

	public void carregaJogo(){
		ControladorJogadores.getInstance().vPlayers = this.vPlayers;
		ControladorJogadores.getInstance().vCartas = this.vCartas;
		ControladorJogadores.getInstance().vCartasUsadas = this.vCartasUsadas;
		ControladorDeTerritorios.getInstance().lstTerritorios = this.lstTerritorios;
		ControladorJogadores.getInstance().currentPlayer = this.currentPlayer;
		ControladorDeJogo.getInstance().rodada = this.rodada;
	}
	
	
}
