package com.tusofia.app.homeworkVerification.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.Course;
import com.tusofia.app.homeworkVerification.domain.entities.Role;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.models.service.StudentServiceModel;
import com.tusofia.app.homeworkVerification.error.UserNotFoundException;
import com.tusofia.app.homeworkVerification.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final RoleService roleService;
	private final UserService userService;
	private final CourseService courseService;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public StudentServiceImpl(StudentRepository studentRepository, RoleService roleService, UserService userService,
			CourseService courseService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.studentRepository = studentRepository;
		this.roleService = roleService;
		this.userService = userService;
		this.courseService = courseService;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public void registerStudent(StudentServiceModel studentServiceModel) {
		
		//Hash the password before db save
		studentServiceModel.setPassword(bCryptPasswordEncoder.encode(studentServiceModel.getPassword()));

		//Generate the username based on given first and last name
		studentServiceModel.setUsername(userService.generateUsername(studentServiceModel.getFirstName(),
				studentServiceModel.getLastName()));
		
		//Set the authority for the student entity
		Set<Role> authorities = new LinkedHashSet<Role>();
		authorities.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_STUDENT"), Role.class));
		studentServiceModel.setAuthorities(authorities);
		
		//Assign vvps course to the student initially
		Set<Course> studentCourses = this.courseService.getAll()
				.stream()
				.map(course -> (this.modelMapper.map(course, Course.class)))
				.collect(Collectors.toSet());
		
		studentServiceModel.setCourses(studentCourses);
		
		Student student = modelMapper.map(studentServiceModel, Student.class);
		studentRepository.saveAndFlush(student);
		
		//TODO: not all students should be assigned this course
		//assignCourse(student.getUsername(), CourseService.VVPS_NAME);
	}
	
	@Override
	public List<StudentServiceModel> findNonEnabledStudents() {
		List<Student> nonEnabledStudents = this.studentRepository.findAllByEnabled(false);
		
		List<StudentServiceModel> nonEnabledStudentModels = nonEnabledStudents
				.stream()
				.map(student -> this.modelMapper.map(student, StudentServiceModel.class))
				.collect(Collectors.toList());
		
		return nonEnabledStudentModels;
	}
	
	@Override
	public List<StudentServiceModel> findEnabledStudents() {
		List<Student> students = this.studentRepository.findAllByEnabled(true);

		List<StudentServiceModel> studentServiceModels = students.stream()
				.map(student -> this.modelMapper.map(student, StudentServiceModel.class))
				.collect(Collectors.toList());

		return studentServiceModels;
	}
	
	@Override
	public void assignCourseToAllStudents(String courseName) {
		Course course = this.modelMapper.map(this.courseService.getCourseByName(courseName), Course.class);
		List<Student> students = this.studentRepository.findAll();
		for(Student student : students) {
			assignCourse(student, course);
		}
	}

	private void assignCourse(Student student, Course course) {
		Set<Course> studentCourses = student.getCourses();
		if(studentCourses == null) {
			studentCourses = new LinkedHashSet<Course>();
		}
		studentCourses.add(course);
		student.setCourses(studentCourses);
		
		this.studentRepository.saveAndFlush(student);
	}

	@Override
	public StudentServiceModel findById(String id) {
		Student student = this.studentRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Student was not found!"));
		return this.modelMapper.map(student, StudentServiceModel.class);
	}
	@Override
	public List<StudentServiceModel> findAllStudents() {
		List<Student> students = this.studentRepository.findAll();
		List<StudentServiceModel> studentServiceModels = students.stream()
				.map(student -> this.modelMapper.map(student, StudentServiceModel.class))
				.collect(Collectors.toList());
		return studentServiceModels;
	}
}