package controllers;

import play.mvc.Controller;

public class Application extends Controller {

	/**
	 * トップページ (紹介)
	 */
	public static void index() {
		// ログインしている場合
		if (Security.isConnected()) {
			Board.index();
		}
		render();
	}

}