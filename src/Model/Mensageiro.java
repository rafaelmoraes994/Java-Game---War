package Model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Mensageiro {
	
	static ObjectOutputStream objOutput = null; 
	
	public Mensageiro() {
		
	}
	
	public static void enviaMensagem(Object msg, Socket cliente) throws IOException{
		if(objOutput == null){
			objOutput = new ObjectOutputStream(cliente.getOutputStream()); 
		}
		objOutput.writeObject(msg);
		objOutput.flush();
	}

}

