
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;



public class GraphicsPanel extends JPanel implements MouseMotionListener{
	Graphics gra;
	GraphicsFrame gf;
	int screen, begindelaycount = 0;
	final int WIDTH = 1200, HEIGHT = 800;// starting values for width and height in pixels
	private Random die = new Random();// I wanted randomness (specifically for colors)
	GameBoard gb;
	Territory newclicked;
	JLabel goldvalue = new JLabel();
	Clip mclip;
	private Territory clickedOnceTerritory = null;
	private boolean riskCheck = false, fire1 = false, fire2 = false, fire3 = false, bombcheck = false, turncheck = false;
	private boolean startScreen = true, transition = false, checkdir = false, gcheck = false, statcheck = false, xiconcheck = false;
	private int flamecount = 0, buttonnumber = 0;
	private int cb1x = 167, cb2x = 607, g1index = -1, g2index = 2;
	int ta, tb, tc, td;
	private int[] cb1values, cb2values, c1values, c2values;
	private long starttime, diff, ogdiff;
	private double transplanex = 0, alpha = 0, orgtrans = 0, deployorgt = 0;
	boolean playerInput = false, delaycheck = false, tcheck = false, lastcheck = false, backcheck = false, mousecheck = false, comp = false;
	private URL airplane, startmusic;
	double originaltime = 0;
	URL music;
	private Image background, title, explosion2, explosion, plane_white, player, computer;
	private Image border, plane, bomb, tank1, tank2, button, stats1rect, stats2rect;
	private Image blackRectangle, creators, button2, startbefore, rulesbefore, startafter, rulesafter;
	private Image creditsbefore, creditsafter,  loading_icon, rulestitle, backbefore, backafter;
	private Image nextbefore, nextafter, mainbefore, mainafter, creditstitle, players, computers;
	private Image deploy, grayrect, roundedsquare, button3, button5, border2, back, deployb;
	private Image num0, num1, num2, num3, num4, num5, num6, num7, num8, num9, crosshair, store;
	private Image orect, startga, chooseborder, roundedsquaregrayed, endturn, end1, end2;
	private Image rulesex1, rulesex2, statistics, attack, defense, infantry, wins, inventory, riskcred;
	private Image goldcoins, sword, arrow, versus, xicon, maingray, computerwhite, playerwhite;
	private Image tank1b, infantryb, bomb1, bomb1b, aupgrade, dupgrade, buy, backa, cancel;
	private boolean crosshaircheck, drawarrow, storecheck = false;
	private int pstartx, tstartx, numclicked = 0, mouseix, mouseiy, cancelcheck = 0;
	private Territory[] clicked;
	private List<Integer> startbombsx, startbombsy, numberwidths, costvalues;
	private List<Image> tempdraw, numbers, wnumbers;
	MasterTimer mt;
	private int mousex, mousey, mousemode, buynumber = 0, deployn = 0, buttondeploy = 0, deploytroop = 0;
	Timer startAnimation, flameAnimation, transTimer, temp, delayTimer;
	Territory mousegetTerritory, og;
	
	private void reset() {
		// TODO Auto-generated method stub
		turncheck = false;
		mousecheck = false;
		backcheck = false;
		transition = false;
		checkdir = false;
		gcheck = false;
		statcheck = false;
		xiconcheck = false;
		storecheck = false;
		comp = false;
		numclicked = 0;
		deploytroop = 0;
		cancelcheck = 0;
		orgtrans = 0;
		gb.eliminated = 0;
		deployorgt = 0;
		numclicked = 0;
		buynumber = 0;
		deployn = 0;
		clicked[0] = null; clicked[1] = null;
		screen = 1;
		URL tempsound = getClass().getResource("/Sounds/StartMusic2.wav");
		playSound(tempsound);
	}
	
