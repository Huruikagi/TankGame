package tankgame.rule;

import tankgame.tank.Tank;


/**
 * 指定の戦車が破壊された際に勝利イベントを発生します。「全破壊」か、「いずれかの破壊」かをコンストラクタで指定します。
 * @author tsuyoshi-kita
 *
 */
public class Defeat extends Rule {

	private Tank[] tanks;
	private int[] idx;
	private byte type;
	
	/**
	 * 目標すべて破壊で勝利イベントを発生する際に指定する。
	 */
	public static final byte ALL = 0;
	
	/**
	 * いずれかの破壊で勝利イベントを発生する際に指定する。
	 */
	public static final byte ANY = 1;
	
	public Defeat(byte type, int...tgets) {
		super(0, "すべての敵を撃破してください。", "すべての敵を撃破しました！");//（処理優先度低）
		this.type = type;
		tanks = new Tank[tgets.length];
		idx = tgets;
	}
	
	@Override
	void request() {
		Tank[] tmp = reqTanks();
		int count = 0;
		for (int i = 0; i < idx.length; i++) {
			tanks[count] = tmp[idx[i]];
			count++;
		}
	}

	@Override
	void check() {
		//設定されたtypeに応じてどちらかで判定
		if (type == ALL) {
			all();
		} else {
			any();
		}
	}
	
	private void all() {
		for (int i = 0; i < tanks.length; i++) {
			//System.out.println(tanks[i].getName());
			if (tanks[i].isAlive())
				return;//生存しているのがいたらスルー
		}
		win();
	}
	
	private void any() {
		for (int i = 0; i < tanks.length; i++) {
			if (! tanks[i].isAlive()) {
				win();//死亡しているのを見つけたら勝利
				return;
			}
		}
	}
}
