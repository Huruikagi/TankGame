package tankgame.rule;

import java.util.*;

import tankgame.block.*;
import tankgame.tank.*;

//ゲーム内の判定・処理を行うクラス

public final class GameRuler {

	static private boolean endFlag = false;
	static private boolean winFlag = false;
	static private String message = "";

	private Tank[] tanks;		//戦車情報
	private BlockField field;	//ブロック情報
	private Rule[] defaultRules;//デフォルトルール
	private Rule[] rules;		//適用ルール	

	public GameRuler(BlockField field, Tank...tanks){
		defaultRules = new Rule[4];
		defaultRules[0] = new TankCollisionBlock();
		defaultRules[1] = new TankCollisionTank();
		defaultRules[2] = new WeaponCollisionBlock();
		defaultRules[3] = new WeaponHitTank();
		
		Rule.setGameRuler(this);
		this.field = field;
		this.tanks = tanks;
	}

	/**
	 * 
	 * @param newRules 置き換えるDefaultRule
	 * @throws RuntimeException このメソッドが呼ばれる以前にaddRuleメソッドが呼ばれていた際にエラー
	 */
	public void updateRule(DefaultRule...newRules) throws RuntimeException {
		if (rules != null) {
			throw new RuntimeException("updateRuleメソッドはaddRuleメソッド以前に実行する。");
		}
		for (int i = 0; i < newRules.length; i++) {
			for (int j = 0; j < defaultRules.length; j++) {
				if (newRules[i].getClass() == defaultRules[j].getClass()) {
					defaultRules[j] = newRules[i];
					break;
				}
			}
		}
	}

	
	public void addRule(Rule...optionalRules) {

		rules = new Rule[defaultRules.length + optionalRules.length];
		System.arraycopy(defaultRules, 0, rules, 0, defaultRules.length);
		System.arraycopy(optionalRules, 0, rules, defaultRules.length, optionalRules.length);
		//デフォルトで置いておくルールに、引数で渡されてきたルールを追加する

		for (Rule r : rules) {
			r.request();
		}//各ルールに情報をセットする

		Arrays.sort(rules);
		//引数のルール配列を（実行優先度が高い順に）ソートしておく

	}


	//各ルールを動作させる
	void checkRules() {
		for (Rule r : rules)
			r.check();
	}


	//勝利メソッド
	//ルールから呼び出される
	void win(Rule r) {
		winFlag = true;
		endFlag = true;
		gameOver();
	}

	//敗北メソッド
	void lose(Rule r) {
		endFlag = true;
		gameOver();
	}

	private void gameOver() {
		for (Tank t : tanks) {
			t.kill();
		}
	}



	//定時処理
	public void onTime() {

		for(Tank t : tanks) {
			if(t.isAlive()) t.action();
		}//（生きてたら）各戦車に動作させる

		checkRules();		//各判定処理の実行

	}
	
	
	


	//オブジェクトのgetter　同パッケージ内にのみ公開
	Tank[] getTanks() {
		return tanks;
	}

	BlockField getField() {
		return field;
	}

	public static boolean getEndFlag() {
		return endFlag;
	}

	public static boolean getWinFlag() {
		return winFlag;
	}

	static String getMessage() {
		return message;
	}
}