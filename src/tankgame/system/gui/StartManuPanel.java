package tankgame.system.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * スタートメニュークラスです。
 * 
 * @author tsuyoshi-kita
 * @see StartPanelLayerUI
 */
@SuppressWarnings("serial")
public class StartManuPanel extends SwitchPanel implements Runnable, ActionListener {
	
	private JLayer<?> layer;
	private JButton stageSelect;
	private JButton config;
	private JButton quit;
	private boolean animation = false;

	StartManuPanel() {
		super(PanelCode.START);
		
		setLayout(null);
		
		Font font = new Font(Font.SANS_SERIF, Font.ITALIC, 30);
		
		stageSelect = new JButton("STAGE SELECT");
		stageSelect.setFont(font);
		stageSelect.setBounds(400, 220, 300, 50);
		stageSelect.addActionListener(this);
		add(stageSelect);
		
		config = new JButton("CONFIG");
		config.setFont(font);
		config.setBounds(400, 290, 300, 50);
		config.addActionListener(this);
		add(config);
		
		quit = new JButton("QUIT");
		quit.setFont(font);
		quit.setBounds(400, 360, 300, 50);
		quit.addActionListener(this);
		add(quit);

		setBackground(Color.WHITE);
	}
	
	@Override
	public void run() {
		while (animation) {
			repaint();
			try {
				Thread.sleep(33);
			} catch (Exception e) {
			}
		}
	}

	@Override
	void start(String message) {
		animation = true;
		new Thread(this, "start manu movie").start();
	}

	@Override
	void stop() {
		animation = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object sourse = e.getSource();
		if (sourse == stageSelect) {
			panelSwitch(PanelCode.STAGE_SELECT);
		}
		else if (sourse == config) {
			panelSwitch(PanelCode.CONFIG);
		}
		else if (sourse == quit) {
			this.stop();
			System.exit(0);
		}
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		layer.setVisible(aFlag);
	}
	
	void setLayer(JLayer<?> layer) {
		this.layer = layer;
	}
}