package View;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import Controller.ControladorDeJogo;
import Model.Territorio;


public class MapPanel extends Mapa{

    private BufferedImage image;
    
    public MapPanel() {
       try {                
          image = ImageIO.read(new File("images/war_tabuleiro_com_nomes.png"));
       } catch (IOException ex) {
    	   System.out.println(ex.getMessage());
    	   System.exit(1);
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 1024 , 768, null);   
    }

	@Override
	protected void acessaTerritorio(Territorio t) {
		ControladorDeJogo.getInstance().verificarEstado(t);
		
	}

}