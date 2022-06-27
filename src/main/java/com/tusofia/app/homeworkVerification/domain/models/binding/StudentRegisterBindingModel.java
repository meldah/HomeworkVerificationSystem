package com.tusofia.app.homeworkVerification.domain.models.binding;

public class StudentRegisterBindingModel extends UserRegisterBindingModel{
	private String faculty;
	private String facultyNumber;
	private int semester;
	private int stream;
	private int group;
	
	public StudentRegisterBindingModel() {
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

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
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
	
}