package com.tusofia.app.homeworkVerification.repository;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.tusofia.app.homeworkVerification.domain.entities.Student;

@Transactional
public interface StudentRepository extends UserBaseRepository<Student> {
	
	Optional<Student> findByFacultyNumber(String facultyNumber);
}
