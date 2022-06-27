package com.tusofia.app.homeworkVerification.domain.models.binding;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class TaskAddBindingModel {
	private String taskName;
	private MultipartFile taskCondition;
	private List<String> fileName;
	private List<Double> maxSize;
	private List<String> extension;
	private String courseId;
	private String exerciseId;
	
	public TaskAddBindingModel() {
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public MultipartFile getTaskCondition() {
		return taskCondition;
	}

	public void setTaskCondition(MultipartFile taskCondition) {
		this.taskCondition = taskCondition;
	}

	public List<String> getFileName() {
		return fileName;
	}

	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}

	public List<Double> getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(List<Double> maxSize) {
		this.maxSize = maxSize;
	}

	public List<String> getExtension() {
		return extension;
	}

	public void setExtension(List<String> extension) {
		this.extension = extension;
	}
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}
}
