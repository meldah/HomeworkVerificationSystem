package com.tusofia.app.homeworkVerification.domain.entities;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "`courses`")
@Access(AccessType.PROPERTY)
public class Course extends BaseEntity {

	private String name;
	private String description;

	private Set<Student> students;
	private Set<Teacher> teachers;
	private Set<Exercise> exercises;

	public Course() {
	}

	public Course(String name) {
		this.name = name;
	}

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "`description`", nullable = true, unique = false)
	@Type(type="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(mappedBy = "courses")
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	@ManyToMany(mappedBy = "courses")
	public Set<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}

	@OneToMany(targetEntity = Exercise.class, orphanRemoval = true)
	@JoinColumn(name = "course_id", referencedColumnName = "id")
	public Set<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Set<Exercise> exercises) {
		this.exercises = exercises;
	}
}