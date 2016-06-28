package tankgame.rule;

/**
 * 基本ルールをまとめ、他のルールと区別するための抽象クラスです。
 * @author tsuyoshi-kita
 *
 */
public abstract class DefaultRule extends Rule {

	DefaultRule(int priority, String message1, String message2) {
		super(priority, message1, message2);
	}

}
