package com.tusofia.app.homeworkVerification.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tusofia.app.homeworkVerification.domain.models.service.UserBaseServiceModel;

public interface UserService extends UserDetailsService{
	
	public String generateUsername(String firstName, String lastName);

	public void setUserEnabled(String id, boolean isEnabled);

	public void deleteUser(String id);

	public void assignUserRole(String id, String role);

	public void changeUserPassword(String email, String password);
}
