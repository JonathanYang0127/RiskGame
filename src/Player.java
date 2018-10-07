import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Player extends Contender{
	int number;
	Image box1, box2, box3, box4, box5;
	BufferedImage masterImage;
	
	public Player(int n, String s1, String s2, String s3, String s4, String s5){
		number = n;
		gold = 100;
		box1 = new ImageIcon(getClass().getResource(s1)).getImage();
		box2 = new ImageIcon(getClass().getResource(s2)).getImage();
		box3 = new ImageIcon(getClass().getResource(s3)).getImage();
		box4 = new ImageIcon(getClass().getResource(s4)).getImage();
		box5 = new ImageIcon(getClass().getResource(s5)).getImage();
		Image bi = number == 1?box2:number == 2?box3:number == 3?box4:box5;
		BufferedImage temp = convertToBufferedImage(bi);
		for(int x = 10; x<temp.getHeight()-10; x++){
			for(int y = 4; y<temp.getWidth()-4; y++){
				temp.setRGB(y, x, 0);
			}
		}
		masterImage = temp;
	}

	@Override
	void move() {
		// TODO Auto-generated method stub
		
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
	
	@Override
	int getMoney() {
		// TODO Auto-generated method stub
		return gold;
	}
//Player class
}
