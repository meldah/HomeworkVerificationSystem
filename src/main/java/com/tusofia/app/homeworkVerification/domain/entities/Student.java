package com.tusofia.app.homeworkVerification.domain.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "`students`")
@Access(AccessType.PROPERTY)
@PrimaryKeyJoinColumn(name = "id")
public class Student extends User {

	private String faculty;
	private String facultyNumber;
	private int semester;
	private int stream;
	private int group;

	private Set<Course> courses;

	public Student() {
	}
	
	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	@Column(nullable = false)
	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	@Column(name = "faculty_number", nullable = false, unique = true)
	public String getFacultyNumber() {
		return facultyNumber;
	}

	public void setFacultyNumber(String facultyNumber) {
		this.facultyNumber = facultyNumber;
	}

	@Column(nullable = false)
	public int getStream() {
		return stream;
	}

	public void setStream(int stream) {
		this.stream = stream;
	}

	@Column(name = "`group`", nullable = false)
	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@ManyToMany(targetEntity = Course.class, fetch = FetchType.LAZY)
	@JoinTable(name = "students_courses",
		joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id") },
		inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id") })
	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
}