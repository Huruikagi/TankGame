package tankgame.system.gui;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

/**
 * ゲーム設定を行うためのクラス。
 * 拡張予定。
 * @author tsuyoshi-kita
 *
 */

@SuppressWarnings("serial")
public class ConfigPanel extends SwitchPanel implements ActionListener {
	
	private JButton back;

	ConfigPanel() {
		super(PanelCode.CONFIG);
		back = new JButton("Back");
		back.addActionListener(this);
		add(back, BorderLayout.SOUTH);
	}

	@Override
	void start(String message) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	void stop() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object sourse = e.getSource();
		
		if (sourse == back) {
			panelSwitch(PanelCode.START);
			//スタート画面に戻る
		}
	}
}