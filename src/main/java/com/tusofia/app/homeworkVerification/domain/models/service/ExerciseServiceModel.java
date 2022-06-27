package com.tusofia.app.homeworkVerification.domain.models.service;

import java.util.List;
import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Task;

public class ExerciseServiceModel extends BaseServiceModel {

	private String name;

	private List<Task> tasks;

	public ExerciseServiceModel() {
	}

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
