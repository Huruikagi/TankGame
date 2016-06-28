package tankgame.block;
import java.awt.Graphics;


/**
 * ステージに配置するブロックの抽象クラスです。
 * @author tsuyoshi-kita
 *
 */
public abstract class Block {
	
	protected boolean canEnter;		//戦車が進入可能か
	protected boolean canThrough;	//弾が通過可能か
	
	Block(boolean enter, boolean through) {
		canEnter = enter;
		canThrough = through;
	}
	
	//描画メソッド　BlockFieldから呼び出す
	abstract void draw(Graphics g, int i, int j);

	//戦車が侵入可能かを返す
	boolean getCanEnt() {
		return canEnter;
	}
	
	//弾が通過可能かを返す
	boolean getCanThrough() {
		return canThrough;
	}

	//弾がこのブロックに命中した際の処理
	//damage 命中した弾のダメージ値
	abstract void hit(int damage);
	
	/**
	 * ブロックが設置されていない状態を表す
	 */
	public static final char NULL = '0';
	
	/**
	 * {@link UnbreakableBlock}を表す
	 */
	public static final char UNBREAK = '1';
	
	/**
	 * {@link BrickBlock}を表す
	 */
	public static final char BRICK = '2';
	
	/**
	 * {@link SafetyBlock}を表す
	 */
	public static final char SAFETY = '3';
	
	/**
	 * {@link WaterBlock}を表す
	 */
	public static final char WATER = '4';
	

	//ファクトリーメソッド
	//サブクラスのインスタンスを生成して返却する
	static Block getInstance(char type) {
		
		Block block = null;
		
		switch (type) {
		case UNBREAK:
			block = new UnbreakableBlock();
			break;
		case BRICK:
			block = new BrickBlock();
			break;
		case SAFETY:
			block = new SafetyBlock();
			break;
		case WATER:
			block = new WaterBlock();
			break;
		}
		
		return block;
	}
}