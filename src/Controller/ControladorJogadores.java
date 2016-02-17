package Controller;

import java.util.ArrayList;

import Model.Cartas;
import Model.Player;
import View.Cliente;

public class ControladorJogadores {
	
	private static ControladorJogadores instance;
	
	public ArrayList<Player> vPlayers = new ArrayList<Player>();
	public Player currentPlayer;
	public Cliente currentCliente;
	public ArrayList<Cartas> vCartas = new ArrayList<Cartas>();
	public ArrayList<Cartas> vCartasUsadas = new ArrayList<Cartas>();
	
	private ControladorJogadores() {
		
	}
	
	public static ControladorJogadores getInstance() {
		if (instance == null)
	         instance = new ControladorJogadores();
	      return instance;
	}

}
