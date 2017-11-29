package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Task extends Model {

	/** 進捗状況: 未着手 */
	public static int TODO = 0;

	/** 進捗状況: 作業中 */
	public static int DOING = 1;

	/** 進捗状況: 完了 */
	public static int DONE = 2;

	/** 優先度 範囲 */
	public static int PRIORITY_MIN = 1;
	public static int PRIORITY_MAX = 10;

	/**
	 * ボード
	 */
	@ManyToOne
	public Board board;

	/**
	 * タイトル
	 */
	public String title;

	/**
	 * 詳細
	 */
	public String detail;

	/**
	 * 作成日時
	 */
	public Date createDate;

	/**
	 * 期限日時
	 */
	public Date dueDate;

	/**
	 * 進捗状況
	 */
	public int progress;

	/**
	 * 優先度
	 */
	public int priority;

	/**
	 * タグ
	 */
	@ManyToMany
	public List<Tag> tags;

	/**
	 * コンストラクタ
	 *
	 * @param board
	 *            ボード
	 * @param title
	 *            タイトル
	 * @param detail
	 *            詳細
	 * @param createDate
	 *            作成日時
	 * @param dueDate
	 *            期限日時
	 * @param progress
	 *            進捗状況
	 * @param priority
	 *            優先度
	 */
	public Task(Board board, String title, String detail, Date createDate, Date dueDate, int progress, int priority) {
		this.board = board;
		this.title = title;
		this.detail = detail;
		this.createDate = createDate;
		this.dueDate = dueDate;
		this.progress = progress;
		this.priority = priority;
		this.tags = new ArrayList<>();
	}

	@Override
	public Task save() {
		correct();
		return super.save();
	}

	/**
	 * 不正値を訂正する
	 */
	public void correct() {
		progress = progress < TODO ? TODO : DONE < progress ? DONE : progress;
		priority = priority < PRIORITY_MIN ? PRIORITY_MIN : PRIORITY_MAX < priority ? PRIORITY_MAX : priority;
	}

	/**
	 * HashMapとして返す
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("title", title);
		map.put("detail", detail);
		map.put("createDate", createDate);
		map.put("dueDate", dueDate);
		map.put("progress", progress);
		map.put("priority", priority);
		map.put("tags", tags.stream().map(tag -> tag.name).collect(Collectors.toList()));
		return map;
	}

}
