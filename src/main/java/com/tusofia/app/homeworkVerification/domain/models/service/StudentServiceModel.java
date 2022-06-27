package com.tusofia.app.homeworkVerification.domain.models.service;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Course;

public class StudentServiceModel extends UserBaseServiceModel {
	
	private String faculty;
	private String facultyNumber;
	private int semester;
	private int stream;
	private int group;
	
	private Set<Course> courses;
	
	public StudentServiceModel(){
	}
	
	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getFacultyNumber() {
		return facultyNumber;
	}

	public void setFacultyNumber(String facultyNumber) {
		this.facultyNumber = facultyNumber;
	}

	public int getStream() {
		return stream;
	}

	public void setStream(int stream) {
		this.stream = stream;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	
}
