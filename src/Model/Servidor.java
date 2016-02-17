package Model;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Controller.ControladorDeJogo;
import Controller.ControladorJogadores;

public class Servidor implements Runnable{

	private static Servidor instance;
	private static ServerSocket servidor = null;
	private static Socket socketCliente = null;
	private static ArrayList<ThreadCliente> vThreadClientes = new ArrayList<ThreadCliente>();
	private static ObjectOutputStream serverOutput;
	private static ArrayList<ObjectOutputStream> vServerOutput = new ArrayList<ObjectOutputStream>();


	public Servidor() throws IOException {
		servidor = new ServerSocket(12348);
		ControladorDeJogo.getInstance().servidorOn = true;
		System.out.println("Porta 12347 aberta!");
	}

	public static Servidor getInstance() throws IOException {
		if (instance == null)
			instance = new Servidor();
		return instance;
	}

	public void imprimeMsgClientes(Object mensagem) throws IOException{
		int i;
		for (i = 0; i < ControladorJogadores.getInstance().vPlayers.size(); i++) {
			if (vThreadClientes.size() != 0) {
				Socket c = vThreadClientes.get(i).getSocketCliente();
				try {
					if(vServerOutput.size() < i+1){
						serverOutput = new ObjectOutputStream(c.getOutputStream());
						vServerOutput.add(serverOutput);
					}
					vServerOutput.get(i).writeObject(mensagem);
					vServerOutput.get(i).flush();
				} catch (IOException e) {
					System.out.println("AQUI3");
					e.printStackTrace();
				}
			}
		}
	}

	public void msgCliente(Object mensagem, ThreadCliente t) throws IOException{
		Socket c = t.getSocketCliente();
		int i;
		try {
			for(i=0; i < vThreadClientes.size(); i++) {
				if(vThreadClientes.get(i) == t){
					if(vServerOutput.size() < i+1){
						serverOutput = new ObjectOutputStream(c.getOutputStream());
						vServerOutput.add(serverOutput);
					}
					vServerOutput.get(i).writeObject(mensagem);
					vServerOutput.get(i).flush();
				}
			}
		} catch (IOException e) {
			System.out.println("AQUI7");
			e.printStackTrace();
		}
	}
	
	public ArrayList<ThreadCliente> getVetorThreads(){
		return vThreadClientes;
	}

	@Override
	public void run() {
		while (true) {
			try {		
				socketCliente = servidor.accept();
				ThreadCliente t = new ThreadCliente(socketCliente);
				vThreadClientes.add(t);
				new Thread(t).start();

			} catch (IOException e) {
				System.out.println(e);
			}
		}	
	}
}