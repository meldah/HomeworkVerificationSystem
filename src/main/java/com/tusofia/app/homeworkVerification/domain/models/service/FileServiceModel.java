package com.tusofia.app.homeworkVerification.domain.models.service;

public class FileServiceModel extends BaseServiceModel {
	
	private String url;
	private String publicId;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
}
