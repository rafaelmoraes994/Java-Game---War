package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Controller.ControladorDeJogo;
import Controller.ControladorJogadores;
import Model.Cartas;


public class JanelaDeTrocas extends JanelaCartas{

	ArrayList<JCheckBox> vCheckBox = new ArrayList<JCheckBox>();
	ArrayList<Cartas> vTradeCards = new ArrayList<Cartas>();

	public JanelaDeTrocas(JFrame owner) {
		super(owner);
		if(ControladorJogadores.getInstance().currentCliente.player.playerCards.size() > 0){
			if(ControladorJogadores.getInstance().currentCliente.player.playerCards.size() > 5){
				this.setSize(85+205*5, 710);
			}
			else{
				this.setSize(85+205*ControladorJogadores.getInstance().currentCliente.player.playerCards.size(), 400);
			}
			
			int i, j = 0;
			for(i=0; i<ControladorJogadores.getInstance().currentCliente.player.playerCards.size(); i++){
				JCheckBox checkBox = new JCheckBox("", false);
				if(i >= 5){
					checkBox.setBounds(90 + 205*j, 600, 40, 40);
					j++;
				}
				else{
					checkBox.setBounds(90 + 205*i, 285, 40, 40);
				}
				vCheckBox.add(checkBox);
				add(checkBox);
			}

			//OK Button
			JButton bOk = new JButton("OK");
			if(ControladorJogadores.getInstance().currentCliente.player.playerCards.size() > 5){
				bOk.setBounds(200*5, 610, 60, 30);
			}
			else{
				bOk.setBounds(200*ControladorJogadores.getInstance().currentCliente.player.playerCards.size(), 300, 60, 30);
			}
			add(bOk);
			bOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int i, cont = 0;
					for(i=0; i<vCheckBox.size(); i++){
						if(vCheckBox.get(i).isSelected()){
							vTradeCards.add(ControladorJogadores.getInstance().currentCliente.player.playerCards.get(i));
							cont++;
						}
					}
					if(cont != 3){
						JOptionPane.showMessageDialog(null, "Você deve escolher exatamente 3 cartas para efetuar a troca");
						vTradeCards = new ArrayList<Cartas>();
						cont = 0;
					}
					else{
						int triangulo = 0, quadrado = 0, circulo = 0, todos = 0;
						for(i=0; i<vTradeCards.size(); i++){
							if(vTradeCards.get(i).getTipo() == "Quadrado"){
								quadrado++;
							}
							else if(vTradeCards.get(i).getTipo() == "Triangulo"){
								triangulo++;
							}
							else if(vTradeCards.get(i).getTipo() == "Circulo"){
								circulo++;
							}
							else{
								todos++;
							}
						}
						if(triangulo == 1 && quadrado == 1){
							if(circulo == 1 || todos == 1){
								ControladorDeJogo.getInstance().trocaCartas();
								procuraCartas();
								removeCartas();
								
							}
						}
						else if(triangulo == 1 && circulo == 1){
							if(todos == 1){
								ControladorDeJogo.getInstance().trocaCartas();
								procuraCartas();
								removeCartas();
							}
						}
						else if(quadrado == 1 && circulo == 1){
							if(todos == 1){
								ControladorDeJogo.getInstance().trocaCartas();
								procuraCartas();
								removeCartas();
							}
						}
						else if(triangulo >= 2 || circulo >= 2 || quadrado >= 2){
							if(triangulo == 3 || quadrado == 3 || circulo == 3){
								ControladorDeJogo.getInstance().trocaCartas();
								procuraCartas();
								removeCartas();
							}
							else{
								if(todos == 1){
									ControladorDeJogo.getInstance().trocaCartas();
									procuraCartas();
									removeCartas();
								}
							}
						}
						else if(todos == 2){
							ControladorDeJogo.getInstance().trocaCartas();
							procuraCartas();
							removeCartas();
						}
						else{
							JOptionPane.showMessageDialog(null, "Só pode ser feita a troca de 3 tipos diferentes ou de 3 tipos iguais de cartas");
						}
					}
					dispose();
				}
			});	
		}
	}
	
	private void removeCartas(){
		int i, j;
		for(i=0; i<vTradeCards.size(); i++){
			for(j=0; j<ControladorJogadores.getInstance().currentCliente.player.playerCards.size(); j++){
				if(ControladorJogadores.getInstance().currentCliente.player.playerCards.get(j) == vTradeCards.get(i)){
					ControladorJogadores.getInstance().vCartasUsadas.add(ControladorJogadores.getInstance().currentCliente.player.playerCards.get(j));
					ControladorJogadores.getInstance().currentCliente.player.playerCards.remove(j);
				}
			}
		}
	}
	
	private void procuraCartas(){
		int i, j;
		for(i=0; i<vTradeCards.size(); i++){
			for(j=0; j<ControladorJogadores.getInstance().currentCliente.player.playerTerritories.size(); j++){
				if(ControladorJogadores.getInstance().currentCliente.player.playerTerritories.get(j).getNome().contentEquals(vTradeCards.get(i).getNomeTerritorio())){
					ControladorJogadores.getInstance().currentCliente.player.playerTerritories.get(j).setQtdExercito(ControladorJogadores.getInstance().currentCliente.player.playerTerritories.get(j).getQtdExercito()+2);
				}
			}	
		}
		
	}
	
}
