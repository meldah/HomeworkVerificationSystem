package com.tusofia.app.homeworkVerification.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import com.tusofia.app.homeworkVerification.domain.models.binding.TeacherRegisterBindingModel;
import com.tusofia.app.homeworkVerification.repository.UserRepository;
import com.tusofia.app.homeworkVerification.validation.annotation.Validator;
import com.tusofia.app.homeworkVerification.validation.checker.EmailChecker;
import com.tusofia.app.homeworkVerification.validation.checker.PasswordChecker;

@Validator
public class TeacherRegisterValidator implements org.springframework.validation.Validator {
	
	private final UserRepository userRepository;
	private final EmailChecker emailChecker;
	private final PasswordChecker passwordChecker;
	
	@Autowired
	public TeacherRegisterValidator(UserRepository userRepository, EmailChecker emailChecker,
			PasswordChecker passwordChecker) {
		this.userRepository = userRepository;
		this.emailChecker = emailChecker;
		this.passwordChecker = passwordChecker;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return TeacherRegisterBindingModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TeacherRegisterBindingModel teacherRegisterBindingModel = (TeacherRegisterBindingModel) target;
		
		// Validate email
		if (!this.emailChecker.validateEmail(teacherRegisterBindingModel.getEmail())) {
			errors.rejectValue("email",
					String.format(ValidationConstants.EMAIL_NOT_VALID, teacherRegisterBindingModel.getEmail()),
					String.format(ValidationConstants.EMAIL_NOT_VALID, teacherRegisterBindingModel.getEmail()));
		} else {
			// Validate that user with same email is not registered
			if (this.userRepository.findByEmail(teacherRegisterBindingModel.getEmail()).isPresent()) {
				errors.rejectValue("email",
						String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, teacherRegisterBindingModel.getEmail()),
						String.format(ValidationConstants.EMAIL_ALREADY_EXISTS,
								teacherRegisterBindingModel.getEmail()));
			}
		}

		// Validate password
		if (!this.passwordChecker.checkPassword(teacherRegisterBindingModel.getPassword())) {
			errors.rejectValue("password", ValidationConstants.PASSWORD_IS_NOT_VALID,
					ValidationConstants.PASSWORD_IS_NOT_VALID);
		} else {
			// Check if passwords match
			if (!teacherRegisterBindingModel.getPassword().equals(teacherRegisterBindingModel.getConfirmPassword())) {
				errors.rejectValue("password", ValidationConstants.PASSWORDS_DO_NOT_MATCH,
						ValidationConstants.PASSWORDS_DO_NOT_MATCH);
			}
		}
		
	}
}
