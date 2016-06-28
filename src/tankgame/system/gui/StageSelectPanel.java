package tankgame.system.gui;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.xml.stream.XMLStreamException;

import tankgame.stage.InvalidStageFileException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * ステージセレクト画面クラスです。
 * @author tsuyoshi-kita
 *
 */
@SuppressWarnings("serial")
public class StageSelectPanel extends SwitchPanel implements ActionListener {

	private JLabel title;
	private JButton back;
	private JButton enter;
	private JScrollPane scroll;
	private JList<StageSymbol> stageList;

	private String stageDirectoryPath = "/home/kbc14a04/desktop/stagefile";

	StageSelectPanel() {
		super(PanelCode.STAGE_SELECT);

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();

		title = new JLabel("STAGE SELECT");
		title.setFont(new Font(Font.SERIF, Font.BOLD, 64));
		title.setForeground(Color.RED);
		gbc.gridx = 0;
		gbc.gridy = 0;
		layout.setConstraints(title, gbc);

		scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		stageList = new JList<StageSymbol>();
		stageList.setCellRenderer(new StageListCellRenderer());
		scroll.getViewport().setView(stageList);
		scroll.setPreferredSize(new Dimension(600, 400));
		gbc.gridy++;
		layout.setConstraints(scroll, gbc);

		enter = new JButton("Enter");
		enter.addActionListener(this);
		gbc.gridx++;
		layout.setConstraints(enter, gbc);

		back = new JButton("Back");
		back.addActionListener(this);
		gbc.gridy++;
		layout.setConstraints(back, gbc);

		add(title);
		add(scroll);
		add(back);
		add(enter);
	}

	@Override
	void start(String message) {
		File dir = new File(stageDirectoryPath);
		stageList.setListData(readDirectory(dir));
		stageList.setSelectedIndex(0);
		stageList.requestFocusInWindow();
	}

	@Override
	void stop() {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == back) {
			panelSwitch(PanelCode.START);
		}
		else if (source == enter) {
			PanelCode code = PanelCode.GAME;
			System.out.println(stageList.getSelectedValue().getName());
			code.setMessage(stageList.getSelectedValue().getFilePath());
			panelSwitch(code);
		}
	}

	
	//引数：ディレクトリを表すFileオブジェクト
	//戻り値：ディレクトリ内部にあるステージファイルから生成されたStageSymbolのリスト
	private Vector<StageSymbol> readDirectory(File directory) {
		Vector<StageSymbol> vector = new Vector<StageSymbol>();
		SimpleStageReader reader = new SimpleStageReader();
		File[] files = directory.listFiles();

		StageSymbol symbol;
		for (File file : files) {
			try {
				symbol = reader.read(file);
				vector.add(symbol);
			} catch (InvalidStageFileException e) {
				//スキーマ形式が間違ってる
				//→読み飛ばし
			} catch (XMLStreamException e) {
				//StAXのエラー
				//→読み飛ばし
			}
		}
		Collections.sort(vector);
		return vector;
	}
}

//ListCellRenderer実装クラス
//StageSymbolから情報を抜き出してカスタム描画するためのクラス
class StageListCellRenderer implements ListCellRenderer<StageSymbol> {
	
	@Override
	public Component getListCellRendererComponent(
			JList<? extends StageSymbol> list,
			StageSymbol stageSymbol,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		
		JLabel label = new JLabel();
		label.setIcon(stageSymbol.getIcon());
		StringBuffer sb = new StringBuffer();
		sb.append("<html><h3>");
		sb.append(stageSymbol.getName());
		sb.append("</h3><p>");
		sb.append(toStar(stageSymbol.getDifficulty()));
		sb.append("</p><p>");
		sb.append(stageSymbol.getSummary());
		sb.append("</p></html>");
		label.setText(sb.toString());
		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(new Color(0,0,255,50));
			label.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		} else {
			label.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		}
		return label;
	}
	
	//難易度の値を★に変換するメソッド
	private String toStar(int num) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append('★');
		}
		for (int i = 0; i < 5 - num; i++) {
			sb.append('☆');
		}
		return sb.toString();
	}
}