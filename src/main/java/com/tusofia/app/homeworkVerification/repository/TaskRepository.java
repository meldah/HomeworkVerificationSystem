package com.tusofia.app.homeworkVerification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tusofia.app.homeworkVerification.domain.entities.Submission;
import com.tusofia.app.homeworkVerification.domain.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>{

	Optional<Task> findByName(String name);

}
