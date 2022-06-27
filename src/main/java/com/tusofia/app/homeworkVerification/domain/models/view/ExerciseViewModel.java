package com.tusofia.app.homeworkVerification.domain.models.view;

import java.util.List;
import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Task;

public class ExerciseViewModel extends BaseViewModel {
	
	private String name;
	
	private List<Task> tasks;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
