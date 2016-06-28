package tankgame.tank.weapon;
import java.awt.Color;
import java.awt.Graphics;

import tankgame.system.etc.PointDouble;
import tankgame.system.etc.UnitVector;

//普通の戦車砲
//同時に10発まで存在できる

public class TankGun extends Weapon {

	private final int MAXNUM = 10;

	private int nextNumber = 0;			//次に発射する弾の配列番号
	private int coolTimeCount;			//クールタイムを数えるカウンタ
	
	private final int GUNSPEED = 10;	//弾速

	
	//コンストラクタ
	TankGun() {
		isExist = new boolean[MAXNUM];
		center = new PointDouble[MAXNUM];	
		direction = new UnitVector[MAXNUM];
		
		coolTime = 30;
		damage = 10;
		for (int i = 0; i < MAXNUM; i++) {
			isExist[i] = false;
			center[i] = new PointDouble();
			direction[i] = new UnitVector();
		}
	}

	
	//移動メソッド
	public void move() {
		for (int i = 0; i < MAXNUM; i++) {
			if (isExist[i]) {
				center[i].addX(direction[i].getX() * GUNSPEED);
				center[i].addY(direction[i].getY() * GUNSPEED);
			}
		}
	}


	//描画メソッド
	public void draw(Graphics g) {
		if (coolTimeCount > 0) {
			coolTimeCount--;
		}
		
		g.setColor(Color.BLACK);
		for (int i = 0; i < MAXNUM; i++) {
			if (isExist[i]) {
				g.fillOval(center[i].getX() - 2, center[i].getY() - 2, 5, 5);
			}
		}
	}


	//発射メソッド
	public void shoot(PointDouble sl, UnitVector sd) {
		if (coolTimeCount == 0) {
			coolTimeCount = coolTime;
			
			isExist[nextNumber] = true;
			center[nextNumber] = new PointDouble(sl.getX(), sl.getY());
			direction[nextNumber] = new UnitVector(sd.getDegree());
			//ちゃんとディープコピーしておく
			
			nextNumber++;
			if (nextNumber == 10) nextNumber -= 10;
		}
	}
}