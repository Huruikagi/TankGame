package tankgame.system.gui;

import java.awt.*;

import tankgame.rule.GameRuler;
import tankgame.stage.*;
import tankgame.system.etc.*;


//ゲームを動かすパネル

@SuppressWarnings("serial")
public class GamePanel extends SwitchPanel implements PeriodicAction {

	private ClockTimer thread;//スレッド
	private Stage stage;//ステージ
	private KeyInput key;//キーリスナー

	private GameInfomationPane gip;

	private boolean paused = false;

	//コンストラクタ
	public GamePanel() {
		super(PanelCode.GAME);
		key = new KeyInput();
		addKeyListener(key);

		gip = new GameInfomationPane();
	}

	//startだけどゲームはまだスタートしない
	@Override
	void start(String message) {
		requestFocusInWindow();

		StageBuilder sb = new StageBuilder();
		try {
			stage = sb.createStage(message, key.getKeys());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void gameStart() {
		thread = new ClockTimer(this, 34);
		thread.setRunFlag(true);
		thread.start();
		//スレッド開始
	}


	@Override
	void stop() {
		thread.setRunFlag(false);
		stage = null;
	}

	//描画処理
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//白で背景塗りつぶし
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 500);

		//ステージに描画処理
		stage.draw(g, this);
		gip.info(g);
	}


	//定時行動
	public void onTime() {
		stage.onTime();
		repaint();
		if (GameRuler.getEndFlag()) {
			thread.setRunFlag(false);
		}
	}

	public void pause() {
		paused = true;
	}
}