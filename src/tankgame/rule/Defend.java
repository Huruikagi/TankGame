package tankgame.rule;

import tankgame.tank.Tank;


/**
 * 指定の戦車が破壊された際に敗北イベントを発生します。「全破壊」か、「いずれかの破壊」かをコンストラクタで指定します。
 * @author tsuyoshi-kita
 *
 */
public class Defend extends Rule {

	private Tank[] tanks;
	private int[] idx;
	private byte type;

	/**
	 * すべての対象が破壊された時に敗北とする際に指定します。
	 */
	public final static byte ALL = 0;

	/**
	 * 対象のいずれか１つでも破壊されれば敗北とする際に指定します。
	 */
	public final static byte ANY = 1;


	public Defend(byte type, int...tgets) {
		super(100, "", "撃破されてしまいました…");
		this.type = type;
		this.idx = tgets;
		tanks = new Tank[idx.length];
	}


	@Override
	void check() {
		//typeによって異なる処理を行う
		if (type == ALL) {
			all();
		} else {
			any();
		}
	}

	private void all() {
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].isAlive())
				return;
		}
		lose();
	}

	private void any() {
		for (int i = 0; i < tanks.length; i++) {
			if (! tanks[i].isAlive()) {
				lose();
				return;
			}
		}
	}

	@Override
	void request() {
		Tank[] tmp = reqTanks();
		int count = 0;
		for (int i = 0; i < idx.length; i++) {
			tanks[count] = tmp[idx[i]];
			count++;
		}
		//必要な戦車の情報だけ取り出して保有しておく
	}
}
