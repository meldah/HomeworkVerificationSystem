package com.tusofia.app.homeworkVerification.domain.models.service;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Course;

public class TeacherServiceModel extends UserBaseServiceModel {
	
	private Set<Course> courses;
	
	public TeacherServiceModel() {
		
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	
}
