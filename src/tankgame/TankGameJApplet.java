package tankgame;

import java.awt.*;
import javax.swing.*;

import tankgame.system.gui.*;

/**
 * ブラウザ上で実行するためのアプレット版。
 * @author tsuyoshi-kita
 *
 */
@SuppressWarnings("serial")
public class TankGameJApplet extends JApplet {
	
	@Override
	public void init() {
		Container contentPane = getContentPane();
		
		BasePanel bp = new BasePanel();
		contentPane.add(bp);
	}
}