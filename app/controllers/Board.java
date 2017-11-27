package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jamonapi.utils.Logger;

import models.Task;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Board extends Controller {

	public static void index() {
		render();
	}

	/**
	 * タスクの追加
	 * params:
	 * title タイトル
	 * detail 詳細
	 * dueDate 期限日時(Locale)
	 * progress 進行状況
	 * priority 優先度
	 */
	public static void addTask() {
		Logger.log(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		// タイトルは必須
		if (!params._contains("title") || params.get("title").equals("")) {
			map.put("ok", false);
			map.put("error", "No title");
			renderJSON(map);
			return;
		}

		String title = params.get("title");
		String detail = params._contains("detail") ? params.get("detail") : "";
		Date createDate = new Date();
		Date dueDate = null;
		try {
			dueDate = params._contains("dueDate") ? DateFormat.getInstance().parse(params.get("dueDate")) : null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int progress = params._contains("progress") ? Integer.valueOf(params.get("progress")) : Task.TODO;
		int priority = params._contains("priority") ? Integer.valueOf(params.get("priority")) : 5;

		User user = User.find("email = ?1", Security.connected()).first();

		Task task = new Task(user.board, title, detail, createDate, dueDate, progress, priority);
		user.board.tasks.add(task);
		task.save();

		map.put("ok", true);

		renderJSON(map);
	}

	/**
	 * タスクの編集
	 * params:
	 * taskId タスクID
	 * title タイトル
	 * detail 詳細
	 * dueDate 期限日時(Locale)
	 * progress 進行状況
	 * priority 優先度
	 */
	public static void editTask() {
		Logger.log(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		// タスクIDは必須
		if (!params._contains("taskId")) {
			map.put("ok", false);
			map.put("error", "No taskId");
			renderJSON(map);
			return;
		}

		User user = User.find("email = ?1", Security.connected()).first();
		Task task = Task.find("board_id = ?1 and id = ?2", user.board.id, params.get("taskId")).first();

		if (params._contains("title")) {
			task.title = params.get("title");
		}
		if (params._contains("detail")) {
			task.detail = params.get("detail");
		}
		if (params._contains("dueDate")) {
			try {
				task.dueDate = DateFormat.getInstance().parse(params.get("dueDate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (params._contains("progress")) {
			int progress = params._contains("progress") ? Integer.valueOf(params.get("progress")) : Task.TODO;
			progress = progress < Task.TODO ? Task.TODO : Task.DONE < progress ? Task.DONE : progress;
			task.progress = progress;
		}
		if (params._contains("priority")) {
			int priority = params._contains("priority") ? Integer.valueOf(params.get("priority")) : 5;
			priority = priority < Task.PRIORITY_MIN ? Task.PRIORITY_MIN
				: Task.PRIORITY_MAX < priority ? Task.PRIORITY_MAX : priority;
			task.priority = priority;
		}
		task.save();

		map.put("ok", true);

		renderJSON(map);
	}

	/**
	 * タスクの取得
	 * params:
	 * first 最初のタスクID
	 * count 取得個数
	 */
	public static void getTasks() {
		Logger.log(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		List<Task> list = Task.all().fetch();

		renderJSON(list);
	}

	/**
	 * タスクの情報取得
	 */
	public static void getTaskInfo() {

	}

}
