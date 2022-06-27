package com.tusofia.app.homeworkVerification.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tusofia.app.homeworkVerification.domain.checkers.HomeworkChecker;
import com.tusofia.app.homeworkVerification.domain.checkers.HomeworkChecker.SubmissionResult;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Submission;
import com.tusofia.app.homeworkVerification.domain.entities.Task;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.SubmissionServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;
import com.tusofia.app.homeworkVerification.error.TaskNotFoundException;
import com.tusofia.app.homeworkVerification.repository.TaskRepository;
import com.tusofia.app.homeworkVerification.service.storage.CloudService;
import com.tusofia.app.homeworkVerification.service.storage.CloudService.FileCloudData;

@Service
public class TaskServiceImpl implements TaskService {
	
	private final TaskRepository taskRepository;
	private final CloudService cloudService;
	private final FileService fileService;
	private final SubmissionService submissionService;
	private final HomeworkChecker checker;
	private final ModelMapper modelMapper;
	
	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, CloudService cloudService, FileService fileService,
			SubmissionService submissionService, HomeworkChecker checker, ModelMapper modelMapper) {
		this.taskRepository = taskRepository;
		this.cloudService = cloudService;
		this.fileService = fileService;
		this.submissionService = submissionService;
		this.checker = checker;
		this.modelMapper = modelMapper;
	}

	@Override
	public void seedVVPSTasksInDataSource() {
		if (this.taskRepository.count() == 0) {
			for (TaskData taskData : VVPS_EXERCISE_1_TASKS) {
				try {
					// Upload task condition files to cloud and save links to them in db
					java.io.File tempFileCondition = new java.io.File(taskData.getTaskConditionFile());
					java.io.File tempFileChecks = new java.io.File(taskData.getTaskConditionChecksFile());
					
					addTask(CourseService.VVPS_NAME, ExerciseService.VVPS_EXERCISE_NAMES[0], taskData.getTaskName(), 
							tempFileCondition, tempFileChecks);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void addTask(String courseName, String exerciseName, String taskName, File condition, File conditionChecks) throws IOException {
		FileCloudData cloudDataCondition = this.cloudService.uploadFile(courseName, exerciseName, taskName, condition);
		this.fileService.addFileByCloudData(cloudDataCondition);
		
		FileCloudData cloudDataChecks = this.cloudService.uploadFile(courseName, exerciseName, taskName, conditionChecks);
		this.fileService.addFileByCloudData(cloudDataChecks);
		
		this.addTaskByCloudData(taskName, cloudDataCondition, cloudDataChecks);

	}

	private void addTaskByCloudData(String taskName, FileCloudData conditonCloudData, FileCloudData conditonChecksCloudData) {
		com.tusofia.app.homeworkVerification.domain.entities.File condition = this.modelMapper
				.map(this.fileService.getFileByUrl(conditonCloudData.getUrl()), 
						com.tusofia.app.homeworkVerification.domain.entities.File.class);
		
		com.tusofia.app.homeworkVerification.domain.entities.File conditionChecks = this.modelMapper
				.map(this.fileService.getFileByUrl(conditonChecksCloudData.getUrl()), 
						com.tusofia.app.homeworkVerification.domain.entities.File.class);
		
		//Setup task data
		Task task = new Task();
		task.setName(taskName);
		task.setCondition(condition);
		task.setConditionChecks(conditionChecks);
		
		//Save task to db
		this.taskRepository.saveAndFlush(task);
	}

	@Override
	public TaskServiceModel findTaskByName(String name) {
		Task task = this.taskRepository.findByName(name)
				.orElseThrow(() -> new TaskNotFoundException("Task with name: " + name + " was not found!"));
		return this.modelMapper.map(task, TaskServiceModel.class);
	}

	@Override
	public TaskServiceModel getTaskById(String id) {
		Task task = this.taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException("Task was not found!"));
		TaskServiceModel taskServiceModel = this.modelMapper.map(task, TaskServiceModel.class);
		return taskServiceModel;
	}

	@Override
	public SubmissionResult processHomeworkSubmission(MultipartFile submission, File contentChecksFile, String fileExtension, CourseServiceModel course,
			ExerciseServiceModel exercise, TaskServiceModel task, Student student) throws IOException {
		SubmissionResult submissionResult = null;
		submissionResult = checker.checkSubmission(submission, contentChecksFile, fileExtension, student);

		return submissionResult;
	}

	@Override
	public void uploadTaskHomework(String courseName, String exerciseName, TaskServiceModel taskServiceModel,
			Student student, MultipartFile file) throws IOException {
		Task task = this.taskRepository.findById(taskServiceModel.getId())
				.orElseThrow(()-> new TaskNotFoundException("Task was not found!"));
		Set<Submission> submissions = task.getSubmissions();
		if(submissions == null) {
			submissions = new LinkedHashSet<Submission>();
		}
		
		SubmissionServiceModel submission = this.submissionService.findByStudentIdAndTaskId(student.getId(),
				taskServiceModel.getId());
		if (submission != null) {
			// There is already a submission from this student
			this.submissionService.deleteSubmission(submission);
			submissions.removeIf(submissionIter -> submissionIter.getId().equals(submission.getId()));
		}
		
		Submission newSubmission = this.submissionService.addSubmission(courseName, exerciseName, taskServiceModel.getName(), student, file);
		submissions.add(newSubmission);
		task.setSubmissions(task.getSubmissions());
		
		this.taskRepository.saveAndFlush(task);
	}

	@Override
	public TaskServiceModel getTaskByName(String taskName) {
		Task task = this.taskRepository.findByName(taskName)
				.orElseThrow(() -> new TaskNotFoundException("Task was not found!"));
		TaskServiceModel taskServiceModel = this.modelMapper.map(task, TaskServiceModel.class);
		return taskServiceModel;
	}
}