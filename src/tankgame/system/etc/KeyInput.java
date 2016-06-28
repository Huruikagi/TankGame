package tankgame.system.etc;
import java.awt.event.*;

//キーボード入力クラス
//キーイベントに応じてキーボード状態を表すboolean配列を書き換える

public class KeyInput implements KeyListener {
	
	private boolean[] keys = new boolean[10];	
	//キー状態を保持する
	
	//ゲッター
	public boolean[] getKeys() {
		return keys;
	}
	
	//キーが押された際の動作
	public void keyPressed(KeyEvent e) {
		int keyNum = e.getKeyCode();

		switch(keyNum) {
		
		case KeyEvent.VK_LEFT://左のとき
			keys[0] = true;
			break;
		case KeyEvent.VK_UP://上のとき
			keys[1] = true;
			break;
		case KeyEvent.VK_RIGHT://右のとき
			keys[2] = true;
			break;
		case KeyEvent.VK_DOWN://下のとき
			keys[3] = true;
			break;
		case KeyEvent.VK_S:
			keys[4] = true;
			break;
		case KeyEvent.VK_A:
			keys[5] = true;
			break;
		case KeyEvent.VK_D:
			keys[6] = true;
			break;
		case KeyEvent.VK_W:
			keys[7] = true;
			break;
		case KeyEvent.VK_Q:
			keys[8] = true;
			break;
		case KeyEvent.VK_ENTER:
			keys[9] = true;
			break;
		default://他は何もしない	
			break;
		}
	}

	
	//キーがはなされたときの動作
	public void keyReleased(KeyEvent e) {
		int keyNum = e.getKeyCode();

		switch (keyNum) {
		
		case KeyEvent.VK_LEFT:	//左のとき
			keys[0] = false;
			break;
		case KeyEvent.VK_UP:		//上のとき
			keys[1] = false;
			break;
		case KeyEvent.VK_RIGHT:	//右のとき
			keys[2] = false;
			break;
		case KeyEvent.VK_DOWN:	//下のとき
			keys[3] = false;
			break;
		case KeyEvent.VK_S:
			keys[4] = false;
			break;
		case KeyEvent.VK_A:
			keys[5] = false;
			break;
		case KeyEvent.VK_D:
			keys[6] = false;
			break;
		case KeyEvent.VK_W:
			keys[7] = false;
			break;
		case KeyEvent.VK_Q:
			keys[8] = false;
			break;
		case KeyEvent.VK_ENTER:
			keys[9] = false;
			break;
		default://他は何もしない	
			break;
		}
	}

	
	//インターフェースで指定されたけど使わない
	public void keyTyped(KeyEvent e) {}

}