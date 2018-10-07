import java.awt.Cursor;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameBoard {
 // this is the logic board
	MasterTimer mt;
	int[] positionsx, positionsy, sx, sy, order, backorder;
	int currentindex = 0, eliminated = 0;
	GraphicsPanel gp;
	int numplayers = 2, numcomputers = 0;
	ArrayList<Territory> territories;
	ArrayList<Contender> contenders;
	boolean[][] adjmatrix;
	Contender currentPlayer, finalwinner = null;
	
	public GameBoard(GraphicsPanel gpanel, MasterTimer mtimer){
		gp = gpanel;
		mt = mtimer;
		homeScreen();
		territories = new ArrayList<Territory>();
		contenders = new ArrayList<Contender>();
		assignPositions();
	}

	public void attack(Territory[] attacking){
		int winner = 0;
		double a = 0, b = 0;
		boolean check;
		if(attacking[1].contender == null){
			winner = 0;
		}
		else{
			a = (1+0.2*(double)attacking[0].contender.aupgradenum)*attacking[0].attack-(1+0.05*(double)attacking[1].contender.dupgradenum)*attacking[1].defense;
			b = (1+0.2*(double)attacking[1].contender.aupgradenum)*attacking[1].attack-(1+0.05*(double)attacking[0].contender.dupgradenum)*attacking[0].defense;
			if(a<0 && b<0){
				attacking[1].defense-=attacking[0].attack;
				attacking[0].defense-=attacking[1].attack;
			}
			else if(a == 0 && b == 0){
				winner = (int) (Math.random()*2);
			}
			else if(a<=0){
				winner = 1;
			}
			else if(b<=0){
				winner = 0;
			}
			else{
				int temp = (int) (Math.random()*(a+b));
				winner = temp<a?0:1;
			}
		}
		if(winner == 0){
			Contender oldcontender = attacking[1].contender;
			attacking[1].cn = attacking[0].cn;
			attacking[1].contender = attacking[0].contender;
			attacking[1].attack = attacking[0].attack/2;
			attacking[1].defense = attacking[0].defense/2;
			attacking[0].attack /= 2;
			attacking[0].defense /= 2;
			if(attacking[0].contender instanceof Player){
				((Player)attacking[0].contender).numterritories++;
			}
			else if(attacking[0].contender instanceof Computer){
				((Computer)attacking[0].contender).numterritories++;
			}
			if(oldcontender == null){
				return;
			}
			else if(oldcontender instanceof Player){
				((Player)oldcontender).numterritories--;
				if(((Player)oldcontender).numterritories == 0){
					eliminated++;
				}
			}
			else if(oldcontender instanceof Computer){
				((Computer)oldcontender).numterritories--;
				if(((Computer)oldcontender).numterritories == 0){
					eliminated++;
				}
			}
			if(eliminated == contenders.size()-1){
				finalwinner = attacking[0].contender;
				gp.setCursor(Cursor.getDefaultCursor());	
				mt.delay1(1500, finalwinner);
			}	
		}
		else{
			attacking[0].attack = 0;
			attacking[0].defense = 0;
		}
	}
	

	public void endGame(Contender w) {
		gp.screen = 7;
	}

	public boolean isAdjacent(Territory one, Territory two){
		return adjmatrix[one.position][two.position];
	}
	
	public void next(){
		if(currentPlayer instanceof Player){
			((Player) currentPlayer).gold+=15*((Player) currentPlayer).numterritories;
		}
		else if(currentPlayer instanceof Computer){
			((Computer) currentPlayer).gold+=15*((Computer) currentPlayer).numterritories;
		}
		currentindex = currentindex != contenders.size()-1?currentindex+1:0;
		currentPlayer = contenders.get(currentindex);
		while(currentPlayer.numterritories==0){
			currentindex = currentindex != contenders.size()-1?currentindex+1:0;
			currentPlayer = contenders.get(currentindex);
		}
		gp.repaint();		
		if(currentPlayer instanceof Computer){
			currentPlayer.move();
		}
		gp.repaint();
	}
	
	public void start() {
		// TODO Auto-generated method stub
		generatePlayers();
		initializeTerritories();
	}
	
	private void assignPositions() {
		// TODO Auto-generated method stub
		//				1   2   3   4   5   6   7  8  9  10  11   12  13  14  15 16   17  18 
		int[] temppx = {62,327,559,732,639,290,62,62,170,233,292,343,176,427,552,552,465,552};  //change
		//				1    2   3   4   5   6   7   8   9  10   11 12  13  14  15   16 17  18 
		int[] temppy = {131,131,131,276,536,570,515,275,274,210,396,479,479,576,514,380,282,210}; //change
		//				1    2   3   4  5   6   7   8   9   10  11  12 13  14  15  16  17  18 
		int[] tempsx = {265,232,265,92,185,350,230,115,123,232,173,210,167,126,188,180,87,180};
		//				1    2   3   4   5   6   7   8   9  10  11 12  13 14 15  16   17  18 
		int[] tempsy = {145,156,145,261,200,165,220,240,212,186,83,97,177,77,150,134,200,170}; 
		positionsx = temppx; positionsy = temppy; sx = tempsx; sy = tempsy;
		int[] temp1 = {14,13,0,2,5,12,7,8,9,10,16,17,15,6,11,1,4,3};  //index is position in list, value is territory number
		order = temp1;
		int[] temp2 = {2,15,3,17,16,4,13,6,7,8,9,14,5,1,0,12,10,11};  //index is territory number, value is position on list
		backorder = temp2;
	}

	private void homeScreen() {
		// TODO Auto-generated method stub
		gp.screen = 1;
	}
	
	public void initializeTerritories() {
		territories = new ArrayList<Territory>();
		ArrayList<Integer> temprandom = new ArrayList<Integer>();
		int assign = 18/contenders.size();
		for(int x = 0; x<contenders.size(); x++){
			for(int y = 0; y<assign; y++){
				temprandom.add(x);
			}
		}
		for(int x = 0; x<18-assign*contenders.size(); x++){
			temprandom.add(-1);
		}
		for(int x:order){
			int tempind = (int)(Math.random()*temprandom.size());
			String tempimage1 = "Images/Territories/"+"G"+(x+1)+".png";
			String tempimage2 = "Images/Territories/"+"R"+(x+1)+".png";
			String tempimage3 = "Images/Territories/"+"Gr"+(x+1)+".png";
			String tempimage4 = "Images/Territories/"+"B"+(x+1)+".png";
			String tempimage5 = "Images/Territories/"+"P"+(x+1)+".png";
			String tempimage6 = "Images/Territories/"+"W"+(x+1)+".png";
			territories.add(new Territory(this, positionsx[x], positionsy[x], sx[x], sy[x],  tempimage1, 
					tempimage2, tempimage3, tempimage4, tempimage5, tempimage6, 
					temprandom.get(tempind) == -1?null:contenders.get(temprandom.get(tempind)), x));
			if(temprandom.get(tempind) == -1){
				temprandom.remove(tempind);
				continue;
			}
			else if(contenders.get(temprandom.get(tempind)) instanceof Player){
				((Player) contenders.get(temprandom.get(tempind))).numterritories++;
			}
			else if(contenders.get(temprandom.get(tempind)) instanceof Computer){
				((Computer) contenders.get(temprandom.get(tempind))).numterritories++;
			}
			temprandom.remove(tempind);
		}
		adjmatrix = new boolean[18][18];
		ArrayList<List<Integer>> adjacency = new ArrayList<List<Integer>>(); //manually initializes the adjacency array
		adjacency.add((List<Integer>) Arrays.asList(8,9,10,2));  adjacency.add((List<Integer>) Arrays.asList(1,10,17,18,3)); adjacency.add((List<Integer>) Arrays.asList(2,18,4));
		adjacency.add((List<Integer>) Arrays.asList(3,18,16,15,5));  adjacency.add((List<Integer>) Arrays.asList(4,15,6)); adjacency.add((List<Integer>) Arrays.asList(5, 15, 14,12,13,7));
		adjacency.add((List<Integer>) Arrays.asList(6,13,8));  adjacency.add((List<Integer>) Arrays.asList(7,13,9,1)); adjacency.add((List<Integer>) Arrays.asList(8,13,11,10,1));
		adjacency.add((List<Integer>) Arrays.asList(9,11,17,2,1));  adjacency.add((List<Integer>) Arrays.asList(9,13,12,17,10)); adjacency.add((List<Integer>) Arrays.asList(13,6,14,15,16,17,11));
		adjacency.add((List<Integer>) Arrays.asList(7,6,12,11,9,8));  adjacency.add((List<Integer>) Arrays.asList(6,15,12)); adjacency.add((List<Integer>) Arrays.asList(6,5,4,16,12,14));
		adjacency.add((List<Integer>) Arrays.asList(12,15,4,18,17));  adjacency.add((List<Integer>) Arrays.asList(18,12,16,8,2,10,11)); adjacency.add((List<Integer>) Arrays.asList(2,3,4,16,17));
		int tempind = 0;
		for(List<Integer> k :adjacency){
			for(int l: k){
				territories.get(tempind).adjacent.add(territories.get(backorder[l-1])); //adds the territory of the position of the list of the territory number
				adjmatrix[tempind][l-1] = true;
			}
			tempind++;
		}
	}

	public void generatePlayers() {
		// TODO Auto-generated method stub
		contenders = new ArrayList<Contender>();
		for(int x = 0; x<numplayers; x++){
			contenders.add(new Player(x+1, "Images/Territories/G0.png", "Images/Territories/R0.png", "Images/Territories/Gr0.png", "Images/Territories/B0.png", "Images/Territories/P0.png"));
		}
		for(int x = 0; x<numcomputers; x++){
			contenders.add(new Computer(x+numplayers+1, "Images/Territories/G0.png", "Images/Territories/R0.png", "Images/Territories/Gr0.png", "Images/Territories/B0.png", "Images/Territories/P0.png", this));
		}
		currentPlayer = contenders.get(currentindex);
	}

}
