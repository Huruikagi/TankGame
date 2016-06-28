package tankgame.block;

import java.awt.Color;
import java.awt.Graphics;


/**
 * ダメージにより破壊されるレンガブロッククラスです。（描くのが面倒だから見た目は全然レンガじゃない）<br>
 * 内部情報として耐久度(HP)を持ち、hitメソッドにより減少します。HPがなくなった時点でnullなブロックとほぼ同じ振る舞いをするようになります。
 * @author tsuyoshi-kita
 *
 */
public class BrickBlock extends Block {

	private boolean hit = false;	//命中フラグ
	private int durability = 100;	//耐久度（HP）

	BrickBlock() {
		super(false, false);
		//戦車も弾も通過不可
	}

	
	//描画処理
	@Override
	void draw(Graphics g, int i, int j) {
		if (canEnter) {
			//耐久0以下でブロックを描画しなくなる
		} else if (hit) {
			//当たったときは明るい色にする
			g.setColor(Color.ORANGE.brighter());
			g.fillRect(20 * i, 20 * j, 20, 20);
			hit = false;	//フラグを折る
		} else {
			//普通のとき
			g.setColor(Color.ORANGE);
			g.fillRect(20 * i, 20 * j, 20, 20);
		}
	}

	
	//弾が命中した際の処理
	@Override
	void hit(int damage) {
		if (! canEnter) {
			//HPが残っている間だけ処理する
			durability -= damage;
			hit = true;  //hitフラグを立てる
			if (durability <= 0) {
				//破壊判定　耐久度が0以下であれば破壊、ブロックの性質を変更する
				canEnter = true;
				canThrough = true;
			}
		}
	}
}