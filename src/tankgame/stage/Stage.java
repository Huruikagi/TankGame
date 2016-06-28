package tankgame.stage;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import tankgame.block.BlockField;
import tankgame.rule.*;
import tankgame.system.gui.GameInfomationPane;
import tankgame.tank.Tank;


/**
 * ゲームステージを表すクラスです。
 * ステージに応じたブロックや戦車などのオブジェクトを保持し、それらに描画通知を伝達します。
 * また、{@link tankgame.rule.GameRuler}オブジェクトを保有し、各処理を起動します。
 * @author tsuyoshi-kita
 * @see tankgame.tank.Tank
 * @see tankgame.block.BlockField
 * @see tankgame.rule.GameRuler
 */
public class Stage {

	@SuppressWarnings("unused")
	private String name;
	
	private BlockField field;		//ブロック情報
	private Tank[] tanks;			//戦車情報
	private GameRuler gm;			//処理するやつ
	private GameInfomationPane gip;	//付加情報描くやつ
	
	/**
	 * 
	 * @param keyStates キーボード情報
	 * @param name ステージ名
	 * @param tanks このステージで使用するTankオブジェクトの配列
	 * @param upRules 上書きするDefaultRule
	 * @param opRules 追加するRule
	 * @param field このステージのBlockFieldオブジェクト
	 */
	public Stage(boolean[] keyStates, String name, Tank[] tanks, DefaultRule[] upRules, Rule[] opRules, BlockField field) {
		this.name = name;
		this.tanks = tanks;
		this.field = field;
		gm = new GameRuler(field, tanks);
		gm.updateRule(upRules);
		gm.addRule(opRules);
		gip = new GameInfomationPane();
	}

	/**
	 * 各オブジェクトを描画します。
	 * @param g Graphicsオブジェクト
	 * @param io ImageObserverオブジェクト
	 */
	public void draw(Graphics g, ImageObserver io) {
		field.draw(g);

		for (Tank t : tanks) {
			t.draw(g, io);
		}
	}

	/**
	 * 定時行動通知です。GameRulerオブジェクトに伝達します。
	 */
	public void onTime() {
		gm.onTime();
	}
}