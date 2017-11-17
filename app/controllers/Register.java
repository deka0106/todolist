package controllers;

import java.util.Map;

import models.User;
import play.mvc.Controller;
import utils.DigestGenerator;
import utils.Validator;

public class Register extends Controller {

	public static void index() {
		render();
	}

	/**
	 * 登録用データを受け付ける
	 */
	public static void postRegister() {
		
		/**
		 * 入力にエラーがあるかどうか
		 */
		boolean ok = true;

		String name = params.get("name");
		session.put("name", name);

		String email = params.get("email");
		Map<String, Object> emailValidation = Validator.validateEmail(email);
		if ((boolean) emailValidation.get("ok")) {
			session.put("email", email);
		}

		String password = params.get("password");
		String hashPassword = DigestGenerator.getSHA256(password);
		if (password.length() < 8 || 20 < password.length()) {
			ok = false;
		} else {
			session.put("password", hashPassword);
		}
		
		if (ok) {
			confirm();
		} else {
			index();
		}
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
	public static void complete(Map<String, String> data) {

		String name = session.get("name");
		String email = session.get("email");
		String password = session.get("password");
		
		session.clear();
		
		User user = new User(name, email, password);
		user.save();

		render();
	}
}
