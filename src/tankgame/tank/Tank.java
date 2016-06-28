package tankgame.tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import tankgame.system.etc.*;
import tankgame.tank.controller.*;
import tankgame.tank.weapon.*;
import static tankgame.tank.PointsAroundTank.*;


/**
 * このゲームの中核となる戦車クラス。
 * @author tsuyoshi-kita
 *
 */
public final class Tank {

	private boolean alive = true;		//生存フラグ
	private boolean hited = false;		//あてられたフラグ

	private String name;
	private int hp;				//HP
	private Color mainColor;		//色

	private PointDouble center;				//中心座標
	private PointDouble[] corners = new PointDouble[8];	//周辺座標
	private boolean[] locked = new boolean[corners.length];	//角がぶつかっているか

	private UnitVector frontDirection;		//車体の正面の向き
	private UnitVector headDirection;		//砲首の向き
	private final int SPEED = 1;		//はやさ

	/**
	 * 「前」を表す定数です
	 */
	public static final int FORWARD = 2;
	/**
	 * 「後ろ」を表す定数です。
	 */
	public static final int BACK = -1;
	/**
	 * 「右」を表す定数です。
	 */
	public static final int RIGHT = 1;
	/**
	 * 「左」を表す定数です。
	 */
	public static final int LEFT = -1;

	private Weapon[] weapons = new Weapon[3];
	//保有している武器

	private TankController controller;
	//操縦者


	/**
	 * 
	 * @param name この戦車の名前文字列
	 * @param hitPoint 耐久度(HP)
	 * @param p 初期配置位置
	 * @param d 初期配置における車体の向き
	 * @param c この戦車の車体色
	 */
	public Tank (String name, int hitPoint, PointDouble p, UnitVector d, Color c) {
		//HPと位置と向きと色を指定

		this.name = name;
		hp = hitPoint;
		mainColor = c;

		center = p;
		frontDirection = d;
		headDirection = new UnitVector(d.getDegree());

		weapons[0] = Weapon.getInstance(Weapon.TANKGUN);
		weapons[1] = Weapon.getInstance(Weapon.HUGEGUN);
		weapons[2] = Weapon.getInstance(Weapon.MINE);
		//武器　ここも選択できても良いかもしれない
		//一応つけかえても問題ないはず

		for (int i = 0; i < corners.length; i++) {
			corners[i] = new PointDouble();
		}
		culcCorners();
	}


	/**
	 * この戦車にTankControllerオブジェクトを設定します。
	 * ゲームスタート後に変更することも可能で、null値を設定することもできます。
	 * @param controller この戦車に設定するTankController
	 */
	public void setController(TankController controller) {
		if (this.controller != null) {
			this.controller.dead();
		}
		this.controller = controller;
		controller.setTank(this);
		controller.start();
	}



	//貰ってきたGraphicsインスタンスを直接回転させてしまうことにはちょっと抵抗があるので
	//それを回避するためにダブルバッファリング的なことをする都合上
	//drawImageメソッドを使うため、それに必要なImageObserverも引数で貰ってくる
	/**
	 * 描画メソッドです。保有しているWeaponオブジェクトにも描画通知を伝達します。<br>
	 * この戦車が既に破壊されている場合は、何もしません。
	 * @param g Graphicsオブジェクト
	 * @param io ImageObserverオブジェクト
	 */
	public void draw (Graphics g, ImageObserver io) {

		if (alive) {

			for (Weapon w : weapons) {
				w.draw(g);
			}
			//持ってる武器の分の描画をする

			Color color;
			if (hited) {
				color = Color.BLACK;
				hited = false;
			} else {
				color = mainColor;
			}//非ダメージ時のちらつき処理

			BufferedImage bi = new BufferedImage(60,60,BufferedImage.TYPE_INT_ARGB);
			Graphics2D bg = bi.createGraphics();
			//Graphics2Dオブジェクトを使う

			bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			//アンチエイリアシングを有効にする

			AffineTransform at = new AffineTransform();
			at.setToRotation(frontDirection.getRad(), 31, 26);
			//回転する（機体を描く用）
			bg.setTransform(at);
			bg.setColor(Color.BLACK);
			bg.fillRect(10, 10, 41, 31);
			bg.setColor(color);
			bg.fillRect(12, 12, 37, 27);
			bg.setColor(Color.BLACK);
			bg.fillRect(48, 25, 2, 2);

			at.setToRotation(headDirection.getRad(), 31, 26);
			//回転する（砲台を描く用）
			bg.setTransform(at);
			bg.fillRect(31, 24, 25, 5);
			bg.fillOval(21, 16, 21, 21);
			bg.setColor(color);	
			bg.fillOval(23, 18, 19, 19);
			bg.fillRect(32, 25, 23, 3);

			bg.dispose();

			g.drawImage(bi, center.getX() - 31, center.getY() - 26, io);
			//貼り付け
		}
	}

	
	/**
	 * 毎フレームに１度外部から呼び出される更新メソッドです。
	 * 保有しているTankController及びWeaponにも伝達します。
	 * {@link Tank#lock(int)}メソッドによりロックされている点はこのメソッドの最後に開放されます。
	 */
	public void action() {
		if (alive) {

			culcCorners();//角を計算しなおす

			controller.action();//操縦者に操作させる

			for (int i = 0; i < locked.length; i++) {
				locked[i] = false;
			}

			for (Weapon w : weapons) {
				w.move();
			}//武器を動作させる

		}
	}


