package tankgame.rule;

import tankgame.block.BlockField;
import tankgame.tank.Tank;


/**
 * ゲーム中の判定・処理を行う各ルールの基底となるクラスです。
 * @author tsuyoshi-kita
 */
public abstract class Rule implements Comparable<Rule> {
	
	static private GameRuler gm;//GameRulerオブジェクトを保持
	
	private int priority;		//処理優先度
	//private int position;		//ルールの立ち位置　これが同じ値のものはGameRulerにセットされない
	
	private String briefingMessage;		//ルール毎のメッセージ
	private String message2;		//ステージ開始前とかに出したらいいな的な　未使用
	

	Rule (int priority, String briefingMessage, String s2) {
		this.priority = priority;
		this.briefingMessage = briefingMessage;
		message2 = s2;
	}
	
	static void setGameRuler(GameRuler gameRuler) {
		gm = gameRuler;
	}

	//判定メソッド
	abstract void check();
	
	//必要な情報をGameRulerに要求する
	abstract void request();
	
	protected Tank[] reqTanks() {
		return gm.getTanks();
	}
	
	protected BlockField reqField() {
		return gm.getField();
	}

	protected void win() {
		gm.win(this);
	}
	
	protected void lose() {
		gm.lose(this);
	}	
	
	/**
	 * サブクラスにより設定された処理優先度を基準に比較されます。
	 */
	@Override
	public int compareTo(Rule other) {
		return this.priority - other.priority;
	}
	
	String getBriefingMessage() {
		return briefingMessage;
	}
	
	String getMessage2() {
		return message2;
	}
}