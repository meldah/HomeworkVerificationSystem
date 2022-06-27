package com.tusofia.app.homeworkVerification.domain.models.view;

import com.tusofia.app.homeworkVerification.domain.entities.File;
import com.tusofia.app.homeworkVerification.domain.entities.Student;

public class SubmissionViewModel extends BaseViewModel {
	
	private File file;
	private boolean isGraded;
	private double grade;
	private String comment;
	private String createdOn;
	
	private Student student;
	
	public SubmissionViewModel() {
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean getIsGraded() {
		return isGraded;
	}

	public void setIsGraded(boolean isGraded) {
		this.isGraded = isGraded;
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

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}
