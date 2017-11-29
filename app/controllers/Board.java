package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		String detail = params._contains("detail") && !params.get("detail").equals("null") ? params.get("detail") : "";
		Date createDate = new Date();
		Date dueDate = null;
		try {
			dueDate = params._contains("dueDate") && !params.get("dueDate").equals("null")
				? DateFormat.getInstance().parse(params.get("dueDate"))
				: null;
			System.out.println(dueDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int progress = params._contains("progress") && !params.get("progress").equals("null")
			? Integer.valueOf(params.get("progress"))
			: Task.TODO;
		int priority = params._contains("priority") && !params.get("priority").equals("null")
			? Integer.valueOf(params.get("priority"))
			: 5;

		User user = User.find("email = ?1", Security.connected()).first();

		Task task = new Task(user.board, title, detail, createDate, dueDate, progress, priority);
		user.board.tasks.add(task);
		task.save();

		map.put("ok", true);
		map.put("task", task.toMap());

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
		Task task = Task.find("board_id = ?1 and id = ?2", user.board.id, Long.parseLong(params.get("taskId"))).first();

		if (task == null) {
			map.put("ok", false);
			map.put("error", "Not found");
			renderJSON(map);
			return;
		}

		if (params._contains("title") && !params.get("title").equals("null")) {
			task.title = params.get("title");
		}
		if (params._contains("detail") && !params.get("detail").equals("null")) {
			task.detail = params.get("detail");
		}
		if (params._contains("dueDate") && !params.get("dueDate").equals("null")) {
			try {
				task.dueDate = DateFormat.getInstance().parse(params.get("dueDate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (params._contains("progress") && !params.get("progress").equals("null")) {
			int progress = Integer.valueOf(params.get("progress"));
			progress = progress < Task.TODO ? Task.TODO : Task.DONE < progress ? Task.DONE : progress;
			task.progress = progress;
		}
		if (params._contains("priority") && !params.get("priority").equals("null")) {
			int priority = Integer.valueOf(params.get("priority"));
			priority = priority < Task.PRIORITY_MIN ? Task.PRIORITY_MIN
				: Task.PRIORITY_MAX < priority ? Task.PRIORITY_MAX : priority;
			task.priority = priority;
		}
		task.save();

		map.put("ok", true);
		map.put("task", task.toMap());

		renderJSON(map);
	}

	/**
	 * タスクの取得
	 * params:
	 * first 取得する最初のタスクのインデックス
	 * count 取得個数
	 * progress タスクの進捗状況
	 */
	public static void getTasks() {
		Logger.log(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		// progressは必須
		if (!params._contains("progress")) {
			map.put("ok", false);
			map.put("error", "No progress");
			renderJSON(map);
			return;
		}

		int progress = !params.get("progress").equals("null")
			? Integer.parseInt(params.get("progress"))
			: Task.TODO;
		int first = params._contains("first") && !params.get("first").equals("null")
			? Integer.parseInt(params.get("first"))
			: 0;
		int count = params._contains("count") && !params.get("count").equals("null")
			? Integer.parseInt(params.get("count"))
			: 5;

		User user = User.find("email = ?1", Security.connected()).first();

		List<Map<String, Object>> list = Task
			.find("board_id = ?1 and progress = ?2 " +
				"order by " +
				"priority asc, " +
				"case when dueDate is null then 0 else 1 end desc, " +
				"dueDate asc, " +
				"createDate asc",
				user.board.id, progress)
			.from(first)
			.fetch(count)
			.stream()
			.map(t -> ((Task) t).toMap()).collect(Collectors.toList());

		map.put("ok", true);
		map.put("tasks", list);

		renderJSON(map);
	}

}
