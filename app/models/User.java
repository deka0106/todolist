package models;

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

}
