package tankgame.system.gui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class SwitchPanel extends JPanel {
	
	private static BasePanel bp;
	
	private final PanelCode panelCode;
	
	SwitchPanel(PanelCode panelCode) {
		this.panelCode = panelCode;
	}
	
	static void setBasePanel(BasePanel basePanel) {
		bp = basePanel;
	}

	protected void panelSwitch(PanelCode to) {
		bp.changePanel(panelCode, to);
	}
	
	abstract void start(String message);
	abstract void stop();
	
}