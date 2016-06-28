package tankgame.rule;

import tankgame.system.etc.PointDouble;
import tankgame.tank.*;


/**
 * 戦車同士の衝突判定・処理を行う基本ルールです。衝突した際には{@link tankgame.tank.Tank#lock(int)}を呼び出し、一時的に戦車の行動を制限します。
 * @author tsuyoshi-kita
 * @see tankgame.tank.Tank
 */
public class TankCollisionTank extends DefaultRule {

	private Tank[] tanks;
	private PointDouble[][] corners;

	public TankCollisionTank() {
		super(1000, "", "");
	}

	@Override 
	void check() {
		for (int i = 0; i < tanks.length; i++) {
			for (int j = 0; j < tanks.length; j++) {
				if (i != j && tanks[i].isAlive() && tanks[j].isAlive()) {
					for (int k = 0; k < corners[0].length; k++) {
						if (tanks[j].inside(corners[i][k]))
							tanks[i].lock(k);
					}
				}
			}
		}
	}

	@Override
	void request() {
		tanks = reqTanks();
		corners = new PointDouble[tanks.length][];
		for (int i = 0; i < tanks.length; i++) {
			corners[i] = tanks[i].getCorners();
		}
	}
}
