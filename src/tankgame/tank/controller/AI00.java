package tankgame.tank.controller;

import tankgame.block.BlockField;
import tankgame.system.etc.*;
import tankgame.tank.*;


/**
 * 自動で攻撃対象を追い、攻撃するTankController実装クラスです。
 * ・マスに沿ってしか進めません。
 * ・移動と攻撃を同時には行えません。
 * ・味方同士で道をうまく譲れないため、混雑しているとはまります。
 * ・目標戦車を追いかけ、一定距離以下になると攻撃に移行します。
 * ・経路探索のための演算処理を独立したスレッドで逐次行っています。
 * @author tsuyoshi-kita
 *
 */
public class AI00 implements TankController, Runnable {
	
	private VMap map;

	private Tank thisTank;
	private PointDouble myLocation;
	private PointDouble enemyLocation;
	private UnitVector headDirection;

	private int turnSpeed = 1;
	private int moveSpeed = 1;

	private int counter = 0;


	/**
	 * 一つの戦車を攻撃対象として与えます。
	 * @param enemy 攻撃対象とする戦車オブジェクト
	 * @param bf ブロック情報を持ったBlockFieldオブジェクト
	 */
	public AI00(Tank enemy, BlockField bf) {
		enemyLocation = enemy.getCenter();
		map = new VMap(bf);
	}



	private enum ActionNum {
		SELECT,
		MOVE,
		FIRE
	}//行動を指定するenum


	private ActionNum actionShift = ActionNum.SELECT;	//行動指定変数

	/**
	 * 内部で大まかに「攻撃」「移動」の２つの状態を持ち、そのどちらかを行います。
	 */
	public void action() {

		switch (actionShift) {

		case SELECT://次の行動の選択
			actionShift = ActionNum.MOVE;
			action();
			break;
		case MOVE://移動
			move();
			break;
		case FIRE://攻撃
			fire();
			break;
		}
	}


	//敵が進入不可ブロックにいなかった場合に地図の更新を行うメソッド
	private void trace() {
		int ex = enemyLocation.getX() / 20;
		int ey = enemyLocation.getY() / 20;

		if (map.canEnter(ex, ey)) {
			mapping(ex, ey);
		}
		//目標の位置が変なとこじゃない場合に地図を上書き
	}



	//カウンターが0になるまで攻撃を行うメソッド
	private void fire() {
		if (counter >= 0) {
			if ( isLeft(myLocation, enemyLocation, headDirection)) {
				thisTank.turnHead(1);
			} else {
				thisTank.turnHead(-1);
			}
			thisTank.fire(0);
			counter--;
		} else {
			actionShift = ActionNum.MOVE;
			moveShift = MoveNum.DICIDE;
		}
	}




	private boolean isLeft(PointDouble m, PointDouble e, UnitVector d) {
		double mx = m.getXD();
		double my = m.getYD();
		double ex = e.getXD();
		double ey = e.getYD();
		double dx = d.getX();
		double dy = d.getY();

		//まず砲身の向きの単位ベクトルdに対して9時の方向に垂直な単位ベクトルpを定義する。
		//つぎに自機から攻撃対象に向かって伸ばしたMEベクトルを考える。
		//ここで、MEベクトルをd,pについて分解したとき、
		//MEベクトルは定数s,tを使って　ME = sd + tp　と表すことができる。
		//この定数tの符号が正であれば攻撃対象は砲身に対して左方向に存在し、負であれば右方向に存在する。
		//このことから、

		return (ey - my) * dx >= (ex - mx) * dy;	
	}



	private MoveNum moveShift = MoveNum.DICIDE;
	private enum MoveNum {
		DICIDE,
		TURN,
		MOVEFORWORD
	}

	//移動メソッド
	private void move() {

		//砲身の向きを前方に修正しながら
		int dhf = culcDisDeg(thisTank.getFrontDirection().getDegree(), headDirection.getDegree());
		if (dhf > 0) {
			thisTank.turnHead(-1);
		} else if (dhf < 0) {
			thisTank.turnHead(1);
		}


		switch (moveShift) {

		case DICIDE:			//方向の決定	四方で数値の低い方向を選ぶ
			decideCourse();
			break;
		case TURN:				//旋回		選んだ方向に向かって機体を回す
			turn();
			break;
		case MOVEFORWORD:		//前進		選んだ方向に向かって進む
			moveForward();
			break;
		}
	}


