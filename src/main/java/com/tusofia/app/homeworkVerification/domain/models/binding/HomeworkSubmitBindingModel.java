package com.tusofia.app.homeworkVerification.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

public class HomeworkSubmitBindingModel {
	
	private MultipartFile file;
	private String extension;
	private String courseId;
	private String exerciseId;
	private String taskId;
	
	public HomeworkSubmitBindingModel() {
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}