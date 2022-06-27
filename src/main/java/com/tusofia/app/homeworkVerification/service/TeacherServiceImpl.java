package com.tusofia.app.homeworkVerification.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.Course;
import com.tusofia.app.homeworkVerification.domain.entities.Role;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Teacher;
import com.tusofia.app.homeworkVerification.domain.models.service.TeacherServiceModel;
import com.tusofia.app.homeworkVerification.error.UserNotFoundException;
import com.tusofia.app.homeworkVerification.repository.TeacherRepository;

@Service
public class TeacherServiceImpl implements TeacherService {

	private final TeacherRepository teacherRepository;
	private final UserService userService;
	private final RoleService roleService;
	private final CourseService courseService;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public TeacherServiceImpl(TeacherRepository teacherRepository, UserService userService, RoleService roleService,
			CourseService courseService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.teacherRepository = teacherRepository;
		this.userService = userService;
		this.roleService = roleService;
		this.courseService = courseService;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public void registerTeacher(TeacherServiceModel teacherServiceModel) {

		//Fill the roles db with the roles on teacher register
		roleService.seedRolesInDataSource();
		
		//TODO START: [muzunov] Remove after an interface for addidng courses is implemented 
		courseService.seedCoursesInDataSource();
		
		// Assign vvps course to the teacher initially
		Set<Course> teacherCourses = teacherServiceModel.getCourses();
		teacherCourses = new LinkedHashSet<Course>();
		teacherCourses
				.add(this.modelMapper.map(this.courseService.getCourseByName(CourseService.VVPS_NAME), Course.class));
		teacherServiceModel.setCourses(teacherCourses);

		// Hash the password before db save
		teacherServiceModel.setPassword(bCryptPasswordEncoder.encode(teacherServiceModel.getPassword()));
		
		//The first registered teacher needn't be verified
		if(this.teacherRepository.count() == 0) {
			teacherServiceModel.setEnabled(true);
		}

		// Generate the username based on given first and last name
		teacherServiceModel.
		setUsername(userService.generateUsername(teacherServiceModel.getFirstName(),
				teacherServiceModel.getLastName()));

		// Set the authority for the teacher entity
		teacherServiceModel.setAuthorities(extractRolesFromRoleService());

		Teacher teacher = modelMapper.map(teacherServiceModel, Teacher.class);
		teacherRepository.saveAndFlush(teacher);
	}
	

	@Override
	public List<TeacherServiceModel> findNonEnabledTeachers() {
		List<Teacher> nonEnabledTeachers = this.teacherRepository.findAllByEnabled(false);
		
		List<TeacherServiceModel> nonEnabledTeacherModels = nonEnabledTeachers
				.stream()
				.map(teacher -> this.modelMapper.map(teacher, TeacherServiceModel.class))
				.collect(Collectors.toList());
		
		return nonEnabledTeacherModels;
	}
	
	@Override
	public List<TeacherServiceModel> findEnabledTeachers() {
		List<Teacher> teachers = this.teacherRepository.findAllByEnabled(true);

		List<TeacherServiceModel> teacherServiceModels = teachers.stream()
				.map(teacher -> this.modelMapper.map(teacher, TeacherServiceModel.class)).collect(Collectors.toList());

		return teacherServiceModels;
	}

	private Set<Role> extractRolesFromRoleService(){
		
		Set<Role> authorities = new LinkedHashSet<Role>();
		authorities.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_TEACHER"), Role.class));
		
		//The first registered teacher is also admin and moderator
		if(this.teacherRepository.count() == 0) {
			authorities.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_ADMIN"), Role.class));
			authorities.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_MODERATOR"), Role.class));
		}
		
		return authorities;
	}

	@Override
	public void assignCourse(String id, String courseName) {
		Course course = this.modelMapper.map(this.courseService.getCourseByName(courseName), Course.class);
		
		Teacher teacher = this.teacherRepository.findById(id)
												.orElseThrow(
												() -> new UserNotFoundException
												("Teacher not found!"));
		Set<Course> teacherCourses = teacher.getCourses();
		teacherCourses.add(course);
		teacher.setCourses(teacherCourses);
		
		this.teacherRepository.saveAndFlush(teacher);
	}

	@Override
	public TeacherServiceModel findById(String id) {
		return this.modelMapper.map(this.teacherRepository.findById(id), TeacherServiceModel.class);
	}
	
}