package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Territorio implements Serializable{

	private Point posExercito;
	private String Nome;
	private GeneralPath poligono;
	private Player playerId;
	private int qtdExercito = 0;
	private ArrayList<Territorio> fronteiras;
	private JLabel contador;
	private Continente continente;
	private int exercitosRecebidos = 0;

	public Territorio(String nome, Ponto p[], float x, float y, Point posExercito, Continente continente) 
	{
		super();

		this.Nome = nome;

		this.posExercito = posExercito;
		
		this.continente = continente;

		GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);				

		gp.moveTo(p[0].get("x") + (x),p[0].get("y") + (y));

		for (int i = 1; i < p.length; i++) {
			gp.lineTo(p[i].get("x") + (x), p[i].get("y") + (y));
		}

		gp.closePath();

		this.poligono = gp;
	}

	public GeneralPath getPoligono() 
	{
		return this.poligono;
	}

	public String getNome() {
		return Nome;
	}

	public int getQtdExercito() {
		return qtdExercito;
	}

	public Continente getContinente() {
		return continente;
	}

	public void setQtdExercito(int qtdExercito) {
		this.qtdExercito = qtdExercito;
		contador.setText(""+qtdExercito);
		changeColor();
	}

	public Point getPosExercito() {
		return posExercito;
	}

	public void setFronteiras(ArrayList<Territorio> fronteiras) {
		this.fronteiras = fronteiras;
	}

	public ArrayList<Territorio> getFronteiras() {
		return fronteiras;
	}

	public int fazFronteira(Territorio t){
		int i;
		for(i=0; i<fronteiras.size(); i++){
			if(fronteiras.get(i) == t){
				return 1;
			}
		}
		return 0;
	}

	public Player getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Player playerId) {
		this.playerId = playerId;
		changeColor();
	}
	
	public JLabel getContador(){
		return contador;
	}

	public void setContador(JLabel contador) {
		this.contador = contador;
		contador.setHorizontalTextPosition(SwingConstants.CENTER);
		contador.setFont(new Font("Ariel", Font.BOLD, 20));
		changeColor();
		
		setQtdExercito(1);
		
		contador.setName("territorio");
		
	}
	
	public int getExercitosRecebidos() {
		return exercitosRecebidos;
	}

	public void setExercitosRecebidos(int exercitosRecebidos) {
		this.exercitosRecebidos = exercitosRecebidos;
	}

	public void incrementContador(){
		setQtdExercito(getQtdExercito()+1);
	}
	
	public void decrementContador(){
		setQtdExercito(getQtdExercito()-1);
	}
	
	public void changeColor(){
		if(contador == null){
			return;
		}
		BufferedImage armyIcon = null;
		String size = "small";
		String colorName = playerId.getColorName();
		if(qtdExercito >= 10){
		   size = "big";
		}
		try{
			switch(colorName){
			case "Branco": 
				armyIcon = ImageIO.read(new File("images/exercicitos/white army "+size+".png"));
				break;
			case "Vermelho":
				armyIcon = ImageIO.read(new File("images/exercicitos/red army "+size+".png"));
				break;

			case "Preto":
				armyIcon = ImageIO.read(new File("images/exercicitos/black army "+size+".png"));
				break;

			case "Verde":
				armyIcon = ImageIO.read(new File("images/exercicitos/green army "+size+".png"));
				break;

			case "Laranja":
				armyIcon = ImageIO.read(new File("images/exercicitos/orange army "+size+".png"));
				break;

			case "Rosa":
				armyIcon = ImageIO.read(new File("images/exercicitos/pink army "+size+".png"));
				break;

			case "Marrom":
				armyIcon = ImageIO.read(new File("images/exercicitos/brown army "+size+".png"));
				break;

			case "Azul":
				armyIcon = ImageIO.read(new File("images/exercicitos/blue army "+size+".png"));
				break;
			case "Amarelo":
				armyIcon = ImageIO.read(new File("images/exercicitos/yellow army "+size+".png"));
				break;
			}
		}catch (IOException ex) {
			System.out.println(ex.getMessage());
			System.exit(1);
		}

		contador.setIcon(new ImageIcon(armyIcon));
		if(colorName != "Branco" && colorName != "Laranja" && colorName != "Amarelo"){
			contador.setForeground(Color.white);
		}
		else{
			contador.setForeground(Color.black);
		}
		if(size == "small"){
			contador.setBounds(getPosExercito().x, getPosExercito().y+10, 30, 30);
		}
		else{
			contador.setBounds(getPosExercito().x, getPosExercito().y, 40, 40);
		}
		
	}	
}
