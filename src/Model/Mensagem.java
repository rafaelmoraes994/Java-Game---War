package Model;

import java.io.Serializable;

public class Mensagem implements Serializable {
	
	private String msg;
	private String nome;
	private Object obj;
	private Object obj2;
	
	public Mensagem(String msg, String nome){
		this.msg = msg;
		this.nome = nome;
	}
	
	public Mensagem(String msg, Object obj){
		this.msg = msg;
		this.obj = obj;
	}
	
	public Mensagem(String msg, Object obj, Object obj2){
		this.msg = msg;
		this.obj = obj;
		this.obj2= obj2;
	}

	public String getMsg() {
		return msg;
	}

	public String getNome() {
		return nome;
	}
	
	public Object getObject(){
		return obj;
	}
	
	public Object getObject2(){
		return obj2;
	}
	
}
