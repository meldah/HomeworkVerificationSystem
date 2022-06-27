package com.tusofia.app.homeworkVerification.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.Course;
import com.tusofia.app.homeworkVerification.domain.entities.Exercise;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.error.CourseNotFoundException;
import com.tusofia.app.homeworkVerification.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

	private final CourseRepository courseRepository;
	private final ExerciseService exerciseService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public CourseServiceImpl(CourseRepository courseRepository, ExerciseService exerciseService,
			ModelMapper modelMapper) {
		this.courseRepository = courseRepository;
		this.exerciseService = exerciseService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public void seedCoursesInDataSource() {
		if(this.courseRepository.count() == 0) {
			this.addVVPSCourse();
		}
	}

	@Override
	public Set<CourseServiceModel> extractAllCourses() {
		return this.courseRepository.findAll()
				.stream()
				.map(r -> this.modelMapper.map(r, CourseServiceModel.class))
				.collect(Collectors.toSet());
	}
	
	@Override
	public Set<CourseServiceModel> getCoursesByStudent(String id) {
		return this.courseRepository.findAllByStudentId(id)
				.stream()
				.map(c -> this.modelMapper.map(c, CourseServiceModel.class))
				.collect(Collectors.toSet());
	}
	
	@Override
	public Set<CourseServiceModel> getCoursesByTeacher(String id) {
		return this.courseRepository.findAllByTeacherId(id)
				.stream()
				.map(c -> this.modelMapper.map(c, CourseServiceModel.class))
				.collect(Collectors.toSet());
	}
	
	private void addVVPSCourse(){
		this.exerciseService.seedVVPSExercisesInDataSource();

		Course vvpsCourse = new Course();
		vvpsCourse.setName(VVPS_NAME);
		Set<ExerciseServiceModel> exerciseServiceModels = exerciseService.extractAllExercises();
		vvpsCourse.setExercises(
				exerciseServiceModels
				.stream()
				.map(e -> this.modelMapper.map(e, Exercise.class))
				.collect(Collectors.toSet()));
		
		this.courseRepository.saveAndFlush(vvpsCourse);
	}

	@Override
	public CourseServiceModel getCourseByName(String courseName) {
		Course course = courseRepository.findByName(courseName)
				.orElseThrow(() -> new CourseNotFoundException("Course " + courseName + " was not found!"));
		
		return this.modelMapper.map(course, CourseServiceModel.class);
	}

	@Override
	public CourseServiceModel getCourseById(String id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new CourseNotFoundException("Course was not found!"));
		return this.modelMapper.map(course, CourseServiceModel.class);
	}

	@Override
	public void addCourse(String courseName, String courseDescription, String teacherId) {
		Course course = new Course();
		//Set course name
		course.setName(courseName);
		
		//Set course description
		if(!courseDescription.isEmpty()) {
			course.setDescription(courseDescription);
		}
		
		this.courseRepository.saveAndFlush(course);
	}

	@Override
	public List<CourseServiceModel> getAll() {
		return this.courseRepository.findAll()
				.stream()
				.map(course -> this.modelMapper.map(course, CourseServiceModel.class))
				.collect(Collectors.toList());
	}

	@Override
	public void addExerciseToCourse(String courseId, Exercise exercise) {
		CourseServiceModel courseServiceModel = this.getCourseById(courseId);
		Course course = this.modelMapper.map(courseServiceModel, Course.class);
		
		Set<Exercise> exercises = course.getExercises();
		if(exercises == null) {
			exercises = new LinkedHashSet<Exercise>();
		}
		exercises.add(exercise);
		
		this.courseRepository.saveAndFlush(course);
	}
}