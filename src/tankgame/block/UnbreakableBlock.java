package tankgame.block;
import java.awt.Color;
import java.awt.Graphics;


/**
 * 破壊不可ブロックです。侵入不可かつ通過不可になります。
 * @author tsuyoshi-kita
 */
public class UnbreakableBlock extends Block {

	UnbreakableBlock() {
		super(false, false);
	}//戦車も弾も侵入できない。

	private boolean hit; //あたったフラグ

	//描画処理
	@Override
	void draw(Graphics g, int i, int j) {

		if (hit) {
			//当たったときは外枠をちらっと明るくする
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(20 * i, 20 * j, 20, 20);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(20 * i + 1, 20 * j + 1, 18, 18);
			hit = false;
		} else {
			g.setColor(Color.GRAY);
			g.fillRect(20 * i, 20 * j, 20, 20);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(20 * i + 1, 20 * j + 1, 18, 18);
		}
	}

	//当たり判定時の処理
	@Override
	void hit(int damage) {
		hit = true;
	}
	
}