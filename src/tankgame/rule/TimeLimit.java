package tankgame.rule;

import java.util.Calendar;


/**
 * 一定時間経過でゲームオーバーになるルールです。
 * コンストラクタでの設定により勝敗いずれかのイベントを生成します。
 * @author tsuyoshi-kita
 *
 */
public class TimeLimit extends Rule {
	
	private boolean start = false;	//ゲームスタートフラグ
	private Calendar limit;	//タイムリミット時刻
	private int time;			//設定された制限時間（秒）
	private byte type;		//設定された勝敗タイプ
	
	/**
	 * タイムリミット時に勝利とする際に設定します。
	 */
	public static final byte WIN = 0;
	
	/**
	 * タイムリミット時に敗北とする際に設定します。
	 */
	public static final byte LOSE = 1;
	
	/**
	 * @param type {@link TimeLimit#WIN}または{@link TimeLimit#LOSE}を指定します。
	 * @param second タイムリミットまでの制限時間（秒）を指定します。
	 */
	public TimeLimit(byte type, int second) {
		super(2000, "", "");
		time = second;
		this.type = type;
	}

	
	//判定メソッド
	@Override
	void check() {
		if (start) {
			if (Calendar.getInstance().compareTo(limit) > 0) {
				//タイムリミットになったとき
				if (type == WIN) {	//typeに応じたイベントを発生
					win();
				} else {
					lose();
				}
			}	
		} else {
			limit = Calendar.getInstance();
			limit.add(Calendar.SECOND, time);
			start = true;
			//スタートしていなければこの最初の判定の際に初期化する。
		}
	}

	//何も要求しない
	@Override
	void request() {
	}
}