	/**
	 * 前進・後退メソッドです。引数で前後どちらに移動するか指定します。<br>
	 * ただし、この戦車の前後の点が他の戦車や侵入不可ブロックによりロックされている場合、移動しません。
	 * また、この戦車が既に破壊されている場合も何も行いません。<br>
	 * このメソッドを含め操縦関係のメソッドは、
	 * 基本的にこの戦車に設定されたTankControllerオブジェクトから呼び出されます。
	 * @see tankgame.rule.TankCollisionTank
	 * @see tankgame.rule.TankCollisionBlock
	 * @param ForB {@link Tank#FORWARD}か{@link Tank#BACK}を指定
	 */
	public void move (int ForB) {
		if (alive) {
			boolean f = !(locked[FL] || locked[FC] || locked[FR]);
			boolean b = !(locked[BL] || locked[BC] || locked[BR]);

			if ( (f && ForB > 0) || (b && ForB < 0) ) {
				center.addX(SPEED * frontDirection.getX() * ForB);
				center.addY(SPEED * frontDirection.getY() * ForB);
			} else if ( (f && ForB < 0) || (b && ForB > 0) ){
				move(-ForB);
			}
		}
	}

	/**
	 * 進行方向に対して水平方向に移動するメソッドです。
	 * 基本的には使用されません。
	 * @param i 移動量
	 */
	public void bound(int i) {
		if (alive) {
			center.addX(frontDirection.getY() * i);
			center.addY(frontDirection.getX() * i);
		}
	}



	/**
	 * 旋回メソッドです。引数で左右どちらに回転するかを指定します。<br>
	 * 周囲の点がロックされている場合、旋回しません。
	 * また、この戦車が既に破壊されている場合も何もしません。
	 * @param RorL {@link Tank#RIGHT}か{@link Tank#LEFT}を指定
	 */
	public void turn (int RorL) {

		boolean r = ! (locked[FR] || locked[BL] );
		boolean l = ! (locked[FL] || locked[BR] );

		if (alive) {
			if ( (RorL > 0 && r) || (RorL < 0 && l) ) {
				frontDirection.revolve(2 * RorL);
				headDirection.revolve(2 * RorL);
			} else if ( (RorL < 0 && r) || (RorL > 0 && l)){
				turn(-RorL);
			}
		}
	}

	/**
	 * 砲台のみ旋回させるメソッドです。引数で回転方向を指定します。
	 * 車体の旋回と同時に行うこともできます。
	 * このメソッドは周囲の点がロックされているか否かに影響されません。
	 * @param RorL {@link Tank#RIGHT}か{@link Tank#LEFT}を指定
	 */
	public void turnHead (int RorL) {
		if (alive) {
			headDirection.revolve(2 * RorL);
		}
	}

	private PointDouble headLocation = new PointDouble();
	
	/**
	 * 攻撃メソッドです。引数で利用する武器を指定します。
	 * @param i 何番目の兵装を起動するか指定
	 */
	public void fire(int i) {
		if (alive) {
			headLocation.set(center.getX() + headDirection.getX() * 30, center.getY() + headDirection.getY() * 30);
			weapons[i].shoot(headLocation, headDirection);
		}
	}


	/**
	 * 被ダメージメソッドです。
	 * このメソッドによりHPが0を下回った場合、自身の{@link Tank#kill()}メソッドが呼び出されます。
	 * @param i ダメージ値
	 */
	public void damage(int i) {
		if (alive) {

			hp -= i;

			if (hp <= 0) {
				kill();
			}
		}
	}

