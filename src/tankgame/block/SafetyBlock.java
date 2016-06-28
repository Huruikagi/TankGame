package tankgame.block;
import java.awt.Color;
import java.awt.Graphics;


/**
 * 安全地帯ブロックです。戦車は侵入可能ですが、弾は通過できません。
 * @author tsuyoshi-kita
 *
 */
public class SafetyBlock extends Block {

	SafetyBlock() {
		super(true, false);
	}//戦車は侵入可能、弾は通過不可
	
	
	//描画判定
	@Override
	void draw(Graphics g, int i, int j) {
		g.setColor(Color.PINK);
		g.fillRect(20 * i, 20 * j, 20, 20);
	}

	//衝突判定
	@Override
	public void hit(int damage) {
	}//あたってもなにもしない

}
