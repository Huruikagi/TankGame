package tankgame.block;
import java.awt.Color;
import java.awt.Graphics;


/**
 * 深く水が満ちたブロックです。戦車は侵入不可能ですが、弾はこの上を通過できます。
 * @author tsuyoshi-kita
 *
 */
public class WaterBlock extends Block {

	WaterBlock() {
		super(false, true);
	}//侵入不可、通過可
	
	//描画処理
	@Override
	void draw(Graphics g, int i, int j) {
		g.setColor(Color.BLUE);
		g.fillRect(20 * i, 20 * j, 20, 20);
	}

	//通過するのでなにもしない
	@Override
	void hit(int damage) {		
	}
}
