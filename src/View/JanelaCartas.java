package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Controller.ControladorJogadores;

public class JanelaCartas extends JDialog {
	public int LARG_DEFAULT;
	public int ALT_DEFAULT;
	
	public JanelaCartas(JFrame owner){

		super(owner, true);
		//Frame
		if(ControladorJogadores.getInstance().currentCliente.player.playerCards.size() == 0){
			LARG_DEFAULT = 220;
			ALT_DEFAULT = 100;
			JLabel lMsg = new JLabel("Você não possui cartas");
			lMsg.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(lMsg);
		}
		else{
			//Panels
			if(ControladorJogadores.getInstance().currentCliente.player.playerCards.size() > 5){
				LARG_DEFAULT = 15+205*5;
				ALT_DEFAULT = 655;
			}
			else{
				LARG_DEFAULT = 15+205*ControladorJogadores.getInstance().currentCliente.player.playerCards.size();
				ALT_DEFAULT = 320;
			}
			
			this.setLayout(null);
			int i, j = 0;
			ImagePanel cardPanel;
			for(i=0; i<ControladorJogadores.getInstance().currentCliente.player.playerCards.size(); i++){
				BufferedImage img;
				try {
					img = ImageIO.read(new File(ControladorJogadores.getInstance().currentCliente.player.playerCards.get(i).getImgCarta()));
					if(i >= 5){
						cardPanel = new ImagePanel(img, 15 + 205*j, 340);
						j++;
					}
					else{
						cardPanel = new ImagePanel(img, 15 + 205*i, 20);
					}
					cardPanel.setVisible(true);
					cardPanel.setSize(200, 250);
					add(cardPanel);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		int sl=screenSize.width;
		int sa=screenSize.height;
		int x=sl/2-LARG_DEFAULT/2;
		int y=sa/2-ALT_DEFAULT/2;
		setBounds(x,y,LARG_DEFAULT,ALT_DEFAULT);
	}
	
}
