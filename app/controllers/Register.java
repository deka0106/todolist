package controllers;

import models.User;
import play.libs.Crypto;
import play.mvc.Controller;

public class Register extends Controller {

	/**
	 * 登録画面
	 */
	public static void signup() {
		// ログインしている場合/board/
		if (Security.isConnected()) {
			Board.index();
			return;
		}
		render();
	}

	/**
	 * 登録完了画面
	 *
	 * params:
	 * name 名前
	 * email メールアドレス
	 * password パスワード
	 */
	public static void authenticate() {
		// ログインしている場合/board/
		if (Security.isConnected()) {
			Board.index();
			return;
		}

		String name = params.get("name");
		String email = params.get("email");
		String password = params.get("password");

		// 引数不足
		if (name == null || email == null || password == null) {
			flash.error("register.error");
			signup();
			return;
		}

		// エラーの有無
		boolean ok = true;
		// 結果
		Result result;

		result = Validator.validate("name", name);
		ok &= result.ok;
		result = Validator.validate("email", email);
		ok &= result.ok;
		result = Validator.validate("password", password);
		ok &= result.ok;

		// エラーが無い場合
		if (ok) {
			User user = new User(name, email, Crypto.passwordHash(password));
			user.save();
			render();
		} else {
			flash.error("register.error");
			signup();
		}
	}

}
