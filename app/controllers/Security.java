package controllers;

import java.util.Objects;

import models.User;
import play.libs.Crypto;

public class Security extends Secure.Security {

	static boolean authenticate(String username, String password) {
		User user = User.find("email = ?1", username).first();
		String hashPassword = Crypto.passwordHash(password);
		return user != null && Objects.equals(hashPassword, user.password);
	}

}
