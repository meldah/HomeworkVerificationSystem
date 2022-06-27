package com.tusofia.app.homeworkVerification.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.Exercise;
import com.tusofia.app.homeworkVerification.domain.entities.Task;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;
import com.tusofia.app.homeworkVerification.error.ExerciseNotFoundException;
import com.tusofia.app.homeworkVerification.repository.ExerciseRepository;
import com.tusofia.app.homeworkVerification.service.TaskService.TaskData;

@Service
public class ExerciseServiceImpl implements ExerciseService {
	
	private final ExerciseRepository exerciseRepository;
	private final TaskService taskService;
	private final ModelMapper modelMapper;
	
	public ExerciseServiceImpl(ExerciseRepository exerciseRepository,TaskService taskService, ModelMapper modelMapper) {
		this.exerciseRepository = exerciseRepository;
		this.taskService = taskService;
		this.modelMapper = modelMapper;
	}

	@Override
	public void seedVVPSExercisesInDataSource() {
		if(this.exerciseRepository.count() == 0) {
			addVVPSExercises();
		}
	}

	@Override
	public Set<ExerciseServiceModel> extractAllExercises() {
		return this.exerciseRepository.findAll()
				.stream()
				.map(e -> this.modelMapper.map(e, ExerciseServiceModel.class))
				.collect(Collectors.toSet());
	}
	
	private void addVVPSExercises(){
		for(int i = 0; i < VVPS_EXERCISE_NAMES.length; i++) {
			Exercise exercise = new Exercise();
			exercise.setName(VVPS_EXERCISE_NAMES[i]);
			
			//Add tasks for the first exercise
			if(i == 0) {
				this.taskService.seedVVPSTasksInDataSource();				
				List<Task> exercise1Tasks = new ArrayList<Task>();
				
				for(TaskData taskCondition : TaskService.VVPS_EXERCISE_1_TASKS) {
					TaskServiceModel taskServiceModel = 
							this.taskService.findTaskByName(taskCondition.getTaskName());
					
					Task task = this.modelMapper.map(taskServiceModel, Task.class);
					exercise1Tasks.add(task);
				}
				
				exercise.setTasks(exercise1Tasks);
			}
			
			this.exerciseRepository.saveAndFlush(exercise);
		}
	}

	@Override
	public ExerciseServiceModel getExerciseById(String id) {
		Exercise exercise = this.exerciseRepository.findById(id)
				.orElseThrow(() -> new ExerciseNotFoundException("Exercise was not found!"));
		return this.modelMapper.map(exercise, ExerciseServiceModel.class);
	}
	
	@Override
	public ExerciseServiceModel getExerciseByName(String exerciseName) {
		Exercise exercise = this.exerciseRepository.findByName(exerciseName)
				.orElseThrow(() -> new ExerciseNotFoundException("Exercise was not found!"));
		return this.modelMapper.map(exercise, ExerciseServiceModel.class);
	}

	@Override
	public void addExercise(String exerciseName) {
		Exercise exercise = new Exercise();
		exercise.setName(exerciseName);
		this.exerciseRepository.saveAndFlush(exercise);
	}

	@Override
	public void addTaskToExercise(ExerciseServiceModel exerciseServiceModel, TaskServiceModel taskServiceModel) {
		Exercise exercise = this.modelMapper.map(exerciseServiceModel, Exercise.class);
		Task task = this.modelMapper.map(taskServiceModel, Task.class);
		
		List<Task> exerciseTasks = exercise.getTasks();
		if(exerciseTasks == null) {
			exerciseTasks = new ArrayList<Task>();
		}
		exerciseTasks.add(task);
		exercise.setTasks(exerciseTasks);
		
		this.exerciseRepository.saveAndFlush(exercise);
		
	}
	
}
