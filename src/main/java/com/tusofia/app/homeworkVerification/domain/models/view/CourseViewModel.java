package com.tusofia.app.homeworkVerification.domain.models.view;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Exercise;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Teacher;

public class CourseViewModel extends BaseViewModel {
	private String name;
	private String description;

	private Set<Student> students;
	private Set<Teacher> teachers;
	private Set<Exercise> exercises;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	public Set<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}
	public Set<Exercise> getExercises() {
		return exercises;
	}
	public void setExercises(Set<Exercise> exercises) {
		this.exercises = exercises;
	}
	
}