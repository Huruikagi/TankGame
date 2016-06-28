package tankgame.system.gui;

import java.io.*;

import javax.xml.stream.*;
import static javax.xml.stream.XMLStreamConstants.*;

import tankgame.stage.InvalidStageFileException;
//import tankgame.stage.StageValidator;

//StAXによりステージの概要の抽出とアイコンの生成を行う
public class SimpleStageReader {

	//	private StageValidator validator;

	public StageSymbol read(File file) throws InvalidStageFileException, XMLStreamException {

		//		try {
		//			validator.validate(file);
		//			//ファイルのスキーマを検証する
		//		} catch (InvalidStageFileException e) {
		//			//妥当でなければ例外を投げる
		//			throw e;
		//		}

		StageSymbol symbol = null;

		//パーサ用ファクトリの生成
		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLStreamReader reader = null;
		BufferedInputStream stream = null;

		try {
			//入力に使用するファイルの設定
			stream = new BufferedInputStream(new FileInputStream(file));

			//パーサの生成
			reader = factory.createXMLStreamReader(stream);

			//フィルターの生成
			StreamFilter filter = new StreamFilter() {
				@Override
				public boolean accept(XMLStreamReader reader) {
					//タグの開始と文字データを扱う（文書終了はデフォルトで処理される）
					return reader.isCharacters() | reader.isStartElement();
				}
			};
			//XMLStreamReaderにフィルタを適用
			reader = factory.createFilteredReader(reader, filter);

			String title = null, summary = null, block = null;
			int difficulty = 0;
			String tag = null;
			
			while (reader.hasNext()) {
				int eventType = reader.next();
				
				if (eventType == START_ELEMENT) {
					tag = reader.getLocalName();
				}
				else if (eventType == CHARACTERS) {
					if (tag == null) {
						//String switchはnullを弾いてやらないとぬるぽ吐く
					} else switch (tag) {
					case "title":
						title = reader.getText();
						System.out.println("title:" + title);
						break;
					case "summary":
						summary = reader.getText();
						break;
					case "difficulty":
						difficulty = Integer.parseInt(reader.getText());
						break;
					case "block":
						block = reader.getText();
						break;
					}
					tag = null;
				}
				if ((block != null) && (summary != null) && (title != null)) {
					symbol = new StageSymbol(file,title,difficulty,summary,block);
					break; //必要な情報が得られたらwhileループを抜ける
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (XMLStreamException e) {
			System.out.println(e);
		} finally {
			//クローズ処理
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {}
			}
		}
		return symbol;
	}
}