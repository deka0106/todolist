package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
		System.out.println(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		// タイトルは必須
		if (!params._contains("title")) {
			map.put("ok", false);
			map.put("error", "タイトルがありません");
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
		progress = progress < Task.TODO ? Task.TODO : Task.DONE < progress ? Task.DONE : progress;
		int priority = params._contains("priority") ? Integer.valueOf(params.get("priority")) : 5;

		User user = User.find("email = ?1", Security.connected()).first();

		Task task = new Task(user.board, title, detail, createDate, dueDate, progress, priority);
		user.board.tasks.add(task);
		task.save();

		map.put("ok", true);
		renderJSON(map);
	}

}
