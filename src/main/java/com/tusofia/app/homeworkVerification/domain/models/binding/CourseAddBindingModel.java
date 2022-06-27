package com.tusofia.app.homeworkVerification.domain.models.binding;

public class CourseAddBindingModel {
	
	private String courseName;
	private String courseDescription;
	private String teacherId;
	
	public CourseAddBindingModel() {
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
}