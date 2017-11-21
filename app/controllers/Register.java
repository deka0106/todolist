package controllers;

import java.util.Map;

import models.User;
import play.libs.Crypto;
import play.mvc.Controller;
import utils.Validator;

public class Register extends Controller {

	/**
	 * 登録画面
	 */
	public static void index() {
		// ログインしている場合/board/
		if (Security.isConnected()) {
			Board.index();
			return;
		}
		render();
	}

	/**
	 * 確認画面
	 *
	 * params:
	 *  name 名前
	 *  email メールアドレス
	 *  password パスワード
	 */
	public static void confirm() {
		// ログインしている場合/board/
		if (Security.isConnected()) {
			Board.index();
			return;
		}

		// 入力にエラーがあるかどうか
		boolean ok = true;

		// 結果
		Map<String, Object> result;

		String name = params.get("name");
		String email = params.get("email");
		String password = params.get("password");

		if (name == null || email == null || password == null) {
			flash.error("何らかのエラーが発生しました");
			index();
			return;
		}

		result = Validator.validateName(name);
		if ((boolean) result.get("ok")) {
			session.put("name", name);
		} else ok = false;

		result = Validator.validateEmail(email);
		if ((boolean) result.get("ok")) {
			session.put("email", email);
		} else ok = false;

		String hashPassword = Crypto.passwordHash(password);
		result = Validator.validatePassword(password);
		if ((boolean) result.get("ok")) {
			session.put("password", hashPassword);
		} else ok = false;

		if (ok) {
			render();
		} else {
			flash.error("登録情報に誤りがありました");
			index();
		}
	}

	/**
	 * 完了画面
	 */
	public static void complete() {
		// ログインしている場合/board/
		if (Security.isConnected()) {
			Board.index();
			return;
		}

		String name = session.get("name");
		String email = session.get("email");
		String password = session.get("password");

		if (name == null || email == null || password == null) {
			flash.error("登録に失敗しました");
			index();
			return;
		}

		session.clear();

		User user = new User(name, email, password);
		user.save();

		render();
	}
}
