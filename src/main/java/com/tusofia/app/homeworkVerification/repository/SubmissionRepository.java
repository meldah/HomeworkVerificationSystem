package com.tusofia.app.homeworkVerification.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tusofia.app.homeworkVerification.domain.entities.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
	@Query("SELECT s FROM Task t INNER JOIN t.submissions s WHERE s.student.id = :studentId AND t.id = :taskId")
	Optional<Submission> findByStudentIdAndTaskId(@Param("studentId") String studentId, @Param("taskId") String taskId);
	
	@Query("SELECT s FROM Task t INNER JOIN t.submissions s WHERE s.isGraded = false AND t.id = :taskId")
	Set<Submission> findNonEvaluatedByTaskId(@Param("taskId") String taskId);
}
