import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.Timer;

public class MasterTimer {
	Timer deployTimer, statanim, delayTimer, delay1Timer, delay2Timer; 
	int deployvar, finaldeploy, deploywidth, attackbar, defensebar, oattack, odefense;
	int nextattack, nextdefense, sa, sb, tempa, tempb;
	double deployorgt, at = 1, dt = 1, delayvar, orgt, dorgt;
	boolean deploydir, dcheck = false, delay2check = false;
	Territory statterritory;
	Contender temp, c;
	GraphicsPanel gp;
	
	public MasterTimer(GraphicsPanel panel){
		gp = panel;
		deploywidth = 30;
	}

	public void deployScreen(boolean dir) {
		// TODO Auto-generated method stub
		finaldeploy = 350;
		deploydir = dir;
		deployvar = deploydir?1:3;
		deployorgt = System.currentTimeMillis();
		deployTimer = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-deployorgt >=1){
					deployorgt = System.currentTimeMillis();
					deploywidth+=deploydir?2:-2;
				}
				if(deploywidth == finaldeploy && deploydir){
					deployTimer.stop();
				}
				else if(deploywidth == 30 && !deploydir){
					deployTimer.stop();
				}
	    		gp.repaint();
			}

		}); deployTimer.start();
		deployvar = deploydir?2:0;
	}
	
	public void statanim(Territory t, int k){
		statterritory = t;
		gp.newclicked = t; orgt = System.currentTimeMillis();
		attackbar = 0; at = 1; nextattack = 0; oattack = 0;
		defensebar = 0; dt = 1; nextdefense = 0; odefense = 0;
		if(t == null  || gp.newclicked == null){
			return;
		}
		tempa = gp.mousegetTerritory.attack;
		tempb = gp.mousegetTerritory.defense;
		nextattack = (int) (k == -1?0:k == 1?36:k == 2?31:0);
		nextdefense = (int) (k == -1?0:k == 1?0:k == 2?31:0);
		sa = nextattack+gp.newclicked.attack<=gp.newclicked.maxattack?nextattack+gp.newclicked.attack:gp.newclicked.maxattack;
		sb = nextdefense+gp.newclicked.defense<=gp.newclicked.maxdefense?nextdefense+gp.newclicked.defense:gp.newclicked.maxdefense;
		nextattack = nextattack+gp.newclicked.attack<=gp.newclicked.maxattack?nextattack:gp.newclicked.maxattack-gp.newclicked.attack;
		nextdefense = nextdefense+gp.newclicked.defense<=gp.newclicked.maxdefense?nextdefense:gp.newclicked.maxdefense-gp.newclicked.defense;
		if(k == 3){
			nextattack = 0;
			nextdefense = 0;
			tempa = 0;
			tempb = 0;
		}
		statanim = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-orgt>=200*at/((double)sa+2)){
					if(attackbar!=tempa){
						attackbar+=attackbar == tempa?0:1;
					}
					else{
						oattack+=oattack == nextattack?0:1;
					}
					at++;
				}
				if(System.currentTimeMillis()-orgt>=200*dt/((double)sb+2)){
					if(defensebar!=gp.newclicked.defense){
						defensebar+=defensebar == tempb?0:1;
					}
					else{
						odefense+=odefense == nextdefense?0:1;
					}
					dt++;
				}
				if(attackbar == tempa && defensebar == tempb && oattack == nextattack && odefense == nextdefense){
					statanim.stop();
				}
				gp.repaint();
			}

		}); statanim.start();
	}

	public void delay(int x){
		dcheck = true;
		delayvar = x;
		dorgt = System.currentTimeMillis();
		delayTimer = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-dorgt >=delayvar){
					dcheck = false;
					delayTimer.stop();
				}
				
			}

		}); delayTimer.start();
	}
	
	public void delay1(int x, Contender ca){
		delayvar = x;
		temp = ca;
		dorgt = System.currentTimeMillis();
		delay1Timer = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-dorgt >=delayvar){
					gp.gb.endGame(temp);
					delay1Timer.stop();
				}
				
			}

		}); delay1Timer.start();
	}
	

	public void delay2(int x, Contender ca){
		delayvar = x;
		dorgt = System.currentTimeMillis();
		delay2check = false;
		c = ca;
		delay2Timer = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-dorgt >=delayvar){
					delay2check = true;
					((Computer)c).owned.get(((Computer)c).xvalue).updateClicked();
					((Computer)c).oppose.updateClicked();
					delay2Timer.stop();
				}
				
			}

		}); delay2Timer.start();
	}
	
	public void delay2(int x, int y){
		delayvar = x;
		dorgt = System.currentTimeMillis();
		delay2check = false;
		delay2Timer = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(System.currentTimeMillis()-dorgt >=delayvar){
					delay2check = true;
					delay2Timer.stop();
				}
				
			}

		}); delay2Timer.start();
	}
	
}
