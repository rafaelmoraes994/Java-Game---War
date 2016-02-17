package View;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import Controller.ControladorDeTerritorios;
import Controller.ControladorJogadores;
import Model.Territorio;

@SuppressWarnings("serial")
public abstract class Mapa extends JPanel {

	// deslocaX e deslocaY é utilizado para alinhar os polígonos criados em cima da imagem dos territorios.

	public Mapa() {
		this.addMouseListener(new MouseListener() {

			// Evento de click para detectar se o ponto clicado está dentro da area do teritorio.
			@Override
			public void mouseClicked(MouseEvent e) {

				// Para cada territorio da lista de territorios
				for(Territorio t : ControladorDeTerritorios.getInstance().lstTerritorios) {

					// Se o ponto clicado for contido pelo poligono do territorio	
					if(t.getPoligono().contains(e.getX(), e.getY())) {
						acessaTerritorio(t);
					}
				}


			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected abstract void acessaTerritorio(Territorio t);

}