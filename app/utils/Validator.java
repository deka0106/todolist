package utils;

import java.util.HashMap;
import java.util.Map;

import models.User;

public class Validator {

	public static Map<String, Object> validateName(String name) {
		Map<String, Object> map = new HashMap<>();

		boolean ok = true;

		map.put("ok", ok);

		return map;
	}

	public static Map<String, Object> validateEmail(String email) {
		Map<String, Object> map = new HashMap<>();

		boolean ok = true;

		if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
			ok = false;
			map.put("error", "メールアドレスが正しくありません");
		} else if (!User.find("email = ?1", email).fetch().isEmpty()) {
			ok = false;
			map.put("error", "既に登録されているメールアドレスです");
		}

		map.put("ok", ok);

		return map;
	}

	public static Map<String, Object> validatePassword(String password) {
		Map<String, Object> map = new HashMap<>();

		boolean ok = true;

		if (password.length() < 8) {
			ok = false;
			map.put("error", "パスワードが短すぎます");
		} else if (20 < password.length()) {
			ok = false;
			map.put("error", "パスワードが長すぎます");
		}

		map.put("ok", ok);

		return map;
	}

}
