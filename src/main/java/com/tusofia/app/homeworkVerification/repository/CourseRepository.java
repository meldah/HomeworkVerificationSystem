package com.tusofia.app.homeworkVerification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tusofia.app.homeworkVerification.domain.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
	
	@Query("SELECT c FROM Student s INNER JOIN s.courses c WHERE s.id = :id")
	List<Course> findAllByStudentId(@Param("id") String id);
	
	@Query("SELECT c FROM Teacher t INNER JOIN t.courses c WHERE t.id = :id")
	List<Course> findAllByTeacherId(@Param("id") String id);
	
	Optional<Course> findByName(String name);
}
