package tankgame.system.etc;
//２次元座標系での位置情報をdouble型で保持するクラス
//内部的にはdoubleだがgetterは基本的にintで返す（使いやすいから）
//Point2Dとか知らない

public class PointDouble {
	
	private double x;		//x座標
	private double y;		//y座標
	
	
	//コンストラクタ　位置情報を設定
	public PointDouble (double x, double y) {
		set(x, y);
	}
	
	//引数無しコンストラクタは原点に配置
	public PointDouble() {
		set(0,0);
	}

	
	//セッター　情報を上書き
	public void set (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	//x座標を引数分ずらすメソッド
	public void addX(double dX) {
		x += dX;
	}
	
	//y座標を引数分ずらすメソッド
	public void addY(double dY) {
		y += dY;
	}

	
	//ゲッター
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public double getXD() {
		return x;
	}
	
	public double getYD() {
		return y;
	}

}