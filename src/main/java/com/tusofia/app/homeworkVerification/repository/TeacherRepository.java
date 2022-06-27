package com.tusofia.app.homeworkVerification.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tusofia.app.homeworkVerification.domain.entities.Teacher;

@Transactional
public interface TeacherRepository extends UserBaseRepository<Teacher>  {
	long count();
	
}
