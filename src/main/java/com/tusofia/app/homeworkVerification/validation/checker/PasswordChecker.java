package com.tusofia.app.homeworkVerification.validation.checker;

import org.springframework.stereotype.Component;

@Component
public class PasswordChecker {
	
	public boolean checkPassword(String password) {
		return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
	}
}
