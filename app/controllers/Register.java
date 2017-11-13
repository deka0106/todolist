package controllers;

import play.mvc.Controller;

public class Register extends Controller {

	public static void index() {
		render();
	}

	/**
	 * 登録用データを受け付ける
	 */
	public static void postRegister() {
		confirm();
	}

	/**
	 * 確認画面
	 */
	public static void confirm() {
		render();
	}

	/**
	 * 完了画面
	 */
	public static void complete() {
		render();
	}
}
