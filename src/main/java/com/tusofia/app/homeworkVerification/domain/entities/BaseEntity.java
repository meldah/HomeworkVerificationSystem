package com.tusofia.app.homeworkVerification.domain.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class BaseEntity {
	
	private String id;

	protected BaseEntity() {
	}

	@Id
	@GeneratedValue(generator = "uuid-string")
	@GenericGenerator(
			name = "uuid-string",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