	/**
	 * 固定メソッドです。
	 * 引数は{@link tankgame.tank.PointsAroundTank}に定義された定数をから指定します。
	 * 主にRuleの処理により呼び出されます。
	 * @param point どの点を固定するか指定します。
	 * @see tankgame.tank.PointsAroundTank
	 * @see tankgame.rule.TankCollisionTank
	 * @see tankgame.rule.TankCollisionBlock
	 */
	public void lock(int point) {
		locked[point] = true;
	}

	/**
	 * この戦車の生存フラグをfalseにし、コントローラに伝達します。
	 */
	public void kill() {
		alive = false;
		controller.dead();
	}


	//自身の現在の四隅の点＋四辺の中央の計８座標を計算して上書きする
	private void culcCorners() {
		double cx = center.getX(), cy = center.getY();
		double ax = frontDirection.getX(), ay = frontDirection.getY();

		corners[FL].set(cx + 20 * ax + 15 * ay, cy + 20 * ay - 15 * ax);//左前方
		corners[FC].set(cx + 20 * ax, cy + 20 * ay);//前方中央
		corners[FR].set(cx + 20 * ax - 15 * ay, cy + 20 * ay + 15 * ax);//右前方
		corners[BL].set(cx - 20 * ax + 15 * ay, cy - 20 * ay - 15 * ax);//左後方
		corners[BC].set(cx - 20 * ax, cy - 20 * ay);//後方中央
		corners[BR].set(cx - 20 * ax - 15 * ay, cy - 20 * ay + 15 * ax);//右後方
		corners[RC].set(cx, cy);//右辺中央
		corners[LC].set(cx, cy);//左辺中央
	}


	//ある点Pが自分の内側かどうか（当たっているかどうか）を判定するメソッド
	public boolean hitToWeapon(PointDouble p) {
		if (inside(p)) {
			hited = true;
			return true;
		} else {
			return false;
		}//判定
	}


	/**
	 * 特定の点がこの戦車の内側にあるかを判定します。
	 * @param p 判定を行う点の座標を表す
	 * @return 引数の点がこの戦車の内側に位置していた時true
	 */
	public boolean inside(PointDouble p) {
		double px = p.getX(), py = p.getY();
		double cx = center.getX(), cy = center.getY();
		double ax = frontDirection.getX(), ay = frontDirection.getY();
		//直線m上の点Rと、mの（正規化された）法線ベクトルNがあると
		//任意の点Pと直線mの距離はRPベクトルとNベクトルの内積により求められる。

		//ここでは、まず戦車の各辺の中点を求め、その点を始点に点Pへ向かうベクトルをつくり、
		//作ったベクトルと法線ベクトル（正面向きの単位ベクトルfrontDirectionの要素より得られる）
		//の内積を計算することで、各辺とPとの距離を求めている。

		double fp = (px - cx - 20 * ax) * ax + (py - cy - 20 * ay) * ay;
		//戦車の正面の直線と、点Pとの距離

		double lp = (px - cx - 15 * ay) * ay - (py - cy - 15 * ax) * ax;
		//戦車の左辺を含む直線と、点Pとの距離

		double bp = (px - cx + 20 * ax) * (-ax) + (py - cy + 20 * ay) * (-ay);
		//戦車の背後の直線と、点Pとの距離

		double rp = (px - cx + 15 * ay) * (-ay) + (py - cy - 15 * ax) * ax;
		//戦車の右辺を含む直線と、点Pとの距離

		if (fp <= 20 && lp <= 15 && bp <= 20 && rp <= 15) {
			return true;
		} else {
			return false;
		}//判定
	}

	/**
	 * @return この戦車が保有している武器の配列
	 */
	public Weapon[] getWeapons() {
		return weapons;
	}

	/**
	 * @return この戦車の中心座標
	 */
	public PointDouble getCenter() {
		return center;
	}

	/**
	 * 
	 * @return この戦車の周辺の点を表す配列
	 */
	public PointDouble[] getCorners() {
		return corners;
	}

	/**
	 * 
	 * @return この戦車の名前文字列
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return この戦車の現在のHP値
	 */
	public int getHP() {
		return hp;
	}

	/**
	 * 
	 * @return この戦車が生存している場合true
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * 
	 * @return この戦車の砲身の向きを表す単位ベクトルオブジェクト
	 */
	public UnitVector getHeadDirection() {
		return headDirection;
	}

	/**
	 * 
	 * @return この戦車の車体の向きを表す単位ベクトルオブジェクト
	 */
	public UnitVector getFrontDirection() {
		return frontDirection;
	}
}