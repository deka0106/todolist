package controllers;

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
	 */
	public static void addTask() {
		System.out.println(params.all().keySet());

		User user = User.find("email = ?1", Security.connected()).first();

	}

}
