package com.tusofia.app.homeworkVerification.domain.models.service;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.File;
import com.tusofia.app.homeworkVerification.domain.entities.Submission;

public class TaskServiceModel extends BaseServiceModel {
	
	private String name;
	private File condition;
	private File conditionChecks;
	
	private Set<Submission> submissions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getCondition() {
		return condition;
	}

	public void setCondition(File condition) {
		this.condition = condition;
	}
	
	public File getConditionChecks() {
		return conditionChecks;
	}

	public void setConditionChecks(File conditionChecks) {
		this.conditionChecks = conditionChecks;
	}

	public Set<Submission> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(Set<Submission> submissions) {
		this.submissions = submissions;
	}
	
}
