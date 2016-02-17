package View;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	
	 BufferedImage image;
	
	public ImagePanel(BufferedImage image, int x, int y){
		this.image = image;
		this.setLocation(x, y);
	}
	
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 170 , 250, null);   
    }
	

}
