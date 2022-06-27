package com.tusofia.app.homeworkVerification.domain.models.service;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.entities.Role;

public class UserBaseServiceModel extends BaseServiceModel {
	
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private boolean isEnabled;
	
	private Set<Role> authorities;
	
	public UserBaseServiceModel() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<Role> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Role> authorities) {
		this.authorities = authorities;
	}
	
}
