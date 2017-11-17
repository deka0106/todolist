package controllers;

import models.User;
import play.mvc.Controller;
import utils.DigestGenerator;

public class Login extends Controller {

	public static void index() {
		render();
	}

	/**
	 * ログイン用データを受け付ける
	 */
	public static void postLogin() {

		String email = params.get("email");
		String password = DigestGenerator.getSHA256(params.get("password"));

		User user = User.authenticate(email, password);

		if (user == null) {
			index();
		} else {
			Board.index();
		}
	}
}
