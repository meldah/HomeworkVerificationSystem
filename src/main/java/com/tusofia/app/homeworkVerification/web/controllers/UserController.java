package com.tusofia.app.homeworkVerification.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tusofia.app.homeworkVerification.domain.models.binding.ChangePasswordBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.binding.StudentRegisterBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.service.StudentServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.UserBaseServiceModel;
import com.tusofia.app.homeworkVerification.service.UserService;
import com.tusofia.app.homeworkVerification.validation.ChangePasswordValidator;
import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
public class UserController extends BaseController {

	protected final UserService userService;
	private final ChangePasswordValidator changePasswordValidator;

	@Autowired
	public UserController(UserService userService, ChangePasswordValidator changePasswordValidator) {
		this.userService = userService;
		this.changePasswordValidator = changePasswordValidator;
	}

	@GetMapping("/login")
	@PreAuthorize("isAnonymous()")
	@PageTitle("Login")
	public ModelAndView login() {
		return super.view("login");
	}

	@GetMapping("/changePassword")
	@PageTitle("Change password")
	public ModelAndView changePassword(@ModelAttribute(name = "changePasswordModel") ChangePasswordBindingModel model,
			ModelAndView modelAndView) {
		modelAndView.addObject("changePasswordModel", model);
		return super.view("/changePassword", modelAndView);
	}

	@PostMapping("/changePassword")
	public ModelAndView changePasswordConfirm(
			@ModelAttribute(name = "changePasswordModel") ChangePasswordBindingModel model, BindingResult bindingResult,
			ModelAndView modelAndView) {
		this.changePasswordValidator.validate(model, bindingResult);

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				if (error instanceof FieldError) {
					FieldError fieldError = (FieldError) error;
					if (fieldError.getField().equals("email")) {
						model.setEmail(null);
					}
				}
			}
			model.setPassword(null);
			model.setConfirmPassword(null);
			return super.view("/changePassword", modelAndView);
		}

		this.userService.changeUserPassword(model.getEmail(), model.getPassword());

		return super.redirect("/home");
	}

	@PostMapping("/users/set-verified/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView verifyUserConfirm(@PathVariable String id) {
		this.userService.setUserEnabled(id, true);

		return super.redirect("/teachers/newTeachers");
	}

	@PostMapping("/users/set-admin/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView setUserAdminConfirm(@PathVariable String id) {
		this.userService.assignUserRole(id, "ROLE_ADMIN");

		return super.redirect("/teachers/allTeachers");
	}
}
