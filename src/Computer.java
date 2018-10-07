import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Computer extends Contender{
	Contender con;
	int number, xvalue, moves;
	Territory temp;
	ArrayList<Territory> owned, poss;
	Image box1, box2, box3, box4, box5;
	Territory oppose;
	BufferedImage masterImage;
	GameBoard gb;
	Timer xr;

	
	public Computer(int n, String s1, String s2, String s3, String s4, String s5, GameBoard gboard){		
		numterritories = 0;
		number = n;
		gold = 100;
		owned = new ArrayList<Territory>();
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
		xvalue = -1;
		gb = gboard;
		masterImage = temp;
		con = this;
	}
	
	
	@Override
	public void move(){
		moves = 0;
		gb.gp.mt.delay2check = true;
		while(gold>=100){
			tanknum++;
			gold-=100;
		}
		while(gold>=50){
			infantrynum++;
			gold-=50;
		}
		poss = new ArrayList<Territory>();
		for(int x = 0; x<18; x++){
			if(gb.territories.get(x).contender == this){
				poss.add(gb.territories.get(x));
			}
		}
		if(poss.size() != 0){	
			while(infantrynum>0){
				int a = (int) (Math.random()*poss.size());
				poss.get(a).attack+=46;
				infantrynum--;
			}
		}
		poss = new ArrayList<Territory>();
		for(int x = 0; x<18; x++){
			if(gb.territories.get(x).contender == this){
				poss.add(gb.territories.get(x));
			}
		}
		if(poss.size() != 0){	
			while(tanknum>0){
				int a = (int) (Math.random()*poss.size());
				poss.get(a).attack+=41;
				poss.get(a).defense+=41;
				tanknum--;
			}
		}
		xr = new Timer(2, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(gb.gp.mt.delay2check == false){
					/*gb.gp.ta = owned.get(xvalue).position == 1 || owned.get(xvalue).position == 2 ||owned.get(xvalue).position == 9?50+owned.get(xvalue).x:owned.get(xvalue).x+50;
					gb.gp.tb = owned.get(xvalue).position == 1 || owned.get(xvalue).position == 2 ||owned.get(xvalue).position == 9?50+owned.get(xvalue).y:owned.get(xvalue).y+owned.get(xvalue).sizey-50;
					gb.gp.tc = temp.position == 1 || temp.position == 2 ||temp.position == 9?30+temp.x:temp.x+30;
					gb.gp.td = temp.position == 1 || temp.position == 2 ||temp.position == 9?30+temp.y:temp.y+temp.sizey-30;
					gb.gp.comp = true;*/
					return;
				}
				temp = recalculate();
				if(temp == null || owned.get(xvalue) == null){
					gb.gp.comp = false;
					xr.stop();
					gb.next();
				}
				else{
					Territory[] attackarray = new Territory[2];
					attackarray[0] = owned.get(xvalue);
					attackarray[1] = temp;
					gb.attack(attackarray);
					gb.gp.mt.delay2(200, con);
				}
				moves++;
			}
	
		});
		xr.start();
	}
	
	public Territory recalculate(){
		owned = new ArrayList<Territory>();
		for(int x = 0; x<18; x++){
			if(gb.territories.get(x).contender == this && gb.territories.get(x).attack != 0){
				owned.add(gb.territories.get(x));
			}
		}
		for(int x = 0; x<owned.size(); x++){
			for(int y = 0; y<18; y++){
				if(gb.territories.get(y).contender!= this && owned.get(x).attack!=0 && gb.isAdjacent(owned.get(x), gb.territories.get(y))){
					int a = owned.get(x).attack-gb.territories.get(y).defense;
					int b = gb.territories.get(y).attack-owned.get(x).defense;
					if(a>=b){
						xvalue = x;
						oppose = gb.territories.get(y);
						owned.get(x).updateClicked();
						gb.territories.get(y).updateClicked();
						return gb.territories.get(y);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	int getMoney() {
		// TODO Auto-generated method stub
		return 0;
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
}
