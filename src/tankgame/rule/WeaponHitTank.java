package tankgame.rule;

import tankgame.system.etc.PointDouble;
import tankgame.tank.Tank;
import tankgame.tank.weapon.Weapon;


/**
 * 戦車に武器の当たり判定・処理を行う基本ルールです。
 * デフォルトでは{@link GameRuler}には引数無しコンストラクタで生成されたこのクラスの実体が設定されます。
 * チームコードを用いたルールを適用するためには、別のインスタンスを生成して上書きする必要があります。
 * 詳細は{@link GameRuler#updateRule(DefaultRule...)}を参照してください。
 * @author tsuyoshi-kita
 * @see GameRuler
 * @see tankgame.tank.Tank
 * @see tankgame.tank.weapon.Weapon
 */
public class WeaponHitTank extends DefaultRule {

	private Tank[] tanks;
	private int[] teamCode;
	
	/**
	 * バトルロワイヤル形式（すべての武器が自分以外の戦車に当たる）で判定を行う設定で、このルールオブジェクトを生成する。
	 */
	public WeaponHitTank() {
		//デフォルト（チームコード指定無し）だとバトルロワイヤル形式
		super(1000, "", "");
	}

	/**
	 * 
	 * @param teamCode チームコード配列
	 */
	public WeaponHitTank(int...teamCode) {
		super(1000, "", "");
		this.teamCode = teamCode;
	}

	@Override 
	void check() {

		for (int i = 0; i < tanks.length; i++) {//攻撃手となる戦車

			for (int k = 0; k < tanks.length; k++) {//攻撃を受ける戦車

				if (tanks[i].isAlive() &&			/*攻撃手が生きていて*/
						tanks[k].isAlive() &&			/*攻撃対象も生きていて*/
						teamCode[i] != teamCode[k]) {	//攻撃手と攻撃対象のチームが異なる場合

					for (Weapon w : tanks[i].getWeapons()) {//攻撃手の戦車の各武器に関して

						boolean[] exs = w.getIsExist();
						PointDouble[] loc = w.getLocations();
						int damage = w.getDamage();
						//弾の情報を取得

						for (int j = 0; j < exs.length; j++) {//各弾に関して

							if (exs[j]) {//現在その弾が存在していれば

								boolean hit = tanks[k].hitToWeapon(loc[j]);//当たり判定

								if (hit) {//当たったら
									tanks[k].damage(damage);//ダメージを与え
									int hp = tanks[k].getHP();
									exs[j] = false;//弾を消す

									System.out.println("tank" + k + ":" + hp);
								}
							}
						}
					}
				}
			}
		}
	}


	@Override
	void request() {
		tanks = reqTanks();
		
		if (teamCode == null) {
			//コンストラクタでteamCodeが渡されなかった場合の処置
			teamCode = new int[tanks.length];
			for (int i = 0; i < teamCode.length; i++) {
				teamCode[i] = i;
			}
		}
		
	}
}