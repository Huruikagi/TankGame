package tankgame.system.etc;


//スレッドクラス
//定期的にonTime()メソッドを呼び出すだけ
public class ClockTimer extends Thread {

	private PeriodicAction listener;		//onTimeさせる対象
	private boolean runFlag = false;	//ループフラグ	
	private long interval;
	
	//コンストラクタ
	public ClockTimer(PeriodicAction p, long interval) {
		listener = p;
		this.interval = interval;
	}
	
	//ループフラグのsetter
	public void setRunFlag(boolean b) {
		runFlag = b;
	}

	
	//ひたすら繰り返すメソッド
	@Override
	public void run() {

		while (runFlag) {

			listener.onTime();

			try {
				Thread.sleep(interval);		//休み
			} catch(Exception e) {
			}
		}
	}
}