	private class VMap {

		private int[][][] data = new int [40][25][3];
		private BlockField field;
		
		VMap(BlockField field) {
			this.field = field;
		}

		synchronized void mapping(int goalX, int goalY) {
			for (int x = 0; x < 40; x++) {
				for (int y = 0; y < 25; y++) {
					if (field.canEntryByIndex(x, y)) {
						data[x][y][0] = Integer.MAX_VALUE;	//入れるブロックは最大値に
					}
					data[x][y][2] = 0;		//再評価フラグ初期化
				}
			}
			for (int x = 0; x < 40; x++) {
				for (int y = 0; y < 25; y++) {
					if ( ! field.canEntryByIndex(x, y)) {
						if (x >= 1) {
							data[x-1][y][0] = -1;
						}
						if (y >= 1) {
							data[x][y-1][0] = -1;
						}
						if (x <= 38) {
							data[x+1][y][0] = -1;
						}
						if (y <= 23) {
							data[x][y+1][0] = -1;
						}
						if (x >= 1 && y >= 1) {
							data[x-1][y-1][0] = -1;
						}
						if (x >= 1 && y <= 23) {
							data[x-1][y+1][0] = -1;
						}
						if (x <= 38 && y <= 23) {
							data[x+1][y+1][0] = -1;
						}
						if (x <= 38 && y >= 1) {
							data[x+1][y-1][0] = -1;
						}
						data[x][y][0] = -1;	
						//入れないブロックは-1に
					}
				}
			}

			data[goalX][goalY][0] = 1;	//スタート地点の評価値を１に
			data[goalX][goalY][1] = thisTank.getFrontDirection().getDegree();//スタート時の進行方向を設定
			data[goalX][goalY][2] = 1; //スタート地点から再評価フラグを立てる

			boolean rewrite;		//どっか書き換えましたフラグ
			int time;
			int dd;

			do {
				rewrite = false;	//まず書き換えましたフラグをfalseに

				for (int x = 0; x < 40; x++) {
					for (int y = 0; y < 25; y++) {
						if (data[x][y][2] == 1) {
							data[x][y][2] = 0;

							if ( (x >= 1) && (data[x-1][y][0] > 0) && (data[x][y][1] != 0) ) {
								//現在ブロックの180方向のブロックに対する評価
								time = data[x][y][0];
								dd = culcDisDeg(data[x][y][1], 180);
								time += Math.abs(dd) / turnSpeed;
								time += 20 / moveSpeed;

								if (time < data[x-1][y][0]) {
									//このブロックからの移動評価値が今保存されている評価値よりも小さければ書き換え
									data[x-1][y][0] = time;
									data[x-1][y][1] = 180;
									data[x-1][y][2] = 1;
									rewrite = true;
								}
							}

							if ( (x <= 38) && (data[x+1][y][0] > 0) && (data[x][y][1] != 180) ) {
								//現在ブロックの0方向のブロックに対する評価
								time = data[x][y][0];
								dd = culcDisDeg(data[x][y][1], 0);
								time += Math.abs(dd) / turnSpeed;
								time += 20 / moveSpeed;

								if (time < data[x+1][y][0]) {
									//このブロックからの移動評価値が今保存されている評価値よりも小さければ書き換え
									data[x+1][y][0] = time;
									data[x+1][y][1] = 0;
									data[x+1][y][2] = 1; 
									rewrite = true;
								}
							}

							if ( (y <= 23) && (data[x][y+1][0] > 0) && (data[x][y][1] != 270)) {
								//現在ブロックの90方向のブロックに対する評価
								time = data[x][y][0];
								dd = culcDisDeg(data[x][y][1], 90);
								time += Math.abs(dd) / turnSpeed;
								time += 20 / moveSpeed;

								if (time < data[x][y+1][0]) {
									//このブロックからの移動評価値が今保存されている評価値よりも小さければ書き換え
									data[x][y+1][0] = time;
									data[x][y+1][1] = 90;
									data[x][y+1][2] = 1;
									rewrite = true;
								}
							}

							if ( (y >= 1) && (data[x][y-1][0] > 0) && (data[x][y][1] != 90) ) {
								//現在ブロックから270方向のブロックに対する評価
								time = data[x][y][0];
								dd = culcDisDeg(data[x][y][1], 270);
								time += Math.abs(dd) / turnSpeed;
								time += 20 / moveSpeed;
								time += dd / turnSpeed;
								time += 20 / moveSpeed;

								if (time < data[x][y-1][0]) {
									//このブロックからの移動評価値が今保存されている評価値よりも小さければ書き換え
									data[x][y-1][0] = time;
									data[x][y-1][1] = 270;
									data[x][y-1][2] = 1;
									rewrite = true;
								}
							}
						}
					}
				}
			} while (rewrite) ;	//書き換えなくなったらループを抜ける（評価地図の完成）
		}

