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

@Entity
@Table(name = "`tasks`")
@Access(AccessType.PROPERTY)
public class Task extends BaseEntity{
	
	private String name;
	private File condition;
	private File conditionChecks;
	
	private Set<Submission> submissions;

	public Task() {
	}

	@Column(name = "`name`", nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToOne(targetEntity = File.class, optional = false)
	public File getCondition() {
		return condition;
	}

	public void setCondition(File condition) {
		this.condition = condition;
	}
	
	@OneToOne(targetEntity = File.class, optional = false)
	public File getConditionChecks() {
		return conditionChecks;
	}

	public void setConditionChecks(File conditionChecks) {
		this.conditionChecks = conditionChecks;
	}

	@OneToMany(targetEntity = Submission.class, orphanRemoval = true, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	public Set<Submission> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(Set<Submission> submissions) {
		this.submissions = submissions;
	}
}