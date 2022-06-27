package com.tusofia.app.homeworkVerification.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import com.tusofia.app.homeworkVerification.domain.checkers.HomeworkChecker.SubmissionResult;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.StudentServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;
import com.tusofia.app.homeworkVerification.service.storage.CloudService.FileCloudData;

public interface TaskService {
	
	//Internal task data storage class
	public class TaskData{
		private String taskName;
		private String taskConditionFile;
		private String taskConditionChecksFile;
		
		public TaskData(String taskName, String taskConditionFile, String taskConditionChecksFile) {
			this.taskName = taskName;
			this.taskConditionFile = taskConditionFile;
			this.taskConditionChecksFile = taskConditionChecksFile;
		}

		public String getTaskName() {
			return taskName;
		}

		public String getTaskConditionFile() {
			return taskConditionFile;
		}

		public String getTaskConditionChecksFile() {
			return taskConditionChecksFile;
		}
	}
	
	//TODO: [muzunov]: Move to external source, not hard-coded
	//Task name - Task condition file
	static final List<TaskData> VVPS_EXERCISE_1_TASKS =
			new ArrayList<TaskData>(Arrays.asList
					(new TaskData("Спазване на конвенции за писане на код", 
							"C:\\Users\\Mel\\Downloads\\DiplomaAllFinal\\Diploma\\homeworkVerification\\src\\main\\resources\\static\\files\\VVPS_Exercise1_Task1.docx",
							"C:\\Users\\Mel\\Downloads\\DiplomaAllFinal\\Diploma\\homeworkVerification\\src\\main\\resources\\static\\files\\VVPS_Exercise1_Task1_Checks.xml"),
					new TaskData("Експертизи на код", 
							"C:\\Users\\Mel\\Downloads\\DiplomaAllFinal\\Diploma\\homeworkVerification\\src\\main\\resources\\static\\files\\VVPS_Exercise1_Task2.docx",
							"C:\\Users\\Mel\\Downloads\\DiplomaAllFinal\\Diploma\\homeworkVerification\\src\\main\\resources\\static\\files\\VVPS_Exercise1_Task2_Checks.xml")));
	
	void seedVVPSTasksInDataSource();
	
	void addTask(String courseName, String exerciseName, String taskName, File conditon, File conditonChecks)  throws IOException ;
	
	TaskServiceModel getTaskById(String id);
	
	TaskServiceModel findTaskByName(String name);

	SubmissionResult processHomeworkSubmission(MultipartFile submission, File contentChecksFile,
			String fileExtension, CourseServiceModel course,
			ExerciseServiceModel exercise, TaskServiceModel task, Student student) throws IOException;

	void uploadTaskHomework(String courseName, String exerciseName, TaskServiceModel taskServiceModel, Student student, MultipartFile file) throws IOException;

	TaskServiceModel getTaskByName(String taskName);
}
