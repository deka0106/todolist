package controllers;

import play.mvc.Controller;

public class Validate extends Controller {

	/**
	 * 名前のチェック
	 */
	public static void name() {
		renderJSON(Validator.validate("name", params.get("value")).getMap());
	}

	/**
	 * メールアドレスのチェック
	 */
	public static void email() {
		renderJSON(Validator.validate("email", params.get("value")).getMap());
	}

	/**
	 * パスワードのチェック
	 */
	public static void password() {
		renderJSON(Validator.validate("password", params.get("value")).getMap());
	}
}
