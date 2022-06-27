package com.tusofia.app.homeworkVerification.domain.entities;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "`teachers`")
@Access(AccessType.PROPERTY)
@PrimaryKeyJoinColumn(name = "id")
public class Teacher extends User {
	
	private Set<Course> courses;

	public Teacher() {
	}

	@ManyToMany(targetEntity = Course.class)
	@JoinTable(name = "teachers_courses",
		joinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id") },
		inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id") })
	public Set<Course> getCourses() {
		return courses;
	}
	
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
}
