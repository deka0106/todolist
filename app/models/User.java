package models;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	/**
	 * ユーザーネーム
	 */
	public String name;

	/**
	 * メールアドレス
	 */
	public String email;

	/**
	 * パスワード
	 */
	public String password;

	/**
	 * ボード
	 */
	@OneToOne(cascade = CascadeType.ALL)
	public Board board;

	/**
	 * コンストラクタ
	 *
	 * @param name
	 *            ユーザーネーム
	 * @param email
	 *            メールアドレス
	 * @param password
	 *            パスワード
	 */
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.board = new Board(this);
	}

	/**
	 * 認証
	 *
	 * @param email
	 *            メールアドレス
	 * @param password
	 *            パスワード
	 * @return 成功: User, 失敗: null
	 */
	public static User authenticate(String email, String password) {
		User user = find("email = ?1", email).first();
		return Objects.equals(password, user.password) ? user : null;
	}

}
