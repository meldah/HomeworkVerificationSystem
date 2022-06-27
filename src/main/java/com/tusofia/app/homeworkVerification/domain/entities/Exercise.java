package com.tusofia.app.homeworkVerification.domain.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "`exercises`")
@Access(AccessType.PROPERTY)
public class Exercise extends BaseEntity {

	private String name;
	
	private List<Task> tasks;

	public Exercise() {
	}

	@Column(name = "`name`", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(targetEntity = Task.class, orphanRemoval = true)
	@JoinColumn(name = "exercise_id", referencedColumnName = "id")
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
