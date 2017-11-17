package controllers;

import java.util.HashMap;
import java.util.Map;

import models.User;
import play.mvc.Controller;
import utils.Validator;

public class Validation extends Controller {

	/**
	 * 名前のチェック
	 */
	public static void name() {
		renderJSON(Validator.validateName(params.get("value")));
	}

	/**
	 * メールアドレスのチェック
	 */
	public static void email() {
		renderJSON(Validator.validateEmail(params.get("value")));
	}

	/**
	 * パスワードのチェック
	 */
	public static void password() {
		renderJSON(Validator.validatePassword(params.get("value")));
	}
}
