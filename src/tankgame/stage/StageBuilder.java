package tankgame.stage;

import java.awt.Color;
import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import tankgame.block.*;
import tankgame.rule.*;
import tankgame.system.etc.*;
import tankgame.tank.*;
import tankgame.tank.controller.*;


/**
 * XML形式のステージファイルをもとに{@link Stage}オブジェクトを生成するクラスです。
 * ステージファイルは事前に既定のXMLSchemaファイルと照合され、正しい形式であることを前提とします。
 * @author tsuyoshi-kita
 *
 */
public class StageBuilder {

	private final int VERSION = 1;
	
	private Element stageElement;
	
	private String title;
	private Tank[] tanks;
	private DefaultRule[] upRules;
	private Rule[] opRules;
	private BlockField field;
	//作業用メンバ変数

	
	/**
	 * 
	 * @param pass ステージファイルのパス文字列
	 * @param key Stageオブジェクトに与えるためのキーボード情報
	 * @return ファイルに従って生成されたStageオブジェクト
	 * @throws Exception なにかエラー
	 */
	public Stage createStage(String pass, boolean[] key) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		//文書を読み込む

		Document doc = db.parse(new FileInputStream(pass));

		stageElement = doc.getDocumentElement();
		//ルート要素(stage)を得る

		String version = stageElement.getAttribute("version");
		if (Integer.parseInt(version) > VERSION) {
			throw new Exception("このステージファイルを読み込むためには新しいバージョンが必要です。");
		}

		title = stageElement.getElementsByTagName("title").item(0).getNodeValue();
		field = new BlockField();
		//Documentの中から情報を拾って組み立てていく

		buildTanks(key);
		buildBlocks();
		buildRules();
		
