package tankgame.system.gui;

import javax.swing.*;


//フレーム
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	//コンストラクタ
	public MainFrame() {
		setTitle("せんしゃげー");
		setBounds(0, 0, 805, 600);
		setResizable(false);
		//タイトルと大きさの設定。ウインドウサイズの変更を不可に
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//ウインドウを閉じたらプログラムが終了するようにする
		
		BasePanel basePanel = new BasePanel();
		add(basePanel);
	}
}