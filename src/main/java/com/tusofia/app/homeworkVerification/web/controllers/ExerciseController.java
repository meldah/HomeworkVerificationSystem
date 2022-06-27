package com.tusofia.app.homeworkVerification.web.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tusofia.app.homeworkVerification.domain.entities.Exercise;
import com.tusofia.app.homeworkVerification.domain.entities.Role;
import com.tusofia.app.homeworkVerification.domain.entities.Student;
import com.tusofia.app.homeworkVerification.domain.entities.Submission;
import com.tusofia.app.homeworkVerification.domain.entities.Task;
import com.tusofia.app.homeworkVerification.domain.entities.User;
import com.tusofia.app.homeworkVerification.domain.models.binding.CourseAddBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.binding.ExerciseAddBindingModel;
import com.tusofia.app.homeworkVerification.domain.models.service.CourseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.ExerciseServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.SubmissionServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.service.TaskServiceModel;
import com.tusofia.app.homeworkVerification.domain.models.view.CourseViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.ExerciseViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.SubmissionViewModel;
import com.tusofia.app.homeworkVerification.domain.models.view.TaskViewModel;
import com.tusofia.app.homeworkVerification.service.CourseService;
import com.tusofia.app.homeworkVerification.service.ExerciseService;
import com.tusofia.app.homeworkVerification.service.RoleService;
import com.tusofia.app.homeworkVerification.service.SubmissionService;
import com.tusofia.app.homeworkVerification.web.annotations.PageTitle;

@Controller
@RequestMapping("/exercise")
public class ExerciseController extends BaseController {
	
	private final CourseService courseService;
	private final RoleService roleService;
	private final ExerciseService exerciseService;
	private final SubmissionService submissionService;
	private final ModelMapper modelMapper;

