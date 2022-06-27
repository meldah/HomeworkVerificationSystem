package com.tusofia.app.homeworkVerification.service;

import java.util.List;
import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Exercise;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;

public interface CourseService {
	final static String VVPS_NAME = "Валидация и верификация на програмни системи";
	
	void seedCoursesInDataSource();
	
	Set<CourseServiceModel> extractAllCourses();
	
	Set<CourseServiceModel> getCoursesByStudent(String id);
	
	Set<CourseServiceModel> getCoursesByTeacher(String id);

	CourseServiceModel getCourseByName(String courseName);
	
	CourseServiceModel getCourseById(String id);

	void addCourse(String courseName, String courseDescription, String teacherId);
	
	List<CourseServiceModel> getAll();

	void addExerciseToCourse(String courseId, Exercise exercise);
}
