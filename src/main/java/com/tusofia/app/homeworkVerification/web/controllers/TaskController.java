package com.tusofia.app.homeworkVerification.web.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tusofia.app.homeworkVerification.domain.checkers.HomeworkChecker.SubmissionResult;
import com.tusofia.app.homeworkVerification.domain.entities.Course;
import com.tusofia.app.homeworkVerification.domain.entities.Exercise;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Task;
import com.tusofia.app.homeworkVerification.domain.models.binding.HomeworkSubmitBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.binding.TaskAddBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;
import com.tusofia.app.homeworkVerification.domain.utils.FileManager;
import com.tusofia.app.homeworkVerification.domain.utils.constants.XMLConstants;
import com.tusofia.app.homeworkVerification.service.CourseService;
import com.tusofia.app.homeworkVerification.service.ExerciseService;
import com.tusofia.app.homeworkVerification.service.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController extends BaseController {
	
	private final ModelMapper modelMapper;
	private final CourseService courseService;
	private final ExerciseService exerciseService;
	private final TaskService taskService;
	
	//TODO: make paths relative
	private static final String CONDITION_CHECKS_FILE_PATH = "C:\\Users\\Mel\\Downloads\\DiplomaAllFinal\\Diploma\\";
	private static final String CONDITION_CHECKS_FILE_NAME = CONDITION_CHECKS_FILE_PATH + "checks.xml";
	
	@Autowired
	public TaskController(ModelMapper modelMapper, CourseService courseService,
			ExerciseService exerciseService, TaskService taskService) {
		this.modelMapper = modelMapper;
		this.courseService = courseService;
		this.exerciseService = exerciseService;
		this.taskService = taskService;
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ModelAndView addTask(@ModelAttribute(name = "addTaskModel") TaskAddBindingModel taskData,
			RedirectAttributes redirectAttributes) {
		String newTaskId = "";
		try {
			File conditionFileAttachment = FileManager.createFileFrom(taskData.getTaskCondition());
			File conditionsXml = createConditionsXML(constructXMLFilePath(taskData.getTaskCondition().getOriginalFilename()),
					taskData.getFileName(), taskData.getMaxSize(), taskData.getExtension());
			
			CourseServiceModel course = this.courseService.getCourseById(taskData.getCourseId());
			
			ExerciseServiceModel exercise = this.exerciseService.getExerciseById(taskData.getExerciseId());
			
			this.taskService.addTask(course.getName(), exercise.getName(), taskData.getTaskName(),
					conditionFileAttachment, conditionsXml);
			
			TaskServiceModel task = this.taskService.getTaskByName(taskData.getTaskName());
			newTaskId = task.getId();
			
			this.exerciseService.addTaskToExercise(exercise, task);
			
			conditionsXml.delete();
			conditionFileAttachment.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return super.redirect("/exercise/evaluate/" + taskData.getCourseId() + "/" + taskData.getExerciseId()
				+ "/" + newTaskId);
	}
	
	private String constructXMLFilePath(String conditionsFileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(CONDITION_CHECKS_FILE_PATH);
		String fileNameNoExtension = conditionsFileName.substring(0, FileManager.getIndexOfExtensionSeparator(conditionsFileName));
		sb.append(fileNameNoExtension);
		sb.append(XMLConstants.XML_EXTENSION);
		
		return sb.toString();
	}

	private File createConditionsXML(String xmlFileName, List<String> fileNames, List<Double> maxSizes, List<String> extensions) throws IOException {
			Element condition = new Element(XMLConstants.CONDITIONS_TAG_NAME);
			Document doc = new Document();
			doc.setRootElement(condition);

			Element files = new Element(XMLConstants.FILES_TAG_NAME);
			doc.getRootElement().addContent(files);

			for(int i = 0; i < fileNames.size(); i++) {
				Element file = new Element(XMLConstants.FILE_TAG_NAME);
				file.addContent(new Element(XMLConstants.NAME_TAG_NAME).setText(fileNames.get(i)));
				file.addContent(new Element(XMLConstants.EXTENSION_TAG_NAME).setText(extensions.get(i)));
				file.addContent(new Element(XMLConstants.MAXSIZE_TAG_NAME).setText(maxSizes.get(i).toString()));
				files.addContent(file);
			}

			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(xmlFileName));

		File conditionChecksFile = new File(xmlFileName);
		return conditionChecksFile;
	}

	@PostMapping("/submit")
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	public ModelAndView submitHomework(
			@ModelAttribute(name = "submitHomeworkModel") HomeworkSubmitBindingModel homework, Principal principal, 
			RedirectAttributes redirectAttributes) {
		CourseServiceModel course = courseService.getCourseById(homework.getCourseId());
		ExerciseServiceModel exercise = exerciseService.getExerciseById(homework.getExerciseId());
		TaskServiceModel task = taskService.getTaskById(homework.getTaskId());
		Student student = (Student) ((Authentication) principal).getPrincipal();

		try {
			File conditionChecksFile = getFileFromURL(task.getConditionChecks().getUrl());
			
			SubmissionResult submissionResult = this.taskService.processHomeworkSubmission(homework.getFile(),
					conditionChecksFile,
					homework.getExtension(), course, exercise, task, student);
			if (submissionResult.isSuccessful) {
				redirectAttributes.addFlashAttribute("homeworkUploadSuccess", submissionResult.message);
			} else {
				redirectAttributes.addFlashAttribute("homeworkUploadFailure", submissionResult.message);
			}

			if (submissionResult.isSuccessful) {
				this.taskService.uploadTaskHomework(course.getName(), exercise.getName(), task, student, homework.getFile());
			}
			
			conditionChecksFile.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.redirect("/exercise/compete/" + homework.getCourseId() + '/' + homework.getExerciseId() + "/"
				+ homework.getTaskId());
	}
	
	private File getFileFromURL(String url) {
		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(CONDITION_CHECKS_FILE_NAME)) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		} catch (IOException e) {
		}
		File conditionChecksFile = new File(CONDITION_CHECKS_FILE_NAME);
		return conditionChecksFile;
	}
	
}
