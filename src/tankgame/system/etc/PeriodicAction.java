package tankgame.system.etc;
//GameThreadを利用したいクラスがimplementsするインターフェース
//定期的に呼び出されるonTimeメソッドを持たせる

public interface PeriodicAction {
	
	public abstract void onTime();
	
}