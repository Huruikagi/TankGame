package tankgame.system.gui;

import java.awt.*;
import java.io.*;

import javax.swing.Icon;

import static tankgame.block.Block.*;

public class StageSymbol implements Comparable<StageSymbol> {
	
	private File file;
	private String name;
	private String block;
	private int difficulty;
	private String summary;
	
	public StageSymbol(File file, String name, int difficulty, String summary, String block) {
		this.file = file;
		this.name = name;
		this.block = block.replaceAll("[\n\r\t ]", "");
		this.difficulty = difficulty;
		this.summary = summary;
	}
	
	public String getFilePath() {
		return file.toString();
	}
	public String getName() {
		return name;
	}
	public String getSummary() {
		return summary;
	}
	public int getDifficulty() {
		return difficulty;
	}
	
	public Icon getIcon() {
		return new Icon() {

			@Override
			public int getIconHeight() {
				return 25 * 3;
			}

			@Override
			public int getIconWidth() {
				return 40 * 3;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				g.fillRect(x, y, getIconWidth(), getIconHeight());
				Color color = Color.WHITE;
				for (int index = 0; index < block.length(); index++) {
					int i = index % 40;
					int j = index / 40;
					char type = block.charAt(index);
					switch (type) {
					case NULL:
						color = Color.WHITE;
						break;
					case UNBREAK:
						color = Color.DARK_GRAY;
						break;
					case BRICK:
						color = Color.ORANGE;
						break;
					case SAFETY:
						color = Color.PINK;
						break;
					case WATER:
						color = Color.BLUE;
					}
					g.setColor(color);
					g.fillRect(x + i * 3, y + j * 3, 3, 3);
				}
			}
		};
	}

	@Override
	public int compareTo(StageSymbol other) {
		if (this.difficulty != other.difficulty) {
			return this.difficulty - other.difficulty;
		} else {
			return (this.name).compareTo(other.name);
		}
	}
}