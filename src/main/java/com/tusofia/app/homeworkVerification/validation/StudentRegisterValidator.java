package com.tusofia.app.homeworkVerification.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import com.tusofia.app.homeworkVerification.validation.annotation.Validator;
import com.tusofia.app.homeworkVerification.validation.checker.EmailChecker;
import com.tusofia.app.homeworkVerification.validation.checker.PasswordChecker;
import com.tusofia.app.homeworkVerification.domain.models.binding.StudentRegisterBindingModel;
import com.tusofia.app.homeworkVerification.repository.StudentRepository;
import com.tusofia.app.homeworkVerification.repository.UserRepository;

@Validator
public class StudentRegisterValidator implements org.springframework.validation.Validator {

	private final UserRepository userRepository;
	private final StudentRepository studentRepository;
	private final EmailChecker emailChecker;
	private final PasswordChecker passwordChecker;

	private static final List<String> FACULTIES = new ArrayList<String>(
			Arrays.asList("Факултет Автоматика", "Електротехнически Факултет", "Машинно-технологичен Факултет",
					"Машиностроителен Факултет", "Факултет Електронна техника и технологии",
					"Факултет по телекомуникации", "Факултет Компютърни системи и технологии", "Факултет по транспорта",
					"Стопански Факултет", "Факултет Приложна математика и информатика",
					"Факултет по германско инженерно обучение и промишлен мениджмънт",
					"Факултет по френско обучение по електроинженерство", "Факултет по английско инженерно обучение"));

	@Autowired
	public StudentRegisterValidator(UserRepository userRepository, StudentRepository studentRepository,
			EmailChecker emailChecker, PasswordChecker passwordChecker) {
		this.userRepository = userRepository;
		this.studentRepository = studentRepository;
		this.emailChecker = emailChecker;
		this.passwordChecker = passwordChecker;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return StudentRegisterBindingModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		StudentRegisterBindingModel studentRegisterBindingModel = (StudentRegisterBindingModel) target;

		// Validate email
		if (!this.emailChecker.validateEmail(studentRegisterBindingModel.getEmail())) {
			errors.rejectValue("email",
					String.format(ValidationConstants.EMAIL_NOT_VALID, studentRegisterBindingModel.getEmail()),
					String.format(ValidationConstants.EMAIL_NOT_VALID, studentRegisterBindingModel.getEmail()));
		}
		else {
			// Validate that user with same email is not registered
			if (this.userRepository.findByEmail(studentRegisterBindingModel.getEmail()).isPresent()) {
				errors.rejectValue("email",
						String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, studentRegisterBindingModel.getEmail()),
						String.format(ValidationConstants.EMAIL_ALREADY_EXISTS,
								studentRegisterBindingModel.getEmail()));
			}
		}

		// Validate password
		if (!this.passwordChecker.checkPassword(studentRegisterBindingModel.getPassword())) {
			errors.rejectValue("password", ValidationConstants.PASSWORD_IS_NOT_VALID,
					ValidationConstants.PASSWORD_IS_NOT_VALID);
		} else {
			// Check if passwords match
			if (!studentRegisterBindingModel.getPassword().equals(studentRegisterBindingModel.getConfirmPassword())) {
				errors.rejectValue("password", ValidationConstants.PASSWORDS_DO_NOT_MATCH,
						ValidationConstants.PASSWORDS_DO_NOT_MATCH);
			}
		}

		// Check if given faculty is one of the existing ones
		if (!FACULTIES.contains(studentRegisterBindingModel.getFaculty())) {
			errors.rejectValue("faculty", ValidationConstants.WRONG_FACULTY, ValidationConstants.WRONG_FACULTY);
		}

		// Check stream
		if (studentRegisterBindingModel.getStream() < 0) {
			errors.rejectValue("stream", ValidationConstants.INVALID_STREAM, ValidationConstants.INVALID_STREAM);
		}

		// Check group
		if (studentRegisterBindingModel.getGroup() < 0) {
			errors.rejectValue("group", ValidationConstants.INVALID_GROUP, ValidationConstants.INVALID_GROUP);
		}

		// Check faculty number
		if (!studentRegisterBindingModel.getFacultyNumber().matches("[0-9]+")) {
			errors.rejectValue("facultyNumber", ValidationConstants.INVALID_FACULTY_NUMBER,
					ValidationConstants.INVALID_FACULTY_NUMBER);
		} else {
			if (this.studentRepository.findByFacultyNumber(studentRegisterBindingModel.getFacultyNumber())
					.isPresent()) {
				errors.rejectValue("facultyNumber", ValidationConstants.FACULTY_NUMBER_EXISTS,
						ValidationConstants.FACULTY_NUMBER_EXISTS);
			}
		}
	}
}