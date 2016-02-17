package View;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelInicial extends JPanel {
	private BufferedImage image;

	public PanelInicial() {
		try {                
			image = ImageIO.read(new File("images/fundo.jpg"));
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

}
