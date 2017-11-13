package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	@OneToMany(cascade = CascadeType.ALL)
	public List<Task> tasks;

	/**
	 * タグリスト
	 */
	@OneToMany(cascade = CascadeType.ALL)
	public List<Tag> tags;

	/**
	 * コンストラクタ
	 */
	public Board(User user) {
		this.user = user;
		this.tasks = new ArrayList<>();
		this.tags = new ArrayList<>();
	}

}
