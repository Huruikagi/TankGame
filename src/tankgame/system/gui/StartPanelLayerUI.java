package tankgame.system.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.*;

import tankgame.block.BlockField;
import tankgame.system.etc.*;
import tankgame.tank.Tank;

@SuppressWarnings("serial")
public class StartPanelLayerUI extends LayerUI<StartManuPanel> {

	private Tank tank;
	private BlockField field;

	public StartPanelLayerUI(){
		tank = new Tank("タイトル戦車", 1, new PointDouble(200,250), new UnitVector(0), Color.BLUE);
		field = new BlockField();
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		
		tank.move(Tank.FORWARD);
		tank.turn(Tank.RIGHT);

		Graphics2D g2 = (Graphics2D)g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.RED);
		g2.setFont(new Font(Font.SERIF, Font.BOLD, 100));
		g2.drawString("Tank Game !", 50, 150);
		
		field.draw(g2);
		tank.draw(g2, c);
		
		g2.dispose();
		
	}
}