package com.tusofia.app.homeworkVerification.service;

import java.util.List;

import com.tusofia.app.homeworkVerification.domain.models.service.StudentServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TeacherServiceModel;

public interface StudentService{
	
	void registerStudent(StudentServiceModel student);
	
	List<StudentServiceModel> findNonEnabledStudents();
	
	List<StudentServiceModel> findEnabledStudents();
	
	StudentServiceModel findById(String id);
	
	List<StudentServiceModel> findAllStudents();

	void assignCourseToAllStudents(String courseName);
}
