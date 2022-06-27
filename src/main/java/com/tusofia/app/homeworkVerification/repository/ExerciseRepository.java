package com.tusofia.app.homeworkVerification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tusofia.app.homeworkVerification.domain.entities.Course;
import com.tusofia.app.homeworkVerification.domain.entities.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, String> {

	Optional<Exercise> findByName(String exerciseName);
}
