package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class Tag extends Model {

	/**
	 * タグネーム
	 */
	public String name;

	/**
	 * タグを持つタスク
	 */
	@ManyToMany
	public Set<Task> tasks;

	/**
	 * コンストラクタ
	 *
	 * @param board
	 *            ボード
	 * @param name
	 *            タグネーム
	 */
	public Tag(String name) {
		this.name = name;
		this.tasks = new HashSet<>();
	}

}