	public GraphicsPanel(GraphicsFrame frame) {
		this.setLayout(null);

	      this.addMouseMotionListener(this);
		gf = frame;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));// Otherwise, the frame starts off small
		mt = new MasterTimer(this);
		gb = new GameBoard(this, mt); // fix with the constructor game board
		addMouseListener();
		initializeArrays();
		setUpBackground();
		initializeDraw();
		pstartx = -296; tstartx = 1200;
	   // setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));		
		clicked =  new Territory[2]; clicked[0] = null; clicked[1] = null;
		startAnimation = new Timer(2, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				diff = System.currentTimeMillis()-starttime;
				if(diff<=6900){
					alpha = 1;
					alpha =  ((double)diff/6900);
					ogdiff = diff;
					repaint();
					return;
				}
				else{
					startScreen = false;
				}
	    		if(diff-ogdiff>=8 && !startScreen){
	    			ogdiff = diff;
	    			tstartx--;
	    		}
	    		fire1 = abs(tstartx)%50 <= 20 && tstartx<=500 ? true:false;
	    		fire2 = abs(tstartx)%60 <= 25 && tstartx<=500 ? true:false;
	    		fire3 = abs(tstartx)%80 <= 34 && tstartx<=500 ? true:false;
	    		if(diff >= 20000){
	    			pstartx++;
	    		}
	    		if(pstartx>=525 && pstartx%120 == 10){
	    			startbombsx.add(pstartx);
	    			startbombsy.add(50);
	    		}
	    		if(diff >= 33400){
	    			riskCheck = true;
	    			startAnimation.stop();
	    			flameAnimation.start();
	    		}
	    		repaint();
			}

			private int abs(int i) {
				return i<0 ? -i : i;
			}
		});
		startAnimation.start();
		flameAnimation = new Timer(2, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				flamecount++;
				if(flamecount == 1200){
					//flameAnimation.stop();
				}
	    		repaint();
			}

		});
		//shortcut();
	}
	
	private void initializeDraw() {
		// TODO Auto-generated method stub
		tempdraw = new ArrayList<Image>();
		for(int x = 1; x<=18; x++){
			tempdraw.add(new ImageIcon(getClass().getResource("Images/Territories/"+"G"+(x)+".png")).getImage());
		}
	}
	
	private void initializeArrays(){
		cb1values = new int[4]; c1values = new int[4];
		cb2values = new int[5]; c2values = new int[5];
		cb1values[0] = 52;cb1values[1] = 167;cb1values[2] = 282;cb1values[3] = 397;
		cb2values[0] = 722;cb2values[1] = 837;cb2values[2] = 952;cb2values[3] = 1062;cb2values[4] = 607;
		for(int x = 0; x<c1values.length; x++){
			c1values[x] = cb1values[x]+8;
		}
		for(int x = 0; x<2; x++){
			c2values[x] = cb2values[x]+8;
		}
		for(int x = 2; x<4; x++){
			c2values[x] = cb2values[x];
		}
		c2values[4] = cb2values[4]+9;
		numbers = new ArrayList<Image>();
		wnumbers = new ArrayList<Image>();
		numberwidths = new ArrayList<Integer>();
		startbombsx = new ArrayList<Integer>();
		startbombsy = new ArrayList<Integer>();	
		costvalues = new ArrayList<Integer>();
		costvalues.add(50); costvalues.add(100); costvalues.add(200); costvalues.add(400); costvalues.add(400);
	}

	private void setUpBackground() {
		//before colors: #FFA126 #420B0B (orange), (red to black tint)
		//after colors: #8F5913 #420B0B (orange to black tint), (red to black tint)
		background = new ImageIcon(getClass().getResource("Images/Background7.jpg")).getImage();
		title = new ImageIcon(getClass().getResource("Images/Title6.png")).getImage();
		border = new ImageIcon(getClass().getResource("Images/Border3.jpg")).getImage();
		border2 = new ImageIcon(getClass().getResource("Images/border4.jpg")).getImage();
		plane = new ImageIcon(getClass().getResource("Images/WarPlane.png")).getImage();
		bomb = new ImageIcon(getClass().getResource("Images/Bomb.gif")).getImage();
		bomb1 = new ImageIcon(getClass().getResource("Images/Bomb1.png")).getImage();
		bomb1b = new ImageIcon(getClass().getResource("Images/Bomb1b.png")).getImage();
		store = new ImageIcon(getClass().getResource("Images/Store.png")).getImage();
		explosion2 = new ImageIcon(getClass().getResource("Images/Fire_Animated.gif")).getImage();
		explosion = new ImageIcon(getClass().getResource("Images/Explosion.png")).getImage();
		tank1 = new ImageIcon(getClass().getResource("Images/Tank1.png")).getImage();
		tank1b = new ImageIcon(getClass().getResource("Images/Tank1b.png")).getImage();
		tank2 = new ImageIcon(getClass().getResource("Images/Tank2.png")).getImage();
		button = new ImageIcon(getClass().getResource("Images/Button.png")).getImage();
		button2 = new ImageIcon(getClass().getResource("Images/Button2.png")).getImage();
		button3 = new ImageIcon(getClass().getResource("Images/Button3.png")).getImage();
		button5 = new ImageIcon(getClass().getResource("Images/Button5.png")).getImage();
		stats1rect = new ImageIcon(getClass().getResource("Images/RedRect.png")).getImage();
		stats2rect = new ImageIcon(getClass().getResource("Images/BlueRect.jpg")).getImage();
		chooseborder = new ImageIcon(getClass().getResource("Images/ChooseBorder.gif")).getImage();
		blackRectangle = new ImageIcon(getClass().getResource("Images/BlackRectangle.png")).getImage();
		creators = new ImageIcon(getClass().getResource("Images/Creators.png")).getImage();
		startafter = new ImageIcon(getClass().getResource("Images/StartAfter.png")).getImage();
		startbefore = new ImageIcon(getClass().getResource("Images/StartBefore.png")).getImage();
		orect = new ImageIcon(getClass().getResource("Images/Orect.png")).getImage();
		startga = new ImageIcon(getClass().getResource("Images/startga.png")).getImage();
		rulesbefore = new ImageIcon(getClass().getResource("Images/RulesBefore.png")).getImage();
		rulesafter = new ImageIcon(getClass().getResource("Images/RulesAfter.png")).getImage();
		creditsbefore = new ImageIcon(getClass().getResource("Images/CreditsBefore.png")).getImage();
		creditsafter = new ImageIcon(getClass().getResource("Images/CreditsAfter.png")).getImage();
		back = new ImageIcon(getClass().getResource("Images/Back.png")).getImage();
		backa = new ImageIcon(getClass().getResource("Images/backa.png")).getImage();
		backbefore = new ImageIcon(getClass().getResource("Images/BackBefore.png")).getImage();
		backafter = new ImageIcon(getClass().getResource("Images/BackAfter.png")).getImage();
		nextbefore = new ImageIcon(getClass().getResource("Images/NextBefore.png")).getImage();
		nextafter = new ImageIcon(getClass().getResource("Images/NextAfter.png")).getImage();
		mainbefore = new ImageIcon(getClass().getResource("Images/MainBefore.png")).getImage();
		mainafter = new ImageIcon(getClass().getResource("Images/MainAfter.png")).getImage();
		maingray = new ImageIcon(getClass().getResource("Images/MainGray.png")).getImage();
		rulestitle =  new ImageIcon(getClass().getResource("Images/RulesTitle.png")).getImage();
		loading_icon =  new ImageIcon(getClass().getResource("Images/Loading_icon.gif")).getImage();
		creditstitle = new ImageIcon(getClass().getResource("Images/CreditsTitle.png")).getImage();
		aupgrade =  new ImageIcon(getClass().getResource("Images/AUpgrade.png")).getImage();
		dupgrade =  new ImageIcon(getClass().getResource("Images/DUpgrade.png")).getImage();
		arrow =  new ImageIcon(getClass().getResource("Images/Arrow.png")).getImage();
		infantry = new ImageIcon(getClass().getResource("Images/Infantry.png")).getImage();
		infantryb = new ImageIcon(getClass().getResource("Images/Infantryb.png")).getImage();
		players = new ImageIcon(getClass().getResource("Images/Players.png")).getImage();		
		xicon = new ImageIcon(getClass().getResource("Images/xicon.png")).getImage();
		crosshair = new ImageIcon(getClass().getResource("Images/Crosshair_Black.png")).getImage();
		sword = new ImageIcon(getClass().getResource("Images/sword.png")).getImage();
		computers = new ImageIcon(getClass().getResource("Images/Computers.png")).getImage();
		player = new ImageIcon(getClass().getResource("Images/Player.png")).getImage();
		computer = new ImageIcon(getClass().getResource("Images/Computer.png")).getImage();
		deploy = new ImageIcon(getClass().getResource("Images/Deploy.png")).getImage();
		goldcoins = new ImageIcon(getClass().getResource("Images/GoldCoins.png")).getImage();
		cancel = new ImageIcon(getClass().getResource("Images/Cancel.png")).getImage();
		attack = new ImageIcon(getClass().getResource("Images/Attack.png")).getImage();
		defense = new ImageIcon(getClass().getResource("Images/Defense.png")).getImage();
		grayrect = new ImageIcon(getClass().getResource("Images/gray_rectangle.png")).getImage();
		computerwhite = new ImageIcon(getClass().getResource("Images/ComputerWhite.png")).getImage();
		playerwhite = new ImageIcon(getClass().getResource("Images/PlayerWhite.png")).getImage();
		wins = new ImageIcon(getClass().getResource("Images/wins.png")).getImage();
		roundedsquare = new ImageIcon(getClass().getResource("Images/RoundedSquare.png")).getImage();
		roundedsquaregrayed = new ImageIcon(getClass().getResource("Images/RoundedSquareGrayed.png")).getImage();
		statistics = new ImageIcon(getClass().getResource("Images/Statistics.png")).getImage();
		endturn = new ImageIcon(getClass().getResource("Images/end.png")).getImage();
		end1 = new ImageIcon(getClass().getResource("Images/end1.png")).getImage();
		end2 = new ImageIcon(getClass().getResource("Images/end2.png")).getImage();
		versus = new ImageIcon(getClass().getResource("Images/Versus.png")).getImage();
		inventory =  new ImageIcon(getClass().getResource("Images/Inventory.png")).getImage();
		deployb =  new ImageIcon(getClass().getResource("Images/Deployb.png")).getImage();
		buy =  new ImageIcon(getClass().getResource("Images/Buy.png")).getImage();
		num0 = new ImageIcon(getClass().getResource("Images/Numbers/num0.png")).getImage(); numbers.add(num0); numberwidths.add(31);
		num1 = new ImageIcon(getClass().getResource("Images/Numbers/num1.png")).getImage(); numbers.add(num1); numberwidths.add(23);
		num2 = new ImageIcon(getClass().getResource("Images/Numbers/num2.png")).getImage(); numbers.add(num2); numberwidths.add(29);
	 	num3 = new ImageIcon(getClass().getResource("Images/Numbers/num3.png")).getImage(); numbers.add(num3); numberwidths.add(21);
		num4 = new ImageIcon(getClass().getResource("Images/Numbers/num4.png")).getImage(); numbers.add(num4); numberwidths.add(28);
		num5 = new ImageIcon(getClass().getResource("Images/Numbers/num5.png")).getImage(); numbers.add(num5); numberwidths.add(23);
		num6 = new ImageIcon(getClass().getResource("Images/Numbers/num6.png")).getImage(); numbers.add(num6); numberwidths.add(25);
		num7 = new ImageIcon(getClass().getResource("Images/Numbers/num7.png")).getImage(); numbers.add(num7); numberwidths.add(26);
	 	num8 = new ImageIcon(getClass().getResource("Images/Numbers/num8.png")).getImage(); numbers.add(num8); numberwidths.add(25);
		num9 = new ImageIcon(getClass().getResource("Images/Numbers/num9.png")).getImage(); numbers.add(num9); numberwidths.add(26);
		wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n0.png")).getImage()); wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n1.png")).getImage());
		wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n2.png")).getImage()); wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n3.png")).getImage());
		wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n4.png")).getImage()); wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n5.png")).getImage());
		wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n6.png")).getImage()); wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n7.png")).getImage());
		wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n8.png")).getImage()); wnumbers.add(new ImageIcon(getClass().getResource("Images/Numbers/n9.png")).getImage());
		rulesex1 = new ImageIcon(getClass().getResource("Images/RulesFinal1.JPG")).getImage();
		rulesex2 = new ImageIcon(getClass().getResource("Images/RulesFinal2.JPG")).getImage();
		riskcred = new ImageIcon(getClass().getResource("Images/RulesCred.JPG")).getImage();
		airplane = getClass().getResource("/Sounds/Airplane.wav");
		startmusic = getClass().getResource("/Sounds/StartMusic.wav");
		music = startmusic;
		playSound(startmusic);
		/*airplane = getClass().getResource("/Sounds/Airplane.wav");
		startmusic = getClass().getResource("/Sounds/StartMusic.wav");
		music = startmusic;
		File temp = new File("/Sounds/StartMusic.wav");
		playSound(temp);*/
		starttime = System.currentTimeMillis();
	}
	
	public void shortcut(){
		pstartx = 2250;
		tstartx = 500;
		riskCheck = true;
		bombcheck = true;
		startScreen = false;
	    this.setLayout(null);
		flameAnimation.start();
	}
	
	
	private void playSound(URL url){
		try {
			mclip = AudioSystem.getClip(null);
			mclip.open(AudioSystem.getAudioInputStream(url));
			mclip.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void lowerVolume(URL url){
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-28f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playTransition(boolean dir){
		transition = true;
		checkdir = dir;      //global variable for timer
		transplanex = dir ? -240 : 1200;
		plane_white = dir ? new ImageIcon(getClass().getResource("Images/Plane_White.png")).getImage()
				          : new ImageIcon(getClass().getResource("Images/Plane_White2.png")).getImage();
		loading_icon.flush();
		orgtrans = System.currentTimeMillis();
		transTimer = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-orgtrans>=3){
					orgtrans = System.currentTimeMillis();
					transplanex+= checkdir ? 3.5 : -3.5;
				}
				if(checkdir && transplanex >= WIDTH+600){
					transition = false;
					transTimer.stop();
				}
				if(!checkdir && transplanex <= -600){
					transition = false;
					transTimer.stop();
				}
		    	repaint();
			}

		}); transTimer.start();
	}
	
	private void addMouseListener(){
		this.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		    	if(screen == 1 && riskCheck == true){
		    		if((e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=170 && e.getY()<=245)){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    		else if((e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=310 && e.getY()<=385)){
		    			buttonnumber = 2;
		    			repaint();
		    		}
		    		else if((e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=450 && e.getY()<=525)){
		    			buttonnumber = 3;
		    			repaint();
		    		}
		    	}
		    	else if(screen == 6){
		    		if((e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=500 && e.getY()<=575)){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    		for(int x = 0; x<cb1values.length; x++){
		    			if((e.getX()>=cb1values[x]+8 && e.getX()<=cb1values[x]+58) &&
			    				(e.getY()>=300 && e.getY()<=350)){
		    				if((x>=g1index && g1index!=-1) || (g1index == -1 && x == 0)){
		    					return;
		    				}
		    				gcheck = x == 0?true:false;
			    			gb.numplayers = x+1;
			    			cb1x = cb1values[x];
			    			g2index = 3-x;
			    			repaint();
			    		}
		    		}
		    		for(int x = 0; x<cb2values.length; x++){
		    			if((e.getX()>=cb2values[x]+8 && e.getX()<=cb2values[x]+58) &&
			    				(e.getY()>=300 && e.getY()<=350)){
		    				if((x>=g2index && x!=4) || (x == 4 && g2index==0)){
		    					return;
		    				}
			    			cb2x = x != 2?cb2values[x]:cb2values[x]-8;
			    			g1index = 3-x;
			    			gb.numcomputers = x == 4?0:x+1;
			    			repaint();
			    		}
		    		}
		    	}
		    	else if(screen == 2){
		    		buttonnumber = 0;
		    		if(mt.deployvar == 1 || mt.deployvar == 3 || mt.dcheck){
		    			return;
		    		}
		    		if(mt.deploywidth == 350){
		    			if(deployn == 0 && e.getX()>=WIDTH+80-mt.deploywidth && e.getX()<=WIDTH+190-mt.deploywidth
		    				&& e.getY()>=706 && e.getY()<=746){
		    				storecheck = true;
		    			}
		    			else if(e.getX()>=WIDTH+140-(mt.finaldeploy-30) && e.getX()<=WIDTH+260-(mt.finaldeploy-30)
			    				&& e.getY()>=230 && e.getY()<=280){
		    				if(deployn != 0){
		    					cancelcheck = 1;
		    				}
		    				else if (gb.currentPlayer.infantrynum>0){
		    					deployn = 1;
		    					buttondeploy = 1;
		    				}
		    				repaint();
		    			}
		    			else if(e.getX()>=WIDTH+140-(mt.finaldeploy-30) && e.getX()<=WIDTH+260-(mt.finaldeploy-30)
			    				&& e.getY()>=410 && e.getY()<=460){
		    				if(deployn != 0){
		    					cancelcheck = 2;
		    				}
		    				else if (gb.currentPlayer.tanknum>0){
		    					deployn = 2;
		    					buttondeploy = 2;
		    				}
		    				repaint();
		    			}
		    			else if(e.getX()>=WIDTH+140-(mt.finaldeploy-30) && e.getX()<=WIDTH+260-(mt.finaldeploy-30)
			    				&& e.getY()>=588 && e.getY()<=638){
		    				if(deployn != 0){
		    					cancelcheck = 3;
		    				}
		    				else if (gb.currentPlayer.bombnum>0){
		    					deployn = 3;
		    					buttondeploy = 3;
		    				}
		    				repaint();
		    			}
		    		}
		    		if(e.getX()>=1021 && e.getX()<=1095 &&
		    				e.getY()>=678 && e.getY()<=712){
		    			turncheck = true;
		    			repaint();
		    		}
		    		if((e.getX()>=WIDTH-80 && e.getX()<=WIDTH-50) &&
		    				(e.getY()>=120 && e.getY()<=870) && mt.deployvar == 0){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    		else if(mt.deployvar == 2 && (e.getX()>=50+WIDTH-130-(mt.finaldeploy-30) && e.getX()<=50+WIDTH-130-(mt.finaldeploy-60)) &&
		    				(e.getY()>=120 && e.getY()<=870)){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    		else{
		    			clickedOnceTerritory = null;
		    			for(Territory t: gb.territories){
		    				t.clickedOnce = false;
		    				if((e.getX()>=t.x && e.getX()<t.x+t.sizex) &&
		    						(e.getY()>=t.y && e.getY()<t.y+t.sizey)
		    						&& !t.isTransparent(e.getX()-t.x, e.getY()-t.y)
		    						&& !t.isBlack(e.getX()-t.x, e.getY()-t.y)){
		    					t.clickedOnce = true;
		    					clickedOnceTerritory = t;
		    				}
		    			}
		    		}
		    	}	
		    	else if(screen == 3){
		    		if((e.getX()>=20 && e.getX()<=190) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    		else if((e.getX()>=WIDTH-190 && e.getX()<=WIDTH-20) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			buttonnumber = 2;
		    			repaint();
		    		}
		    	}
		    	else if(screen == 4){
		    		if((e.getX()>=20 && e.getX()<=190) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    		else if((e.getX()>=WIDTH-190 && e.getX()<=WIDTH-20) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			buttonnumber = 2;
		    			repaint();
		    		}
		    	}
		    	else if(screen == 5){
		    		if((e.getX()>=WIDTH-190 && e.getX()<=WIDTH-20) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			buttonnumber = 1;
		    			repaint();
		    		}
		    	}
		    	else if(screen == 7){
		    		if((e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=HEIGHT-450 && e.getY()<=525)){
		    			lastcheck = true;
		    			repaint();
		    		}
		    	}			
		    	else if(screen == 8){
		    		if((e.getX()>=1020 && e.getX()<=1140) &&
		    				(e.getY()>=130 && e.getY()<=180)){
		    			backcheck = true;
		    			repaint();
		    		}
		    		else if((e.getX()>=470 && e.getX()<=590) &&
		    				(e.getY()>=210 && e.getY()<=260) && gb.currentPlayer.gold>=costvalues.get(0)){
		    			buynumber = 1;
		    			repaint();
		    		}
		    		else if((e.getX()>=470 && e.getX()<=590) &&
		    				(e.getY()>=410 && e.getY()<=460) && gb.currentPlayer.gold>=costvalues.get(1)){
		    			buynumber = 2;
		    			repaint();
		    		}
		    		else if((e.getX()>=470 && e.getX()<=590) &&
		    				(e.getY()>615 && e.getY()<=665) && gb.currentPlayer.gold>=costvalues.get(2)){
		    			buynumber = 3;
		    			repaint();
		    		}
		    		else if((e.getX()>=1020 && e.getX()<=1140) &&
		    				(e.getY()>=210 && e.getY()<=260) && gb.currentPlayer.gold>=costvalues.get(3)){
		    			buynumber = 4;
		    			repaint();
		    		}
		    		else if((e.getX()>=1020 && e.getX()<=1140) &&
		    				(e.getY()>=420 && e.getY()<=470) && gb.currentPlayer.gold>=costvalues.get(4)){
		    			buynumber = 5;
		    			repaint();
		    		}
		    	}
		    }
		    public void mouseReleased(MouseEvent e) {
		    	if(screen == 1 && riskCheck == true){
		    		if(buttonnumber == 1 && (e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=170 && e.getY()<=245)){
		    			screen = 6;
		    			playTransition(true);
		    			playerInput = true;
		    			buttonnumber = 0;
		    			mclip.stop();
		    			repaint();
		    		}
		    		else if(buttonnumber == 2 && (e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=310 && e.getY()<=385)){
		    			screen = 3;
		    			playTransition(true);
		    			repaint();
		    		}
		    		else if(buttonnumber == 3 && (e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=450 && e.getY()<=525)){
		    			screen = 5;
		    			playTransition(true);
		    			repaint();
		    		}
		    		buttonnumber = 0;
		    	}
		    	else if(screen == 6){
		    		if(buttonnumber == 1 && (e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=500 && e.getY()<=575)){
		    			screen = 2;
		    			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));
		    			mousemode = 2;
		    			repaint();
		    			gb.start();
		    			mt.delay(100);
		    			goldvalue.setBounds(880, 662, 300, 35);
		    			goldvalue.setFont(new Font(goldvalue.getFont().getName(), Font.PLAIN, 28));
		    			//gb.gp.add(goldvalue);
		    		}
		    		buttonnumber = 0;
		    	}
		    	else if(screen == 2){
		    		if(mt.deployvar == 1 || mt.deployvar == 3 || mt.dcheck || gb.currentPlayer instanceof Computer){
		    			return;
		    		}		
		    		if(mt.deploywidth == 350){
		    			if(storecheck && e.getX()>=WIDTH+80-mt.deploywidth && e.getX()<=WIDTH+190-mt.deploywidth
		    				&& e.getY()>=706 && e.getY()<=746){
		    				screen = 8;
		    				repaint();
		    			}
		    			else if(e.getX()>=WIDTH+140-(mt.finaldeploy-30) && e.getX()<=WIDTH+260-(mt.finaldeploy-30)
			    				&& e.getY()>=230 && e.getY()<=280){
		    				if(cancelcheck == 0 && gb.currentPlayer.infantrynum>0){
		    					gb.currentPlayer.infantrynum--;
		    					mt.deployScreen(false);
		    					deploytroop = 1;
		    				}
		    				else if(deployn == 1){
		    					gb.currentPlayer.infantrynum++;
		    					deployn = 0;
		    					deploytroop = 0;
		    				}
		    				repaint();
		    			}
		    			else if(e.getX()>=WIDTH+140-(mt.finaldeploy-30) && e.getX()<=WIDTH+260-(mt.finaldeploy-30)
			    				&& e.getY()>=410 && e.getY()<=460){
		    				if(cancelcheck == 0 && gb.currentPlayer.tanknum>0){
		    					gb.currentPlayer.tanknum--;
		    					mt.deployScreen(false);
		    					deploytroop = 2;
		    				}
		    				else if(deployn == 2){
		    					gb.currentPlayer.tanknum++;
		    					deployn = 0;
		    					deploytroop = 0;
		    				}
		    				repaint();
		    			}
		    			else if(e.getX()>=WIDTH+140-(mt.finaldeploy-30) && e.getX()<=WIDTH+260-(mt.finaldeploy-30)
			    				&& e.getY()>=588 && e.getY()<=638){
		    				if(cancelcheck == 0 && gb.currentPlayer.bombnum>0){
		    					gb.currentPlayer.bombnum--;
		    					mt.deployScreen(false);
		    					deploytroop = 3;
		    				}
		    				else if(deployn == 3){
		    					gb.currentPlayer.bombnum++;
		    					deployn = 0;
		    					deploytroop = 0;
		    				}
		    				repaint();
		    			}
	    				buttondeploy = 0;
	    				cancelcheck = 0;
		    			storecheck = false;
		    		}
		    		if(buttonnumber == 1 &&(e.getX()>=WIDTH-80 && e.getX()<=WIDTH-50) &&
		    				(e.getY()>=120 && e.getY()<=870) && mt.deployvar == 0){
		    			mt.deployScreen(true);
		    			repaint();
		    		}
		    		else if(mt.deployvar == 2 && (e.getX()>=50+WIDTH-130-(mt.finaldeploy-30) && e.getX()<=50+WIDTH-130-(mt.finaldeploy-60)) &&
		    				(e.getY()>=120 && e.getY()<=870) && buttonnumber == 1){
		    			mt.deployScreen(false);
		    			repaint();
		    		}
		    		if(e.getX()>=1021 && e.getX()<=1095 &&
		    				e.getY()>=678 && e.getY()<=712){
		    			turncheck = false;
		    			gb.next();
		    			repaint();
		    		}
		    		if(clickedOnceTerritory != null){
		    			if((e.getX()>=clickedOnceTerritory.x && e.getX()<clickedOnceTerritory.x+clickedOnceTerritory.sizex) &&
		    					(e.getY()>=clickedOnceTerritory.y && e.getY()<clickedOnceTerritory.y+clickedOnceTerritory.sizey)
		    					&& !clickedOnceTerritory.isTransparent(e.getX()-clickedOnceTerritory.x, e.getY()-clickedOnceTerritory.y)){
		    				if(((deployn == 1 || deployn == 2) && gb.currentPlayer == clickedOnceTerritory.contender) || (deployn == 3&& gb.currentPlayer != clickedOnceTerritory.contender)){
		    					clickedOnceTerritory.attack += (int) (deployn == 1?36:deployn == 2?31:-clickedOnceTerritory.attack);
	    						clickedOnceTerritory.defense += (int) (deployn == 1?0:deployn == 2?31:-clickedOnceTerritory.defense);
	    						if(clickedOnceTerritory.attack>clickedOnceTerritory.maxattack){
	    							clickedOnceTerritory.attack = clickedOnceTerritory.maxattack;
	    						}
	    						if(clickedOnceTerritory.defense>clickedOnceTerritory.maxdefense){
	    							clickedOnceTerritory.defense = clickedOnceTerritory.maxdefense;
	    						}
	    						deployn = 0;
	    						deploytroop = 0;
		    					//mt.deployScreen(true);
		    				}
		    				else if(clickedOnceTerritory.contender == gb.currentPlayer && clicked[0]!=clickedOnceTerritory && clicked[0] == null){
		    					 if(clickedOnceTerritory.attack == 0){
		    						 return;
		    					 }
		    					mouseix = e.getX();
		    					mouseiy = e.getY();
		    					drawarrow = true;
		    					numclicked++;
		    					clicked[0] = clickedOnceTerritory;
		    					clickedOnceTerritory.updateClicked();
	    						if(clicked[1]!=null && clicked[0]!= null){
	    							gb.attack(clicked);
	    							clicked[0].updateClicked(); clicked[1].updateClicked();
	    							clicked[0] = null; clicked[1] = null;
		    						//gb.next();
		    						repaint();
	    						}
		    				}
		    				else if (clickedOnceTerritory.contender != gb.currentPlayer  && clicked[1]!=clickedOnceTerritory && clicked[0] != null){
		    					if(!gb.isAdjacent(clicked[0], clickedOnceTerritory)){
		    						return;
		    					}
		    					numclicked++;
		    					clicked[1] = clickedOnceTerritory;
		    					clickedOnceTerritory.updateClicked();
		    					drawarrow = false;
	    						if(clicked[1]!=null && clicked[0]!= null){
	    							gb.attack(clicked);
	    							clicked[0].updateClicked(); clicked[1].updateClicked();
	    							clicked[0] = null; clicked[1] = null;
		    						//gb.next(); 
		    						repaint();
	    						}
		    				}
		    				else if(clickedOnceTerritory.contender == gb.currentPlayer && clicked[0]==clickedOnceTerritory){
		    					clicked[0] = null;
		    					drawarrow = false;
		    					clickedOnceTerritory.updateClicked();
		    					numclicked--;
		    				}
		    			}
		    		}
		    	}
		    	else if(screen == 3){
		    		if(buttonnumber == 1 && (e.getX()>=20 && e.getX()<=190) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			screen = 1;
		    			playTransition(false);
		    			repaint();
		    		}
		    		else if(buttonnumber == 2 &&(e.getX()>=WIDTH-190 && e.getX()<=WIDTH-20) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			screen = 4;
		    			repaint();
		    		}
		    		buttonnumber = 0;
		    	}
		    	else if(screen == 4){
		    		if(buttonnumber == 1 && (e.getX()>=20 && e.getX()<=190) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			screen = 3;
		    			repaint();
		    		}
		    		else if(buttonnumber == 2 && (e.getX()>=WIDTH-190 && e.getX()<=WIDTH-20) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			screen = 1;
		    			playTransition(false);
		    			repaint();
		    		}
		    		buttonnumber = 0;
		    	}
		    	else if(screen == 5){
		    		if(buttonnumber == 1 && (e.getX()>=WIDTH-190 && e.getX()<=WIDTH-20) &&
		    				(e.getY()>=HEIGHT-95 && e.getY()<=HEIGHT-20)){
		    			screen = 1;
		    			playTransition(false);
		    			repaint();
		    		}
		    		buttonnumber = 0;
		    	}
		    	else if(screen == 7){
		    		if(lastcheck && (e.getX()>=WIDTH/2-85 && e.getX()<=WIDTH/2+85) &&
		    				(e.getY()>=HEIGHT-450 && e.getY()<=525)){
		    			reset();
		    		}
	    			lastcheck = false;
		    	}			
		    	else if(screen == 8){
		    		if(backcheck && (e.getX()>=1020 && e.getX()<=1140) &&
		    				(e.getY()>=130 && e.getY()<=180)){
		    			screen = 2;
		    			repaint();
		    		}
		    		else if(buynumber == 1 && (e.getX()>=470 && e.getX()<=590) &&
		    				(e.getY()>=210 && e.getY()<=260)){
		    			gb.currentPlayer.gold-=costvalues.get(0);
		    			gb.currentPlayer.infantrynum++;
		    			repaint();
		    		}
		    		else if(buynumber == 2 && (e.getX()>=470 && e.getX()<=590) &&
		    				(e.getY()>=410 && e.getY()<=460)){
		    			gb.currentPlayer.gold-=costvalues.get(1);
		    			gb.currentPlayer.tanknum++;
		    			repaint();
		    		}
		    		else if(buynumber == 3 && (e.getX()>=470 && e.getX()<=590) &&
		    				(e.getY()>615 && e.getY()<=665)){
		    			gb.currentPlayer.gold-=costvalues.get(2);
		    			gb.currentPlayer.bombnum++;
		    			repaint();
		    		}
		    		else if(buynumber == 4 && (e.getX()>=1020 && e.getX()<=1140) &&
		    				(e.getY()>=210 && e.getY()<=260)){
		    			gb.currentPlayer.gold-=costvalues.get(3);
		    			gb.currentPlayer.aupgradenum++;
		    			repaint();
		    		}
		    		else if(buynumber == 5 && (e.getX()>=1020 && e.getX()<=1140) &&
		    				(e.getY()>=420 && e.getY()<=470)){
		    			gb.currentPlayer.gold-=costvalues.get(4);
		    			gb.currentPlayer.dupgradenum++;
		    			repaint();
		    		}
		    		backcheck = false;
		    		buynumber = 0;
		    	}
		    }
		});
		this.requestFocusInWindow();
	}
	
		public void mouseMoved(MouseEvent e) {
			if(gb.currentPlayer instanceof Computer){
				setCursor(Cursor.getDefaultCursor());
				return;
			}
			if(screen != 2){
				setCursor(Cursor.getDefaultCursor());
				return;
			}
			mousex = e.getX();
			mousey = e.getY();
			mousegetTerritory = getTerritory(mousex, mousey);
			if(mousemode == 1 && mousex <=10){
    			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));
			}
			if(deploytroop != 0){
				if(mousegetTerritory == null){
					setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));
	    			mousecheck = true;
				}
				else if(((deployn == 1 || deployn == 2) && mousegetTerritory!=null && gb.currentPlayer == mousegetTerritory.contender)){
	    			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));
	    			mousecheck = true;
				}
				else if(deployn == 3 && (mousegetTerritory.contender == null || gb.currentPlayer != mousegetTerritory.contender)){
					setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));
	    			mousecheck = true;
				}
				else{
					setCursor(Cursor.getDefaultCursor());
					mousecheck = false;
				}
			}
			if(mousemode == 2 && clicked[0] == null && clicked[1] == null){
				if(mousegetTerritory == null){
					tcheck = false;
				}
				else if(mousegetTerritory!=og){
					statcheck = false;
					tcheck = true;
					mt.statanim(mousegetTerritory,deploytroop!=0?deploytroop:-1);
				}
			}
			if(mousemode == 2  && (mousegetTerritory == null || (mousegetTerritory.contender == null || mousegetTerritory.contender != gb.currentPlayer))){
				setCursor(Cursor.getDefaultCursor());	//default cursor		
				crosshaircheck = false;
				statcheck = false;
			}
			else if (mousemode == 2 && mt.deploywidth == 350 && mt.deployvar == 2 && (e.getX()>=50+WIDTH-130-(mt.finaldeploy-30) 
					&& e.getX()<=50+WIDTH-130-(60)) && (e.getY()>=120 && e.getY()<= this.HEIGHT-150)){ //when deploy is expanded and mouse is over rect
				setCursor(Cursor.getDefaultCursor());	//default cursor		
				crosshaircheck = false;
				statcheck = false;

			}
			else if (mousemode == 2){
    			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));  //black cursor
    			crosshaircheck = true;  //sword icon
    			statcheck = false;
    			xiconcheck = false;
    			if(mousegetTerritory.attack== 0){
    				xiconcheck = true;
    			}
			}
			if(drawarrow && mousegetTerritory != null && mousegetTerritory.contender != gb.currentPlayer){
				if(gb.isAdjacent(clicked[0], mousegetTerritory)){
					statcheck = true;
					if(mousegetTerritory!=og){
						mt.statanim(mousegetTerritory, -1);
					}
				}
				else{
					statcheck = false;
				}
			}
			repaint();
			og = getTerritory(mousex, mousey);
	    }

	    public void mouseDragged(MouseEvent e) {

	    }

	
	private Territory getTerritory(int x, int y){
		for(Territory t: gb.territories){
			if((x>=t.x && x<t.x+t.sizex) &&
					(y>=t.y && y<t.y+t.sizey)
					&& !t.isTransparent(x-t.x, y-t.y)){
				return t;
			}
		}
		return null;
	}
	
	void drawArrow(int a, int b, int c, int d){
		Graphics2D g2d = (Graphics2D)gra;
		AffineTransform oldtrans = new AffineTransform();
	    AffineTransform trans = new AffineTransform();
	    double angle = Math.atan(((double)(d-b))/((double)(c-a)));
	   trans.rotate(angle, a, b);
	  	g2d.setTransform(trans);
	  	int temp = (int)Math.sqrt((c-a)*(c-a)+(d-b)*(d-b));
		gra.drawImage(arrow, a, b-15, c<a?-temp:temp, 30, null);
	  	g2d.setTransform(oldtrans);
	}
	
	@Override
	public void paintComponent(Graphics g) { //width = 1200, height = 800]
		gra = g;
		if(transition){
			g.drawImage(blackRectangle, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(plane_white, (int)transplanex, 100, 240, 160, null);
			g.drawImage(loading_icon, this.WIDTH-200, this.HEIGHT-144, 294, 194, null);
			return;
		}
		if(screen == 1){
			//colors: #472C04 #D5CE5F
			if(startScreen){
				g.drawImage(blackRectangle, 0, 0, this.WIDTH, this.HEIGHT, null);
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
				g.drawImage(creators, 50, this.HEIGHT/2-40, this.WIDTH-100, 80, null);
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				return;
			}
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(plane, pstartx, 50, 296, 160, null);
			if(tstartx>500){
				g.drawImage(tank1, tstartx+10, HEIGHT-130, 360, 129, null);
				g.drawImage(tank1, tstartx+370, HEIGHT-130, 360, 130, null);
				g.drawImage(tank2, 500-tstartx, HEIGHT-152, 435, 150, null);
			}
			else{
				g.drawImage(tank1, 510, HEIGHT-130, 360, 129, null);
				g.drawImage(tank1, 870, HEIGHT-130, 360, 130, null);
				g.drawImage(tank2, 0, HEIGHT-152, 435, 150, null);
			}
			if(fire1 && !bombcheck){g.drawImage(explosion, 410, HEIGHT-116, 35, 40, null);}
			if(fire2 && !bombcheck){g.drawImage(explosion,  500, HEIGHT-124, 35, 40, null);}
			if(fire3 && !bombcheck){g.drawImage(explosion,  860, HEIGHT-124, 35, 40, null);}
			for(int x = 0; x<startbombsx.size(); x++){
				if(startbombsy.get(x) != 700){
					g.drawImage(bomb, startbombsx.get(x), startbombsy.get(x), 40, 90, null);
					startbombsy.set(x, startbombsy.get(x)+1);
					continue;
				}
				bombcheck = true;
			}
			if(bombcheck == true){
				g.setClip(560, HEIGHT-120, pstartx-1160, 120);
 				g.drawImage(explosion2, 560, HEIGHT-120, 273, 120, null);
 				g.drawImage(explosion2, 600, HEIGHT-120, 273, 120, null);
 				g.drawImage(explosion2, 790, HEIGHT-120, 273, 120, null);
 				g.drawImage(explosion2, 900, HEIGHT-120, 273, 120, null);
 				g.drawImage(explosion2, 1000, HEIGHT-120, 273, 120, null);
 				g.setClip(null);
			}
			if(riskCheck){
				g.drawImage(title, 490, 10, 220, 100, null);
				g.drawImage(button, WIDTH/2-85, 170, 170, 75, null); g.drawImage(startbefore, WIDTH/2-65, 182, 130, 55, null);
				g.drawImage(button, WIDTH/2-85, 310, 170, 75, null); g.drawImage(rulesbefore, WIDTH/2-65, 325, 130, 50, null);
				g.drawImage(button, WIDTH/2-85, 450, 170, 75, null); g.drawImage(creditsbefore, WIDTH/2-70, 465, 140, 50, null);
				if(buttonnumber == 1){
					g.drawImage(button2, WIDTH/2-85, 170, 170, 75, null); g.drawImage(startafter, WIDTH/2-65, 182, 130, 55, null);
				}
				if(buttonnumber == 2){
					g.drawImage(button2, WIDTH/2-85, 310, 170, 75, null); g.drawImage(rulesafter, WIDTH/2-65, 325, 130, 50, null);
				}
				if(buttonnumber == 3){
					g.drawImage(button2, WIDTH/2-85, 450, 170, 75, null); g.drawImage(creditsafter, WIDTH/2-70, 465, 140, 50, null);
				}
 			}
		}
		else if(screen == 6){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(title, this.WIDTH/2-110, 15, 220, 100, null);
			g.drawImage(border, 50, 120, this.WIDTH-100, this.HEIGHT-170, null);
			for(int x = 0; x<18; x++){
				g.drawImage(tempdraw.get(gb.order[x]), gb.positionsx[gb.order[x]], gb.positionsy[gb.order[x]], gb.sx[gb.order[x]], gb.sy[gb.order[x]], null);
			}
			g.drawImage(blackRectangle, 50+this.WIDTH-130, 120, 30, this.HEIGHT-170, null);
			g.drawImage(deploy, 50+this.WIDTH-125, 394, 20, 82, null);
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.8));		
			g.drawImage(statistics, 844, 142, 250, 72, null);
			g.drawImage(blackRectangle, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(chooseborder, cb1x, 292, 66, 66, null);
			g.drawImage(chooseborder, cb2x, 292, 66, 66, null);
			g.drawImage(players, 110, 200, 268, 92, null);
			g.drawImage(computers, this.WIDTH-480, 200, 390, 92, null);
			if(g1index == -1){
				g.drawImage(roundedsquaregrayed, c1values[0], 300, 50, 50, null);
				for(int x= 1; x<4; x++){ 
					g.drawImage(roundedsquare, c1values[x], 300, 50, 50, null);
				}
			}
			else{
				for(int x= 0; x<4; x++){ 
					g.drawImage(x<g1index?roundedsquare:roundedsquaregrayed, c1values[x], 300, 50, 50, null);
				}
			}
			for(int x= 0; x<4; x++){ 
				g.drawImage(x<g2index?roundedsquare:roundedsquaregrayed, c2values[x], 300, 50, 50, null);
			}
			if(gcheck){
				g.drawImage(roundedsquaregrayed, c2values[4], 300, 50, 50, null);
			}
			g.drawImage(gcheck?roundedsquaregrayed:g2index!=4?roundedsquare:roundedsquaregrayed, c2values[4], 300, 50, 50, null);
			g.drawImage(num1, 60, 300, 50, 50, null); g.drawImage(num2, 175, 300, 50, 50, null);
			g.drawImage(num3, 290, 300, 50, 50, null); g.drawImage(num4, 405, 300, 50, 50, null);
			g.drawImage(num1, 730, 300, 50, 50, null); g.drawImage(num2, 845, 300, 50, 50, null);
			g.drawImage(num3, 950, 300, 50, 50, null); g.drawImage(num4, 1060, 300, 50, 50, null);
			g.drawImage(num0, 615, 300, 50, 50, null);
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1));
			g.drawImage(button3, WIDTH/2-85, 500, 170, 75, null); g.drawImage(startga, WIDTH/2-65, 512, 130, 55, null);
			if(buttonnumber == 1){
				g.drawImage(button5, WIDTH/2-85, 500, 170, 75, null); g.drawImage(startga, WIDTH/2-65, 512, 130, 55, null);
			}
		}
		else if(screen == 2){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(title, this.WIDTH/2-110, 15, 220, 100, null);
			g.drawImage(border, 50, 120, this.WIDTH-100, this.HEIGHT-170, null);
			g.drawImage(statistics, 844, 142, 250, 72, null);
			if((clicked[0] == null || clicked[1] == null) && !(clicked[0] == null && clicked[1] == null) || (clicked[0] == null && clicked[1] == null && tcheck)){
				Territory tempTerritory;
				if(clicked[0] == null && clicked[1] == null){
					tempTerritory = mousegetTerritory;
				}
				else{
					tempTerritory = clicked[0] == null?clicked[1]:clicked[0];
				}
				if(mousecheck == true || deployn == 0){
					g.drawImage(attack, 844, 222, 105, 35, null);
					g.drawImage(stats1rect, 850, 264, mt.statterritory == tempTerritory?(260*mt.attackbar)/tempTerritory.maxattack+2: (260*tempTerritory.attack)/tempTerritory.maxattack+2, 13, null); //tertiary for which territory statanim is workin on
					g.drawImage(orect, 850+(mt.statterritory == tempTerritory?(260*mt.attackbar)/tempTerritory.maxattack+2:(260*tempTerritory.attack)/tempTerritory.maxattack+2), 264, 
						mt.statterritory == tempTerritory?(260*mt.oattack)/tempTerritory.maxattack:(260*mt.nextattack)/tempTerritory.maxattack, 13, null);
					g.drawImage(defense, 844, 292, 112, 35, null);
					g.drawImage(stats2rect, 850, 332,  mt.statterritory == tempTerritory?(260*mt.defensebar)/tempTerritory.maxdefense+2:(260*tempTerritory.defense)/tempTerritory.maxdefense+2, 13, null);
					g.drawImage(orect, 850+(mt.statterritory == tempTerritory?(260*mt.defensebar)/tempTerritory.maxdefense+2:(260*tempTerritory.defense)/tempTerritory.maxdefense+2), 332, 
						mt.statterritory == tempTerritory?(260*mt.odefense)/tempTerritory.maxdefense:(260*mt.nextdefense)/tempTerritory.maxdefense, 13, null);
				}
			}
			if(statcheck == true){
				g.drawImage(versus, 940, 352, 57, 40, null);
				g.drawImage(blackRectangle, 844, 370, 90, 4, null);
				g.drawImage(blackRectangle, 1010, 370, 90, 4, null);
				g.drawImage(attack, 844, 396, 105, 35, null);
				g.drawImage(stats1rect, 850, 438, (260*mt.attackbar)/mousegetTerritory.maxattack+2, 13, null);
				g.drawImage(defense, 844, 466, 112, 35, null);
				g.drawImage(stats2rect, 850, 506, (260*mt.defensebar)/mousegetTerritory.maxdefense+2, 13, null);
			}
			String tempg = "";
			if(gb.currentPlayer instanceof Player){
				Player temp = (Player)gb.currentPlayer;
				g.drawImage(temp.masterImage, 830, 608, 283, 120, null);
				g.drawImage(player, 844, 622, 105, 35, null);
				g.drawImage(numbers.get(((Player)gb.currentPlayer).number), 950, 622, numberwidths.get(((Player)gb.currentPlayer).number), 35, null);
				g.drawImage(goldcoins, 846, 662, 27, 35, null);
				tempg+=((Player)gb.currentPlayer).gold;
			}
			else{
				Computer temp = (Computer)gb.currentPlayer;
				g.drawImage(temp.masterImage, 830, 608, 283, 120, null);
				g.drawImage(computer, 844, 622, 140, 35, null);
				g.drawImage(numbers.get(((Computer)gb.currentPlayer).number-gb.numplayers), 985, 622, numberwidths.get(((Computer)gb.currentPlayer).number-gb.numplayers), 35, null);
				g.drawImage(goldcoins, 846, 662, 27, 35, null);
				tempg+=((Computer)gb.currentPlayer).gold;
			}
			g.drawImage(blackRectangle, 1044, 625, 20, 20, null);
			g.drawImage(blackRectangle, 1075, 625, 20, 20, null);
			g.drawImage(!turncheck?end1:end2, 1021, 678, 74, 34, null);
			g.drawImage(endturn, 1030, 683, 56, 24, null);
			int sum = 880;
			for(int x = 0; x<tempg.length(); x++){
				int a = ((int)tempg.charAt(x)-'0');
				g.drawImage(numbers.get(a), sum, 662, numberwidths.get(a), 35, null);
				sum+=numberwidths.get(a)-10;
			}
			for(Territory t :gb.territories){
				g.drawImage(!(t.grayed) && t.cn == -1 ? t.image6:t.cn == 1 || t.grayed? t.image1:t.cn == 2? t.image2:t.cn == 3? t.image3:t.cn == 4? t.image4:t.image5, t.x, t.y, t.sizex, t.sizey, null);
			}
			g.drawImage(blackRectangle, 50+this.WIDTH-130-(mt.deploywidth-30), 120, mt.deploywidth, this.HEIGHT-170, null);
			g.setClip(50+this.WIDTH-130-(mt.deploywidth-60), 120, mt.deploywidth-30, this.HEIGHT-170);
			g.drawImage(border2, 50+this.WIDTH-130-(mt.deploywidth-60)-400, 120, this.WIDTH-100, this.HEIGHT-170, null);
			g.drawImage(blackRectangle, 50+this.WIDTH-130-(mt.deploywidth-30), 120, mt.deploywidth, 3, null);
			g.drawImage(blackRectangle, 50+this.WIDTH-130-(mt.deploywidth-30), 120+this.HEIGHT-170-3, mt.deploywidth, 3, null);
			String j = "";
			int sumn = 0;
			ArrayList<String> tempn = new ArrayList<String>();
			ArrayList<Integer> yvalues = new ArrayList<Integer>();
			ArrayList<Integer> xvalues = new ArrayList<Integer>();
			yvalues.add(280); yvalues.add(460); yvalues.add(638);
			xvalues.add(this.WIDTH+140-(mt.deploywidth-30)); xvalues.add(this.WIDTH+140-(mt.deploywidth-30)); xvalues.add(this.WIDTH+140-(mt.deploywidth-30));
			g.drawImage(inventory, this.WIDTH-10-mt.deploywidth, 125, 300, 74, null);
			g.drawImage(infantry, this.WIDTH-10-(mt.deploywidth-30), 200, 75, 148, null); j+=gb.currentPlayer.infantrynum; tempn.add(j);
			if(deployn == 0 || deployn == 1){
				g.drawImage(buttondeploy == 1 || gb.currentPlayer.infantrynum == 0?end2:end1, this.WIDTH+140-(mt.deploywidth-30), 230, 120, 50, null); 	
				g.drawImage(deployn != 0?cancel:deployb, this.WIDTH+145-(mt.deploywidth-30), 241, 110, 28, null); 
			}
			j = "";
			g.drawImage(tank1, this.WIDTH-40-(mt.deploywidth-30), 405, 150, 64, null); j+=gb.currentPlayer.tanknum; tempn.add(j);
			if(deployn == 0 || deployn == 2){
				g.drawImage(buttondeploy == 2 || gb.currentPlayer.tanknum == 0?end2:end1, this.WIDTH+140-(mt.deploywidth-30), 410, 120, 50, null); 
				g.drawImage(deployn != 0?cancel:deployb, this.WIDTH+145-(mt.deploywidth-30), 422, 110, 28, null); 
			}
			j = "";
			g.drawImage(bomb1, this.WIDTH-40-(mt.deploywidth-30), 580, 150, 66, null); j+=gb.currentPlayer.bombnum; tempn.add(j);
			if(deployn == 0 || deployn == 3){
				g.drawImage(buttondeploy == 3 || gb.currentPlayer.bombnum == 0?end2:end1, this.WIDTH+140-(mt.deploywidth-30), 588, 120, 50, null); 
				g.drawImage(deployn != 0?cancel:deployb, this.WIDTH+145-(mt.deploywidth-30), 599, 110, 28, null); 
			}
			for(int x = 0; x<tempn.size(); x++){
				sumn = 0;
				for(int y = 0; y<tempn.get(x).length(); y++){
					if(deployn == 0 || deployn == x+1){
						int z = ((int)tempn.get(x).charAt(y)-'0');
						g.drawImage(numbers.get(z), xvalues.get(x)+sumn, yvalues.get(x),numberwidths.get(z), 35, null);
						sumn+=numberwidths.get(z)-10;
					}
				}
			}
			if(deployn == 0){
				g.drawImage(!storecheck?end1:end2, this.WIDTH+80-mt.deploywidth, 706, 110, 40, null); g.drawImage(store,  this.WIDTH+85-mt.deploywidth, 711, 100, 30, null);
			}
			g.setClip(null);
			g.drawImage(blackRectangle, 50+this.WIDTH-130-(3-30), 120, 3, this.HEIGHT-170, null);
			g.drawImage(deploy, 50+this.WIDTH-125-(mt.deploywidth-30), 394, 20, 82, null);
			if(mousemode == 1){
				g.drawImage(crosshair, mousex-10, mousey-10, 20, 20, null);
			}
			else if(deploytroop != 0 && mousecheck == false){
				g.drawImage(xicon, mousex-15, mousey-15, 30, 30, null);
			}
			else if(deploytroop == 1){
				g.drawImage(infantry, mousex-15, mousey-24, 30, 48, null);
			}
			else if(deploytroop == 2){
				g.drawImage(tank1, mousex-28, mousey-10, 83, 30, null);
			}
			else if(deploytroop == 3){
				g.drawImage(bomb1, mousex-34, mousey-15, 68, 30, null);
			}	
			else if(mousemode == 2 && crosshaircheck && !drawarrow){
				if(!xiconcheck){
					g.drawImage(sword, mousex-15, mousey-15, 30, 30, null);
				}
				else{
					g.drawImage(xicon, mousex-15, mousey-15, 30, 30, null);
				}
			}	
			else if(mousemode == 2 && drawarrow){
				drawArrow(mouseix, mouseiy, mousex, mousey);
			}
			if(comp == true){
				drawArrow(ta, tb, tc, td);
			}
		}
		else if(screen == 3){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(rulestitle, 477, 10, 246, 100, null);
			g.drawImage(rulesex1, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(button, 20, HEIGHT-95, 170, 75, null); g.drawImage(mainbefore, 40, HEIGHT-83, 130, 55, null);
			g.drawImage(button, WIDTH-188, HEIGHT-95, 170, 75, null); g.drawImage(nextbefore, WIDTH-168, HEIGHT-83, 130, 55, null);
			if(buttonnumber == 1){                    
				g.drawImage(button2, 20, HEIGHT-95, 170, 75, null); g.drawImage(mainafter, 40, HEIGHT-83, 130, 55, null);
			}
			else if(buttonnumber == 2){
				g.drawImage(button2, WIDTH-188, HEIGHT-95, 170, 75, null); g.drawImage(nextafter, WIDTH-168, HEIGHT-83, 130, 55, null);
			}
		}
		else if(screen == 4){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(rulestitle, 477, 10, 246, 100, null); 
			g.drawImage(rulesex2, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(button, 24, HEIGHT-98, 170, 75, null); g.drawImage(backbefore, 44, HEIGHT-86, 130, 55, null);
			g.drawImage(button, WIDTH-186, HEIGHT-98, 170, 75, null); g.drawImage(mainbefore, WIDTH-166, HEIGHT-83, 130, 55, null);
			if(buttonnumber == 1){
				g.drawImage(button2, 24, HEIGHT-98, 170, 75, null); g.drawImage(backafter, 44, HEIGHT-86, 130, 55, null);
			}
			else if(buttonnumber == 2){
				g.drawImage(button2, WIDTH-186, HEIGHT-98, 170, 75, null); g.drawImage(mainafter, WIDTH-166, HEIGHT-83, 130, 55, null);
			}
		}
		else if(screen == 5){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(creditstitle, 462, 10, 276, 100, null);
			g.drawImage(riskcred, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(button, WIDTH-190, HEIGHT-95, 170, 75, null); g.drawImage(mainbefore, WIDTH-170, HEIGHT-83, 130, 55, null);
			if(buttonnumber == 1){
				g.drawImage(button2, WIDTH-190, HEIGHT-95, 170, 75, null); g.drawImage(mainafter, WIDTH-170, HEIGHT-83, 130, 55, null);
			}
		}
		else if(screen == 7){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(title, this.WIDTH/2-110, 15, 220, 100, null);
			g.drawImage(border, 50, 120, this.WIDTH-100, this.HEIGHT-170, null);
			for(Territory t :gb.territories){
				g.drawImage(!(t.grayed) && t.cn == -1 ? t.image6:t.cn == 1 || t.grayed? t.image1:t.cn == 2? t.image2:t.cn == 3? t.image3:t.cn == 4? t.image4:t.image5, t.x, t.y, t.sizex, t.sizey, null);
			}
			g.drawImage(blackRectangle, 50+this.WIDTH-130, 120, 30, this.HEIGHT-170, null);
			g.drawImage(deploy, 50+this.WIDTH-125, 394, 20, 82, null);
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.8));		
			g.drawImage(statistics, 844, 142, 250, 72, null);
			g.drawImage(blackRectangle, 0, 0, this.WIDTH, this.HEIGHT, null);
			if(gb.currentPlayer instanceof Player){
				g.drawImage(playerwhite, 340, 300, 243, 92, null);
				g.drawImage(wnumbers.get(((Player)gb.currentPlayer).number), 593, 300, numberwidths.get(((Player)gb.currentPlayer).number)*92/35, 92, null);
				g.drawImage(wins, 603+numberwidths.get(((Player)gb.currentPlayer).number)*92/35, 300, 245, 92, null);
			}
			else if(gb.currentPlayer instanceof Computer){
				g.drawImage(computerwhite, 250, 300, 394, 92, null);
				g.drawImage(wnumbers.get(((Computer)gb.currentPlayer).number-gb.numplayers), 654, 300, numberwidths.get(((Computer)gb.currentPlayer).number-gb.numplayers)*92/35, 92, null);
				g.drawImage(wins, 654+numberwidths.get(((Computer)gb.currentPlayer).number-gb.numplayers)*92/35+10, 300, 245, 92, null);
			}
			g.drawImage(button3, WIDTH/2-85, 450, 170, 75, null); g.drawImage(maingray, WIDTH/2-65, 460, 130, 55, null);
			if(lastcheck == true){
				g.drawImage(button5, WIDTH/2-85, 450, 170, 75, null); g.drawImage(maingray, WIDTH/2-65, 460, 130, 55, null);
			}
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1));
		}
		else if(screen == 8){
			g.drawImage(background, 0, 0, this.WIDTH, this.HEIGHT, null);
			g.drawImage(title, this.WIDTH/2-110, 15, 220, 100, null);
			g.drawImage(border, 50, 120, this.WIDTH-100, this.HEIGHT-170, null);
			g.drawImage(store, this.WIDTH/2-81, 125, 162, 50, null);
			g.drawImage(goldcoins, 65, 130, 27, 35, null);
			String tempg = "";
			tempg+=gb.currentPlayer.gold;
			int sum = 95;
			for(int x = 0; x<tempg.length(); x++){
				int z = ((int)tempg.charAt(x)-'0');
				g.drawImage(numbers.get(z), sum, 130, numberwidths.get(z), 35, null);
				sum+=numberwidths.get(z)-10;
			}
			g.drawImage(!backcheck?back:backa, 1025, 135, 110, 40, null);
			g.drawImage(infantryb, 140, 200, 75, 148, null);
			g.drawImage(buynumber == 1 || gb.currentPlayer.gold<costvalues.get(0)?end2:end1, 470, 210, 120, 50, null);  g.drawImage(buy, 488, 215, 84, 40, null); 
			g.drawImage(tank1b, 70, 410, 250, 90, null);
			g.drawImage(buynumber == 2 || gb.currentPlayer.gold<costvalues.get(1)?end2:end1, 470, 410, 120, 50, null);  g.drawImage(buy, 488, 415, 84, 40, null); 
			g.drawImage(bomb1b, 80, 610, 180, 80, null);
			g.drawImage(buynumber == 3 || gb.currentPlayer.gold<costvalues.get(2)?end2:end1, 470, 615, 120, 50, null);  g.drawImage(buy, 488, 620, 84, 40, null); 
			g.drawImage(blackRectangle, this.WIDTH/2-2, 190, 4, this.HEIGHT-270, null);
			g.drawImage(aupgrade, this.WIDTH/2+12, 210, 296, 50, null);
			g.drawImage(buynumber == 4 || gb.currentPlayer.gold<costvalues.get(3)?end2:end1, 1020, 210, 120, 50, null);  g.drawImage(buy, 1038, 215, 84, 40, null); 
			g.drawImage(dupgrade, this.WIDTH/2+12, 420, 315, 50, null);
			g.drawImage(buynumber == 5 || gb.currentPlayer.gold<costvalues.get(4)?end2:end1, 1020, 420, 120, 50, null);  g.drawImage(buy, 1038, 425, 84, 40, null); 
			int sumn = 0;
			ArrayList<String> tempn = new ArrayList<String>();
			ArrayList<Integer> yvalues = new ArrayList<Integer>();
			ArrayList<Integer> xvalues = new ArrayList<Integer>();
			yvalues.add(265); yvalues.add(465); yvalues.add(670); yvalues.add(265); yvalues.add(475); 
			xvalues.add(470); xvalues.add(470); xvalues.add(470); xvalues.add(1020); xvalues.add(1020);
			String a = "";
			a+=gb.currentPlayer instanceof Player?((Player)gb.currentPlayer).infantrynum:((Computer)gb.currentPlayer).infantrynum; tempn.add(a);
			a = "";
			a+=gb.currentPlayer instanceof Player?((Player)gb.currentPlayer).tanknum:((Computer)gb.currentPlayer).tanknum; tempn.add(a); 
			a = "";
			a+=gb.currentPlayer instanceof Player?((Player)gb.currentPlayer).bombnum:((Computer)gb.currentPlayer).bombnum; tempn.add(a);
			a = "";
			a+=gb.currentPlayer instanceof Player?((Player)gb.currentPlayer).aupgradenum:((Computer)gb.currentPlayer).aupgradenum; tempn.add(a); 
			a = "";
			a+=gb.currentPlayer instanceof Player?((Player)gb.currentPlayer).dupgradenum:((Computer)gb.currentPlayer).dupgradenum; tempn.add(a); 
			a = "";
			for(int x = 0; x<tempn.size(); x++){
				sumn = 0;
				for(int y = 0; y<tempn.get(x).length(); y++){
					int z = ((int)tempn.get(x).charAt(y)-'0');
					g.drawImage(numbers.get(z), xvalues.get(x)+sumn, yvalues.get(x),numberwidths.get(z), 35, null);
					sumn+=numberwidths.get(z)-10;
				}
			}
		}
	}
	
}


//____________________________________________

