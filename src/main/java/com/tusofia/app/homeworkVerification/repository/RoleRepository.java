package com.tusofia.app.homeworkVerification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tusofia.app.homeworkVerification.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	Role findByAuthority(String authority);
}
