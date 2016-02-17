package Model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Controller.ControladorDeCartas;
import Controller.ControladorDeJogo;
import Controller.ControladorDeTerritorios;
import Controller.ControladorJogadores;


public class ThreadCliente implements Runnable  {

	private Socket cliente;
	private ObjectInputStream threadInput = null;

	public ThreadCliente(Socket s) {
		this.cliente = s;
	}

	public Socket getSocketCliente(){
		return cliente;
	}

	@Override
	public void run() {
		try {
			threadInput = new ObjectInputStream(cliente.getInputStream());

			while(true) {
				Object objInput = threadInput.readObject();

				if(objInput instanceof String){
					String str = (String)objInput;
					if (str.equals("close")) {
						Servidor.getInstance().imprimeMsgClientes("close");
						break;
					}
					else if(str.equals("verifica vetor de players")){
						if(ControladorJogadores.getInstance().vPlayers.size() >= 3){
							if(ControladorJogadores.getInstance().vPlayers.size() == 6){
								JOptionPane f = new JOptionPane("Erro");
								f.setVisible(true);
								JOptionPane.showMessageDialog(f,"Jogo ja está cheio");
								Servidor.getInstance().msgCliente("cheio", this);
							}
							Servidor.getInstance().msgCliente("pronto para jogar", Servidor.getInstance().getVetorThreads().get(0));
						}
					}
					else if(str.equals("muda frame OrdemJogadores")){
						Servidor.getInstance().imprimeMsgClientes("muda frame OrdemJogadores");
					}
					else if(str.equals("inicializa territorios")){
						int i,j;
						for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
							if(ControladorJogadores.getInstance().vPlayers.get(i).getName().equals(ControladorJogadores.getInstance().currentCliente.player.getName())){
								ControladorJogadores.getInstance().currentCliente.player = ControladorJogadores.getInstance().vPlayers.get(i);
							}
							for(j=0; j<ControladorJogadores.getInstance().vPlayers.get(i).playerTerritories.size(); j++){
								JLabel army = new JLabel();
								ControladorJogadores.getInstance().vPlayers.get(i).playerTerritories.get(j).setContador(army);
							}		
						}
						ControladorJogadores.getInstance().currentPlayer = ControladorJogadores.getInstance().vPlayers.get(0);
						Mensagem msg = new Mensagem("inicializa territorios", ControladorJogadores.getInstance().vPlayers, ControladorDeTerritorios.getInstance().lstTerritorios);
						Servidor.getInstance().imprimeMsgClientes(msg);
					}
					else if(str.equals("sorteia")){
						int numero;
						Random randomizer = new Random();
						ArrayList<Player> vNovaOrdem = new ArrayList<Player>();
						while(!ControladorJogadores.getInstance().vPlayers.isEmpty()){
							numero = randomizer.nextInt(ControladorJogadores.getInstance().vPlayers.size());
							vNovaOrdem.add(ControladorJogadores.getInstance().vPlayers.get(numero));
							ControladorJogadores.getInstance().vPlayers.remove(numero);
						}
						ControladorJogadores.getInstance().vPlayers = vNovaOrdem;
						ControladorJogadores.getInstance().currentPlayer = ControladorJogadores.getInstance().vPlayers.get(0);
						Mensagem msg = new Mensagem("atualiza vetor", ControladorJogadores.getInstance().vPlayers);
						Servidor.getInstance().imprimeMsgClientes(msg);

					}
					else if(str.equals("cartas")){
						ControladorDeCartas.getInstance();
						Servidor.getInstance().imprimeMsgClientes("cartas");
					}
					else if(str.equals("objetivos")){
						ControladorDeJogo.getInstance().criacaoObjetivos();
						Mensagem msg = new Mensagem("objetivos", ControladorJogadores.getInstance().vPlayers);
						Servidor.getInstance().imprimeMsgClientes(msg);
					}
					else if(str.equals("distribui territorios")){
						ControladorDeJogo.getInstance().selectTerritory();
						Mensagem msg = new Mensagem("distribui territorios", ControladorJogadores.getInstance().vPlayers, ControladorDeTerritorios.getInstance().lstTerritorios);
						Servidor.getInstance().imprimeMsgClientes(msg);	
					}
				}
				else if(objInput instanceof Mensagem){
					Mensagem msg = (Mensagem)objInput;
					Servidor.getInstance().imprimeMsgClientes(msg.getNome() + " disse: " + msg.getMsg());	
				}
				else if(objInput instanceof Player){
					Player p = (Player)objInput;
					int i;
					boolean found = false;;
					for(i=0; i<ControladorJogadores.getInstance().vPlayers.size(); i++){
						if(ControladorJogadores.getInstance().vPlayers.get(i).getColorName().equals( p.getColorName())){
							Servidor.getInstance().msgCliente("cor repetida", this);
							found = true;
						}
					}
					if(found == false){
						ControladorJogadores.getInstance().vPlayers.add(p);
						Servidor.getInstance().msgCliente(p, this);
					}
				}
				else if(objInput instanceof EstadoDoJogo){
					EstadoDoJogo novoEstado = (EstadoDoJogo)objInput;
					Servidor.getInstance().imprimeMsgClientes(novoEstado);
				}
			}

			Servidor.getInstance().imprimeMsgClientes(" se desconectou");

			threadInput.close();
			cliente.close();

		} catch (IOException | ClassNotFoundException e) {
		}

	}
}
