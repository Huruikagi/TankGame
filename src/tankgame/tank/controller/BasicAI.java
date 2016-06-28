package tankgame.tank.controller;

import tankgame.system.etc.PointDouble;
import tankgame.system.etc.UnitVector;

public class BasicAI {
	
	//２点間の距離測定メソッド
	public int rangeFind(PointDouble m, PointDouble e) {
		return
		(int) Math.sqrt( Math.pow(m.getXD() - e.getX(),2) 
				+ Math.pow(m.getYD() - e.getYD(), 2));
		//√((c−a)^2+(d−b)^2)
	}
	
	
	//左右判定
	public boolean isLeft(PointDouble m, PointDouble e, UnitVector d) {
		double mx = m.getXD();
		double my = m.getYD();
		double ex = e.getXD();
		double ey = e.getYD();
		double dx = d.getX();
		double dy = d.getY();

		return (ey - my) * dx >= (ex - mx) * dy;	
	}
	
	
	//二つの角度（方角）の差を求めるメソッド
	//右回りと左回りで絶対値が小さい方の値を返す
	public int angleDiffalence(int from, int to) {
		int a = to - from;
		int b = to - (from + 360);

		if (Math.abs(a) <= Math.abs(b)) {
			return a;
		} else {
			return b;
		}
	}
}