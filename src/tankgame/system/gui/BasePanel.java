package tankgame.system.gui;

import java.util.*;

import javax.swing.*;
import javax.swing.plaf.LayerUI;


/**
 * 複数のパネルを保有しており、外部よりメッセージを受けることで表示を切り替える。
 * @author tsuyoshi-kita
 *
 */
@SuppressWarnings("serial")
public class BasePanel extends JPanel {

	private Map<PanelCode, SwitchPanel> panels;

	public BasePanel() {
//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//		} catch (ClassNotFoundException e) {
//		} catch (InstantiationException e) {
//		} catch (IllegalAccessException e) {
//		} catch (UnsupportedLookAndFeelException e) {
//		}
//		SwingUtilities.updateComponentTreeUI(this);
		

		SwitchPanel.setBasePanel(this);
		//SwitchPanelクラスにこのオブジェクトを伝える

		setLayout(new OverlayLayout(this));
		//OverlayLayoutで各パネルを重ねて保有しておく

		panels = new EnumMap<PanelCode, SwitchPanel>(PanelCode.class);
		//EnumMapの生成

		StartManuPanel smp = new StartManuPanel();
		panels.put(PanelCode.START, smp);
		LayerUI<StartManuPanel> layerUI = new StartPanelLayerUI();
		JLayer<StartManuPanel> jLayer = new JLayer<StartManuPanel>(smp, layerUI);
		smp.setLayer(jLayer);
		add(jLayer);
		//スタートメニューパネルと修飾レイヤーの生成、設定

		SwitchPanel ssp = new StageSelectPanel();
		ssp.setVisible(false);
		panels.put(PanelCode.STAGE_SELECT, ssp);
		add(ssp);
		//ステージセレクトパネルの生成、設定

		SwitchPanel gp = new GamePanel();
		gp.setVisible(false);
		panels.put(PanelCode.GAME, gp);
		add(gp);
		//ゲームパネルの生成、設定

		//		BriefingPanel bf = new BriefingPanel(gp);
		//		bf.setVisible(false);
		//		panels.put(PanelCode.BRIEFING, bf);
		//		add(bf);
		//		//ブリーフィングパネルの生成、設定
		//		
		//		ResultPanel rsp = new ResultPanel();
		//		rsp.setVisible(false);
		//		panels.put(PanelCode.RESULT, rsp);
		//		add(rsp);
		//		//リザルトパネルの生成、設定

		SwitchPanel conf = new ConfigPanel();
		conf.setVisible(false);
		panels.put(PanelCode.CONFIG, conf);
		add(conf);
		//コンフィグパネルの生成、設定

		smp.start("");
		//スタートメニュー起動
	}


	//PanelCodeの要素を受け取り、該当するパネルを表示する。
	//現在表示されているパネルは非表示にする。
	//必要に応じてPanelCodeからメッセージを取り出し、処理をする。
	void changePanel(PanelCode from, PanelCode to) {

		SwitchPanel currentPanel = panels.get(from);
		currentPanel.setVisible(false);
		currentPanel.stop();

		SwitchPanel nextPanel = panels.get(to);
		nextPanel.setVisible(true);
		nextPanel.start(to.pullMessage());

	}
}