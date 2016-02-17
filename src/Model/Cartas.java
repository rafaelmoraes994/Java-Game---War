package Model;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Cartas implements Serializable {
	
	String nomeTerritorio;
	String tipo;
	String imgCarta;
	
	public Cartas (String nome, String tipo, String imagemCarta){
		super();
		this.nomeTerritorio = nome;
		this.tipo = tipo;
		this.imgCarta = imagemCarta;
		
	}

	public String getNomeTerritorio() {
		return nomeTerritorio;
	}

	public String getTipo() {
		return tipo;
	}

	public String getImgCarta() {
		return imgCarta;
	}

}
