import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Territory {
	//private ArrayList<Troop> troops;
	GameBoard gb;
	Contender contender;
	JLabel label;
	Image image1, image2, image3, image4, image5, image6;
	BufferedImage bimage1, bimage2, bimage3, bimage4, bimage5, bimage6;
	ArrayList<Territory> adjacent;
	int x, y, sizex, sizey, defense = 0, attack = 0, position;
	int maxattack, maxdefense;
	int cn = -1; //White, Gray, Red, Green, Blue, Purple
	boolean grayed = false, clicked = false, clickedOnce = false;
	
	public Territory(GameBoard g, int a, int b, int sizx, int sizy, String img1, String img2, String img3, String img4, String img5, String img6, Contender c, int p){
		gb = g;
		x = a; y = b;
		adjacent = new ArrayList<Territory>();
		image1 = new ImageIcon(getClass().getResource(img1)).getImage(); bimage1 = convertToBufferedImage(image1);
		image2 = new ImageIcon(getClass().getResource(img2)).getImage(); bimage2 = convertToBufferedImage(image2);
		image3 = new ImageIcon(getClass().getResource(img3)).getImage(); bimage3 = convertToBufferedImage(image3);
		image4 = new ImageIcon(getClass().getResource(img4)).getImage(); bimage4 = convertToBufferedImage(image4);
		image5 = new ImageIcon(getClass().getResource(img5)).getImage(); bimage5 = convertToBufferedImage(image5);
		image6 = new ImageIcon(getClass().getResource(img6)).getImage(); bimage6 = convertToBufferedImage(image6);
		bimage1 = convertToBufferedImage(bimage1.getScaledInstance(sizx, sizy, Image.SCALE_SMOOTH));
		bimage2 = convertToBufferedImage(bimage2.getScaledInstance(sizx, sizy, Image.SCALE_SMOOTH));
		bimage3 = convertToBufferedImage(bimage3.getScaledInstance(sizx, sizy, Image.SCALE_SMOOTH));
		bimage4 = convertToBufferedImage(bimage4.getScaledInstance(sizx, sizy, Image.SCALE_SMOOTH));
		bimage5 = convertToBufferedImage(bimage5.getScaledInstance(sizx, sizy, Image.SCALE_SMOOTH));
		bimage6 = convertToBufferedImage(bimage6.getScaledInstance(sizx, sizy, Image.SCALE_SMOOTH));
		contender = c;
		sizex = sizx;
		sizey = sizy;
		if(c == null){
			cn = -1;
			attack = 0; defense = 0;
		}
		else if(c instanceof Player){
			cn = ((Player)c).number+1;
			attack = 10; defense = 10;
		}
		else if(c instanceof Computer){
			cn = ((Computer)c).number+1;
			attack = 10; defense = 10;
		}
		maxattack = 100;
		maxdefense = 100;
		position = p;
	}
	
	public void updateClicked(){
		
		if(!clicked){
			clicked = true;
			grayed = true;
			return;
		}
		clicked = false;
		grayed = false;
	}
	
	private BufferedImage convertToBufferedImage(Image image)
	{
	    BufferedImage newImage = new BufferedImage(
	        image.getWidth(null), image.getHeight(null),
	        BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = newImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return newImage;
	}
	
	public boolean isBlack(int x, int y){
		BufferedImage img = cn == 1? bimage1:cn == 2? bimage2:cn == 3? bimage3: cn == 4? bimage4 : bimage5;
		int pixel = img.getRGB(x,y);
		return ((pixel & 0x00FFFFFF) == 0);
	}
	
	public boolean isTransparent(int x, int y) {
		BufferedImage img = cn == 1? bimage1:cn == 2? bimage2:cn == 3? bimage3: cn == 4? bimage4 : bimage5;
		 int pixel = img.getRGB(x,y);
		 return (pixel>>24) == 0x00? true: false;
	}
}
