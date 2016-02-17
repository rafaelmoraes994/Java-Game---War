package Model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
	String name;
	String colorName;
	Color color;
	public ArrayList<Territorio> playerTerritories = new ArrayList<Territorio>();
	public ArrayList<Cartas> playerCards = new ArrayList<Cartas>();
	Objetivo objetivo;
	
	public Player(String name, Color color, String colorName, Objetivo obj) 
	{
		this.name = name;
		this.color = color;
		this.colorName = colorName;
		this.objetivo = obj;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}
	
	public String getColorName() {
		return colorName;
	}

	public Objetivo getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Objetivo objetivo) {
		this.objetivo = objetivo;
	}
	
}
