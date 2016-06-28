package tankgame.tank.weapon;
import java.awt.Color;
import java.awt.Graphics;

import tankgame.system.etc.PointDouble;
import tankgame.system.etc.UnitVector;

//なんか大きい砲弾
//同時に5発まで存在できる
public class HugeGun extends Weapon {

	private final int MAXNUM = 5;	//同時存在数

	private int nextNumber = 0;		//次に発射する玉の配列番号
	private int coolTimeCount;		//クールタイムを数えるカウンタ
	
	private final int GUNSPEED = 8;	//だんそく

	HugeGun() {
		isExist = new boolean[MAXNUM];
		center = new PointDouble[MAXNUM];	
		direction = new UnitVector[MAXNUM];
		
		coolTime = 50;
		damage = 50;
		for (int i = 0; i < MAXNUM; i++) {
			isExist[i] = false;
			center[i] = new PointDouble();
			direction[i] = new UnitVector();
		}
	}

	public void move() {
		for (int i = 0; i < MAXNUM; i++) {
			if (isExist[i]) {
				center[i].addX(direction[i].getX() * GUNSPEED);
				center[i].addY(direction[i].getY() * GUNSPEED);
			}
		}
	}


	public void draw(Graphics g) {
		if (coolTimeCount > 0) {
			coolTimeCount--;
		}
		
		g.setColor(Color.BLACK);
		for (int i = 0; i < MAXNUM; i++) {
			if (isExist[i]) {
				g.setColor(Color.BLACK);
				g.fillOval(center[i].getX() - 5, center[i].getY() - 5, 11, 11);
				g.setColor(Color.DARK_GRAY);
				g.fillOval(center[i].getX() - 4, center[i].getY() - 4, 5, 5);
			}
		}
	}


	public void shoot(PointDouble sl, UnitVector sd) {
		if (coolTimeCount == 0) {
			coolTimeCount = coolTime;
			
			isExist[nextNumber] = true;
			center[nextNumber] = new PointDouble(sl.getX(), sl.getY());
			direction[nextNumber] = new UnitVector(sd.getDegree());
			nextNumber++;
			if (nextNumber == MAXNUM) nextNumber -= MAXNUM;
		}
	}

	void del(int number) {
		isExist[number] = false;
	}
}
