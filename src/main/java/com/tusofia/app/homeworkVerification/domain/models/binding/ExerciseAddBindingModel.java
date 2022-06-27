package com.tusofia.app.homeworkVerification.domain.models.binding;

public class ExerciseAddBindingModel {
	
	private String exerciseName;
	private String courseId;
	private String teacherId;
	
	public ExerciseAddBindingModel() {
	}

	public String getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
}
