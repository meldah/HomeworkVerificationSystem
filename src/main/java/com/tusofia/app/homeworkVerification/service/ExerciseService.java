package com.tusofia.app.homeworkVerification.service;

import java.util.Set;

import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;

public interface ExerciseService {
	
	//TODO: [muzunov] Extract names to external source
	static final String[] VVPS_EXERCISE_NAMES =
			new String[] { "Спазване на конвенции за писане на код. Експертизи на код",
							"Съставяне на план на тестване и тестови случаи. Функционално тестване." };
	
	void seedVVPSExercisesInDataSource();
	
	Set<ExerciseServiceModel> extractAllExercises();
	
	ExerciseServiceModel getExerciseById(String id);

	void addExercise(String exerciseName);

	ExerciseServiceModel getExerciseByName(String exerciseName);
	
	void addTaskToExercise(ExerciseServiceModel exercise, TaskServiceModel task);
}
