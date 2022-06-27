package com.tusofia.app.homeworkVerification.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.Role;
import com.tusofia.app.homeworkVerification.domain.models.service.RoleServiceModel;
import com.tusofia.app.homeworkVerification.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void seedRolesInDataSource() {
		if(this.roleRepository.count() == 0) {
			this.roleRepository.saveAndFlush(new Role("ROLE_STUDENT"));
			this.roleRepository.saveAndFlush(new Role("ROLE_TEACHER"));
			this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
			this.roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
		}
	}

	@Override
	public Set<RoleServiceModel> extractAllRoles() {
		return this.roleRepository.findAll()
					.stream()
					.map(r -> this.modelMapper.map(r, RoleServiceModel.class))
					.collect(Collectors.toSet());
	}

	@Override
	public RoleServiceModel findByAuthority(String authority) {
		return modelMapper.map(this.roleRepository.findByAuthority(authority), RoleServiceModel.class);
	}
}
