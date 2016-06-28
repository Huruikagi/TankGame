package tankgame.block;
import java.awt.Graphics;


/**
 * ステージのブロック配置情報を保持し、パッケージ外部とのやりとりを仲介します。
 * @see Block
 * @author tsuyoshi-kita
 *
 */
public class BlockField {

	private Block[][] blocks;	//配置されているブロック
	
	/**
	 * デフォルトで４辺に{@link UnbreakableBlock}が配置されます。
	 */
	public BlockField() {
		blocks = new Block[40][25];
		
		for (int i = 0; i < 25; i ++) {
			setBlock(Block.UNBREAK, 0, i);
			setBlock(Block.UNBREAK, 39, i);
		}
		for (int i = 1; i < 39; i++) {
			setBlock(Block.UNBREAK, i, 0);
			setBlock(Block.UNBREAK, i, 24);
		}
	}
	
	
//	//ステージ幅を設定
//	void setBlocks(int i, int j) {
//		blocks = new Block[i][j];
//	}
	
	
	/**
	 * 指定された種別のブロックを、指定された位置に配置します。
	 * 位置の指定はx座標ではなく配列の添字を指定して行う点に注意してください。
	 * {@literal （0<=x<=39, 0<=y<=24）}
	 * @param type Blockクラスのstatic定数よりタイプを指定する
	 * @param x x方向の配置位置
	 * @param y y方向の配置位置
	 */
	public void setBlock(char type, int x, int y) {
		blocks[x][y] = Block.getInstance(type);
	}

	
	/**
	 * 保持している各ブロックを順番に描画します。
	 * @param g 描画するGraphicsオブジェクト
	 */
	public void draw(Graphics g) {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[0].length; j++) {
				if (blocks[i][j] != null)
					blocks[i][j].draw(g, i, j);
			}
		}
	}

	
	/**
	 * 特定の座標を弾が通過できるか判定します。衝突判定が発生した場合、
	 * 該当するBlockのhit(int damage)メソッドを呼び出します。
	 * @param x 判定するx座標
	 * @param y 判定するy座標
	 * @param damage 弾の持つダメージ値
	 * @return 通過できるとき{@code true}
	 */
	public boolean canThrough(int x, int y, int damage) {
		int i = x / 20;
		int j = y / 20;
		
		if (blocks[i][j] != null) {
			//その位置にブロックがあった場合
			blocks[i][j].hit(damage);
			return blocks[i][j].getCanThrough();
			
		}else{
			//ブロックがない場合
			return true;
		}
	}
	
	
	/**
	 * 特定の座標に戦車が侵入できるか判定します。
	 * @param x 判定するx座標
	 * @param y 判定するy座標
	 * @return 侵入可能なときtrue
	 */
	public boolean canEntry(int x, int y) {
		int i = x / 20;
		int j = y / 20;
		
		if (blocks[i][j] != null) {
			//ブロックがある場合
			return blocks[i][j].getCanEnt();
		
		}else{
			//ブロックがない場合
			return true;
		}
	}
	
	/**
	 * 座標の代わりに配列の添字を引数に渡し、戦車が侵入できるブロックかを判定します。
	 * @param x x方向の添字
	 * @param y y方向の添字
	 * @return 侵入可能なときtrue
	 */
	public boolean canEntryByIndex(int x, int y) {
		if (blocks[x][y] != null) {
			//ブロックがある場合
			return blocks[x][y].getCanEnt();
		}else{
			//ブロックがない場合
			return true;
		}
	}
}