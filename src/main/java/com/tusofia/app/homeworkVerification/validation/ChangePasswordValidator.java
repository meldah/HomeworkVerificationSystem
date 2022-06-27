package com.tusofia.app.homeworkVerification.validation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import com.tusofia.app.homeworkVerification.domain.entities.User;
import com.tusofia.app.homeworkVerification.domain.models.binding.ChangePasswordBindingModel;
import com.tusofia.app.homeworkVerification.repository.UserRepository;
import com.tusofia.app.homeworkVerification.validation.annotation.Validator;
import com.tusofia.app.homeworkVerification.validation.checker.EmailChecker;
import com.tusofia.app.homeworkVerification.validation.checker.PasswordChecker;

@Validator
public class ChangePasswordValidator implements org.springframework.validation.Validator {
	private final UserRepository userRepository;
	private final EmailChecker emailChecker;
	private final PasswordChecker passwordChecker;
	
	@Autowired
	public ChangePasswordValidator(UserRepository userRepository,
			EmailChecker emailChecker, PasswordChecker passwordChecker) {
		this.userRepository = userRepository;
		this.emailChecker = emailChecker;
		this.passwordChecker = passwordChecker;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePasswordBindingModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ChangePasswordBindingModel changePasswordBindingModel = (ChangePasswordBindingModel) target;

		// Validate email
		if (!this.emailChecker.validateEmail(changePasswordBindingModel.getEmail())) {
			errors.rejectValue("email",
					String.format(ValidationConstants.EMAIL_NOT_VALID, changePasswordBindingModel.getEmail()),
					String.format(ValidationConstants.EMAIL_NOT_VALID, changePasswordBindingModel.getEmail()));
		}
		else {
			// Validate that user with this email is registered
			Optional<User> user = this.userRepository.findByEmail(changePasswordBindingModel.getEmail());
			if (!user.isPresent()) {
				errors.rejectValue("email",
						String.format(ValidationConstants.USER_DOES_NOT_EXIST, changePasswordBindingModel.getEmail()),
						String.format(ValidationConstants.USER_DOES_NOT_EXIST,
								changePasswordBindingModel.getEmail()));
			}
		}

		// Validate password
		if (!this.passwordChecker.checkPassword(changePasswordBindingModel.getPassword())) {
			errors.rejectValue("password", ValidationConstants.PASSWORD_IS_NOT_VALID,
					ValidationConstants.PASSWORD_IS_NOT_VALID);
		} else {
			// Check if passwords match
			if (!changePasswordBindingModel.getPassword().equals(changePasswordBindingModel.getConfirmPassword())) {
				errors.rejectValue("password", ValidationConstants.PASSWORDS_DO_NOT_MATCH,
						ValidationConstants.PASSWORDS_DO_NOT_MATCH);
			}
		}
	}
}
