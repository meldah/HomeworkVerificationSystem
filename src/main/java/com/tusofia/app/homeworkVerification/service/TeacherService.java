package com.tusofia.app.homeworkVerification.service;

import java.util.List;

import com.tusofia.app.homeworkVerification.domain.models.service.TeacherServiceModel;

public interface TeacherService {
	void registerTeacher(TeacherServiceModel teacherServiceModel);
	
	List<TeacherServiceModel> findNonEnabledTeachers();

	List<TeacherServiceModel> findEnabledTeachers();
	
	TeacherServiceModel findById(String id);
	
	void assignCourse(String teacherId, String courseName);
}