		return new Stage(key, title, tanks, upRules, opRules, field);
	}
	
	
	
	//戦車を生成するメソッド
	private void buildTanks(boolean[] key) {
		Element tanksElement = (Element)(stageElement.getElementsByTagName("tanks").item(0));
		//tanks要素の取得
		NodeList tankElements = tanksElement.getElementsByTagName("tank");
		//tank要素（複数）の取得
		tanks = new Tank[tankElements.getLength()];
		for (int i = 0; i < tanks.length; i++) {
			//各tank要素から情報を抜き出して変換していく
			Element element = (Element)(tankElements.item(i));
			String name = element.getAttribute("name");
			int hp = Integer.parseInt(element.getAttribute("hp"));
			int x = Integer.parseInt(element.getAttribute("x")) * 20;
			int y = Integer.parseInt(element.getAttribute("y")) * 20;
			PointDouble loc = new PointDouble(x,y);
			UnitVector dir = new UnitVector(Integer.parseInt(element.getAttribute("dir")));
			String rgb = element.getAttribute("rgb");
			int r = Integer.parseInt(rgb.substring(1, 3), 16);
			int g = Integer.parseInt(rgb.substring(3, 5), 16);
			int b = Integer.parseInt(rgb.substring(5, 7), 16);
			Color color = new Color(r,g,b);
			tanks[i] = new Tank(name, hp, loc, dir, color);
		}
		for (int i = 0; i < tanks.length; i++) {
			//コントローラを設定するためのループ２周目
			Element element = (Element)(tankElements.item(i));
			Element coElement = (Element)(element.getElementsByTagName("*").item(0));
			if (coElement.getNodeName().equals("PLAYER")) {
				//PLAYERはキー情報を渡すのみ
				tanks[i].setController(new Player(key));
			}
			if (coElement.getNodeName().equals("AI00")) {
				//AI00の場合
				//tget要素が指定している相手がどの戦車のものかを探して渡す
				//あとステージのブロック情報も渡す
				Element tgetElement = (Element)(coElement.getElementsByTagName("tget").item(0));
				String tgetName = tgetElement.getAttribute("name");
				int tgetNum = searchIdx(tgetName);
				tanks[i].setController(new AI00(tanks[tgetNum], field));
			}	
		}
	}
	
	
	//ブロックを生成するメソッド
	private void buildBlocks() {
		Element blockElement = (Element)(stageElement.getElementsByTagName("block").item(0));
		String source = blockElement.getTextContent();
		String blocks = source.replaceAll("[\n\r\t ]", "");
		for (int index = 0; index < blocks.length(); index++) {
			int x = index % 40;
			int y = index / 40;
			char type = blocks.charAt(index);
			field.setBlock(type, x, y);
		}
	}
	

	//ルールを生成するメソッド
	private void buildRules() {
		Element rulesElement = (Element)(stageElement.getElementsByTagName("rules").item(0));
		//rules要素の取得
		List<Rule> upRuleList = new ArrayList<Rule>();
		List<Rule> opRuleList = new ArrayList<Rule>();

		for (Node ch = rulesElement.getFirstChild();
		ch != null; ch = ch.getNextSibling()) {

			Element ruleElement;

			if (ch.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			} else {
				ruleElement = (Element)ch;
			}

			String tagName = ruleElement.getNodeName();

			if (tagName.equals("DEFEAT")) {
				//Defeatルールの場合
				String typeString = ruleElement.getAttribute("type");
				byte type = Defeat.ALL;
				if (typeString.equals("ANY")) {
					type = Defeat.ANY;
				}
				NodeList tgetElements = ruleElement.getElementsByTagName("tget");
				int[] tgetIdx = new int[tgetElements.getLength()];
				for (int j = 0; j < tgetElements.getLength(); j++) {
					Element tget = (Element)(tgetElements.item(j));
					tgetIdx[j] = searchIdx(tget.getAttribute("name"));
				}
				opRuleList.add(new Defeat(type, tgetIdx));
			}
			else if (tagName.equals("DEFEND")) {
				//Defendルールの場合
				String typeString = ruleElement.getAttribute("type");
				byte type = Defend.ALL;
				if (typeString.equals("ANY")) {
					type = Defend.ANY;
				}
				NodeList tgetElements = ruleElement.getElementsByTagName("tget");
				int[] tgetIdx = new int[tgetElements.getLength()];
				for (int j = 0; j < tgetElements.getLength(); j++) {
					Element tget = (Element)(tgetElements.item(j));
					tgetIdx[j] = searchIdx(tget.getAttribute("name"));
				}
				opRuleList.add(new Defend(type, tgetIdx));
			}
			else if (tagName.equals("TIMELIMIT")) {
				//TimeLimitルールの場合
				String typeString = ruleElement.getAttribute("type");
				byte type = TimeLimit.LOSE;
				if (typeString.equals("WIN")) {
					type = TimeLimit.WIN;
				}
				int time = Integer.parseInt(ruleElement.getAttribute("time"));
				opRuleList.add(new TimeLimit(type, time));
			}
			else if (tagName.equals("TEAM")) {
				//チームルールの場合
				int[] teamCode = new int[tanks.length];
				int num = 1;	//チームコードに値する数値
				NodeList teams = ruleElement.getElementsByTagName("team");
				for (int i = 0; i < teams.getLength(); i++) {
					Element team = (Element)teams.item(i);
					NodeList members = team.getElementsByTagName("member");
					for (int j = 0; j < members.getLength(); j++) {
						String name = ((Element)members.item(j)).getAttribute("name");
						int idx = searchIdx(name);
						teamCode[idx] = num;
						System.out.println(name + ":" + num);
					}
					num++;
				}
				upRuleList.add(new WeaponHitTank(teamCode));
			}
		}

		opRules = new Rule[opRuleList.size()];
		opRuleList.toArray(opRules);
		upRules = new DefaultRule[upRuleList.size()];
		upRuleList.toArray(upRules);
	}
	
	
	private int searchIdx(String name) {
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
}