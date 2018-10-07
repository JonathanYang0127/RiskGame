public abstract class Contender {
	int numterritories, gold;
	int infantrynum = 0, tanknum = 0, bombnum = 0, aupgradenum = 0, dupgradenum = 0;
	
	abstract void move();
	abstract int getMoney();
	
}
