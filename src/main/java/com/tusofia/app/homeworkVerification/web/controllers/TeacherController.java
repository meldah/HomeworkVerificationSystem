package com.tusofia.app.homeworkVerification.web.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

import com.tusofia.app.homeworkVerification.domain.models.binding.StudentRegisterBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.binding.TeacherRegisterBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TeacherServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.view.AllTeachersViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.NewTeachersViewModel;
import com.tusofia.app.homeworkVerification.service.TeacherService;
import com.tusofia.app.homeworkVerification.service.UserService;
import com.tusofia.app.homeworkVerification.validation.ChangePasswordValidator;
import com.tusofia.app.homeworkVerification.validation.TeacherRegisterValidator;
import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
@RequestMapping("/teachers")
public class TeacherController extends UserController {
	
	private final TeacherService teacherService;
	private final TeacherRegisterValidator teacherRegisterValidator;
	private final ModelMapper modelMapper;
	
	@Autowired
	public TeacherController(UserService userService, TeacherService teacherService, 
			TeacherRegisterValidator teacherRegisterValidator, ModelMapper modelMapper,
			ChangePasswordValidator changePasswordValidator) {
		super(userService, changePasswordValidator);
		this.teacherService = teacherService;
		this.teacherRegisterValidator = teacherRegisterValidator;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/register")
	@PreAuthorize("isAnonymous()")
	@PageTitle("Register teacher")
	public ModelAndView register(@ModelAttribute(name = "teacherRegisterModel") TeacherRegisterBindingModel model,
			ModelAndView modelAndView) {
		modelAndView.addObject("teacherRegisterModel", model);
		return super.view("/teachers/register", modelAndView);
	}
    
	@PostMapping("/register")
	@PreAuthorize("isAnonymous()")
	public ModelAndView registerConfirm(
			@ModelAttribute(name = "teacherRegisterModel") TeacherRegisterBindingModel model,
			BindingResult bindingResult, ModelAndView modelAndView) {
		
		this.teacherRegisterValidator.validate(model, bindingResult);

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError error : errors) {
				if(error instanceof FieldError) {
					FieldError fieldError = (FieldError) error;
					if(fieldError.getField().equals("email")) {
						model.setEmail(null);
					}
				}
			}
			model.setPassword(null);
			model.setConfirmPassword(null);
			return super.view("/teachers/register", modelAndView);
		}
		
		try {
			teacherService.registerTeacher(modelMapper.map(model, TeacherServiceModel.class));
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			return super.redirect("/teachers/register");
		}
		return super.redirect("/home");
	}
	
	@GetMapping("/newTeachers")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PageTitle("New teachers")
	public ModelAndView newTeachers(ModelAndView modelAndView) {
		List<TeacherServiceModel> teacherServiceModels = 
				this.teacherService.findNonEnabledTeachers();
		
		List<NewTeachersViewModel> teachers = teacherServiceModels
				.stream()
				.map(serviceModel -> this.modelMapper.map(serviceModel, NewTeachersViewModel.class))
				.collect(Collectors.toList());
		
		
		modelAndView.addObject("teachers", teachers);
		return super.view("/teachers/newTeachers", modelAndView);
	}
	
	@GetMapping("/allTeachers")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PageTitle("All teachers")
	public ModelAndView allTeachers(ModelAndView modelAndView) {
		List<TeacherServiceModel> teacherServiceModels = 
				this.teacherService.findEnabledTeachers();
		
		List<AllTeachersViewModel> teachers = teacherServiceModels
				.stream()
				.map(serviceModel -> this.modelMapper.map(serviceModel, AllTeachersViewModel.class))
				.collect(Collectors.toList());
		
		
		modelAndView.addObject("teachers", teachers);
		return super.view("/teachers/allTeachers", modelAndView);
	}
	
	@PostMapping("/delete/{id}")	
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView deleteTeacherConfirm(@PathVariable String id) {
		super.userService.deleteUser(id);
		return super.redirect("/teachers/allTeachers");
	}
	
}
