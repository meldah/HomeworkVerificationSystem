package com.tusofia.app.homeworkVerification.domain.entities;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "`submissions`")
@Access(AccessType.PROPERTY)
public class Submission extends BaseEntity {

	private File file;
	
	private boolean isGraded;
	private double grade;
	private String comment;
	private String createdOn;
	
	private Student student;
	
	public Submission() {
	}
	
	@OneToOne(targetEntity = File.class, optional = false, cascade = CascadeType.REMOVE)
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	@Column(name = "`is_graded`", columnDefinition="bit default 0", nullable = false)
	public boolean getIsGraded() {
		return isGraded;
	}

	public void setIsGraded(boolean isGraded) {
		this.isGraded = isGraded;
	}

	@Column(name = "`grade`", nullable = true, unique = false)
	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	@Column(name = "`comment`", nullable = true, unique = false)
	@Type(type="text")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "`created_on`", nullable = false, unique = false)
	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	@OneToOne(targetEntity = Student.class, optional = false)
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}