	@Autowired
	public ExerciseController(CourseService courseService, RoleService roleService,
			ExerciseService exerciseService,
			SubmissionService submissionService, ModelMapper modelMapper) {
		this.courseService = courseService;
		this.roleService = roleService;
		this.exerciseService = exerciseService;
		this.submissionService = submissionService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ModelAndView addCourse(@ModelAttribute(name = "addExerciseModel") ExerciseAddBindingModel exerciseData, 
			RedirectAttributes redirectAttributes){
		
		//Add the exercise
		this.exerciseService.addExercise(exerciseData.getExerciseName());
		
		Exercise exercise = this.modelMapper.map
				( this.exerciseService.getExerciseByName(exerciseData.getExerciseName()),
				Exercise.class);
		
		//Add the course
		this.courseService.addExerciseToCourse(exerciseData.getCourseId(), exercise);
		
		return super.redirect("/course/viewCourse/" + exerciseData.getCourseId());
	}
	
	@GetMapping("/compete/{courseId}/{exerciseId}")
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@PageTitle("Compete")
	public ModelAndView competeInExercise(@PathVariable("courseId") String courseId,
			@PathVariable("exerciseId") String exerciseId, ModelAndView modelAndView, Principal principal) {
		addCourseToView(courseId, modelAndView);
		
		addExerciseAndTaskToView(exerciseId, modelAndView, principal);

		return super.view("/exercises/compete", modelAndView);
	}

	@GetMapping("/evaluate/{courseId}/{exerciseId}")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PageTitle("Evaluate")
	public ModelAndView evaluateExercise(@PathVariable("courseId") String courseId,
			@PathVariable("exerciseId") String exerciseId, ModelAndView modelAndView, Principal principal) {
		addCourseToView(courseId, modelAndView);
		
		addExerciseAndTaskToView(exerciseId, modelAndView, principal);

		return super.view("/exercises/evaluateExerciseHomeworks", modelAndView);
	}

	@GetMapping("/compete/{courseId}/{exerciseId}/{taskId}")
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@PageTitle("Compete")
	public ModelAndView competeInExerciseWithGivenTask(@PathVariable("courseId") String courseId,
			@PathVariable("exerciseId") String exerciseId, @PathVariable("taskId") String taskId,
			ModelAndView modelAndView, Principal principal) {
		addCourseToView(courseId, modelAndView);

		addExerciseTaskAndSubmissionsToView(exerciseId, taskId, modelAndView, principal);
		return super.view("/exercises/compete", modelAndView);
	}

	private void addExerciseTaskAndSubmissionsToView(String exerciseId, String taskId, ModelAndView modelAndView,
			Principal principal) {
		ExerciseViewModel exercise = addExerciseToView(exerciseId, modelAndView);

		if ((exercise.getTasks() != null) && (exercise.getTasks().size() > 0)) {
			for(Task task : exercise.getTasks()) {
				if(task.getId().equals(taskId)) {
					TaskViewModel activeTask = this.modelMapper.map(task, TaskViewModel.class);
					addTaskToView(modelAndView, activeTask);
					
					User user = (User)((Authentication) principal).getPrincipal();
					Role roleStudent = this.modelMapper
							.map(this.roleService.findByAuthority("ROLE_STUDENT"), Role.class);
					Role roleTeacher = this.modelMapper
							.map(this.roleService.findByAuthority("ROLE_TEACHER"), Role.class);
					
					if((user.getAuthorities().contains(roleStudent))) {
						addStudentSubmissionToView(modelAndView, principal, activeTask);
					}
					else if((user.getAuthorities().contains(roleTeacher))){
						addTaskUnevaluatedSubmissionsToView(modelAndView, activeTask);
					}
				}
			}
		}
	}
	
	@GetMapping("/evaluate/{courseId}/{exerciseId}/{taskId}")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PageTitle("Compete")
	public ModelAndView evaluateExerciseWithGivenTask(@PathVariable("courseId") String courseId,
			@PathVariable("exerciseId") String exerciseId, @PathVariable("taskId") String taskId,
			ModelAndView modelAndView, Principal principal) {
		addCourseToView(courseId, modelAndView);
		addExerciseTaskAndSubmissionsToView(exerciseId, taskId, modelAndView, principal);
		
		return super.view("/exercises/evaluateExerciseHomeworks", modelAndView);
	}
	
	private void addExerciseAndTaskToView(String exerciseId, ModelAndView modelAndView, Principal principal) {
		ExerciseViewModel exercise = addExerciseToView(exerciseId, modelAndView);

		if ((exercise.getTasks() != null) && (exercise.getTasks().size() > 0)) {
			TaskViewModel activeTask = this.modelMapper.map(exercise.getTasks().get(0), TaskViewModel.class);
			addTaskToView(modelAndView, activeTask);
			
			User user = (User)((Authentication) principal).getPrincipal();
			Role roleStudent = this.modelMapper
					.map(this.roleService.findByAuthority("ROLE_STUDENT"), Role.class);
			Role roleTeacher = this.modelMapper
					.map(this.roleService.findByAuthority("ROLE_TEACHER"), Role.class);
			
			if((user.getAuthorities().contains(roleStudent))) {
				addStudentSubmissionToView(modelAndView, principal, activeTask);
			}
			else if((user.getAuthorities().contains(roleTeacher))){
				addTaskUnevaluatedSubmissionsToView(modelAndView, activeTask);
			}
		}
	}

	private void addTaskUnevaluatedSubmissionsToView(ModelAndView modelAndView, TaskViewModel activeTask) {
		 
		Set<SubmissionViewModel> submissionViewModels = 
				this.submissionService.findNonEvaluatedSubmissionsByTaskId(activeTask.getId())
				.stream()
				.map(c -> this.modelMapper.map(c, SubmissionViewModel.class))
				.collect(Collectors.toSet());
		 
		 modelAndView.addObject("submissions", submissionViewModels);
	}

	private void addStudentSubmissionToView(ModelAndView modelAndView, Principal principal, TaskViewModel activeTask) {
		Student student = (Student) ((Authentication) principal).getPrincipal();
		SubmissionServiceModel submissionServiceModel = this.submissionService
				.findByStudentIdAndTaskId(student.getId(), activeTask.getId());
		
		if(submissionServiceModel != null) {
			SubmissionViewModel submissionViewModel = this.modelMapper.map(submissionServiceModel,
					SubmissionViewModel.class);
			modelAndView.addObject("submission", submissionViewModel);
		}
	}
	
	private void addTaskToView(ModelAndView modelAndView, TaskViewModel activeTask) {
		modelAndView.addObject("activeTask", activeTask);
	}

	private void addCourseToView(String courseId, ModelAndView modelAndView) {
		CourseServiceModel courseServiceModel = this.courseService.getCourseById(courseId);
		CourseViewModel course = this.modelMapper.map(courseServiceModel, CourseViewModel.class);
		modelAndView.addObject("course", course);
	}
	
	private ExerciseViewModel addExerciseToView(String exerciseId, ModelAndView modelAndView) {
		ExerciseServiceModel exerciseServiceModel = 
				this.exerciseService.getExerciseById(exerciseId);
		
		ExerciseViewModel exercise = this.modelMapper.map(exerciseServiceModel, ExerciseViewModel.class);
		modelAndView.addObject("exercise", exercise);
		return exercise;
	}
}