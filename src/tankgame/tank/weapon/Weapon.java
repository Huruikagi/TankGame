package tankgame.tank.weapon;
import java.awt.Graphics;

import tankgame.system.etc.PointDouble;
import tankgame.system.etc.UnitVector;


//武器の抽象クラス

public abstract class Weapon {
	
	protected boolean[] isExist;		//アクティブかどうか
	protected PointDouble[] center;		//現在の中心座標
	protected UnitVector[] direction;	//方向
	
	protected int coolTime;		//クールタイムの長さ
	protected int damage;		//攻撃力
	
	public int getDamage() {
		return damage;
	}
	
	//発射メソッド
	public abstract void shoot(PointDouble sl, UnitVector sd);
	
	//移動メソッド
	public abstract void move();
	
	//描画メソッド
	public abstract void draw(Graphics g);
	
	
	//ゲッター
	public boolean[] getIsExist() {
		return isExist;
	}
	
	public PointDouble[] getLocations() {
		return center;
	}
	
	
	public static final int TANKGUN = 0;
	public static final int HUGEGUN = 1;
	public static final int MINE = 2;
	
	public static Weapon getInstance(int weaponType) {
		
		Weapon weapon = null;
		
		switch (weaponType) {
		
		case TANKGUN:
			weapon = new TankGun();
			break;
		case HUGEGUN:
			weapon = new HugeGun();
			break;
		case MINE:
			weapon = new Mine();
			break;
		}
		
		return weapon;
	}
}