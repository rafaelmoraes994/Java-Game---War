package Model;

import java.io.Serializable;

public class Continente implements Serializable{
	
	String nome;
	int qtdExercitos;
	int bonusExercitos;
	
	public Continente(String nome,int qtdExercitos, int bonusExercitos){
		this.nome = nome;
		this.qtdExercitos = qtdExercitos;
		this.bonusExercitos = bonusExercitos;
	}

	public int getQtdExercitos() {
		return qtdExercitos;
	}

	public int getBonusExercitos() {
		return bonusExercitos;
	}

	public String getNome() {
		return nome;
	}

}
