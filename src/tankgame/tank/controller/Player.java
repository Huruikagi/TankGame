package tankgame.tank.controller;
import tankgame.tank.*;

/**
 * プレイヤー機にセットするTankController実装クラスです。
 * {@link tankgame.system.etc.KeyInput}クラスで生成された、キーボード情報を表す配列をコンストラクタで要求し、
 * フレーム毎にその情報に基づいた動を行います。
 * @author tsuyoshi-kita
 */
public class Player implements TankController {

	private boolean[] keyStates;
	private Tank player;
	
	/**
	 * @param boo キーボード状態を表すboolean配列
	 * @see tankgame.system.etc.KeyInput
	 */
	public Player(boolean[] boo) {
		keyStates = boo;
	}
	
	/**
	 * キーボード情報に応じて戦車に行動させます。
	 */
	@Override
	public void action() {
		if (keyStates[0]) {
			player.turn(Tank.LEFT);
		}
		if (keyStates[1]) {
			player.move(Tank.FORWARD);
		}
		if (keyStates[2]) {
			player.turn(Tank.RIGHT);
		}
		if (keyStates[3]) {
			player.move(Tank.BACK);
		}
		if (keyStates[4]) {
			player.fire(0);
		}
		if (keyStates[5]) {
			player.turnHead(Tank.LEFT);
		}
		if (keyStates[6]) {
			player.turnHead(Tank.RIGHT);
		}
		if (keyStates[7]) {
			player.fire(1);
		}
		if (keyStates[8]) {
			player.fire(2);
		}
	}
	

	@Override
	public void setTank(Tank t) {
		player = t;
	}
	
	@Override
	public void dead() {
		player = null;
	}

	@Override
	public void start() {
	}
}
