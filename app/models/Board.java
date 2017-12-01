package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import play.db.jpa.Model;

@Entity
public class Board extends Model {

	/**
	 * ユーザー
	 */
	@OneToOne
	public User user;

	/**
	 * タスクリスト
	 */
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "board")
	public Set<Task> tasks;

	/**
	 * コンストラクタ
	 */
	public Board(User user) {
		this.user = user;
		this.tasks = new HashSet<>();
	}

}
