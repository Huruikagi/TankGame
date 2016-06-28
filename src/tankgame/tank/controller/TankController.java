package tankgame.tank.controller;

import tankgame.tank.*;

/**
 * 戦車を操縦できることを表すインターフェースです。
 * プレイヤーやAIがこのインターフェースを実装します。
 * @author tsuyoshi-kita
 * @see tankgame.tank.Tank
 *
 */
public interface TankController {
	
//	public static final int PLAYER = 0;
//	public static final int AI00 = 1;
//	public static final int AI01 = 2;
	
	/**
	 * このコントローラが操縦対象とする戦車を設定します。
	 * 通常は{@link tankgame.tank.Tank#setController(TankController)}の
	 * 内部で自動的に呼び出されます。
	 * @param t 操縦対象のTankオブジェクト
	 */
	public abstract void setTank(Tank t);
	
	/**
	 * 毎フレームに１度呼び出されるメソッドです。
	 * このメソッドの中で、操縦方法を実装します。
	 */
	public abstract void action();
	
	/**
	 * 操縦対象の戦車が破壊された時に呼び出されます。
	 * 必要であれば終了処理を実装します。
	 */
	public abstract void dead();
	
	/**
	 * 開始時に１度呼び出されるメソッドです。
	 */
	public abstract void start();
	
}