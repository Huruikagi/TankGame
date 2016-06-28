package tankgame.rule;

import tankgame.block.BlockField;
import tankgame.system.etc.PointDouble;
import tankgame.tank.Tank;


/**
 * 戦車とブロックの衝突判定・処理を行う基本ルールです。衝突した際は{@link tankgame.tank.Tank#lock(int)}メソッドにより一時的に戦車の行動を制限します。
 * @author tsuyoshi-kita
 * @see tankgame.tank.Tank
 * @see tankgame.block.BlockField
 */
public class TankCollisionBlock extends DefaultRule {

	private Tank[] tanks;
	private PointDouble[][] tankCorners;
	//戦車の角の位置
	
	private BlockField field;
	
	
	public TankCollisionBlock() {
		super(1001, "", "");
	}
	

	@Override
	void check() {
		for (int j = 0; j < tanks.length; j++) {	//各戦車に関して
			if (tanks[j].isAlive()) {			//生きてたら
				for (int i = 0; i < tankCorners[0].length; i++) {//それぞれの座標に関して
					if (! field.canEntry(tankCorners[j][i].getX(), tankCorners[j][i].getY()))
						tanks[j].lock(i);		//入れなかったらロック
				}
			}
		}
	}

	
	void request() {
		tanks = reqTanks();
		tankCorners = new PointDouble[tanks.length][];
		//戦車の台数分用意

		for (int i = 0; i < tanks.length; i++) {
			tankCorners[i] = tanks[i].getCorners();
		}//それぞれの戦車から貰ってくる

		field = reqField();
	}
}