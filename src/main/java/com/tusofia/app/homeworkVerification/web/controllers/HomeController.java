package com.tusofia.app.homeworkVerification.web.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.User;
import com.tusofia.app.homeworkVerification.domain.models.view.StudentsBaseViewModel;
import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
public class HomeController extends BaseController {
	
	private final ModelMapper modelMapper;
	
	@Autowired
	public HomeController(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@GetMapping("/")
	@PreAuthorize("isAnonymous()")
	public ModelAndView index() {
		return super.view("index");
	}
	
	@GetMapping("/home")
	@PreAuthorize("isAuthenticated()")
	@PageTitle("Home")
	public ModelAndView home(ModelAndView modelAndView, Principal principal) {
		User user = (User)((Authentication) principal).getPrincipal();
		if(user instanceof Student) {
			Student student = (Student)user;
			StudentsBaseViewModel model = this.modelMapper.map(student, StudentsBaseViewModel.class);
			modelAndView.addObject("studentDataModel", model);
		}
		return super.view("/home", modelAndView);
	}
}
