package tankgame.rule;

import tankgame.block.BlockField;
import tankgame.system.etc.PointDouble;
import tankgame.tank.Tank;
import tankgame.tank.weapon.Weapon;


/**
 * 武器の弾とブロックの衝突判定・処理を行う基本ルールです。
 * @author tsuyoshi-kita
 *
 */
public class WeaponCollisionBlock extends DefaultRule {
	
	private Tank[] tanks;
	private BlockField field;

	public WeaponCollisionBlock() {
		super(1000, "", "");
	}

	@Override 
	void check() {
		for (Tank tank : tanks) {
			//各戦車に関して
			if (tank.isAlive()) {
				//その戦車が生きてたら
				Weapon[] weapons = tank.getWeapons();//その戦車が持ってる兵装
				
				for (Weapon w : weapons) {//それぞれに関して

					boolean[] exs = w.getIsExist();
					PointDouble[] loc = w.getLocations();
					int damage = w.getDamage();
					//弾の情報を取得

					for (int i = 0; i < exs.length; i++) {//各弾に関して
						if (exs[i]) {//存在していたら
							if (! field.canThrough(loc[i].getX(), loc[i].getY(), damage)) {
								//当たり判定。当たったら消す。
								exs[i] = false;
							}
						}
					}
				}
			}
		}
	}
	

	@Override
	void request() {
		tanks = reqTanks();
		field = reqField();
	}
}