		synchronized int getData(int x, int y) {
			return data[x][y][0];
		}

		synchronized boolean canEnter(int x, int y) {
			return data[x][y][0] != -1 && data[x][y][0] != Integer.MAX_VALUE;
		}
	}


	//地図の更新メソッド
	private void mapping(int goalX, int goalY) {
		map.mapping(goalX, goalY);
	}


	private int preX, preY ,dir = 0;

	//進路選択
	private void decideCourse() {

		int x = myLocation.getX() / 20;
		int y = myLocation.getY() / 20;
		int min = Integer.MAX_VALUE;	//四方で一番数字が小さい方向

		if (map.getData(x, y) >= 300) {	//このくらい近づくまで続ける

			x = myLocation.getX() / 20;
			y = myLocation.getY() / 20;				

			if (x >= 1 && min >= map.getData(x-1, y) && map.canEnter(x-1, y)) {
				min = map.getData(x-1, y);
				dir = 180;
			}
			if (y >= 1 && min >= map.getData(x, y-1) && map.canEnter(x, y-1)) {
				min = map.getData(x, y-1);
				dir = 270;
			}
			if (y <= 23 && min >= map.getData(x, y+1) && map.canEnter(x, y+1)) {
				min = map.getData(x, y+1);
				dir = 90;
			}
			if (x <= 38 && min >= map.getData(x+1, y) && map.canEnter(x+1, y)) {
				min = map.getData(x+1, y);
				dir = 0;
			}

			if (min <= 200) {
				actionShift = ActionNum.FIRE;
			} else {
				moveShift = MoveNum.TURN;
			}

			preX = x;
			preY = y;

		} else {
			counter = 66;
			actionShift = ActionNum.FIRE;
		}
	}


	//旋回メソッド
	private void turn() {

		int dd = culcDisDeg(thisTank.getFrontDirection().getDegree(), dir);

		if (dd < 0) {
			thisTank.turn(-turnSpeed);
		} else if (dd > 0) {
			thisTank.turn(turnSpeed);
		} else {
			moveShift = MoveNum.MOVEFORWORD;
			move();
		}
	}

	//前進メソッド
	private void moveForward() {

		int x = myLocation.getX() / 20;
		int y = myLocation.getY() / 20;

		if ( x == preX && y == preY ) {
			thisTank.move(moveSpeed);
		} else {
			moveShift = MoveNum.DICIDE;
			move();
		}
	}


	//二つの角度（方角）の差を求めるメソッド
	//右回りと左回りで絶対値が小さい方の値を返す
	private int culcDisDeg(int from, int to) {

		int a = to - from;
		int b = to - (from + 360);

		if (Math.abs(a) <= Math.abs(b)) {
			return a;
		} else {
			return b;
		}
	}


	boolean traceFlag = false;

	@Override
	public void run() {

		while (traceFlag) {
			trace();
			try {
				Thread.sleep(666);
			} catch (InterruptedException e) {
			}
		}
	}
	
	@Override
	public void setTank(Tank t) {
		thisTank = t;
		myLocation = t.getCenter();
		headDirection = t.getHeadDirection();
	}

	/**
	 * 目標戦車までの経路探索をするためのスレッドを起動します。
	 */
	@Override
	public void start() {
		traceFlag = true;
		new Thread(this, thisTank.getName() + "'s controller thread").start();
	}

	/**
	 * 探索スレッドを終了します。
	 */
	@Override
	public void dead() {
		traceFlag = false;
		thisTank = null;
	}

}