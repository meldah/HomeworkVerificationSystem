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
import com.tusofia.app.homeworkVerification.domain.models.service.StudentServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TeacherServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.view.AllStudentsViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.AllTeachersViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.NewStudentsViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.NewTeachersViewModel;
import com.tusofia.app.homeworkVerification.service.StudentService;
import com.tusofia.app.homeworkVerification.service.UserService;
import com.tusofia.app.homeworkVerification.validation.ChangePasswordValidator;
import com.tusofia.app.homeworkVerification.validation.StudentRegisterValidator;
import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
@RequestMapping("/students")
public class StudentController extends UserController {

	private final StudentService studentService;
	private final ModelMapper modelMapper;
	private final StudentRegisterValidator studentRegisterValidator;
	
	@Autowired
    public StudentController(UserService userService, StudentService studentService, 
    		ModelMapper modelMapper, StudentRegisterValidator studentRegisterValidator,
    		ChangePasswordValidator changePasswordValidator) {
		super(userService, changePasswordValidator);
		this.studentService = studentService;
		this.modelMapper = modelMapper;
		this.studentRegisterValidator = studentRegisterValidator;
	}

	@GetMapping("/register")
	@PreAuthorize("isAnonymous()")
	@PageTitle("Register student")
	public ModelAndView register(@ModelAttribute(name = "studentRegisterModel") StudentRegisterBindingModel model, ModelAndView modelAndView) {
		modelAndView.addObject("studentRegisterModel", model);
		return super.view("/students/register", modelAndView);
	}
    
	@PostMapping("/register")
	@PreAuthorize("isAnonymous()")
	public ModelAndView registerConfirm(@ModelAttribute(name = "studentRegisterModel") StudentRegisterBindingModel model, BindingResult bindingResult,
			ModelAndView modelAndView) {

		this.studentRegisterValidator.validate(model, bindingResult);

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError error : errors) {
				if(error instanceof FieldError) {
					FieldError fieldError = (FieldError) error;
					if(fieldError.getField().equals("email")) {
						model.setEmail(null);
					}
					else if(fieldError.getField().equals("faculty")) {
						model.setFaculty(null);
					}
	/*				else if(fieldError.getField().equals("stream")) {
						model.setStream(0);
					}
					else if(fieldError.getField().equals("group")) {
						model.setGroup(0);
					}*/
					else if(fieldError.getField().equals("facultyNumber")) {
						model.setFacultyNumber(null);
					}
				}
			}
			model.setPassword(null);
			model.setConfirmPassword(null);
			return super.view("/students/register", modelAndView);
		}
		
		try {
			studentService.registerStudent(modelMapper.map(model, StudentServiceModel.class));
		} catch (IllegalArgumentException iae) {
			return super.redirect("/students/register");
		}
		return super.redirect("/home");
	}
	
	@GetMapping("/newStudents")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PageTitle("New students")
	public ModelAndView newStudents(ModelAndView modelAndView) {
		List<StudentServiceModel> studentServiceModels = 
				this.studentService.findNonEnabledStudents();
		
		List<NewStudentsViewModel> students = studentServiceModels
				.stream()
				.map(serviceModel -> this.modelMapper.map(serviceModel, NewStudentsViewModel.class))
				.collect(Collectors.toList());
		
		
		modelAndView.addObject("students", students);
		return super.view("/students/newStudents", modelAndView);
	}
	
	@GetMapping("/allStudents")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PageTitle("All students")
	public ModelAndView allStudents(ModelAndView modelAndView) {
		List<StudentServiceModel> studentServiceModels = 
				this.studentService.findEnabledStudents();
		
		List<AllStudentsViewModel> students = studentServiceModels
				.stream()
				.map(serviceModel -> this.modelMapper.map(serviceModel, AllStudentsViewModel.class))
				.collect(Collectors.toList());
		
		
		modelAndView.addObject("students", students);
		return super.view("/students/allStudents", modelAndView);
	}
	
	@PostMapping("/delete/{id}")	
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView deleteStudentConfirm(@PathVariable String id) {
		super.userService.deleteUser(id);
		
		return super.redirect("/students/allStudents");
	}
}
