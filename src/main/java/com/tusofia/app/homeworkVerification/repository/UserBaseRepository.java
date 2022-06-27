package com.tusofia.app.homeworkVerification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.tusofia.app.homeworkVerification.domain.entities.Teacher;
import com.tusofia.app.homeworkVerification.domain.entities.User;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<T, String>{
    
	long countByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    
    Optional<T> findByEmail(String email);
    
    Optional<T> findByUsername(String username);
    
    List<T> findAllByEnabled(boolean isEnabled);
}
