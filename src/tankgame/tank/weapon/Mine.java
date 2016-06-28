package tankgame.tank.weapon;
import java.awt.Color;
import java.awt.Graphics;

import tankgame.system.etc.PointDouble;
import tankgame.system.etc.UnitVector;

//地雷的な武器　とりあえず作ってみただけ

public class Mine extends Weapon {

	private final int MAXNUM = 1;

	private int nextNumber = 0;	//次に発射する玉の配列番号
	private int coolTimeCount;	//クールタイムのカウンタ
	
	
	Mine() {
		isExist = new boolean[MAXNUM];
		center = new PointDouble[MAXNUM];	
		direction = new UnitVector[MAXNUM];
		
		coolTime = 100;
		damage = 100;
		for (int i = 0; i < MAXNUM; i++) {
			isExist[i] = false;
			center[i] = new PointDouble();
			direction[i] = new UnitVector();
		}
	}
	

	public void draw(Graphics g) {
		if (coolTimeCount > 0) {
			coolTimeCount--;
		} else {
			isExist[0] = false;
		}
		
		g.setColor(Color.BLACK);
		for (int i = 0; i < MAXNUM; i++) {
			if (isExist[i]) {
				g.fillRect(center[i].getX() - 10, center[i].getY() - 10, 20, 20);
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
			if (nextNumber == MAXNUM) nextNumber = 0;
		}
	}

	//動かない
	public void move() {
	}

}
