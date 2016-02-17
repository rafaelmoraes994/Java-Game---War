package Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Model.Cartas;

public class ControladorDeCartas {

	private static ControladorDeCartas instance;
//	private BufferedImage imgCarta;
	
	ArrayList<String> vFileName = new ArrayList<String>();
	
	ArrayList<String> vQuadrados = new ArrayList<String>(Arrays.asList("Angola", "Som�lia", "California", "Mexico", 
			"NovaYork", "China", "Cor�ia do Norte", "Ir�", "Jord�nia", "Let�nia", "Sib�ria", "Siria", "Argentina", "It�lia",
			"Su�cia", "Nova Zel�ndia"));
	ArrayList<String> vCirculos = new ArrayList<String>(Arrays.asList("Arg�lia", "Nig�ria", "Calgary", "Groelandia",
			"Quebec", "Ar�bia Saudita", "Bangladesh", "Cazaquist�o", "Est�nia", "Jap�o", "Paquist�o", "Brasil", "Espanha",
			"Reino Unido", "Ucr�nia", "Perth"));
	ArrayList<String> vTriangulos = new ArrayList<String>(Arrays.asList("Africa do Sul", "Egito", "Alasca", "Texas",
			"Vancouver", "Cor�ia do Sul", "�ndia", "Iraque", "Mong�lia", "Russia", "Tail�ndia", "Turquia", "Peru", "Venezuela",
			"Fran�a", "Pol�nia", "Rom�nia", "Austr�lia", "Indon�sia"));

	public static ControladorDeCartas getInstance() {
		if (instance == null)
	         instance = new ControladorDeCartas();
	      return instance;
	}

	public ControladorDeCartas(){
		int i;
		//Cards
		for(i=0; i<vQuadrados.size(); i++){
			vFileName.add("images/cartas/" + vQuadrados.get(i) + ".png");
//				imgCarta = ImageIO.read(new File("images/cartas/" + vQuadrados.get(i) + ".png"));
			ControladorJogadores.getInstance().vCartas.add(new Cartas(vQuadrados.get(i), "Quadrado", vFileName.get(i)));

		}

		for(i=0; i<vCirculos.size(); i++){
			vFileName.add("images/cartas/" + vCirculos.get(i) + ".png");
//				imgCarta = ImageIO.read(new File("images/cartas/" + vCirculos.get(i) + ".png"));
			ControladorJogadores.getInstance().vCartas.add(new Cartas(vCirculos.get(i), "Circulo", vFileName.get(i)));

		}

		for(i=0; i<vTriangulos.size(); i++){
			vFileName.add("images/cartas/" + vTriangulos.get(i) + ".png");
//				imgCarta = ImageIO.read(new File("images/cartas/" + vTriangulos.get(i) + ".png"));
			ControladorJogadores.getInstance().vCartas.add(new Cartas(vTriangulos.get(i), "Triangulo", vFileName.get(i)));

		}
		vFileName.add("images/cartas/Coringa1.png");
//			imgCarta = ImageIO.read(new File("images/cartas/Coringa1.png"));
		ControladorJogadores.getInstance().vCartas.add(new Cartas("Coringa1", "Todos", vFileName.get(i)));
		
		vFileName.add("images/cartas/Coringa2.png");
//			imgCarta = ImageIO.read(new File("images/cartas/Coringa2.png"));
		ControladorJogadores.getInstance().vCartas.add(new Cartas("Coringa2", "Todos", vFileName.get(i)));
	}
}
