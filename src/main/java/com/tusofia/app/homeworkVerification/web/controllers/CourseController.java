package com.tusofia.app.homeworkVerification.web.controllers;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tusofia.app.homeworkVerification.domain.checkers.HomeworkChecker.SubmissionResult;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Teacher;
import com.tusofia.app.homeworkVerification.domain.models.binding.CourseAddBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.binding.HomeworkSubmitBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.view.CourseViewModel;
import com.tusofia.app.homeworkVerification.service.CourseService;
import com.tusofia.app.homeworkVerification.service.StudentService;
import com.tusofia.app.homeworkVerification.service.TeacherService;
import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {
	
	private final CourseService courseService;
	private final TeacherService teacherService;
	private final StudentService studentService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public CourseController(CourseService courseService, TeacherService teacherService,
			StudentService studentService, ModelMapper modelMapper) {
		this.courseService = courseService;
		this.teacherService = teacherService;
		this.studentService = studentService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ModelAndView addCourse(@ModelAttribute(name = "addCourseModel") CourseAddBindingModel courseData, 
			RedirectAttributes redirectAttributes){
		
		//Add the course
		this.courseService.addCourse(courseData.getCourseName(), courseData.getCourseDescription(), 
				courseData.getTeacherId());
		//Set course teacher
		this.teacherService.assignCourse(courseData.getTeacherId(), courseData.getCourseName());
		
		//Assign course to all students
		this.studentService.assignCourseToAllStudents(courseData.getCourseName());
		
		return super.redirect("/course/getAllByTeacher/" + courseData.getTeacherId());
	}
	
	@GetMapping("/getAllByStudent/{id}")
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@PageTitle("My courses")
	public ModelAndView allCoursesByStudentId(@PathVariable String id, ModelAndView modelAndView) {
		Set<CourseServiceModel> courseServiceModels = 
				this.courseService.getCoursesByStudent(id);
		
		List<CourseViewModel> courses = courseServiceModels
				.stream()
				.map(serviceModel -> this.modelMapper.map(serviceModel, CourseViewModel.class))
				.collect(Collectors.toList());
		
		modelAndView.addObject("courses", courses);
		return super.view("/courses/courses", modelAndView);
	}
	
	@GetMapping("/getAllByTeacher/{id}")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PageTitle("My courses")
	public ModelAndView allCoursesByTeacherId(@PathVariable String id, ModelAndView modelAndView) {
		Set<CourseServiceModel> courseServiceModels = 
				this.courseService.getCoursesByTeacher(id);
		
		List<CourseViewModel> courses = courseServiceModels
				.stream()
				.map(serviceModel -> this.modelMapper.map(serviceModel, CourseViewModel.class))
				.collect(Collectors.toList());
		
		modelAndView.addObject("courses", courses);
		return super.view("/courses/courses", modelAndView);
	}
	
	@GetMapping("/viewCourse/{id}")
	@PreAuthorize("isAuthenticated()")
	@PageTitle("View Course")
	public ModelAndView competeCourse(@PathVariable String id, ModelAndView modelAndView) {
		CourseServiceModel courseServiceModel = 
				this.courseService.getCourseById(id);
		
		CourseViewModel course = this.modelMapper.map(courseServiceModel, CourseViewModel.class);
		
		modelAndView.addObject("course", course);
		return super.view("/courses/viewCourse", modelAndView);
	}
	
}