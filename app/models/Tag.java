package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Tag extends Model {

	/**
	 * ボード
	 */
	@ManyToOne
	public Board board;

	/**
	 * タグネーム
	 */
	public String name;

	/**
	 * タグを持つタスク
	 */
	@ManyToMany
	public List<Task> tasks;

	/**
	 * コンストラクタ
	 *
	 * @param board
	 *            ボード
	 * @param name
	 *            タグネーム
	 */
	public Tag(Board board, String name) {
		this.board = board;
		this.name = name;
		this.tasks = new ArrayList<>();
	}

}