package com.tusofia.app.homeworkVerification.domain.models.service;

public class RoleServiceModel extends BaseServiceModel {

	private String authority;

	protected RoleServiceModel() {
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
