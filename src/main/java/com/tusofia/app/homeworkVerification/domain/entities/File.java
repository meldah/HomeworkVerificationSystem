package com.tusofia.app.homeworkVerification.domain.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "`files`")
@Access(AccessType.PROPERTY)
public class File extends BaseEntity {
	private String url;
	
	private String publicId;

	public File() {
	}

	@Column(name = "`url`", unique = true,  nullable = false)
	@Type(type="text")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "public_Id", unique = true, nullable = false)
	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
}
