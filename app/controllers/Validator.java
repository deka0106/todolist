package controllers;

import models.User;
import play.utils.Java;

public class Validator {

	static Result name(String name) {
		Result res = new Result();

		return res;
	}

	static Result email(String email) {
		Result res = new Result();

		if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
			res.error("メールアドレスが正しくありません");
		} else if (!User.find("email = ?1", email).fetch().isEmpty()) {
			res.error("既に登録されているメールアドレスです");
		}

		return res;
	}

	static Result password(String password) {
		Result res = new Result();

		if (password.length() < 8) {
			res.error("パスワードが短すぎます");
		} else if (20 < password.length()) {
			res.error("パスワードが長すぎます");
		}

		return res;
	}

	public static Result validate(String key, Object... value) {
		Result res = null;
		try {
			res = (Result) Java.invokeChildOrStatic(Validator.class, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
