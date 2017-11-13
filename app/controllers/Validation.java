package controllers;

import java.util.HashMap;
import java.util.Map;

import models.User;
import play.mvc.Controller;

public class Validation extends Controller {

	/**
	 * 名前のチェック
	 */
	public static void name() {
		System.out.println(params.all().keySet());

		Map<String, Object> map = new HashMap<>();
		map.put("ok", true);

		renderJSON(map);
	}

	/**
	 * メールアドレスのチェック
	 */
	public static void email() {
		System.out.println(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		boolean ok = true;

		String email = params.get("value");
		if (!User.find("email = ?1", email).fetch().isEmpty()) {
			ok = false;
			map.put("error", "既に登録されているメールアドレスです");
		}

		map.put("ok", ok);
		renderJSON(map);
	}

	/**
	 * パスワードのチェック
	 */
	public static void password() {
		System.out.println(params.all().keySet());

		Map<String, Object> map = new HashMap<>();

		boolean ok = true;

		String password = params.get("value");
		if (password.length() < 8) {
			ok = false;
			map.put("error", "パスワードが短すぎます");
		} else if (20 < password.length()) {
			ok = false;
			map.put("error", "パスワードが長すぎます");
		}

		map.put("ok", ok);
		renderJSON(map);
	}
}
