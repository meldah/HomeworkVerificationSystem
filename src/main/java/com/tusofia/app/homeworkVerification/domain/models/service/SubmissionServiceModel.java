package com.tusofia.app.homeworkVerification.domain.models.service;

import com.tusofia.app.homeworkVerification.domain.entities.File;
import com.tusofia.app.homeworkVerification.domain.entities.Student;

public class SubmissionServiceModel extends BaseServiceModel {

	private File file;
	
	private boolean isGraded;
	private double grade;
	private String comment;
	private String createdOn;
	
	private Student student;
	
	public SubmissionServiceModel() {
	}
	
	public boolean getIsGraded() {
		return isGraded;
	}

	public void setIsGraded(boolean isGraded) {
		this.isGraded = isGraded;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
}
