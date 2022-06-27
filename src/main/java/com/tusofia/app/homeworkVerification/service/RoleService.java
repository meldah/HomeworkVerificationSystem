package com.tusofia.app.homeworkVerification.service;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.models.service.RoleServiceModel;


public interface RoleService {

	void seedRolesInDataSource();
	
	Set<RoleServiceModel> extractAllRoles();
	
	RoleServiceModel findByAuthority(String authority);
}
