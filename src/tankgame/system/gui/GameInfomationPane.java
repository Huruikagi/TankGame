package tankgame.system.gui;
import java.awt.*;

import tankgame.rule.GameRuler;


//なんというか

public class GameInfomationPane {

	public void info(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 500, 800, 70);
		g.setColor(Color.WHITE);
		g.fillRect(5, 505, 790, 60);
		
		boolean t = false;
		if (t) {
			
		} else if (GameRuler.getEndFlag()) {
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 80));
			if (GameRuler.getWinFlag()) {
				g.drawString("STAGE CLEAR", 80, 280);
			} else {
				g.drawString("GAME OVER", 100, 280);
			}
		}
	}
	
	public void briefing(String stageName, String[] ruleMessages) {
		
	}
	
	public void result() {
		
	}
	
	public void paurse() {
		
	}
}